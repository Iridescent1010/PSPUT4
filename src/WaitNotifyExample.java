import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaitNotifyExample {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private boolean flag = false;

    public void waitForSignal() {
        lock.lock();
        try {
            while (!flag) {
                condition.await(); // Espera hasta que la bandera se establezca
            }
            // Realiza alguna acción después de recibir la señal
            System.out.println("Recibida la señal.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void sendSignal() {
        lock.lock();
        try {
            // Realiza alguna acción antes de enviar la señal
            System.out.println("Enviando la señal.");
            flag = true; // Establece la bandera
            condition.signal(); // Despierta a un hilo que esté esperando
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        WaitNotifyExample example = new WaitNotifyExample();

        // Crea e inicia un hilo que espera la señal
        Thread thread1 = new Thread(() -> {
            example.waitForSignal();
        });
        thread1.start();

        // Crea e inicia un hilo que envía la señal
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            example.sendSignal();
        });
        thread2.start();
    }
}