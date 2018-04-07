package FightClub.Extra;

import robocode.*;

import java.util.concurrent.ThreadLocalRandom;

public class EmotionEngine {

    //VARS for Leader:
    double O, C, E, A, N;
    double pleasure, arousal, dominance;
    int emotion; // Carly Rae Jepsen's Iconic Album!
    //Joy=1
    //Pride=2
    //Fear=3
    //Anger=4


    @Override
    public String toString() {
        return "EmotionEngine{" +

                "pleasure=" + pleasure +
                ", arousal=" + arousal +
                ", dominance=" + dominance +
                ", emotion=" + emotion +
                '}';
    }

    public int getEmotion() {
        return emotion;
    }

    public EmotionEngine(double o, double c, double e, double a, double n) {
        O = o;
        C = c;
        E = e;
        A = a;
        N = n;
        pleasure = 0.59 * A + 0.19 * N + 0.21 * E;
        arousal = 0.57 * N + 0.30 * A + 0.15 * O;
        dominance = 0.60 * N + 0.32 * A + 0.25 * O;
        //Define emotion from
        updateMood();
    }


    public void updateMood() {
        System.out.println(this.toString());
        if (pleasure >= 0.5 && pleasure <= 1 && arousal >= 0 && arousal <= 0.60) {
            emotion = 1;
        } else if (arousal >= 0.1 && arousal <= 0.70 && dominance >= 0.30 && dominance <= 1) {
            emotion = 2;
        } else if (pleasure >= -1 && pleasure <= -0.3 && arousal <= -0.5 && dominance < -0.5) {
            emotion = 3;
        } else if (arousal >= -0.7 && arousal <= 0.3 && dominance >= 0.5 && dominance <= 0.5) {
            emotion = 4;
        } else {
            emotion = ThreadLocalRandom.current().nextInt(0, 5);
        }


    }

    public void modifyP(double p) {
        if ((p + pleasure) > 1) {
            pleasure = 1;
        } else if ((p + pleasure) < 1) {
            pleasure = -1;
        } else pleasure = pleasure + p;

        updateMood();
    }

    public void modifyA(double a) {
        if ((a + arousal) > 1) {
            arousal = 1;
        } else if ((a + arousal) < 1) {
            arousal = -1;
        } else arousal = arousal + a;
        updateMood();

    }

    public void modifyD(double d) {
        if ((d + dominance) > 1) {
            dominance = 1;
        } else if ((d + dominance) < 1) {
            dominance = -1;
        } else dominance = dominance + d;
        updateMood();

    }


}
