package com.bizconf.audio.constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.TimeZoneEntity;
import com.bizconf.audio.util.IntegerUtil;

public class SiteConstant {
	
	
	//站点状态
	public static final String[] SITE_STATUS=new String[]{
		"0",//全部         
		"1",//正常                
		"2" //锁定              
	};
	
	//租用模式
	public static final String[] SITE_HIREMODE = new String[]{
		"0",//全部
		"1",//包月模式
		"2" //计费模式
	};
	
	//包月模式
	public static final Integer SITE_HIREMODE_MONTH = 1;
	
	//计费模式
	public static final Integer SITE_HIREMODE_TIME = 2;
	
	
	
	
	
	
	
	//计费类型
	public static final String[] SITE_CHARGEMODE = new String[]{
		"0", //全部
		"1", //name host
		"2", //active user
		"3",  //seats
		"4"   //time
	};
	
	//name host
	public static final int SITE_CHARGEMODE_NAMEHOST = 1; 
	
	//active user
	public static final int SITE_CHARGEMODE_ACTIVEUSER = 2; 
	
	//seats
	public static final int SITE_CHARGEMODE_SEATES = 3; 
	
	//time
	public static final int SITE_CHARGEMODE_TIME = 4;
	
	
	
	
	
	
	
	//站点类型
	
	public static final String[] SITE_TYPES=new String[]{
		"0",//全部
		"1",//正式
		"2" //试用
	};
	
	//正式
	public static final Integer SITEFLAG_FORMAL = 1;
	
	//试用
	public static final Integer SITEFLAG_TRY = 2;
	
	

	
	
	/*时区对应关系
	 * 
	 */
	public static final TimeZoneEntity[] TIMEZONE_WITH_CITY=new TimeZoneEntity[]{
		new TimeZoneEntity(1,"GMT-12:00",-43200000),
		new TimeZoneEntity(2,"GMT-11:00",-39600000),
		new TimeZoneEntity(3,"GMT-10:00",-36000000),
		new TimeZoneEntity(4,"GMT-08:00",-28800000),
		new TimeZoneEntity(5,"GMT-07:00",-25200000),
		new TimeZoneEntity(6,"GMT-07:00",-25200000),
		new TimeZoneEntity(7,"GMT-06:00",-21600000),
		new TimeZoneEntity(8,"GMT-06:00",-21600000),
		new TimeZoneEntity(9,"GMT-06:00",-21600000),
		new TimeZoneEntity(10,"GMT-05:00",-18000000),
		new TimeZoneEntity(11,"GMT-05:00",-18000000),
		new TimeZoneEntity(12,"GMT-05:00",-18000000),
		new TimeZoneEntity(13,"GMT-04:00",-14400000),
		new TimeZoneEntity(14,"GMT-04:00",-14400000),
		new TimeZoneEntity(15,"GMT-03:00",-10800000),
		new TimeZoneEntity(16,"GMT-03:00",-10800000),
		new TimeZoneEntity(17,"GMT-03:00",-10800000),
		new TimeZoneEntity(18,"GMT-02:30",-9000000),
		new TimeZoneEntity(19,"GMT-02:00",-7200000),
		new TimeZoneEntity(20,"GMT-01:00",-3600000),
		new TimeZoneEntity(21,"GMT+00:00",+0),
		new TimeZoneEntity(22,"GMT+00:00",+0),
		new TimeZoneEntity(23,"GMT+01:00",+3600000),
		new TimeZoneEntity(24,"GMT+01:00",+3600000),
		new TimeZoneEntity(25,"GMT+01:00",+3600000),
		new TimeZoneEntity(26,"GMT+02:00",+7200000),
		new TimeZoneEntity(27,"GMT+02:00",+7200000),
		new TimeZoneEntity(28,"GMT+02:00",+7200000),
		new TimeZoneEntity(29,"GMT+02:00",+7200000),
		new TimeZoneEntity(30,"GMT+02:00",+7200000),
		new TimeZoneEntity(31,"GMT+03:00",+10800000),
		new TimeZoneEntity(32,"GMT+03:00",+10800000),
		new TimeZoneEntity(33,"GMT+03:00",+10800000),
		new TimeZoneEntity(34,"GMT+03:30",+12600000),
		new TimeZoneEntity(35,"GMT+04:00",+14400000),
		new TimeZoneEntity(36,"GMT+04:00",+14400000),
		new TimeZoneEntity(37,"GMT+04:30",+16200000),
		new TimeZoneEntity(38,"GMT+05:00",+18000000),
		new TimeZoneEntity(39,"GMT+05:00",+18000000),
		new TimeZoneEntity(40,"GMT+05:30",+19800000),
		new TimeZoneEntity(41,"GMT+05:30",+19800000),
		new TimeZoneEntity(42,"GMT+06:00",+21600000),
		new TimeZoneEntity(43,"GMT+07:00",+25200000),
		new TimeZoneEntity(44,"GMT+08:00",+28800000),
		new TimeZoneEntity(45,"GMT+08:00",+28800000),
		new TimeZoneEntity(46,"GMT+08:00",+28800000),
		new TimeZoneEntity(47,"GMT+08:00",+28800000),
		new TimeZoneEntity(48,"GMT+09:00",+32400000),
		new TimeZoneEntity(49,"GMT+09:00",+32400000),
		new TimeZoneEntity(50,"GMT+09:00",+32400000),
		new TimeZoneEntity(51,"GMT+09:30",+34200000),
		new TimeZoneEntity(52,"GMT+10:00",+36000000),
		new TimeZoneEntity(53,"GMT+10:00",+36000000),
		new TimeZoneEntity(54,"GMT+10:00",+36000000),
		new TimeZoneEntity(55,"GMT+10:30",+37800000),
		new TimeZoneEntity(56,"GMT+11:00",+39600000),
		new TimeZoneEntity(57,"GMT+11:00",+39600000),
		new TimeZoneEntity(58,"GMT+11:00",+39600000),
		new TimeZoneEntity(59,"GMT+12:00",+43200000),
		new TimeZoneEntity(60,"GMT+12:00",+43200000)
	};
	

