package proyectofinal.graficas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import proyectofinal.logica.Estadisticos;
import proyectofinal.logica.Graficos;

public class GraficaSecuencia extends Graficos {

    //datos de la segunda categoría
    private Estadisticos datosCat2;
    private String nombreCat1;
    private String nombreCat2;
    private boolean mostrarCat1; //controla si se dibuja la categoría 1
    private boolean mostrarCat2; //controla si se dibuja la categoría 2

    //colores para las líneas de datos y las bandas estadísticas
    private static final Color COLOR_CAT1 = new Color(0, 0, 200);
    private static final Color COLOR_CAT2 = new Color(200, 0, 0);
    private static final Color COLOR_MEDIA_C1 = new Color(0, 180, 180);
    private static final Color COLOR_MEDIA_C2 = new Color(255, 140, 0);
    private static final Color COLOR_DESV_C1 = new Color(100, 200, 255);
    private static final Color COLOR_DESV_C2 = new Color(255, 200, 100);

    //márgenes del área de dibujo dentro del panel
    private static final int MG_IZQ = 55;
    private static final int MG_DER = 20;
    private static final int MG_SUP = 40;
    private static final int MG_INF = 50;

    public GraficaSecuencia(String titulo, Estadisticos datosCat1, String nombreCat1, Estadisticos datosCat2, String nombreCat2, boolean mostrarCat1, boolean mostrarCat2) {
  
        super(titulo, datosCat1);
        this.datosCat2 = datosCat2;
        this.nombreCat1 = nombreCat1;
        this.nombreCat2 = nombreCat2;
        this.mostrarCat1 = mostrarCat1;
        this.mostrarCat2 = mostrarCat2;
    }

    //activan o desactivan cada categoría y redibujan la gráfica
    public void setMostrarCat1(boolean v) {
        this.mostrarCat1 = v; repaint(); 
    }
    public void setMostrarCat2(boolean v) { 
        this.mostrarCat2 = v; repaint(); 
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
        int yMin = MG_SUP;  //arriba = valores grandes
        int yMax = alto - MG_INF; //abajo = valores pequeños

        int valMax = calcularMaxGlobal();
        if (valMax == 0) valMax = 60;

        //ejes
        g.setColor(Color.BLACK);
        g.drawLine(xMin, yMax, xMax, yMax); //eje X
        g.drawLine(xMin, yMin, xMin, yMax); //eje Y

        //marcas y números del eje Y
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        for (int i = 0; i <= 6; i++) {
            int val = valMax * i / 6;
            int yPix = pyVal(val, yMin, yMax, 0, valMax);
            g.drawLine(xMin - 4, yPix, xMin, yPix);
            g.drawString(String.valueOf(val), xMin - 28, yPix + 4);
        }

        //etiqueta "Valor" del eje Y, carácter a carácter porque no podemos rotar sin Graphics2D
        String ejeY = "Valor";
        int yLetra  = (yMin + yMax) / 2 - 25;
        for (int i = 0; i < ejeY.length(); i++) {
            g.drawString(String.valueOf(ejeY.charAt(i)), 5, yLetra + i * 11);
        }

        //dibujamos las series que estén activas
        if (mostrarCat1 && misDatos != null) {
            dibujarSerie(g, misDatos.getDatos(), xMin, xMax, yMin, yMax, 0, valMax, COLOR_CAT1, COLOR_MEDIA_C1, COLOR_DESV_C1, misDatos.media(), misDatos.desvStd());
        }
        
        if (mostrarCat2 && datosCat2 != null) {
            dibujarSerie(g, datosCat2.getDatos(), xMin, xMax, yMin, yMax, 0, valMax, COLOR_CAT2, COLOR_MEDIA_C2, COLOR_DESV_C2, datosCat2.media(), datosCat2.desvStd());
        }

        //números del eje X (posición 1, 2, 3...)
        int nPuntos = calcularMaxPuntos();
        g.setColor(Color.BLACK);
        for (int i = 0; i < nPuntos; i++) {
            int xPix = pxPos(i, nPuntos, xMin, xMax);
            g.drawLine(xPix, yMax, xPix, yMax + 4);
            g.drawString(String.valueOf(i + 1), xPix - 3, yMax + 15);
        }

        dibujarLeyenda(g, ancho, alto);
    }

