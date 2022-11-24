import java.util.*;
import java.util.concurrent.*;


public class Test {

    public static void main(String[] args) throws InterruptedException {
      ProducerConsumer pc = new ProducerConsumer();


        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pc.produce();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pc.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

    }
}
class ProducerConsumer{
    private Queue<Integer> queue = new LinkedList<Integer>();
    private final int LIMIT = 10;
    private final  Object lock = new Object();
    Random random = new Random();
    public void produce() throws InterruptedException {
        while (true){
            synchronized (lock){
                while(queue.size()==LIMIT) {
                    lock.wait();
                }

                queue.add(random.nextInt(100));
                lock.notify();
            }
        }
    }
    public void consume() throws InterruptedException{
            while (true){
                synchronized (lock){
                    while (queue.size()==0){
                        lock.wait();
                    }

                    int value = queue.poll();
                    System.out.println(value);
                    System.out.println("queue size is: " + queue.size());
                    lock.notify();
                }

                Thread.sleep(1000);
            }
    }


}

