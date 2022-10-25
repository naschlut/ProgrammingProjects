import java.util.Scanner;
public class Main {
	public static void main(String[] args) {
		Scanner t = new Scanner (System.in);
		int row;
		int column;
			for(int r = 0;r<5;r++  ) {
				for(int c = 0;c<5;c++) {
					//System.out.println("r: "+(r+1)+ "  c: "+(c+1));
					if(t.nextInt()==1) {
						System.out.println(Math.abs(3-r-1)+Math.abs(3-c-1));
						System.exit(0);
					}
				}
			}
			}    
		}
 