	public static final List<TimeZoneEntity> TIMEZONE_WITH_CITY_LIST =Arrays.asList(TIMEZONE_WITH_CITY);
	
	public static final HashMap<Integer, TimeZoneEntity>TIMEZONE_WITH_CITY_MAP=new HashMap<Integer, TimeZoneEntity>();
	
	
	
	/*
	 * 授权与字段对应关系
	 * 
	 * 
	 * 是一个String的数组：	0、与Empower表的对应的CODE；
	 * 						1、在EmpowerConfig的属性名称；
	 * 						2、是否允许企业管理员设置全局变量；(0是不允许、1是允许)
	 * 						3、是否允许企业管理员权限分配给用户(0是不允许、1是允许)
	 * 
	 */
	
	public static final String[][] EMPOWER_CODE_FIELD_ARRAY=new String[][]{
		{"1","phoneFlag","1","1"},
		{"2","autoFlag","1","1"},
		{"3","shareMediaFlag","1","1"},
		{"4","recordFlag","1","1"},
//		{"5","shareDocsFlag","0"},
//		{"6","shareScreenFlag","0"},
//		{"7","whiteBoardFlag","0"},
//		{"8","noteFlag","0"},
		{"9","videoFlag","0","0"},
		{"10","audioFlag","0","0"},
//		{"11","noticeFlag","0"},
//		{"12","fileFlag","0"},
//		{"13","questionFlag","0"},
		{"14","videoNumber","0","0"},
		{"15","audioNumber","0","0"}
	};
	
	public static final List<String[]> EMPOWER_CODE_FIELD_LIST =Arrays.asList(EMPOWER_CODE_FIELD_ARRAY);

	public static final HashMap<Integer, String>EMPOWER_CODE_CODE2FIELD_MAP=new HashMap<Integer, String>();
	public static final HashMap<String, Integer>EMPOWER_CODE_FIELD2CODE_MAP=new HashMap<String, Integer>();

	public static final Integer EMPOWER_DISABLED = 0;// 授权方式 ：不授权
	public static final Integer EMPOWER_ENABLED = 1;// 授权方式 ：授权使用

	public static final Integer EMPOWER_DEFAULT_SITE = 1;// 授权的默认值，主要是针对老企业的
	public static final Integer EMPOWER_DEFAULT_GLOBAL = 1;// 全局变量的默认值，主要是针对老企业的
	public static final Integer EMPOWER_DEFAULT_USER = 1;// 企业用户的的默认值，主要是针对老企业的
	

	public static final Integer EMPOWER_CODE_PHONE = 1;//电话功能
	public static final Integer EMPOWER_CODE_AUTOCALL = 2;//自动外呼
	public static final Integer EMPOWER_CODE_SHAREMEDIA = 3;//媒体共享
	public static final Integer EMPOWER_CODE_RECORD = 4;//录制
	public static final Integer EMPOWER_CODE_SHAREDOCS = 5;//文档共享
	public static final Integer EMPOWER_CODE_SHARESCREEN = 6;//屏幕共享
	public static final Integer EMPOWER_CODE_WHITEBOARD = 7;//白板
	public static final Integer EMPOWER_CODE_NOTE = 8;//笔记
	public static final Integer EMPOWER_CODE_VIDEO = 9;//视频
	public static final Integer EMPOWER_CODE_AUDIO = 10;//音频
	public static final Integer EMPOWER_CODE_NOTICE = 11;//公告
	public static final Integer EMPOWER_CODE_FILE = 12;//文件传输
	public static final Integer EMPOWER_CODE_QUESTIONFLAG= 13;//问卷调查
	public static final Integer EMPOWER_CODE_VIDEONUMBER= 14;//视频路数
	public static final Integer EMPOWER_CODE_AUDIONUMBER= 15;//音频路数
	
	//public static final EmpowerConfig EMPOWER_CONFIG_DEFAULT=new EmpowerConfig();
	
	
	
	
	static{
		
		for(TimeZoneEntity timeZone:TIMEZONE_WITH_CITY){
			TIMEZONE_WITH_CITY_MAP.put(timeZone.getTimeZoneId(),timeZone);
		}
		for(String[] tmpArr:EMPOWER_CODE_FIELD_LIST){
			EMPOWER_CODE_CODE2FIELD_MAP.put(IntegerUtil.parseInteger(tmpArr[0]), tmpArr[1]);
			EMPOWER_CODE_FIELD2CODE_MAP.put(tmpArr[1],IntegerUtil.parseInteger(tmpArr[0]));
		}
	}
		
	
	/*
	 * 站点出错信息配置：（需加中英文资源）
	 * license不足，错误码为-1
	 */
	
	//创建站点或修改站点时license不足
	public static final Integer SITE_CREATE_ERROR_LICENCE = -1;
	
	//发送站点过期提醒消息值
	public static final Integer SEND_SITE_EXP_REMIND = 1; 
	
	//发送站点过期邮件值
	public static final Integer SEND_SITE_EXP = 1; 
	
	//提前提醒日期
	public static final Integer BEFORE_SITE_EXP_REMIND_DATES = 30; 
	
	//创建站点时license最小值
	public static final int SITE_CREATE_MIN_LICENSE = 2;
	//创建站点时license最大值
	public static final int SITE_CREATE_MAX_LICENSE = 5000;
	
}
