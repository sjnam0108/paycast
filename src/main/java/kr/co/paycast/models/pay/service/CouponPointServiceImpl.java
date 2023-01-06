package kr.co.paycast.models.pay.service;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.CouponPolicy;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.models.pay.StorePolicy;
import kr.co.paycast.models.pay.dao.CouponDao;
import kr.co.paycast.models.pay.dao.CouponPolicyDao;
import kr.co.paycast.models.pay.dao.PolicyDao;
import kr.co.paycast.models.pay.dao.StoreCouponLogDao;
import kr.co.paycast.models.pay.dao.StoreOrderCouponDao;
import kr.co.paycast.models.pay.dao.StoreOrderPointDao;
import kr.co.paycast.models.pay.dao.StorePointLogDao;
import kr.co.paycast.models.store.StoreCouponLog;
import kr.co.paycast.models.store.StoreOrderCoupon;
import kr.co.paycast.models.store.StoreOrderPoint;
import kr.co.paycast.models.store.StorePointLog;
import kr.co.paycast.utils.Util;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("CouponPointService")
public class CouponPointServiceImpl implements CouponPointService {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private CouponDao couponDao;
    
    @Autowired
    private PolicyDao policyDao;
    
    @Autowired
    private CouponPolicyDao couponPolicyDao;
    
    @Autowired
    private StoreOrderCouponDao orderCouponDao;
    
    @Autowired
    private StoreCouponLogDao couponLogDao;
    
    @Autowired
    private StoreOrderPointDao orderPointDao;
    
    @Autowired
    private StorePointLogDao pointLogDao;
    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}
	
	@Override
	public StoreCoupon get(int id) {
		
		return couponDao.get(id);
	}

	@Override
	public List<StoreCoupon> getCouponList(int storeId, int deleteState) {
		
		return couponDao.getList(storeId, deleteState);
	}
	
	@Override
	public void saveOrUpdate(StoreCoupon coupon) {
		
		couponDao.saveOrUpdate(coupon);
	}
	
	@Override
	public DataSourceResult getCouponRead(DataSourceRequest request) {
		
		return couponDao.getRead(request);
	}

	@Override
	public StorePolicy getPolicy(int policyId) {
		
		return policyDao.getPolicy(policyId);
	}
	
	@Override
	public void saveOrUpdate(StorePolicy policy) {
		
		policyDao.saveOrUpdate(policy);
	}
		
	@Override
	public List<StorePolicy> getPolicyList(int storeId) {
		
		return policyDao.getPolicyList(storeId);
	}
	
	@Override
	public List<StorePolicy> getPolicyList(int storeId, String type) {
		
		return policyDao.getPolicyList(storeId, type);
	}

	@Override
	public CouponPolicy getCouponPolicy(int couponPolicyId) {
		
		return couponPolicyDao.getCouponPolicy(couponPolicyId);
	}
	
	@Override
	public List<CouponPolicy> getCouponPolicyList(int storeId) {
		
		return couponPolicyDao.getCouponPolicyList(storeId);
	}
	
	@Override
	public void saveOrUpdate(CouponPolicy cp) {
		
		couponPolicyDao.saveOrUpdate(cp);
	}

	@Override
	public List<StoreOrderCoupon> getCouponStamp(int storeId, String tel, String type) {

		return orderCouponDao.getCouponStamp(storeId, tel, type);
	}

	@Override
	public void saveOrUpdate(StoreOrderCoupon orderCoupon) {
		orderCouponDao.saveOrUpdate(orderCoupon);
		
		StoreCouponLog log = new StoreCouponLog(orderCoupon.getTel(), orderCoupon.getType(), orderCoupon.getStampCnt(), orderCoupon.getStampTotal(), orderCoupon.getUseState(), 
					orderCoupon.getEndDate(), orderCoupon.getStore(), orderCoupon.getCoupon(), orderCoupon);
		couponLogDao.saveOrUpdate(log);
	}
	
	@Override
	public List<StoreOrderCoupon> getIssueCouponRead(String storeId, String tel) {
		
		return orderCouponDao.getIssueCouponRead(Util.parseInt(storeId), tel, "C");
	}


	@Override
	public StoreOrderCoupon getCoupon(int couponId) {

		return orderCouponDao.getCoupon(couponId);
	}
	
	@Override
	public List<StoreOrderPoint> getPointbyTel(int storeId, String tel) {
		
		return orderPointDao.getPointbyTel(storeId, tel);
	}

	@Override
	public void saveOrUpdate(StoreOrderPoint savePoint) {
		orderPointDao.saveOrUpdate(savePoint);
		
		StorePointLog log = new StorePointLog(savePoint.getTel(), savePoint.getPointCnt(), savePoint.getPointTotal(), savePoint.getStore());
		pointLogDao.saveOrUpdate(log);
	}

	@Override
	public List<StoreOrderPoint> getIssuePointRead(String storeId, String tel) {
		
		return orderPointDao.getPointbyTel(Util.parseInt(storeId), tel);
	}

}
