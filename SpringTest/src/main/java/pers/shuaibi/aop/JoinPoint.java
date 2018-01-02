package pers.shuaibi.aop;

/**
 * 
 * @author 宋帅比
 *
 */
public class JoinPoint {
	//传入方法的参数
	private Object[] args;
	//要执行的方法名
	private String methodName;
	//目标对象
	private Object target;
	//代理对象
	private Object proxy;

	public JoinPoint(Object[] args, String methodName, Object target, Object proxy) {
		super();
		this.args = args;
		this.methodName = methodName;
		this.target = target;
		this.proxy = proxy;
	}

	public Object[] getArgs() {
		return args;
	}

	public String getMethodName() {
		return methodName;
	}

	public Object getTarget() {
		return target;
	}

	public Object getThis() {
		return proxy;
	}
	
	
	
}
