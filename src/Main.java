import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //le damos la direccion del fichero del que queremos hacer copia periodica
        //cuando empezamos el proceso, le damos como nombre la fehca y hora
        Scanner scanner = new Scanner(System.in);
        System.out.println("intruduzca la ruta absoluta de la carpeta de la que hacer copia de seguridad");
        //ruta de ejemplo donde guardar en el servidor ftp C:/Users/extre/Desktop/cosas/prueba
        String sourceFolder = scanner.nextLine();

        for (;;){
            String dondeEstaElArchivo= horaSistemaZona().split(" ")[0]+"_"+horaSistemaZona().split(" ")[1]+".zip";
            WaitNotifyExample wait = new WaitNotifyExample();

            ZipCompresor comp = new ZipCompresor(wait, dondeEstaElArchivo, sourceFolder);

            FTPUploaderRunnable upload = null;
            try {
                upload = new FTPUploaderRunnable(wait, dondeEstaElArchivo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Crear una instancia de Thread y pasarle el objeto Runnable
            Thread hiloUp = new Thread(upload);
            Thread hiloComp = new Thread(comp);

            // Iniciar el hilo
            hiloUp.start();
            hiloComp.start();
            System.out.println(dondeEstaElArchivo);
            Thread.sleep(30000);
        }


    }
    public static String horaSistemaZona(){
        String hora;
        ZonedDateTime horaActual = ZonedDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH_mm_ss z");
        hora = horaActual.format(formato);
        return hora;
    }
}
