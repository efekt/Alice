package it.efekt.alice.db;

import it.efekt.alice.core.AliceBootstrap;
import org.hibernate.Session;

import javax.persistence.*;

@Entity
@Table(name="game_stats", schema = "alice_bot_db")
public class GameStats {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="guild_id")
    private String guildId;


    @Column(name="user_id")
    private String userId;

    @Column(name="game_name")
    private String gameName;

    //In seconds
    @Column(name="time_played")
    private int timePlayed;


    public GameStats(){}

    public GameStats(String userId, String guildId, String gameName){
        this.userId = userId;
        this.guildId = guildId;
        this.gameName = gameName;
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

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(int timePlayed) {
        this.timePlayed = timePlayed;
    }

    public void addTimePlayed(long timePlayed){
        this.timePlayed += timePlayed;
    }

    public boolean isInvalidUser(){
        return AliceBootstrap.alice.getJDA().getUserById(this.userId) == null;
    }

    public boolean isBot(){
        return AliceBootstrap.alice.getJDA().getUserById(this.userId).isBot();
    }


    public void save(){
        Session session = AliceBootstrap.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(this);
        session.getTransaction().commit();
    }
}
