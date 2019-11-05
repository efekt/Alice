package it.efekt.alice.db.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.efekt.alice.db.AliceDb;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name="guild_config", schema = "alice_bot_db")
public class GuildConfig extends AliceDb {

    @Id
    @Column(name="id")
    private String id;

    @Column(name="prefix")
    private String prefix;

    @Column(name="timezone")
    private String timezone;

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

    public void setLocaleAndSave(String locale){
        setLocale(locale);
        save();
    }

    public String getLogChannel() {
        return logChannel;
    }

    public void setLogChannelAndSave(String logChannel) {
        this.logChannel = logChannel;
        save();
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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public ZonedDateTime getGuildDateTime(){
        if (this.timezone == null){
            return ZonedDateTime.now();
        } else {
            ZonedDateTime nowGuildTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of(this.timezone));
            return nowGuildTime;
        }
    }

    public boolean isCmdDisabled(String cmdAlias){
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

    public void setCmdDisabled(String cmdAlias){
        if (!isCmdDisabled(cmdAlias)){
            JsonObject object = new JsonParser().parse(this.disabledFeatures).getAsJsonObject();
            JsonArray array = object.get("disabled").getAsJsonArray();
            array.add(cmdAlias);
            this.disabledFeatures = object.toString();
            this.save();
        }
    }
    public void setCmdEnabled(String cmdAlias){
        if (isCmdDisabled(cmdAlias)){
            JsonObject object = new JsonParser().parse(this.disabledFeatures).getAsJsonObject();
            JsonArray array = object.get("disabled").getAsJsonArray();
            array.remove(new JsonParser().parse(cmdAlias));
            this.disabledFeatures = object.toString();
            this.save();
        }
    }

    public boolean isEmpty(){
        return this.id == null || this.id.isEmpty();
    }
}
