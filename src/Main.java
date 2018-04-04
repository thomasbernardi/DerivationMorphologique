import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import RequeterRezo.*;
import RequeterRezo.Mot;

public class Main {
    public static void main(String args[]) throws IOException, MalformedURLException, InterruptedException{
        Transformation t = new Transformation("er", "ais", PartOfSpeech.VERBE);
        System.out.println( t.apply("parler"));
//        Set<Word> words = Word.motsPossibles("chat");
//        for (Word word : words) {
//
//            System.out.println(word.getPos().name());
//        }
    }
}
