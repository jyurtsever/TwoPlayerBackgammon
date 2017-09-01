public class Test {
	public static void main(String[] args) throws InterruptedException {
        count();
       
	}
	public static void count() throws InterruptedException {
		int i = 0;
        while(i < 5000) {
        	i = i + 1;
        	if (i % 100 == 0) {
        		System.out.println(i);
        	}
        	Thread.sleep(10);
        }
	}
}