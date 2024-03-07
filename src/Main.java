import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        WaitNotifyExample wait = new WaitNotifyExample();


        FTPUploaderRunnable comp = null;
        try {
            comp = new FTPUploaderRunnable(wait);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Crear una instancia de Thread y pasarle el objeto Runnable
        Thread hilo = new Thread(comp);

        // Iniciar el hilo
        hilo.start();
    }
}
