import java.util.Scanner;
 
public class Watermellon {
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner t = new Scanner(System.in);
		int pounds = t.nextInt();
		int q = pounds;
		if(pounds == 2) {
			System.out.println("NO");
			System.exit(0);
		}
		for(int i  = 0; i<q;i++) {
			if(pounds % 2 == 0 && i % 2 ==0) {
				System.out.println("YES");
				System.exit(0);
			}
			pounds--;
		}
		System.out.println("NO");
 
	}
 
}
