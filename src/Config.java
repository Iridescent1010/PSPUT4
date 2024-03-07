public class Config {
    private String server = "localhost";
    private int port = 21; //puerto del servidor ftp, 21 por defecto
    private String user = "admin";
    private String pass = "admin";
    private String localFilePath = "texto.txt";//donde se encuentra el archivo a subir
    private String remoteDirectory = "/cosas/";//ruta dentro del servidor ftp, a donde va el archivo a subir dentro de la maquina que aloja el servidor ftp
}
