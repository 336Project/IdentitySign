package com.ateam.identity.sign.access.I;
/**
 * 
 * @Version
 * @TODO 接口访问url
 */
public interface HURL {
	String BASE_URL = "http://c641484739.xicp.net/jspx";
	//login
	String URL_USER_LOGIN = BASE_URL+"/webservice/app/login.do";
	//查找学生列表
	@Deprecated
	String FIND_SUTDENT_LIST = BASE_URL+"/webservice/app/loadStudent.do";
	//签到接口
	String SIGN_IN=BASE_URL+"/webservice/app/uploadData.do";
	//数据同步
	String DATA_SYNC=BASE_URL+"/webservice/app/synData.do";
}
