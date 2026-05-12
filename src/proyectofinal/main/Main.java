package proyectofinal.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import proyectofinal.graficas.GraficaBigote;
import proyectofinal.graficas.GraficaPastel;
import proyectofinal.graficas.GraficaSecuencia;
import proyectofinal.logica.Estadisticos;
import proyectofinal.persistencia.LeerArchivo;

/**
 * Ventana principal del sistema de Visualización de Datos. Esta clase construye
 * la interfaz de usuario, los eventos de los botones
 *
 * @author Santiago Guillermo Lopez
 * @author Elias Miguel Sigales
 */
public class Main extends JFrame implements ActionListener {

    //nombres de los autores que se muestran en "Acerca de"
    private static final String AUTORES = "Autores del proyecto:\nElias Miguel Sigales\nSantiago Guillermo Lopez";

    //panel con las tres pestañas
    private JTabbedPane pestañas;

    //las tres gráficas, empiezan en null hasta que se cargue un archivo
    private GraficaPastel graficaPastel;
    private GraficaSecuencia graficaSecuencia;
    private GraficaBigote graficaBigote;

    //aviso para avisar que necesita archvio para ver la grafica
    private static final String avisoGrafica = "Carga un archivo para ver la gráfica";

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

    /**
     * Constructor que inicializa la ventana principal. Configura el tamaño, la
     * posición centrada y construye los menús y paneles iniciales
     */
    public Main() {
        super("Visualización de Datos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); //abre la ventana centrada en la pantalla

        construirMenu();
        construirContenido();

        setVisible(true);
    }

    /**
     * Crea la barra de herramientas superior con opciones de información y
     * salida
     */
    private void construirMenu() {
        JMenuBar barra = new JMenuBar();

        JMenu menuAcerca = new JMenu("Acerca de");
        menuAcerca.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(Main.this, AUTORES, "Acerca del proyecto", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        //opción para cerrar el programa
        JMenu menuSalir = new JMenu("Salir");
        menuSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.exit(0);
            }
        });

