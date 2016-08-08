package com.vietek.taxioperation.util;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServlet;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.vietek.taxioperation.controller.BasicController;

@Transactional
public class ControllerUtils extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8677416328847357273L;
	static private ApplicationContext applicationContex = new ClassPathXmlApplicationContext("spring.xml");
	private static Timer pingTimer;;

	public static ApplicationContext getApplicationContex() {
		return applicationContex;
	}

	public static Timer getPingTimer() {
		return pingTimer;
	}

	public static void setPingTimer(Timer pingTimer) {
		ControllerUtils.pingTimer = pingTimer;
	}

	public ControllerUtils() {
		// if (pingTimer == null) {
		// pingTimer = new Timer("PingDBTimer");
		// pingTimer.scheduleAtFixedRate(new PingDB(), 0, 10 * 1000);
		// }
	}

	public static void setApplicationContex(ApplicationContext applicationContex) {
		ControllerUtils.applicationContex = applicationContex;
	}

	public static BasicController<?> getController(Class<?> clazz) {
		if (applicationContex == null) {
			applicationContex = new ClassPathXmlApplicationContext("spring.xml");
		}
		BasicController<?> dao = (BasicController<?>) applicationContex.getBean(clazz);
		return dao;
	}

	public static Session getCurrentSession() {
		SessionFactoryImpl localSessionFactoryBean = (SessionFactoryImpl) applicationContex.getBean("sessionFactory");
		Session session = localSessionFactoryBean.openSession();
		return session;
	}
}

class PingDB extends TimerTask {

	@Override
	public void run() {
		try {
			Session session = ControllerUtils.getCurrentSession();
			Query query = session.createSQLQuery("SET GLOBAL max_allowed_packet=1073741824");
			query.executeUpdate();
			session.close();
		} catch (Exception ex) {

			Timer pingTimer = ControllerUtils.getPingTimer();
			if (pingTimer != null) {
				pingTimer.cancel();
				pingTimer = null;
			}
			pingTimer = new Timer("PingDBTimer");
			pingTimer.scheduleAtFixedRate(new PingDB(), 0, 10 * 1000);
		}
	}

}