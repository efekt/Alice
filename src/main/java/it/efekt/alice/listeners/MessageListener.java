package it.efekt.alice.listeners;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.TextChannelConfig;
import it.efekt.alice.db.model.UserStats;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {
    private Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        // ignoring private messages
        if (e.isFromType(ChannelType.PRIVATE)){
            return;
        }
        try {
            Guild guild = e.getGuild();
            User user = e.getAuthor();
            TextChannelConfig textChannelConfig = AliceBootstrap.alice.getTextChannelConfigManager().get(e.getTextChannel());

            if (textChannelConfig.isImgOnly() && !e.getMessage().getAttachments().stream().anyMatch(Message.Attachment::isImage)){
                try {
                    // send dm to the person that was trying to send msg.
                    e.getMessage().delete().completeAfter(1, TimeUnit.SECONDS);
                } catch (ErrorResponseException exc){
                    logger.debug("Couldn't remove message, was already removed");
                }
            }

            // ignoring bots
            if (user.isBot()){
                return;
            }

            UserStats userStats = AliceBootstrap.alice.getUserStatsManager().getUserStats(user, guild);
            userStats.addAndSave(1);
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }
}
