import java.util.Random;
import java.util.concurrent.BrokenBarrierException;



public class Worker implements Runnable {
        private final String name;
        private final GroupActivity group;
        private final Runnable soloTask;
        Worker(String name, GroupActivity group, Runnable soloTask) {
            this.name = name;
            this.group = group;
            this.soloTask = soloTask;
        }
        public void run() {
            System.out.println(name + " starting");
            try {
                while (true) {
                    System.out.println(name + " is waiting for Santa");
                    group.arrive();
                    System.out.println(name + " at " + group);
                    group.leave();
                    System.out.println(name + " is " + soloTask);
                    soloTask.run();
                }
            } catch (InterruptedException ex) {
                // quit
            } catch (BrokenBarrierException ex) {
                // quit
            }
            System.out.println(name + " quitting");
        }

    

    
}