package com.hibase.baseweb.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 类备注：时间转换工具类
 *
 * @author hufeng
 * @version 1.0
 * @date 2018-05-23 9:31
 * @desc
 * @since 1.7
 */
public class DateUtil {

	/**
	 * 字符创转换时间，格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date formatTimeByYMDHM(String date) throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return dateFormat.parse(date);
	}

	/**
	 * 字符串转换时间，格式为yyyy-MM-dd HH:mm
	 *
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date formatTimeByYMDHM1(String date) throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		return dateFormat.parse(date);
	}

	/**
	 * 加上小时
	 *
	 * @param targerTime
	 * @param hour
	 * @return
	 * @throws Exception
	 */
	public static Date addHour(Date targerTime, int hour) throws Exception {

		Calendar calendar = Calendar.getInstance();

		if (targerTime == null) {

			targerTime = calendar.getTime();
		}

		calendar.add(Calendar.HOUR_OF_DAY, hour);

		return calendar.getTime();
	}

	/**
	 * 字符串转换时间，格式为yyyy-MM-dd
	 */
	public static String formatCurrentTimeByYMD() throws Exception {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(Calendar.getInstance().getTime());
	}
}
