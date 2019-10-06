package it.efekt.alice.db;

import it.efekt.alice.core.Alice;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.UserStats;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class UserStatsManager {
    private Logger logger = LoggerFactory.getLogger(UserStatsManager.class);
    private Alice alice;
    private List<UserStats> userStats = new ArrayList<>();

    public UserStatsManager(Alice alice){
        this.alice = alice;
        loadUserStats();
    }

    public void loadUserStats(){
        logger.info("Loading user stats...");
        Session session = AliceBootstrap.hibernate.getSession();
        session.beginTransaction();
        this.userStats.addAll(session.createQuery("from UserStats").getResultList());
        session.getTransaction().commit();
        logger.info("Loaded all " + this.userStats.size() + " user stats");
    }

    public void saveAllUserStats(){
        userStats.forEach(UserStats::save);
    }

    public UserStats getUserStats(User user, Guild guild){
        for (UserStats userStats : this.userStats){
            if (userStats.getGuildId().equalsIgnoreCase(guild.getId()) && userStats.getUserId().equalsIgnoreCase(user.getId())){
                return userStats;
            }
        }
        UserStats newUserStats = new UserStats(user.getId(), guild.getId());
        this.userStats.add(newUserStats);
        return newUserStats;
    }

    public List<UserStats> getUserStats(Guild guild){
        List<UserStats> stats = new ArrayList<>();
        for (UserStats userStats : this.userStats){
            if (userStats.getGuildId().equalsIgnoreCase(guild.getId())){
                stats.add(userStats);
            }
        }

        return stats;
    }

    public void removeAllInvalidUsers(){
        this.userStats.removeIf(UserStats::isInvalidUser);
    }

    public void removeAll(Guild guild){
        this.userStats.removeIf(uStats -> uStats.getGuildId().equals(guild.getId()) && uStats.remove());
    }
}
