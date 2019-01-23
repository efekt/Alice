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
        embedBuilder.setTitle("Alice - Centrum Pomocy");
        embedBuilder.setThumbnail("https://i.imgur.com/qZe2WZz.jpg");

            for (Command cmd : AliceBootstrap.alice.getCmdManager().getCommands().values()){
                if (cmd.canUseCmd(e.getMember())) {
                    embedBuilder.addField(getGuildPrefix(e.getGuild()) + cmd.getAlias() + cmd.getUsageInfo(), cmd.getDesc(), false);
                }
            }
        e.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
