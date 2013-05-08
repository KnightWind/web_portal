package com.bizconf.audio.component.email;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Action;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Duration;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Repeat;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

import com.bizconf.audio.component.email.model.SendMail;

public class EmailUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		SendMail mailInfo=new SendMail();
		mailInfo.setFromEmail("dick_zhao@bizconf.cn");
		mailInfo.setFromName("Dick");
		mailInfo.setServerHost("smtp.bizconf.cn");
		mailInfo.setServerPort("25");
		mailInfo.setUserName("dick_zhao@bizconf.cn");
		mailInfo.setUserPass("qwer1234");
		String mailSubject="2012年技术总结总结会议（发送邮件测试）";
		mailInfo.setSubject(mailSubject);
		mailInfo.setContentType("html");
		
		
//		mailInfo.setContent("<font color=\"red\">2012年技术总结总结会议</font><br/>请点击下面的网址看会议介绍，<a href=\"http://www.baidu.com\" target=\"_blank\">百度</a><br/><br/><br/><br/>");
		
		String content = getContent(new File("D:/martin/portal/audio-portal/WebContent/templates/email02.html"));
		System.out.println("the content is:"+content);
		mailInfo.setContent(content);
		mailInfo.setValidate(true);
		mailInfo.setToEmail("robert_han@bizconf.cn");

		//mailInfo.addFilePath("D:\\工作日志\\git使用步骤.txt");
		//mailInfo.addFilePath("D:\\工作日志\\2013-01\\晚会小品.docx");
//		mailInfo.addCcEmail("chris_gao@bizconf.cn");
		
		//mailInfo.setCalFlag(true);
		
		mailInfo.setTimeZone("Asia/Shanghai");
		java.util.Calendar calendar=java.util.Calendar.getInstance();
		calendar.set(2013,2, 22, 14, 20,0);
		//mailInfo.setStartTime(calendar.getTime());

