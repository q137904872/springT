package song.test;


import pers.shuaibi.core.BeanFactory;
import pers.shuaibi.core.SAX_SpringXMLLoad;

public class testMain {
	public static void main(String[] args) {
		BeanFactory sxl  = BeanFactory.newBeanFactory(new SAX_SpringXMLLoad("src/main/resources/spring.xml"));
		Test t1 = (Test) sxl.getBean("test1");
		t1.test("帅比");
		
	}
}
