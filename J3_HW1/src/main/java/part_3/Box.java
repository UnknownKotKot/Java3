package part_3;

import java.util.ArrayList;

public class Box <T extends Fruit>{

    private Fruit fruit;
    private float boxWeight;
    private static ArrayList<Fruit> aList = new ArrayList<>();

    public Box(T t, int count) {
        this.fruit = t;
        this.boxWeight = Math.abs(count*t.getPiece_weight());
        aList.add(t);
    }

    public float getWeight(){
        return boxWeight;
    }

    public void setBoxWeight(float boxWeight) {
        this.boxWeight = boxWeight;
    }

    public Fruit getFruit() {
        return fruit;
    }

    public boolean compare(Box<?> b){
        return b.getWeight()==this.getWeight();
    }

    public void addFruit2(Box<?> b){
        if (!(b.getWeight()==0)) {
            if (!(b.getFruit().equals(this.fruit))) {
                if (b.getFruit().getClass().equals(this.fruit.getClass())) {
                    this.boxWeight += b.boxWeight;
                    System.out.println("fruit from box: "
                            + b.getFruit().getName()
                            + ", count:"
                            + b.boxWeight
                            + ", added successfully to box with "
                            + this.fruit.getName());
                    System.out.println("new weight: "
                            + this.getWeight());
                    b.setBoxWeight(0);
                } else System.out.println("wrong fruit");
            } else System.out.println("wrong box");
        }else System.out.println("empty box");
    }
}
