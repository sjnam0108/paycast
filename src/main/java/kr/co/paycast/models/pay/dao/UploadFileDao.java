package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.pay.UploadFile;

public interface UploadFileDao {
	
	// Common
	public UploadFile get(Integer id);
	public void saveOrUpdate(UploadFile uploadFile);
	public void delete(UploadFile uploadFile);
	public void delete(List<UploadFile> uploadFiles);
}
