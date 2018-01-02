package pers.shuaibi.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import pers.shuaibi.conf_entity.AOP_ConnectionPoint;
import pers.shuaibi.conf_entity.BeanInfo;
import pers.shuaibi.conf_entity.PropertyInfo;
import pers.shuaibi.core.interf.ConfLoad;
import pers.shuaibi.utils.SongUtils;

/**
 * 
 * 配置文件解析器  sax解析
 * @author 宋帅比
 */
public class SAX_SpringXMLLoad implements ConfLoad{
	private Map<String, Object> beans;//bean集合
	private Map<String, BeanInfo> beanInfos;//配置集合
	private Map<String,String> pointcuts;//AOP切点
	private Map<String,List<AOP_ConnectionPoint>> aspect_points;//切面与连接点
	private SongUtils songUtils;
	
	public SAX_SpringXMLLoad(String url) {
		songUtils = SongUtils.getSongUtils();
		try {
			File inputFile = new File(url);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			SpringHandler handler = new SpringHandler();   
			saxParser.parse(inputFile,handler);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public Map<String, BeanInfo> getLoadResultBeanInfos() {
		return beanInfos;
	}
	@Override
	public Map<String, Object> getLoadResultBeans() {
		return beans;
	}

	@Override
	public Map<String, String> getLoadResultPointcuts() {
		return pointcuts;
	}
	@Override
	public Map<String, List<AOP_ConnectionPoint>> getLoadResultAspect_point() {
		return aspect_points;
	}
	
	
	private class SpringHandler extends DefaultHandler{
		private boolean isBean = false;
		private BeanInfo beanInfo;
		private String aspectRef;
		private List<PropertyInfo> propertyInfos;
		private PropertyInfo propertyInfo;
		
		@Override
		public void startDocument() throws SAXException {
			pointcuts = new HashMap<String,String>();
			aspect_points = new HashMap<String,List<AOP_ConnectionPoint>>();
			beans = new HashMap<String, Object>();
			beanInfos = new HashMap<String, BeanInfo>();
			isBean = false;
			beanInfo =  null;
			propertyInfos = null;
			propertyInfo = null;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			if(qName.equalsIgnoreCase("bean")) {
				//扫描到bean标签执行beanLoad
				this.beanLoad(attributes);
			}
			
			if(qName.equalsIgnoreCase("property")) {
				this.propertyLoad(attributes);
			}
			
			if(qName.equalsIgnoreCase("aop:pointcut")) {
				this.pointcutLoad(attributes);
			}
			
			if(qName.equalsIgnoreCase("aop:aspect")) {
				aspectRef = attributes.getValue("ref");
			}
			
			if(qName.equalsIgnoreCase("aop:conpoint")) {
				if(aspectRef!=null&&aspectRef.length()>0) {
					AOP_ConnectionPoint ac = new AOP_ConnectionPoint();
					ac.setAspectRef(aspectRef);
					ac.setMethodName(attributes.getValue("method"));
					ac.setPointcut_ref(attributes.getValue("pointcut-ref"));
					ac.setType(attributes.getValue("type"));
					List<AOP_ConnectionPoint> connPoints = aspect_points.get(ac.getPointcut_ref());
					if(connPoints == null) {
						connPoints = new ArrayList<AOP_ConnectionPoint>();
						aspect_points.put(ac.getPointcut_ref(), connPoints);
					}
					connPoints.add(ac);
				}
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if(qName.equalsIgnoreCase("bean")) {
				isBean = false;
				beanInfo.setPropertys(propertyInfos);
				beanInfos.put(beanInfo.getId(), beanInfo);
			}
			
			if(qName.equalsIgnoreCase("aop:aspect")) {
				aspectRef = null;
			}
		}
		
		//将bean标签配置信息存入beanInfo，视情况是否直接实例化
		private void beanLoad(Attributes attributes) {
			//配置存入beanInfo
			beanInfo = new BeanInfo();
			propertyInfos = new ArrayList<PropertyInfo>();
			isBean = true;
			String id = attributes.getValue("id");
			String cla = attributes.getValue("class");
			String lazy_init = attributes.getValue("lazy-init");
			if(id!=null&&id.length()>0) {
				beanInfo.setId(id);
			}
			if(cla!=null&&cla.length()>0) {
				beanInfo.setClas(cla);
			}
			if(Boolean.parseBoolean(lazy_init)) {
				beanInfo.setLazy_init(true);
			}
			
			//非延迟加载则 .直接实例化对象并加入缓存
			if(!beanInfo.isLazy_init()) {
				Object bean = songUtils.objectInstantiation(cla);
				beans.put(id,bean);
			}
		}
		
		
		//将Property标签配置信息存入propertyInfo，视情况是否注入属性
		private void propertyLoad(Attributes attributes) {
			//是否bean中Property
			if(isBean) {
				//配置存入 property
				propertyInfo = new PropertyInfo();
				String name =  attributes.getValue("name");
				String ref = attributes.getValue("ref");
				String value = attributes.getValue("value");
				if(name!=null&&name.length()>0) {
					propertyInfo.setName(name);
				}
				propertyInfo.setRef(ref);
				propertyInfo.setValue(value);
				propertyInfo.setBeanID(beanInfo.getId());
				propertyInfos.add(propertyInfo);
				
			}
		}
		
		private void pointcutLoad(Attributes attributes) {
			pointcuts.put(attributes.getValue("id"), attributes.getValue("expression"));
		}
		
	}




}
