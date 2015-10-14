package com.ateam.identity.sign.access.I;
/**
 * 
 * @author 李晓�? * @Create_date 2015-3-25 上午10:07:58
 * @Version 
 * @TODO 接口访问url
 */
public interface HURL {
	//login
	public static final String URL_USER_LOGIN = "/webservice/app/login.do";
	//查找学生列表
	@Deprecated
	public static final String FIND_SUTDENT_LIST = "/webservice/app/loadStudent.do";
	//签到接口
	public static final String SIGN_IN="/webservice/app/uploadData.do";
	//数据同步
	public static final String DATA_SYNC="/webservice/app/synData.do";
}
