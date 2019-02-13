package it.efekt.alice.modules;

import it.efekt.alice.core.Alice;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.UserStats;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class UserStatsManager {
    private Alice alice;
    private List<UserStats> userStats = new ArrayList<>();

    public UserStatsManager(Alice alice){
        this.alice = alice;
        loadUserStats();
    }

    public void loadUserStats(){
        Session session = AliceBootstrap.sessionFactory.getCurrentSession();
        session.beginTransaction();
        this.userStats.addAll(session.createQuery("from UserStats").getResultList());
        session.getTransaction().commit();
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

}
