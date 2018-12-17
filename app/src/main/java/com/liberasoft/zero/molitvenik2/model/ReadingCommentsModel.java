package com.liberasoft.zero.molitvenik2.model;

public class ReadingCommentsModel {
  private String username;
  private String timeStamp;
  private String message;

  public ReadingCommentsModel(String username, String timeStamp, String message) {
    this.username = username;
    this.timeStamp = timeStamp;
    this.message = message;
  }

  public ReadingCommentsModel() {}

  public String getUsername() {
    return username;
  }

  public String getTimeStamp() {
    return timeStamp;
  }

  public String getMessage() {
    return message;
  }
}

