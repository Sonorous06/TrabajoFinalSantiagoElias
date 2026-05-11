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
    private boolean mostrarCat1;
    private boolean mostrarCat2;

    //colores para las líneas de datos y las bandas estadísticas
    private static final Color colorCat1 = new Color(0, 0, 200);
    private static final Color colorCat2 = new Color(200, 0, 0);
    private static final Color colorMediaC1 = new Color(0, 180, 180);
    private static final Color colorMediaC2 = new Color(255, 140, 0);
    private static final Color colorDesvC1 = new Color(100, 200, 255);
    private static final Color colorDesvC2 = new Color(255, 200, 100);

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

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ancho, alto);

        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.drawString(titulo, ancho / 3, 22);

        int xMin = MG_IZQ;
        int xMax = ancho - MG_DER;
        int yMin = MG_SUP;
        int yMax = alto - MG_INF;

        int valMax = calcularMaxGlobal();
        if (valMax == 0) valMax = 10;

        g.setColor(Color.BLACK);
        g.drawLine(xMin, yMax, xMax, yMax);
        g.drawLine(xMin, yMin, xMin, yMax);

        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        for (int i = 0; i <= 6; i++) {
            int val = valMax * i / 6;
            int yPix = pyVal(val, yMin, yMax, 0, valMax);
            g.drawLine(xMin - 4, yPix, xMin, yPix);
            g.drawString(String.valueOf(val), xMin - 28, yPix + 4);
        }

        String ejeY = "Valor";
        int yLetra = (yMin + yMax) / 2 - 25;
        for (int i = 0; i < ejeY.length(); i++) {
            g.drawString(String.valueOf(ejeY.charAt(i)), 5, yLetra + i * 11);
        }

        if (mostrarCat1 && misDatos != null) {
            dibujarSerie(g, misDatos.getDatosOriginales(), xMin, xMax, yMin, yMax, 0, valMax, colorCat1, colorMediaC1, colorDesvC1, misDatos.media(), misDatos.desvStd());
        }

        if (mostrarCat2 && datosCat2 != null) {
            dibujarSerie(g, datosCat2.getDatosOriginales(), xMin, xMax, yMin, yMax, 0, valMax, colorCat2, colorMediaC2, colorDesvC2, datosCat2.media(), datosCat2.desvStd());
        }

        int nPuntos = calcularMaxPuntos();
        g.setColor(Color.BLACK);
        for (int i = 0; i < nPuntos; i++) {
            int xPix = pxPos(i, nPuntos, xMin, xMax);
            g.drawLine(xPix, yMax, xPix, yMax + 4);
            g.drawString(String.valueOf(i + 1), xPix - 3, yMax + 15);
        }

        dibujarLeyenda(g, ancho, alto);
    }

    private void dibujarSerie(Graphics g, int[] datos, int xMin, int xMax, int yMin, int yMax, int vMin, int vMax, Color colorLinea, Color colorMedia, Color colorDesv, double media, double desv) {

        int n = datos.length;
        if (n == 0) return;

        int yMedia = pyVal((int) media, yMin, yMax, vMin, vMax);
        g.setColor(colorMedia);
        g.drawLine(xMin, yMedia, xMax, yMedia);

        g.setColor(colorDesv);
        int yMas = pyVal((int) (media + desv), yMin, yMax, vMin, vMax);
        int yMenos = pyVal((int) (media - desv), yMin, yMax, vMin, vMax);
        if (yMenos > yMax) yMenos = yMax;
        dibujarLineaPunteada(g, xMin, yMas, xMax, yMas);
        dibujarLineaPunteada(g, xMin, yMenos, xMax, yMenos);

        g.setColor(colorLinea);
        for (int i = 0; i < n - 1; i++) {
            g.drawLine(pxPos(i, n, xMin, xMax), pyVal(datos[i], yMin, yMax, vMin, vMax), pxPos(i + 1, n, xMin, xMax), pyVal(datos[i + 1], yMin, yMax, vMin, vMax));
        }

        for (int i = 0; i < n; i++) {
            int xp = pxPos(i, n, xMin, xMax);
            int yp = pyVal(datos[i], yMin, yMax, vMin, vMax);
            g.setColor(Color.WHITE);
            g.fillOval(xp - 4, yp - 4, 8, 8);
            g.setColor(colorLinea);
            g.drawOval(xp - 4, yp - 4, 8, 8);
        }
    }

    private void dibujarLineaPunteada(Graphics g, int x1, int y1, int x2, int y2) {
        int x = x1;
        boolean on = true;
        while (x < x2) {
            int xFin = x + (on ? 6 : 4);
            if (xFin > x2) xFin = x2;
            if (on) g.drawLine(x, y1, xFin, y2);
            x = xFin;
            on = !on;
        }
    }

    private int calcularMaxGlobal() {
        int max = 0;
        if (mostrarCat1 && misDatos != null && misDatos.maximo() > max) max = misDatos.maximo();
        if (mostrarCat2 && datosCat2 != null && datosCat2.maximo() > max) max = datosCat2.maximo();
        return (int) (max * 1.1) + 1;
    }

    private int calcularMaxPuntos() {
        int n1 = (mostrarCat1 && misDatos != null) ? misDatos.getNumDatos() : 0;
        int n2 = (mostrarCat2 && datosCat2 != null) ? datosCat2.getNumDatos() : 0;
        int n = (n1 > n2) ? n1 : n2;
        return (n > 0) ? n : 1;
    }

    private int pxPos(int i, int total, int xMin, int xMax) {
        if (total <= 1) return (xMin + xMax) / 2;
        return xMin + i * (xMax - xMin) / (total - 1);
    }

    private int pyVal(int val, int yMin, int yMax, int vMin, int vMax) {
        if (vMax == vMin) return (yMin + yMax) / 2;
        return yMax - (val - vMin) * (yMax - yMin) / (vMax - vMin);
    }

    private void dibujarLeyenda(Graphics g, int ancho, int alto) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        int xL = MG_IZQ + 5;
        int yL = alto - MG_INF + 22;
        int paso = 100;

        if (mostrarCat1) {
            g.setColor(colorCat1);
            g.fillRect(xL, yL - 8, 14, 3);
            g.setColor(Color.BLACK);
            g.drawString(nombreCat1, xL + 18, yL);
            xL += paso;
        }
        if (mostrarCat2) {
            g.setColor(colorCat2);
            g.fillRect(xL, yL - 8, 14, 3);
            g.setColor(Color.BLACK);
            g.drawString(nombreCat2, xL + 18, yL);
            xL += paso;
        }

        g.setColor(colorMediaC1);
        g.fillRect(xL, yL - 8, 14, 3);
        g.setColor(Color.BLACK);
        g.drawString("Media", xL + 18, yL);
        xL += paso;

        g.setColor(colorDesvC1);
        dibujarLineaPunteada(g, xL, yL - 6, xL + 14, yL - 6);
        g.setColor(Color.BLACK);
        g.drawString("Media±σ", xL + 18, yL);
    }
}