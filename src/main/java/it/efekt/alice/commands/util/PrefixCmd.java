package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.GuildConfig;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PrefixCmd extends Command {

    public PrefixCmd(String alias) {
        super(alias);
        setDescription(AMessage.CMD_PREFIX_DESC);
        setShortUsageInfo(AMessage.CMD_PREFIX_SHORT_USAGE_INFO);
        setFullUsageInfo(AMessage.CMD_PREFIX_FULL_USAGE_INFO);
        addPermission(Permission.ADMINISTRATOR);
        setCategory(CommandCategory.DISCORD_ADMIN_UTILS);
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        String prefix = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).getPrefix();
        if (this.getArgs().length == 1){
            String newPrefix = this.getArgs()[0];

            if (newPrefix.length() != 1){
                e.getChannel().sendMessage(AMessage.CMD_PREFIX_REQUIREMENTS.get(e)).complete();
                return true;
            }

            GuildConfig guildConfig = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild());
            guildConfig.setPrefix(newPrefix);
            guildConfig.save();
            e.getChannel().sendMessage(AMessage.CMD_NEW_PREFIX_SET.get(e, newPrefix)).complete();
            AliceBootstrap.alice.getGuildLogger().log(e.getGuild(), AMessage.CMD_CHANGED_PREFIX_LOG.get(e, e.getUser().getName(), newPrefix));
            return true;
        }
        return false;
    }
}
