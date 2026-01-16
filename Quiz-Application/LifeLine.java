package main;

import java.util.Scanner;

public class LifeLine {
	static int ap = 1;
	static int ff = 1;
	
	public boolean showLifeLines(int qno, QuestionBank qb) {
		Scanner ob = new Scanner(System.in);
		
		if(ap == 1 || ff == 1) {
			if(ap == 1) {
				System.out.println("Enter 1 for Audience Poll");
			}
			if(ff == 1) {
				System.out.println("Enter 2 for Fifty-Fifty");
			}
			char answer = qb.qns[qno].answer;
			int ans;
			if(answer == 'a') {
				ans = 0;
			}
			else if(answer == 'b') {
				ans = 1;
			}
			else if(answer == 'c') {
				ans = 2;
			}
			else {
				ans = 3;
			}
			int choice = ob.nextInt();
			if(choice == 1) {
				audiencePoll(qno, ans);
			}
			else if(choice == 2) {
				fifty(qno, qb, ans);
			}
			return true;
		}
		else {
			return false;
		}
	}
	public void audiencePoll(int qno, int ans) {
		System.out.println("Using Audience poll...\n");
		String[] percentages = new String[4];
		percentages[ans] = "65%";
		String p[] = {"9%", "11%", "15%"};
		int j = 0;
		for(int i = 0; i<4; i++) {
			if(i == ans) {
				continue;
			}
			percentages[i] = p[j++];
		}
		for(int i = 0; i<4; i++) {
			System.out.println("Option " + (i + 1) + " : " + percentages[i]);
		}
		ap = 0;
	}
	public void fifty(int qno, QuestionBank qb, int ans) {
	    System.out.println("Using Fifty Fifty...\n");
	    int cnt = 0;
	    for (int i = 0; i < 4 && cnt < 2; i++) {
	        if (i != ans && !qb.qns[qno].options[i].isEmpty()) {
	            qb.qns[qno].options[i] = "";
	            cnt++;
	        }
	    }

	    ff = 0;
	}
}
