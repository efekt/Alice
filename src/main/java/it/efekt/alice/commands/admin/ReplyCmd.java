package it.efekt.alice.commands.admin;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class ReplyCmd extends Command {
    private final String BASE_URL = "https://discordapp.com/channels/";

    public ReplyCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.BOT_ADMIN);
        setIsAdminCommand(true);
        setPrivateChannelCmd(true);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        if (getArgs().length < 2){
            e.getPrivateChannel().sendMessage("You need to provide at least 2 arguments: \n- message url\n- your reply").complete();
            return true;
        }

        if (isValidURL(getArgs()[0])){
            String strippedUrl = getArgs()[0].replace(BASE_URL, "");
            String guildId = strippedUrl.split("/")[0];
            String channelId = strippedUrl.split("/")[1];
            String msgId = strippedUrl.split("/")[2];

            String replyMessage = "";
            for (int i = 1; i <= getArgs().length - 1; i++) {
                replyMessage = replyMessage.concat(getArgs()[i]).concat(" ");
            }

            Message originMessage = e.getJDA().getGuildById(guildId).getTextChannelById(channelId).retrieveMessageById(msgId).complete();
            replyMessage = replyMessage.replace("@user", originMessage.getAuthor().getAsMention());
            originMessage.getTextChannel().sendTyping().complete();
            originMessage.getTextChannel().sendMessage(replyMessage).completeAfter(3, TimeUnit.SECONDS);
            e.getPrivateChannel().sendMessage("AMessage sent in reply to " + originMessage.getAuthor().getName() + ":\n```" + replyMessage + "```").complete();
            return true;

        } else {
            e.getPrivateChannel().sendMessage("You need to provide me a valid url and a message").queue();
            return true;
        }
    }

    private boolean isValidURL(String url){
        try {
            new URL(url);
            if (url.startsWith(BASE_URL)){
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }
}
