package it.efekt.alice.listeners;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.GameStats;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;

//todo save only alfanumeric strings!
public class GameListener extends ListenerAdapter {
    private Logger logger = LoggerFactory.getLogger(GameListener.class);
    private HashMap<String, Long> lastUpdate = new HashMap<>();

    @Override
    public void onUserActivityEnd(UserActivityEndEvent e) {
        long beforeTime = System.currentTimeMillis();
        try {
            User user = e.getUser();
            Guild guild = e.getGuild();

            if (AliceBootstrap.alice.getUserStatsManager().getUserStats(user, guild).isGameStatsOptedOut()){
                return;
            }

            Activity oldGame;

            try {
                oldGame = e.getOldActivity();
            } catch (NullPointerException exc){
                exc.printStackTrace();
                return;
            }

            if (user.isBot()) {
                return;
            }

            if (!oldGame.getType().equals(Activity.ActivityType.PLAYING)){
                return;
            }

            if (!e.getMember().getOnlineStatus().equals(OnlineStatus.ONLINE)){
                return;
            }

            if (e.getMember().getActivities().stream().anyMatch(activity -> activity.getType().equals(oldGame.getType()))){
                return;
            }

            if (oldGame.getTimestamps() == null) {
                return;
            }

            String gameName = oldGame.getName();

            if (gameName.length() > 128){
                return;
            }

            long elapsed = oldGame.getTimestamps().getElapsedTime(ChronoUnit.MINUTES);
            long elapsedMilis = oldGame.getTimestamps().getElapsedTime(ChronoUnit.MILLIS);
//            long sinceStartupTime = System.currentTimeMillis() - AliceBootstrap.STARTUP_TIME;

            if (elapsed >= 0) {
//                if (elapsedMilis > sinceStartupTime){
//                    return;
//                }

                if (lastUpdate.containsKey(guild.getId().concat(user.getId()))){
                    long sinceLastUpdate = System.currentTimeMillis() - lastUpdate.get(guild.getId().concat(user.getId()));
                    //logger.debug("sinceLastUpdate: " + sinceLastUpdate);
                    //logger.debug("elapsedMillis: " + elapsedMilis);
                    if (elapsedMilis > sinceLastUpdate){
                        //logger.debug("not saved");
                        return;
                    }
                }


                    //AliceBootstrap.alice.getGameStatsManager().addTimePlayed(user, guild, gameName, elapsed);
                    GameStats gameStats = AliceBootstrap.alice.getGameStatsManager().getGameStats(user, guild, gameName);
                    gameStats.addTimePlayed(elapsed);
                    gameStats.save();
                    lastUpdate.put(guild.getId().concat(user.getId()), System.currentTimeMillis());
                    //logger.debug("saved");
                    //logger.debug("user: " + user.getId() + " nick: " + user.getName() + " server: " + guild.getId() + " game: " + gameName + " addedTime: " + elapsed + "min" + " took: " + (System.currentTimeMillis() - beforeTime) + "ms");
                    AliceBootstrap.alice.getGuildLogger().log(e.getGuild(), AMessage.LOGGER_USER_STOPPED_PLAYING.get(e.getGuild(), user.getName(), gameName, String.valueOf(elapsed)));


            }
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }
}
