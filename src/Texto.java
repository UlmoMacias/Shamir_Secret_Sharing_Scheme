import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JPanel; 
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
*Clase Texto que trabaja con archivos de texto.
*/
public class Texto{

	private static String txt;
	private static JFileChooser fileChooser = new JFileChooser();
	private static File Directorio = fileChooser.getCurrentDirectory();
	private static FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo de Texto","txt","aes");
	private static String PathFile = "";
	private static String nombre;

	/**
	* Constructor Clase Texto vacio
	*/
	public Texto(){

	}

	/**
	* Metodo que abre un archivo 
	*@return boolean 'true' si se logro abrir 'false' si no 
	*/
	public boolean abrirArchivo() {
		String aux="";   
		txt="";
		try{
			JFileChooser file=new JFileChooser();
			file.showOpenDialog(file);
			File abre=file.getSelectedFile();
			if(abre!=null){
				setNombre(abre);     
				FileReader archivos=new FileReader(abre);
				BufferedReader lee=new BufferedReader(archivos);
				while((aux=lee.readLine())!=null){
					txt+= aux+ "\n";
				}
				lee.close();
			}    
			return true;
		}
		catch(IOException ex)
		{
			JOptionPane.showMessageDialog(null,ex+"" +
				"\nNo se ha encontrado el archivo",
				"Error",JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}

	/**
	* Metodo que regresa el texto
	* @return String
	*/
	public static String getTexto(){
		return txt;
	}

	/**
	* Metodo que establece el nombre del texto
	* @param a -- File del que se tomara el nombre
	*/	
	private static void setNombre(File a){
		nombre = a.toString();
	}

	/**
	* Metodo que regresa el texto
	* @return String
	*/
	public static String getNombre(){
		return nombre;
	}

	/**
	* Metodo que escribe un texto yuna ruta direccion.
	* @param text-- String con el texto.
	* @param path -- String con la direccion destino. 
	*/		
	public static void write(String text, String path){
		String a = path + ".txt";
		BufferedWriter bw = null;
		try {
			//Specify the file name and path here
			File file = new File(a);
			/* This logic will make sure that the file 
			* gets created if it is not present at the
			* specified location*/
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(text);
			System.out.println("File written Successfully");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}finally{ 
			try{
				if(bw!=null)
					bw.close();
			}catch(Exception ex){
				System.out.println("Error in closing the BufferedWriter"+ex);
			}
		}
	} 
}