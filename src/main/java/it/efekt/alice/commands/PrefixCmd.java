package it.efekt.alice.commands;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.core.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PrefixCmd extends Command {

    public PrefixCmd(String alias) {
        super(alias);
        this.setDesc("Podaj nowy prefix po spacji");
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        String prefix = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).getCmdPrefix();
        if (args.length == 1){
            String newPrefix = args[0];
            AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).setCmdPrefix(newPrefix);
            e.getChannel().sendMessage("Nowy prefix dla tego serwera: " + newPrefix).queue();
        } else {
            e.getChannel().sendMessage("Użyj " + prefix + "prefix <NowyPrefix>").queue();
        }
    }
}
