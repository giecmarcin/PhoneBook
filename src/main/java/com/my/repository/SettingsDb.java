package com.my.repository;

/**
 * Created by Marcin on 01.09.2016.
 */
public class SettingsDb {
    private String urlToDb;
    private String user;
    private String passowrd;

    public SettingsDb(String urlToDb, String user, String passowrd) {
        this.urlToDb = urlToDb;
        this.user = user;
        this.passowrd = passowrd;
    }

    public String getUrlToDb() {
        return urlToDb;
    }

    public void setUrlToDb(String urlToDb) {
        this.urlToDb = urlToDb;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassowrd() {
        return passowrd;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
    }
}
