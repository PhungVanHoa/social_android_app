package com.liberasoft.zero.molitvenik2.model;

public class UserModel {

  private String username;
  private String dateJoined;

  public UserModel(String username, String dateJoined) {
    this.username = username;
    this.dateJoined = dateJoined;
  }

  public UserModel() {
  }

  public String getUsername() {
    return username;
  }

  public String getDateJoined() {
    return dateJoined;
  }
}
