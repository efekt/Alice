package it.efekt.alice.db;

import it.efekt.alice.core.AliceBootstrap;
import org.hibernate.Session;

public abstract class AliceDb {
    public void save(){
        Session session = AliceBootstrap.hibernate.getSession();
        session.beginTransaction();
        session.saveOrUpdate(this);
        session.getTransaction().commit();
    }

    public boolean remove(){
        Session session = AliceBootstrap.hibernate.getSession();
        session.beginTransaction();
        session.remove(this);
        session.getTransaction().commit();
        return true;
    }
}
