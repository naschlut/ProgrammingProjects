import java.util.Scanner;

public class Watermellon {

	public static void main(String[] args) {
		int accumulator = 0;
		Scanner t = new Scanner(System.in);
		int totalParticipants = t.nextInt();
		int Participants[] = new int [totalParticipants];
		int kfinisher = t.nextInt();
		for(int i = 0 ; i<totalParticipants;i++) {
			int q =t.nextInt();
			Participants[i] = q;
		}
		for(int i = 0; i<totalParticipants;i++) {
			if(Participants[i]>=Participants[kfinisher-1]&&Participants[i]>0) {
				accumulator+=1;
			}
		}
		System.out.println(accumulator);

	}

}
