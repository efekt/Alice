package it.efekt.alice.db;

import it.efekt.alice.core.Alice;
import it.efekt.alice.core.AliceBootstrap;
import org.hibernate.Session;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "user_stats", schema = "alice_bot_db")
public class UserStats {
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

    public void addAndSave(int messagesAmount){
        addMessagesAmount(messagesAmount);
        save();
    }

    public boolean isBot(){
        return AliceBootstrap.alice.getJDA().getUserById(this.userId).isBot();
    }

    public boolean isInvalidUser(){
        return AliceBootstrap.alice.getJDA().getUserById(this.userId) == null;
    }

    public void save(){
        Session session = AliceBootstrap.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(this);
        session.getTransaction().commit();
    }
}
