package tm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;





public class ClienteFTP {


    private FTPClient mFTPClient;
    private String rutaDominio;
    private int puertoDominio;
    private String usuario;
    private String clave;
    private static final String TAG = "ClienteFTP";
   

    public ClienteFTP(String ruta, int puerto, String usuariop, String clavep)  {

        this.rutaDominio = ruta;
        this.puertoDominio = puerto;
        this.usuario = usuariop;
        this.clave = clavep;

    }

    public String getRutaDominio() {
        return rutaDominio;
    }

    public void setRutaDominio(String rutaDominio) {
        this.rutaDominio = rutaDominio;
    }

    public int getPuertoDominio() {
        return puertoDominio;
    }

    public void setPuertoDominio(int puertoDominio) {
        this.puertoDominio = puertoDominio;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean conectar()throws IOException {

        try {
            mFTPClient = new FTPClient();
            // connecting to the host
            mFTPClient.connect(rutaDominio);

            // now check the reply code, if positive mean connection success
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                // login using username & password
                boolean status = mFTPClient.login(usuario, clave);

                /* Set File Transfer Mode
                 *
                 * To avoid corruption issue you must specified a correct
                 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
                 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE
                 * for transferring text, image, and compressed files.
                 */
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();

                return status;
            }
        } catch(Exception e) {
            System.out.println("Error: could not connect to host " + rutaDominio );
         	e.printStackTrace();
        }

        return false;
    }


    public boolean desconectar()
    {
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            return true;
        } catch (Exception e) {
        	System.out.println("Error occurred while disconnecting from ftp server.");
         	e.printStackTrace();
        }

        return false;
    }

    public String darDirectorioActual()
    {
        try {
            String workingDir = mFTPClient.printWorkingDirectory();
            return workingDir;
        } catch(Exception e) {
        	System.out.println( "Error: could not get current working directory.");
         	e.printStackTrace();
        }

        return null;
    }

    public boolean ftpChangeDirectory(String directory_path)
    {
        try {
            return mFTPClient.changeWorkingDirectory(directory_path);
        } catch(Exception e) {
        	System.out.println("Error: could not change directory to " + directory_path);
         	e.printStackTrace();
        }

        return false;
    }


    public void imprimirArchivosDeDirectorio(String dir_path)
    {
        try {
            FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
            int length = ftpFiles.length;

            for (int i = 0; i < length; i++) {
                String name = ftpFiles[i].getName();
                boolean isFile = ftpFiles[i].isFile();

                if (isFile) {
                	System.out.println( "File : " + name);
                }
                else {
                	System.out.println( "Directory : " + name);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<String> imprimirDirectorios(String dir_path)
    {
    	ArrayList<String> dirs = new ArrayList<String>();
        try {
            FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
            int length = ftpFiles.length;

            for (int i = 0; i < length; i++) {
                String name = ftpFiles[i].getName();
                boolean isFile = ftpFiles[i].isFile();

                if (!isFile) {
                	System.out.println(name);
                	dirs.add(name);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return dirs;
    }

    public boolean crearDirectorio(String new_dir_path)
    {
        try {
            boolean status = mFTPClient.makeDirectory(new_dir_path);
            return status;
        } catch(Exception e) {
        	System.out.println( "Error: could not create new directory named " + new_dir_path);
         	e.printStackTrace();
        }

        return false;
    }

    public boolean eliminarDirectorio(String dir_path)
    {
        try {
            boolean status = mFTPClient.removeDirectory(dir_path);
            return status;
        } catch(Exception e) {
        	System.out.println("Error: could not remove directory named " + dir_path);
         	e.printStackTrace();
        }

        return false;
    }


    public boolean eliminarArchivo(String filePath)
    {
        try {
            boolean status = mFTPClient.deleteFile(filePath);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean renombrarArchivo(String from, String to)
    {
        try {
            boolean status = mFTPClient.rename(from, to);
            return status;
        } catch (Exception e) {
        	System.out.println( "Could not rename file: " + from + " to: " + to);
         	e.printStackTrace();
        }

        return false;
    }

    public boolean bajarArchivo(String srcFilePath, String desFilePath)
    {
    	System.out.println("metodo: bajarArchivo; srcFilePath: " + srcFilePath + "; desFilePath: " + desFilePath);
        boolean status = false;
        try {
            FileOutputStream desFileStream = new FileOutputStream(desFilePath);;
            status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
            desFileStream.close();

            return status;
        } catch (Exception e) {
        	System.out.println( "download failed");
         	e.printStackTrace();
        }

        return status;
    }
    
    public List<String> bajarArchivosDirectorio(String srcDirPath, String desDirPath, Long idOrden)
    {
    	System.out.println("metodo: bajarArchivosDirectorio; srcDirPath: " + srcDirPath + "; desDirPath: " + desDirPath);
    	List<String> nombres = new ArrayList<String>(); 
        try {
        	FTPFile[] ftpFiles = mFTPClient.listFiles(srcDirPath);
            int length = ftpFiles.length;

            for (int i = 0; i < length; i++) {
                String name = ftpFiles[i].getName();
                boolean isFile = ftpFiles[i].isFile();

                if (isFile) {
                	if(!bajarArchivo(srcDirPath + "/" + name, desDirPath + "/" +idOrden+"_"+name)) {
                		System.out.println("false : " + name);
                		throw new Exception("Error al descargar la imagen " + i + ", nombre: " + name);
                	}
                	nombres.add(idOrden+"_"+name);
                	System.out.println("true : " + name);
                }
                else {

                }
            }
        } catch (Exception e) {
        	System.out.println( "download failed");
        	System.out.println( e.getMessage());
         	e.printStackTrace();
        }

        return nombres;
    }
    
    public List<String> darNombreArchivosDirectorio(String srcDirPath, Long idOrden)
    {
    	System.out.println("metodo: darNumeroArchivosDirectorio; srcDirPath: " + srcDirPath);
    	List<String> nombres = new ArrayList<String>(); 
        try {
        	FTPFile[] ftpFiles = mFTPClient.listFiles(srcDirPath);
            int length = ftpFiles.length;

            for (int i = 0; i < length; i++) {
                boolean isFile = ftpFiles[i].isFile();
                String name = ftpFiles[i].getName();
                if (isFile) {
                	nombres.add("lazy.gif");
                }
                else {

                }
            }
        } catch (Exception e) {
        	System.out.println( "download failed");
        	System.out.println( e.getMessage());
         	e.printStackTrace();
        }

        return nombres;
    }




    public boolean subirArchivo(String srcFilePath, String desFileName,
                                String desDirectory)
    {
        boolean status = false;
        try {
            FileInputStream srcFileStream = new FileInputStream(srcFilePath);

            // change working directory to the destination directory
            if (ftpChangeDirectory(desDirectory)) {
            	System.out.println("cambiar de directorio");
                status = mFTPClient.storeFile(desFileName, srcFileStream);
               
            }else {
            	System.out.println("no cambia de directorio");
            }

            srcFileStream.close();
            return status;
        } catch (Exception e) {
        	System.out.println("upload failed");
        	e.printStackTrace();
        }

        return status;
    }
    
    public boolean estaConectado() {
    	return mFTPClient.isConnected();
    }


}
