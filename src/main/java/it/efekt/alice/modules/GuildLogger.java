package it.efekt.alice.modules;

import it.efekt.alice.core.Alice;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.GuildConfig;
import it.efekt.alice.lang.AMessage;
import it.efekt.alice.lang.LangCode;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class GuildLogger extends ListenerAdapter {
    private Alice alice;
    private Logger logger = LoggerFactory.getLogger(GuildLogger.class);

    public GuildLogger(Alice alice){
        this.alice = alice;
    }

    @Override
    public void onGenericGuild(GenericGuildEvent e){

        try {
            if (e instanceof GuildJoinEvent){
                GuildJoinEvent event = (GuildJoinEvent) e;
                logger.info("GUILD JOIN: " + event.getGuild().getName() + " : " + event.getGuild().getId() + " members: " + event.getGuild().getMembers().size());
            }

            if (e instanceof GuildLeaveEvent){
                GuildLeaveEvent event = (GuildLeaveEvent) e;
                logger.info("GUILD LEAVE: " + event.getGuild().getName() + " : " + event.getGuild().getId() + " members: " + event.getGuild().getMembers().size());
            }

            if (isLoggerSet(e.getGuild())) {

                if (e instanceof GuildMemberJoinEvent) {
                    GuildMemberJoinEvent event = (GuildMemberJoinEvent) e;

                    if (isOptedOut(event.getUser(), e.getGuild())){
                        return;
                    }

                    log(e.getGuild(), AMessage.LOGGER_USER_JOINS_GUILD.get(e.getGuild(), event.getMember().getEffectiveName()));
                }

                if (e instanceof GuildMemberRemoveEvent) {
                    GuildMemberRemoveEvent event = (GuildMemberRemoveEvent) e;

                    if (isOptedOut(event.getUser(), e.getGuild())){
                        return;
                    }

                    log(e.getGuild(), AMessage.LOGGER_USER_LEAVES_GUILD.get(e.getGuild(), event.getMember().getEffectiveName()));
                }
            }
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent e){
        if (isOptedOut(e.getUser(), e.getGuild())){
            return;
        }

        try {
            log(e.getGuild(), AMessage.LOGGER_USER_CHANGES_STATUS.get(e.getGuild(), e.getUser().getName(), e.getNewOnlineStatus().name()));
        } catch(Exception exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e){
        if (isOptedOut(e.getEntity().getUser(), e.getGuild())){
            return;
        }

        try {
        log(e.getGuild(), AMessage.LOGGER_USER_JOINS_VOICE.get(e.getGuild(), e.getMember().getEffectiveName(), e.getChannelJoined().getName()));
        } catch(Exception exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e){
        if (isOptedOut(e.getEntity().getUser(), e.getGuild())){
            return;
        }

        try {
        log(e.getGuild(), AMessage.LOGGER_USER_LEAVES_VOICE.get(e.getGuild(), e.getMember().getEffectiveName(), e.getChannelLeft().getName()));
        } catch(Exception exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent e){
        if (isOptedOut(e.getEntity().getUser(), e.getGuild())){
            return;
        }

        try {
        log(e.getGuild(), AMessage.LOGGER_USER_SWITCHES_VOICE.get(e.getGuild(), e.getMember().getEffectiveName(), e.getChannelLeft().getName(), e.getChannelJoined().getName()));
        } catch(Exception exc){
            exc.printStackTrace();
        }
    }



    private boolean isLoggerSet(Guild guild){
        try {
            if (!guild.isAvailable()) {
                return false;
            }

            GuildConfig config = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(guild);
            return config.getLogChannel() != null;
        }
        catch(Exception exc){
            return false;
        }

    }

    private GuildConfig getGuildConfig(Guild guild){
        return AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(guild);
    }

    public void log(Guild guild, String message){
        try {

        if (isLoggerSet(guild)){
            TextChannel logChannel;

            try {
                logChannel = guild.getJDA().getTextChannelById(getGuildConfig(guild).getLogChannel());
            } catch(IllegalArgumentException exc){
                return;
            }

            if (logChannel == null){
                return;
            }

            boolean containsInvalidUser = logChannel.getMembers().stream().anyMatch(member -> !member.hasPermission(Permission.ADMINISTRATOR) && !member.hasPermission(Permission.MANAGE_SERVER));
            boolean hasAliceCorrectPermissions = guild.getSelfMember().hasPermission(Permission.ADMINISTRATOR) || guild.getSelfMember().hasPermission(Permission.MANAGE_SERVER);

            if (!hasAliceCorrectPermissions){
                message = AMessage.LOGGER_ALICE_NO_PERMS.get(guild);
            } else if (containsInvalidUser){
                message = AMessage.LOGGER_CHANNEL_USER_WRONG_PERMS.get(guild);
            }

            try {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
                ZonedDateTime guildDateTime =  alice.getGuildConfigManager().getGuildConfig(guild).getGuildDateTime();
                String dateTime = guildDateTime.format(dateFormat);
                logChannel.sendMessage("`[" + dateTime + "]` " + message).queue();
            } catch(InsufficientPermissionException exc) {
                User guildOwner = guild.getOwner().getUser();
                getGuildConfig(guild).setLogChannelAndSave(null);
                guildOwner.openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage(AMessage.MODULE_LOGGER_INSUFFICIENT_PERMS.get(AliceBootstrap.alice.getLanguageManager().getLang(LangCode.valueOf(getGuildConfig(guild).getLocale())), guildOwner.getAsMention(), logChannel.getAsMention())).queue();
                });
            }
        }
        } catch(Exception exc){
            exc.printStackTrace();
        }
    }

    private boolean isOptedOut(User user, Guild guild){
        return AliceBootstrap.alice.getUserStatsManager().getUserStats(user, guild).isLoggerOptedOut();
    }

}
