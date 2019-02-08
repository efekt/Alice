package it.efekt.alice.db;

import it.efekt.alice.core.AliceBootstrap;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="guild_config")
public class GuildConfig {

    @Id
    @Column(name="id")
    private String id;

    @Column(name="prefix")
    private String prefix;

    // Id of TextChannel
    @Column(name="log_channel")
    private String logChannel;

    public GuildConfig(String id, String defaultPrefix){
        this.id = id;
        this.prefix = defaultPrefix;
    }

    public GuildConfig(){

    }

    public String getLogChannel() {
        return logChannel;
    }

    public void setLogChannel(String logChannel) {
        this.logChannel = logChannel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void save(){
        Session session = AliceBootstrap.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(this);
        session.getTransaction().commit();
    }

    @Override
    public String toString() {
        return "GuildConfig{" +
                "id='" + id + '\'' +
                ", prefix='" + prefix + '\'' +
                '}';
    }
}
