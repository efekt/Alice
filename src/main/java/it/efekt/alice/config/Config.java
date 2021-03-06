package it.efekt.alice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.String.format;
// This class is used by snakeyaml
public class Config {
    private final Logger logger = LoggerFactory.getLogger(Config.class);
    private String token;
    private int shardsTotal;
    private String imgurClientId;
    private String googleAnalyticsId;
    private String mysqlPassword;
    private String mysqlUrl;
    private String mysqlUser;
    private String mysqlDatabase;
    private int mysqlPort;
    private String igdbApiKey;
    private String discordBotListApiToken;
    private String restUser;
    private String restPassword;
    private String lavaPlayerNodeUrl;
    private String ipv6Block;

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

    public String getRestUser() {
        return restUser;
    }

    public void setRestUser(String restUser) {
        this.restUser = restUser;
    }

    public String getRestPassword() {
        return restPassword;
    }

    public void setRestPassword(String restPassword) {
        this.restPassword = restPassword;
    }

    public void setDiscordBotListApiToken(String discordBotListApiToken) {
        this.discordBotListApiToken = discordBotListApiToken;
    }

    public int getShardsTotal() {
        return shardsTotal;
    }

    public void setShardsTotal(int shardsTotal) {
        this.shardsTotal = shardsTotal;
    }

    public String getLavaPlayerNodeUrl() {
        return lavaPlayerNodeUrl;
    }

    public void setLavaPlayerNodeUrl(String lavaPlayerNodeUrl) {
        this.lavaPlayerNodeUrl = lavaPlayerNodeUrl;
    }

    public String getIpv6Block() {
        return ipv6Block;
    }

    public void setIpv6Block(String ipv6Block) {
        this.ipv6Block = ipv6Block;
    }

    @Override
    public String toString(){
        return new StringBuilder().append(format("token: %s\n", token)).toString();
    }
}
