package it.efekt.alice.commands;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.commands.core.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpCmd extends Command {

    public HelpCmd(String alias){
        super(alias);
        setDescription("Komenda pomocy");
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Alice bot - Pomoc");

        for (Command cmd : AliceBootstrap.alice.getCmdManager().getCommands().values()){
            embedBuilder.addField(getGuildPrefix(e.getGuild()) + cmd.getAlias() + cmd.getUsageInfo(), cmd.getDesc(), false);
        }
        e.getChannel().sendMessage(embedBuilder.build()).queue();
    }

}
