package it.efekt.alice.listeners;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.UserStats;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.ExceptionEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageListener extends ListenerAdapter {
    private Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        Guild guild = e.getGuild();
        User user = e.getAuthor();

        UserStats userStats = AliceBootstrap.alice.getUserStatsManager().getUserStats(user, guild);
        userStats.addAndSave(1);
        //At this moment it saves info to db every time new message is received, todo make it save periodically later...
        //userStats.save();
    }

    @Override
    public void onException(ExceptionEvent e){
        logger.error(e.getCause().getLocalizedMessage());
    }

}
