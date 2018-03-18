public class Mot {
    private String nom;
    private PartOfSpeech pos;
    public Mot(String nom, PartOfSpeech pos) {
        this.nom = nom;
        this.pos = pos;
    }

    public String getNom() {
        return this.nom;
    }

    public PartOfSpeech getPos() {
        return pos;
    }
}
