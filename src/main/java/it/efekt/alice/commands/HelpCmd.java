package it.efekt.alice.commands;

import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.commands.core.CommandManager;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class HelpCmd extends Command {

    public HelpCmd(String alias){
        super(alias);
        setShortUsageInfo(Message.CMD_HELP_SHORT_USAGE_INFO);
        setDescription(Message.CMD_HELP_DESCRIPTION);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Message.CMD_HELP_TITLE.get(e));
        embedBuilder.setThumbnail(e.getJDA().getSelfUser().getEffectiveAvatarUrl());
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
        CommandManager cmdManager = AliceBootstrap.alice.getCmdManager();

        if (getArgs().length == 1 && !cmdManager.isValidAlias(getArgs()[0])){
            embedBuilder.addField(Message.CMD_HELP_COMMAND_UNKNOWN.get(e), Message.CMD_HELP_NOT_FOUND_TRY_AGAIN.get(e), false);
        }

        if (getArgs().length == 1 && cmdManager.isValidAlias(getArgs()[0])){
            String alias = getArgs()[0];
            Command cmd = cmdManager.getCommand(alias);

            if (!cmd.canUseCmd(e.getMember())){
                return true;
            }

            String prefix = getGuildPrefix(e.getGuild());
            String requiredPermissions = cmd.getPermissions().toString().replaceAll("\\[", "")
                    .replaceAll("]", "");

            embedBuilder.setTitle(null);
            String localizedDesc = cmd.getDesc().get(e);
            embedBuilder.addField(prefix + alias + " " + cmd.getShortUsageInfo().get(e), localizedDesc.isEmpty() ? "-" : localizedDesc + "\n" + cmd.getFullUsageInfo().get(e), false);
            embedBuilder.addField(Message.CMD_HELP_CATEGORY.get(e), cmd.getCommandCategory().getName(), false);
            if (!requiredPermissions.isEmpty()) {
                embedBuilder.addField(Message.CMD_HELP_REQUIRED_PERMS.get(e), requiredPermissions, false);
            }
        }

        if (getArgs().length == 0){
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
                    if (getGuildConfig(e.getGuild()).isDisabled(cmd.getAlias())){
                        continue;
                    }

                    if (cmd.canUseCmd(e.getMember())) {
                        String nsfwString = cmd.isNsfw() ? " " + Message.CMD_NSFW_NOTIFICATION.get(e): "";
                        String guildPrefix = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).getPrefix();
                        commandsAliases.add("`" + guildPrefix + cmd.getAlias() + nsfwString+"`");
                    }
                }

                String commandAliasesFormated = commandsAliases.toString().replaceAll("\\[", "")
                        .replaceAll("\\[", "")
                        .replaceAll("\\]", "")
                        .replaceAll(",", "");

                embedBuilder.addField(cat.getName(),commandAliasesFormated, false);
                embedBuilder.setFooter(getGuildPrefix(e.getGuild()) + getAlias() + " " +Message.CMD_HELP_FOOTER.get(e), "https://images-ext-2.discordapp.net/external/YZ0U9nMuSvG1cb1raXhrkw8Ut8ZBVQT4ia-alVadE7E/https/i.imgur.com/qZe2WZz.jpg");
            }
        }

        e.getChannel().sendMessage(embedBuilder.build()).complete();
        return true;
    }
}
