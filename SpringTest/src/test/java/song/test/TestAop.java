package song.test;

import pers.shuaibi.aop.JoinPoint;

public class TestAop {
	public void before(JoinPoint jp) {
		System.out.println(jp.getTarget().getClass().getName()+"."+jp.getMethodName()+":方法开始执行");
	}
	public void after(JoinPoint jp) {
		System.out.println(jp.getTarget().getClass().getName()+"."+jp.getMethodName()+"方法执行完成");
	}
}