//		java.util.Calendar stopCalendar=java.util.Calendar.getInstance();
		calendar.set(2013,2, 22, 18, 20,0);
		//mailInfo.setStopTime(calendar.getTime());
		
		mailInfo.setAddress("网络会议");
		mailInfo.setWarnSubject(mailSubject+"Asia/Shanghai");
		mailInfo.setBeforeMinute(30);
		mailInfo.setWarnCount(3);
		mailInfo.setGapMinute(15);
		send(mailInfo);

		mailInfo=null;
		
	}
	
	
	private static String getContent(File file){
		String content = null;
		StringBuilder budiler= new StringBuilder();
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br=new BufferedReader(reader);
			while((content=br.readLine())!=null){
				
				budiler.append(content);
			}
			br.close();
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return budiler.toString();
	}
	
	private static Properties getEmailProperties(SendMail mailInfo){
		Properties props=null;
		if(mailInfo != null){
			props = new Properties();
			props.put("mail.smtp.host", mailInfo.getServerHost()); // SMTP服务器
			props.put("mail.smtp.port", mailInfo.getServerPort()); // 设置smtp端口
//			props.put("mail.transport.protocol", "smtp"); // 发邮件协议
			props.put("mail.smtp.auth", mailInfo.isValidate() ? "true" : "false");
		}
		return props;
	}
	
	public static boolean send(SendMail mailInfo){
		boolean sendStatus=false;
		if(mailInfo != null ){
			
			// 判断是否需要身份认证
			boolean validate=mailInfo.isValidate();
			EmailAuthor authenticator = null;
			Properties properties = getEmailProperties(mailInfo);
			if(validate){
				if (mailInfo.isValidate()) {
					// 如果需要身份认证，则创建一个密码验证器
					authenticator = new EmailAuthor(mailInfo.getUserName(),mailInfo.getUserPass());
				}
			}
			// 根据邮件会话属性和密码验证器构造一个发送邮件的session
			//Session mailSession = Session.getDefaultInstance(properties,authenticator);
			Session mailSession = Session.getInstance(properties,authenticator);
			Message mailMessage=null;
			Address[] ccEmailArray=null;

			try {
				// 根据session创建一个邮件消息
				mailMessage = new MimeMessage(mailSession);
				// 设置邮件消息的发送者
				mailMessage.setFrom(new InternetAddress(mailInfo.getFromEmail(),mailInfo.getFromName()));
				
				// 设置邮件接收者
				mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mailInfo.getToEmail()));
				
				
				
				//设置邮件抄送者
				List<String> emailCcList=mailInfo.getCcEmailList();
				if(emailCcList != null && emailCcList.size() > 0){
					ccEmailArray=new Address[emailCcList.size()];
					for(int ii=0;ii<emailCcList.size();ii++){
						ccEmailArray[ii]=new InternetAddress(emailCcList.get(ii));
					}
					mailMessage.setRecipients(Message.RecipientType.CC, ccEmailArray);
				}
				emailCcList=null;
				
				
				
				Multipart multipart = null;//new MimeMultipart();
				
				
				
				//如果是添加OUTLOOK日历
				if(mailInfo.isCalFlag()){
					multipart=getCalMultipart(mailInfo);
				}
				
				if(multipart==null){
					multipart=new MimeMultipart();
					
				}
				
				// 设置邮件消息的主题
				mailMessage.setSubject(mailInfo.getSubject());
				
				

				//设置邮件附件
				List<String> fileList=mailInfo.getFilePathList();
				if(fileList != null && fileList.size() > 0){
					BodyPart filePart= null;
					DataSource dataSource =null;
					for(String eachFilePath:fileList){
						if(eachFilePath != null){
							dataSource = new FileDataSource(eachFilePath);
							 if(dataSource != null){
								 filePart = new MimeBodyPart();
								 filePart.setDataHandler(new DataHandler(dataSource));
								 filePart.setFileName(MimeUtility.encodeText(new File(eachFilePath).getName()));
								 multipart.addBodyPart(filePart);
							 }
							 filePart= null;
							 dataSource =null;
						}
					}
				}
				fileList=null;
				
				

				//设置邮件内容及内容格式
				String contentType=mailInfo.getContentType();
				if(contentType==null || "".equals(contentType.trim())){
					contentType="txt";
				}
				contentType=contentType.toLowerCase();
				String mailContent=mailInfo.getContent();
				BodyPart bodyContent= new MimeBodyPart();
				if("html".equals(contentType)){
					bodyContent.setContent(mailContent, "text/html;charset=utf8");
				}else{
					bodyContent.setText(mailContent);
				}
	            multipart.addBodyPart(bodyContent);
	            bodyContent=null;
				
				
				
				// 设置邮件消息发送的时间
				mailMessage.setSentDate(new Date());
				mailMessage.addHeader("X-Priority","1");


				mailMessage.setContent(multipart);
				mailMessage.saveChanges();
				Transport.send(mailMessage);
				sendStatus = true;
				
				
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		return sendStatus;
	}
	
	
	/**
	 * 获取邮件中OUTLook日历格式
	 * @param mailInfo
	 * @return
	 */
	public static Multipart getCalMultipart(SendMail mailInfo) {
		Multipart multipart = null;//
		if (mailInfo != null) {
			try{
				multipart=new MimeMultipart();
				BodyPart icalBodyPart=new MimeBodyPart();
				
				//时区
				String timeZone=mailInfo.getTimeZone();
				TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
				TimeZone timezone = registry.getTimeZone(timeZone);
				
	
				//会议开始时间、结束时间、地点
				java.util.Calendar calendar=java.util.Calendar.getInstance();
				calendar.setTimeZone(timezone);
				calendar.setTime(mailInfo.getStartTime());
				//设置会议开始时间
				DateTime startTime = new DateTime(calendar.getTime());
				
				calendar.setTime(mailInfo.getStopTime());
				
				DateTime endTime = new DateTime(calendar.getTime());
				VEvent vevent =new VEvent(startTime, endTime, mailInfo.getSubject());
	
				vevent.getProperties().add(timezone.getVTimeZone().getTimeZoneId());//时区
				vevent.getProperties().add(new Location(mailInfo.getAddress()));//会议地点
//		        vevent.getProperties().add(new Summary(mailInfo.getSubject()));//邮件主题
//		        vevent.getProperties().add(new Description(mailInfo.getContent()));//邮件内容
				vevent.getProperties().add(new UidGenerator("meeting invite").generateUid());// 设置uid
				
				//设置提醒,

				//时间 提前10分钟
				Integer beforeMinute=mailInfo.getBeforeMinute();
				if(beforeMinute!=null && beforeMinute .intValue() >0){
					beforeMinute=-1 * beforeMinute.intValue() ;
				}else{
					beforeMinute=-10;
				}
				VAlarm valarm = new VAlarm(new Dur(0, 0, beforeMinute , 0));
				
				
				//提醒总次数
				Integer warnCount=mailInfo.getWarnCount();
				if(warnCount==null || warnCount.shortValue() < 0){
					warnCount=3;
				}
				valarm.getProperties().add(new Repeat(warnCount));
				
				
				//间隔分钟数
				Integer gapMinute=mailInfo.getGapMinute();
				if(gapMinute==null || gapMinute.intValue() < 0){
					gapMinute=5;
				}
				valarm.getProperties().add(new Duration(new Dur(0, 0, gapMinute, 0)));

		        //提醒窗口显示的文字信息
		        valarm.getProperties().add(Action.DISPLAY);
//		        String warn
		        valarm.getProperties().add(new Description(mailInfo.getWarnSubject()));
		        vevent.getAlarms().add(valarm);//将VAlarm加入VEvent
				
				
		        Calendar icsCalendar = new Calendar();
				icsCalendar.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
				icsCalendar.getProperties().add(Version.VERSION_2_0);
				icsCalendar.getProperties().add(Method.REQUEST);
				icsCalendar.getComponents().add(vevent);//将VEvent加入Calendar
				CalendarOutputter co = new CalendarOutputter(false);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				co.output(icsCalendar, os);
				byte[] mailbytes = os.toByteArray();
				
				icalBodyPart.setContent(mailbytes, "text/calendar;method=REQUEST;charset=UTF-8");
				multipart.addBodyPart(icalBodyPart);
				
			}catch(Exception e){
				e.printStackTrace();
			}

		}

		return multipart;
	}
	
	
	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	public static boolean sendAA(SendEmailInfo emailInfo,boolean htmlFlag) throws Exception{
//		
//		boolean sendStatus=false;
//		// 判断是否需要身份认证
//		EmailAuthor authenticator = null;
//		Properties properties =null;// getEmailProperties(emailInfo);
//		if (emailInfo.isValidate()) {
//			// 如果需要身份认证，则创建一个密码验证器
//			authenticator = new EmailAuthor(emailInfo.getUserName(),emailInfo.getUserPass());
//		}
//		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
//		Session sendMailSession = Session.getDefaultInstance(properties,authenticator);
//		Message mailMessage=null;
//		Address[] ccEmailArray=null;
//		
//		try {
//			
//			
//			
//			
//			
//			
//			// 设置邮件消息的主要内容
//			String mailContent = emailInfo.getContent();
//			
//			Multipart multipart = new MimeMultipart();
//			
//			
//			
////			
////			EmailMeetingObject mtgObject=new EmailMeetingObject();
////			mtgObject.setAddress("中国北京中关村大厦");
////			mtgObject.setPromptMsg("2012年度中关村论坛开始时间：16:00");
////			mtgObject.setSubject("2012年度中关村论坛");
////			SimpleDateFormat format =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////			String startTimeStr="2013-02-05 16:00:00";
////			String stopTimeStr="2013-02-05 18:00:00";
////			Date startTime=null;
////			Date stopTime=null;
////			try {
////				startTime = format.parse(startTimeStr);
////				stopTime= format.parse(stopTimeStr);
////			} catch (Exception e) {
////				e.printStackTrace();
////			}
////			mtgObject.setStartTime(startTime);
////			mtgObject.setStopTime(stopTime);
////			BodyPart mtgBodyPart=getMeetingPart(mtgObject);
////			multipart.addBodyPart(mtgBodyPart);
////			mtgBodyPart = null;
//			
//			
//			
//			BodyPart bodyContent= new MimeBodyPart();
//			if(htmlFlag){
//				bodyContent.setContent(mailContent, "text/html;CHARSET=utf8");
//			}else{
//				bodyContent.setText(mailContent);
//			}
//            multipart.addBodyPart(bodyContent);
//
//			
//
//			//设置邮件附件
//			List<String> fileList=emailInfo.getFilePathList();
//			if(fileList != null && fileList.size() > 0){
//				BodyPart filePart= null;
//				DataSource dataSource =null;
//				for(String eachFilePath:fileList){
//					if(eachFilePath != null){
//						dataSource = new FileDataSource(eachFilePath);
//						 if(dataSource != null){
//							 filePart = new MimeBodyPart();
//							 filePart.setDataHandler(new DataHandler(dataSource));
//							 filePart.setFileName(MimeUtility.encodeText(new File(eachFilePath).getName()));
//							 multipart.addBodyPart(filePart);
//						 }
//						 filePart= null;
//						 dataSource =null;
//					}
//				}
//			}
//			fileList=null;
//			
//		
//			
//            mailMessage.setContent(multipart);
//            mailMessage.saveChanges();
//			Transport.send(mailMessage);
//			sendStatus=true;
//		} catch (MessagingException ex) {
//			
//			ex.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}finally{
//			sendMailSession=null;
//			mailMessage=null;
//			ccEmailArray=null;
//			properties.clear();
//			properties=null;
//		}
//		return sendStatus;
//	}
//	
//	
//	
//	/**
//	 *  返回邮件的会议的时间加入日历中
//	 * */
//	private static BodyPart getMeetingPart(EmailMeetingObject mtgObject) {
//		
//		if(mtgObject!=null){
//			
//			
//			try {
//				//
//				bodyPart= new MimeBodyPart();
//				
//				
//				
//				
//				//提醒窗口显示的文字信息
//				valarm.getProperties().add(Action.DISPLAY);
//				valarm.getProperties().add(new Description(mtgObject.getPromptMsg()));
//				vevent.getAlarms().add(valarm);//将VAlarm加入VEvent
//				
//				Calendar icsCalendar = new Calendar();
//				icsCalendar.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
//				icsCalendar.getProperties().add(Version.VERSION_2_0);
//				icsCalendar.getProperties().add(Method.REQUEST);
//				icsCalendar.getComponents().add(vevent);//将VEvent加入Calendar
//				CalendarOutputter co = new CalendarOutputter(false);
//				ByteArrayOutputStream os = new ByteArrayOutputStream();
//				co.output(icsCalendar, os);
//				byte[] mailbytes = os.toByteArray();
////	        multipart.setSubType
//				bodyPart.setContent(mailbytes, "text/calendar;method=REQUEST;charset=UTF-8");
////	        multipart.addBodyPart(timeBodyPart);
//			} catch (SocketException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ValidationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (MessagingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	        
//			
//		}
//		return bodyPart;
//	}
////	
	
//	
//	private  static void sendEmail( EmailInfo emailInfo) throws Exception{
//
//		boolean sendStatus=false;
//		// 判断是否需要身份认证
//		EmailAuthor authenticator = null;
//		Properties properties = getEmailProperties(emailInfo);
//		if (emailInfo.isValidate()) {
//			// 如果需要身份认证，则创建一个密码验证器
//			authenticator = new EmailAuthor(emailInfo.getUserName(),emailInfo.getUserPass());
//		}
//		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
//		Session sendMailSession = Session.getDefaultInstance(properties,authenticator);
//		Message mailMessage=null;
//		Address[] ccEmailArray=null;
//		
//		try {
//			// 根据session创建一个邮件消息
//			mailMessage = new MimeMessage(sendMailSession);
//			// 设置邮件消息的发送者
//			mailMessage.setFrom(new InternetAddress(emailInfo.getFromEmail(),emailInfo.getFromName()));
//			
//			
//			// 设置邮件接收者
//			mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailInfo.getToEmail()));
//			
//			
//			
//			//设置邮件抄送者
//			List<String> tmpCcEmailList=emailInfo.getCcEmailList();
//			if(tmpCcEmailList != null && tmpCcEmailList.size() > 0){
//				ccEmailArray=new Address[tmpCcEmailList.size()];
//				for(int ii=0;ii<tmpCcEmailList.size();ii++){
//					ccEmailArray[ii]=new InternetAddress(tmpCcEmailList.get(ii));
//				}
//				mailMessage.setRecipients(Message.RecipientType.CC, ccEmailArray);
//			}
//			tmpCcEmailList=null;
//			
//			
//			// 设置邮件消息的主题
//			mailMessage.setSubject(emailInfo.getSubject());
//			
//			
//			// 设置邮件消息发送的时间
//			mailMessage.setSentDate(new Date());
//			
//			
//			// 设置邮件消息的主要内容
//			String mailContent = emailInfo.getContent();
//			
//			Multipart multipart =getContent(emailInfo);
//			
//            mailMessage.setContent(multipart);
//            mailMessage.saveChanges();
//			Transport.send(mailMessage);
//			sendStatus=true;
//		} catch (MessagingException ex) {
//			
//			ex.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}finally{
//			sendMailSession=null;
//			mailMessage=null;
//			ccEmailArray=null;
//			properties.clear();
//			properties=null;
//		}
//	}
//	
//
//    private static Multipart getContent( EmailInfo emailInfo) throws Exception {
//        //------VEvent Start----------
//        //时区
//        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
//        TimeZone timezone = registry.getTimeZone("Asia/Shanghai");
//        //会议地点
//        String location = "21-C";
//        //会议时间
//        java.util.Calendar cal = java.util.Calendar.getInstance();
//        cal.setTimeZone(timezone);
//        cal.set(2012, 11 , 28, 15, 30);
//        DateTime start = new DateTime(cal.getTime());
//        cal.set(2012, 11 , 28, 17, 00);
//        DateTime end = new DateTime(cal.getTime());
//        VEvent vevent = new VEvent(start, end, emailInfo.getSubject());
//        vevent.getProperties().add(timezone.getVTimeZone().getTimeZoneId());//时区
//        vevent.getProperties().add(new Location(location));//会议地点
//        vevent.getProperties().add(new Summary(emailInfo.getSubject()));//邮件主题
//        vevent.getProperties().add(new Description(emailInfo.getContent()));//邮件内容
//        vevent.getProperties().add(new UidGenerator("meeting invite").generateUid());// 设置uid
//        //与会人
//        Set<String> emailSet = new HashSet<String>();
//        emailSet.add(emailInfo.getFromEmail());
//        emailSet.add(emailInfo.getToEmail());
//        for (String email : emailSet) {
//            Attendee attendee = new Attendee(URI.create("mailto:" + email));
//            attendee.getParameters().add(Role.CHAIR);
//            attendee.getParameters().add(PartStat.NEEDS_ACTION);
//            attendee.getParameters().add(Rsvp.TRUE);
//            attendee.getParameters().add(new Cn(email.substring(0, email.indexOf("@"))));
//            vevent.getProperties().add(attendee);
//        }
//        //--------VEvent Over----------
//        //--------VAlarm Start----------
//        //提醒,提前10分钟
//        VAlarm valarm = new VAlarm(new Dur(0, 0, -50, 0));
//        //每5分钟提醒一次，提醒三次
//        valarm.getProperties().add(new Repeat(10));
//        valarm.getProperties().add(new Duration(new Dur(0, 0, 1, 0)));
//        //提醒窗口显示的文字信息
//        valarm.getProperties().add(Action.DISPLAY);
//        valarm.getProperties().add(new Description("Progress Meeting at 9:30am"));
//        vevent.getAlarms().add(valarm);//将VAlarm加入VEvent
//        //--------VAlarm Over-------------
//        //--------日历对象 Start---------------
//        Calendar icsCalendar = new Calendar();
//        icsCalendar.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
//        icsCalendar.getProperties().add(Version.VERSION_2_0);
//        icsCalendar.getProperties().add(Method.REQUEST);
//        icsCalendar.getComponents().add(vevent);//将VEvent加入Calendar
//        //将日历对象转换为二进制流
//        CalendarOutputter co = new CalendarOutputter(false);
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        co.output(icsCalendar, os);
//        byte[] mailbytes = os.toByteArray();
//        //--------日历对象 Over------------------
//        MimeMultipart mm = new MimeMultipart();
//        mm.setSubType("related");
//        BodyPart mbp = new MimeBodyPart();
//        mbp.setContent(mailbytes, "text/calendar;method=REQUEST;charset=UTF-8");
//        mm.addBodyPart(mbp);
//        return mm;
//    }

}
