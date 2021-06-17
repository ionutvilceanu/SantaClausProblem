import java.util.Deque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class GroupActivity{
    private final String name;
    private final Semaphore permit;
    private final CyclicBarrier groupBarrier;
    private final CyclicBarrier entryBarrier;
    private final CyclicBarrier exitBarrier;

    GroupActivity(String name,
            final int size,
            final Deque<Runnable> deque,
            final boolean prepend, // addFirst if high-priority
            final Runnable action) {

        this.name = name;
        permit = new Semaphore(size, true); // fair
        entryBarrier = new CyclicBarrier(size + 1);
        exitBarrier = new CyclicBarrier(size + 1);

        // Wake up Santa when a sufficiently large group has formed
        groupBarrier = new CyclicBarrier(size, new Runnable() {
            public void run() {
                Runnable task = new Runnable() {
                    public void run() {
                        try {
                            entryBarrier.await(); // hitch
                            permit.release(size); // form new team
                            System.out.println("Santa is " + action);
                            action.run();
                            exitBarrier.await();  // unhitch
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        } catch (BrokenBarrierException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                };
                if (prepend) {
                    deque.addFirst(task);
                } else {
                    deque.addLast(task);
                }
            }
        });
    }

    void arrive() throws InterruptedException, BrokenBarrierException {
        permit.acquire();       // join team
        groupBarrier.await();   // wakeup Santa
        entryBarrier.await();   // hitch and go
    }

    void leave() throws InterruptedException, BrokenBarrierException {
        exitBarrier.await();    // unhitch
    }

    @Override public String toString() {
        return name;
    }
}
