package proyectofinal.graficas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import proyectofinal.logica.Estadisticos;
import proyectofinal.logica.Graficos;

/**
 * Esta clase dibuja una grafica de caja y bigotes permite comparar visualmente
 * dos categorías de datos, mostrando sus cuartiles, valores min, max y
 * la media.
 */
public class GraficaBigote extends Graficos {

    //datos de la segunda categoría
    private Estadisticos datosCat2;
    private String nombreCat1;
    private String nombreCat2;
    private boolean mostrarCat1; //controla si se dibuja la caja de la categoria 1
    private boolean mostrarCat2; //controla si se dibuja la caja de la categoria 2

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

    /**
     * Crea una nueva grafica de bigotes configurando los datos y que categorias
     * mostrar.
     *
     * @param titulo Texto que aparecerá arriba de la gráfica.
     * @param datosCat1 Objeto con los cálculos de la primera categoría.
     * @param nombreCat1 Nombre de la primera categoría.
     * @param datosCat2 Objeto con los cálculos de la segunda categoría.
     * @param nombreCat2 Nombre de la segunda categoría.
     * @param mostrarCat1 Si es verdadero, se dibuja la primera caja.
     * @param mostrarCat2 Si es verdadero, se dibuja la segunda caja.
     */
    public GraficaBigote(String titulo, Estadisticos datosCat1, String nombreCat1, Estadisticos datosCat2, String nombreCat2, boolean mostrarCat1, boolean mostrarCat2) {

        super(titulo, datosCat1);
        this.datosCat2 = datosCat2;
        this.nombreCat1 = nombreCat1;
        this.nombreCat2 = nombreCat2;
        this.mostrarCat1 = mostrarCat1;
        this.mostrarCat2 = mostrarCat2;
    }

    /**
     * Cambia la visibilidad de la categoria 1 y actualiza el dibujo.
     *
     * @param v verdadero para mostrar, falso para ocultar.
     */
    //activan o desactivan cada categoria y redibujan la gráfica
    public void setMostrarCat1(boolean v) {
        this.mostrarCat1 = v;
        repaint();
    }

    public void setMostrarCat2(boolean v) {
        this.mostrarCat2 = v;
        repaint();
    }

    /**
     * Metodo principal de dibujo que organiza el fondo, los ejes, la cuadricula
     * y llama a la creación de las cajas.
     *
     * @param g Objeto de graficos donde se realiza el dibujo.
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
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.drawString(titulo, ancho / 3, 22);

        //límites del area donde se dibuja la grafica
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

    /**
     * Dibuja los elementos de una caja individual: cuerpo, bigotes y el punto
     * de la media.
     *
     * @param g Objeto de gráficos.
     * @param datos Objeto con los valores estadísticos (mín, máx, cuartiles,
     * media).
     * @param cx Posición horizontal central de la caja.
     * @param anchoCaja Ancho que tendrá la caja.
     * @param yMin Límite superior del área de dibujo (en píxeles).
     * @param yMax Límite inferior del área de dibujo (en píxeles).
     * @param vMin Valor numérico mínimo en el eje Y.
     * @param vMax Valor numérico máximo en el eje Y.
     * @param colorBorde Color de las líneas.
     * @param colorRell Color de relleno de la caja.
     */
    //dibuja una caja completa: relleno Q1-Q3, borde, mediana, bigotes y punto de media
    private void dibujarCaja(Graphics g, Estadisticos datos, int cx, int anchoCaja, int yMin, int yMax, int vMin, int vMax, Color colorBorde, Color colorRell) {

        //convertimos los cinco puntos clave a píxeles
        int yMinPix = pyVal(datos.minimo(), yMin, yMax, vMin, vMax);
        int yMaxPix = pyVal(datos.maximo(), yMin, yMax, vMin, vMax);
        int yQ1Pix = pyVal((int) datos.cuartil1(), yMin, yMax, vMin, vMax);
        int yQ2Pix = pyVal((int) datos.cuartil2(), yMin, yMax, vMin, vMax);
        int yQ3Pix = pyVal((int) datos.cuartil3(), yMin, yMax, vMin, vMax);
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

    /**
     * Busca el valor más pequeño entre todas las categorías que se están mostrando.
     * @return El valor mínimo global.
     */
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

    /**
     * Busca el valor más grande entre todas las categorías que se están mostrando.
     * @return El valor máximo global.
     */
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

    /**
     * Convierte un valor de los datos a una posición en píxeles dentro de la ventana.
     * 
     * @param val Valor numérico a convertir.
     * @param yMin Límite superior del dibujo.
     * @param yMax Límite inferior del dibujo.
     * @param vMin Valor mínimo del eje.
     * @param vMax Valor máximo del eje.
     * @return La posición en píxeles calculada.
     */
    private int pyVal(int val, int yMin, int yMax, int vMin, int vMax) {
        if (vMax == vMin) {
            return (yMin + yMax) / 2;
        }
        return yMax - (val - vMin) * (yMax - yMin) / (vMax - vMin);
    }

    /**
     * Dibuja una leyenda en la parte inferior para ver cada categoría 
     * 
     * @param g Objeto de gráficos.
     * @param ancho Ancho total del panel.
     * @param alto Alto total del panel.
     */
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
