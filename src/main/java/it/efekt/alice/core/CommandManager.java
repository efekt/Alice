package it.efekt.alice.core;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;

public class CommandManager extends ListenerAdapter {
    private HashMap<String, Command> commands = new HashMap<>();
    private Alice alice;

    public CommandManager(Alice alice){
        this.alice = alice;
        this.alice.getJDA().addEventListener(this);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        User user = e.getAuthor();
        Message msg = e.getMessage();
        String prefix = this.alice.getConfig().getPrefix();

        if (msg.getContentDisplay().startsWith(prefix)){
            String cmdAlias = msg.getContentDisplay().replaceFirst(prefix, "");
                if (this.commands.containsKey(cmdAlias)){
                    //todo permissions system?
                    this.commands.get(cmdAlias).execute(e);
                }
        }
    }

    public void setExecutor(Command command){
        this.commands.put(command.getAlias(), command);
    }

    public HashMap<String, Command> getCommands(){
        return this.commands;
    }
}
