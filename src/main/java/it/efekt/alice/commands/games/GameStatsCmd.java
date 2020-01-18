package it.efekt.alice.commands.games;

import com.google.gson.Gson;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameStatsCmd extends Command {
    private Logger logger = LoggerFactory.getLogger(GameStatsCmd.class);
    private List<String> blacklist = new ArrayList<>();
    private final String BLACKLIST_PATH = "./games_blacklist.json";
    private final int MIN_TIME_PLAYED = 30;
    private final int MAX_TO_PRINT = 15;

    public GameStatsCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.GAMES);
        setDescription(AMessage.CMD_GAMESTATS_DESC);
        try {
            loadBlacklist();
        } catch (FileNotFoundException e) {
            logger.info("couldn't find " + BLACKLIST_PATH + " file");
        }
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        long timeBefore = System.currentTimeMillis();
        Guild guild = e.getGuild();
        HashMap<String, Long> guildGameStats = AliceBootstrap.alice.getGameStatsManager().getGameTimesOnGuild(guild);
        int page = 1;
        String userName = null;

        if (getArgs().length == 1 && getArgs()[0].matches("-?\\d+")) {
            page = Integer.parseInt(getArgs()[0]);

        }

        if (getArgs().length >= 1 && getArgs()[0].equalsIgnoreCase("all")){
            if (getArgs().length == 2 && getArgs()[1].matches("-?\\d+")) {
                page = Integer.parseInt(getArgs()[1]);
            }
            guildGameStats = AliceBootstrap.alice.getGameStatsManager().getAllGameTimeStats();
        }

        // checking user's stats
        if (getArgs().length >= 1 && !e.getMessage().getMentionedUsers().isEmpty()){
            User user = e.getMessage().getMentionedUsers().get(0);
            guildGameStats = AliceBootstrap.alice.getGameStatsManager().getGameStats(e.getGuild(), user);
            userName = user.getName();
            // if page number is given
            if (getArgs().length >= 2 && getArgs()[1].matches("-?\\d+")){
                page = Integer.parseInt(getArgs()[1]);
            }
        }

        if (guildGameStats.isEmpty()){
            e.getChannel().sendMessage(AMessage.CMD_TOP_NOTHING_FOUND.get(e)).complete();
            return true;
        }

        LinkedHashMap<String, Long> sorted = guildGameStats.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String,Long>::getValue).thenComparing(Map.Entry::getKey).reversed())
                .collect(LinkedHashMap::new,(map,entry) -> map.put(entry.getKey(),entry.getValue()),LinkedHashMap::putAll);
        String output = "";
        List<String> gamesList = new ArrayList<>();
        sorted.forEach((key, value) -> {
                    for (String blacklistItem : this.blacklist){
                        if (blacklistItem.equalsIgnoreCase(key)){
                            return;
                        }
                    }
                    gamesList.add(key);
                }
        );
        int beginIndex = (page - 1) * MAX_TO_PRINT; // Begin index

        int maxPages = (int) Math.ceil((float)gamesList.size() / (float)MAX_TO_PRINT);

        if (page <= 0 || gamesList.size() < beginIndex){
            e.getChannel().sendMessage(AMessage.CMD_TOP_WRONG_PAGE.get(e, String.valueOf(maxPages))).complete();
            return true;
        }

        List<String> subList = gamesList.subList(beginIndex, Math.min(beginIndex + MAX_TO_PRINT, gamesList.size()));


        for (String entry : subList){
            beginIndex++;
            String gameName = entry;
            long timePlayed = sorted.get(entry);
            long day = TimeUnit.MINUTES.toDays(timePlayed);
            long hoursPlayed = TimeUnit.MINUTES.toHours(timePlayed) - (day * 24);
            long minutesPlayed = timePlayed - (TimeUnit.MINUTES.toHours(timePlayed) * 60);

            // Print only if timePlayed is larger than 30 minutes
            //if (timePlayed >= MIN_TIME_PLAYED){
                gameName = gameName.length()>35 ? gameName.substring(0, 30).concat("...") : gameName;
                output = output.concat("**"+beginIndex+".** **" + gameName + "**: _" + day + "d " + hoursPlayed + "h " + minutesPlayed + "m " + "_\n");
            //}
            if (beginIndex >= MAX_TO_PRINT * page){
                break;
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
        // if userName is not set = no user was provided with a mention
        if (userName == null) {
            embedBuilder.setTitle(AMessage.CMD_GAMESTATS_EMBED_TITLE.get(e));
        } else {
            embedBuilder.setTitle(AMessage.CMD_GAMESTATS_EMBED_TITLE_USER.get(e, userName));
        }
        embedBuilder.addField(AMessage.CMD_TOP_FOOTER.get(e, String.valueOf(beginIndex)), output, false);
        embedBuilder.setFooter(AMessage.CMD_GAMESTATS_PAGE.get(e, String.valueOf(page), String.valueOf(maxPages)), e.getJDA().getSelfUser().getEffectiveAvatarUrl());
        logger.info(guildGameStats.size() + " GameStats gathered and sorted in: " + (System.currentTimeMillis() - timeBefore) + "ms");
        e.getChannel().sendMessage(embedBuilder.build()).complete();
        return true;
    }

    public void loadBlacklist() throws FileNotFoundException{
        logger.info("loading games blacklist");
        this.blacklist.clear();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(BLACKLIST_PATH));
        Gson gson = new Gson();
        this.blacklist = gson.fromJson(bufferedReader, ArrayList.class);
        logger.info("loaded " + this.blacklist.size() + " blacklisted game names");
    }

}
