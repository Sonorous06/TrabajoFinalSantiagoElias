package proyectofinal.logica;

import java.awt.Graphics;
import javax.swing.JPanel;

public abstract class Graficos extends JPanel {

    //wey con estas haces que tus subclases , uses los datos directamente
    protected String titulo; //el nombre que esta arriba de la grafia
    protected String ejeX; // nombre del eje horiz
    protected String ejeY; // nombre del eje vert
            
   //todas las graficas necesitan los calculos de la clase
    protected Estadisticos misDatos;
    
    //cuando crees una grafica , necesitas darle titulo y los datos
    public Graficos(String titulo , Estadisticos datos){
        this.titulo = titulo;
        this.misDatos = datos;
    }
    
    // necesitas escribir el codigo del sibujo en cada grafica 
    public abstract void dibujarGrafica(Graphics g);
    
    // esete se llama automaticamente cuando se abre la ventana
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        dibujarGrafica(g);       
    }
}
