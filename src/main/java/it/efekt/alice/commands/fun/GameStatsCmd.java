package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameStatsCmd extends Command {
    public GameStatsCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.FUN);
        setDescription(Message.CMD_GAMESTATS_DESC);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        Guild guild = e.getGuild();
        HashMap<String, Long> guildGameStats = AliceBootstrap.alice.getGameStatsManager().getGameTimesOnGuild(guild);
        LinkedHashMap<String, Long> sorted = guildGameStats.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String,Long>::getValue).thenComparing(Map.Entry::getKey).reversed())
                .collect(LinkedHashMap::new,(map,entry) -> map.put(entry.getKey(),entry.getValue()),LinkedHashMap::putAll);
        String output = "";
        int i = 1;
        int maxToPrint = 20;
        for (Map.Entry<String, Long> entry : sorted.entrySet()){
            String gameName = entry.getKey();
            long timePlayed = entry.getValue();
            long hoursPlayed = TimeUnit.SECONDS.toHours(timePlayed);
            GregorianCalendar cal = new GregorianCalendar(0,0,0,0,0, (int)timePlayed);
            Date dateNow = cal.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH'h' mm'm'");

            if (hoursPlayed >= 0.5){
                output = output.concat("`"+i+"` " + gameName + ": " + dateFormat.format(dateNow) + "\n");
            }
            if (i >= maxToPrint){
                break;
            }

            i++;
        }
        e.getChannel().sendMessage(output).complete();
        return true;
    }
}
