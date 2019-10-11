package it.efekt.alice.listeners;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
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
        System.out.println("fired");
        try {
            User user = e.getUser();
            Guild guild = e.getGuild();

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

            long elapsed = oldGame.getTimestamps().getElapsedTime(ChronoUnit.MINUTES);
            long elapsedMilis = oldGame.getTimestamps().getElapsedTime(ChronoUnit.MILLIS);
//            long sinceStartupTime = System.currentTimeMillis() - AliceBootstrap.STARTUP_TIME;

            if (elapsed >= 1) {
//                if (elapsedMilis > sinceStartupTime){
//                    return;
//                }

                if (lastUpdate.containsKey(guild.getId().concat(user.getId()))){
                    long sinceLastUpdate = System.currentTimeMillis() - lastUpdate.get(guild.getId().concat(user.getId()));
                    logger.debug("sinceLastUpdate: " + sinceLastUpdate);
                    logger.debug("elapsedMillis: " + elapsedMilis);
                    if (elapsedMilis > sinceLastUpdate){
                        logger.debug("not saved");
                        return;
                    }
                }

                Runnable runnable = () -> {
                    AliceBootstrap.alice.getGameStatsManager().addTimePlayed(user, guild, gameName, elapsed);
                    lastUpdate.put(guild.getId().concat(user.getId()), System.currentTimeMillis());
                    logger.debug("saved");
                    logger.debug("user: " + user.getId() + " nick: " + user.getName() + " server: " + guild.getId() + " game: " + gameName + " addedTime: " + elapsed + "min" + " took: " + (System.currentTimeMillis() - beforeTime) + "ms");
                    AliceBootstrap.alice.getGuildLogger().log(e.getGuild(), Message.LOGGER_USER_STOPPED_PLAYING.get(e.getGuild(), user.getName(), gameName, String.valueOf(elapsed)));
                };
                new Thread(runnable).start();

            }
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }
}
