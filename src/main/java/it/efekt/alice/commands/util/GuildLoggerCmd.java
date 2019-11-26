package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.GuildConfig;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GuildLoggerCmd extends Command {

    public GuildLoggerCmd(String alias) {
        super(alias);
        addPermission(Permission.ADMINISTRATOR);
        setCategory(CommandCategory.DISCORD_ADMIN_UTILS);
        setDescription(AMessage.CMD_LOGGER_DESC);
        setShortUsageInfo(AMessage.CMD_LOGGER_SHORT_USAGE_INFO);
        setFullUsageInfo(AMessage.CMD_LOGGER_FULL_USAGE_INFO);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        GuildConfig config = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild());

        if (getArgs().length == 0){
            if (config.getLogChannel() == null){
                e.getChannel().sendMessage(AMessage.CMD_LOGGER_NOT_SET.get(e)).complete();
                return true;
            }
            e.getChannel().sendMessage(AMessage.CMD_LOGGER_CURRENTLY_USED.get(e, e.getJDA().getTextChannelById(config.getLogChannel()).getAsMention())).complete();
            return true;
        }

        if (getArgs().length == 1 && !e.getMessage().getMentionedChannels().isEmpty()){
            TextChannel mentionedChannel = e.getMessage().getMentionedChannels().stream().findFirst().get();
            config.setLogChannelAndSave(mentionedChannel.getId());
            e.getChannel().sendMessage(AMessage.CMD_LOGGER_SET.get(e, mentionedChannel.getAsMention())).complete();
            return true;
        } else if(getArgs().length == 1 && getArgs()[0].equalsIgnoreCase("disable")){
            config.setLogChannelAndSave(null);
            e.getChannel().sendMessage(AMessage.CMD_LOGGER_DISABLED.get(e)).complete();
            return true;
        }
        return false;
    }
}
