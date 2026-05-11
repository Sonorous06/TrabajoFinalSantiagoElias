package proyectofinal.graficas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import proyectofinal.logica.Estadisticos;
import proyectofinal.logica.Graficos;

public class GraficaBigote extends Graficos {

    //datos de la segunda categoría
    private Estadisticos datosCat2;
    private String nombreCat1;
    private String nombreCat2;
    private boolean mostrarCat1; //controla si se dibuja la caja de la categoría 1
    private boolean mostrarCat2; //controla si se dibuja la caja de la categoría 2

    //colores de borde y relleno para cada caja
    private static final Color colorBorde1 = new Color(180, 50, 50);
    private static final Color colorRelleno1 = new Color(220, 80, 80);
    private static final Color colorBorde2 = new Color(200, 120, 0);
    private static final Color colorRelleno2 = new Color(255, 165, 0);

    //márgenes del área de dibujo dentro del panel
    private static final int MG_IZQ = 60;
    private static final int MG_DER = 20;
    private static final int MG_SUP = 40;
    private static final int MG_INF = 50;

    public GraficaBigote(String titulo, Estadisticos datosCat1, String nombreCat1, Estadisticos datosCat2, String nombreCat2, boolean mostrarCat1, boolean mostrarCat2) {

        super(titulo, datosCat1);
        this.datosCat2 = datosCat2;
        this.nombreCat1 = nombreCat1;
        this.nombreCat2 = nombreCat2;
        this.mostrarCat1 = mostrarCat1;
        this.mostrarCat2 = mostrarCat2;
    }

    //activan o desactivan cada categoría y redibujan la gráfica
    public void setMostrarCat1(boolean v) {
        this.mostrarCat1 = v;
        repaint();
    }

    public void setMostrarCat2(boolean v) {
        this.mostrarCat2 = v;
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
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.drawString(titulo, ancho / 3, 22);

        //límites del área donde se dibuja la gráfica
        int xMin = MG_IZQ;
        int xMax = ancho - MG_DER;
        int yMin = MG_SUP;
        int yMax = alto - MG_INF;

        //rango del eje Y con un 10% de margen arriba y abajo
        int vMin = calcularMinGlobal();
        int vMax = calcularMaxGlobal();
        int pad = (int) ((vMax - vMin) * 0.10);
        if (pad < 2) {
            pad = 2;
        }
        vMin = (vMin - pad > 0) ? vMin - pad : 0;
        vMax = vMax + pad;

        //ejes
        g.setColor(Color.BLACK);
        g.drawLine(xMin, yMax, xMax, yMax); //eje X
        g.drawLine(xMin, yMin, xMin, yMax); //eje Y

        //cuadrícula suave y marcas con números en el eje Y
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        for (int i = 0; i <= 6; i++) {
            int val = vMin + (vMax - vMin) * i / 6;
            int yPix = pyVal(val, yMin, yMax, vMin, vMax);
            g.setColor(new Color(220, 220, 220)); //línea de cuadrícula gris claro
            g.drawLine(xMin + 1, yPix, xMax, yPix);
            g.setColor(Color.BLACK);
            g.drawLine(xMin - 4, yPix, xMin, yPix); //marca del eje
            g.drawString(String.valueOf(val), xMin - 32, yPix + 4);
        }

        //etiqueta "Valor" del eje Y, carácter a carácter porque no podemos rotar sin Graphics2D
        String ejeY = "Valor";
        int yLetra = (yMin + yMax) / 2 - 25;
        for (int i = 0; i < ejeY.length(); i++) {
            g.drawString(String.valueOf(ejeY.charAt(i)), 5, yLetra + i * 11);
        }

        //si hay dos cajas las separamos, si hay una la centramos
        boolean dosCajas = mostrarCat1 && mostrarCat2 && misDatos != null && datosCat2 != null;
        int anchoUtil = xMax - xMin;
        int anchoCaja = anchoUtil / (dosCajas ? 5 : 3);
        int cx1 = dosCajas ? xMin + anchoUtil / 3 : (xMin + xMax) / 2;
        int cx2 = dosCajas ? xMin + 2 * anchoUtil / 3 : (xMin + xMax) / 2;

        //dibujamos las cajas activas con su etiqueta de categoría abajo
        if (mostrarCat1 && misDatos != null) {
           dibujarCaja(g, misDatos, cx1, anchoCaja, yMin, yMax, vMin, vMax, colorBorde1, colorRelleno1);
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.drawString(nombreCat1, cx1 - 6, yMax + 18);
        }
        if (mostrarCat2 && datosCat2 != null) {
            dibujarCaja(g, datosCat2, cx2, anchoCaja, yMin, yMax, vMin, vMax, colorBorde2, colorRelleno2);
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.drawString(nombreCat2, cx2 - 6, yMax + 18);
        }

        dibujarLeyenda(g, ancho, alto);
    }

