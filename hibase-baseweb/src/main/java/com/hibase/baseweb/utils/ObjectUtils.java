package com.hibase.baseweb.utils;

import java.util.List;

/**
 * 对象判空工具类
 */
public class ObjectUtils {

	/**
	 * 判断对象是否为空
	 * @param obj 对象
	 * @return
	 */
	public static boolean isNull(Object obj) {
		return (null == obj);
	}
	
	/**
	 * 判断数组是否为空
	 * @param list
	 * @return
	 */
	public static <T> boolean isNull(List<T> list) {
		return (null == list || list.size() == 0);
	}

	/**
	 * 判断数组是否不为空
	 *
	 * @param list
	 * @return
	 */
	public static <T> boolean isNotNull(List<T> list) {
		return !isNull(list);
	}
}