    //dibuja una serie completa: línea de media, bandas ±σ, línea de datos y puntos
    private void dibujarSerie(Graphics g, int[] datos, int xMin, int xMax, int yMin, int yMax, int vMin, int vMax, Color colorLinea, Color colorMedia, Color colorDesv, double media, double desv) {

        int n = datos.length;
        if (n == 0) return;

        //línea horizontal de la media
        int yMedia = pyVal((int) media, yMin, yMax, vMin, vMax);
        g.setColor(colorMedia);
        g.drawLine(xMin, yMedia, xMax, yMedia);

        //líneas punteadas de media + σ y media - σ
        g.setColor(colorDesv);
        int yMas = pyVal((int) (media + desv), yMin, yMax, vMin, vMax);
        int yMenos = pyVal((int) (media - desv), yMin, yMax, vMin, vMax);
        if (yMenos > yMax) yMenos = yMax; //que no salga del área de dibujo
        dibujarLineaPunteada(g, xMin, yMas,   xMax, yMas);
        dibujarLineaPunteada(g, xMin, yMenos, xMax, yMenos);

        //línea que conecta todos los valores en secuencia
        g.setColor(colorLinea);
        for (int i = 0; i < n - 1; i++) {
            g.drawLine(pxPos(i, n, xMin, xMax), pyVal(datos[i], yMin, yMax, vMin, vMax), pxPos(i + 1, n, xMin, xMax), pyVal(datos[i + 1], yMin, yMax, vMin, vMax));
        }

        //círculos sobre cada punto de la línea
        for (int i = 0; i < n; i++) {
            int xp = pxPos(i, n, xMin, xMax);
            int yp = pyVal(datos[i], yMin, yMax, vMin, vMax);
            g.setColor(Color.WHITE);
            g.fillOval(xp - 4, yp - 4, 8, 8);
            g.setColor(colorLinea);
            g.drawOval(xp - 4, yp - 4, 8, 8);
        }
    }

    //simula una línea punteada alternando segmentos de 6px dibujados y 4px vacíos
    private void dibujarLineaPunteada(Graphics g, int x1, int y1, int x2, int y2) {
        int x = x1;
        boolean on = true;
        while (x < x2) {
            int xFin = x + (on ? 6 : 4);
            if (xFin > x2) xFin = x2;
            if (on) g.drawLine(x, y1, xFin, y2);
            x  = xFin;
            on = !on;
        }
    }

    //devuelve el valor máximo entre las categorías activas más un 10% de margen
    private int calcularMaxGlobal() {
        int max = 0;
        if (mostrarCat1 && misDatos != null && misDatos.maximo() > max) max = misDatos.maximo();
        if (mostrarCat2 && datosCat2 != null && datosCat2.maximo() > max) max = datosCat2.maximo();
        return (int) (max * 1.1) + 1;
    }

    //devuelve cuántos puntos tiene la serie más larga (para escalar el eje X)
    private int calcularMaxPuntos() {
        int n1 = (mostrarCat1 && misDatos  != null) ? misDatos.getNumDatos() : 0;
        int n2 = (mostrarCat2 && datosCat2 != null) ? datosCat2.getNumDatos() : 0;
        int n = (n1 > n2) ? n1 : n2;
        return (n > 0) ? n : 1;
    }

    //convierte un índice de posición a píxeles en el eje X
    private int pxPos(int i, int total, int xMin, int xMax) {
        if (total <= 1) return (xMin + xMax) / 2;
        return xMin + i * (xMax - xMin) / (total - 1);
    }

    //convierte un valor numérico a píxeles en el eje Y (invertido: valores grandes arriba)
    private int pyVal(int val, int yMin, int yMax, int vMin, int vMax) {
        if (vMax == vMin) return (yMin + yMax) / 2;
        return yMax - (val - vMin) * (yMax - yMin) / (vMax - vMin);
    }

    //dibuja la leyenda con los colores de cada serie y las líneas estadísticas
    private void dibujarLeyenda(Graphics g, int ancho, int alto) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        int xL = MG_IZQ + 5;
        int yL = alto - MG_INF + 22;
        int paso = 100;

        if (mostrarCat1) {
            g.setColor(COLOR_CAT1);
            g.fillRect(xL, yL - 8, 14, 3);
            g.setColor(Color.BLACK);
            g.drawString(nombreCat1, xL + 18, yL);
            xL += paso;
        }
        if (mostrarCat2) {
            g.setColor(COLOR_CAT2);
            g.fillRect(xL, yL - 8, 14, 3);
            g.setColor(Color.BLACK);
            g.drawString(nombreCat2, xL + 18, yL);
            xL += paso;
        }

        //entrada de la línea de media
        g.setColor(COLOR_MEDIA_C1);
        g.fillRect(xL, yL - 8, 14, 3);
        g.setColor(Color.BLACK);
        g.drawString("Media", xL + 18, yL);
        xL += paso;

        //entrada de las bandas ±σ con línea punteada
        g.setColor(COLOR_DESV_C1);
        dibujarLineaPunteada(g, xL, yL - 6, xL + 14, yL - 6);
        g.setColor(Color.BLACK);
        g.drawString("Media ", xL + 18, yL);
    }
}