    //dibuja una caja completa: relleno Q1-Q3, borde, mediana, bigotes y punto de media
    private void dibujarCaja(Graphics g, Estadisticos datos, int cx, int anchoCaja, int yMin, int yMax, int vMin, int vMax, Color colorBorde, Color colorRell) {

        //convertimos los cinco puntos clave a píxeles
        int yMinPix = pyVal(datos.minimo(), yMin, yMax, vMin, vMax);
        int yMaxPix = pyVal(datos.maximo(), yMin, yMax, vMin, vMax);
        int yQ1Pix = pyVal((int) datos.cuartil_1(), yMin, yMax, vMin, vMax);
        int yQ2Pix = pyVal((int) datos.cuartil_2(), yMin, yMax, vMin, vMax);
        int yQ3Pix = pyVal((int) datos.cuartil_3(), yMin, yMax, vMin, vMax);
        int yMediaPix = pyVal((int) datos.media(), yMin, yMax, vMin, vMax);

        int mitad = anchoCaja / 2; //la mitad del ancho para centrar en cx
        int tapa = anchoCaja / 4; //ancho de la línea horizontal al final del bigote

        //caja de Q1 a Q3 (Q3 queda más arriba en pantalla porque Y está invertido)
        g.setColor(colorRell);
        g.fillRect(cx - mitad, yQ3Pix, anchoCaja, yQ1Pix - yQ3Pix);
        g.setColor(colorBorde);
        g.drawRect(cx - mitad, yQ3Pix, anchoCaja, yQ1Pix - yQ3Pix);

        //línea de la mediana (Q2)
        g.drawLine(cx - mitad, yQ2Pix, cx + mitad, yQ2Pix);

        //bigote superior: de Q3 al máximo
        g.drawLine(cx, yQ3Pix, cx, yMaxPix);
        g.drawLine(cx - tapa, yMaxPix, cx + tapa, yMaxPix);

        //bigote inferior: de Q1 al mínimo
        g.drawLine(cx, yQ1Pix, cx, yMinPix);
        g.drawLine(cx - tapa, yMinPix, cx + tapa, yMinPix);

        //punto amarillo en la media
        g.setColor(Color.YELLOW);
        g.fillOval(cx - 6, yMediaPix - 6, 12, 12);
        g.setColor(colorBorde);
        g.drawOval(cx - 6, yMediaPix - 6, 12, 12);
    }

    //mínimo entre las categorías activas
    private int calcularMinGlobal() {
        int min = Integer.MAX_VALUE;
        if (mostrarCat1 && misDatos != null && misDatos.minimo() < min) {
            min = misDatos.minimo();
        }
        if (mostrarCat2 && datosCat2 != null && datosCat2.minimo() < min) {
            min = datosCat2.minimo();
        }
        return (min == Integer.MAX_VALUE) ? 0 : min;
    }

    //máximo entre las categorías activas
    private int calcularMaxGlobal() {
        int max = 0;
        if (mostrarCat1 && misDatos != null && misDatos.maximo() > max) {
            max = misDatos.maximo();
        }
        if (mostrarCat2 && datosCat2 != null && datosCat2.maximo() > max) {
            max = datosCat2.maximo();
        }
        return max;
    }

    //convierte un valor numérico a píxeles en el eje Y (invertido: valores grandes arriba)
    private int pyVal(int val, int yMin, int yMax, int vMin, int vMax) {
        if (vMax == vMin) {
            return (yMin + yMax) / 2;
        }
        return yMax - (val - vMin) * (yMax - yMin) / (vMax - vMin);
    }

    //dibuja los cuadros de color y el punto de media en la leyenda
    private void dibujarLeyenda(Graphics g, int ancho, int alto) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        int xL = MG_IZQ + 5;
        int yL = alto - 10;

        if (mostrarCat1) {
            g.setColor(colorRelleno1);
            g.fillRect(xL, yL - 10, 12, 12);
            g.setColor(Color.BLACK);
            g.drawRect(xL, yL - 10, 12, 12);
            g.drawString(" " + nombreCat1, xL + 14, yL);
            xL += 70;
        }

        if (mostrarCat2) {
            g.setColor(colorRelleno2);
            g.fillRect(xL, yL - 10, 12, 12);
            g.setColor(Color.BLACK);
            g.drawRect(xL, yL - 10, 12, 12);
            g.drawString(" " + nombreCat2, xL + 14, yL);
            xL += 70;
        }

        //círculo amarillo que representa la media en la leyenda
        g.setColor(Color.YELLOW);
        g.fillOval(xL, yL - 9, 10, 10);
        g.setColor(Color.BLACK);
        g.drawOval(xL, yL - 9, 10, 10);
        g.drawString(" Media", xL + 14, yL);
    }
}
