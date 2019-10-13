package it.efekt.alice.db.model;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.AliceDb;
import javax.persistence.*;

@Entity
@Table(name="game_stats", schema = "alice_bot_db")
public class GameStats extends AliceDb {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="guild_id")
    private String guildId;

    @Column(name="user_id")
    private String userId;

    @Column(name="game_name")
    private String gameName;

    //In minutes
    @Column(name="time_played")
    private long timePlayed;

    public GameStats(){}

    public GameStats(String userId, String guildId, String gameName){
        this.userId = userId;
        this.guildId = guildId;
        this.gameName = gameName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(long timePlayed) {
        this.timePlayed = timePlayed;
    }

    public void addTimePlayed(long timePlayed){
        this.timePlayed += timePlayed;
    }

    public boolean isInvalidUser(){
        return AliceBootstrap.alice.getShardManager().getUserById(this.userId) == null;
    }

    public boolean isBot(){
        return AliceBootstrap.alice.getShardManager().getUserById(this.userId).isBot();
    }

}
