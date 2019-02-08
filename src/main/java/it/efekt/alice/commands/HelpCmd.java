package it.efekt.alice.commands;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.commands.core.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);



        for (CommandCategory cat : CommandCategory.values()){
            List<Command> cmds = AliceBootstrap.alice.getCmdManager().getCommands(cat);
            if (cmds.isEmpty() || cmds == null){
                continue;
            }

            if (cat == CommandCategory.BOT_ADMIN){
                continue;
            }

            List<String> commandsAliases = new ArrayList<>();

            for (Command cmd : cmds){
                if (cmd.canUseCmd(e.getMember())) {
                    String nsfwString = cmd.isNsfw() ? " (nsfw)" : "";
                    String guildPrefix = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).getPrefix();
                    commandsAliases.add("`" + guildPrefix + cmd.getAlias() + nsfwString+"`");
                }
            }

            String commandAliasesFormated = commandsAliases.toString().replaceAll("\\[", "")
                    .replaceAll("\\[", "")
                    .replaceAll("\\]", "")
                    .replaceAll(",", "");

            embedBuilder.addField(cat.getName(),commandAliasesFormated, false);

        }


        e.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
