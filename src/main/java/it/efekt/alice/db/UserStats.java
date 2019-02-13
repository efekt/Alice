package it.efekt.alice.db;

import it.efekt.alice.core.AliceBootstrap;
import org.hibernate.Session;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_stats")
public class UserStats {
    @Column(name="guild_id")
    private String guildId;

    @Id
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
