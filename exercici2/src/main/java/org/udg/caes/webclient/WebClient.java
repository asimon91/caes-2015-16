package org.udg.caes.webclient;

import java.io.InputStream;

/**
 * Created by imartin on 10/10/16.
 */
public class WebClient {
  public String getContent(ConnectionFactory connectionFactory) {
    StringBuffer content = new StringBuffer();
    try {
      InputStream is = connectionFactory.getData();
      int count;
      while (-1 != (count = is.read())) {
        content.append(new String(Character.toChars(count)));
      }
    } catch (Exception e) {
      return null;
    }
    return content.toString();
  }
}
