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
                if (sc.hasNextLine()) sc.nextLine();
                
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

}
