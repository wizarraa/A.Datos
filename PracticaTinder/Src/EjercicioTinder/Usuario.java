package EjercicioTinder;

import java.util.List;

public class Usuario {

    private String codigo;
    private List<String> aficiones;

    public Usuario(
            String codigo,
            List<String> aficiones) {

        this.codigo = codigo;
        this.aficiones = aficiones;
    }

    public String getCodigo() {
        return codigo;
    }

    public List<String> getAficiones() {
        return aficiones;
    }

    @Override
    public String toString() {

        return codigo
                + "{"
                + String.join( // Separa todos los elementos de la lista con "|"
                "|",
                aficiones
        )
                + "}";
    }
}