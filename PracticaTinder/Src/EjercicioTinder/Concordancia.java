package EjercicioTinder;

import java.util.List;

public class Concordancia {

    private String codigo1;
    private String codigo2;
    private List<String> aficionesComunes;

    public Concordancia(
            String codigo1,
            String codigo2,
            List<String> aficionesComunes) {

        this.codigo1 = codigo1;
        this.codigo2 = codigo2;
        this.aficionesComunes =
                aficionesComunes;
    }

    public String getCodigo1() {
        return codigo1;
    }

    public String getCodigo2() {
        return codigo2;
    }

    public List<String>
    getAficionesComunes() {

        return aficionesComunes;
    }
}