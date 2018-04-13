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

    public Optional<Set<Transformation>> apply (String mot) {

        Optional<Set<Transformation>> result = Optional.empty();
        Set<Word> possible = Word.motsPossibles(mot);
        System.out.println(possible);

        if (casParticuliers.containsKey(mot)) {
            Set<Transformation> s = new HashSet<>();
            s.add(casParticuliers.get(mot));
            result = Optional.of(s);
        } else {
            Set<Transformation> transformed = new HashSet<>();
            for (int i = 0; i < regles.size() && !result.isPresent(); i++) {
                Regle r = regles.get(i);
                for (Iterator<Word> it = possible.iterator(); it.hasNext();) {
                    Word w = it.next();
                    if (r.match(w)) {
//                        System.out.println(r);
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
            Optional<PartOfSpeech> posIn = Optional.empty();
            Optional<PartOfSpeech> posOut = Optional.empty();
            String[] split = curr.split(";");
            String terminaisonIn = "";
            String terminaisonOut = "";
            if (split.length == 2) {
                String[] input = split[0].split(":");
                String[] output = split[1].split(":");
                if (input.length > 0 && output.length > 0) {
                    terminaisonIn = input.length > 1 ? input[1] : "";
                    posIn = PartOfSpeech.posOfString(input[0]);
                    terminaisonOut = output.length > 1 ? output[1] : "";
                    posOut = PartOfSpeech.posOfString(output[0]);
                } else {
                    logger.warning("regle malforme : " + curr);
                }
            } else {
                logger.warning("regle malforme : " + curr);
            }



            if (posIn.isPresent() && posOut.isPresent()) {
                //forward
                if (!map.containsKey(terminaisonIn)) {
                    map.put(terminaisonIn, new HashMap<>());
                }
                Map<PartOfSpeech, Set<Transformation>> forward = map.get(terminaisonIn);
                if (!forward.containsKey(posIn.get())) {
                    forward.put(posIn.get(), new HashSet<>());
                }
                forward.get(posIn.get()).add(new Transformation(terminaisonIn, posIn.get(), terminaisonOut, posOut.get()));

                //backwards
                if (!map.containsKey(terminaisonOut)) {
                    map.put(terminaisonOut, new HashMap<>());
                }
                Map<PartOfSpeech, Set<Transformation>> backwards = map.get(terminaisonOut);
                if (!backwards.containsKey(posOut.get())) {
                    backwards.put(posOut.get(), new HashSet<>());
                }

                backwards.get(posOut.get()).add(new Transformation(terminaisonOut, posOut.get(), terminaisonIn, posIn.get()));

            } else {
                logger.warning("One of the provided parts of speech did not match POS in system in rule : " + curr);
            }
        }
        List<Regle> regles = map.keySet().stream()
                .map(terminaison -> new Regle(terminaison, map.get(terminaison)))
                .sorted((p1, p2) -> Integer.compare(p1.size(), p2.size()))
                .collect(Collectors.toList());
        regles.stream().forEach(el -> System.out.println(el));
        return new Regles(new HashMap<>(), regles);
    }

}
