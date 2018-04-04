import java.util.Optional;

public enum PartOfSpeech {
    VERBE, NOM, ADJECTIF, ADVERBE, PRONOM, CONJONCTION;
    public static Optional<PartOfSpeech> posOfString(String rep) {
        String rep_ = rep.split(":")[0].toLowerCase();
        if (rep_.equals("nom")) {
            return Optional.of(NOM);
        } else if (rep_.equals("adj")) {
            return Optional.of(ADJECTIF);
        } else if (rep_.equals("ver")) {
            return Optional.of(VERBE);
        } else if (rep_.equals("adv")) {
            return Optional.of(ADVERBE);
        } else if (rep_.equals("pro")) {
            return Optional.of(PRONOM);
        } else if (rep_.equals("conj")) {
            return Optional.of(CONJONCTION);
        } else {
            return Optional.empty();
        }
    }
}
