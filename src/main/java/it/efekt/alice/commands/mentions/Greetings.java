package it.efekt.alice.commands.mentions;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Greetings extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        Message msg = e.getMessage();
        if (msg.isMentioned(e.getJDA().getSelfUser(), Message.MentionType.USER)){
            if (msg.getContentDisplay().toLowerCase().contains(it.efekt.alice.lang.Message.ALICE_GOODBYE_REQUIRED.get(e))){
                e.getChannel().sendMessage(it.efekt.alice.lang.Message.ALICE_GOODBYE_RESPONSE.get(e, e.getAuthor().getAsMention())).queue();
            }

            if (msg.getContentDisplay().toLowerCase().contains(it.efekt.alice.lang.Message.ALICE_MORNING_REQUIRED.get(e))){
                e.getChannel().sendMessage(it.efekt.alice.lang.Message.ALICE_MORNING_RESPONSE.get(e,e.getAuthor().getAsMention()) + " ;)").queue();
            }
        }
    }


}
