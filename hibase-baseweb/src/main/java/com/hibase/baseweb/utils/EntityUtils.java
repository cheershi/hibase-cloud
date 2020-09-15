package com.hibase.baseweb.utils;

import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型方法类用来拷贝转化对象
 */
public class EntityUtils {

	/**
	 * 实体转换
	 * @param source 源对象
	 * @param targetClass 目标对象的Class类型
	 * @return
	 */
	public static <S, T> T transform(S source, Class<? extends T> targetClass) {
		if (!ObjectUtils.isNull(source)) {
			T target = null;
			try {
				target = targetClass.newInstance();
				BeanUtils.copyProperties(source, target);
			} catch (Exception e) {
				return null;
			}
			return target;
		} else {
			return null;
		}
	}

	/**
	 * 实体列表转换
	 * @param sourceList
	 * @param targetClass
	 * @return
	 */
	public static <S,T> List<T> transform(List<S> sourceList, Class<? extends T> targetClass) {
		List<T> result = new ArrayList<T>();
		if (!ObjectUtils.isNull(sourceList)) {
			for (S source : sourceList) {
				result.add(transform(source, targetClass));
			}
		}
		return result;
	}

    /**
     * 实体转换以json方式转换
     * @param source 源对象
     * @param targetClass 目标对象的Class类型
     * @return
     */
    public static <S, T> T transformByJson(S source, Class<? extends T> targetClass) {
        if (!ObjectUtils.isNull(source)) {
            T target = null;
            try {

                Gson gson = new Gson();

                String sourceJson = gson.toJson(source);

                target = gson.fromJson(sourceJson, targetClass);
            } catch (Exception e) {
                return null;
            }
            return target;
        } else {
            return null;
        }
    }


    /**
     * 实体列表转换
     * @param sourceList
     * @param targetClass
     * @return
     */
    public static <S,T> List<T> transformByJson(List<S> sourceList, Class<? extends T> targetClass) {
        List<T> result = new ArrayList<T>();
        if (!ObjectUtils.isNull(sourceList)) {
            for (S source : sourceList) {
                result.add(transformByJson(source, targetClass));
            }
        }
        return result;
    }
/*
	public static void main(String[] args){

		A a = new A();
		a.setAge(20);
		a.setName("zhangsan");
		a.setSex("男");

		List<C> cs = new ArrayList<>();

		C c =new C();

		c.setAge(20);
		c.setName("lisi");
		cs.add(c);

		a.setCs(cs);

		B b = EntityUtils.transformByJson(a, B.class);
		System.out.println(new Gson().toJson(b));
	}

	@Data
	public static class A{

		private String name;
		private int age;
		private String sex;
		private List<C> cs;
	}

	@Data
	public static class B{

		private String name;
		private String sex;
		private List<D> cs;
	}

	@Data
	public static class C{

		private String name;
		private int age;
	}

	@Data
	public static class D{

		private String name;
		private int age;
	}*/
}
