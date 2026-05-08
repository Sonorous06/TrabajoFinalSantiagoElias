package proyectofinal.graficas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import proyectofinal.logica.Estadisticos;
import proyectofinal.logica.Graficos;

public class GraficaPastel extends Graficos {

    //datos de la segunda categoría (la primera la hereda en misDatos)
    private Estadisticos datosCat2;
    private String nombreCat1;
    private String nombreCat2;
    private boolean mostrarNumeros; //true = muestra la suma de valores, false = porcentaje

    //colores de cada rebanada
    private static final Color COLOR_CAT1 = new Color(34, 139, 34);
    private static final Color COLOR_CAT2 = new Color(255, 215, 0);

    public GraficaPastel(String titulo, Estadisticos datosCat1, String nombreCat1, Estadisticos datosCat2, String nombreCat2, boolean mostrarNumeros) {
        super(titulo, datosCat1);
        this.datosCat2 = datosCat2;
        this.nombreCat1 = nombreCat1;
        this.nombreCat2 = nombreCat2;
        this.mostrarNumeros = mostrarNumeros;
    }

    //cambia el modo de etiquetado y redibuja
    public void setMostrarNumeros(boolean mostrarNumeros) {
        this.mostrarNumeros = mostrarNumeros;
        repaint();
    }

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
        if (diametro < 50) diametro = 50;
        int xCirculo = (ancho - diametro) / 2;
        int yCirculo = margen + 10;

        //dibujamos las dos rebanadas
        g.setColor(COLOR_CAT1);
        g.fillArc(xCirculo, yCirculo, diametro, diametro, 0, anguloCat1);

        g.setColor(COLOR_CAT2);
        g.fillArc(xCirculo, yCirculo, diametro, diametro, anguloCat1, anguloCat2);

        //borde del círculo
        g.setColor(Color.BLACK);
        g.drawOval(xCirculo, yCirculo, diametro, diametro);

        //etiquetas en el centro de cada rebanada
        g.setFont(new Font("SansSerif", Font.BOLD, 13));
        int radio = diametro / 2;
        int cx = xCirculo + radio; //centro X del círculo
        int cy = yCirculo + radio; //centro Y del círculo

        String etiq1 = construirEtiqueta(nombreCat1, totalCat1, totalGlobal);
        double angMed1 = Math.toRadians(anguloCat1 / 2.0);
        g.setColor(Color.BLACK);
        g.drawString(etiq1, cx + (int) (radio * 0.55 * Math.cos(angMed1)) - 15, cy - (int) (radio * 0.55 * Math.sin(angMed1)));
        String etiq2 = construirEtiqueta(nombreCat2, totalCat2, totalGlobal);
        double angMed2 = Math.toRadians(anguloCat1 + anguloCat2 / 2.0);
        g.drawString(etiq2, cx + (int) (radio * 0.55 * Math.cos(angMed2)) - 15, cy - (int) (radio * 0.55 * Math.sin(angMed2)));
        dibujarLeyenda(g, ancho, yCirculo + diametro + 20);
    }

    //recorre el arreglo y suma todos los valores
    private int sumarDatos(Estadisticos datos) {
        int suma = 0;
        for (int valor : datos.getDatos()) {
            suma = suma + valor;
        }
        return suma;
    }

    //devuelve el texto de la etiqueta según el modo activo
    private String construirEtiqueta(String nombre, int count, int total) {
        if (mostrarNumeros) {
            return nombre + " " + count;
        }
        double pct = (double) count / total * 100;
        return nombre + " " + String.format("%.0f%%", pct);
    }

    //dibuja los cuadros de color con el nombre de cada categoría
    private void dibujarLeyenda(Graphics g, int ancho, int y) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        int cuad  = 14;
        int xCat1 = ancho / 2 - 80;
        int xCat2 = ancho / 2 + 20;

        g.setColor(COLOR_CAT1);
        g.fillRect(xCat1, y, cuad, cuad);
        g.setColor(Color.BLACK);
        g.drawRect(xCat1, y, cuad, cuad);
        g.drawString(" " + nombreCat1, xCat1 + cuad, y + cuad - 2);

        g.setColor(COLOR_CAT2);
        g.fillRect(xCat2, y, cuad, cuad);
        g.setColor(Color.BLACK);
        g.drawRect(xCat2, y, cuad, cuad);
        g.drawString(" " + nombreCat2, xCat2 + cuad, y + cuad - 2);
    }
}