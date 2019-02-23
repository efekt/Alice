package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.db.GuildConfig;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PrefixCmd extends Command {

    public PrefixCmd(String alias) {
        super(alias);
        setDescription("Ustawia prefix, który będzie używany na tym serwerze");
        setShortUsageInfo(" `prefix`");
        setFullUsageInfo("`prefix` - twój nowy wymarzony prefix");
        addPermission(Permission.ADMINISTRATOR);
        setCategory(CommandCategory.UTILS);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        String prefix = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).getPrefix();
        if (this.getArgs().length == 1){
            String newPrefix = this.getArgs()[0];

            if (newPrefix.length() != 1){
                e.getChannel().sendMessage("Prefix musi składać się z jednego znaku").queue();
                return true;
            }

            GuildConfig guildConfig = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild());
            guildConfig.setPrefix(newPrefix);
            guildConfig.save();
            e.getChannel().sendMessage("Nowy prefix dla tego serwera: " + newPrefix).queue();
            AliceBootstrap.alice.getGuildLogger().log(e.getGuild(), e.getAuthor().getAsMention() + " zmienił prefix na: " + newPrefix);
            return true;
        } else {
            e.getChannel().sendMessage("Użyj " + prefix + "prefix <NowyPrefix>").queue();
            return true;
        }
    }
}
