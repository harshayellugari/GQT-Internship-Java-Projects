package main;

import java.util.Scanner;

public class KBCMain {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner ob = new Scanner(System.in);
		int start;
		int prize = 0;
		while(true) {
			System.out.println("Welocome to the Quizzzz!!!");
			System.out.println("1. Enter the quiz\n2. Exit the Quiz");
			System.out.print("Choose option : ");
			start = ob.nextInt();
			if(start == 1 || start == 2) {
				break;
			}
			else {
				System.out.println("Invalid option!!!");
				System.out.print("Enter valid option : ");
				start = ob.nextInt();
			}
		}
		if(start == 2) {
			System.out.println("Thank you !! Exiting the Quizzzz");
			ob.close();
			return;
		}
		QuestionBank qb = new QuestionBank();
		LifeLine ll = new LifeLine();
		for(int i = 0; i<qb.qns.length; i++) {
			char answer;
			qb.qns[i].display(i);
			if(i != 15) {
				System.out.println("\nE. Leave with current amount of "+prize+ ".\nL. Use Lifelines.\n");
	            System.out.print("Enter your answer (A/B/C/D or H/L): ");
	            answer = Character.toLowerCase(ob.next().charAt(0));
	            if (answer == 'l') {
	            	boolean avail = ll.showLifeLines(i, qb);
	            	if(avail) {
	            		ob.nextLine();
	            		qb.qns[i].display(i);
	                    System.out.print("Now Enter your answer (A/B/C/D): ");
	                    answer = Character.toLowerCase(ob.next().charAt(0));
	            	}
	            	else {
	            		ob.nextLine();
	            		System.out.println("No available lifelines..");
	                    System.out.print("Enter your answer (A/B/C/D): ");
	                    answer = Character.toLowerCase(ob.next().charAt(0));
	            	}
	            }
			}
			else {
				System.out.print("Enter your answer (A/B/C/D) for the Final Question : ");
	            answer = Character.toLowerCase(ob.next().charAt(0));
			}
            
            if(answer == 'e') {
            	System.out.println("Congrats! you won : " + prize);
            	ob.close();
            	return;
            }
            
            if (answer == qb.qns[i].answer) {
            	System.out.println(ANSI_GREEN + "Correct Answer" + ANSI_RESET);
            	prize = qb.qns[i].prize;
            } else {
                System.out.println(ANSI_RED + "Wrong Answer!" + ANSI_RESET);
                System.out.println("Game Over!! Better luck next time.. "); 
                ob.close();
                return;
            }

            System.out.println("--------------------------------");
		}
		System.out.println("Congratulations!!! You become Crorepati ");
		ob.close();
	}

}
