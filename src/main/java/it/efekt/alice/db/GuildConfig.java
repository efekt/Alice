package it.efekt.alice.db;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.efekt.alice.core.AliceBootstrap;
import org.hibernate.Session;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="guild_config", schema = "alice_bot_db")
public class GuildConfig {

    @Id
    @Column(name="id")
    private String id;

    @Column(name="prefix")
    private String prefix;

    // Id of TextChannel
    @Column(name="log_channel")
    private String logChannel;

    @Column(name="locale")
    @ColumnDefault("en_US")
    private String locale = "en_US";

    @Column(name="disabled_features", length = 65535, columnDefinition = "text")
    private String disabledFeatures = "{\"disabled\":[]}";

    public GuildConfig(String id, String defaultPrefix){
        this.id = id;
        this.prefix = defaultPrefix;
    }

    public GuildConfig(){

    }

    public String getDisabledFeatures() {
        return disabledFeatures;
    }

    public void setDisabledFeatures(String disabledFeatures) {
        this.disabledFeatures = disabledFeatures;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
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

    public boolean isDisabled(String cmdAlias){
        if (this.disabledFeatures == null || this.disabledFeatures.isEmpty()){
            JsonObject object = new JsonObject();
            object.add("disabled", new JsonArray());
            this.disabledFeatures = object.toString();
            return false;
        }

        JsonObject object = new JsonParser().parse(this.disabledFeatures).getAsJsonObject();
        JsonArray array = object.get("disabled").getAsJsonArray();
        return array.contains(new JsonParser().parse(cmdAlias));
    }

    public void setDisabled(String cmdAlias){
        if (!isDisabled(cmdAlias)){
            JsonObject object = new JsonParser().parse(this.disabledFeatures).getAsJsonObject();
            JsonArray array = object.get("disabled").getAsJsonArray();
            array.add(cmdAlias);
            this.disabledFeatures = object.toString();
            this.save();
        }
    }
    public void setEnabled(String cmdAlias){
        if (isDisabled(cmdAlias)){
            JsonObject object = new JsonParser().parse(this.disabledFeatures).getAsJsonObject();
            JsonArray array = object.get("disabled").getAsJsonArray();
            array.remove(new JsonParser().parse(cmdAlias));
            this.disabledFeatures = object.toString();
            this.save();
        }
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
