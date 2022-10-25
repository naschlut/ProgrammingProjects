import java.util.Scanner;

public class Watermellon {

	public static void main(String[] args) {
		int x = 0 ;
		Scanner t = new Scanner(System.in);
		int n = t.nextInt();
		for(int i = 0 ; i<=n;i++) {
			String a  = t.nextLine();
			if(a.contains("+")) {
				x++;
				
			}
			if(a.contains("-")) {
				x--;
			}
		}
		System.out.println(x);

	}

}
