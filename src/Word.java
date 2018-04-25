import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import RequeterRezo.*;
import RequeterRezo.Mot;

public class Word {
    private String nom;
    private PartOfSpeech pos;
    private Optional<Transformation> applied;

    public Word(String nom, PartOfSpeech pos) {
        this.nom = nom;
        this.pos = pos;
        applied = Optional.empty();
    }

    public Word(String nom, PartOfSpeech pos, Transformation transformation) {
        this.nom = nom;
        this.pos = pos;
        applied = Optional.of(transformation);
    }

    public String getNom() {
        return this.nom;
    }

    public String toString() { return nom + " " + this.pos.name(); }

    public Optional<Transformation> appliedTransformation() { return applied; }

    public String getTransformation() {
        if (applied.isPresent()) {
            return applied.get().toString();
        } else {
            return "None";
        }
    }

    public boolean wordExists() {
        Set<Word> words = motsPossibles(this.nom);
        if (words.size() == 0) {
            return false;
        } else {
            boolean exists = false;
            for (Word word : words) {
                exists = exists || this.getPos().equals(word.getPos());
            }
            return exists;
        }
    }

    public Optional<List<Boolean>> compareSemantique() {
        Optional<Mot> mot = getMot(getNom());
        if (applied.isPresent() && mot.isPresent()){
            List<Boolean> result = applied.get().getSemantique()
                    .stream()
                    .map(el -> !mot.get().getRelations_sortantes().get(el).isEmpty())
                    .collect(Collectors.toList());
            return Optional.of(result);
        } else {
            return Optional.empty();
        }
    }

    public PartOfSpeech getPos() {
        return pos;
    }

    public static Optional<Mot> getMot(String nom) {
        RequeterRezo systeme = new RequeterRezo("36h", 3000);

        Optional<Mot> mot = Optional.empty();
        try {
            Mot requete = systeme.requete(nom);
            if (requete != null) mot = Optional.of(requete);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mot;
    }

    public static Set<Word> motsPossibles(String nom) {

        Optional<Mot> mot = getMot(nom);

        if (mot.isPresent()) {
            List<Terme> lemmaList = mot.get().getRelations_sortantes().get("r_lemma");
            String lemma = null;
            if (lemmaList != null && lemmaList.size() > 0) {
                lemma = lemmaList.get(0).getTerme();
            }
            Set<Word> lemmas = null;
            if (lemma != null && !nom.equals(lemma)) {
                lemmas = motsPossibles(lemma);
            }
            List<Terme> pos = mot.get().getRelations_sortantes().get("r_pos");
            Set<Word> words = pos.stream()
                    .map(terme -> PartOfSpeech.posOfString(terme.getTerme()))
                    .distinct()
                    .filter(terme -> terme.isPresent())
                    .map(terme -> new Word(nom, terme.get()))
                    .collect(Collectors.toSet());
            if (lemmas != null) {
                lemmas = lemmas.stream()
                        .filter(el -> el.getPos().equals(PartOfSpeech.VERBE))
                        .collect(Collectors.toSet());
                if (lemmas.size() > 0) {
                    words = words.stream()
                            .filter(el -> !el.getPos().equals(PartOfSpeech.VERBE))
                            .collect(Collectors.toSet());
                    words.addAll(lemmas);
                }
            }
            return words;
        } else {
            return new HashSet<>();
        }
    }

    public static String getLemme(String nom) {
        RequeterRezo systeme = new RequeterRezo("36h", 3000);

        Optional<Mot> mot = Optional.empty();
        try {
            Mot requete = systeme.requete(nom);
            if (requete != null) mot = Optional.of(requete);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mot.isPresent()) {
            List<Terme> pos = mot.get().getRelations_sortantes().get("r_lemma");
            return pos.get(0).getTerme();
        } else {
            return "None";
        }
    }
}
