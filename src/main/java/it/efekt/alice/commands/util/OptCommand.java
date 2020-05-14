package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.UserStats;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class OptCommand extends Command {

    public OptCommand(String alias) {
        super(alias);
        setCategory(CommandCategory.UTILS);
        setDescription(AMessage.CMD_OPTOUT_DESC);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        Guild guild = e.getGuild();

        if (getArgs().length == 0){
            UserStats userStats = AliceBootstrap.alice.getUserStatsManager().getUserStats(e.getAuthor(), e.getGuild());
            e.getTextChannel().sendMessage(getOptOutEmbed(guild, userStats)).complete();
            return true;
        }

        if (getArgs().length != 1){
            return false;
        }

        String feature = getArgs()[0];

        UserStats userStats = AliceBootstrap.alice.getUserStatsManager().getUserStats(e.getAuthor(), e.getGuild());

        switch(feature){
            case "logger":
                userStats.setLoggerOptedOut(!userStats.isLoggerOptedOut());
                e.getTextChannel().sendMessage(getOptOutEmbed(guild, userStats)).complete();
                userStats.save();
                return true;

            case "gameStats":
                userStats.setGameStatsOptedOut(!userStats.isGameStatsOptedOut());
                e.getTextChannel().sendMessage(getOptOutEmbed(guild, userStats)).complete();
                userStats.save();
                return true;

            case "spamLvl":
                userStats.setSpamLvlOptedOut(!userStats.isSpamLvlOptedOut());
                e.getTextChannel().sendMessage(getOptOutEmbed(guild, userStats)).complete();
                userStats.save();
                return true;

            default:
                return false;
        }
    }

    private MessageEmbed getOptOutEmbed(Guild guild, UserStats userStats){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String guildPrefix = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(guild).getPrefix();
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
        embedBuilder.setTitle(AMessage.CMD_OPTOUT_EMBED_TITLE.get(guild));
        embedBuilder.setDescription(AMessage.CMD_OPTOUT_EMBED_DESC.get(guild));

        String loggerCmd = "`"+guildPrefix+"opt logger`";
        String gameStatsCmd = "`"+guildPrefix+"opt gameStats`";
        String spamLvlCmd = "`"+guildPrefix+"opt spamLvl`";

        embedBuilder.addField("Logger", userStats.isLoggerOptedOut() ? ":x:\n" + loggerCmd : ":white_check_mark:\n" + loggerCmd, true);
        embedBuilder.addField("Game Stats", userStats.isGameStatsOptedOut() ? ":x:\n" + gameStatsCmd : ":white_check_mark:\n" + gameStatsCmd, true);
        embedBuilder.addField("Spam Lvl", userStats.isSpamLvlOptedOut() ? ":x:\n" + spamLvlCmd : ":white_check_mark:\n" + spamLvlCmd, true);
        return embedBuilder.build();
    }

}
