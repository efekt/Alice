package it.efekt.alice.db.model;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.AliceDb;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;

@Entity
@Table(name = "user_stats", schema = "alice_bot_db")
public class UserStats extends AliceDb {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="guild_id")
    private String guildId;

    @Column(name="user_id")
    private String userId;

    @Column(name="messages_amount")
    @ColumnDefault("0")
    private int messagesAmount;

    @Column(name="optout_logger")
    private boolean isLoggerOptedOut;

    @Column(name="optout_spamlvl")
    private boolean isSpamLvlOptedOut;

    @Column(name="optout_gamestats")
    private boolean isGameStatsOptedOut;

    public UserStats(){}

    public UserStats(String userId, String guildId){
        this.userId = userId;
        this.guildId = guildId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMessagesAmount() {
        return messagesAmount;
    }

    public void setMessagesAmount(int messagesAmount) {
        this.messagesAmount = messagesAmount;
    }

    public void addMessagesAmount(int amount){
        this.setMessagesAmount(this.messagesAmount += amount);
    }

    public void addMessagesAmountAndSave(int messagesAmount){
        addMessagesAmount(messagesAmount);
        save();
    }

    public boolean isLoggerOptedOut() {
        return isLoggerOptedOut;
    }

    public void setLoggerOptedOut(boolean loggerOptedOut) {
        isLoggerOptedOut = loggerOptedOut;
    }

    public boolean isSpamLvlOptedOut() {
        return isSpamLvlOptedOut;
    }

    public void setSpamLvlOptedOut(boolean spamLvlOptedOut) {
        isSpamLvlOptedOut = spamLvlOptedOut;
    }

    public boolean isGameStatsOptedOut() {
        return isGameStatsOptedOut;
    }

    public void setGameStatsOptedOut(boolean gameStatsOptedOut) {
        isGameStatsOptedOut = gameStatsOptedOut;
    }

    @Transient
    public boolean isBot(){
            return AliceBootstrap.alice.getShardManager().getUserById(this.userId).isBot();
    }

    @Transient
    public boolean isInvalidUser(){
        return AliceBootstrap.alice.getShardManager().getUserById(this.userId) == null;
    }
}
