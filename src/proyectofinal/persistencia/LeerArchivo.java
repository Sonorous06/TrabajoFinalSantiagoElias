package proyectofinal.persistencia;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFileChooser;

public class LeerArchivo {

    public static int[] obtenerDatos(String categoriaElegida) {

        JFileChooser selector = new JFileChooser();
        //muestra la ventana para abrir 
        int estado = selector.showOpenDialog(null);

        //si le dieron dio clic en abrir
        if (estado == JFileChooser.APPROVE_OPTION) {

            try {
                //obtiene el archivo que se eligio
                File archivo = selector.getSelectedFile();

                //cuenta cuantos datso tiene la regla para crear el arreglo
                Scanner sc = new Scanner(archivo);

                //saltamos el encabezado
                if (sc.hasNextLine()) {
                    sc.nextLine();
                }

                ArrayList<Integer> listaTemporal = new ArrayList<>();

                while (sc.hasNext()) {
                    //lee letras
                    String categoria = sc.next();
                    //le numeros
                    int valor = sc.nextInt();
                    //solo se guarda si esta entre 0 y 60 y es la categoria correcta
                    if (valor >= 0 && valor <= 60 && categoria.equalsIgnoreCase(categoriaElegida)) {
                        listaTemporal.add(valor);
                    }
                }
                sc.close();

                //Convertimos la lista a un areglo de enteros para la clasa de estadisticos
                int[] resultado = new int[listaTemporal.size()];
                for (int i = 0; i < listaTemporal.size(); i++) {
                    resultado[i] = listaTemporal.get(i);
                }
                return resultado;
            } catch (Exception e) {
                System.out.println("Error al leer: " + e.getMessage());
            }

        }
        //si falla , devuelve nada
        return null;
    }

    // igual que obtenerDatos pero recibe el archivo ya elegido, sin abrir diálogo
    public static int[] obtenerDatosDe(File archivo, String categoriaElegida) {
        try {
            Scanner sc = new Scanner(archivo);

            if (sc.hasNextLine()) {
                sc.nextLine(); // saltamos encabezado
            }
            ArrayList<Integer> listaTemporal = new ArrayList<>();

            while (sc.hasNext()) {
                String categoria = sc.next();
                int valor = sc.nextInt();
                if (valor >= 0 && valor <= 60 && categoria.equalsIgnoreCase(categoriaElegida)) {
                    listaTemporal.add(valor);
                }
            }
            sc.close();

            int[] resultado = new int[listaTemporal.size()];
            for (int i = 0; i < listaTemporal.size(); i++) {
                resultado[i] = listaTemporal.get(i);
            }
            return resultado;

        } catch (Exception e) {
            System.out.println("Error al leer: " + e.getMessage());
        }
        return null;
    }

    //----------esta clase es nueva , sirve para poder leer las cate de manera dinamica
    //lee el archivo linea por linea , toma la primera columna , si no la habias visto , la guarda y regresa un arreglo de lo que encontro
    public static String[] leerCategorias(File archivo) {
        ArrayList<String> categorias = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(archivo);
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while (scanner.hasNext()) {
                String cat = scanner.next();
                scanner.nextInt();
                if (!categorias.contains(cat)) {
                    categorias.add(cat);
                }
            }
            scanner.close();
        } catch (Exception e) {
             System.out.println("Error: " + e.getMessage());
        }
        return categorias.toArray(new String[0]);
    }

}
