package pers.shuaibi.core.interf;

import java.util.List;
import java.util.Map;

import pers.shuaibi.conf_entity.AOP_ConnectionPoint;
import pers.shuaibi.conf_entity.BeanInfo;
import pers.shuaibi.conf_entity.PropertyInfo;
/**
 * 配置文件解析器接口
 * @author 宋帅比
 *
 */
public interface ConfLoad {
	
	/**
	 * beanfactory会通过此方法获取加载配置文件时直接实例化的bean加入缓存,若无则直接返回null即可
	 * @return Map<String, Object> key:bean标签的id属性  Object:bean实例化对象
	 */
	Map<String, Object> getLoadResultBeans();
		
	/**
	 * beanfactory通过此方法获取bean配置信息
	 * @return Map<String, BeanInfo> key:bean标签的id属性  BeanInfo:配置文件bean标签实体类
	 */
	Map<String, BeanInfo> getLoadResultBeanInfos();
	
	/**
	 * AOP通过此方法获取切点信息
	 * @return Map<String,String> key:配置文件aop:pointcut标签id属性.  value:配置文件aop:pointcut标签expression属性.
	 */
	Map<String,String> getLoadResultPointcuts();
	
	
	/**
	 * AOP通过此方法获取切面与连接点信息
	 * @return Map<String,AOP_ConnectionPoint> key:配置文件aop:conpoint标签pointcut-ref属性 
	 */
	Map<String,List<AOP_ConnectionPoint>> getLoadResultAspect_point();
	
}
