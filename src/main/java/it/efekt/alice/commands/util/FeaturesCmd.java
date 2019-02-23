package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;

public class FeaturesCmd extends Command {

    public FeaturesCmd(String alias) {
        super(alias);
        setDescription("Zadządzanie dostępnością komend Alice na tym serwerze \n `disable/enable` `nazwa komendy` - wyłącza/włącza komendę");
        setCategory(CommandCategory.UTILS);
        addPermission(Permission.ADMINISTRATOR);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        HashMap<String, Command> cmds = AliceBootstrap.alice.getCmdManager().getCommands();
        if (getArgs().length == 0){
            printFeatures(e);
            return true;
        }

        if (getArgs().length == 2){
            String arg = getArgs()[0];
            String chosenAlias = getArgs()[1];

            if (chosenAlias == null || chosenAlias.isEmpty() || !cmds.containsKey(chosenAlias)){
                e.getChannel().sendMessage("Podano nieprawidłową nazwę funkcji").queue();
                return true;
            }

            if (chosenAlias.equalsIgnoreCase(getAlias())){
                e.getChannel().sendMessage("Nie możesz wyłączyć tej funkcji").queue();
                return true;
            }

            if (arg.equalsIgnoreCase("disable")){
                AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).setDisabled(chosenAlias);
                e.getChannel().sendMessage("Funkcjonalność " + chosenAlias + " została wyłączona").queue();
                return true;
            }

            if (arg.equalsIgnoreCase("enable")){
                AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).setEnabled(chosenAlias);
                e.getChannel().sendMessage("Funkcjonalność " + chosenAlias + " została włączona").queue();
                return true;
            }
        }
        return false;
    }

    private void printFeatures(MessageReceivedEvent e){
        HashMap<String, Command> cmds = AliceBootstrap.alice.getCmdManager().getCommands();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Lista funkcji bota");
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);

        for (Command cmd : cmds.values()){
            boolean isDisabled = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).isDisabled(cmd.getAlias());
            String featureStatus = isDisabled ? ":x: " : ":white_check_mark: ";

            embedBuilder.addField(featureStatus + cmd.getAlias(), "", false);
        }
        e.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
