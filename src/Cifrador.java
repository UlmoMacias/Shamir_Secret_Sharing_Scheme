import javax.swing.JOptionPane;
import java.security.MessageDigest;
import java.util.Vector;
import java.util.LinkedList;
import java.io.*;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.CipherInputStream;
import javax.crypto.Cipher;
import java.math.BigInteger;
/**
*Clase Cifrador que esconde el mensaje usando Shamir Sharing Secret Scheme (SSSS)(S4).
*
*/
public class Cifrador {
    private String nombreTextoOriginal;
    private String crypted;
    private String mensaje;
    private String evaluaciones;
    private String nombreTextoAGuardarNEval;
    private int N;
    private int T;
    private String password;
    private String hashedPass;
    private Polinomio x;

    /**
    *Constructor vacio sin parametros
    */
    public Cifrador(){}

    /**
    *Metodo que oculta el mensaje
    *@param String cadena con el mensaje
    */
    public void cifrarMensaje(String aux){
        Texto a = new Texto();
        mensaje = aux;
        hashingPassword();
        construyePol();
        Texto texto = new Texto();
        //escribir Evaluaciones
        a.write(x.evaluarN(N),nombreTextoAGuardarNEval+".frag");
        //escribir AES
        //String aes = 
        aesEncrypt(new BigInteger(hashedPass,16).abs().toByteArray());
        //a.write(aes,nombreTextoOriginal);
    }

