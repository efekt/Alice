package it.efekt.alice.db;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.GuildConfig;
import net.dv8tion.jda.api.entities.Guild;
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

public class GuildConfigManager {
    private Logger logger = LoggerFactory.getLogger(GuildConfigManager.class);

    public GuildConfig getGuildConfig(Guild guild){
        Session session = AliceBootstrap.hibernate.getSession();
        Transaction transaction = session.getTransaction();
        GuildConfig result = null;

        try {
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GuildConfig> criteriaQuery = criteriaBuilder.createQuery(GuildConfig.class);
            Root<GuildConfig> guildConfigRoot = criteriaQuery.from(GuildConfig.class);

            List<Predicate> conditions = new ArrayList<>();
            conditions.add(criteriaBuilder.equal(guildConfigRoot.get("id"), guild.getId()));

            criteriaQuery.select(guildConfigRoot).where(conditions.toArray(new Predicate[0]));

            Query<GuildConfig> query = session.createQuery(criteriaQuery);


            try {
                result = query.getSingleResult();
                transaction.commit();
            } catch (NoResultException exc) {
                transaction.rollback();
                GuildConfig guildConfig = new GuildConfig(guild.getId(), AliceBootstrap.DEFAULT_PREFIX);
                guildConfig.save();
                return guildConfig;
            }
        } catch (Exception e){
            if (transaction != null){
                transaction.rollback();
                e.printStackTrace();
            }
        } finally {
            session.close();
        }
        session.close();
        return result;
    }

    public boolean hasConfig(String guildId){
        Session session = AliceBootstrap.hibernate.getSession();
        Transaction transaction = session.beginTransaction();
        boolean result = false;

        try {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GuildConfig> criteriaQuery = criteriaBuilder.createQuery(GuildConfig.class);
            Root<GuildConfig> guildConfigRoot = criteriaQuery.from(GuildConfig.class);

            List<Predicate> conditions = new ArrayList<>();
            conditions.add(criteriaBuilder.equal(guildConfigRoot.get("id"), guildId));

            criteriaQuery.select(guildConfigRoot).where(conditions.toArray(new Predicate[0]));

            Query<GuildConfig> query = session.createQuery(criteriaQuery);

            try {
                query.getSingleResult();
                transaction.commit();
                result = true;
            } catch (NoResultException exc) {
                result = false;
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
}
