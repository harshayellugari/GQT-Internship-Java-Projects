package main;

public class Question {
    String qn;
    String[] options;
    char answer;
    int prize;

    public Question(String qn, String[] options, char answer, int prize) {
        this.qn = qn;
        this.options = options;
        this.answer = answer;
        this.prize = prize;
    }
}
