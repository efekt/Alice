package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.GuildConfig;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class GuildLoggerCmd extends Command {

    public GuildLoggerCmd(String alias) {
        super(alias);
        addPermission(Permission.ADMINISTRATOR);
        setCategory(CommandCategory.DISCORD_ADMIN_UTILS);
        setDescription(AMessage.CMD_LOGGER_DESC);
        setShortUsageInfo(AMessage.CMD_LOGGER_SHORT_USAGE_INFO);
        setFullUsageInfo(AMessage.CMD_LOGGER_FULL_USAGE_INFO);

        optionData.add(new OptionData(OptionType.STRING, "channel", "channel", false));
        setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        GuildConfig config = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild());

        if (getArgs().length == 0){
            if (config.getLogChannel() == null){
                e.sendMessageToChannel(AMessage.CMD_LOGGER_NOT_SET.get(e));
                return true;
            }
            e.sendMessageToChannel(AMessage.CMD_LOGGER_CURRENTLY_USED.get(e, e.getJDA().getTextChannelById(config.getLogChannel()).getAsMention()));
            return true;
        }

        if (getArgs().length == 1 && !e.getMentions().getChannels().isEmpty()){
            GuildChannel mentionedChannel = e.getMentions().getChannels().stream().findFirst().get();
            config.setLogChannelAndSave(mentionedChannel.getId());
            e.sendMessageToChannel(AMessage.CMD_LOGGER_SET.get(e, mentionedChannel.getAsMention()));
            return true;
        } else if(getArgs().length == 1 && getArgs()[0].equalsIgnoreCase("disable")){
            config.setLogChannelAndSave(null);
            e.sendMessageToChannel(AMessage.CMD_LOGGER_DISABLED.get(e));
            return true;
        }
        return false;
    }
}
