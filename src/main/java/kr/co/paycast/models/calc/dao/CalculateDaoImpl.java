package kr.co.paycast.models.calc.dao;

import java.util.Date;
import java.util.List;

import kr.co.paycast.models.calc.StoreCalcDay;
import kr.co.paycast.models.calc.StoreCalcMenu;
import kr.co.paycast.models.calc.StoreCalcStats;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class CalculateDaoImpl implements CalculateDao {
    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public void saveOrUpdate(StoreCalcDay oneCalcDay) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(oneCalcDay);
	}

	@Override
	public void saveOrUpdate(StoreCalcStats oneCalcStats) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(oneCalcStats);
	}

	@Override
	public void saveOrUpdate(StoreCalcMenu oneCalcMenu) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(oneCalcMenu);
	}

	@Override
	public List<StoreCalcDay> calcDayList(Date fromDate1, Date toDate2,int storeId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreCalcDay> list = session.createCriteria(StoreCalcDay.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.ge("orderCreation", fromDate1))
                .add(Restrictions.le("orderCreation", toDate2))
                .addOrder(Order.desc("orderMonthDay"))
				.list();
		
		return list;
	}

	@Override
	public void calcDayDelete(List<StoreCalcDay> calcDayList) {
		Session session = sessionFactory.getCurrentSession();
		
        for (StoreCalcDay storeCalcDay : calcDayList) {
            session.delete(session.load(StoreCalcDay.class, storeCalcDay.getId()));
        }
	}

	@Override
	public List<StoreCalcStats> calcMonthList(Date fromDate1, Date toDate2,int storeId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreCalcStats> list = session.createCriteria(StoreCalcStats.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.ge("orderCreation", fromDate1))
                .add(Restrictions.le("orderCreation", toDate2))
                .addOrder(Order.desc("orderCreation"))
				.list();
		
		return list;
	}

	@Override
	public void calcMonthDelete(List<StoreCalcStats> calcMonthList) {
		Session session = sessionFactory.getCurrentSession();
		
        for (StoreCalcStats storeCalcStats : calcMonthList) {
            session.delete(session.load(StoreCalcStats.class, storeCalcStats.getId()));
        }
	}

	@Override
	public List<StoreCalcMenu> calcMenuList(Date fromDate1, Date toDate2,int storeId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreCalcMenu> list = session.createCriteria(StoreCalcMenu.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.ge("orderCreation", fromDate1))
                .add(Restrictions.le("orderCreation", toDate2))
                .addOrder(Order.asc("orderMenuId"))
				.list();
		
		return list;
	}

	@Override
	public void calcMenuDelete(List<StoreCalcMenu> calcMenuList) {
		Session session = sessionFactory.getCurrentSession();
		
        for (StoreCalcMenu storeCalcMenu : calcMenuList) {
            session.delete(session.load(StoreCalcMenu.class, storeCalcMenu.getId()));
        }
	}

}
