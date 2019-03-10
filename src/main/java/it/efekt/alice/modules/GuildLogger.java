package it.efekt.alice.modules;

import it.efekt.alice.core.Alice;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.GuildConfig;
import it.efekt.alice.lang.LangCode;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GuildLogger extends ListenerAdapter {
    private Alice alice;

    public GuildLogger(Alice alice){
        this.alice = alice;
    }

    @Override
    public void onGenericGuild(GenericGuildEvent e){
        try {
            if (isLoggerSet(e.getGuild())) {
                GuildConfig config = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild());

                if (e instanceof GuildMemberJoinEvent) {
                    GuildMemberJoinEvent event = (GuildMemberJoinEvent) e;
                    log(e.getGuild(), Message.LOGGER_USER_JOINS_GUILD.get(e.getGuild(), event.getMember().getEffectiveName()));
                }

                if (e instanceof GuildMemberLeaveEvent) {
                    GuildMemberLeaveEvent event = (GuildMemberLeaveEvent) e;
                    log(e.getGuild(), Message.LOGGER_USER_LEAVES_GUILD.get(e.getGuild(), event.getMember().getEffectiveName()));
                }
            }
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent e){
        log(e.getGuild(), Message.LOGGER_USER_CHANGES_STATUS.get(e.getGuild(), e.getUser().getName(), e.getNewOnlineStatus().name()));
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e){
        log(e.getGuild(), Message.LOGGER_USER_JOINS_VOICE.get(e.getGuild(), e.getMember().getEffectiveName(), e.getChannelJoined().getName()));
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e){
        log(e.getGuild(), Message.LOGGER_USER_LEAVES_VOICE.get(e.getGuild(), e.getMember().getEffectiveName(), e.getChannelLeft().getName()));
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent e){
        log(e.getGuild(), Message.LOGGER_USER_SWITCHES_VOICE.get(e.getGuild(), e.getMember().getEffectiveName(), e.getChannelLeft().getName(), e.getChannelJoined().getName()));
    }



    private boolean isLoggerSet(Guild guild){
        GuildConfig config = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(guild);
        return config.getLogChannel() != null;
    }

    private GuildConfig getGuildConfig(Guild guild){
        return AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(guild);
    }

    public void log(Guild guild, String message){
        if (isLoggerSet(guild)){
            if (guild.getJDA().getTextChannelById(getGuildConfig(guild).getLogChannel()) == null){
                return;
            }

            TextChannel logChannel = guild.getJDA().getTextChannelById(getGuildConfig(guild).getLogChannel());

            try {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
                String dateTime = LocalDateTime.now().format(dateFormat);
                logChannel.sendMessage("`[" + dateTime + "]` " + message).queue();
            } catch(InsufficientPermissionException exc){
                User guildOwner = guild.getOwner().getUser();
                getGuildConfig(guild).setLogChannel(null);
                guildOwner.openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage(Message.MODULE_LOGGER_INSUFFICIENT_PERMS.get(AliceBootstrap.alice.getLanguageManager().getLang(LangCode.valueOf(getGuildConfig(guild).getLocale())), guildOwner.getAsMention(), logChannel.getAsMention())).queue();
                });
            } catch(Exception exc){
                exc.printStackTrace();
            }
        }
    }

}
