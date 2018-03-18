public class Transformation {
    private String ending;
    private String transformation;
    private PartOfSpeech newPos;

    public Transformation(String ending, PartOfSpeech pos, String transformation, PartOfSpeech newPos) {
        this.ending = ending;
        this.transformation = transformation;
        this.newPos = newPos;
    }

    /**
     *
     * @param mot qui va etre tranforme
     * @return ensemble de tous les tranformations de "mot" selon cette regle
     */
    public Mot apply(String mot) {
        String base = mot.substring(0, mot.length() - ending.length());
        String transformed = base + transformation;
        return new Mot(transformed, newPos);
    }
}
