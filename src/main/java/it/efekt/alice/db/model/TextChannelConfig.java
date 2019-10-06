package it.efekt.alice.db.model;

import it.efekt.alice.db.AliceDb;
import org.hibernate.annotations.Type;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="textchannel_config", schema = "alice_bot_db")
public class TextChannelConfig extends AliceDb {

    @Id
    @Column(name="id")
    private String channelId;

    @Column(columnDefinition = "TINYINT", name="img_only")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean imgOnly;

    public TextChannelConfig(String channelId){
        this.channelId = channelId;
    }

    public TextChannelConfig(){

    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public boolean isImgOnly() {
        return imgOnly;
    }

    public void setImgOnly(boolean imgOnly) {
        this.imgOnly = imgOnly;
        save();
    }
}
