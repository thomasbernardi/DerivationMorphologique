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
        System.out.println("Enter rules file:");
        String filename = userInput.nextLine();
        FileInputStream fichierRegles = new FileInputStream(filename);
        Regles regles = Regles.lectureRegles(new Scanner(fichierRegles));

        //System.out.println(regles);
        System.out.println("Gimme some words...");

        boolean exit = false;
        while (!exit) {
            String line = userInput.nextLine();
            if (line.equals("x")) exit = true;

            Optional<Set<Word>> applied = regles.apply(line);
            if (applied.isPresent()) {
                int max = applied.get().stream()
                        .max((p1, p2) -> Integer.compare(p1.toString().length(), p2.toString().length()))
                        .get().toString().length();
                applied.get().stream().forEach(
                        el -> System.out.println(el + "   " +
                                String.join("",
                                        Collections.nCopies(max - el.toString().length(), " ")) +
                                " avec la regle: " + el.getTransformation()));
            } else {
                System.out.println("Il apparait qu'il n'y a aucune regle qui correspond a ce mot.");
            }
        }


//        Set<Word> words = Word.motsPossibles("chat");
//        for (Word word : words) {
//
//            System.out.println(word.getPos().name());
//        }
    }
}

//CLASSGRAMATICAL:TERMINAISON;CLASSGRAMMATICAL:TERMINAISON;
