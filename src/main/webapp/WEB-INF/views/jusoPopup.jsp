<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- Taglib -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%
	request.setCharacterEncoding("UTF-8"); //해당시스템의 인코딩타입이 UTF-8일 경우

	//한글이 깨지는 경우 주석 제거
	String inputYn = request.getParameter("inputYn");
	String roadFullAddr = request.getParameter("roadFullAddr");
	String roadAddrPart1 = request.getParameter("roadAddrPart1");
	String roadAddrPart2 = request.getParameter("roadAddrPart2");
	String engAddr = request.getParameter("engAddr");
	String jibunAddr = request.getParameter("jibunAddr");
	String zipNo = request.getParameter("zipNo");
	String addrDetail = request.getParameter("addrDetail");
	String admCd = request.getParameter("admCd");
	String rnMgtSn = request.getParameter("rnMgtSn");
	String bdMgtSn = request.getParameter("bdMgtSn");
	String detBdNmList = request.getParameter("detBdNmList");

	String bdNm = request.getParameter("bdNm");
	String bdKdcd = request.getParameter("bdKdcd");
	String siNm = request.getParameter("siNm");
	String sggNm = request.getParameter("sggNm");
	String emdNm = request.getParameter("emdNm");
	String liNm = request.getParameter("liNm");
	String rn = request.getParameter("rn");
	String udrtYn = request.getParameter("udrtYn");
	String buldMnnm = request.getParameter("buldMnnm");
	String buldSlno = request.getParameter("buldSlno");
	String mtYn = request.getParameter("mtYn");
	String lnbrMnnm = request.getParameter("lnbrMnnm");
	String lnbrSlno = request.getParameter("lnbrSlno");
	String emdNo = request.getParameter("emdNo");
	String entX = request.getParameter("entX");
	String entY = request.getParameter("entY");
%>
</head>

<script language="javascript">

function init(){
	var url = location.href;
	var confmKey = "U01TX0FVVEgyMDE5MDczMTEyMTMzNjEwODkxNzk=";
// 	var confmKey = "U01TX0FVVEgyMDE5MTAyMTA5MTgwMDEwOTEyNjU=";
	var resultType = "4";
	
	var inputYn= "<%=inputYn%>";
	if(inputYn != "Y"){
		document.form.confmKey.value = confmKey;
		document.form.returnUrl.value = url;
		document.form.resultType.value = resultType;
		document.form.action = "http://www.juso.go.kr/addrlink/addrMobileLinkUrl.do";
		document.form.submit();
	} else {
		opener.jusoCallBack("<%=roadFullAddr%>","<%=roadAddrPart1%>","<%=addrDetail%>","<%=roadAddrPart2%>",
		"<%=engAddr%>", "<%=jibunAddr%>","<%=zipNo%>","<%=admCd%>","<%=rnMgtSn%>","<%=bdMgtSn%>",
		"<%=detBdNmList%>","<%=bdNm%>","<%=bdKdcd%>", "<%=siNm%>","<%=sggNm%>","<%=emdNm%>",
		"<%=liNm%>","<%=rn%>","<%=udrtYn%>","<%=buldMnnm%>","<%=buldSlno%>","<%=mtYn%>",
		"<%=lnbrMnnm%>","<%=lnbrSlno%>","<%=emdNo%>", "<%=entX%>", "<%=entY%>");

		window.close();
	}
}

</script>
<body onload="init();">
	<form id="form" name="form" method="post">

		<input type="hidden" id="confmKey" name="confmKey" value=""/>
		<input type="hidden" id="returnUrl" name="returnUrl" value=""/>
		<input type="hidden" id="resultType" name="resultType " value=""/>
	</form>
</body>
</html>