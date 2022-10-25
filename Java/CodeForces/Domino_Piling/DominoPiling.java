import java.util.Scanner;

public class Watermellon {

	public static void main(String[] args) {
		Scanner t = new Scanner(System.in);
		int M = t.nextInt();
		int N = t.nextInt();
		double mult = M*N;
		int q =(int) mult/2;
		System.out.println(q);
	}

}
