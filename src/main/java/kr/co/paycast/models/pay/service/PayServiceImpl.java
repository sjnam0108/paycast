package kr.co.paycast.models.pay.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.AppUser;
import kr.co.paycast.models.pay.UploadFile;
import kr.co.paycast.models.pay.dao.AppUserDao;
import kr.co.paycast.models.pay.dao.UploadFileDao;

@Transactional
@Service("payService")
public class PayServiceImpl implements PayService {

	@Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private AppUserDao appUserDao;
    
    @Autowired
    private UploadFileDao uploadFileDao;
    

	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	@Override
	public AppUser getAppUser(int id) {
		return appUserDao.get(id);
	}

	@Override
	public List<AppUser> getAppUserList() {
		return appUserDao.getList();
	}

	@Override
	public void saveOrUpdate(AppUser appUser) {
		appUserDao.saveOrUpdate(appUser);
	}

	@Override
	public void deleteAppUser(AppUser appUser) {
		appUserDao.delete(appUser);
	}

	@Override
	public void deleteAppUsers(List<AppUser> appUsers) {
		appUserDao.delete(appUsers);
	}

	@Override
	public DataSourceResult getAppUserList(DataSourceRequest request) {
		return appUserDao.getList(request);
	}

	@Override
	public List<AppUser> getAppUserListBySiteIdUserId(int siteId, int userId) {
		return appUserDao.getListBySiteIdUserId(siteId, userId);
	}

	@Override
	public List<AppUser> getActiveAppUserListBySiteId(int siteId) {

		return appUserDao.getActiveListBySiteId(siteId);
	}

	@Override
	public List<AppUser> getAppUserListByToken(String token) {
		
		return appUserDao.getListByToken(token);
	}

	@Override
	public UploadFile getUploadFile(Integer id) {
		
		return uploadFileDao.get(id);
	}

	@Override
	public void saveOrUpdate(UploadFile uploadFile) {
		
		uploadFileDao.saveOrUpdate(uploadFile);
	}

	@Override
	public void deleteUploadFile(UploadFile uploadFile) {
		
		uploadFileDao.delete(uploadFile);
	}

	@Override
	public void deleteUploadFiles(List<UploadFile> uploadFiles) {
		
		uploadFileDao.delete(uploadFiles);
	}

	@Override
	public void deactivateAppUserBySystem(String token) {
		
		List<AppUser> appUsers = getAppUserListByToken(token);
		for(AppUser appUser : appUsers) {
			appUser.setStatus("S");
			appUser.touchWho(null);
			
			saveOrUpdate(appUser);
		}
	}
}
