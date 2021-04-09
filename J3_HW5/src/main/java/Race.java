import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Race {

    private ArrayList<Stage> stages;
    private final int carsCount;
    public CyclicBarrier cb1;
    public CyclicBarrier cb2;
    public CountDownLatch cdl;
    public Semaphore smp;
    private final String START_ANNOUNCEMENT = "ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!";
    private final String END_ANNOUNCEMENT = "ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!";

    public Race( int carsCount, Stage...stages){
        this.stages = new ArrayList<>(Arrays.asList(stages));
        this.carsCount = carsCount;
        this.cb1 = new CyclicBarrier(carsCount, ()-> System.out.println(START_ANNOUNCEMENT));
        this.cb2 = new CyclicBarrier(carsCount, ()-> System.out.println(END_ANNOUNCEMENT));
        this.cdl = new CountDownLatch(carsCount);
        this.smp = new Semaphore(carsCount/2, true);
    }

    public ArrayList<Stage> getStages () {
        return stages;
    }
    public int getCarsCount() {
        return this.carsCount;
    }
}
