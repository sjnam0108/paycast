package kr.co.paycast.models.store.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.store.StoreDelivery;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderList;
import kr.co.paycast.models.store.StoreOrderPay;
import kr.co.paycast.models.store.StoreOrderVerification;
import kr.co.paycast.models.store.dao.StoreCancelDao;
import kr.co.paycast.models.store.dao.StoreOrderDao;
import kr.co.paycast.utils.PayUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.store.StoreSalesView;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("StorePayService")
public class StorePayServiceImpl implements StorePayService {
	private static final Logger logger = LoggerFactory.getLogger(StorePayServiceImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;
    
	@Autowired
	private MessageManager msgMgr;
    
    @Autowired
    private StoreOrderDao storeOrderDao;
    
    @Autowired
    private StoreCancelDao storeCancelDao;
    
    @Autowired 
    private StoreService storeService;
    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	@Override
	public StoreOrderPay getOrderPay(int idInt) {
		return storeOrderDao.getOrderPayAuthDate(idInt);
	}

	@Override
	public void saveOrUpdate(StoreOrderPay orderPay) {
		storeOrderDao.saveOrUpdate(orderPay);
	}
	
	@Override
	public List<StoreSalesView> getRead(String fromDate, int storeId, Locale locale) {
		List<StoreSalesView> storeSalesView = new ArrayList<StoreSalesView>();
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat transF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DecimalFormat deChg = new DecimalFormat("###,###,###,###");    

		try {
			Date now = new Date();
			Date fromDate1 = transFormat.parse(fromDate);
			Date toDate2 = Util.setMaxTimeOfDate(transFormat.parse(fromDate));
		
			// 매출조회시 주문취소 / 즉시 취소 여부를 가져와서 해당 버튼을 보여준다
			Store store = storeService.getStore(storeId);
			boolean immediateTF = false;
			if(store != null && store.getStoreOpt() != null){
				immediateTF = store.getStoreOpt().isImmediate();
			}
			
	        List<StoreOrder> orderList = storeOrderDao.getOrderListDate(fromDate1, toDate2, storeId);
			String cancelTitle = msgMgr.message("sales.cancel", locale);			// 주문취소
			String deviceM = msgMgr.message("sales.deviceM", locale);				// 모바일
			String deviceK = msgMgr.message("sales.deviceK", locale);				// 키오스크
			String totalText = msgMgr.message("sales.totalText", locale);			// 합계
			String numGen = msgMgr.message("sales.numGen", locale);					// 취소번호생성
			String request = msgMgr.message("sales.request", locale);				// 취소 요청 중
			String numRecreate = msgMgr.message("sales.numRecreate", locale);		// 취소 번호 재생성
			String failRetry = msgMgr.message("sales.failRetry", locale);			// 취소 실패 다시 시도
			String cancelCom = msgMgr.message("sales.cancelCom", locale);			// 취소 완료
			String cancelComMsg = msgMgr.message("sales.cancelComMsg", locale);		// 취소가 완료되었습니다.
			String orderCard = msgMgr.message("sales.card", locale);				// 신용카드
			String orderRF = msgMgr.message("sales.refill", locale);				// 리필
			String immediateTitle = msgMgr.message("sales.immediateCanCel", locale);// 즉시취소
			
	        if(orderList.size() > 0){
	        	int amtCalC = 0;
	        	int totalCountCalC = 0;
	        	int totalAmtCalC = 0;
	        	String orderPayMethod = "";
	        	String orderTableNum = "";
	        	String orderDevice = ""; 
	        	String orderSeq = "";
	        	boolean firestCheck = false;
	        	Date compareDate = new Date();
	        	for(StoreOrder order : orderList){
	        		int orderPayId = order.getOrderPayId();
	        		StoreOrderPay orderPay = storeOrderDao.getOrderPayAuthDate(orderPayId);
	        		List<StoreOrderList> resOrderList = storeOrderDao.getOrderList(order.getOrderNumber());
	        		if(resOrderList.size() > 0){
	        			for(StoreOrderList list : resOrderList){
	    	        		StoreSalesView salesView = new StoreSalesView();
	    	        		boolean rowTf = false;
	    	        		if(compareDate.compareTo(order.getWhoCreationDate()) == 0){
	    	        			salesView.setDate("");
	    	        			rowTf = true;
	    	        		}else{
	    	        			if(firestCheck){
	    	        				StoreSalesView salesViewFirest = new StoreSalesView();
	    	        				salesViewFirest.setDate("");
	    	        				salesViewFirest.setMenu(orderTableNum);
	    	        				salesViewFirest.setAmount("");
	    	        				salesViewFirest.setPacking("");
	    	        				salesViewFirest.setAmt(totalText);
	    	        				salesViewFirest.setBoardYn("Y");
	    	        				salesViewFirest.setTotalamt(deChg.format(amtCalC));
	    	        				salesViewFirest.setFnname(orderPayMethod);
	    	        				if("ME".equals(orderDevice)||"MS".equals(orderDevice)){
	    	        					salesViewFirest.setOrderDevice(deviceM);	
	    	        				}else if("K".equals(orderDevice)){
	    	        					salesViewFirest.setOrderDevice(deviceK);
	    	        				}else{
	    	        					salesViewFirest.setOrderDevice(orderDevice);
	    	        				}
	    	        				salesViewFirest.setOrdernum(orderSeq);
	    	        				salesViewFirest.setOrderNumButton(orderSeq);
		    	        			storeSalesView.add(salesViewFirest);
	    	        			}else{
	    	        				firestCheck = true;
	    	        			}
	    	        			
	    	        			amtCalC = 0;
	    	        			compareDate = order.getWhoCreationDate();
	    	        			salesView.setDate(transF.format(order.getWhoCreationDate()));
	    	        		}
	    	        		String menu = list.getOrderMenuName();
	    	        		
	    	        		if(!Util.isNotValid(list.getOrderSelectEss())){
								String[] menu1 = list.getOrderSelectEss().split(",");
								for(int t=0; t < menu1.length; t++){
									if(!Util.isNotValid(menu1[t])){
										String[] menu2 = menu1[t].split("_");
										if(menu2.length > 0){
											String[] menu3 = menu2[1].split("\\(");
											if(!"".equals(menu) && menu3.length > 1){
												menu += "<br > - ";
											}
											menu += PayUtil.separationistName(menu3);
										}
									}
								}
	    	        		}
	    	        		if(!Util.isNotValid(list.getOrderSelectAdd())){
								String[] menu1 = list.getOrderSelectAdd().split(",");
								for(int t=0; t < menu1.length; t++){
									if(!Util.isNotValid(menu1[t])){
										String[] menu2 = menu1[t].split("_");
										if(menu2.length > 0){
											String[] menu3 = menu2[1].split("\\|\\|");
											if(menu3.length > 0){
												for(int ttt=0; ttt < menu3.length; ttt++){
													String[] menu4 = menu3[ttt].split("\\(");
													if(!"".equals(menu) && menu4.length > 1){
														menu += "<br > - ";
													}
													menu += PayUtil.separationistName(menu4);
												}
											}
										}
									}
								}
	    	        		}
	    	        		salesView.setMenu(menu);
	    	        		
    	        			// 0보다 클 경우 포장
	    	        		String packing = "";
	    	        		// orderType 주문 선택 (S : 매장이용, P : 포장, D : 배달)
	    	        		if(list.getOrderMenuPacking() > 0){
	    	        			if("D".equals(order.getOrderType())){
	    	        				packing = "delivery";
	    	        			}else{
	    	        				packing = "packing";
	    	        			}
	    	        		}
	    	        		salesView.setPacking(packing);
	    	        		
	    	        		if(order.getDeliveryId() > 0){
	    	        			StoreDelivery deli = storeOrderDao.getDeliveryOne(order.getDeliveryId());
	    	        			
	    	        			salesView.setRoadAddr(deli.getRoadAddr());
	    	        			salesView.setAddrDetail(deli.getAddrDetail());
	    	        			salesView.setStoreMsg(deli.getStoreMsg());
	    	        			salesView.setDeliMsg(deli.getDeliMsg());
	    	        		}
	    	        		
	    	        		String phone = PayUtil.phoneChange(Util.parseString(order.getTelNumber(), ""));
	    	        		salesView.setTelNumber(phone);
	    	        		
	    	        		salesView.setAmount(String.valueOf(list.getOrderMenuAmount()));
	    	        		salesView.setAmt(deChg.format(list.getOrderMenuAmt()));
	    	        		salesView.setTotalamt(deChg.format(list.getOrderMenuTotalAmt()));
	    	        		if(rowTf){
	    	        			salesView.setFnname("");
	    	        			salesView.setOrderDevice("");
	    	        			salesView.setOrdernum("");
	    	        			salesView.setOrderNumButton("");
	    	        		}else{
	    	        			orderPayMethod = orderPay.getPayMethod();
	    						if("CARD".equals(orderPayMethod) || "11".equals(orderPayMethod)){
	    							orderPayMethod = orderCard;
	    						}
	    						if("RF".equals(orderPayMethod)){
	    							orderPayMethod = orderRF;
	    						}
	    						orderTableNum = "";
	    						if(!Util.isNotValid(order.getOrderTable()) && !"0000".equals(order.getOrderTable())){
	    							orderTableNum = "TABLE : " + order.getOrderTable(); 
	    						}
	    						
	    	        			orderDevice = order.getOrderDevice();
	    	        			orderSeq = order.getOrderSeq();
	    	        			salesView.setFnname("");
	    	        			salesView.setOrderDevice("");
	    	        			salesView.setOrdernum("");
	    	        			// 2019.04.10 취소 로직을 추가 
	    	        			// 1. 모바일 / 키오스크에 대한  취소 버튼을 생성
	    	        			// 2019.04.13 취소 로직 구현시 취소 에 대한 상태 표시로 인하여 승인번호 표현하기
	    	        			// 취소 상태 표시 (N : default, 1 : 취소승인번호 생성, 2 : 취소요청 (고객이 요청), Y : 취소가 완료된 상태 - 취소결과 받은상태, X - 취소요청 실패)
	    	        			String cancelStatus = order.getOrderCancelStatus();
	    	        			StoreOrderVerification verification = null;
	    	        			if(!"N".equals(cancelStatus)){
	    	        				verification = storeCancelDao.getStoreCancelOneDao(order.getOrderCancelId());
	    	        			}
	    	        			
	    	        			// K : 키오스크 ( 결제된 device 에 따라서 취소코드가 생성될때 방법이 다름 )
	    	        			// MS : 스마일페이 모바일  / ME : easypay
	    	        			String deviceScritp = "salesCancel"; // 모바일 일경우
	    	        			if("K".equals(order.getOrderDevice())){
	    	        				deviceScritp = "salesKioskCancel";
	    	        			}
	    	        			
	    	        			// 2019.11.12 리필일 경우 주문 취소에 대한 버튼은 나오지 않는다.
	    	        			if(!"RF".equals(order.getOrderTid())){
	    	        				//2019.04.24 엑셀 출력으로 인항여 보여주는 부분을 나누어 보여줌.
	    	        				if("1".equals(cancelStatus)){
	    	        					salesView.setOrdernum(numGen);
	    	        					salesView.setOrderNumButton("<button id='delete-btn' checkId='"+order.getId()+"' type='button' class='btn btn-sm btn-danger "+deviceScritp+"'>"+cancelTitle+"</button><br />"+verification.getVerificationCode());
	    	        				}else if("2".equals(cancelStatus)){
	    	        					if (now.after(verification.getDestDate()) && now.before(verification.getCancelDate())){
	    	        						salesView.setOrdernum(request);		    	        				
	    	        						salesView.setOrderNumButton(request+"<br>"+verification.getVerificationCode());		    	        				
	    	        					}else{
	    	        						salesView.setOrdernum(numRecreate);
	    	        						salesView.setOrderNumButton("<button id='delete-btn' checkId='"+order.getId()+"' type='button' class='btn btn-sm btn-danger "+deviceScritp+"'>"+cancelTitle+"</button><br /> "+numRecreate+" : "+verification.getVerificationCode());
	    	        					}
	    	        				}else if("F".equals(cancelStatus)){
	    	        					salesView.setOrdernum(failRetry);
	    	        					salesView.setOrderNumButton("<button id='delete-btn' checkId='"+order.getId()+"' type='button' class='btn btn-sm btn-danger "+deviceScritp+"'>"+cancelTitle+"</button><br />"+failRetry);
	    	        				}else if("Y".equals(cancelStatus)){
	    	        					salesView.setOrdernum(cancelCom);
	    	        					salesView.setOrderNumButton(cancelComMsg + "<br>"+verification.getVerificationCode());
	    	        				}else{
	    	        					// 상태가 N 일 경우 및 그외 일 경우 (없음)
	    	        					salesView.setOrdernum("");
	    	        					String buttonPlus = "<button id='delete-btn' checkId='"+order.getId()+"' type='button' class='btn btn-sm btn-danger "+deviceScritp+"'>"+cancelTitle+"</button>";
	    	        					
	    	        					// 2020.07.06 즉시 주문 취소는 키오스크결제 내역이 아니며 즉시취소 버튼이 true 일경우에만 해당 
	    	        					if(immediateTF && !"K".equals(order.getOrderDevice())){
	    	        						// 주문을 즉시 취소한다. 
	    	        						// 2020.07.02 팀장님 요청으로 모바일 경우 즉시 취소 가능 버튼 추가
	    	        						buttonPlus ="<button checkId='"+order.getId()+"' type='button' class='btn btn-sm btn-danger immediateCanCel'>"+immediateTitle+"</button>";	    	        						
	    	        					}
	    	        					
	    	        					salesView.setOrderNumButton(buttonPlus);
	    	        				}
	    	        			}else{
    	        					salesView.setOrdernum("");
    	        					salesView.setOrderNumButton("");
	    	        			}
	    	        		}
	    	        		amtCalC = amtCalC + list.getOrderMenuTotalAmt();
	    	        		// 취소가 완료 되었을 경우 가격 합산을 제외
	    	        		if(!"Y".equals(order.getOrderCancelStatus())){
	    	        			// 모든 가격 합산 
	    	        			totalAmtCalC = totalAmtCalC + list.getOrderMenuTotalAmt();
	    	        			totalCountCalC = totalCountCalC + list.getOrderMenuAmount();
	    	        		}
	    	        		
	    	        		storeSalesView.add(salesView);
	        			}
	        		}
	        	}
	        	
    			// 마지막 목록에 대한 합계 값
				StoreSalesView salesView1 = new StoreSalesView();
				salesView1.setDate("");
				salesView1.setMenu(orderTableNum);
				salesView1.setAmount("");
				salesView1.setAmt(totalText);
				salesView1.setBoardYn("Y");
				salesView1.setTotalamt(deChg.format(amtCalC));
				salesView1.setFnname(orderPayMethod);
				if("ME".equals(orderDevice)||"MS".equals(orderDevice)){
					salesView1.setOrderDevice(deviceM);	
				}else if("K".equals(orderDevice)){
					salesView1.setOrderDevice(deviceK);
				}else{
					salesView1.setOrderDevice(orderDevice);
				}
				salesView1.setOrdernum(orderSeq);
				salesView1.setOrderNumButton(orderSeq);
				salesView1.setPacking("");
    			storeSalesView.add(salesView1);
    			
    			// 하루 모든 금액
				StoreSalesView salesView2 = new StoreSalesView();
				salesView2.setDate(totalText);
				salesView2.setMenu("");
				salesView2.setAmount(deChg.format(totalCountCalC));
				salesView2.setAmt("");
				salesView2.setTotalamt(deChg.format(totalAmtCalC));
				salesView2.setFnname("");
				salesView2.setOrderDevice("");
				salesView2.setOrdernum("");
				salesView2.setOrderNumButton("");
				salesView2.setPacking("");
    			storeSalesView.add(salesView2);
	        }
		} catch (Exception e) {
    		logger.error("StoreSalesView Read", e);
    		throw new ServerOperationForbiddenException("ReadError");
		}
		return storeSalesView;
	}

}
