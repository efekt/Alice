package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.GuildConfig;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class GuildLoggerCmd extends Command {

    public GuildLoggerCmd(String alias) {
        super(alias);
        addPermission(Permission.ADMINISTRATOR);
        setCategory(CommandCategory.UTILS);
        setDescription("Ustawia kanał na którym mają zapisywać się logi serwerowe.\n" + "`disable` - wyłącza logi");
        setShortUsageInfo(" `#kanał` lub `disable`");
        setFullUsageInfo("`#kanał` - kanał na którym mają pojawiać się logi\n`disable` - wyłącza logowanie");
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        GuildConfig config = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild());

        if (getArgs().length == 0){
            if (config.getLogChannel() == null){
                e.getChannel().sendMessage("Nie ustawiono kanału logów").queue();
                return true;
            }
            e.getChannel().sendMessage("Aktualnie używany kanał logów: " + e.getJDA().getTextChannelById(config.getLogChannel()).getAsMention()).queue();
            return true;
        }

        if (getArgs().length == 1 && !e.getMessage().getMentionedChannels().isEmpty()){
            TextChannel mentionedChannel = e.getMessage().getMentionedChannels().stream().findFirst().get();
            config.setLogChannel(mentionedChannel.getId());
            config.save();
            e.getChannel().sendMessage("Ustawiono kanał logów na: " + mentionedChannel.getAsMention()).queue();
            return true;
        } else if(getArgs().length == 1 && getArgs()[0].equalsIgnoreCase("disable")){
            config.setLogChannel(null);
            config.save();
            e.getChannel().sendMessage("Wyłączono logi na tym serwerze").queue();
            return true;
        }
        return false;
    }
}
