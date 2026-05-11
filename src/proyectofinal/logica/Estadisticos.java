package proyectofinal.logica;

import java.util.Arrays;

public class Estadisticos {

    private int[] datos;
    private int numDatos;

    private int[] datosOriginales;

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

    public int[] getDatosOriginales() {
        return this.datosOriginales;
    }

    private void ordenar() {
        Arrays.sort(this.datos);
    }

    private int suma() {
        int total = 0;
        for (int a : this.datos) {
            total = total + a;
        }
        return total;
    }

    public int getNumDatos() {
        return this.numDatos;
    }

    public int[] getDatos() {
        return this.datos;
    }

    public int minimo() {
        //Accedemos al primer dato
        return this.datos[0];
    }

    public int maximo() {
        //Accedemos al primer dato
        return this.datos[this.numDatos - 1];
    }

    public int rango() {
        //sin repetir codigo , simplemente restamos nuestos metodos
        return maximo() - minimo();
    }

    public double cuartil_1() {
        //usamos el + 1 para compensar los espacios entre los numeros
        double p = (this.numDatos + 1) * 0.25;
        return calcularInterpolacion(p);
    }

    //El cuartil 2 , es el valor que divide los datos en partes iguales
    public double cuartil_2() {
        double p = (this.numDatos + 1) * 0.50;
        return calcularInterpolacion(p);
    }

    public double cuartil_3() {
        //calculamos el 75% de los datos
        double p = (this.numDatos + 1) * 0.75;
        return calcularInterpolacion(p);
    }

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

    //es RIC es la diferencia entre el 3er y 1er cuartil
    public double ric() {
        return cuartil_3() - cuartil_1();
    }

    // la media solo es la suma total entre el numero de datos
    public double media() {
        //hacemos un cast para devolver un double
        // usamos nuestro mismo metodo
        return (double) suma() / this.numDatos;
    }

    public double varianza() {
        double sumaCuadrados = 0;
        double m = media();

        for (int d : this.datos) {
            sumaCuadrados += Math.pow(d - m, 2);
        }
        //dividimos la suma entre el total de datos
        return sumaCuadrados / this.numDatos;
    }

    //ls desviacion estandar es la raiz cuadrada de la varianza
    public double desvStd() {
        return Math.sqrt(varianza());
    }

    //el coeficiente de variacion es la desvEstan / media x 100
    public double coefVar() {
        return (desvStd() / media()) * 100;
    }

}
