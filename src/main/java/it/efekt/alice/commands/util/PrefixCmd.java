package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.db.GuildConfig;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PrefixCmd extends Command {

    public PrefixCmd(String alias) {
        super(alias);
        setDescription(Message.CMD_PREFIX_DESC);
        setShortUsageInfo(Message.CMD_PREFIX_SHORT_USAGE_INFO);
        setFullUsageInfo(Message.CMD_PREFIX_FULL_USAGE_INFO);
        addPermission(Permission.ADMINISTRATOR);
        setCategory(CommandCategory.UTILS);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        String prefix = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).getPrefix();
        if (this.getArgs().length == 1){
            String newPrefix = this.getArgs()[0];

            if (newPrefix.length() != 1){
                e.getChannel().sendMessage(Message.CMD_PREFIX_REQUIREMENTS.get(e)).queue();
                return true;
            }

            GuildConfig guildConfig = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild());
            guildConfig.setPrefix(newPrefix);
            guildConfig.save();
            e.getChannel().sendMessage(Message.CMD_NEW_PREFIX_SET.get(e, newPrefix)).queue();
            AliceBootstrap.alice.getGuildLogger().log(e.getGuild(), Message.CMD_CHANGED_PREFIX_LOG.get(e, e.getAuthor().getName(), newPrefix));
            return true;
        }
        return false;
    }
}
