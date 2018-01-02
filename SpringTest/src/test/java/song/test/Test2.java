package song.test;

public class Test2 implements Hello{
	
	private int str;
	private boolean bl;
	
	public void sayHello(String str) {
		System.out.println("hello！"+str);
	}

	public int getStr() {
		return str;
	}

	public void setStr(int str) {
		this.str = str;
	}

	public boolean isBl() {
		return bl;
	}

	public void setBl(boolean bl) {
		this.bl = bl;
	}

	
}
