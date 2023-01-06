package kr.co.paycast.models.store.service;

import javax.servlet.http.HttpSession;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.store.StoreAlimTalk;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StoreAllimTalkService {
	// Common
	public void flush();
	
	public void save(StoreAlimTalk alimTalk);

	public DataSourceResult getAllimTalkList(DataSourceRequest request, HttpSession session, Boolean isStoreChk);

}
