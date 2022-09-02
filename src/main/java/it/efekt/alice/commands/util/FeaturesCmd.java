package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;

public class FeaturesCmd extends Command {

    public FeaturesCmd(String alias) {
        super(alias);
        setDescription(AMessage.CMD_FEATURES_DESC);
        setCategory(CommandCategory.DISCORD_ADMIN_UTILS);
        addPermission(Permission.ADMINISTRATOR);
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        HashMap<String, Command> cmds = AliceBootstrap.alice.getCmdManager().getCommands();
        if (getArgs().length == 0){
            printFeatures(e);
            return true;
        }

        if (getArgs().length == 2){
            String arg = getArgs()[0];
            String chosenAlias = getArgs()[1];

            if (chosenAlias == null || chosenAlias.isEmpty() || !cmds.containsKey(chosenAlias)){
                e.getChannel().sendMessage(AMessage.CMD_FEATURES_WRONG_FEATURE_GIVEN.get(e)).complete();
                return true;
            }

            if (chosenAlias.equalsIgnoreCase(getAlias())){
                e.getChannel().sendMessage(AMessage.CMD_FEATURES_CANNOT_DISABLE.get(e)).complete();
                return true;
            }

            if (arg.equalsIgnoreCase("disable")){
                AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).setCmdDisabled(chosenAlias);
                e.getChannel().sendMessage(AMessage.CMD_FEATURES_HAS_BEEN_DISABLED.get(e, chosenAlias)).complete();
                return true;
            }

            if (arg.equalsIgnoreCase("enable")){
                AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).setCmdEnabled(chosenAlias);
                e.getChannel().sendMessage(AMessage.CMD_FEATURES_HAS_BEEN_ENABLED.get(e, chosenAlias)).complete();
                return true;
            }
        }
        return false;
    }

    private void printFeatures(CombinedCommandEvent e){
        HashMap<String, Command> cmds = AliceBootstrap.alice.getCmdManager().getCommands();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(AMessage.CMD_FEATURES_LIST.get(e));
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);

        for (Command cmd : cmds.values()){
            boolean isDisabled = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).isCmdDisabled(cmd.getAlias());
            String featureStatus = isDisabled ? ":x: " : ":white_check_mark: ";

            embedBuilder.addField(featureStatus + cmd.getAlias(), "", false);
        }
        e.getChannel().sendMessageEmbeds(embedBuilder.build()).complete();
    }
}
