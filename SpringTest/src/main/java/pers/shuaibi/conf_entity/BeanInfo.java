package pers.shuaibi.conf_entity;

import java.util.List;

/**
 * 
 * @author 宋帅比
 *配置文件bean标签
 */
public class BeanInfo {
	private String id;
	private String clas;
	/**
	 * 懒加载
	 * true 调用实例是才会实例化
	 * falase 读配置文件时直接实例化
	 */
	private boolean lazy_init = false;
	private List<PropertyInfo> propertys;
	public BeanInfo() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClas() {
		return clas;
	}
	public void setClas(String clas) {
		this.clas = clas;
	}
	public List<PropertyInfo> getPropertys() {
		return propertys;
	}
	public void setPropertys(List<PropertyInfo> propertys) {
		this.propertys = propertys;
	}
	public boolean isLazy_init() {
		return lazy_init;
	}
	public void setLazy_init(boolean lazy_init) {
		this.lazy_init = lazy_init;
	}
	
	
}
