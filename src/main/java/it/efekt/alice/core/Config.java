package it.efekt.alice.core;

import static java.lang.String.format;

public class Config {
    private String token;
    private String imgurClientId;
    private String mysqlPassword;
    private String mysqlUrl;
    private String mysqlUser;
    private String mysqlDatabase;
    private int mysqlPort;

    public void setImgurClientId(String imgurClientId){
        this.imgurClientId = imgurClientId;
    }

    public String getImgurClientId(){
        return this.imgurClientId;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }

    public int getMysqlPort() {
        return mysqlPort;
    }

    public void setMysqlPort(int mysqlPort) {
        this.mysqlPort = mysqlPort;
    }

    public String getMysqlPassword() {
        return mysqlPassword;
    }

    public void setMysqlPassword(String mysqlPassword) {
        this.mysqlPassword = mysqlPassword;
    }

    public String getMysqlUrl() {
        return mysqlUrl;
    }

    public void setMysqlUrl(String mysqlUrl) {
        this.mysqlUrl = mysqlUrl;
    }

    public String getMysqlUser() {
        return mysqlUser;
    }

    public void setMysqlUser(String mysqlUser) {
        this.mysqlUser = mysqlUser;
    }

    public String getMysqlDatabase() {
        return mysqlDatabase;
    }

    public void setMysqlDatabase(String mysqlDatabase) {
        this.mysqlDatabase = mysqlDatabase;
    }

    @Override
    public String toString(){
        return new StringBuilder().append(format("token: %s\n", token)).toString();
    }
}
