package it.efekt.alice.db;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.TextChannelConfig;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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

public class TextChannelConfigManager {
    private Logger logger = LoggerFactory.getLogger(TextChannelConfigManager.class);

    public TextChannelConfig get(TextChannel textChannel){
        Session session = AliceBootstrap.hibernate.getSession();
        Transaction transaction = session.beginTransaction();
        TextChannelConfig result = null;

        try {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<TextChannelConfig> criteriaQuery = criteriaBuilder.createQuery(TextChannelConfig.class);
            Root<TextChannelConfig> textChannelConfigRoot = criteriaQuery.from(TextChannelConfig.class);

            List<Predicate> conditions = new ArrayList<>();
            conditions.add(criteriaBuilder.equal(textChannelConfigRoot.get("channelId"), textChannel.getId()));

            criteriaQuery.select(textChannelConfigRoot).where(conditions.toArray(new Predicate[0]));

            Query<TextChannelConfig> query = session.createQuery(criteriaQuery);

            try {
                result = query.getSingleResult();
                transaction.commit();
            } catch (NoResultException exc) {
                transaction.rollback();
                TextChannelConfig textChannelConfig = new TextChannelConfig(textChannel.getId());
                textChannelConfig.save();
                return textChannelConfig;
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
