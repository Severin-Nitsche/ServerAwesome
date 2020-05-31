package com.github.severinnitsche.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleClient {
  
  protected Socket socket;
  
  private String statusResponseCode = HTTPStatusCode.ClientErrorResponses.NOT_FOUND.getMessage();
  private StringBuilder header;
  private String body = "<html><head><title>404 Not Found</title></head><body><h1>404 Not Found</h1></body></html>";
  
  public SimpleClient(Socket client) {
    this.socket = client;
    this.header = new StringBuilder();
  }
  
  public SimpleClient status(HTTPStatusCode code) {
    statusResponseCode = code.getMessage();
    return this;
  }
  
  public SimpleClient status(int code) {
    return status(HTTPStatusCode.get(code));
  }
  
  public SimpleClient header(String header, String value) {
    this.header.append(header).append(": ").append(value).append('\n');
    return this;
  }
  
  public SimpleClient body(String body) {
    this.body = body;
    return this;
  }
  
  public void close() throws IOException {
    PrintWriter writer = new PrintWriter(socket.getOutputStream());
    writer.println(statusResponseCode);
    writer.println(header.toString());
    writer.println(body);
    writer.flush();
    writer.close();
  }
  
  public InputStream getInputStream() throws IOException {
    return socket.getInputStream();
  }
  
  @Override
  public String toString() {
    return socket.toString();
  }
}
