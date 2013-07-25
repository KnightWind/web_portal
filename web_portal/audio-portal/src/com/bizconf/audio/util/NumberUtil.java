package com.bizconf.audio.util;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class NumberUtil {
	private static Logger logger =Logger.getLogger(NumberUtil.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(parseIntegerDefaultZero("-d10"));
	}
	
	/**
	 * 验证是否为数字(所有数值型，包括正负整数、小数、0)
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		logger.info("str=="+str);
		boolean flag=false;
		if(!StringUtil.isEmpty(str)){
			Pattern pattern=Pattern.compile("0|(-?((0\\.\\d+)|(([1-9]\\d*)(\\.\\d+)?)))");
			flag=pattern.matcher(str).matches();
			pattern=null;
		}
		return flag;
	}
	
	/**
	 * 验证是否为整型数字(<0、=0、>0的整数)
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str){
		logger.info("str=="+str);
		boolean flag=false;
		if(!StringUtil.isEmpty(str)){
			Pattern pattern=Pattern.compile("-?\\d+");
//			Pattern pattern=Pattern.compile("0|(-?[1-9]\\d*)");
			flag=pattern.matcher(str).matches();
			pattern=null;
		}
		return flag;
	}
	
	
	
	/**
	 * 验证是否正整数(>0)
	 * @param str
	 * @return
	 */
	public static boolean isPosInteger(String str){
		logger.info("str=="+str);
		boolean flag=false;
		if(!StringUtil.isEmpty(str)){
			Pattern pattern=Pattern.compile("\\d+");
			flag=pattern.matcher(str).matches();
			pattern=null;
		}
		return flag;
	}

	
	/**
	 * 验证是否非负整数(>=0)
	 * @param str
	 * @return
	 */
	public static boolean isNonInteger(String str){
		logger.info("str=="+str);
		boolean flag=false;
		if(!StringUtil.isEmpty(str)){
			Pattern pattern=Pattern.compile("0|([1-9]\\d*)");
			flag=pattern.matcher(str).matches();
			pattern=null;
		}
		return flag;
	}
	
	/**
	 * 将字符串转成Integer型
	 * @param str
	 * @return
	 */
	public static Integer parseInteger(String str){
		logger.info("str=="+str);
		Integer integer=null;
		if(isInteger(str)){
			integer=Integer.valueOf(str);
		}
		return integer;
	}

	/**
	 * 将字符串转成Integer型,默认为0
	 * @param str
	 * @return
	 */
	public static Integer parseIntegerDefaultZero(String str) {
		logger.info("str=="+str);
		Integer integer = null;
		if (isInteger(str)) {
			integer = Integer.valueOf(str);
		}
		if (integer == null) {
			integer = 0;
		}
		return integer;
	}
	
	
	/**
	 * 将字符串转成长整型 
	 * @param str
	 * @return
	 */
	public static Long parseLong(String str){
		logger.info("str=="+str);
		Long lng=null;
		if(isInteger(str)){
			lng=Long.valueOf(str);
		}
		return lng;
	}
	
	/**
	 * 将字符串转成长整型 ,默认为0
	 * @param str
	 * @return
	 */
	public static Long parseLongDefaultZero(String str) {
		logger.info("str=="+str);
		Long lng = null;
		if (isInteger(str)) {
			lng = Long.valueOf(str);
			
		}
		if (lng == null) {
			lng = 0l;
		}
		return lng;
	}
	
	/**
	 * 将字符串转成Double型 
	 * @param str
	 * @return
	 */
	public static Double parseDouble(String str){
		logger.info("str=="+str);
		Double d=null;
		if(isNumber(str)){
			d=Double.valueOf(str);
		}
		return d;
		
	}
	
	/**
	 * 将字符串转成Double型,如果不正确则默认0
	 * @param str
	 * @return
	 */
	public static Double parseDoubleDefaultZero(String str){
		logger.info("str=="+str);
		Double d=null;
		if(isNumber(str)){
			d=Double.valueOf(str);
		}
		if (d == null) {
			d = 0d;
		}
		return d;
	}
	
	
	

}
