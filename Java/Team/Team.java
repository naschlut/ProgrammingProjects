import java.util.Scanner;

public class Watermellon {

	public static void main(String[] args) {
		int accumulator = 0;
		Scanner t = new Scanner(System.in);
		int problems = t.nextInt();
		int pass = 0;
		for(int i =0 ; i<problems;i++) {
			int row[]=new int [3];
			row[0] = t.nextInt();
			row[1] = t.nextInt();
			row[2]= t.nextInt();
			for(int j = 0 ; j<row.length;j++) {
				if(row[j] == 1) {
					pass++;
				}
			}
			if(pass>=2) {
				accumulator++;
			}
			pass=0;
		
	}
		System.out.println(accumulator);
	}

}
