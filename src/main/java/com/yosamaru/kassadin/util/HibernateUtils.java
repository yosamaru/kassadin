package com.yosamaru.kassadin.util;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtils {
	private static SessionFactory sessionFactory;
	private static Session session;

	static {
		Configuration cfg = new Configuration().configure();

		StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
		ServiceRegistry service = ssrb.build();
		sessionFactory = cfg.buildSessionFactory(service);

	}

	/**
	 * 无参的构造函数
	 */
	private HibernateUtils() {

	}

	/**
	 * 获取session对象
	 */
	public static Session getSession() {
		return sessionFactory.openSession();
	}

	/**
	 * 关闭session对象
	 */
	public static void closeSession() {
		if (session != null && session.isOpen()) {
			session.close();
		}
	}

	/**
	 * 重载关闭session对象方法
	 *
	 * @param session Session对象
	 */
	public static void closeSession(Session session) {
		if (session != null && session.isOpen()) {
			session.close();
		}
	}

}
