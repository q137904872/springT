package pers.shuaibi.conf_entity;
/**
 * @author 宋帅比
 * 配置文件标签property
 * */
public class PropertyInfo {
	private String beanID;
	private String name;
	private String value;
	private String ref;
	public PropertyInfo() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getBeanID() {
		return beanID;
	}
	public void setBeanID(String beanID) {
		this.beanID = beanID;
	}
	
	
}
