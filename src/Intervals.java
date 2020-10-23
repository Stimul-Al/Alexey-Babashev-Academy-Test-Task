import java.util.HashMap;
import java.util.Map;

public class Intervals {

    private static final String[] NOTE = {"C", "D","E","F","G","A","B"};
    private static Map<String, Integer> intervals = intervalsNote();
    private static Map<String, Integer> octave = getOctave();
    private static final double SEMITONE = 0.5;
    private static final double TONE = 1;
    private static final int LENGTH_OCTAVE = 7;
    private static String sharp = "#";
    private static String flat = "b";

    public static String intervalConstruction(String[] args) {
        double startPositionNote = getPositionNote(args[1]);

        int countSemitone = intervals.get(args[0]);

        int startPosNote = octave.get(args[1].substring(0, 1));

        int interval = Integer.parseInt(args[0].substring(1, 2)) - 1;

        String answerNote = getAnswerNote(startPosNote, interval, getDirection(args));

        double finishPositionNote = getFinishPositionNoteWithSign(getDirection(args), startPositionNote, countSemitone);

        int positionAnswerNote = octave.get(answerNote);

        return finalResult(finishPositionNote, positionAnswerNote, answerNote);
    }

    public static String intervalIdentification(String[] args) {
        double startPosNote = 0;
        double finishPosNote = 0;

        if (getDirection(args)) {
            startPosNote = getPositionNote(args[0]);
            finishPosNote = getPositionNote(args[1]);
        } else {
            startPosNote = getPositionNote(args[1]);
            finishPosNote = getPositionNote(args[0]);
        }

        int semitoneCount = 0;
        while(startPosNote != finishPosNote) {
            if (!(startPosNote == 2.5 & semitoneCount != 0)) {
                semitoneCount++;
            }
            startPosNote += SEMITONE;
            if (startPosNote == 6.5) {
                startPosNote = 0;
            }
        }

        String interval = getNameInterval(semitoneCount);
        return interval;
    }

    private static String getNameInterval(int semitoneInterval) {
        String interval = "";
        for (Map.Entry<String, Integer> entry : intervals.entrySet()) {
            if (entry.getValue() == semitoneInterval) {
                interval += entry.getKey();
                break;
            }
        }
        return interval;
    }

    private static double getPositionNote(String arg) {
        double startPos = 0;
        String altSign = "";
        int countSign = 0;
        if (arg.length() > 1) {
            altSign = arg.substring(1, 2);
            countSign = arg.length() == 3? 2 : 1;
        }
        String note = arg.substring(0, 1);
        startPos += octave.get(note);
        while(countSign > 0) {
            startPos += altSign.equals(flat) ? -SEMITONE : (altSign.equals(sharp) ? SEMITONE : 0);
            countSign--;
        }
        startPos = startPos == -0.5? 6 : (startPos == 6.5 ? 0 : startPos);
        return startPos;
    }

    private static boolean getDirection(String[] args) {
        boolean direction = true;
        if(args.length == 3 && args[2].equals("dsc")) {
            direction = false;
        }
        return direction;
    }

    private static String getAnswerNote(int startPosNote, int interval, boolean sort) {
        String finishNote;

        int finishPosNote = getFinishPositionNoteWithoutSign(startPosNote, interval, sort);

        finishNote = NOTE[finishPosNote];

        return finishNote;
    }

    private static int getFinishPositionNoteWithoutSign(int startPos, int interval, boolean sortAsc) {
        int finishPos = 0;
        if (sortAsc) {
            finishPos = startPos + interval;
            finishPos -= finishPos > 6 ? LENGTH_OCTAVE : 0;
        } else {
            finishPos = startPos - interval;
            finishPos += finishPos < 0 ? LENGTH_OCTAVE : 0;
        }
        return finishPos;
    }

    private static String finalResult(double finishPositionNote, double positionAnswerNote, String answerNote) {
        String finalResult = answerNote;

        double difference = finishPositionNote - positionAnswerNote;
        while (difference != 0) {
            finalResult += difference > 0 ? sharp : flat;
            difference += difference > 0 ? -SEMITONE : SEMITONE;
        }

        return finalResult;
    }

    private static double getFinishPositionNoteWithSign(boolean sort, double startPositionNote, int countSemitone) {
        double finishPos = startPositionNote;

        if (sort) {
            while (countSemitone > 0) {
                finishPos += SEMITONE;
                if (!(finishPos == 2.5 || finishPos == 6.5)) {
                    countSemitone--;
                }
                finishPos -= finishPos > LENGTH_OCTAVE ? LENGTH_OCTAVE : 0;
            }
        } else {
            while (countSemitone > 0) {
                finishPos -= SEMITONE;
                if (!(finishPos == 2 || finishPos == -0.5)) {
                    countSemitone--;
                }
                finishPos += finishPos < 0 ? LENGTH_OCTAVE : 0;
            }
        }


        return finishPos;
    }

    private static Map<String, Integer> getOctave() {
        Map<String, Integer> octave = new HashMap<>();
        octave.put("C", 0);
        octave.put("D", 1);
        octave.put("E", 2);
        octave.put("F", 3);
        octave.put("G", 4);
        octave.put("A", 5);
        octave.put("B", 6);
        return octave;
    }

    private static Map<String, Integer> intervalsNote() {
        Map<String, Integer> intervalsMap = new HashMap<>();
        intervalsMap.put("m2", 1);
        intervalsMap.put("M2", 2);
        intervalsMap.put("m3", 3);
        intervalsMap.put("M3", 4);
        intervalsMap.put("P4", 5);
        intervalsMap.put("P5", 7);
        intervalsMap.put("m6", 8);
        intervalsMap.put("M6", 9);
        intervalsMap.put("m7", 10);
        intervalsMap.put("M7", 11);
        intervalsMap.put("P8", 12);
        return intervalsMap;
    }

    public static void main(String[] args) {
        String[] task1 = {"M2", "F", "dsc"};
        String[] task2 = {"F", "D#", "dsc"};
        System.out.println(intervalConstruction(task1));
        System.out.println(intervalIdentification(task2));
    }
}
