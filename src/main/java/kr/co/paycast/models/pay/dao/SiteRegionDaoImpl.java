package kr.co.paycast.models.pay.dao;

import java.util.HashMap;
import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Region;
import kr.co.paycast.models.fnd.RolePrivilege;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.pay.SiteRegion;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class SiteRegionDaoImpl implements SiteRegionDao {
    @Autowired
    private SessionFactory sessionFactory;
    
	@Override
	public SiteRegion get(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<SiteRegion> list = session.createCriteria(SiteRegion.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SiteRegion> getList() {
		return sessionFactory.getCurrentSession().createCriteria(SiteRegion.class).list();
	}

	@Override
	public void saveOrUpdate(SiteRegion siteRegion) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(siteRegion);
	}

	@Override
	public void delete(SiteRegion siteRegion) {
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(SiteRegion.class, siteRegion.getId()));
	}

	@Override
	public void delete(List<SiteRegion> siteRegions) {
		Session session = sessionFactory.getCurrentSession();
		
        for (SiteRegion siteRegion : siteRegions) {
            session.delete(session.load(SiteRegion.class, siteRegion.getId()));
        }
	}

	@Override
	public int getCount() {
		return ((Long)sessionFactory.getCurrentSession().createCriteria(SiteRegion.class)
				.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

	@Override
	public DataSourceResult getList(DataSourceRequest request) {
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("region", Region.class);
		map.put("site", Site.class);
		
        return request.toDataSourceResult(sessionFactory.getCurrentSession(), SiteRegion.class, map);
	}

	@Override
	public boolean isRegistered(int siteId, int regionId) {
		Session session = sessionFactory.getCurrentSession();
		
		Criterion rest1 = Restrictions.eq("region.id", regionId);
		Criterion rest2 = Restrictions.eq("site.id", siteId);
		
		@SuppressWarnings("unchecked")
		List<RolePrivilege> list = session.createCriteria(SiteRegion.class)
				.createAlias("region", "region")
				.createAlias("site", "site")
				.add(Restrictions.and(rest1, rest2)).list();
		
		return !list.isEmpty();
	}

	@Override
	public void saveOrUpdate(List<SiteRegion> siteRegions) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(siteRegions);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SiteRegion> getListBySiteId(int siteId) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(SiteRegion.class)
				.createAlias("region", "region")
				.createAlias("site", "site")
				.add(Restrictions.eq("site.id", siteId)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SiteRegion> getListByRegionId(int regionId) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(SiteRegion.class)
				.createAlias("region", "region")
				.createAlias("site", "site")
				.add(Restrictions.eq("region.id", regionId)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SiteRegion> getDefaultValueListBySiteId(int siteId) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(SiteRegion.class)
				.createAlias("region", "region")
				.createAlias("site", "site")
				.add(Restrictions.eq("defaultValue", "Y"))
				.add(Restrictions.eq("site.id", siteId)).list();
	}

	@Override
	public SiteRegion get(int siteId, String regionCode) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<SiteRegion> list = session.createCriteria(SiteRegion.class)
				.createAlias("region", "region")
				.createAlias("site", "site")
				.add(Restrictions.eq("region.regionCode", regionCode))
				.add(Restrictions.eq("site.id", siteId)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
}