        barra.add(menuAcerca);
        barra.add(menuSalir);
        setJMenuBar(barra);
    }

    /**
     * Define la estructura de paneles dentro de la ventana, incluyendo el botón
     * de carga y las pestanas para las gráficas.
     */
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

    /**
     * Crea el panel de la gráfica de pastel con sus controles
     *
     * @return El panel para la pestaña de pastel.
     */
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

        panel.add(new JLabel(avisoGrafica, SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    /**
     * Crea el panel de la gráfica de secuencia con filtros
     *
     * @return El panel para la pestaña de secuencia.
     */
    private JPanel construirPestañaSecuencia() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        chkSeqCat1 = new JCheckBox("Categoría ", true);
        chkSeqCat2 = new JCheckBox("Categoría ", false);

        chkSeqCat1.setActionCommand("seq_cat1");
        chkSeqCat2.setActionCommand("seq_cat2");
        chkSeqCat1.addActionListener(this);
        chkSeqCat2.addActionListener(this);

        controles.add(new JLabel("Mostrar:"));
        controles.add(chkSeqCat1);
        controles.add(chkSeqCat2);
        panel.add(controles, BorderLayout.SOUTH);

        //aviso grafica
        panel.add(new JLabel(avisoGrafica, SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    /**
     * Crea el panel de la gráfica de caja y bigote con filtros de categoría.
     * @return El panel para la pestaña de bigote.
     */
    private JPanel construirPestañaBigote() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        chkBoxCat1 = new JCheckBox("Categoría ", true);
        chkBoxCat2 = new JCheckBox("Categoría ", false);

        chkBoxCat1.setActionCommand("box_cat1");
        chkBoxCat2.setActionCommand("box_cat2");
        chkBoxCat1.addActionListener(this);
        chkBoxCat2.addActionListener(this);

        controles.add(new JLabel("Mostrar:"));
        controles.add(chkBoxCat1);
        controles.add(chkBoxCat2);
        panel.add(controles, BorderLayout.SOUTH);

        panel.add(new JLabel(avisoGrafica, SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    /**
     * Gestiona las acciones sobre los componentes de la interfaz
     * @param e El evento generado por el usuario
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "cargar":
                cargarDatos();
                break;

            case "salir":
                System.exit(0);
                break;

            //cambia el modo del pastel entre suma de valores y porcentaje
            case "pastel_numeros":
                if (graficaPastel != null) {
                    graficaPastel.setMostrarNumeros(true);
                }
                break;

            case "pastel_porcentaje":
                if (graficaPastel != null) {
                    graficaPastel.setMostrarNumeros(false);
                }
                break;

            //muestra u oculta cada categoría en la gráfica de secuencia
            case "seq_cat1":
                if (graficaSecuencia != null) {
                    graficaSecuencia.setMostrarCat1(chkSeqCat1.isSelected());
                }
                break;

            case "seq_cat2":
                if (graficaSecuencia != null) {
                    graficaSecuencia.setMostrarCat2(chkSeqCat2.isSelected());
                }
                break;

            //muestra u oculta cada categoría en el bigote
            case "box_cat1":
                if (graficaBigote != null) {
                    graficaBigote.setMostrarCat1(chkBoxCat1.isSelected());
                }
                break;

            case "box_cat2":
                if (graficaBigote != null) {
                    graficaBigote.setMostrarCat2(chkBoxCat2.isSelected());
                }
                break;
        }
    }

   /**
     * Proceso de selección de archivo y lectura de datos.
     * toma las categorías y construye los objetos estadísticos para las graficas.
     */
    private void cargarDatos() {
        JFileChooser selector = new JFileChooser();
        int estado = selector.showOpenDialog(this);
        if (estado != JFileChooser.APPROVE_OPTION) {
            return;
        }

        java.io.File archivo = selector.getSelectedFile();

        String[] cats = LeerArchivo.leerCategorias(archivo);

        if (cats.length < 2) {
            return;
        }

        int[] arr1 = LeerArchivo.obtenerDatosDe(archivo, cats[0]);
        int[] arr2 = LeerArchivo.obtenerDatosDe(archivo, cats[1]);

        Estadisticos datos1 = new Estadisticos(arr1, arr1.length);
        Estadisticos datos2 = new Estadisticos(arr2, arr2.length);

        actualizarGraficas(datos1, datos2, cats[0], cats[1]);
    }

    /**
     * Reemplaza las graficas por unas nuevas basadas en los datos cargados.
     * 
     * @param datos1 Estadísticas de la categoría 1
     * @param datos2 Estadísticas de la categoría .
     * @param cat1 Nombre de la categoría 1
     * @param cat2 Nombre de la categoría 2.
     */
    private void actualizarGraficas(Estadisticos datos1, Estadisticos datos2, String cat1, String cat2) {

        //para que muestre Categoria y junto el nombre de la categoria
        chkSeqCat1.setText("Categoría " + cat1);
        chkSeqCat2.setText("Categoría " + cat2);
        chkBoxCat1.setText("Categoría " + cat1);
        chkBoxCat2.setText("Categoría " + cat2);

        graficaPastel = new GraficaPastel("Pastel", datos1, cat1, datos2, cat2, btnNumeros.isSelected());
        panelPastel.removeAll();
        panelPastel.add(graficaPastel, BorderLayout.CENTER);
        panelPastel.add(construirControlesPastel(), BorderLayout.SOUTH);
        panelPastel.repaint();

        graficaSecuencia = new GraficaSecuencia("Secuencia", datos1, cat1, datos2, cat2, chkSeqCat1.isSelected(), chkSeqCat2.isSelected());
        panelSecuencia.removeAll();
        panelSecuencia.add(graficaSecuencia, BorderLayout.CENTER);
        panelSecuencia.add(construirControlesSecuencia(), BorderLayout.SOUTH);
        panelSecuencia.repaint();

        graficaBigote = new GraficaBigote("Caja y bigote", datos1, cat1, datos2, cat2, chkBoxCat1.isSelected(), chkBoxCat2.isSelected());
        panelBigote.removeAll();
        panelBigote.add(graficaBigote, BorderLayout.CENTER);
        panelBigote.add(construirControlesBigote(), BorderLayout.SOUTH);
        panelBigote.repaint();

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

    /**
     * Método de inicio de la aplicación.
     * @param args Argumentos de consola.
     */
    public static void main(String[] args) {
        new Main();
    }
}
