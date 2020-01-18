package it.efekt.alice.commands.core;

import it.efekt.alice.core.Alice;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    private HashMap<String, Command> commands;
    private Alice alice;

    public CommandManager(Alice alice){
        this.alice = alice;
        this.commands = new HashMap<>();
    }

    public void setExecutor(Command command){
        this.commands.put(command.getAlias(), command);
    }

    public HashMap<String, Command> getCommands(){
        return this.commands;
    }

    public Command getCommand(String alias){
        return this.commands.get(alias);
    }

    public boolean isValidAlias(String alias){
        return this.commands.containsKey(alias);
    }

    public List<Command> getCommands(CommandCategory category){
        List<Command> commands = new ArrayList<>();
        for (Command cmd : this.commands.values()){
            if (cmd.getCommandCategory() == category){
                commands.add(cmd);
            }
        }
        return commands;
    }
}