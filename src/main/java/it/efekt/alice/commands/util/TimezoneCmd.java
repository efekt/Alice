package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.GuildConfig;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.zone.ZoneRulesException;
import java.util.Locale;

public class TimezoneCmd extends Command {

    public TimezoneCmd(String alias) {
        super(alias);
        addPermission(Permission.ADMINISTRATOR);
        setCategory(CommandCategory.DISCORD_ADMIN_UTILS);
        setDescription(AMessage.CMD_TIMEZONE_DESC);
        setFullUsageInfo(AMessage.CMD_TIMEZONE_FULL_USAGE_INFO);
        setShortUsageInfo(AMessage.CMD_TIMEZONE_SHORT_USAGE_INFO);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        GuildConfig guildConfig = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild());

        if (getArgs().length == 0){
            ZonedDateTime guildDateTime = guildConfig.getGuildDateTime();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
            String dateTime = guildDateTime.format(dateFormat);
            String timezoneName =  guildDateTime.getZone().getDisplayName(TextStyle.SHORT_STANDALONE, Locale.ENGLISH);
            e.getTextChannel().sendMessage( AMessage.CMD_TIMEZONE_CURRENT.get(e.getGuild(), timezoneName) + dateTime + " ("+ guildDateTime.getOffset().toString()+")").complete();
            return true;
        }

        if (getArgs().length == 1){
            String timezone = getArgs()[0];

            // make sure that all timezones are saved in the same format...
            if (!timezone.contains("GMT")){
                e.getTextChannel().sendMessage(AMessage.CMD_TIMEZONE_WRONG.get(e.getGuild())).complete();
                return true;
            }

            try {
                ZoneId zoneId = ZoneId.of(timezone);
                guildConfig.setTimezone(zoneId.toString());
                guildConfig.save();
                e.getTextChannel().sendMessage(AMessage.CMD_TIMEZONE_CHANGED.get(e.getGuild(), zoneId.getId())).complete();
                return true;
            } catch (ZoneRulesException exc){
                e.getTextChannel().sendMessage(AMessage.CMD_TIMEZONE_WRONG.get(e.getGuild())).complete();
                return true;
            }
        }

        return false;
    }
}
