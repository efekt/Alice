package it.efekt.alice.db;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.GameStats;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameStatsManager {
    private Logger logger = LoggerFactory.getLogger(GameStatsManager.class);

    public void addTimePlayed(User user, Guild guild, String gameName, long time){
        String userId = user.getId();
        String guildId = guild.getId();

        Session session = AliceBootstrap.hibernate.getSession();
        session.beginTransaction();

        String hql = "Update GameStats gs set gs.timePlayed =(gs.timePlayed + :time) where gs.userId =:userId and gs.guildId = :guildId and gs.gameName = :gameName";
        Query query = session.createQuery(hql);
        query.setParameter("time", time);
        query.setParameter("userId", userId);
        query.setParameter("guildId", guildId);
        query.setParameter("gameName", gameName);

        query.executeUpdate();
        session.getTransaction().commit();
    }

    public GameStats getGameStats(User user, Guild guild, String gameName){
        Session session = AliceBootstrap.hibernate.getSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GameStats> criteriaQuery = criteriaBuilder.createQuery(GameStats.class);
        Root<GameStats> gameStatsRoot = criteriaQuery.from(GameStats.class);

        List<Predicate> conditions = new ArrayList<>();
        conditions.add(criteriaBuilder.equal(gameStatsRoot.get("guildId"), guild.getId()));
        conditions.add(criteriaBuilder.equal(gameStatsRoot.get("userId"), user.getId()));
        conditions.add(criteriaBuilder.equal(gameStatsRoot.get("gameName"), gameName));

        criteriaQuery.select(gameStatsRoot).where(conditions.toArray(new Predicate[0]));

        Query<GameStats> query = session.createQuery(criteriaQuery);
        GameStats result;

        try {
            result = query.getSingleResult();
            session.getTransaction().commit();
        } catch (NoResultException exc){
            session.getTransaction().rollback();
            GameStats gameStats = new GameStats(user.getId(), guild.getId(), gameName);
            gameStats.save();
            return gameStats;
        }

        return result;
    }

    public List<GameStats> getGameStats(Guild guild){
        Session session = AliceBootstrap.hibernate.getSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GameStats> criteriaQuery = criteriaBuilder.createQuery(GameStats.class);
        Root<GameStats> gameStatsRoot = criteriaQuery.from(GameStats.class);

        List<Predicate> conditions = new ArrayList<>();
        conditions.add(criteriaBuilder.equal(gameStatsRoot.get("guildId"), guild.getId()));

        criteriaQuery.select(gameStatsRoot).where(conditions.toArray(new Predicate[0]));

        Query<GameStats> query = session.createQuery(criteriaQuery);
        List<GameStats> results = query.getResultList();

        session.getTransaction().commit();

        return results;
    }

    public HashMap<String, Long> getAllGameTimeStats(){
        HashMap<String, Long> gameTimes = new HashMap<>();
        Session session = AliceBootstrap.hibernate.getSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GameStats> criteriaQuery = criteriaBuilder.createQuery(GameStats.class);
        Root<GameStats> gameStatsRoot = criteriaQuery.from(GameStats.class);

        criteriaQuery.select(gameStatsRoot);

        Query<GameStats> query = session.createQuery(criteriaQuery);
        List<GameStats> results = query.getResultList();
        session.getTransaction().commit();

        for (GameStats gameStats : results){
            if (gameTimes.containsKey(gameStats.getGameName())){
                gameTimes.put(gameStats.getGameName(), gameTimes.get(gameStats.getGameName()) + gameStats.getTimePlayed());
            } else {
                gameTimes.put(gameStats.getGameName(), gameStats.getTimePlayed());
            }
        }

        return gameTimes;
    }

    public HashMap<String, Long> getGameTimesOnGuild(Guild guild){
        HashMap<String, Long> gameTimes = new HashMap<>();
        Session session = AliceBootstrap.hibernate.getSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GameStats> criteriaQuery = criteriaBuilder.createQuery(GameStats.class);
        Root<GameStats> gameStatsRoot = criteriaQuery.from(GameStats.class);

        List<Predicate> conditions = new ArrayList<>();
        conditions.add(criteriaBuilder.equal(gameStatsRoot.get("guildId"), guild.getId()));

        criteriaQuery.select(gameStatsRoot).where(conditions.toArray(new Predicate[0]));

        Query<GameStats> query = session.createQuery(criteriaQuery);
        List<GameStats> results = query.getResultList();
        session.getTransaction().commit();

        for (GameStats gameStats : results){
            if (gameTimes.containsKey(gameStats.getGameName())){
                gameTimes.put(gameStats.getGameName(), gameTimes.get(gameStats.getGameName()) + gameStats.getTimePlayed());
            } else {
                gameTimes.put(gameStats.getGameName(), gameStats.getTimePlayed());
            }
        }

        return gameTimes;
    }
}
