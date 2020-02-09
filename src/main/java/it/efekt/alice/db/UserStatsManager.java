package it.efekt.alice.db;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.UserStats;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
        Transaction transaction = session.beginTransaction();

        try {
            String hql = "Update UserStats us set us.messagesAmount =(us.messagesAmount + :messageCount) where us.userId =:userId and us.guildId = :guildId";
            Query query = session.createQuery(hql);
            query.setParameter("messageCount", messageCount);
            query.setParameter("userId", userId);
            query.setParameter("guildId", guildId);

            query.executeUpdate();
            transaction.commit();
        } catch (Exception exc){
            if (transaction != null){
                transaction.rollback();
                exc.printStackTrace();
            }
        } finally {
            session.close();
        }
    }

    public UserStats getUserStats(User user, Guild guild){
        Session session = AliceBootstrap.hibernate.getSession();
        Transaction transaction = session.beginTransaction();
        UserStats result = null;

        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UserStats> criteriaQuery = criteriaBuilder.createQuery(UserStats.class);
            Root<UserStats> userStatsRoot = criteriaQuery.from(UserStats.class);

            List<Predicate> conditions = new ArrayList<>();
            conditions.add(criteriaBuilder.equal(userStatsRoot.get("guildId"), guild.getId()));
            conditions.add(criteriaBuilder.equal(userStatsRoot.get("userId"), user.getId()));

            criteriaQuery.select(userStatsRoot).where(conditions.toArray(new Predicate[0]));

            Query<UserStats> query = session.createQuery(criteriaQuery);

            try {
                result = query.getSingleResult();
                transaction.commit();
            } catch (NoResultException exc) {
                transaction.rollback();
                UserStats userStats = new UserStats(user.getId(), guild.getId());
                userStats.save();
                return userStats;
            }
        } catch (Exception exc){
            if (transaction != null){
                transaction.rollback();
                exc.printStackTrace();
            }
        } finally {
            session.close();
        }

        return result;
    }

    public List<UserStats> getUserStats(Guild guild){
        Session session = AliceBootstrap.hibernate.getSession();
        Transaction transaction = session.beginTransaction();
        List<UserStats> results = null;

        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UserStats> criteriaQuery = criteriaBuilder.createQuery(UserStats.class);
            Root<UserStats> userStatsRoot = criteriaQuery.from(UserStats.class);

            List<Predicate> conditions = new ArrayList<>();
            conditions.add(criteriaBuilder.equal(userStatsRoot.get("guildId"), guild.getId()));

            criteriaQuery.select(userStatsRoot).where(conditions.toArray(new Predicate[0]));

            Query<UserStats> query = session.createQuery(criteriaQuery);
            results = query.getResultList();

            transaction.commit();
        } catch (Exception exc){
            if (transaction != null){
                transaction.rollback();
                exc.printStackTrace();
            }
        } finally {
            session.close();
        }

        return results;
    }

}
