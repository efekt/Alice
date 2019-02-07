package it.efekt.alice.modules;

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
        setCategory(CommandCategory.BLANK);
        setDescription("Ustawia kanał na którym mają zapisywać się logi serwerowe");
        setUsageInfo(" <#kanał>");
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        GuildConfig config = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild());

        if (getArgs().length == 0){
            if (config.getLogChannel() == null){
                e.getChannel().sendMessage("Nie ustawiono kanału logów").queue();
            }
            e.getChannel().sendMessage("Aktualnie używany kanał logów: " + e.getJDA().getTextChannelById(config.getLogChannel()).getAsMention()).queue();
        }

        if (getArgs().length == 1 && !e.getMessage().getMentionedChannels().isEmpty()){
            TextChannel mentionedChannel = e.getMessage().getMentionedChannels().stream().findFirst().get();
            // Zapisz channel id w setId config
            config.setLogChannel(mentionedChannel.getId());
            config.save();
            e.getChannel().sendMessage("Ustawiono kanał logów na: " + mentionedChannel.getAsMention()).queue();
        }
    }
}
