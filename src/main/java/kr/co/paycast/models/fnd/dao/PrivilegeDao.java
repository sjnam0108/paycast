package kr.co.paycast.models.fnd.dao;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Privilege;

public interface PrivilegeDao {
	// Common
	public Privilege get(int id);
	public List<Privilege> getList();
	public void saveOrUpdate(Privilege privilege);
	public void delete(Privilege privilege);
	public void delete(List<Privilege> privileges);
	public int getCount();

	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);

	// for Privilege specific
	public Privilege get(String ukid);
	public Privilege get(org.hibernate.Session hnSession, String ukid);
}
