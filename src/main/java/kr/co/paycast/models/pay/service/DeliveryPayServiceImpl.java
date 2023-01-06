package kr.co.paycast.models.pay.service;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.CouponPolicy;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.models.pay.StoreDeliveryPay;
import kr.co.paycast.models.pay.StoreDeliveryPolicy;
import kr.co.paycast.models.pay.StorePolicy;
import kr.co.paycast.models.pay.dao.CouponDao;
import kr.co.paycast.models.pay.dao.CouponPolicyDao;
import kr.co.paycast.models.pay.dao.DeliveryDao;
import kr.co.paycast.models.pay.dao.PolicyDao;
import kr.co.paycast.models.pay.dao.StoreCouponLogDao;
import kr.co.paycast.models.pay.dao.StoreOrderCouponDao;
import kr.co.paycast.models.pay.dao.StoreOrderPointDao;
import kr.co.paycast.models.pay.dao.StorePointLogDao;
import kr.co.paycast.models.store.StoreCouponLog;
import kr.co.paycast.models.store.StoreDelivery;
import kr.co.paycast.models.store.StoreOrderCoupon;
import kr.co.paycast.models.store.StoreOrderPoint;
import kr.co.paycast.models.store.StorePointLog;
import kr.co.paycast.utils.Util;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("DeliveryPayService")
public class DeliveryPayServiceImpl implements DeliveryPayService {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private DeliveryDao deliveryDaodao;
       
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	@Override
	public StoreDeliveryPay get(int id) {
		return deliveryDaodao.getDeliveryPay(id);
	}

	@Override
	public List<StoreDeliveryPay> getDeliveryPayList(int storeId) {
		return deliveryDaodao.getDeliveryPayList(storeId);
	}

	@Override
	public void saveOrUpdate(StoreDeliveryPay deliveryPay) {
		deliveryDaodao.saveOrUpdate(deliveryPay);
		
	}
	@Override
	public void deliveryPayDelete(StoreDeliveryPay deliveryPay) {
		deliveryDaodao.deliveryPayDelete(deliveryPay);
		
	}
	
	@Override
	public DataSourceResult getDeliveryPayRead(DataSourceRequest request) {
		return deliveryDaodao.getDeliveryPayRead(request);
	}

	@Override
	public StoreDeliveryPolicy getDeliveryPolicy(int id) {
		// TODO Auto-generated method stub
		return deliveryDaodao.getDeliveryPolicy(id);
	}

	@Override
	public List<StoreDeliveryPolicy> getDeliveryPolicyList(int storeId) {
		// TODO Auto-generated method stub
		return deliveryDaodao.getDeliveryPolicyList(storeId);
	}

	@Override
	public void saveOrUpdate(StoreDeliveryPolicy deliveryPay) {
		// TODO Auto-generated method stub
		deliveryDaodao.saveOrUpdate(deliveryPay);
	}

	@Override
	public DataSourceResult getgetMinOrderPayRead(DataSourceRequest request) {
		// TODO Auto-generated method stub
		return deliveryDaodao.getDeliveryPayRead(request);
	}


}
