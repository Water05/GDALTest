package cn.test;

public class StringTest {
   public static void main(String [] args){
	   String str1 = "/home/kjxx/block/GF1/PMS1/20150904/0001020088/GF1_PMS1_130323331003_20150904_0001020088.TIFF";
	   String str2 = str1.substring(str1.length()-46, str1.length());
	   System.out.println(str2);
   }

}
