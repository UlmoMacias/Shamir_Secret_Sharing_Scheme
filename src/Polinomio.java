import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.math.BigInteger;
import java.util.Vector;

/**
*Clase Polinomio que crea polinomios, evalua con numeros aleatorios, y evalua laGrange.
*/
public class Polinomio{
	private BigInteger[] coeficientes;
	private int grado;
	private SecureRandom sr = new SecureRandom();
	private BigInteger prime = new BigInteger("208351617316091241234326746312124448251235562226470491514186331217050270460481");

    /** 
    *Constructor de una polinomio
    */
	public Polinomio(){

	}

	/** 
    *Constructor de un polinomio con parametros
    *@param g --  entero con el grado necesario
    *@param key1 -- cadena con la llave para usar como K, el termino independiente.
    */
	public Polinomio(int g, String key1){
		BigInteger key = new BigInteger(key1,16);
		int aux2 =0 ;
		if (g < 0) {
			BigInteger aux1 = toBI(0);
			coeficientes = new BigInteger[0];
			coeficientes[0] = aux1;
			grado = 0;
		} else {
			coeficientes = new BigInteger[g + 1];
			coeficientes[0] = key;
			genSR();
			for(int i = 1; i <= g; i++){
				coeficientes[i] = toBI(sr.nextInt());
				grado = g;
			}
		}
	}

    /** 
    *Metodo que dado un entero, regresa su valor en BigInteger.
    *@param x -- entero a cambiar por BigInteger.
    *@return BigInteger con la representacion del entero.
    */
	private BigInteger toBI(int x) {
		return BigInteger.valueOf(x);
	}

    /** 
    *Metodo que regresa el grado de un polinomio.
    *@return entero con el grado.
    */
	public int grado() {
		return this.grado;
	}

    /** 
    *Metodo genera un SecureRandom.
    */
	private void genSR(){
		SecureRandom sr = null;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
		// para garantizar el caracter aleatorio generemos una nueva semilla
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		sr.nextBytes(new byte[1]); 
	}

    /** 
    *Metodo evalua N numeros pseudoaleatorios en el polinomio.
    *@return String con las evaluaciones requeridas.
    *@param n -- entero con las evaluaciones necesaarias.
    */
	public String evaluarN(int n){
		String aux = "";
		for (int i =0;i<n ;i++ ) {
			BigInteger foo = toBI(sr.nextInt());
			aux += evaluar(foo);
		}
	  	//aux += "}";
		return aux;
	}

    /** 
    *Metodo auxiliar para evaluar por separado. 
    */
	private String evaluar(BigInteger x){
		return ""+x + "," + evaluarAux(x) + "\n";
	}

    /** 
    *Metodo auxiliar para evaluar.
    */
	private BigInteger evaluarAux(BigInteger x) {
		BigInteger valor = toBI(0);
		for (int i = this.grado; i >= 0; i--){
			valor = this.coeficientes[i].add(x.multiply(valor));
		}
		return valor;
	}

	/**
	* Metodo que evalua usando interpolacion de Lagrange sobre un arreglo de puntos en Zp².
	* Este metodo regresa el "shared secret", que es, f(0) = a0 = K.
	* @param x -- el valor (i.e. f(x)).
	* @param puntos --el arreglo con los puntos en Zp².
	* @return BigInteger -- el "shared secret".
	*/
	public BigInteger lagrange(BigInteger x, Vector[] puntos) {
		BigInteger secret = new BigInteger("0"), numerador = new BigInteger("1"), denominador = new BigInteger("1"), 
		temp = null, aux = null, quotient = null;
		for(int i = 0; i < puntos.length; i++) {
			numerador = new BigInteger("1");
			denominador = new BigInteger("1");
			for(int j = 0; j < puntos.length; j++) {
				if(j != i) {
					temp = (BigInteger)puntos[i].elementAt(0);
					aux = (BigInteger)puntos[j].elementAt(0);
					numerador = numerador.multiply(x.subtract(aux).mod(prime)).mod(prime);
					denominador = denominador.multiply(temp.subtract(aux).mod(prime)).mod(prime);
				}
			}
			quotient = numerador.multiply(denominador.modInverse(prime)).mod(prime);
			secret = secret.add(((BigInteger)puntos[i].elementAt(1)).multiply(quotient).mod(prime)).mod(prime);
		}
		return secret;
	}

    /** 
    *Metodo que regresa el menor entre dos BigInteger.
    *@param x -- BigInteger a comparar. 
    *@param y -- BigInteger a comparar. 
    *@return boolean.
    *@return'true' si x es mayor que y.
    *@return 'false' si y es mayor que x.
    */
	public boolean lessThan(BigInteger x, BigInteger y){
		if ((x.max(y)).equals(x)) {
			return false;
		}
		else return true;
	}

    /** 
    *Metodo convierte un polinomio a texto
    *@return Cadena con el texto.
    */
	public String toString() {
		BigInteger aux = toBI(0);
		if (grado ==  0) return "" + coeficientes[0];
		if (grado ==  1) return coeficientes[1] + "x + " + coeficientes[0];
		String s = coeficientes[grado] + "x^" + grado;
		for (int i = grado-1; i >= 0; i--) {
			if (coeficientes[i].equals(aux)) continue;
			else if (lessThan(aux,coeficientes[i])) s = s + " + " + ( coeficientes[i]);
			else if (lessThan(coeficientes[i],aux)) s = s + " - " + (coeficientes[i].negate());
			if (i == 1) s = s + "x";
			else if (i >  1) s = s + "x^" + i;
		}
		return s;
	}
}