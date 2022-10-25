import java.util.Scanner;

public class Watermellon {

	public static void main(String[] args) {
		int x = 0 ;
		Scanner t = new Scanner(System.in);
		String n = t.nextLine();
		String q = t.nextLine();
		n = n.toUpperCase();
		q = q.toUpperCase();
		if(n.compareTo(q)>0) {
			System.out.println(1);
		}
		if(n.compareTo(q)==0) {
			System.out.println(0);
		}
		if(n.compareTo(q)<0) {
			System.out.println(-1);
		}
		
	}

}
