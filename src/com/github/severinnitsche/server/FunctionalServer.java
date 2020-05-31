package com.github.severinnitsche.server;

//utility

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

//tangent
//Exceptions

/**
 * @author Severin Leonard Christian Nitsche
 */

public class FunctionalServer {
  
  private ServerSocket slave;
  private PrintWriter sout;
  private Thread listener;
  private volatile boolean run;
  
  protected volatile HashMap<String, HashMap<String, HashMap<String, HashMap<String, BiConsumer<SimpleClient, String[]>>>>> handler;
  protected volatile ArrayList<Thread> requestHandler;
  
  public FunctionalServer(String conf) throws IOException {
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy,MM,dd@HH:mm");
    //config 'n stuff
    BufferedReader reader = new BufferedReader(new FileReader(new File(conf)));
    while (reader.ready()) {
      String line = reader.readLine();
      if (line.startsWith("port")) slave = new ServerSocket(Integer.parseInt(line.split("\\D+")[1]));
      else if (line.startsWith("log"))
        sout = new PrintWriter(line.substring("log".length()).trim() + "log" + dateFormat.format(date) + ".log");
    }
    //convenience stdout
    System.setOut(new PrintStream(System.out) {
      @Override public void print(String s) {
        super.print(s);
        sout.print(s);
      }
      @Override public void println(String s) {
        super.println(s);
        sout.println(s);
      }
    });
    System.setErr(new PrintStream(System.err) {
      @Override public void print(String s) {
        super.print(s);
        sout.print("Err: "+s);
      }
      @Override public void println(String s) {
        super.println(s);
        sout.println("Err: "+s);
      }
    });
    //Setup Map & List
    handler = new HashMap<>();
    requestHandler = new ArrayList<>();
    //start Server
    System.out.println("Started Session @" + new Date());
    listener = new Thread(this::accept);
    run = true;
    listener.start();
  }
  
  public FunctionalServer() throws IOException {
    this("./FSconf/FServer.conf");
  }
  
  private void accept() {
    while (run) {
      Socket client = null;
      try {
        client = slave.accept();
        final Socket threadClient = client;
        Thread distributor = new Thread(() -> {
          try {
            InputStream in = threadClient.getInputStream();
            StringBuilder builder = new StringBuilder();
            for (int i = in.read(); i != '\n'; i = in.read()) {
              builder.append((char) i);
            }
            String line = builder.toString().toLowerCase();
            String[] info = line.split("[\\s]+");
            String method = info[0];
            String address = info[1];
            info = info[2].split("/");
            String protocol = info[0];
            String version = info[1];
            handle(method, address, protocol, version, threadClient);
          } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            String log = "Invalid request from " + threadClient;
            System.out.println(log);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
        Thread cleaner = new Thread(() -> {
          long timeout = 2_000;
          distributor.start();
          try {
            distributor.join(timeout);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          if (distributor.getState() == Thread.State.TERMINATED)
            return;
          System.out.println("Malicious delaying detected");
          try {
            threadClient.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
        cleaner.start();
      } catch (SocketException e) {
        assert !run : "Unexpected SocketException\n";
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  public void log(String msg) {
    System.out.println(msg);
  }
  
  protected void logRequest(String method, String address, String protocol, String version, Socket client) {
    String log = "Got " + protocol + " version " + version + " " + method + " request to " + address + " from " + client;
    System.out.println(log);
  }
  
  private String first(String[] strings, String delimiter, int offset) {
    StringBuilder builder = new StringBuilder();
    for(int i = 1; i < offset; i++) builder.append(delimiter).append(strings[i]);
    return builder.toString();
  }
  
  private String[] last(String[] strings, String delimiter, int offset) {
    String[] val = new String[strings.length-offset];
    if (strings.length - offset >= 0) System.arraycopy(strings, offset, val, offset - offset, strings.length - offset);
    //for(int i = offset; i < strings.length; i++) val[i-offset] = strings[i];
    return val;
  }
  
  protected void handle(String method, String address, String protocol, String version, Socket client) {
    logRequest(method, address, protocol, version, client);
    Thread clerk = new Thread(() -> {
      String[] split = address.split("/");
      for(int i = split.length; i >= 0; i--) {
        String adr = first(split,"/",i);
        try {
          handler.get(method).get(adr).get(protocol).get(version).accept(new SimpleClient(client),last(split,"/",i));
          break;
        } catch (NullPointerException e) {
          String log = "No request handler registered for " + protocol + " " + version + " " + method + " request to " + adr + " from " + client;
          System.out.println(log);
        }
      }
      try {
        client.close();
      } catch (IOException f) {
        f.printStackTrace();
      }
      requestHandler.remove(this);
    });
    requestHandler.add(clerk);
    clerk.start();
  }
  
  
  //Register Request Handler
  public void register(String method, String address, String protocol, String version, BiConsumer<SimpleClient, String[]> handler) {
    if (this.handler.get(method) == null) {
      HashMap<String, BiConsumer<SimpleClient, String[]>> vMap = new HashMap<>();
      vMap.put(version, handler);
      HashMap<String, HashMap<String, BiConsumer<SimpleClient, String[]>>> pMap = new HashMap<>();
      pMap.put(protocol, vMap);
      HashMap<String, HashMap<String, HashMap<String, BiConsumer<SimpleClient, String[]>>>> aMap = new HashMap<>();
      aMap.put(address, pMap);
      this.handler.put(method, aMap);
    } else if (this.handler.get(method).get(address) == null) {
      HashMap<String, BiConsumer<SimpleClient, String[]>> vMap = new HashMap<>();
      vMap.put(version, handler);
      HashMap<String, HashMap<String, BiConsumer<SimpleClient, String[]>>> pMap = new HashMap<>();
      pMap.put(protocol, vMap);
      this.handler.get(method).put(address, pMap);
    } else if (this.handler.get(method).get(address).get(protocol) == null) {
      HashMap<String, BiConsumer<SimpleClient, String[]>> vMap = new HashMap<>();
      vMap.put(version, handler);
      this.handler.get(method).get(address).put(protocol, vMap);
    } else {
      this.handler.get(method).get(address).get(protocol).put(version, handler);
    }
  }
  
  public void register(String method, String address, String protocol, String version, Consumer<SimpleClient> handler) {
    register(method, address, protocol, version, (socket, strings) -> handler.accept(socket));
  }
  
  public void shutdown() throws IOException {
    run = false;
    slave.close();
    System.out.println("Closed Session @" + new Date());
    sout.flush();
  }
  
}
