package it.efekt.alice.listeners;

import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AnalyticsListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent e){
        AliceBootstrap.analytics.reportGuildJoin(e.getGuild());
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e){
        AliceBootstrap.analytics.reportGuildLeave(e.getGuild());
    }
}


