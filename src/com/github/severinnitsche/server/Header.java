package com.github.severinnitsche.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Header {

  public static Map<String, String> getHeader(SimpleClient client) throws IOException {
    return getHeader(client.socket.getInputStream());
  }
  
  public static Map<String, String> getHeader(InputStream stream) throws IOException {
    Map<String,String> map = new HashMap<>();
    String line;
    while(!(line = getLine(stream)).equals("")) {
      String[] keyVal = line.split(":");
      if(keyVal.length == 2) map.put(keyVal[0].toLowerCase().trim(),keyVal[1].toLowerCase().trim());
    }
    return map;
  }
  
  private static String getLine(InputStream source) throws IOException {
    StringBuilder builder = new StringBuilder();
    char c;
    while (source.available() > 0 && (c = (char)source.read()) != 10) {
      builder.append(c);
    }
    return builder.toString();
  }
  
}
