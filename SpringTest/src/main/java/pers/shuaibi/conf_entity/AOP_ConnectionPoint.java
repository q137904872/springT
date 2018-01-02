package pers.shuaibi.conf_entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import pers.shuaibi.aop.JoinPoint;

/**
 * 连接点信息
 * @author 宋帅比
 *
 */
public class AOP_ConnectionPoint {
	//拦截方法名
	private String methodName;
	//切面
	private Object aspect;
	//切面beanid
	private String aspectRef;
	//切点
	private String pointcut_ref; 
	//拦截类型
	private String type;
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Object getAspect() {
		return aspect;
	}
	public void setAspect(Object aspect) {
		this.aspect = aspect;
	}
	public String getAspectRef() {
		return aspectRef;
	}
	public void setAspectRef(String aspectRef) {
		this.aspectRef = aspectRef;
	}
	public String getPointcut_ref() {
		return pointcut_ref;
	}
	public void setPointcut_ref(String pointcut_ref) {
		this.pointcut_ref = pointcut_ref;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 执行当前切面拦截方法
	 */
	public void invokeAspect(JoinPoint joinPoint) {
		try {
			
			Method [] methods =aspect.getClass().getMethods();
			Method method = null;
			List<Object> args =  new ArrayList<>();
			
			if(methods!=null) {
				for(Method mt:methods) {
					if(mt.getName().equals(methodName)) {
						method = mt;
						break;
					}			
				}
			}
			
			if(method!=null) {
				Class<?>[]  parameterTypes = method.getParameterTypes();
				if(parameterTypes!=null&&parameterTypes.length>0) {
					if(parameterTypes[0].equals(JoinPoint.class)) {
						args.add(joinPoint);
					}
				}
				
				method.invoke(aspect, args.toArray());
			}

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
