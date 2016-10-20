package com.ateam.identity.sign.access.I;
/**
 * 
 * @Version
 * @TODO 接口访问url
 */
public interface HURL {
	String BASE_URL = "http://www.rtkjedu.com/jspx";
	//login
	String URL_USER_LOGIN = "/webservice/app/login.do";
	//查找学生列表
	@Deprecated
	String FIND_SUTDENT_LIST = "/webservice/app/loadStudent.do";
	//签到接口
	String SIGN_IN="/webservice/app/uploadData.do";
	//数据同步
	String DATA_SYNC="/webservice/app/synData.do";
}
