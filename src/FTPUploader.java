import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FTPUploader {
    public static void main(String[] args) {
        String server = "localhost";
        int port = 21;
        String user = "admin";
        String pass = "admin";
        String localFilePath = "texto.txt";//donde se encuentra el archivo
        String remoteDirectory = "/cosas/";//ruta dentro del servidor ftp

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            File localFile = new File(localFilePath);
            FileInputStream inputStream = new FileInputStream(localFile);

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