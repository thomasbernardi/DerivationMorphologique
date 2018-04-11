import java.util.Map;
import java.util.Set;
import java.util.regex.*;
import java.util.stream.Collectors;

public class Regle {
    private String ending;
    private String pattern;
    private Map<PartOfSpeech, Set<Transformation>> transformations;

    public Regle(String ending, Map<PartOfSpeech, Set<Transformation>> transformations) {
        this.ending = ending;
        pattern = "[a-zA-Z]*" + ending;
        this.transformations = transformations;
    }

    /**
     *
     * @param mot
     * @param pos
     * @return vrai ssi cette regle applique au mot avec un des POS donnes
     */
    public boolean match (String mot, Set<PartOfSpeech> pos) {
        if (Pattern.matches(pattern, mot)) {
            return transformations.keySet().stream()
                    .map(el -> pos.contains(el))
                    .reduce(false, (acc, el) -> acc || el);
        } else {
            return false;
        }
    }

    public boolean match (Word mot) {
        return Pattern.matches(pattern, mot.getNom())
                && transformations.containsKey(mot.getPos());
    }

    /**
     *
     * @param mot qui va etre tranforme
     * @return ensemble de tous les tranformations de "mot" selon cette regle
     */
    public Set<Transformation> apply(Word mot) {
        return transformations.get(mot.getPos());
    }

    public String toString() {
        return ending + " -- " + transformations;
    }
}
