package it.efekt.alice.modules;

import it.efekt.alice.core.Alice;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.GuildConfig;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateOnlineStatusEvent;
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
        if (isLoggerSet(e.getGuild())){
            GuildConfig config = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild());

            TextChannel logChannel = e.getJDA().getTextChannelById(config.getLogChannel());

            if (e instanceof GuildMemberJoinEvent){
                GuildMemberJoinEvent event = (GuildMemberJoinEvent) e;
                log(e.getGuild(),event.getMember().getEffectiveName() + " dołącza do serwera");
            }

            if (e instanceof GuildMemberLeaveEvent){
                GuildMemberLeaveEvent event = (GuildMemberLeaveEvent) e;
                log(e.getGuild(), event.getMember().getEffectiveName() + " odchodzi z serwera");
            }
        }
    }

    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent e){
        log(e.getGuild(), e.getUser().getName() + " zmienia status na: " + e.getNewOnlineStatus());
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e){
        log(e.getGuild(), e.getMember().getEffectiveName() + " wchodzi na kanał: " + e.getChannelJoined().getName());
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e){
        log(e.getGuild(), e.getMember().getEffectiveName() + " wychodzi z kanału: " + e.getChannelLeft().getName());
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent e){
        log(e.getGuild(), e.getMember().getEffectiveName() + " zmienił kanał: " + e.getChannelLeft().getName() + " na: " + e.getChannelJoined().getName());
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
            TextChannel logChannel = guild.getJDA().getTextChannelById(getGuildConfig(guild).getLogChannel());
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
            String dateTime = LocalDateTime.now().format(dateFormat);
            logChannel.sendMessage("`["+dateTime + "]` " + message ).queue();
        }
    }

}
