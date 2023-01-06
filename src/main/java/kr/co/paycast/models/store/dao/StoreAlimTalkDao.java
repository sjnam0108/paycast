package kr.co.paycast.models.store.dao;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.store.StoreAlimTalk;


public interface StoreAlimTalkDao {

	public void save(StoreAlimTalk alimTalk);

	public List<StoreAlimTalk> getAllimTalkList(DataSourceRequest request, Boolean isStoreChk);

	public List<StoreAlimTalk> getSMSList(DataSourceRequest request, Boolean isStoreChk);

}
