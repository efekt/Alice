package it.efekt.alice.db;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.UserStats;
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
import java.util.List;

public class UserStatsManager {
    private Logger logger = LoggerFactory.getLogger(UserStatsManager.class);

    public void addMessageCount(User user, Guild guild, int messageCount){
        String userId = user.getId();
        String guildId = guild.getId();

        Session session = AliceBootstrap.hibernate.getSession();
        session.beginTransaction();

        String hql = "Update UserStats us set us.messagesAmount =(us.messagesAmount + :messageCount) where us.userId =:userId and us.guildId = :guildId";
        Query query = session.createQuery(hql);
        query.setParameter("messageCount", messageCount);
        query.setParameter("userId", userId);
        query.setParameter("guildId", guildId);

        query.executeUpdate();
        session.getTransaction().commit();
    }

    public UserStats getUserStats(User user, Guild guild){
        Session session = AliceBootstrap.hibernate.getSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<UserStats> criteriaQuery = criteriaBuilder.createQuery(UserStats.class);
        Root<UserStats> userStatsRoot = criteriaQuery.from(UserStats.class);

        List<Predicate> conditions = new ArrayList<>();
        conditions.add(criteriaBuilder.equal(userStatsRoot.get("guildId"), guild.getId()));
        conditions.add(criteriaBuilder.equal(userStatsRoot.get("userId"), user.getId()));

        criteriaQuery.select(userStatsRoot).where(conditions.toArray(new Predicate[0]));

        Query<UserStats> query = session.createQuery(criteriaQuery);
        UserStats result;

        try {
            result = query.getSingleResult();
            session.getTransaction().commit();
        } catch (NoResultException exc){
            session.getTransaction().rollback();
            UserStats userStats = new UserStats(user.getId(), guild.getId());
            userStats.save();
            return userStats;
        }
        return result;
    }

    public List<UserStats> getUserStats(Guild guild){
        Session session = AliceBootstrap.hibernate.getSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<UserStats> criteriaQuery = criteriaBuilder.createQuery(UserStats.class);
        Root<UserStats> userStatsRoot = criteriaQuery.from(UserStats.class);

        List<Predicate> conditions = new ArrayList<>();
        conditions.add(criteriaBuilder.equal(userStatsRoot.get("guildId"), guild.getId()));

        criteriaQuery.select(userStatsRoot).where(conditions.toArray(new Predicate[0]));

        Query<UserStats> query = session.createQuery(criteriaQuery);
        List<UserStats> results = query.getResultList();

        session.getTransaction().commit();

        return results;
    }

}
