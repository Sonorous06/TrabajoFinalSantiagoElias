package proyectofinal.logica;

import java.util.Arrays;

/**
 * Clase encargada de realizar todos los calculos estadisticos del sistema.
 * Procesa un conjunto de datos para obtener medidas de tendencia central,
 * de dispersión y de posición (cuartiles).
 */
public class Estadisticos {

    private int[] datos;
    private int numDatos;

    private int[] datosOriginales;

    /**
     * Crea un objeto de estadísticas y prepara los datos.
     * Guarda una copia original y otra ordenada para los cálculos.
     * 
     * @param arr El arreglo con los números a procesar.
     * @param dim La cantidad de datos que contiene el arreglo.
     */
    public Estadisticos(int[] arr, int dim) {
        this.numDatos = dim;

        this.datosOriginales = new int[this.numDatos];
        this.datos = new int[this.numDatos];

        for (int i = 0; i < this.numDatos; i++) {
            this.datosOriginales[i] = arr[i];
            this.datos[i] = arr[i];
        }

        this.ordenar();
    }

    /**
     * Devuelve los datos en el orden en que fueron leídos del archivo.
     * @return Arreglo de enteros original.
     */
    public int[] getDatosOriginales() {
        return this.datosOriginales;
    }

    /**
     * Ordena los datos de menor a mayor para poder calcular mínimos, máximos y cuartiles.
     */
    private void ordenar() {
        Arrays.sort(this.datos);
    }

    /**
     * Calcula la suma total de todos los elementos del conjunto.
     * @return La suma acumulada.
     */
    private int suma() {
        int total = 0;
        for (int a : this.datos) {
            total = total + a;
        }
        return total;
    }

    /**
     * Obtiene la cantidad total de datos procesados.
     * @return El valor de numDatos.
     */
    public int getNumDatos() {
        return this.numDatos;
    }

    /**
     * Devuelve los datos ya ordenados.
     * @return Arreglo de enteros ordenado.
     */
    public int[] getDatos() {
        return this.datos;
    }

    /**
     * Obtiene el valor más pequeño del conjunto.
     * @return El primer dato del arreglo ordenado.
     */
    public int minimo() {
        //Accedemos al primer dato
        return this.datos[0];
    }

    /**
     * Obtiene el valor más grande del conjunto.
     * @return El último dato del arreglo ordenado.
     */
    public int maximo() {
        //Accedemos al primer dato
        return this.datos[this.numDatos - 1];
    }

    /**
     * Calcula la diferencia entre el valor máximo y el mínimo.
     * @return El rango de los datos.
     */
    public int rango() {
        //sin repetir codigo , simplemente restamos nuestos metodos
        return maximo() - minimo();
    }

    /**
     * Calcula el primer cuartil1 que representa el 25% de los datos.
     * @return El valor del primer cuartil.
     */
    public double cuartil1() {
        //usamos el + 1 para compensar los espacios entre los numeros
        double p = (this.numDatos + 1) * 0.25;
        return calcularInterpolacion(p);
    }

    /**
     * Calcula el segundo cuartil 2 , que divide los datos al 50%.
     * @return El valor de la mediana.
     */
    //El cuartil 2 , es el valor que divide los datos en partes iguales
    public double cuartil2() {
        double p = (this.numDatos + 1) * 0.50;
        return calcularInterpolacion(p);
    }

    /**
     * Calcula el tercer cuartil 3 que representa el 75% de los datos.
     * @return El valor del tercer cuartil.
     */
    public double cuartil3() {
        //calculamos el 75% de los datos
        double p = (this.numDatos + 1) * 0.75;
        return calcularInterpolacion(p);
    }

    /**
     * Método para calcular valores exactos de los cuartiles cuando
     * la posición cae entre dos números.
     * 
     * @param p La posición teórica calculada.
     * @return El valor interpolado.
     */
    private double calcularInterpolacion(double p) {
        //si la psocion calculada es primera o menor , damos el primer dato del array
        if (p <= 1) {
            return datos[0];
        }
        //si la posicion es igual o mayor al total de los datos , regresa el ultimo dato
        if (p >= numDatos) {
            return datos[numDatos - 1];
        }
        //entero , si p es 2.75 , i se convierte en 2 
        int i = (int) p;
        //decimal , si p es 2.75 e i es 2 , la frac es .75
        double fraccion = p - i;
        //formula de la interpolacion
        return datos[i - 1] + fraccion * (datos[i] - datos[i - 1]);
    }

    /**
     * Calcula el  ric, que es la distancia entre Q3 y Q1.
     * @return La diferencia entre el tercer y primer cuartil.
     */
    //es RIC es la diferencia entre el 3er y 1er cuartil
    public double ric() {
        return cuartil3() - cuartil1();
    }

    /**
     * Obtiene el promedio de los datos.
     * @return El valor de la media.
     */
    // la media solo es la suma total entre el numero de datos
    public double media() {
        //hacemos un cast para devolver un double
        // usamos nuestro mismo metodo
        return (double) suma() / this.numDatos;
    }

    /**
     * Calcula qué tan dispersos están los datos de la media.
     * @return El valor de la varianza.
     */
    public double varianza() {
        double sumaCuadrados = 0;
        double m = media();

        for (int d : this.datos) {
            sumaCuadrados += Math.pow(d - m, 2);
        }
        //dividimos la suma entre el total de datos
        return sumaCuadrados / this.numDatos;
    }

    /**
     * Calcula la desviación estándar, que indica cuánto suelen alejarse 
     * los valores del promedio.
     * @return La raíz cuadrada de la varianza.
     */
    //ls desviacion estandar es la raiz cuadrada de la varianza
    public double desvStd() {
        return Math.sqrt(varianza());
    }

    /**
     * Calcula la relación entre la desviación estándar y la media en porcentaje.
     * @return El coeficiente de variación.
     */
    //el coeficiente de variacion es la desvEstan / media x 100
    public double coefVar() {
        return (desvStd() / media()) * 100;
    }

}
