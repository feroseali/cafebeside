package com.team.cafebeside.configs;

public class ServerConnector {
	public static String BASE_URL			=	"http://feroseali.in";
    public static String REGISTER_CUSTOMER	=	BASE_URL+"/cafebeside/reg.php";
    public static String LOGIN				=	BASE_URL+"/cafebeside/login.php";
    public static String LOGOUT				=	BASE_URL+"/cafebeside/logout.php";
    public static String URI_ITEMS			= 	BASE_URL+"/cafebeside/getSubMenu.php?c_id=";
    public static String GET_CATEGORY_URL	=	BASE_URL+"/cafebeside/getTodaysCategory.php";
    public static String POST_CARDINFO		=	BASE_URL+"/cafebeside/postCardinfo.php";
    public static String POST_ORDERINFO		=	BASE_URL+"/cafebeside/postOrders.php";
    public static String GET_BILLSTATUS		=	BASE_URL+"/cafebeside/getBillStatus.php";

}
