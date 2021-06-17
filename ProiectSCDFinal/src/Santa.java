import java.util.concurrent.BlockingQueue;

public class Santa implements Runnable {
        private final BlockingQueue<Runnable> queue;
        Santa(BlockingQueue<Runnable> queue) {
            this.queue = queue;
        }
        public void run() {
            try {
                while (true) {
                    System.out.println("Santa is sleeping");
                    queue.take().run();
                }
            } catch (InterruptedException ex) {
                // quit
            }
            System.out.println("Santa is done");
        }
    }