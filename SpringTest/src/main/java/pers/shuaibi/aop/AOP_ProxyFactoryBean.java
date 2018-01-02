package pers.shuaibi.aop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pers.shuaibi.conf_entity.AOP_ConnectionPoint;
import pers.shuaibi.conf_entity.BeanInfo;
import pers.shuaibi.core.BeanFactory;
import pers.shuaibi.core.interf.ConfLoad;
import pers.shuaibi.utils.MapTree;
import pers.shuaibi.utils.SongUtils;

/**
 * 代理类生产工厂
 * @author 宋帅比
 *
 */
public class AOP_ProxyFactoryBean {
	private SongUtils songUtils;
	private Map<String,List<AOP_ConnectionPoint>> interceptorChain;//拦截链
	private Map<String,String> proxyClass;//需代理的类及beanid
	private MapTree mapTree; //目录结构class 方便索引
	
	public AOP_ProxyFactoryBean() {
		this.songUtils = SongUtils.getSongUtils();
	}
	
	/**
	 * 根据配置文件生成所有代理类
	 * @param beans
	 * @param ssx
	 * @return
	 */
	public Map<String, Object> generateProxys(Map<String, Object> beans,ConfLoad ssx) {
		//代理类集合
		Map<String, Object> proxys = new HashMap<String, Object>();
		this.init(ssx,beans);
		for(String clazz:proxyClass.keySet()) {
			Object proxy = this.getProxy(beans, proxyClass.get(clazz), clazz);
			proxys.put(proxyClass.get(clazz), proxy);
		}		
		return proxys;
	}
	
	/**
	 * 初始化属性
	 */
	private void init(ConfLoad ssx,Map<String, Object> beans) {
		this.interceptorChain = new HashMap<String,List<AOP_ConnectionPoint>>();
		this.proxyClass = new HashMap<String,String>();
		
		//从beanInfos中提取需要信息
		Map<String, String> beanClass = this.processInfo(ssx.getLoadResultBeanInfos());
		this.pathTree(beanClass.keySet());
		this.generateInterceptorInfo(beans,ssx.getLoadResultPointcuts(), ssx.getLoadResultAspect_point(), beanClass);

		
	}
	
	/**
	 * 获取代理类
	 * @param beans
	 * @param beanid
	 * @param clazz
	 * @return
	 */
	private Object getProxy(Map<String, Object> beans,String beanid,String clazz) {
		Object bean = beans.get(beanid);
		if(bean==null) {
			bean = songUtils.objectInstantiation(clazz);
			beans.put(beanid, bean);
		}
		Object proxyBean = AopHandler.newProxyInstance(bean, interceptorChain);
		return proxyBean;
	}
	
	/**
	 * 处理配置信息生成拦截链与需要代理的类
	 * @param pointcuts  Map<String,String> key:配置文件aop:pointcut标签id属性.  value:配置文件<aop:pointcut>标签expression属性.
	 * @param aspect_points Map<String,AOP_ConnectionPoint> key:配置文件<aop:conpoint>标签pointcut-ref属性 
	 * @param beanClass Map<String,String> key: className  value:beanid
	 */
	private void generateInterceptorInfo(Map<String, Object> beans,Map<String,String> pointcuts,Map<String,List<AOP_ConnectionPoint>> aspect_points,Map<String,String> beanClass) {
		//设置切面
		for(String key:aspect_points.keySet()) {
			List<AOP_ConnectionPoint> acs = aspect_points.get(key);
			for(AOP_ConnectionPoint ac:acs) {
				ac.setAspect(beans.get(ac.getAspectRef()));
			}
			
		}
		
		//处理所有切点 找到所有需代理的类和拦截的方法
		for(String key:pointcuts.keySet()) {
			String expression = pointcuts.get(key);
			//xx.xx.xx.* *结尾为拦截xx.xx.xx包及其子包下所有类
			if(expression.substring(expression.length()-1).equals("*")) {
				String str = expression.substring(0, expression.length()-2);
				Set<String> set = mapTree.getLeafNodes(str, new HashSet<String>());
				this.putProxyClass(set, beanClass);
				this.putInterceptorChain(set,aspect_points.get(key));
			}else {
				if(mapTree.getParent(expression)!=null&&mapTree.getParent(expression).length()>0) {
					Set<String> set = mapTree.getChildLeafNodes(expression);
					this.putProxyClass(set, beanClass);
					this.putInterceptorChain(set,aspect_points.get(key));
				}else {
					String className = expression.substring(0, expression.lastIndexOf("."));
					if(mapTree.getParent(className)!=null&&mapTree.getParent(className).length()>0) {
						proxyClass.put(className, beanClass.get(className));
						List<AOP_ConnectionPoint> connectionPoints =  interceptorChain.get(expression);
						if(connectionPoints==null) {
							connectionPoints = new ArrayList<AOP_ConnectionPoint>();
							interceptorChain.put(expression, connectionPoints);
						}
						connectionPoints.addAll(aspect_points.get(key));
					}
				}
			}
		}
		
	}
	
	/**
	 * 将需要拦截方法加入拦截链集合
	 * @param classNames 
	 * @param connectionPoint 连接点
	 */
	private void putInterceptorChain(Set<String> classNames,List<AOP_ConnectionPoint> connectionPoint) {
		if(classNames!=null&&connectionPoint!=null) {
			for(String className:classNames) {
				List<String> methods = songUtils.getInterfacesMethods(className);
				for(String method:methods) {
					String interceptorChainKey = className+"."+method;
					List<AOP_ConnectionPoint> connectionPoints =  interceptorChain.get(interceptorChainKey);
					if(connectionPoints==null) {
						connectionPoints = new ArrayList<AOP_ConnectionPoint>();
						interceptorChain.put(interceptorChainKey, connectionPoints);
					}
					connectionPoints.addAll(connectionPoint);
				}
			}
		}
	}
	
	/**
	 * 将需要代理的类添加到proxyClass
	 * @param clazzs
	 * @param beanClass
	 */
	private void putProxyClass(Set<String> clazzs,Map<String,String> beanClass) {
		if(clazzs!=null) {
			for(String className:clazzs) {
				proxyClass.put(className, beanClass.get(className));
			}
		}
	}
	
	//提取beanInfos中需要的信息
	private Map<String,String> processInfo(Map<String, BeanInfo> beanInfos) {
		Map<String,String> beanClass = new HashMap<String,String>();
		for(String key:beanInfos.keySet()) {
			BeanInfo beanInfo = beanInfos.get(key);
			beanClass.put(beanInfo.getClas(), beanInfo.getId());
		}
		return beanClass;
}
	 
	/**
	 * 将所有bean className按照包目录结构存入树中方便切点索引，叶节点即使完整className
	 * @param beansPath className集合
	 * @return MapTree 树
	 */
	public MapTree pathTree(Set<String> beansPath) {
		this.mapTree = new MapTree();
		for(String bp:beansPath) {
			String [] ss = bp.split("\\.");
			String parent = new String();
			StringBuffer child = new StringBuffer();
			for(int i=0;i<ss.length;i++) {
				parent = child.toString();
				if(i!=0) {
					child.append("."+ss[i]);
				}else {
					child.append(ss[i]);
				}
				
				if(parent!=null&&parent.length()>0) {
					mapTree.add(parent, child.toString());
				}
			}
		}
		return mapTree;
	}
	
}
