package main;

public class QuestionBank {

    public Question[] qns = new Question[16];

    public QuestionBank() {
        qns[0] = new Question(
                "Who is the main protagonist of Attack on Titan?",
                new String[]{"Levi Ackerman", "Eren Yeager", "Armin Arlert", "Erwin Smith"},
                'b', 1000);

        qns[1] = new Question(
                "What is the name of the city where Eren was born?",
                new String[]{"Wall Sina", "Wall Rose", "Shiganshina", "Trost"},
                'c', 2000);

        qns[2] = new Question(
                "Which Titan can harden its body?",
                new String[]{"Colossal Titan", "Armored Titan", "Cart Titan", "Jaw Titan"},
                'b', 3000);

        qns[3] = new Question(
                "Who killed Eren Yeager?",
                new String[]{"Mikasa Ackerman", "Levi Ackerman", "Reiner Braun", "Armin Arlert"},
                'a', 5000);

        qns[4] = new Question(
                "What is the name of the military group that fights Titans outside the walls?",
                new String[]{"Garrison", "Military Police", "Survey Corps", "Royal Guard"},
                'c', 10000);

        qns[5] = new Question(
                "Which Titan is known for its enormous size?",
                new String[]{"Beast Titan", "Armored Titan", "Colossal Titan", "Attack Titan"},
                'c', 20000);

        qns[6] = new Question(
                "Who is the captain of the Levi Squad?",
                new String[]{"Erwin Smith", "Levi Ackerman", "Hange Zoe", "Mikasa Ackerman"},
                'b', 40000);

        qns[7] = new Question(
                "What power does the Attack Titan possess?",
                new String[]{"Future memories", "Hardening", "Flight", "Regeneration"},
                'a', 80000);

        qns[8] = new Question(
                "Which character inherits the Colossal Titan?",
                new String[]{"Reiner Braun", "Bertholdt Hoover", "Armin Arlert", "Zeke Yeager"},
                'c', 160000);

        qns[9] = new Question(
                "Who is Eren Yeager's half-brother?",
                new String[]{"Reiner Braun", "Zeke Yeager", "Falco Grice", "Porco Galliard"},
                'b', 320000);

        qns[10] = new Question(
                "What is the name of Eren's Titan form?",
                new String[]{"Founding Titan", "War Hammer Titan", "Attack Titan", "Beast Titan"},
                'c', 640000);

        qns[11] = new Question(
                "Which Titan can control other Titans?",
                new String[]{"Attack Titan", "Colossal Titan", "Founding Titan", "Jaw Titan"},
                'c', 1250000);

        qns[12] = new Question(
                "Who is the commander that sacrificed himself against the Beast Titan?",
                new String[]{"Levi Ackerman", "Erwin Smith", "Hange Zoe", "Keith Shadis"},
                'b', 2500000);

        qns[13] = new Question(
                "What is the name of the event where Eren destroys the world?",
                new String[]{"The Fall", "The March", "The Rumbling", "The Collapse"},
                'c', 5000000);

        qns[14] = new Question(
                "Which clan does Mikasa belong to?",
                new String[]{"Yeager", "Reiss", "Ackerman", "Tybur"},
                'c', 7500000);

        qns[15] = new Question(
                "Which character says: 'If you don't fight, you can't win'?",
                new String[]{"Levi Ackerman", "Eren Yeager", "Erwin Smith", "Armin Arlert"},
                'b', 10000000);
    }
}
