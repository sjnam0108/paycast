package kr.co.paycast.models.fnd.dao;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Region;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class RegionDaoImpl implements RegionDao {
    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public Region get(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Region> list = session.createCriteria(Region.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Region> getList() {
		return sessionFactory.getCurrentSession().createCriteria(Region.class).list();
	}

	@Override
	public void saveOrUpdate(Region region) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(region);
	}

	@Override
	public void delete(Region region) {
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(Region.class, region.getId()));
	}

	@Override
	public void delete(List<Region> regions) {
		Session session = sessionFactory.getCurrentSession();
		
        for (Region region : regions) {
            session.delete(session.load(Region.class, region.getId()));
        }
	}

	@Override
	public int getCount() {
		return ((Long)sessionFactory.getCurrentSession().createCriteria(Region.class)
				.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

	@Override
	public DataSourceResult getList(DataSourceRequest request) {
        return request.toDataSourceResult(sessionFactory.getCurrentSession(), Region.class);
	}

	@Override
	public Region get(String regionCode) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Region> list = session.createCriteria(Region.class)
				.add(Restrictions.eq("regionCode", regionCode)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public Region getByRegionName(String regionName) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Region> list = session.createCriteria(Region.class)
				.add(Restrictions.eq("regionName", regionName)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Region> getListByCountryCode(String countryCode) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(Region.class)
				.add(Restrictions.eq("countryCode", countryCode)).list();
	}
}
