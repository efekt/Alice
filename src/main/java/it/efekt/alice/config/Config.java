package it.efekt.alice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.String.format;
// This class is used by snakeyaml
public class Config {
    private final Logger logger = LoggerFactory.getLogger(Config.class);
    private String token;
    private String imgurClientId;
    private String googleAnalyticsId;
    private String mysqlPassword;
    private String mysqlUrl;
    private String mysqlUser;
    private String mysqlDatabase;
    private int mysqlPort;
    private String igdbApiKey;
    private String discordBotListApiToken;

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

    public String getGoogleAnalyticsId() {
        return googleAnalyticsId;
    }

    public void setGoogleAnalyticsId(String googleAnalyticsId) {
        this.googleAnalyticsId = googleAnalyticsId;
    }

    public String getIgdbApiKey() {
        return igdbApiKey;
    }

    public void setIgdbApiKey(String igdbApiKey) {
        this.igdbApiKey = igdbApiKey;
    }

    public String getDiscordBotListApiToken() {
        return discordBotListApiToken;
    }

    public void setDiscordBotListApiToken(String discordBotListApiToken) {
        this.discordBotListApiToken = discordBotListApiToken;
    }

    @Override
    public String toString(){
        return new StringBuilder().append(format("token: %s\n", token)).toString();
    }
}
