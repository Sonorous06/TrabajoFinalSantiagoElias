package proyectofinal.graficas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import proyectofinal.logica.Estadisticos;
import proyectofinal.logica.Graficos;

/**
 * Clase que genera una grafica de pastel divide el circulo en dos partes
 * proporcionales a la suma de los valores de cada categoria y permite mostrar
 * totales o porcentajes.
 */
public class GraficaPastel extends Graficos {

    //datos de la segunda categoría (la primera la hereda en misDatos)
    private Estadisticos datosCat2;
    private String nombreCat1;
    private String nombreCat2;
    private boolean mostrarNumeros; //true = muestra la suma de valores, false = porcentaje

    //colores de cada rebanada
    private static final Color colorCat1 = new Color(128, 0, 128);
    private static final Color colorCat2 = new Color(30, 144, 255);

    /**
     * Crea la grafica de pastel con dos categorias de datos.
     *
     * @param titulo Nombre que se muestra arriba de la grafica.
     * @param datosCat1 Objeto con los datos de la primera categoria.
     * @param nombreCat1 Nombre de la primera categoría.
     * @param datosCat2 Objeto con los datos de la segunda categoria.
     * @param nombreCat2 Nombre de la segunda categoría.
     * @param mostrarNumeros Define si se ven los totales (true) o porcentajes
     * (false).
     */
    public GraficaPastel(String titulo, Estadisticos datosCat1, String nombreCat1, Estadisticos datosCat2, String nombreCat2, boolean mostrarNumeros) {
        super(titulo, datosCat1);
        this.datosCat2 = datosCat2;
        this.nombreCat1 = nombreCat1;
        this.nombreCat2 = nombreCat2;
        this.mostrarNumeros = mostrarNumeros;
    }
    
    /**
     * Cambia la forma en que se muestran las etiquetas (número o porcentaje)
     * y vuelve a dibujar la gráfica.
     * 
     * @param mostrarNumeros true para números, false para porcentajes.
     */
    //cambia el modo de etiquetado y redibuja
    public void setMostrarNumeros(boolean mostrarNumeros) {
        this.mostrarNumeros = mostrarNumeros;
        repaint();
    }

    /**
     * Método que realiza el dibujo del círculo, las rebanadas y las etiquetas.
     * Calcula los ángulos basándose en la suma total de los datos.
     * 
     * @param g Objeto de gráficos para dibujar.
     */
    @Override
    public void dibujarGrafica(Graphics g) {

        int ancho = getWidth();
        int alto = getHeight();

        //fondo blanco
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ancho, alto);

        //título
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString(titulo, ancho / 3, 25);

        //sumamos los valores de cada categoría para saber qué proporción le toca a cada una
        int totalCat1 = sumarDatos(misDatos);
        int totalCat2 = sumarDatos(datosCat2);
        int totalGlobal = totalCat1 + totalCat2;

        if (totalGlobal == 0) {
            g.drawString("Sin datos", ancho / 2 - 30, alto / 2);
            return;
        }

        //ángulo de cada rebanada proporcional a su suma
        int anguloCat1 = (int) Math.round((double) totalCat1 / totalGlobal * 360);
        int anguloCat2 = 360 - anguloCat1;

        //posición y tamaño del círculo dentro del panel
        int margen = 60;
        int diametro = alto - margen * 2 - 40;
        if (diametro < 50) {
            diametro = 50;
        }
        int xCirculo = (ancho - diametro) / 2;
        int yCirculo = margen + 10;

        //dibujamos las dos rebanadas
        g.setColor(colorCat1);
        g.fillArc(xCirculo, yCirculo, diametro, diametro, 0, anguloCat1);

        g.setColor(colorCat2);
        g.fillArc(xCirculo, yCirculo, diametro, diametro, anguloCat1, anguloCat2);

        //borde del círculo
        g.setColor(Color.BLACK);
        g.drawOval(xCirculo, yCirculo, diametro, diametro);

        //etiquetas en el centro de cada rebanada
        g.setFont(new Font("SansSerif", Font.BOLD, 13));
        int radio = diametro / 2;
        int cx = xCirculo + radio;
        int cy = yCirculo + radio;

        String etiq1 = construirEtiqueta(nombreCat1, totalCat1, totalGlobal);
        double angMed1 = Math.toRadians(anguloCat1 / 2.0);
        g.setColor(Color.BLACK);
        g.drawString(etiq1, cx + (int) (radio * 0.55 * Math.cos(angMed1)) - 15, cy - (int) (radio * 0.55 * Math.sin(angMed1)));
        String etiq2 = construirEtiqueta(nombreCat2, totalCat2, totalGlobal);
        double angMed2 = Math.toRadians(anguloCat1 + anguloCat2 / 2.0);
        g.drawString(etiq2, cx + (int) (radio * 0.55 * Math.cos(angMed2)) - 15, cy - (int) (radio * 0.55 * Math.sin(angMed2)));
        dibujarLeyenda(g, ancho, yCirculo + diametro + 20);
    }
    
    /**
     * Calcula la suma total de todos los números dentro de un objeto Estadisticos.
     * 
     * @param datos Objeto que contiene el arreglo de números.
     * @return La suma de todos los valores encontrados.
     */
    //recorre el arreglo y suma todos los valores
    private int sumarDatos(Estadisticos datos) {
        int suma = 0;
        for (int valor : datos.getDatos()) {
            suma = suma + valor;
        }
        return suma;
    }

    /**
     * Crea el texto que se mostrará sobre cada rebanada.
     * 
     * @param nombre El nombre de la categoría.
     * @param count La suma de la categoría.
     * @param total La suma global de ambas categorías.
     * @return Texto formateado con el nombre y el valor o porcentaje.
     */
    //devuelve el texto de la etiqueta según el modo activo
    private String construirEtiqueta(String nombre, int count, int total) {
        if (mostrarNumeros) {
            return nombre + " " + count;
        }
        double pct = (double) count / total * 100;
        return nombre + " " + String.format("%.0f%%", pct);
    }

    /**
     * Dibuja los cuadros de color y nombres en la parte inferior para explicar la gráfica.
     * 
     * @param g Objeto de gráficos.
     * @param ancho Ancho del panel para centrar los elementos.
     * @param y Posición vertical donde empezará la leyenda.
     */
    //dibuja los cuadros de color con el nombre de cada categoría
    private void dibujarLeyenda(Graphics g, int ancho, int y) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        int cuad = 14;
        int xCat1 = ancho / 2 - 80;
        int xCat2 = ancho / 2 + 20;

        g.setColor(colorCat1);
        g.fillRect(xCat1, y, cuad, cuad);
        g.setColor(Color.BLACK);
        g.drawRect(xCat1, y, cuad, cuad);
        g.drawString(" " + nombreCat1, xCat1 + cuad, y + cuad - 2);

        g.setColor(colorCat2);
        g.fillRect(xCat2, y, cuad, cuad);
        g.setColor(Color.BLACK);
        g.drawRect(xCat2, y, cuad, cuad);
        g.drawString(" " + nombreCat2, xCat2 + cuad, y + cuad - 2);
    }
}
