package it.efekt.alice.commands;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.commands.core.CommandManager;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import java.util.ArrayList;
import java.util.List;

public class HelpCmd extends Command {

    public HelpCmd(String alias){
        super(alias);
        setShortUsageInfo(AMessage.CMD_HELP_SHORT_USAGE_INFO);
        setDescription(AMessage.CMD_HELP_DESCRIPTION);
        setCategory(CommandCategory.UTILS);
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(AMessage.CMD_HELP_TITLE.get(e));
        embedBuilder.setThumbnail(e.getJDA().getSelfUser().getEffectiveAvatarUrl());
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
        CommandManager cmdManager = AliceBootstrap.alice.getCmdManager();

        if (getArgs().length == 1 && !cmdManager.isValidAlias(getArgs()[0])){
            embedBuilder.addField(AMessage.CMD_HELP_COMMAND_UNKNOWN.get(e), AMessage.CMD_HELP_NOT_FOUND_TRY_AGAIN.get(e), false);
        }

        if (getArgs().length == 1 && cmdManager.isValidAlias(getArgs()[0])){
            String alias = getArgs()[0];
            Command cmd = cmdManager.getCommand(alias);

            if (!cmd.canUseCmd(e.getMember())){
                return true;
            }

            String prefix = getGuildPrefix(e.getGuild());
            String requiredPermissions = cmd.getPermissions().toString().replace("[", "")
                    .replace("]", "");

            embedBuilder.setTitle(null);
            String localizedDesc = cmd.getDesc().get(e);
            embedBuilder.addField(prefix + alias + " " + cmd.getShortUsageInfo().get(e), localizedDesc.isEmpty() ? "-" : localizedDesc + "\n" + cmd.getFullUsageInfo().get(e), false);
            embedBuilder.addField(AMessage.CMD_HELP_CATEGORY.get(e), cmd.getCommandCategory().getName(), false);
            if (!requiredPermissions.isEmpty()) {
                embedBuilder.addField(AMessage.CMD_HELP_REQUIRED_PERMS.get(e), requiredPermissions, false);
            }
        }

        if (getArgs().length == 0){
            for (CommandCategory cat : CommandCategory.values()){
                List<Command> cmds = AliceBootstrap.alice.getCmdManager().getCommands(cat);
                if (cmds == null || cmds.isEmpty()){
                    continue;
                }

                if (cmds.stream().allMatch(cmd -> AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).isCmdDisabled(cmd.getAlias()))){
                    continue;
                }

                if (cat.equals(CommandCategory.DISCORD_ADMIN_UTILS) && !e.getMember().hasPermission(Permission.ADMINISTRATOR)){
                    continue;
                }

                if (cat == CommandCategory.BOT_ADMIN){
                    continue;
                }

                List<String> commandsAliases = new ArrayList<>();

                for (Command cmd : cmds){
                    if (getGuildConfig(e.getGuild()).isCmdDisabled(cmd.getAlias())){
                        continue;
                    }

                    if (cmd.canUseCmd(e.getMember())) {
                        String nsfwString = cmd.isNsfw() ? " " + AMessage.CMD_NSFW_NOTIFICATION.get(e): "";
                        String guildPrefix = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).getPrefix();
                        commandsAliases.add("`" + guildPrefix + cmd.getAlias() + "`");
                    }
                }

                String commandAliasesFormated = commandsAliases.toString().replace("[", "")
                        .replace("[", "")
                        .replace("]", "")
                        .replace(",", "");

                embedBuilder.addField(cat.getName(),commandAliasesFormated, false);
                embedBuilder.setFooter(getGuildPrefix(e.getGuild()) + getAlias() + " " + AMessage.CMD_HELP_FOOTER.get(e), e.getJDA().getSelfUser().getEffectiveAvatarUrl());
            }
        }

        e.sendEmbeddedMessageToChannel(embedBuilder.build());
        return true;
    }
}
