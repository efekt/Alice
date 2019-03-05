package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameStatsCmd extends Command {
    private final int MIN_TIME_PLAYED = 30;
    private final int MAX_TO_PRINT = 20;

    public GameStatsCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.GAMES);
        setDescription(Message.CMD_GAMESTATS_DESC);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        Guild guild = e.getGuild();
        HashMap<String, Long> guildGameStats = AliceBootstrap.alice.getGameStatsManager().getGameTimesOnGuild(guild);

        if (guildGameStats.isEmpty()){
            e.getChannel().sendMessage(Message.CMD_GAMESTATS_NOT_FOUND.get(e)).complete();
            return true;
        }

        LinkedHashMap<String, Long> sorted = guildGameStats.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String,Long>::getValue).thenComparing(Map.Entry::getKey).reversed())
                .collect(LinkedHashMap::new,(map,entry) -> map.put(entry.getKey(),entry.getValue()),LinkedHashMap::putAll);
        String output = "";
        int i = 1;
        for (Map.Entry<String, Long> entry : sorted.entrySet()){
            String gameName = entry.getKey();
            long timePlayed = entry.getValue();
            long day = TimeUnit.MINUTES.toDays(timePlayed);
            long hoursPlayed = TimeUnit.MINUTES.toHours(timePlayed) - (day * 24);
            long minutesPlayed = timePlayed - (TimeUnit.MINUTES.toHours(timePlayed) * 60);

            // Print only if timePlayed is larger than 30 minutes
            if (timePlayed >= MIN_TIME_PLAYED){
                output = output.concat("**"+i+".** **" + gameName + "**: _" + day + "d " + hoursPlayed + "h " + minutesPlayed + "m " + "_\n");
            }
            if (i >= MAX_TO_PRINT){
                break;
            }
            i++;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
        embedBuilder.setTitle("Most played games on this server");
        embedBuilder.addField("TOP-" + i, output, false);
        e.getChannel().sendMessage(embedBuilder.build()).complete();
        return true;
    }
}
