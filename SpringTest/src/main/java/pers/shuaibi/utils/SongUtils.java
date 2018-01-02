package pers.shuaibi.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 宋帅比
 *
 */
public class SongUtils
 {
	
	private SongUtils() {}
	
	/**
	 * 将传入字符串首字母大写
	 * @param string 
	 * @return
	 */
	public  String toUpperFristChar(String string) {  
	    char[] charArray = string.toCharArray();  
	    charArray[0] -= 32;  
	    return String.valueOf(charArray);  
	}
	
	/**
	 * 反射实例化对象
	 * @param className 全路径类名
	 * @return
	 */
	public  Object objectInstantiation(String className) {
		
		Object obj = null;
		try {
			obj = Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return obj;
	} 
	
	/**
	 * 返回类的所有接口的方法名
	 * @param className 全路径类名
	 * @return
	 */
	public List<String> getInterfacesMethods(String className){
		List<String> ms = new ArrayList<String>();
		try {
			Class<?>[] interFaces = Class.forName(className).getInterfaces();
			for(Class<?> interFace:interFaces) {
				Method[] methods = interFace.getMethods();
				for(Method method:methods) {
					ms.add(method.getName());
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return ms;
	}

	
	/**
	 * 反射调用set方法注入
	 * @param object 调用set方法的对象
	 * @param attribute 需要set值得属性名
	 * @param value 值
	 */
	public  void invokeSet(Object object,String attribute,Object value) {
		try {
			Object val = value ;
			Class<? extends Object> obj = object.getClass();
			Class<?> fieldType = obj.getDeclaredField(attribute).getType();
			//基本类型注入若值为String先进行转换
			if(value.getClass().equals(String.class)) {
				val = stringToAllType((String)value,fieldType);
			}
			obj.getMethod("set"+toUpperFristChar(attribute),fieldType).invoke(object, val);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * String 转其他基本数据类型
	 * @param string 
	 * @param type 
	 * @return
	 */
	public  Object stringToAllType(String string,Class<?> type) {
		
		if(type.equals(byte.class)||type.equals(Byte.class)) {
			return Byte.parseByte(string);
		}
		if(type.equals(short.class)||type.equals(Short.class)) {
			return Short.parseShort(string);
		}
		if(type.equals(int.class)||type.equals(Integer.class)) {
			return Integer.parseInt(string);
		}
		if(type.equals(long.class)||type.equals(Long.class)) {
			return Long.parseLong(string);
		}
		if(type.equals(float.class)||type.equals(Float.class)) {
			return Float.parseFloat(string);
		}
		if(type.equals(double.class)||type.equals(Double.class)) {
			return Double.parseDouble(string);
		}
		if(type.equals(boolean.class)||type.equals(Boolean.class)) {
			return Boolean.parseBoolean(string);
		}
		if(type.equals(char.class)||type.equals(Character.class)) {
			return string.charAt(0);
		}
		return null;
		
	}
	
	public static SongUtils getSongUtils() {
		return SongUtils.SongUtilsHolder.INSTANCE;
	}
	
	private static class SongUtilsHolder {
		private static final SongUtils INSTANCE = new SongUtils();
	}
}
