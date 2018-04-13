import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import RequeterRezo.*;
import RequeterRezo.Mot;

public class Word {
    private String nom;
    private PartOfSpeech pos;

    public Word(String nom, PartOfSpeech pos) {
        this.nom = nom;
        this.pos = pos;
    }

    public String getNom() {
        return this.nom;
    }

    public String toString() { return nom + " " + this.pos.name(); }

    public PartOfSpeech getPos() {
        return pos;
    }

    public static Set<Word> motsPossibles(String nom) {
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
            List<Terme> pos = mot.get().getRelations_sortantes().get("r_pos");
            return pos.stream()
                    .map(terme -> PartOfSpeech.posOfString(terme.getTerme()))
                    .distinct()
                    .filter(terme -> terme.isPresent())
                    .map(terme -> new Word(nom, terme.get()))
                    .collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }
}
