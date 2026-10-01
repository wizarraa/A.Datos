package EjercicioTinder;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {

    static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {

        // Pedir nombre del fichero
        System.out.print("Introduce el nombre del fichero: ");
        String nombreFichero = teclado.nextLine().trim();

        Path fichero = Paths.get("Src", "EjercicioTinder", nombreFichero);

        // Comprobar que el fichero existe
        if (!Files.exists(fichero)) {
            System.out.println("ERROR: El fichero no existe.");
            return;
        }

        // Comprobar que se puede leer
        if (!Files.isReadable(fichero)) {
            System.out.println("ERROR: No se puede leer el fichero.");
            return;
        }

        //Comprobar tamaño máximo
        try {
            if (Files.size(fichero) > 10000) {
                System.out.println(
                        "ERROR: El fichero supera los 10000 bytes."
                );
                return;
            }
        } catch (IOException e) {
            System.out.println("ERROR al comprobar el tamaño del fichero.");
            return;
        }

        // MENÚ
        int opcion;

        do {

            mostrarMenu();

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {

                case 1:
                    anadirUsuario(fichero);
                    break;

                case 2:
                    mostrarUsuarios(fichero);
                    break;

                case 3:
                    generarConcordancias(fichero);
                    break;

                case 4:
                    System.out.println("Programa finalizado.");
                    break;

                default:
                    System.out.println("ERROR: Opción no válida.");
            }

        } while (opcion != 4);

        teclado.close();
    }

    public static void mostrarMenu() {

        System.out.println();
        System.out.println("========== MENÚ PRINCIPAL ==========");
        System.out.println("1. Añadir usuario");
        System.out.println("2. Mostrar usuarios introducidos");
        System.out.println("3. Generar fichero de concordancias");
        System.out.println("4. Salir");
        System.out.println("====================================");
    }
    // LEER USUARIOS DEL FICHERO
    public static List<Usuario> leerUsuarios(Path fichero)
            throws IOException {

        List<Usuario> usuarios = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(fichero)) {

            String linea;

            while ((linea = br.readLine()) != null) {

                if (linea.trim().isEmpty()) { // Si la linea está vacía salta a la siguiente.Ignora lineas vacias de un texto
                    continue;
                }

                String[] partes = linea.trim().split("\\s+"); // divide en palabras una linea que tiene espacios entre palabras y la mete en un array

                // Código + al menos una afición
                if (partes.length < 2) {
                    throw new IOException(
                            "Hay una línea sin aficiones."
                    );
                }

                String codigo = partes[0]; // Guardamos codigo usuario

                // Comprobar formato de código
                if (!codigo.matches("U\\d+")) {
                    throw new IOException(
                            "Código de usuario incorrecto: " + codigo
                    );
                }

                // Comprobar usuarios duplicados
                for (Usuario usuario : usuarios) {

                    if (usuario.getCodigo().equals(codigo)) {

                        throw new IOException(
                                "Usuario duplicado: " + codigo
                        );
                    }
                }

                List<String> aficiones = new ArrayList<>();

                for (int i = 1; i < partes.length; i++) {

                    String aficion = partes[i];

                    // Solo letras mayúsculas
                    if (!aficion.matches("[A-Z]+")) {

                        throw new IOException(
                                "Afición incorrecta: " + aficion
                        );
                    }

                    aficiones.add(aficion);
                }

                // Comprobar si el texto ya existente está en orden alfabético
                for (int i = 1; i < aficiones.size(); i++) {

                    if (aficiones.get(i - 1)// obtiene el elemento antes de "i" para compararlo con "i"
                            .compareTo(aficiones.get(i)) > 0) {

                        throw new IOException(
                                "Las aficiones de " + codigo
                                        + " no están ordenadas."
                        );
                    }
                }
                // Convierto los datos del fichero a objetos para el programa
                usuarios.add(
                        new Usuario(codigo, aficiones)
                );
            }
        }

        return usuarios;
    }

    // AÑADIR USUARIO

    public static void anadirUsuario(Path fichero) {

        System.out.println();
        System.out.println("----- AÑADIR USUARIO -----");

        try {

            List<Usuario> usuarios = leerUsuarios(fichero);

            // Pedir código
            System.out.print("Código de usuario: ");
            String codigo = teclado.nextLine().trim();

            // Comprobar formato U + números
            if (!codigo.matches("U\\d+")) {

                System.out.println(
                        "ERROR: El código debe tener el formato U seguido de números."
                );

                return;
            }

            // Comprobar que no existe
            for (Usuario usuario : usuarios) {

                if (usuario.getCodigo().equals(codigo)) {

                    System.out.println(
                            "ERROR: El usuario "
                                    + codigo
                                    + " ya existe."
                    );

                    return;
                }
            }

            // Pedir aficiones
            System.out.print(
                    "Introduce las aficiones separadas por espacios: "
            );

            String linea = teclado.nextLine().trim();

            // Debe tener al menos una afición
            if (linea.isEmpty()) {

                System.out.println(
                        "ERROR: El usuario debe tener al menos una afición."
                );

                return;
            }

            String[] partes = linea.split("\\s+");

            List<String> aficiones = new ArrayList<>();

            for (String aficion : partes) {

                // Comprobar mayúsculas
                if (!aficion.matches("[A-Z]+")) {

                    System.out.println(
                            "ERROR: Las aficiones deben estar escritas en mayúsculas."
                    );

                    return;
                }

                aficiones.add(aficion);
            }

            // Ordenar alfabéticamente
            Collections.sort(aficiones);

            // Añadir al fichero
            try (BufferedWriter bw =
                         Files.newBufferedWriter(
                                 fichero,
                                 StandardOpenOption.APPEND)) {

                // Si el fichero referencia tiene lineas saltamos de línea, para no sobre escribir
                if (Files.size(fichero) > 0) {
                    bw.newLine();
                }

                bw.write(codigo);

                for (String aficion : aficiones) {

                    bw.write(" ");
                    bw.write(aficion);
                }
            }

            System.out.println(
                    "Usuario añadido correctamente."
            );

        } catch (IOException e) {

            System.out.println(
                    "ERROR: " + e.getMessage()
            );
        }
    }



    //MOSTRAR USUARIOS


    public static void mostrarUsuarios(Path fichero) {

        System.out.println();
        System.out.println("----- USUARIOS -----");

        try {

            List<Usuario> usuarios =
                    leerUsuarios(fichero);

            if (usuarios.isEmpty()) {

                System.out.println(
                        "No hay usuarios."
                );

                return;
            }

            for (Usuario usuario : usuarios) {

                System.out.println(usuario);
            }

        } catch (IOException e) {

            System.out.println(
                    "ERROR: " + e.getMessage()
            );
        }
    }

    // GENERAR CONCORDANCIAS

    public static void generarConcordancias(Path fichero) {

        System.out.println();
        System.out.println("----- GENERAR CONCORDANCIAS -----");

        int minimo;

        do {

            minimo = leerEntero(
                    "Número mínimo de aficiones comunes: "
            );

            if (minimo < 1) {

                System.out.println(
                        "ERROR: El número debe ser mayor o igual que 1."
                );
            }

        } while (minimo < 1);


        try {

            List<Usuario> usuarios =
                    leerUsuarios(fichero);

            List<Concordancia> concordancias =
                    new ArrayList<>();

            // Comparar todas las parejas
            for (int i = 0; i < usuarios.size(); i++) {

                for (int j = i + 1;
                     j < usuarios.size();
                     j++) {

                    Usuario usuario1 =
                            usuarios.get(i);

                    Usuario usuario2 =
                            usuarios.get(j);

                    List<String> comunes =
                            obtenerAficionesComunes(
                                    usuario1,
                                    usuario2
                            );

                    // Comprobar mínimo
                    if (comunes.size() >= minimo) {

                        concordancias.add(
                                new Concordancia(
                                        usuario1.getCodigo(),
                                        usuario2.getCodigo(),
                                        comunes
                                )
                        );
                    }
                }
            }

            // Si no hay parejas
            if (concordancias.isEmpty()) {

                System.out.println(
                        "No hay parejas con al menos "
                                + minimo
                                + " aficiones comunes."
                );

                return;
            }

            // Ordenar de mayor a menor número de aficiones comunes

            concordancias.sort(
                    Comparator.comparingInt(
                            (Concordancia c) ->
                                    c.getAficionesComunes().size()
                    ).reversed()
            );

            // Crear fichero
            escribirConcordancias(
                    concordancias
            );

            System.out.println(
                    "Fichero concordancias.txt creado correctamente."
            );

            System.out.println(
                    "Número de parejas: "
                            + concordancias.size()
            );

        } catch (IOException e) {

            System.out.println(
                    "ERROR: " + e.getMessage()
            );
        }
    }

    // OBTENER AFICIONES COMUNES

    public static List<String> obtenerAficionesComunes(
            Usuario usuario1,
            Usuario usuario2) {

        List<String> comunes =
                new ArrayList<>();

        for (String aficion :
                usuario1.getAficiones()) {

            if (usuario2.getAficiones()
                    .contains(aficion)) { // comprueba si tiene esa aficcion con contains

                comunes.add(aficion);
            }
        }

        Collections.sort(comunes);

        return comunes;
    }

    // ESCRIBIR CONCORDANCIAS

    public static void escribirConcordancias(
            List<Concordancia> concordancias)
            throws IOException {

        Path salida =
                Paths.get("concordancias.txt");

        try (BufferedWriter bw =
                     Files.newBufferedWriter(
                             salida,
                             StandardOpenOption.CREATE, // Crea el fichero si no existe
                             StandardOpenOption.TRUNCATE_EXISTING)) { // si existe borra su contenido

            for (Concordancia concordancia :
                    concordancias) {

                bw.write(
                        concordancia.getCodigo1() // escribimos el codigo del primer usuario
                );

                bw.write(" ");

                bw.write(
                        concordancia.getCodigo2() // lo mismo del segundo
                );

                for (String aficion :
                        concordancia.getAficionesComunes()) {

                    bw.write(" ");
                    bw.write(aficion);
                }

                bw.newLine();
            }
        }
    }

    // LEER NÚMERO ENTERO

    public static int leerEntero(
            String mensaje) {

        while (true) {

            System.out.print(mensaje);

            String texto =
                    teclado.nextLine().trim();

            try {

                return Integer.parseInt(texto);

            } catch (NumberFormatException e) {

                System.out.println(
                        "ERROR: Debes introducir un número entero."
                );
            }
        }
    }
}