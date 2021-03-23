package part_3;

public class Orange extends Fruit{

    private String name;
    private final float PIECE_WEIGHT = 1.5f;

    public Orange(String name) {
        this.name = name;
        super.setPiece_weight(PIECE_WEIGHT);
    }

    public String getName() {
        return name;
    }

    public float getPIECE_WEIGHT() {
        return PIECE_WEIGHT;
    }
}
