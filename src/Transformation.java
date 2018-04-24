public class Transformation {
    private int endLength;
    private String ending;
    private String newEnding;
    private PartOfSpeech pos;
    private PartOfSpeech newPos;
    public Transformation(String ending, PartOfSpeech pos, String newEnding, PartOfSpeech newPos) {
        this.ending = ending;
        this.endLength = ending.length();
        this.pos = pos;
        this.newPos = newPos;
        this.newEnding = newEnding;
    }

    public Word apply(String mot) {
        String base = mot.substring(0, mot.length() - endLength);
        return new Word(base + newEnding, newPos, this);
    }

    public String getEnding() { return ending; }

    public PartOfSpeech getPos() { return pos; }

    public String toString() {
        return "-" + ending + " comme " + pos.name() + " => -" + newEnding + " comme " + newPos.name();
    }
}
