public class Transformation {
    private int ending;
    private String newEnding;
    private PartOfSpeech newPos;
    public Transformation(String ending, String newEnding, PartOfSpeech newPos) {
        this.ending = ending.length();
        this.newPos = newPos;
        this.newEnding = newEnding;
    }

    public Word apply(String mot) {
        String base = mot.substring(0, mot.length() - ending);
        return new Word(base + newEnding, newPos);
    }

    public String toString() {
        return ending + " -> " + newEnding + " :: " + newPos.name();
    }
}
