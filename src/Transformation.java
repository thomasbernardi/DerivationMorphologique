import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Transformation {
    private int endLength;
    private String ending;
    private String newEnding;
    private PartOfSpeech pos;
    private PartOfSpeech newPos;
    private List<String> semantique;
    public Transformation(String ending, PartOfSpeech pos, String newEnding, PartOfSpeech newPos, List<String> semantique) {
        this.ending = ending;
        this.endLength = ending.length();
        this.pos = pos;
        this.newPos = newPos;
        this.newEnding = newEnding;
        if (semantique == null) {
            this.semantique = new ArrayList<>();
        } else {
            this.semantique = semantique.stream()
                    .filter(el -> el.length() > 0)
                    .collect(Collectors.toList());
        }
    }

    public Word apply(String mot) {
        String base = mot.substring(0, mot.length() - endLength);
        return new Word(base + newEnding, newPos, this);
    }

    public List<String> getSemantique() {
        return semantique;
    }

    public String getEnding() { return ending; }

    public PartOfSpeech getPos() { return pos; }

    public String toString() {
        return "-" + ending + " comme " + pos.name() + " => -" + newEnding + " comme " + newPos.name() + " avec semantique " +
                getSemantique();

    }
}
