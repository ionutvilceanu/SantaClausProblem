

import java.util.Deque;
import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
 


public class SantaClausProblem {

    static final int ELF_QUORAM = 3;
    static final int NUM_ELVES = 10;
    static final int NUM_REINDEER = 9;

    /** Simulates the Santa Claus Problem. */
    public static void main(String[] unused) throws InterruptedException {

        ExecutorService executor = Executors.newCachedThreadPool();

        BlockingDeque<Runnable> deque = new LinkedBlockingDeque<Runnable>();

        Runnable deliverToys = makeRandomDelay(1000, "delivering toys");
        Runnable onHoliday = makeRandomDelay(2000, "on holiday");
        Runnable designToys = makeRandomDelay(500, "designing toys");
        Runnable makeToys = makeRandomDelay(1000, "making toys");

        GroupActivity sleigh = new GroupActivity(
                "sleigh", NUM_REINDEER, deque, true, deliverToys);

        for (int i = 0; i < NUM_REINDEER; i++)
            executor.execute(new Worker("reindeer-"+(i+1), sleigh, onHoliday));

        GroupActivity meeting = new GroupActivity(
                "meeting", ELF_QUORAM, deque, false, designToys);

        for (int i = 0; i < NUM_ELVES; i++)
            executor.execute(new Worker("elf-"+(i+1), meeting, makeToys));

        executor.execute(new Santa(deque));

        // Run simulation for 30 seconds and then shut it down.
        Thread.sleep(30000);
        executor.shutdownNow();
        executor.awaitTermination(2, TimeUnit.SECONDS);
    }
    /** Represents a time-consuming activity. */
    static Runnable makeRandomDelay(final int delay, final String name) {
    	final Random RAND = new Random();
        return new Runnable() {
            public void run() {
                try {
                    Thread.sleep(RAND.nextInt(delay));
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // propagate
                }
            }
            @Override public String toString() {
                return name;
            }
        };
    }
}
