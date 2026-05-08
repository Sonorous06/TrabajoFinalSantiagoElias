package proyectofinal.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import proyectofinal.graficas.GraficaBigote;
import proyectofinal.graficas.GraficaPastel;
import proyectofinal.graficas.GraficaSecuencia;
import proyectofinal.logica.Estadisticos;
import proyectofinal.persistencia.LeerArchivo;

public class Main extends JFrame implements ActionListener {

    //nombres de los autores que se muestran en "Acerca de"
    private static final String AUTORES = "Autores del proyecto:\n[Tu nombre]\n[Nombre de tu compañero]";

    //letras de las dos categorías que maneja el programa
    private static final String CAT1 = "A";
    private static final String CAT2 = "G";

    //panel con las tres pestañas
    private JTabbedPane pestañas;

    //las tres gráficas, empiezan en null hasta que se cargue un archivo
    private GraficaPastel graficaPastel;
    private GraficaSecuencia graficaSecuencia;
    private GraficaBigote graficaBigote;

    //panel contenedor de cada pestaña
    private JPanel panelPastel;
    private JPanel panelSecuencia;
    private JPanel panelBigote;

    //botones del pastel para elegir entre suma de valores o porcentaje
    private JToggleButton btnNumeros;
    private JToggleButton btnPorcentaje;
    private ButtonGroup grupoPastel; //agrupa los botones para que solo uno esté activo a la vez

    //checkboxes para mostrar u ocultar cada categoría en la gráfica de secuencia
    private JCheckBox chkSeqCat1;
    private JCheckBox chkSeqCat2;

    //checkboxes para mostrar u ocultar cada categoría en el bigote
    private JCheckBox chkBoxCat1;
    private JCheckBox chkBoxCat2;

    //etiqueta al pie de la ventana que muestra el estado del programa
    private JLabel lblEstado;

    public Main() {
        super("Visualización de Datos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); //abre la ventana centrada en la pantalla

        construirMenu();
        construirContenido();
        construirBarraEstado();
 
        setVisible(true);
    }

    //crea la barra de menú con las opciones Archivo y Acerca de
    private void construirMenu() {
        JMenuBar barra = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");

        //opción para abrir el selector de archivo
        JMenuItem itemCargar = new JMenuItem("Cargar datos...");
        itemCargar.setActionCommand("cargar");
        itemCargar.addActionListener(this);

        //opción para cerrar el programa
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.setActionCommand("salir");
        itemSalir.addActionListener(this);

        menuArchivo.add(itemCargar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        //menú que muestra los nombres del equipo
        JMenu menuAcerca = new JMenu("Acerca de");
        JMenuItem itemAutores = new JMenuItem("Autores");
        itemAutores.setActionCommand("autores");
        itemAutores.addActionListener(this);
        menuAcerca.add(itemAutores);

        barra.add(menuArchivo);
        barra.add(menuAcerca);
        setJMenuBar(barra);
    }

    //arma el área principal: botón de carga arriba y las tres pestañas abajo
    private void construirContenido() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());

        //botón de carga en la parte superior
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCargar = new JButton("Cargar datos");
        btnCargar.setActionCommand("cargar");
        btnCargar.addActionListener(this);
        panelTop.add(btnCargar);
        panelPrincipal.add(panelTop, BorderLayout.NORTH);

        //creamos las tres pestañas y las agregamos al panel
        pestañas = new JTabbedPane();

        panelPastel = construirPestañaPastel();
        panelSecuencia = construirPestañaSecuencia();
        panelBigote = construirPestañaBigote();

        pestañas.addTab("Pastel", panelPastel);
        pestañas.addTab("Secuencia", panelSecuencia);
        pestañas.addTab("Bigote", panelBigote);

        panelPrincipal.add(pestañas, BorderLayout.CENTER);
        add(panelPrincipal);
    }

