package com.liberasoft.zero.molitvenik2.model;

import java.util.HashMap;

public class PrayersModel {

  private String title;
  private String text;
  private String date;
  private String postedBy;
  private String id;
  private HashMap<String, String> likes;
  private boolean prayerPrivate;

  public PrayersModel(String title, String text, String date, String postedBy, String id, HashMap<String, String> likes, boolean prayerPrivate) {
    this.title = title;
    this.text = text;
    this.date = date;
    this.postedBy = postedBy;
    this.id = id;
    this.likes = likes;
    this.prayerPrivate = prayerPrivate;
  }

  public PrayersModel() {
  }

  public String getTitle() {
    return title;
  }

  public String getText() {
    return text;
  }

  public String getDate() {
    return date;
  }

  public String getPostedBy() {
    return postedBy;
  }

  public String getId() {
    return id;
  }

  public HashMap<String, String> getLikes() {
    return likes;
  }

  public boolean isPrayerPrivate() {
    return prayerPrivate;
  }
}

