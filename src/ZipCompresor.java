import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompresor implements Runnable {
    private String sourceFolder; //ruta de la carpeta a comprimir
    private String destinationFolder = "src/copiasSeguridad";  //ruta de la carpeta donde quieres que aparezca el .zip
    private String zipFileName;             //nombre del .zip

    private WaitNotifyExample wait;
    public ZipCompresor(WaitNotifyExample wait, String nombreFichero, String sourceFolder) {
        this.wait = wait;
        this.zipFileName = nombreFichero;
        this.sourceFolder = sourceFolder;
    }

    public void zipFolder(String sourceFolder, String destinationFolder, String zipFileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(destinationFolder + File.separator + zipFileName);
        ZipOutputStream zos = new ZipOutputStream(fos);

        addFolderToZip(sourceFolder, sourceFolder, zos);

        zos.close();
        fos.close();
    }

    private void addFolderToZip(String folderPath, String sourceFolder, ZipOutputStream zos) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files == null) {
            return; // si la carpeta está vacia salimos del metodo
        }

        byte[] buffer = new byte[1024];

        for (File file : files) {
            if (file.isDirectory()) {
                addFolderToZip(file.getAbsolutePath(), sourceFolder, zos);
            } else {
                String filePath = file.getAbsolutePath().substring(sourceFolder.length() + 1);
                ZipEntry zipEntry = new ZipEntry(filePath);
                zos.putNextEntry(zipEntry);

                FileInputStream fis = new FileInputStream(file);
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }

                fis.close();
                zos.closeEntry();
            }
        }
    }

    @Override
    public void run() {

        try {
            zipFolder(sourceFolder, destinationFolder, zipFileName);
            System.out.println("Carpeta comprimida exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al comprimir la carpeta: " + e.getMessage());
            e.printStackTrace();
        }
        wait.sendSignal();
    }
}