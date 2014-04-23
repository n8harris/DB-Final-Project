package dataaccesslayer;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import bo.Player;
import bo.Team;
import bo.TeamSeasonPlayer;

public class HibernateUtil {

	private static final SessionFactory sessionFactory;

	static {
		try {
			Configuration cfg = new Configuration()
				.addAnnotatedClass(bo.Player.class)
				.addAnnotatedClass(bo.PlayerSeason.class)
				.addAnnotatedClass(bo.BattingStats.class)
				.addAnnotatedClass(bo.CatchingStats.class)
				.addAnnotatedClass(bo.FieldingStats.class)
				.addAnnotatedClass(bo.PitchingStats.class)
				.configure();
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
			applySettings(cfg.getProperties());
			sessionFactory = cfg.buildSessionFactory(builder.build());
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static Player retrievePlayerById(Integer id) {
        Player p=null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.getTransaction();
		try {
			tx.begin();
			org.hibernate.Query query;
			query = session.createQuery("from bo.Player where id = :id ");
		    query.setParameter("id", id);
		    if (query.list().size()>0) {
		    	p = (Player) query.list().get(0);
		    }
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return p;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<Player> retrievePlayersByName(String nameQuery, Boolean exactMatch) {
        List<Player> list=null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.getTransaction();
		try {
			tx.begin();
			org.hibernate.Query query;
			if (exactMatch) {
				query = session.createQuery("from bo.Player where name = :name ");
			} else {
				query = session.createQuery("from bo.Player where name like '%' + :name + '%' ");
			}
		    query.setParameter("name", nameQuery);
		    list = query.list();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return list;
	}
	
	public static boolean persistPlayer(Player p) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.getTransaction();
		try {
			tx.begin();
			session.save(p);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean persistTeam(Team t){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.getTransaction();
		try {
			tx.begin();
			session.save(t);
			tx.commit();
		} catch (Exception e){
			tx.rollback();
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	public static boolean persistTeamSeasonPlayer(TeamSeasonPlayer tsp) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.getTransaction();
		try {
			tx.begin();
			session.save(tsp);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Team retrieveTeamById(Integer valueOf) {
		// TODO Auto-generated method stub
		return null;
	}
		
}