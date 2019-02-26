package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.GuildConfig;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class GuildLoggerCmd extends Command {

    public GuildLoggerCmd(String alias) {
        super(alias);
        addPermission(Permission.ADMINISTRATOR);
        setCategory(CommandCategory.UTILS);
        setDescription(Message.CMD_LOGGER_DESC);
        setShortUsageInfo(Message.CMD_LOGGER_SHORT_USAGE_INFO);
        setFullUsageInfo(Message.CMD_LOGGER_FULL_USAGE_INFO);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        GuildConfig config = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild());

        if (getArgs().length == 0){
            if (config.getLogChannel() == null){
                e.getChannel().sendMessage(Message.CMD_LOGGER_NOT_SET.get(e)).queue();
                return true;
            }
            e.getChannel().sendMessage(Message.CMD_LOGGER_CURRENTLY_USED.get(e, e.getJDA().getTextChannelById(config.getLogChannel()).getAsMention())).queue();
            return true;
        }

        if (getArgs().length == 1 && !e.getMessage().getMentionedChannels().isEmpty()){
            TextChannel mentionedChannel = e.getMessage().getMentionedChannels().stream().findFirst().get();
            config.setLogChannel(mentionedChannel.getId());
            config.save();
            e.getChannel().sendMessage(Message.CMD_LOGGER_SET.get(e, mentionedChannel.getAsMention())).queue();
            return true;
        } else if(getArgs().length == 1 && getArgs()[0].equalsIgnoreCase("disable")){
            config.setLogChannel(null);
            config.save();
            e.getChannel().sendMessage(Message.CMD_LOGGER_DISABLED.get(e)).queue();
            return true;
        }
        return false;
    }
}
