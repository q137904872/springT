package song.test;

public class Test1 implements Test{
	
	private Hello test2;
	
	public void test(String str) {
		System.out.println("属性注入测试调用 test2.sayHello");
		test2.sayHello(str);
	}

	public Hello getTest2() {
		return test2;
	}

	public void setTest2(Hello test2) {
		this.test2 = test2;
	}

	
}