    /** 
    *Metodo que encripta con una contraseña usando aes.
    *@param byte[] passwd -- arreglo de bytes de la contrasenia.
    */
    private void aesEncrypt(byte[] passwd){
        int toWrite;
        String fileName;
        CipherInputStream ci;
        FileOutputStream wr; 
        SecretKeySpec sec;
        Cipher cipher;
        try {
            fileName = nombreTextoOriginal;
            cipher = Cipher.getInstance("AES");
            wr = new FileOutputStream(fileName+".aes",true);
            sec = new SecretKeySpec(passwd,0,16,"AES");
            cipher.init(Cipher.ENCRYPT_MODE,sec);
            ci = new CipherInputStream(new FileInputStream(nombreTextoOriginal),cipher);
            while((toWrite=ci.read()) != -1)
                wr.write(toWrite);
            wr.close();
            ci.close();
            JOptionPane.showMessageDialog(null, "The encrypted file was saved as: "+fileName);
        } catch(Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    /** 
    *Metodo que construye un polinomio con el grado requerido y la contrasenia.
    */
    private void construyePol(){
        x = new Polinomio(T-1,hashedPass);
    }

    /**
    *Metodo que descifra el mensaje.
    *@param text -- Cadena con las evaluaciones.
    *@param text2 -- Cadena con el aes. 
    */
    public void descifrarMensaje(String text,String text2){
        byte[] passwd = getK();
        createClear(passwd);
    }

    /**
    * Metodo que lee el archivo con las evaluaciones.
    * @return byte[] Contrasenia escondida en el polinomio f(0) = K.
    */
    private byte[] getK() {
        Vector[] array = null;
        byte[] array1 = null;
        MessageDigest md;
        LinkedList<Vector> list = new LinkedList<Vector>();
        Polinomio poli = new Polinomio();
        try {
            String strLine;
            FileInputStream fstream = new FileInputStream(evaluaciones);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            System.out.println("Reading "+evaluaciones+"...");
            while((strLine=br.readLine()) != null) {
                list.add(new Vector(2));
                ((Vector)list.getLast()).add(0,new BigInteger(strLine.substring(0,strLine.indexOf(','))));
                ((Vector)list.getLast()).add(1,new BigInteger(strLine.substring(strLine.indexOf(',')+1,strLine.length())));
            }
            in.close();
            array = new Vector[list.size()];
            for(int i = 0; i < array.length; i++)
                array[i] = (Vector)list.get(i);
            System.out.println("Evaluating Lagrange interpolating polynomial...");
            return poli.lagrange(new BigInteger("0"),array).toByteArray();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        return null;
    }

    /**
     * Metodo que crea el archivo desencriptado usando la contrasenia (en bytes) y el archivo encriptado.
     * @param passwd -- la contrasenia en un arreglo de bytes.
     */ 
    public void createClear(byte[] passwd) {
        int toWrite;
        String fileName;
        CipherInputStream ci;
        FileOutputStream wr; 
        SecretKeySpec sec;
        Cipher cipher;
        try {
            if(crypted.lastIndexOf('.') == -1)
                fileName = crypted;
            else
                fileName = crypted.substring(0,crypted.lastIndexOf('.'));
            cipher = Cipher.getInstance("AES");
            wr = new FileOutputStream(fileName+".desencriptado",true);
            sec = new SecretKeySpec(passwd,0,16,"AES");
            cipher.init(Cipher.DECRYPT_MODE,sec);
            ci = new CipherInputStream(new FileInputStream(crypted),cipher);
            while((toWrite=ci.read()) != -1)
                wr.write(toWrite);
            wr.close();
            ci.close();
            System.out.println("The clear file was saved as "+fileName +".desencriptado");
        } catch(Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    /** 
    *Metodo que define el nombre del texto en el que seran guardadas las n evaluaciones.
    *@param String con el nombre a usar.
    */
    public void setNTAGNE(String a){
        nombreTextoAGuardarNEval =a;
    }

    /** 
    *Metodo que define el nombre del texto en el que seran guardado el texto cifrado.
    *@param String con el nombre a usar.
    */
    public void setNombreCifrado(String a){
        crypted =a;
    }

    /** 
    *Metodo que define el nombre del texto con las evaluaciones
    *@param String con el nombre a usar.
    */
    public void setNombreEvaluaciones(String a){
        evaluaciones = a;
    }

    /** 
    *Metodo que define el nombre del texto original
    *@param String con el nombre a usar.
    */
    public void setNombreOriginal(String a){
        nombreTextoOriginal =a;

    }

    /**
    *Metodo que define la contraseña.
    *@param String con el nombre a usar.
    */
    public void setPassword(String pass){
        password = pass;
    }

    /** 
    *Metodo que realiza un hashing a una contrasenia usando sha-256.
    */
    private void hashingPassword(){
        try{    
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            hashedPass = bytesToHex(md.digest());
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 
    *Metodo qe convierte un arreglo de bytes a cadena.
    *@param bytes -- arreglo de bytes.
    *@return String con el texto.
    */
    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            return result.toString();
    }

    /** 
    *Metodo que define un numero N y verifica que sea natural.
    */
    public void setN(String res2){
        if (isNumeric(res2)) {
            int aux = Integer.parseInt(res2);
            N = aux; 
        }
        else
            JOptionPane.showMessageDialog(null, "El dato ingresado no es un numero, por favor cerrar y volver a intentar");
    }

    /** 
    *Metodo que define un numero T y verifica que sea natural.
    */
    public void setT(String res3){
        if (isNumeric(res3)) {
            int aux = Integer.parseInt(res3);
        T = aux; 
        }
        else
            JOptionPane.showMessageDialog(null, "El dato ingresado no es un numero, por favor cerrar y volver a intentar");
    }

    /** 
    *Metodo que verifica si una cadena es numerica.
    */
    private boolean isNumeric(String s) {  
        return s != null && s.matches("[-+]?\\d*\\.?\\d+") && s.indexOf(".") == -1;  
    }

    /**
    *Metodo que convierte a binario
    *@return String
    */
    private String toBinary(byte caracter){
        byte byteDeCaracter = (byte)caracter;
        String binario="";
        for( int i = 7; i>=0; i--){
            binario = binario + ( ( ( byteDeCaracter & ( 1<<i ) ) > 0 ) ? "1" : "0" ) ;
        }
        return binario;
    }

    /**
    *Metodo que convierte un int a char
    *@return String
    */
    private String toChar(String binario){
        int i = Integer.parseInt(binario ,2);
        String aChar = new Character((char)i).toString();
        return aChar;        
    }

    /**
    *Metodo que convierte a binario
    *@return int
    */
    private int toCharInt(String binario){
        int i = Integer.parseInt(binario ,2);        
        return i;
    }
   /**
   *Metodo que convierte un mensaje a binario
   *@return String
   */
   private String getMensajeToBinary(String mensaje){
    String men = "";
    char[] mensaje_tmp = mensaje.toCharArray();
    for(int i=0; i<mensaje_tmp.length;i++){
        men = men + toBinary( (byte) mensaje_tmp[i]);
    }
    return men;
}

    /**
    *Metodo que convierte de binario a String
    *@return String
    */
    private String getMensajeToString(String[] mensaje){
        String men ="";
        for(int i=4; i<mensaje.length;i++){
            men = men + toChar(mensaje[i]) ;
        }
        return men;
    }
}