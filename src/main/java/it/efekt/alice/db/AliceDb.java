package it.efekt.alice.db;

import it.efekt.alice.core.AliceBootstrap;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class AliceDb {
    public void save(){
        Session session = AliceBootstrap.hibernate.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.saveOrUpdate(this);
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

    public boolean remove(){
        Session session = AliceBootstrap.hibernate.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.remove(this);
            transaction.commit();
            return true;
        } catch (Exception exc){
            if (transaction != null){
                transaction.rollback();
                exc.printStackTrace();
            }
        } finally {
            session.close();
        }
        return false;
    }
}
