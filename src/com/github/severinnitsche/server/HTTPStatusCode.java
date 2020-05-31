package com.github.severinnitsche.server;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public interface HTTPStatusCode {
  
  String VERSION = "HTTP/1.1";
  
  enum InformationResponses implements HTTPStatusCode {
    CONTINUE(100), SWITCHING_PROTOCOL(101);
    
    public final int CODE;
    
    InformationResponses(int code) {
      CODE = code;
    }
    
    public static HTTPStatusCode get(int i) {
      return Arrays.stream(InformationResponses.values()).filter(ir -> ir.CODE - 100 - i == 0).findFirst().orElse(null);
    }
    
    @Override
    public String getMessage() {
      switch (this) {
        case CONTINUE:
          return VERSION + " 100 Continue";
        case SWITCHING_PROTOCOL:
          return VERSION + " 101 Switching Protocols";
      }
      ;
      throw new AssertionError("May I ask you to inform your code supplier to include all possible branches into the switch protocol.");
    }
  }
  
  enum SuccessfulResponses implements HTTPStatusCode {
    OK(200), CREATED(201), ACCEPTED(202), NON_AUTHORITATIVE_INFORMATION(203), NO_CONTENT(204), RESET_CONTENT(205), PARTIAL_CONTENT(206);
    
    public final int CODE;
    
    SuccessfulResponses(int code) {
      CODE = code;
    }
    
    public static HTTPStatusCode get(int i) {
      return Arrays.stream(SuccessfulResponses.values()).filter(ir -> ir.CODE - 200 - i == 0).findFirst().orElse(null);
    }
    
    @Override
    public String getMessage() {
      switch (this) {
        case OK:
          return VERSION + " 200 OK";
        case CREATED:
          return VERSION + " 201 Created";
        case ACCEPTED:
          return VERSION + " 202 Accepted";
        case NON_AUTHORITATIVE_INFORMATION:
          return VERSION + " 203 Non-Authoritative Information";
        case NO_CONTENT:
          return VERSION + " 204 No Content";
        case RESET_CONTENT:
          return VERSION + " 205 Reset Content";
        case PARTIAL_CONTENT:
          return VERSION + " 206 Partial Content";
      }
      throw new AssertionError("Add new branches");
    }
  }
  
  enum RedirectionMessages implements HTTPStatusCode {
    MULTIPLE_CHOICE(300), MOVED_PERMANENTLY(301), FOUND(302), SEE_OTHER(303), NOT_MODIFIED(304), USE_PROXY(305), UNUSED(306), TEMPORARY_REDIRECT(307), PERMANENT_REDIRECT(308);
    
    public final int CODE;
    
    RedirectionMessages(int code) {
      CODE = code;
    }
    
    public static HTTPStatusCode get(int i) {
      return Arrays.stream(RedirectionMessages.values()).filter(ir -> ir.CODE - 300 - i == 0).findFirst().orElse(null);
    }
    
    @Override
    public String getMessage() {
      switch (this) {
        case MULTIPLE_CHOICE:
          return VERSION + " 300 Multiple Choice";
        case MOVED_PERMANENTLY:
          return VERSION + " 301 Moved Permanently";
        case FOUND:
          return VERSION + " 302 Found";
        case SEE_OTHER:
          return VERSION + " 303 See Other";
        case NOT_MODIFIED:
          return VERSION + " 304 Not Modified";
        case USE_PROXY:
          return VERSION + " 305 Use Proxy";
        case UNUSED:
          return VERSION + " 306 unused";
        case TEMPORARY_REDIRECT:
          return VERSION + " 307 Temporary Redirect";
        case PERMANENT_REDIRECT:
          return VERSION + " 308 Permanent Redirect";
      }
      throw new AssertionError("Add new branches");
    }
  }
  
  enum ClientErrorResponses implements HTTPStatusCode {
    BAD_REQUEST(400), UNAUTHORIZED(401), PAYMENT_REQUIRED(402), FORBIDDEN(403), NOT_FOUND(404), METHOD_NOT_ALLOWED(405), NOT_ACCEPTABLE(406), PROXY_AUTHENTICATION_REQUIRED(407), REQUEST_TIMEOUT(408), CONFLICT(409), GONE(410), LENGTH_REQUIRED(411), PRECONDITION_FAILED(412), PAYLOAD_TO_LARGE(413), URI_TOO_LONG(414), UNSUPPORTED_MEDIA_TYPE(415), REQUEST_RANGE_NOT_SATISFIABLE(416), EXPECTATION_FAILED(417), MISDIRECTED_REQUEST(421), UPGRADE_REQUIRED(426), PRECONDITION_REQUIRED(428), TOO_MANY_REQUESTS(429), REQUEST_HEADER_FIELDS_TOO_LARGE(431), UNAVAILABLE_FOR_LEGAL_REASONS(451);
    
    public final int CODE;
    
    ClientErrorResponses(int code) {
      CODE = code;
    }
    
    public static HTTPStatusCode get(int i) {
      return Arrays.stream(ClientErrorResponses.values()).filter(ir -> ir.CODE - 400 - i == 0).findFirst().orElse(null);
    }
    
    @Override
    public String getMessage() {
      switch (this) {
        case BAD_REQUEST:
          return VERSION + " 400 Bad Request";
        case UNAUTHORIZED:
          return VERSION + " 401 Unauthorized";
        case PAYMENT_REQUIRED:
          return VERSION + " 402 Payment Required";
        case FORBIDDEN:
          return VERSION + " 403 Forbidden";
        case NOT_FOUND:
          return VERSION + " 404 Not Found";
        case METHOD_NOT_ALLOWED:
          return VERSION + " 405 Method Not Allowed";
        case NOT_ACCEPTABLE:
          return VERSION + " 406 Not Acceptable";
        case PROXY_AUTHENTICATION_REQUIRED:
          return VERSION + " 407 Proxy Authentication Required";
        case REQUEST_TIMEOUT:
          return VERSION + " 408 Request Timeout";
        case CONFLICT:
          return VERSION + " 409 Conflict";
        case GONE:
          return VERSION + " 410 Gone";
        case LENGTH_REQUIRED:
          return VERSION + " 411 Length Required";
        case PRECONDITION_FAILED:
          return VERSION + " 412 Precondition Failed";
        case PAYLOAD_TO_LARGE:
          return VERSION + " 413 Payload Too Large";
        case URI_TOO_LONG:
          return VERSION + " 414 URI Too Long";
        case UNSUPPORTED_MEDIA_TYPE:
          return VERSION + " 415 Unsupported Media Type";
        case REQUEST_RANGE_NOT_SATISFIABLE:
          return VERSION + " 416 Request Range Not Satisfiable";
        case EXPECTATION_FAILED:
          return VERSION + " 417 Expectation Failed";
        case MISDIRECTED_REQUEST:
          return VERSION + " 421 Misdirected Request";
        case UPGRADE_REQUIRED:
          return VERSION + " 426 Upgrade Required";
        case PRECONDITION_REQUIRED:
          return VERSION + " 428 Precondition Required";
        case TOO_MANY_REQUESTS:
          return VERSION + " 429 Too Many Requests";
        case REQUEST_HEADER_FIELDS_TOO_LARGE:
          return VERSION + " 431 Request Header Fields Too Large";
        case UNAVAILABLE_FOR_LEGAL_REASONS:
          return VERSION + " Unavailable For Legal Reasons";
      }
      throw new AssertionError("Add new branches");
    }
  }
  
  enum ServerErrorResponses implements HTTPStatusCode {
    INTERNAL_SERVER_ERROR(500), NOT_IMPLEMENTED(501), BAD_GATEWAY(502), SERVICE_UNAVAILABLE(503), GATEWAY_TIMEOUT(504), HTTP_VERSION_NOT_SUPPORTED(505), VARIANT_ALSO_NEGOTIATES_506(506), VARIANT_ALSO_NEGOTIATES_507(507), NETWORK_AUTHENTICATION_REQUIRED(511);
    
    public final int CODE;
    
    ServerErrorResponses(int code) {
      CODE = code;
    }
    
    public static HTTPStatusCode get(int i) {
      return Arrays.stream(ServerErrorResponses.values()).filter(ir -> ir.CODE - 500 - i == 0).findFirst().orElse(null);
    }
    
    @Override
    public String getMessage() {
      switch (this) {
        case INTERNAL_SERVER_ERROR:
          return VERSION + " 500 Internal Server Error";
        case NOT_IMPLEMENTED:
          return VERSION + " 501 Not Implemented";
        case BAD_GATEWAY:
          return VERSION + " 502 Bad Gateway";
        case SERVICE_UNAVAILABLE:
          return VERSION + " 503 Service Unavailable";
        case GATEWAY_TIMEOUT:
          return VERSION + " 504 Gateway Timeout";
        case HTTP_VERSION_NOT_SUPPORTED:
          return VERSION + " 505 HTTP Version Not Supported";
        case VARIANT_ALSO_NEGOTIATES_506:
          return VERSION + " 506 Variant Also Negotiates";
        case VARIANT_ALSO_NEGOTIATES_507:
          return VERSION + " 507 Variant Also Negotiates";
        case NETWORK_AUTHENTICATION_REQUIRED:
          return VERSION + " 511 Network Authentication Required";
      }
      throw new AssertionError("Add pending branches");
    }
  }
  
  static HTTPStatusCode get(int g, int i) {
    try {
      return (HTTPStatusCode) HTTPStatusCode.class.getClasses()[5 - g].getDeclaredMethod("get", int.class).invoke(null, i);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      e.printStackTrace();
    }
    throw new AssertionError("Impossible");
  }
  
  static HTTPStatusCode get(int code) {
    return get(code / 100, code % 100);
  }
  
  String getMessage();
  
}
