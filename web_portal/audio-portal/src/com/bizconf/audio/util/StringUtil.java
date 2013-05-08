package com.bizconf.audio.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.bizconf.encrypt.Base64;

public class StringUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//			String str="中国人.民工";
//			String format="^[a-zA-Z0-9_\\-&\u4e00-\u9fa5]{4,32}$";
//		 Pattern pattern= Pattern.compile(format);  
//		 Matcher matcher = pattern.matcher(str);
//		 System.out.println(""+matcher.find());
		System.out.println((int)"中".toCharArray()[0]);
		System.out.println(Base64.encode("join/joinpage?joinType=3&cId=1196&scode=81317079","UTF-8"));

		System.out.println(Base64.decode("am9pbi9qb2lucGFnZT9qb2luVHlwZT0zJmNJZD0xMTk2JnNjb2RlPTgxMzE3MDc5","UTF8"));
		// TODO Auto-generated method stub
//		StringBuffer tmpBuffer=new StringBuffer("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
//		tmpBuffer.append("01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
//		List<String> myList=splitByLength(tmpBuffer.toString(),23);
//		for(String eachString:myList){
//			System.out.println("-->>"+eachString);
//			
//			
//			
//			
//		}
		
//		long time1=System.currentTimeMillis();
//		for(int ii=0;ii<10000000;ii++){
//			isNotBlank("                                 ");
//		}
//		long time2=System.currentTimeMillis();
//		
//		System.out.println("isNotBlank  usertime:"+(time2-time1)+" ms");
//		 time1=System.currentTimeMillis();
//		for(int ii=0;ii<10000000;ii++){
////			isEmpty("                                 ");
//		}
//		 time2=System.currentTimeMillis();
//		
//		System.out.println(" isEmpty  usertime:"+(time2-time1)+" ms");
//		MD5 md5=new MD5();
//		System.out.println(" admin md5:"+md5.encrypt("admin")+" ");
		
//		String tempStr="adbcedfdsa";
//
//		System.out.println(tempStr);
//		System.out.println(tempStr.replaceAll("d", "5"));
		
	}
	
	public static String parseAsciiByString(String str){
		
		return null;
	}

	public static String parseStringByAscii(String ascii){
		
		
		return null;
	}
	
	//读取InputStream
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();// 网页的二进制数据
		outStream.close();
		inStream.close();
		return data;
	}
	
	
	
	//根据长度将字符串分割一个字符List
	
	public static List<String> splitByLength(StringBuffer buffer,int splitLength){
		long time1=System.currentTimeMillis();
		List<String> tmpList=null;
		if(buffer!=null && buffer.length() >0 && splitLength > 0){
			int bufferLength=buffer.length();

			System.out.println("String bufferLength-->>"+bufferLength);
			tmpList=new ArrayList<String>();
			if(splitLength > bufferLength){
				tmpList.add(buffer.toString());
			}else{
				int listSize=bufferLength / splitLength;
				if(bufferLength / splitLength > 0){
					listSize++;
				}
				for(int ii=1;ii<=listSize;ii++){
					if(ii==listSize){
						tmpList.add(buffer.substring(splitLength*(ii-1)));
					}else{
						tmpList.add(buffer.substring(splitLength*(ii-1),splitLength*ii));
					}
				}
				
				
			}
		}

		long time2=System.currentTimeMillis();
		System.out.println("buffer User Time-->>"+(time2-time1));
		return tmpList;
	}
	

	//根据长度将字符串分割一个字符List
	
	public static List<String> splitByLength(String str,int splitLength){
		long time1=System.currentTimeMillis();
		List<String> tmpList=null;
		if(str!=null && str.length() >0 && splitLength > 0){
			int strLength=str.length();
			System.out.println("String strLength-->>"+strLength);
			tmpList=new ArrayList<String>();
			if(splitLength > strLength){
				tmpList.add(str.toString());
			}else{
				int listSize=strLength / splitLength;
				if(strLength / splitLength > 0){
					listSize++;
				}
				for(int ii=1;ii<=listSize;ii++){
					if(ii==listSize){
						tmpList.add(str.substring(splitLength*(ii-1)));
					}else{
						tmpList.add(str.substring(splitLength*(ii-1),splitLength*ii));
					}
				}
				
				
			}
		}
		long time2=System.currentTimeMillis();
		System.out.println("String User Time-->>"+(time2-time1));
		return tmpList;
	}
	
    /**
     * 检查字符串是否为空
     * <p>Checks if a String is not empty (""), not null and not whitespace only.</p>
     *
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank("bob")     = true
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is
     *  not empty and not null and not whitespace
     * @since 2.0
     */
    public static boolean isNotBlank(String str) {
    	boolean isBlank = true;
    	if (str == null || "".equals(str.trim())) {
    		isBlank = false;
		}
        return isBlank;
    }
    
    

	public static boolean isEmpty(String str) { 
		boolean empty=false;
		if(str==null || str.trim().length() <=0){
			empty=true;
		}
		return empty;
	}

	
	//返回信息
		public static  String returnJsonStr(int status, Object object){
				JSONObject json = new JSONObject();
				JSONArray jsonArrSite = new JSONArray();
				json.put("status", status);
				jsonArrSite.add(object.toString());
				json.put("userBase", jsonArrSite);
				json.put("message",object.toString());
				return json.toString();
		}
		
		public static String firstUpper(String str){
			if(isEmpty(str)){
				return null;
			}
			char[] chars;
			chars=str.toCharArray();
			chars[0]-=32;
			String upperStr=new String(chars);
			chars=null;
			return upperStr;
		}
		
		
		
		public static String getIpAddr(HttpServletRequest request) {
	        String ip = request.getHeader("x-forwarded-for");
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("Proxy-Client-IP");
	        }
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("WL-Proxy-Client-IP");
	        }
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getRemoteAddr();
	        }
	        return ip;
	    }


}
