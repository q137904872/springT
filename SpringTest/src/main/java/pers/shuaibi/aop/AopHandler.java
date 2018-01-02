package pers.shuaibi.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

import pers.shuaibi.conf_entity.AOP_ConnectionPoint;

/**
 * 
 * @author 宋帅比
 *
 */
public class AopHandler implements InvocationHandler {
	
	// 目标对象  
    private Object targetObject;

    //拦截链
    private Map<String,List<AOP_ConnectionPoint>> interceptorChain;
    
    
    private AopHandler() {
		super();
	}

	private AopHandler(Object targetObject,Map<String,List<AOP_ConnectionPoint>> interceptorChain) {
        this.targetObject=targetObject;  
        this.interceptorChain = interceptorChain;
	}

	public static Object newProxyInstance(Object targetObject,Map<String,List<AOP_ConnectionPoint>> interceptorChain){  
		AopHandler ah = new AopHandler(targetObject, interceptorChain);
        //该方法用于为指定类装载器、一组接口及调用处理器生成动态代理类实例    
        //第一个参数指定产生代理对象的类加载器，需要将其指定为和目标对象同一个类加载器  
        //第二个参数要实现和目标对象一样的接口，所以只需要拿到目标对象的实现接口  
        //第三个参数表明这些被拦截的方法在被拦截时需要执行哪个InvocationHandler的invoke方法  
        //根据传入的目标返回一个代理对象  
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),  
                targetObject.getClass().getInterfaces(),ah);  
    }

	@Override
	//关联的这个实现类的方法被调用时将被执行  
    /*InvocationHandler接口的方法，proxy表示代理，method表示原对象被调用的方法，args表示方法的参数*/ 
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		JoinPoint jp = new JoinPoint(args, method.getName(), targetObject, this);
		Object ret=null; 
        //获取当前方法的拦截方法
        List<AOP_ConnectionPoint> interceptorMethod = interceptorChain.get(targetObject.getClass().getName()+"."+method.getName());
        if(interceptorMethod!=null&&!interceptorMethod.isEmpty()) {
        	
        	for(AOP_ConnectionPoint connPoint:interceptorMethod) {
        		if(connPoint.getType().equals("before")) {
        			connPoint.invokeAspect(jp);
        		}
        	}
        	
        	ret = method.invoke(targetObject, args);
        	for(AOP_ConnectionPoint connPoint:interceptorMethod) {
        		if(connPoint.getType().equals("after")) {
        			connPoint.invokeAspect(jp);
        		}
        	}
        	
        }else {
        	ret = method.invoke(targetObject, args);
        }
        
        return ret;  
	}

}
