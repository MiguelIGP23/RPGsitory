package migp.logica;

//Utilidad simple para colorear la salida de consola
public final class Consola {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_ROJO = "\u001B[31m";
    public static final String ANSI_VERDE = "\u001B[32m";
    public static final String ANSI_AMARILLO = "\u001B[33m";
    public static final String ANSI_AZUL = "\u001B[34m";
    public static final String ANSI_MAGENTA = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    private Consola() {}

    public static String color(String ansiColor, String texto) {
        return ansiColor + texto + ANSI_RESET;
    }
}
