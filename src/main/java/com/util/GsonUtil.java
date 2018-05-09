package com.util;

import java.lang.reflect.Type;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
		
	/**
	 * 返回json字符串
	 * 
	 * @param obj
	 *            格式化对象
	 * @param skipOption
	 *            需要忽略的字段
	 * @param typeOfSrc
	 *            转换对象(不能为空)
	 * @return
	 */
	public static String toJson(Object obj, String[] skipOption, Type typeOfSrc) {
		if (typeOfSrc == null) {
			throw new IllegalArgumentException("Gson指定类型不能为空");
		}
		ExclusionStrategy excludeStrategy = new SetterExclusionStrategy(skipOption);
		Gson gson = new GsonBuilder().setExclusionStrategies(excludeStrategy).create();
		String jsonStr = gson.toJson(obj, typeOfSrc);
		return jsonStr;
	}

	/**
	 * 返回json字符串
	 * 
	 * @param obj
	 *            格式化对象
	 * @param skipOption
	 *            需要忽略的字段
	 * @return
	 */
	public static String toJson(Object obj, String... skipOption) {
		ExclusionStrategy excludeStrategy = new SetterExclusionStrategy(skipOption);
		Gson gson = new GsonBuilder().setExclusionStrategies(excludeStrategy).create();
		String result = gson.toJson(obj);
		return result;
	}

	/**
	 * gson过滤帮助类
	 * 
	 * 
	 */
	private static class SetterExclusionStrategy implements ExclusionStrategy {
		private String[] fields;

		public SetterExclusionStrategy(String[] fields) {
			this.fields = fields;

		}

		@Override
		public boolean shouldSkipClass(Class<?> arg0) {
			return false;
		}

		/**
		 * 过滤字段的方法
		 */
		@Override
		public boolean shouldSkipField(FieldAttributes f) {
			if (fields != null) {
				for (String name : fields) {
					if (f.getName().equals(name)) {
						return true;// true 代表此字段要过滤
					}
				}
			}
			return false;
		}
	}

	/**
	 * 使用@Expose注解转换json。。没有此注解的字段将被忽略
	 * 
	 * @param pageRes
	 * @return
	 */
	public static String toJsonByExpose(Object obj) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
		return gson.toJson(obj);
	}

}