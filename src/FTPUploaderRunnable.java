import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FTPUploaderRunnable implements Runnable{
    private String server; //= "localhost";
    private int port; //= 21; //puerto del servidor ftp, 21 por defecto
    private String user; //= "admin";
    private String pass; //= "admin";
    private String localFilePath; //= "texto.txt";//donde se encuentra el archivo a subir
    private String remoteDirectory; //= "/cosas/";//ruta dentro del servidor ftp, a donde va el archivo a subir dentro de la maquina que aloja el servidor ftp
    private  WaitNotifyExample wait;

    public FTPUploaderRunnable(WaitNotifyExample wait) throws IOException {
        Properties propiedades = new Properties();
        FileInputStream archivoPropiedades = new FileInputStream("src/conexion.properties");
        propiedades.load(archivoPropiedades);
        this.server=propiedades.getProperty("server");
        this.port=Integer.parseInt(propiedades.getProperty("port"));
        this.pass=propiedades.getProperty("pass");
        this.user=propiedades.getProperty("user");
        this.localFilePath = propiedades.getProperty("localFilePath");
        this.remoteDirectory = propiedades.getProperty("remoteDirectory");
        this.wait = wait;
    }

    public static void main(String[] args) {
        String server = "localhost";
        int port = 21; //puerto del servidor ftp, 21 por defecto
        String user = "admin";
        String pass = "admin";
        String localFilePath = "texto.txt";//donde se encuentra el archivo a subir
        String remoteDirectory = "/cosas/";//ruta dentro del servidor ftp, a donde va el archivo a subir dentro de la maquina que aloja el servidor ftp

    }

    @Override
    public void run() {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            File localFile = new File(localFilePath);
            FileInputStream inputStream = new FileInputStream(localFile);
            wait.waitForSignal();

            boolean uploaded = ftpClient.storeFile(remoteDirectory + localFile.getName(), inputStream);
            inputStream.close();
            if (uploaded) {
                System.out.println("El archivo se ha subido exitosamente.");
            } else {
                System.out.println("Error al subir el archivo.");
            }
        } catch (IOException ex) {
            System.out.println("Error al subir el archivo: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
