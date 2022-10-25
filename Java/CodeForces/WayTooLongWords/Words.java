import java.util.Scanner;
 
public class Watermellon {
 
	public static void main(String[] args) {
		Scanner t = new Scanner(System.in);
		int number = t.nextInt();
		String[] Words;
		Words = new String[number+1];
		for(int i =0; i<number+1;i++) {
			String next = t.nextLine();
			if(next.length() < 11) {
				Words[i] = next;
				
			}
			else {
				char Firstchar =next.charAt(0);
				char Lastchar = next.charAt(next.length()-1);
				String f = String.valueOf(Firstchar);
				String l = String.valueOf(Lastchar);
				int len = next.length()-2;
				String saying = f+Integer.toString(len)+l;
				Words[i]= saying;
			}
 
		}
		for(int i = 0; i <Words.length;i++) {
			System.out.println(Words[i]);
		}
		System.exit(0);
 
	}
 
}
