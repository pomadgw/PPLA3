package com.surverior.android.app;

public class AppConfig {
	// URL
	public static String URL_ROOT = "https://surverior.pomadgw.xyz";

	// Server user login url
	public static String URL_LOGIN = URL_ROOT + "/api/authenticate";

	// Server user register url
	public static String URL_REGISTER = URL_ROOT + "/api/users/register";

    public static String URL_RENEW_TOKEN = URL_ROOT + "/api/token";

	public static String URL_GET_USER_DATA = URL_ROOT + "/api/users/current";

	public static String URL_UPDATE = URL_ROOT + "/api/users/current/update";

	public static String URL_PHOTO = URL_ROOT + "/photo/users";

	public static String URL_SURVEY_ADD = URL_ROOT + "/api/surveys/add";

	//public static String URL_SURVEY_GET_LIST = URL_ROOT + "/api/surveys/list";
	public static String URL_SURVEY_GET_LIST = URL_ROOT + "/api/surveys/list/criteria"; //TODO: fix link for timeline

	public static String URL_SURVEY_GET_SURVEY = URL_ROOT + "/api/surveys/user";

	public static String URL_SURVEY_GET_QUESTION = URL_ROOT + "/api/surveys/";

	public static String JWT_SECRET = "yGZw4lv9pDXWcptjEXU9ozHctQe7X5Rv";
}
