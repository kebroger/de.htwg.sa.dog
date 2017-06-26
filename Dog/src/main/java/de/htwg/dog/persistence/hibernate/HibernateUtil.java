package de.htwg.dog.persistence.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory;

	static {
		 final Configuration cfg = new
				 Configuration();
			      cfg.configure("/hibernate.cfg.xml");
			      sessionFactory = cfg.buildSessionFactory();
	}
	
	private HibernateUtil() {
	}
	
	public static SessionFactory getInstance() {
		return sessionFactory;
	}

}
