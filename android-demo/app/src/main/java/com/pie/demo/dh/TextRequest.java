package com.pie.demo.dh;

import java.util.Objects;

public class TextRequest {
    private String appId;
    private String text;
    private String phoneToken;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextRequest that = (TextRequest) o;
        return Objects.equals(appId, that.appId) &&
                Objects.equals(text, that.text) &&
                Objects.equals(phoneToken, that.phoneToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, text, phoneToken);
    }

    @Override
    public String toString() {
        return "TextRequest{" +
                "appId='" + appId + '\'' +
                ", text='" + text + '\'' +
                ", phoneToken='" + phoneToken + '\'' +
                '}';
    }

    public String getPhoneToken() {
        return phoneToken;
    }

    public void setPhoneToken(String phoneToken) {
        this.phoneToken = phoneToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
