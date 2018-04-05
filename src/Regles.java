import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Regles {
    private Map<String, Transformation> casParticuliers;
    private List<Regle> regles;
    public Regles(Map<String, Transformation> casParticuliers, List<Regle> regles) {
        this.casParticuliers = casParticuliers;
        this.regles = regles;
    }

    public Optional<Set<Word>> apply (String mot) {

        Optional<Set<Word>> result = Optional.empty();
        Set<Word> possible = Word.motsPossibles(mot);

        if (casParticuliers.containsKey(mot)) {
            Set<Word> s = new HashSet<>();
            s.add(casParticuliers.get(mot).apply(mot));
            result = Optional.of(s);
        } else {
            Set<Word> transformed = new HashSet<>();
            for (int i = 0; i < regles.size() && !result.isPresent(); i++) {
                Regle r = regles.get(i);
                for (Iterator<Word> it = possible.iterator(); it.hasNext();) {
                    Word w = it.next();
                    if (r.match(w)) {
                        System.out.println(r);
                        transformed.addAll(r.apply(w));
                        it.remove();
                    }
                }
            }
            if (!transformed.isEmpty()) {
                result = Optional.of(transformed);
            }
        }
        return result;
    }

    public String toString() {
        return regles.toString();
    }

    public static Regles lectureRegles(Scanner in) {
        Logger logger = Logger.getLogger("LectureRegles");
        Map<String, Map<PartOfSpeech, Set<Transformation>>> map = new HashMap<>();

        while (in.hasNextLine()) {

            String curr = in.nextLine();
            String[] split = curr.split(";");
            String[] input = split[0].split(":");
            String[] output = split[1].split(":");
            String terminaisonIn = input.length > 1 ? input[1] : "";
            Optional<PartOfSpeech> posIn = PartOfSpeech.posOfString(input[0]);
            String terminaisonOut = output.length > 1 ? output[1] : "";
            Optional<PartOfSpeech> posOut = PartOfSpeech.posOfString(output[0]);

            if (posIn.isPresent() && posOut.isPresent()) {
                //forward
                if (!map.containsKey(terminaisonIn)) {
                    map.put(terminaisonIn, new HashMap<>());
                }
                Map<PartOfSpeech, Set<Transformation>> forward = map.get(terminaisonIn);
                if (!forward.containsKey(posIn.get())) {
                    forward.put(posIn.get(), new HashSet<>());
                }
                forward.get(posIn.get()).add(new Transformation(terminaisonIn, terminaisonOut, posOut.get()));

                //backwards
                if (!map.containsKey(terminaisonOut)) {
                    map.put(terminaisonOut, new HashMap<>());
                }
                Map<PartOfSpeech, Set<Transformation>> backwards = map.get(terminaisonOut);
                if (!backwards.containsKey(posOut.get())) {
                    backwards.put(posOut.get(), new HashSet<>());
                }

                backwards.get(posOut.get()).add(new Transformation(terminaisonOut, terminaisonIn, posIn.get()));

            } else {
                logger.warning("One of the provided parts of speech did not match POS in system.");
            }
        }
        List<Regle> regles = map.keySet().stream()
                .map(terminaison -> new Regle(terminaison, map.get(terminaison)))
                .collect(Collectors.toList());
        return new Regles(new HashMap<>(), regles);
    }

}
