package proyectofinal.logica;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Clase base para todas las graficas del sistema.
 * Define la estructura comun, como el título y los datos estadísticos, 
 * que cualquier gráfica debe tener para funcionar.
 */
public abstract class Graficos extends JPanel {

    public String titulo; //el nombre que esta arriba de la grafia
    public String ejeX; // nombre del eje horiz
    public String ejeY; // nombre del eje vert
            
   //todas las graficas necesitan los calculos de la clase
    protected Estadisticos misDatos;
    
    /**
     * Constructor para inicializar una gráfica con su nombre y su conjunto de datos.
     * 
     * @param titulo El texto que se mostrará como título.
     * @param datos Los datos ya procesados por la clase Estadisticos.
     */
    //cuando crees una grafica , necesitas darle titulo y los datos
    public Graficos(String titulo , Estadisticos datos){
        this.titulo = titulo;
        this.misDatos = datos;
    }
    
    /**
     * Método obligatorio que define cómo se debe dibujar cada tipo de gráfica.
     * Cada clase hija debe escribir su propio código aquí.
     * 
     * @param g El objeto de dibujo de Java (Graphics).
     */
    // necesitas escribir el codigo del dibujo en cada grafica 
    public abstract void dibujarGrafica(Graphics g);
    
    /**
     * Método de Java Swing que se encarga de recargar el panel.
     * Llama automáticamente a dibujarGrafica para mostrar los cambios.
     * 
     * @param g El contexto de gráficos del sistema.
     */
    // esete se llama automaticamente cuando se abre la ventana
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        dibujarGrafica(g);       
    }
}
