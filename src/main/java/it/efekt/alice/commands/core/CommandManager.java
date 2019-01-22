package it.efekt.alice.commands.core;

import it.efekt.alice.core.Alice;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import java.util.HashMap;

public class CommandManager extends ListenerAdapter {
    private HashMap<String, Command> commands = new HashMap<>();
    private Alice alice;

    public CommandManager(Alice alice){
        this.alice = alice;
    }

    public void setExecutor(Command command){
        this.alice.getJDA().addEventListener(command);
        this.commands.put(command.getAlias(), command);
    }

    public HashMap<String, Command> getCommands(){
        return this.commands;
    }

    public Command getCommand(String alias){
        return this.commands.get(alias);
    }
}