package it.efekt.alice.modules.mentions;

import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Greetings extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        Message msg = e.getMessage();
        if (msg.isMentioned(e.getJDA().getSelfUser(), Message.MentionType.USER)){
            if (msg.getContentDisplay().toLowerCase().contains(AMessage.ALICE_GOODBYE_REQUIRED.get(e))){
                e.getChannel().sendMessage(AMessage.ALICE_GOODBYE_RESPONSE.get(e, e.getAuthor().getAsMention())).queue();
            }

            if (msg.getContentDisplay().toLowerCase().contains(AMessage.ALICE_MORNING_REQUIRED.get(e))){
                e.getChannel().sendMessage(AMessage.ALICE_MORNING_RESPONSE.get(e,e.getAuthor().getAsMention()) + " ;)").queue();
            }
        }
    }


}
