<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height: 100%;">
<head>
<meta name="robots" content="noindex, nofollow">
<meta http-equiv="content-type" content="text/html; charset=euc-kr">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, target-densitydpi=medium-dpi" />
<title>EasyPay 8.0 webpay mobile</title>
<link rel="stylesheet" type="text/css" href="/resources/easypay/css/easypay.css" />
<link rel="stylesheet" type="text/css" href="/resources/easypay/css/board.css" />
</head>
<script type="text/javascript">
</script>
</head>
<body id="container_skyblue">
<form name="frm_pay" method="post">  
<div id="div_mall">
   <div class="contents1">
            <div class="con1t1">
                <p>EP8.0 Webpay Mobile<br>결과 페이지</p>
            </div>
    </div>
    <div class="contents">
        <section class="section00 bg_skyblue">
            <fieldset>
                <legend>주문</legend>
                <br>
                <div class="roundTable">
                    <table width="100%" class="table_roundList" cellpadding="5">          
                        <tbody>
                            <tr>
                                <td colspan="2">PG거래번호</td>
                                <td class="r">[${r_cno}]</td>
                            </tr>
                            <tr>
                                <td colspan="2">총 결제금액</td>
                                <td class="r">${r_amount}</td>
                            </tr>
                            <tr>
                                <td colspan="2">주문번호</td>
                                <td class="r">${r_order_no}</td>
                            </tr>
                            <tr>
                                <td colspan="2">승인번호</td>
                                <td class="r">${r_auth_no}</td>
                            </tr>
                            <tr>
                                <td colspan="2">승인일시</td>
                                <td class="r">${r_tran_date}</td>
                            </tr>
                            <tr>
                                <td colspan="2">에스크로 사용유무</td>
                                <td class="r">${r_escrow_yn}</td>
                            </tr>
                            <tr>
                                <td colspan="2">복합결제 유무 </td>
                                <td class="r">${r_complex_yn}</td>
                            </tr>
                            <tr>
                                <td colspan="2">상태코드</td>
                                <td class="r">${r_stat_cd}</td>
                            </tr>
                            <tr>
                                <td colspan="2">상태메시지</td>
                                <td class="r">${r_stat_msg}</td>
                            </tr>
                            <tr>
                                <td colspan="2">결제수단 </td>
                                <td class="r">${r_pay_type}</td>
                            </tr>
                            <tr>
                                <td colspan="2">카드번호</td>
                                <td class="r">${r_card_no}</td>
                            </tr>
                            <tr>
                                <td colspan="2">발급사코드</td>
                                <td class="r">${r_issuer_cd}</td>
                            </tr>
                            <tr>
                                <td colspan="2">발급사명</td>
                                <td class="r">${r_issuer_nm}</td>
                            </tr>
                            <tr>
                                <td colspan="2">[매입사코드]매입사명</td>
                                <td class="r">[${r_acquirer_cd}] ${r_acquirer_nm}</td>
                            </tr>
<!--                             <tr> -->
<!--                                 <td colspan="2">매입사명</td> -->
<%--                                 <td class="r">[${acquirer_cd}] ${acquirer_nm}</td> --%>
<!--                             </tr> -->
                            <tr>
                                <td colspan="2">할부개월</td>
                                <td class="r">${r_install_period}</td>
                            </tr>
                            <tr>
                                <td colspan="2">무이자여부</td>
                                <td class="r">${r_noint}</td>
                            </tr>
                            <tr>
                                <td colspan="2">부분취소 가능여부</td>
                                <td class="r">${r_part_cancel_yn}</td>
                            </tr>
                            <tr>
                                <td colspan="2">신용카드종류</td>
                                <td class="r">${r_card_gubun}</td>
                            </tr>
                            <tr>
                                <td colspan="2">신용카드구분</td>
                                <td class="r">${r_card_biz_gubun}</td>
                            </tr>
                            <tr>
                                <td colspan="2">쿠폰 사용유무</td>
                                <td class="r">${r_cpon_flag}</td>
                            </tr>  
                             <tr>
                                <td colspan="2">은행코드</td>
                                <td class="r">${r_bank_cd}</td>
                            </tr>  
                             <tr>
                                <td colspan="2">은행명</td>
                                <td class="r">${r_bank_nm}</td>
                            </tr>  
                             <tr>
                                <td colspan="2">계좌번호</td>
                                <td class="r">${r_account_no}</td>
                            </tr>  
                             <tr>
                                <td colspan="2">입금자명</td>
                                <td class="r">${r_deposit_nm}</td>
                            </tr>  
                             <tr>
                                <td colspan="2">계좌사용만료일</td>
                                <td class="r">${r_expire_date}</td>
                            </tr>  
                             <tr>
                                <td colspan="2">현금영수증 결과코드</td>
                                <td class="r">${r_cash_res_cd}</td>
                            </tr>  
                             <tr>
                                <td colspan="2">현금영수증 결과메세지</td>
                                <td class="r">${r_cash_res_msg}</td>
                            </tr>      
                             <tr>
                                <td colspan="2">현금영수증 승인번호</td>
                                <td class="r">${r_cash_auth_no}</td>
                            </tr> 
                             <tr>
                                <td colspan="2">현금영수증 승인일시</td>
                                <td class="r">${r_cash_tran_date}</td>
                            </tr> 
                             <tr>
                                <td colspan="2">현금영수증 발행용도</td>
                                <td class="r">${r_cash_issue_type}</td>
                            </tr> 
                             <tr>
                                <td colspan="2">현금영수증 인증구분</td>
                                <td class="r">${r_cash_auth_type}</td>
                            </tr> 
                             <tr>
                                <td colspan="2">현금영수증 인증번호</td>
                                <td class="r">${r_cash_auth_value}</td>
                            </tr> 
                            <tr>
                                <td colspan="2">휴대폰 PhoneID</td>
                                <td class="r">${r_auth_id}</td>
                            </tr>
                            <tr>
                                <td colspan="2">휴대폰 인증번호</td>
                                <td class="r">${r_billid}</td>
                            </tr>
                            <tr>
                                <td colspan="2">휴대폰번호</td>
                                <td class="r">${r_mobile_no}</td>
                            </tr>
                            <tr>
                                <td colspan="2">안심결제 사용유무</td>
                                <td class="r">${r_mob_ansim_yn}</td>
                            </tr>
                            <tr>
                                <td colspan="2">포인트사/쿠폰사</td>
                                <td class="r">${r_cp_cd}</td>
                            </tr>       
                            <tr>
                                <td colspan="2">잔액</td>
                                <td class="r">${r_rem_amt}</td>
                            </tr>  
                            <tr>
                                <td colspan="2">장바구니 결제여부</td>
                                <td class="r">${r_bk_pay_yn}</td>
                            </tr>                          
                            <tr>
                                <td colspan="2">매입취소일시</td>
                                <td class="r">${r_canc_acq_date}</td>
                            </tr>                 
                            <tr>
                                <td colspan="2">취소일시</td>
                                <td class="r">${r_canc_date}</td>
                            </tr>    
                            <tr>
                                <td colspan="2">환불예정일시</td>
                                <td class="r">${r_refund_date}</td>
                            </tr>                              
                        </tbody>
                    </table>
                </div>
            </fieldset>
        </section>
    </div>
</div>
</body>
</html>