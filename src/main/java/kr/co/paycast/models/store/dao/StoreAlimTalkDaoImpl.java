package kr.co.paycast.models.store.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.store.StoreAlimTalk;
import kr.co.paycast.utils.Util;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class StoreAlimTalkDaoImpl implements StoreAlimTalkDao {
	private static final Logger logger = LoggerFactory.getLogger(StoreAlimTalkDaoImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public void save(StoreAlimTalk alimTalk) {
		Session session = sessionFactory.getCurrentSession();
		if (session != null) {
			String sql= "INSERT INTO MTS_ATALK_MSG (TRAN_ID,TRAN_SENDER_KEY,TRAN_TMPL_CD,TRAN_CALLBACK,TRAN_PHONE,TRAN_SUBJECT,TRAN_MSG,TRAN_DATE,TRAN_TYPE,TRAN_STATUS,TRAN_REPLACE_TYPE,TRAN_REPLACE_MSG, TRAN_ETC1)" +
					"VALUES (:tranId,:senderKey,:tmplCd,:phone,:telNumber,:subject,:msg,:nowDate,5,'1','S',:smsmsg, :storenm)";
			Query query = session.createSQLQuery(sql);
			String now = Util.toSimpleString(new Date(), "yyyyMMddHHmmss");
			query.setParameter("tranId", alimTalk.getShortName());
			query.setParameter("senderKey", alimTalk.getSenderKey());
			query.setParameter("tmplCd", alimTalk.getTmplCd());
			query.setParameter("phone", alimTalk.getPhone());
			query.setParameter("telNumber", alimTalk.getTelNumber());
			query.setParameter("subject", alimTalk.getSubject());
			query.setParameter("msg", alimTalk.getMsg());
			query.setParameter("nowDate", now);
			query.setParameter("smsmsg", alimTalk.getSmsmsg());
			query.setParameter("storenm", alimTalk.getName());
			
			query.executeUpdate();
		}
	}

	@Override
	public List<StoreAlimTalk> getAllimTalkList(DataSourceRequest request, Boolean isStoreChk) {
		List<StoreAlimTalk> alimList = new ArrayList<StoreAlimTalk>();
		Session session = sessionFactory.getCurrentSession();
		if (session != null) {
			String sql = "SELECT a.TRAN_DATE as date, a.TRAN_ID as shortName, a.TRAN_ETC1 as name, COUNT(1) as cnt FROM( ";
			sql += " SELECT DATE_FORMAT(TRAN_DATE, '%Y-%m-%d') as TRAN_DATE, TRAN_ID, TRAN_ETC1 "; 
			sql += " FROM MTS_ATALK_MSG_LOG WHERE ";
			sql += " TRAN_DATE between :startDate and :endDate";
			sql += " AND TRAN_RSLT = '1000' ";
			if(isStoreChk){
				sql += " AND TRAN_ID = :shortName ";
			}
			sql += ") a GROUP BY a.TRAN_DATE, a.TRAN_ID";
			sql += " ORDER BY a.TRAN_DATE DESC "; 
			Query query = session.createSQLQuery(sql);
			query.setParameter("startDate", request.getReqStrValue2());
			query.setParameter("endDate", request.getReqStrValue3());
			if(isStoreChk){
				query.setParameter("shortName", request.getReqStrValue1());
			}
			
			List results = query.list();
			for (Iterator iterator = results.iterator(); iterator.hasNext();){
				Object[] alim = (Object[]) iterator.next(); 
				StoreAlimTalk alimTalk = new StoreAlimTalk((String)alim[0], (String)alim[1], (String)alim[2], Util.parseInt(alim[3].toString()), 0);
				alimList.add(alimTalk);
			}
			logger.info("alimList.size() [{}]", alimList.size());
		}
		return alimList;
	}

	@Override
	public List<StoreAlimTalk> getSMSList(DataSourceRequest request, Boolean isStoreChk) {
		List<StoreAlimTalk> smsList = new ArrayList<StoreAlimTalk>();
		Session session = sessionFactory.getCurrentSession();
		if (session != null) {
			String sql = "SELECT a.TRAN_DATE as date, a.TRAN_ID as shortName, a.TRAN_ETC1 as name, COUNT(1) as cnt FROM( ";
			sql += " SELECT DATE_FORMAT(TRAN_DATE, '%Y-%m-%d') as TRAN_DATE, TRAN_ID, TRAN_ETC1 "; 
			sql += " FROM MTS_SMS_MSG_LOG WHERE ";
			sql += " TRAN_DATE between :startDate and :endDate";
			sql += " AND TRAN_RSLT = '00' ";
			if(isStoreChk){
				sql += " AND TRAN_ID = :shortName ";
			}
			sql += ") a GROUP BY a.TRAN_DATE, a.TRAN_ID";
			sql += " ORDER BY a.TRAN_DATE DESC "; 
			Query query = session.createSQLQuery(sql);
			query.setParameter("startDate", request.getReqStrValue2());
			query.setParameter("endDate", request.getReqStrValue3());
			if(isStoreChk){
				query.setParameter("shortName", request.getReqStrValue1());
			}
			
			List results = query.list();
			for (Iterator iterator = results.iterator(); iterator.hasNext();){
				Object[] alim = (Object[]) iterator.next(); 
				StoreAlimTalk alimTalk = new StoreAlimTalk((String)alim[0], (String)alim[1], (String)alim[2], 0, Util.parseInt(alim[3].toString()));
				smsList.add(alimTalk);
			}
			logger.info("smsList.size() [{}]", smsList.size());
		}
		return smsList;
	}
}
