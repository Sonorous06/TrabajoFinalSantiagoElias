package proyectofinal.logica;

import java.util.Arrays;

public class Estadisticos {

    private int[] datos;
    private int numDatos;

    public Estadisticos(int[] arr, int dim) {
        //guardamos la cantidad de datos
        this.numDatos = dim;

        //creamos nuestro propio arreglo
        this.datos = new int[this.numDatos];

        //copiamos los valores uno por uno
        for (int i = 0; i < this.numDatos; i++) {
            this.datos[i] = arr[i];
        }

        //llamamos a nuestro metodo para que ya esten ordenamos los numeros
        this.ordenar();
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

    private String tablaFrecu() {
        String tabla = "Numero\tF.Abs\tF.Rel\tF.Por\tFrecuencia\n";

        for (int i = 0; i < this.numDatos; i++) {

            // primero revisamos si el número de la posición i lo conto
            boolean yaContado = false;
            for (int k = 0; k < i; k++) {
                if (this.datos[i] == this.datos[k]) {
                    yaContado = true;
                }
            }

            // si NO ha sido contado, hacemos todo el conteo y lo pegamos a la tabla
            if (yaContado == false) {
                int fAbs = 0;

                // contamos cuántas veces aparece el número en todo el arreglo
                for (int j = 0; j < this.numDatos; j++) {
                    if (this.datos[i] == this.datos[j]) {
                        fAbs = fAbs + 1;
                    }
                }

                double fRel = (double) fAbs / this.numDatos;
                double fPor = fRel * 100;

                String asteriscos = "";
                for (int m = 0; m < fAbs; m++) {
                    asteriscos = asteriscos + "*";
                }

                tabla = tabla + this.datos[i] + "\t" + fAbs + "\t"
                        + String.format("%.2f", fRel) + "\t"
                        + String.format("%.2f%%", fPor) + "\t"
                        + asteriscos + "\n";
            }
        }
        return tabla;
    }

    @Override
    public String toString() {
        String reporte = "Datos numericos: ";

        // vamos pegando cada número del arreglo
        for (int d : this.datos) {
            reporte += d + " "; // El += significa  agrega esto a lo anterior
        }
        // pegamos la info línea por línea
        reporte += "\n" + tablaFrecu();
        reporte += "\nCantidad de datos: " + this.numDatos;
        reporte += "\nMinimo: " + minimo();
        reporte += "\nMaximo: " + maximo();
        reporte += "\nRango: " + rango();

        // agregamos los cálculos con formato de 2 decimales
        reporte += "\n1er cuartil: " + String.format("%.2f", cuartil_1());
        reporte += "\n2do cuartil: " + String.format("%.2f", cuartil_2());
        reporte += "\n3er cuartil: " + String.format("%.2f", cuartil_3());
        reporte += "\nMedia: " + String.format("%.2f", media());
        reporte += "\nDesv. estandar: " + String.format("%.3f", desvStd());

        return reporte;
    }
}
