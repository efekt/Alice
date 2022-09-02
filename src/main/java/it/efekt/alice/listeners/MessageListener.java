package it.efekt.alice.listeners;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.TextChannelConfig;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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

        Guild guild = e.getGuild();
        User user = e.getAuthor();

        // ignoring bots
        if (user.isBot()){
            return;
        }

        // Adding msg count to users
        try {
            if (!AliceBootstrap.alice.getUserStatsManager().getUserStats(user, guild).isSpamLvlOptedOut()) {
                AliceBootstrap.alice.getUserStatsManager().addMessageCount(user, guild, 1);
            }
        } catch(Exception exc){
            exc.printStackTrace();
        }

        TextChannelConfig textChannelConfig = AliceBootstrap.alice.getTextChannelConfigManager().get(e.getChannel().asTextChannel());

        if (textChannelConfig.isImgOnly() && e.getMessage().getAttachments().stream().noneMatch(Message.Attachment::isImage)){
            try {
                // send dm to the person that was trying to send msg.
                e.getMessage().delete().completeAfter(1, TimeUnit.SECONDS);
            } catch (ErrorResponseException exc){
                logger.debug("Couldn't remove message, was already removed");
            }
        }
    }
}
