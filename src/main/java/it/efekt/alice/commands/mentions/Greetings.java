package it.efekt.alice.commands.mentions;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Greetings extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        Message msg = e.getMessage();
        if (msg.isMentioned(e.getJDA().getSelfUser(), Message.MentionType.USER)){
            if (msg.getContentDisplay().contains("dobranoc")){
                e.getChannel().sendMessage("Dobrej nocy! "+e.getAuthor().getAsMention()).queue();
            }

            if (msg.getContentDisplay().contains("dzień dobry")){
                e.getChannel().sendMessage("Dzień Dobry! "+e.getAuthor().getAsMention() + " ;)").queue();
            }
        }
    }


}
