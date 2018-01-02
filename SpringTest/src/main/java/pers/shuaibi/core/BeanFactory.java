package pers.shuaibi.core;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pers.shuaibi.aop.AOP_ProxyFactoryBean;
import pers.shuaibi.conf_entity.BeanInfo;
import pers.shuaibi.conf_entity.PropertyInfo;
import pers.shuaibi.core.interf.ConfLoad;
import pers.shuaibi.utils.SongUtils;

/**
 * bean生产工厂 
 * @author hasee宋帅比
 *
 */
public class BeanFactory {
	private Map<String, Object> beans;//bean集合
	private Map<String, Object> proxys;//代理集合
	private Map<String, BeanInfo> beanInfos;//配置集合
	private SongUtils songUtils;
	private ConfLoad xl;
	
	private BeanFactory() {}
	
	private BeanFactory(ConfLoad xl) {
		this.songUtils = SongUtils.getSongUtils();
		this.xl = xl;
		this.init();
		if(this.getBean("proxyFactory")!=null) {
			this.proxys = new AOP_ProxyFactoryBean().generateProxys(this.beans, xl);
		}
		this.injection();
	}

	
	
	/**
	 * 获取bean实例:若无实例则此时加载并注入属性存入map。
	 * 递归:注入属性为未加载bean递归调用
	 * @param name bean id
	 * @return bean
	 */
	public Object getBean(String name) {
		Object bean = beans.get(name);
		//加载未加载的bean
		if(bean==null) {
			BeanInfo beanInfo = beanInfos.get(name);
			if(beanInfo!=null) {
				bean = songUtils.objectInstantiation(beanInfo.getClas());
				beans.put(name,bean);
				//根据配置信息注入属性
				List<PropertyInfo> proInfos = beanInfo.getPropertys();
				for(PropertyInfo proInfo:proInfos) {
					String pname = proInfo.getName();
					String ref = proInfo.getRef();
					String value = proInfo.getValue();
					if(value!=null&&value.length()>0){
						songUtils.invokeSet(bean,pname,value);
					}else if(ref!=null&&ref.length()>0) {
						songUtils.invokeSet(bean,pname,this.getBean(ref));
						System.out.println("bean调用时注入："+this.getBean(ref));
					}
				}
			}
		}else if(proxys!=null&&proxys.get(name)!=null) {
			bean = proxys.get(name);
		}
		return bean;
		
	}

	public static BeanFactory newBeanFactory(ConfLoad xl) {
		return new BeanFactory(xl);
		
	}
	
	private void init() {
		this.beanInfos = xl.getLoadResultBeanInfos();
		//加载配置时是否有直接实例化bean
		if(xl.getLoadResultBeans()==null||xl.getLoadResultBeans().isEmpty()) {
			beans = new HashMap<String, Object>();
			for(String key:beanInfos.keySet()) {
				BeanInfo beanInfo = beanInfos.get(key);
				if(!beanInfo.isLazy_init()) {
					this.getBean(key);
				}
			}
		}else {
			this.beans = xl.getLoadResultBeans();		
		}
	}
	
	
	//已实例化bean 未注入属性在此统一注入
	private void injection() {
		//获取所有已实例化对象需注入的属性
		List<PropertyInfo> delayIn = new ArrayList<PropertyInfo>();
		for(String key:beans.keySet()) {
			List<PropertyInfo> pros = beanInfos.get(key).getPropertys();
			if(pros!=null) {
				delayIn.addAll(pros);
			}
		}
		//属性注入
		if(delayIn!=null) {
			for(PropertyInfo proInfo:delayIn) {
				if(proInfo.getValue()!=null&&proInfo.getValue().length()>0) {
					songUtils.invokeSet(beans.get(proInfo.getBeanID()),proInfo.getName(),proInfo.getValue());
				}else {
					songUtils.invokeSet(beans.get(proInfo.getBeanID()),proInfo.getName(),this.getBean(proInfo.getRef()));
				}
			}
		}
	}
	
}
