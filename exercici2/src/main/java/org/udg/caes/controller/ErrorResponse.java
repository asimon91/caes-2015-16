package org.udg.caes.controller;

/**
 * Created by imartin on 30/09/16.
 */
public class ErrorResponse implements Response {
  String message;

  public ErrorResponse(String message) {
    this.message = message;
  }

  public String getContent() {
    return this.message;
  }
}
