package com.bizconf.audio.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.web.util.HtmlUtils;

import com.bizconf.audio.entity.EmailConfig;

public class ObjectUtil {
	

	private static final Logger logger = Logger.getLogger(ObjectUtil.class);
	
	private static final String [][] HTML_CODE_MAP=new String[][]{
		{"<","&lt;"},
		{">","&gt;"},
		{" ","&nbsp;"},
		{"'","&apos;"},
		{"\"","&quot;"}
	} ;
	
	
	
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String userName="zhaost";
//		SiteBase siteBase=new SiteBase();
//		getFieldFromObject(siteBase);
//		Object[] fieldNameArr=new Object[]{"abc","123","cde","def"};
//		Arrays.fill(fieldNameArr,"asd");
//		Arrays.fill(fieldNameArr,"123");
//		Arrays.fill(fieldNameArr,"abc");
//		org.springframework.util.ObjectUtils..addObjectToArray(fieldNameArr, "hijdk");
//		System.out.println("fieldNameArr--"+fieldNameArr.length);
				

//		List  emailList=new ArrayList ();
		EmailConfig emailConfig=new EmailConfig();
//		emailConfig.setId(10);
//		emailConfig.setEmailName("dic><k");
//		emailConfig.setEmailHost("fmail.bizconf.cn");
//		emailConfig.setEmailSender("dick_zhao@bizconf.cn");
//		emailConfig.setEmailAccount("dick_zhao@bizconf.cnqqqqqqq");
//		emailConfig.setCreateTime(DateUtil.getOffsetDateByGmtDate(new Date(),14400000L));
//		emailConfig.setEmailPassword("qwer1234");
//		emailList.add(emailConfig);
//		emailConfig=new EmailConfig();
//		emailConfig.setId(6);
//		emailConfig.setEmailName("中国北京<>&&001");
//		emailConfig.setEmailHost("fmail.bizconf.cn");
//		emailConfig.setEmailSender("dick_zhao@bizconf.cn");
//		emailConfig.setEmailAccount("dick_zhao@bizconf.cnqqqqqqq");
//		emailConfig.setCreateTime(DateUtil.getGmtDate(new Date()));
//		emailConfig.setEmailPassword("qwer1234");
//		emailList.add(emailConfig);
////		emailList=parseHtmlWithList(emailList,"emailName");
		String str="string";
		System.out.println("AAAAAAAA=="+emailConfig.getClass().getName());
//		System.out.println(offsetDateWithList(emailList,28800000L,new String[]{"createTime"}));
		
//		System.out.println(getFieldFromObject(emailConfig));
//		for(Object object:emailList){
//			System.out.println(parseHtml(emailConfig,"emailName"));
	}
	
	
	/**
	 * 将指定的字段转成OFFSET的日期数据
	 * @param object
	 * @param offset
	 * @param fields
	 * @return
	 */
	public static Object offsetDate(Object object,Long offset,String[] fields){
		if(object!=null && offset!= null && offset.longValue() !=0 && fields!=null && fields.length >0 ){
			Date gmtDate=null;
			Date offsetDate=null;
			for(String field:fields){
				if(field!=null && !"".equals(field.trim())){
					try {
						gmtDate=(Date) getFieldValue(object,field);
						offsetDate=DateUtil.getOffsetDateByGmtDate(gmtDate, offset);
						object=setFieldValue(object,field,offsetDate);
					} catch (Exception e) {
						// TODO Auto-generated catch block
//						e.printStackTrace();
					}
				}
			}
		}
		return object;
	}
	
	
	
	public static List offsetDateWithList(List list, Long offset,String[] fields){
		List tmpList=null;
		if(list!=null && list.size() > 0 ){
			tmpList=new ArrayList();
			for(Object object:list){
				tmpList.add(offsetDate(object,offset,fields));
			}
		}
		return tmpList;
	}
	
	/**
	 * 对List中指定属性HTML转义存到DB中
	 * @param list
	 * @param fields
	 * @return
	 */
	public static List parseHtmlWithList(List list,String...fields){
		List tmpList=null;
		if(list!=null && list.size()>0 && fields!=null && fields.length >0){
			tmpList=new ArrayList();
			for(Object object:list){
				tmpList.add(parseHtml(object,fields));
			}
		}
		return tmpList;
	}
	

	/**
	 * 对List中指定属性的HTML代码 转成可见实际代码
	 * @param list
	 * @param fields
	 * @return
	 */
	public static List parseCharWithList(List list,String...fields){
		List tmpList=null;
		if(list!=null && list.size()>0 && fields!=null && fields.length >0){
			tmpList=new ArrayList();
			for(Object object:list){
				tmpList.add(parseChar(object,fields));
			}
		}
		return tmpList;
	}

	
	
	/**
	 * 转成HTML字符
	 * @param object   需要转义的对象
	 * @param fields   指定字段
	 * @return
	 */
	public static Object parseHtml(Object object,String... fields){
		if(object!=null && fields !=null && fields.length > 0){
			String svalue="";
			String nvalue="";
			for(String field:fields){
				if(field!=null && !"".equals(field.trim())){
					svalue=String.valueOf(getFieldValue(object,field));
					nvalue=HtmlUtils.htmlEscape(svalue);
					object=setFieldValue(object,field,nvalue);
					nvalue=null;
					svalue=null;
				}
			}
		}
		return object;
	}
	
	
	
	/**
	 * 转成实际字符
	 * @param object   需要转义的对象
	 * @param fields   指定字段
	 * @return
	 */
	public static Object parseChar(Object object,String... fields){
		if(object!=null && fields !=null && fields.length > 0){
			String svalue="";
			String nvalue="";
			for(String field:fields){
				if(field!=null && !"".equals(field.trim())){
					svalue=String.valueOf(getFieldValue(object,field));
					nvalue=HtmlUtils.htmlUnescape(svalue);
					object=setFieldValue(object,field,nvalue);
					nvalue=null;
					svalue=null;
				}
			}
		}
		return object;
	}
	
	
	
	
	
	public static Object getFieldValue(Object object,String fieldName) {
		Object valObject =null;
		if(object!=null){
			Class clszz=object.getClass();
			String getMethodName="";
			Method getMdthod=null;
			char[] fieldChars=null;
			try {
				fieldChars=fieldName.toCharArray();
				fieldChars[0]-=32;
				getMdthod = clszz.getMethod("get"+new String(fieldChars));
				valObject=(Object) getMdthod.invoke(object);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				fieldChars=null;
				getMdthod=null;
				clszz=null;
			}
		}
		
		return valObject;
	}
	
	
	public static Field getFieldByObjectAndFieldName(Object object,String fieldName){
		Field field=null;
		if(object!=null){
			Class clszz=object.getClass();
			Field[] fieldArray=clszz.getDeclaredFields();
			if(fieldArray!=null && fieldArray.length >0){
				for(Field eachField:fieldArray){
					if(fieldName!=null && eachField!=null  && fieldName.equals(eachField.getName())){
						field=eachField;
						break;
					}
				}
			}
		}
		return field;
		
	}

	public static Object setFieldValue(Object object,String fieldName,Object value) {
		if(object!=null){
			Class clszz=object.getClass();
			String setMethodName="";
			Method setMdthod=null;
			char[] fieldChars=null;
			Field field =null;
			try {
				fieldChars=fieldName.toCharArray();
				fieldChars[0]-=32;
				field = object.getClass().getDeclaredField(fieldName);
				setMethodName="set"+new String(fieldChars);
				setMdthod=clszz.getDeclaredMethod(setMethodName, field.getType()); 
				setMdthod.invoke(object, value);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				fieldChars=null;
				setMdthod=null;
				setMethodName=null;
				clszz=null;
			}
		}
		
		return object;
	}
	
	
	
	
	/**
	 * 获取对象的所有字段
	 * */
	public static String[] getFieldFromObject(Object object){
		String[] fieldNameArr=null;
		if(object!=null){
			Class clszz=object.getClass();
			Field[] fieldArray=clszz.getDeclaredFields();
			List<String> fieldList=new ArrayList<String>();
			if(fieldArray != null && fieldArray.length > 0){
				String eachFieldName="";
				for (Field eachField:fieldArray){
					if(eachField != null){
						eachFieldName=eachField.getName();
						if(!"serialVersionUID".equals(eachFieldName)){
							fieldList.add(eachFieldName);
						}
					}
				}
			}
			fieldNameArr=new String[fieldList.size()];//fieldList.toArray();
			if(fieldList!=null && fieldList.size() >0 ){
				for(int ii=0;ii<fieldList.size();ii++){
					fieldNameArr[ii]=fieldList.get(ii);
				}
			}
			fieldList=null;
		}
		return fieldNameArr;
	}
	

	
	
	/**
	 * 对象数据拷贝
	 * 
	 * 返回 toObject
	 * 
	 * id,username,password
	 * */
	
	public static Object copyObject(Object fromObject,Object toObject,String copyField) throws Exception{
		if(copyField==null ||  "".endsWith(copyField)){
			return null;
		}
		Object fromValue=null;
		Method fromMethodGet,toMethodGet,toMethodSet;
		Class fromClass;
		Class toClass;
		fromClass=fromObject.getClass();
		toClass=toObject.getClass();
		char[] nameChars;
		String tmpMethodName;
		Field[] fromFieldArr = fromObject.getClass().getDeclaredFields(); 
		String tmpField=","+copyField+",";
		String oneFieldName="";
		final int  arrLen=fromFieldArr.length;
		for(int ii=0;ii<arrLen;ii++){
			oneFieldName = fromFieldArr[ii].getName();    
			if(tmpField.indexOf(","+oneFieldName+",")>-1){
				nameChars=oneFieldName.toCharArray();
				nameChars[0]-=32;
				tmpMethodName=new String(nameChars);
				fromMethodGet = fromClass.getMethod("get"+tmpMethodName);
				toMethodGet = toClass.getMethod("get"+tmpMethodName);
				fromValue=(Object) fromMethodGet.invoke(fromObject);
				toMethodSet=toClass.getDeclaredMethod("set"+tmpMethodName, fromFieldArr[ii].getType()); 
				if(toMethodSet!=null && toObject!=null){
					toMethodSet.invoke(toObject, fromValue);
				}
			}
		}
		oneFieldName="";
		nameChars=null;
		fromFieldArr=null;
		fromMethodGet=null;
		toMethodGet=null;
		toMethodSet=null;
		fromClass=null;
		toClass=null;
		return toObject;
	}
	
	

}
