package cn.bean;

import java.util.HashMap;
import java.util.Map;

public class GetKeyOfCity {
	public static  Map<String, String> mapList= new HashMap<String, String>();
	static {
		 mapList.put("GF1_PMS1_130323331003_20150904_0001020088.TIFF", "shenyang");
	}
 public static String getKeyOfImage(String fileName){ 
	return (String) mapList.get(fileName);
	 
 }
}
