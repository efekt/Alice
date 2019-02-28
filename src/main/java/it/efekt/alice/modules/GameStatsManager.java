package it.efekt.alice.modules;

import it.efekt.alice.core.Alice;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.GameStats;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class GameStatsManager {
    private Alice alice;
    private List<GameStats> gameStats = new ArrayList<>();

    public GameStatsManager(Alice alice){
        this.alice = alice;
        loadGameStats();
    }

    public void loadGameStats(){
        Session session = AliceBootstrap.hibernate.getSession();
        session.beginTransaction();
        this.gameStats.addAll(session.createQuery("from GameStats").getResultList());
        session.getTransaction().commit();
    }

    public void saveAllUserStats(){
        gameStats.forEach(GameStats::save);
    }

    public GameStats getGameStats(User user, Guild guild, String gameName){
        for (GameStats gameStats : this.gameStats){
            if (gameStats.getGuildId().equalsIgnoreCase(guild.getId()) && gameStats.getUserId().equalsIgnoreCase(user.getId()) && gameStats.getGameName().equalsIgnoreCase(gameName)){
                return gameStats;
            }
        }
        GameStats newGameStats = new GameStats(user.getId(), guild.getId(), gameName);
        this.gameStats.add(newGameStats);
        return newGameStats;
    }

    public List<GameStats> getGameStats(Guild guild){
        List<GameStats> stats = new ArrayList<>();
        for (GameStats gameStats : this.gameStats){
            if (gameStats.getGuildId().equalsIgnoreCase(guild.getId())){
                stats.add(gameStats);
            }
        }

        return stats;
    }

    public void removeAllInvalidUsers(){
        this.gameStats.removeIf(GameStats::isInvalidUser);
    }

    public void clearAll(){
        this.gameStats.clear();
    }
}
