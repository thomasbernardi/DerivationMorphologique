import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.regex.Pattern;

import RequeterRezo.*;
import RequeterRezo.Mot;

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
            System.out.println(Word.motsPossibles(line));
            Optional<Set<Word>> applied = regles.apply(line);
            if (applied.isPresent()) {
                applied.get().stream().forEach(
                        el -> System.out.println(el));
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
