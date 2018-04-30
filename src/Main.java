import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.regex.Pattern;

import RequeterRezo.*;
import RequeterRezo.Mot;

/**
 * Il faut remplacer la fin du mot et pas juste concatener à la suite
 *
 * il faut vérifier chaque mot en sortie si il existe dans rézodump si il existe on le précise
 *
 * les regles seront trié par ordre croissant, on traite la première regles donc la + longue ensuite pour toutes les autres regles
 *
 * si une regle se trouve dans la regle initiale on l'applique pas
 *
 * Ex : Manger
 * 1 er regle ( la + longue) ver:ger;nom:geur
 * 2nd regle (ver:er;nom:ant) on l'ignore car 'er' exist in 'ger'
 */
public class Main {
    public static void main(String args[]) throws IOException, MalformedURLException, InterruptedException{
        Scanner userInput = new Scanner(System.in);
//        System.out.println("Test:");
//        String test = userInput.nextLine();
//        String[] arr = test.split(":");
//        System.out.println(Arrays.toString(arr));
        System.out.println("Enter rules file:");
        String filename = userInput.nextLine();
        FileInputStream fichierRegles = new FileInputStream(filename);
        Regles regles = Regles.lectureRegles(new Scanner(fichierRegles));

        //System.out.println(regles);
        System.out.println("Gimme some words...");

        while (true) {
            String line = userInput.nextLine();
            if (line.equals("x")) break;

            Optional<Set<Word>> applied = regles.apply(line);
            if (applied.isPresent()) {
                int max = applied.get().stream()
                        .max((p1, p2) -> Integer.compare(p1.toString().length(), p2.toString().length()))
                        .get().toString().length();
                applied.get().stream().forEach(
                        el -> {
                            System.out.println(el + "   " +
                                    String.join("",
                                            Collections.nCopies(max - el.toString().length(), " ")) +
                                    " avec la regle: " + el.getTransformation());
                            System.out.println(el.wordExists() ? "Such a word exists" : "DNE");
                            if (el.appliedTransformation().isPresent()) {
                                Optional<List<String>> semantiquePossible = el.getSemantique();
                                Optional<List<Boolean>> semantiqueVerifie = el.compareSemantique();
                                if (semantiquePossible.isPresent() && semantiqueVerifie.isPresent() &&
                                        semantiquePossible.get().size() > 0) {
                                    for (int i = 0; i < semantiquePossible.get().size(); i++) {
                                        System.out.println("semantique attendu : " + semantiquePossible.get().get(i) +
                                                " -::- " + (semantiqueVerifie.get().get(i) ? "est valide" : "n'est pas valide"));
                                    }
                                } else {
                                    System.out.println("Pas de semantique specifie");
                                }

                            }
                            System.out.println();
                        });
            } else {
                System.out.println("Il apparait qu'il n'y a aucune regle qui correspond a ce mot.");
            }
            System.out.println("Next word...");
        }


//        Set<Word> words = Word.motsPossibles("chat");
//        for (Word word : words) {
//
//            System.out.println(word.getPos().name());
//        }
    }
}

//CLASSGRAMATICAL:TERMINAISON;CLASSGRAMMATICAL:TERMINAISON;
