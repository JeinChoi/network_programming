import java.util.ArrayList;

class Example{
	int i;
	public Example(int i) {
		this.i =i;
	}
	
}
public class Ex {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Example> a = new ArrayList<Example>();
		
		for(int i=0;i<3;i++) {
		Example ex3 = new Example(1);
		a.add(ex3);
		}Example ex4 = new Example(1);
		System.out.println(a.get(0).equals(ex4));//false
		System.out.println(ex4.equals(ex4));//false
		
	}

}
