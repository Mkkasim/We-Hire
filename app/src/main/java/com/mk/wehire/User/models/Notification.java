package com.mk.wehire.User.models;

import android.app.Activity;
import android.content.Context;

public class Notification {

    String userFcmToken;
    String title;
    String body;
    String CompId,UserId,key;

    public Notification() {
    }

    public Notification(String userFcmToken, String title, String body, String compId, String userId, String key) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        CompId = compId;
        UserId = userId;
        this.key = key;
    }

    public String getUserFcmToken() {
        return userFcmToken;
    }

    public void setUserFcmToken(String userFcmToken) {
        this.userFcmToken = userFcmToken;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCompId() {
        return CompId;
    }

    public void setCompId(String compId) {
        CompId = compId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
