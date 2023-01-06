/**
* 쿠키저장
* @param cname 키값
* @param cvalue 저장할 문자열
* @param exdays 쿠키 저장 일수
*/
 var setCookie = function(cname, cvalue, exdays) {
	var d = new Date();
	d.setTime(d.getTime() + (exdays*24*60*60*1000));
	var expires = "expires="+d.toUTCString();
	document.cookie = cname + "=" + escape(cvalue) + ";path=/;" + expires;
};

  /**
   * 쿠키 가져오기
   * @param cname 키값
   * @return str
   */
  var getCookie = function(cname) {
	  var name = cname + "=";
      var ca = document.cookie.split(';');
      for(var i = 0; i < ca.length; i++) {
          var c = ca[i];
          while (c.charAt(0) == ' ') {
              c = c.substring(1);
          }
          if (c.indexOf(name) == 0) {
              return c.substring(name.length, c.length);
          }
      } 
      return "";
  };

  var getCookieCheck = function(name) {
	  var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
	  return value? value[2] : null;
	};
  

  /**
   * 쿠키 삭제
   * @param cname 키값
   */
  var delCookie = function( cname ) {
   setCookie( cname );
  };

  /**
   * 배열데이타 쿠키 저장
   * @param cname 키값
   * @param carray 저장할 배열
   * @param exdays 쿠키 저장 일수
   */

  var setCookieArray = function(cname, carray, exdays) {
	var str = "";
	for( var key in carray ){
		if(str != "" ) str += ",";
		str += key+":"+carray[key];
	}
	this.setCookie( cname, str, exdays );
  };

  

  /**
   * 쿠키에서 배열로 저장된 데이타 가져옴
   * @param cname
   * @return array
   */
var getCookieArray = function(cname) {
	var str = unescape(this.getCookie(cname));
	var tmp1 = str.split(",");
	var reData = {};
	for( var i in tmp1 ){
		var tmp2 = tmp1[i].split(":");
		reData[tmp2[0]] = tmp2[1];
	}
	return reData;
};


function inputPhoneNumber(obj) {

	var inputName = obj.name;
    var number = obj.value.replace(/[^0-9]/g, "");
    var phone = "";

    if(number.length > 16) {
    	phone = number.substr(0, 15);
    }else{
    	return number;
    }
    obj.value = phone;
    
    if(inputName == "BuyerTel"){
    	$("input[name=deliTel]").val(phone);
    }else{
    	$("input[name=BuyerTel]").val(phone);
    }
}
