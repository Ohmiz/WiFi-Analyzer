package kr.co.generic.wifianalyzer;

import java.util.Random;

public class HoonProperty {

	public static String DevicesUUID = "";
	public static String AppNM = "";
	
    static public final String ip = "kdh1732.iisweb.co.kr";
    static public final String context = "";
    static public boolean bActive = true;
	static public final String UserMonitorUrl = "http://" + ip + context
			+ "/UserMonitor.do";
	
	static boolean korean = false;

	//static private String adUnitId = "ca-app-pub-8400711397327177/5917450847";
	static private String adUnitId = "ca-app-pub-5110228858121381/6958298555";
	static private String adUnitId2 = "ca-app-pub-5110228858121381/6958298555";
	
	//static private String adUnitId = "ca-app-pub-8400711397327177/5034016847";
	//static private String adUnitId2 = "ca-app-pub-5110228858121381/7455459751";
	static public int ADShowCnt = 1;
	
	public static String getAdId(int firstpercent) {
		Random rand = new Random();
		if(rand.nextInt(100) < firstpercent)
		{
			return adUnitId;
		}else return adUnitId2;
	}
}