    //pestaña del pastel con sus dos botones de modo
    private JPanel construirPestañaPastel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        btnNumeros = new JToggleButton("Número de elementos", true);
        btnPorcentaje = new JToggleButton("Porcentaje");

        btnNumeros.setActionCommand("pastel_numeros");
        btnPorcentaje.setActionCommand("pastel_porcentaje");
        btnNumeros.addActionListener(this);
        btnPorcentaje.addActionListener(this);

        //el grupo hace que solo un botón esté activo a la vez
        grupoPastel = new ButtonGroup();
        grupoPastel.add(btnNumeros);
        grupoPastel.add(btnPorcentaje);

        controles.add(new JLabel("Mostrar como:"));
        controles.add(btnNumeros);
        controles.add(btnPorcentaje);
        panel.add(controles, BorderLayout.SOUTH);

        //mensaje inicial hasta que se cargue un archivo
        panel.add(new JLabel("Carga un archivo para ver la gráfica", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    //pestaña de secuencia con checkboxes para mostrar u ocultar cada categoría
    private JPanel construirPestañaSecuencia() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        chkSeqCat1 = new JCheckBox("Categoría " + CAT1, true);
        chkSeqCat2 = new JCheckBox("Categoría " + CAT2, false);

        chkSeqCat1.setActionCommand("seq_cat1");
        chkSeqCat2.setActionCommand("seq_cat2");
        chkSeqCat1.addActionListener(this);
        chkSeqCat2.addActionListener(this);

        controles.add(new JLabel("Mostrar:"));
        controles.add(chkSeqCat1);
        controles.add(chkSeqCat2);
        panel.add(controles, BorderLayout.SOUTH);

        panel.add(new JLabel("Carga un archivo para ver la gráfica", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    //pestaña del bigote con checkboxes para mostrar u ocultar cada categoría
    private JPanel construirPestañaBigote() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        chkBoxCat1 = new JCheckBox("Categoría " + CAT1, true);
        chkBoxCat2 = new JCheckBox("Categoría " + CAT2, false);

        chkBoxCat1.setActionCommand("box_cat1");
        chkBoxCat2.setActionCommand("box_cat2");
        chkBoxCat1.addActionListener(this);
        chkBoxCat2.addActionListener(this);

        controles.add(new JLabel("Mostrar:"));
        controles.add(chkBoxCat1);
        controles.add(chkBoxCat2);
        panel.add(controles, BorderLayout.SOUTH);

        panel.add(new JLabel("Carga un archivo para ver la gráfica", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    //etiqueta en la parte de abajo que le dice al usuario qué está pasando
    private void construirBarraEstado() {
        lblEstado = new JLabel("  Listo. Carga un archivo para comenzar.");
        add(lblEstado, BorderLayout.SOUTH);
    }

    //aquí llegan todos los eventos de botones, menús y checkboxes
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "cargar":
                cargarDatos();
                break;

            case "salir":
                System.exit(0);
                break;

            //muestra un cuadro con los nombres del equipo
            case "autores":
                JOptionPane.showMessageDialog(this, AUTORES, "Acerca del proyecto", JOptionPane.INFORMATION_MESSAGE);
                break;

            //cambia el modo del pastel entre suma de valores y porcentaje
            case "pastel_numeros":
                if (graficaPastel != null) graficaPastel.setMostrarNumeros(true);
                break;

            case "pastel_porcentaje":
                if (graficaPastel != null) graficaPastel.setMostrarNumeros(false);
                break;

            //muestra u oculta cada categoría en la gráfica de secuencia
            case "seq_cat1":
                if (graficaSecuencia != null) graficaSecuencia.setMostrarCat1(chkSeqCat1.isSelected());
                break;

            case "seq_cat2":
                if (graficaSecuencia != null) graficaSecuencia.setMostrarCat2(chkSeqCat2.isSelected());
                break;

            //muestra u oculta cada categoría en el bigote
            case "box_cat1":
                if (graficaBigote != null) graficaBigote.setMostrarCat1(chkBoxCat1.isSelected());
                break;

            case "box_cat2":
                if (graficaBigote != null) graficaBigote.setMostrarCat2(chkBoxCat2.isSelected());
                break;
        }
    }

    //abre el selector de archivo una sola vez y saca los datos de las dos categorías
    private void cargarDatos() {
        JFileChooser selector = new JFileChooser();
        int estado = selector.showOpenDialog(this);

        //si el usuario canceló, no hacemos nada
        if (estado != JFileChooser.APPROVE_OPTION) {
            lblEstado.setText("  Carga cancelada.");
            return;
        }

        //con el mismo archivo filtramos cada categoría por separado
        java.io.File archivo = selector.getSelectedFile();
        int[] arr1 = LeerArchivo.obtenerDatosDe(archivo, CAT1);
        int[] arr2 = LeerArchivo.obtenerDatosDe(archivo, CAT2);

        //si no encontró datos de alguna categoría avisamos y paramos
        if (arr1 == null || arr1.length == 0) {
            lblEstado.setText("  No se encontraron datos para " + CAT1);
            return;
        }
        if (arr2 == null || arr2.length == 0) {
            lblEstado.setText("  No se encontraron datos para " + CAT2);
            return;
        }

        //creamos los objetos estadísticos con los datos de cada categoría
        Estadisticos datosA = new Estadisticos(arr1, arr1.length);
        Estadisticos datosG = new Estadisticos(arr2, arr2.length);

        actualizarGraficas(datosA, datosG);
    }

    //crea las tres gráficas con los datos cargados y las mete en sus pestañas
    private void actualizarGraficas(Estadisticos datosA, Estadisticos datosG) {

        graficaPastel = new GraficaPastel("Pastel", datosA, CAT1, datosG, CAT2, btnNumeros.isSelected());
        panelPastel.removeAll();
        panelPastel.add(graficaPastel, BorderLayout.CENTER);
        panelPastel.add(construirControlesPastel(), BorderLayout.SOUTH);
        panelPastel.repaint();

        graficaSecuencia = new GraficaSecuencia("Secuencia", datosA, CAT1, datosG, CAT2, chkSeqCat1.isSelected(), chkSeqCat2.isSelected());
        panelSecuencia.removeAll();
        panelSecuencia.add(graficaSecuencia, BorderLayout.CENTER);
        panelSecuencia.add(construirControlesSecuencia(), BorderLayout.SOUTH);
        panelSecuencia.repaint();

        graficaBigote = new GraficaBigote("Boxplot", datosA, CAT1, datosG, CAT2, chkBoxCat1.isSelected(), chkBoxCat2.isSelected());
        panelBigote.removeAll();
        panelBigote.add(graficaBigote, BorderLayout.CENTER);
        panelBigote.add(construirControlesBigote(), BorderLayout.SOUTH);
        panelBigote.repaint();

        //actualizamos la barra de estado con cuántos registros se cargaron
        lblEstado.setText("  Datos cargados — " + CAT1 + ": " + datosA.getNumDatos() + " registros | " + CAT2 + ": " + datosG.getNumDatos() + " registros");
    }

    //arma el panel de botones del pastel
    private JPanel construirControlesPastel() {
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        controles.add(new JLabel("Mostrar como:"));
        controles.add(btnNumeros);
        controles.add(btnPorcentaje);
        return controles;
    }

    //arma el panel de checkboxes de secuencia
    private JPanel construirControlesSecuencia() {
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        controles.add(new JLabel("Mostrar:"));
        controles.add(chkSeqCat1);
        controles.add(chkSeqCat2);
        return controles;
    }

    //arma el panel de checkboxes del bigote
    private JPanel construirControlesBigote() {
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        controles.add(new JLabel("Mostrar:"));
        controles.add(chkBoxCat1);
        controles.add(chkBoxCat2);
        return controles;
    }

    public static void main(String[] args) {
        new Main();
    }
}