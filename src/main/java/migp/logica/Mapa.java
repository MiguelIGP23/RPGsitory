package migp.logica;

import migp.modelo.Equipable;
import migp.modelo.Monstruo;
import migp.modelo.enums.TiposEquipable;
import migp.persistencia.DaoEquipable;
import migp.persistencia.DaoMonstruo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

//Genera y gestiona la cuadrícula de exploración del juego
public class Mapa {

    public static final int FILAS = 6;
    public static final int COLUMNAS = 6;
    public static final int MONSTRUOS_INICIALES = 8;
    public static final int ARMAS_INICIALES = 2;
    public static final int ESCUDOS_INICIALES = 2;
    public static final int CONSUMIBLES_INICIALES = 3;
    public static final int VIDA_COMPILADOR_OSCURO = 150;
    public static final int RANGO_NIVEL_MONSTRUO = 1;
    public static final int EXTRA_NIVEL_RARO = 2;
    public static final double PROBABILIDAD_ENCUENTRO_DURO = 0.15;
    public static final double PROBABILIDAD_OBJETO_SUPERIOR = 0.18;

    private final Casilla[][] casillas;
    private final DaoMonstruo daoMonstruo;
    private final DaoEquipable daoEquipable;
    private final Random random;
    private int monstruosGenerados;

    public Mapa(DaoMonstruo daoMonstruo, DaoEquipable daoEquipable, int nivelValienteInicial) {
        this.casillas = new Casilla[FILAS][COLUMNAS];
        this.daoMonstruo = daoMonstruo;
        this.daoEquipable = daoEquipable;
        this.random = new Random();
        this.monstruosGenerados = 0;

        inicializarCasillas();
        generarContenido();
        revelarEntorno(1, 1);
        getCasilla(1, 1).marcarVisitada();
    }

    public Casilla getCasilla(int fila, int columna) {
        return casillas[fila - 1][columna - 1];
    }

    public int getMonstruosGenerados() {
        return monstruosGenerados;
    }

    public int getFilas() {
        return FILAS;
    }

    public int getColumnas() {
        return COLUMNAS;
    }

    //Genera el contenido real de la casilla con el nivel actual del valiente
    public void prepararCasilla(Casilla casilla, int nivelValiente) {
        if (casilla.getJefeFinal() && casilla.getMonstruo() == null) {
            casilla.setMonstruo(crearCompiladorOscuro());
            return;
        }
        if (casilla.getMonstruoPendiente() && casilla.getMonstruo() == null) {
            casilla.setMonstruo(generarMonstruoParaNivel(nivelValiente));
        }
        if (casilla.getTipoObjetoPendiente() != null && casilla.getObjeto() == null) {
            Equipable objeto = generarEquipableParaNivel(casilla.getTipoObjetoPendiente(), nivelValiente);
            if (objeto != null) {
                casilla.setObjeto(objeto);
            }
        }
    }

    //Comprueba si una coordenada está dentro del mapa
    public boolean dentroLimites(int fila, int columna) {
        return fila >= 1 && fila <= FILAS && columna >= 1 && columna <= COLUMNAS;
    }

    //Revela la casilla actual y sus adyacentes ortogonales
    public void revelarEntorno(int fila, int columna) {
        revelarSiExiste(fila, columna);
        revelarSiExiste(fila - 1, columna);
        revelarSiExiste(fila + 1, columna);
        revelarSiExiste(fila, columna - 1);
        revelarSiExiste(fila, columna + 1);
    }

    //Genera la representación del mapa con el contenido conocido por el jugador
    public String representar(int filaJugador, int columnaJugador) {
        StringBuilder mapa = new StringBuilder();
        mapa.append(Consola.color(Consola.ANSI_CYAN, "=== MAPA ===")).append("\n");
        mapa.append("\n");
        mapa.append("     ");
        for (int columna = 1; columna <= COLUMNAS; columna++) {
            mapa.append(String.format("  %-3d ", columna));
        }
        mapa.append("\n");

        for (int fila = 1; fila <= FILAS; fila++) {
            mapa.append(String.format("%-3d ", fila));
            for (int columna = 1; columna <= COLUMNAS; columna++) {
                mapa.append(formatearCasilla(fila, columna, filaJugador, columnaJugador));
            }
            mapa.append("\n");
        }

        mapa.append("\n");
        mapa.append(Consola.color(Consola.ANSI_VERDE, "J")).append(" Jugador   ");
        mapa.append(Consola.color(Consola.ANSI_ROJO, "M")).append(" Monstruo   ");
        mapa.append(Consola.color(Consola.ANSI_AZUL, "O")).append(" Equipo   ");
        mapa.append(Consola.color(Consola.ANSI_VERDE, "C")).append(" Curativo   ");
        mapa.append(Consola.color(Consola.ANSI_MAGENTA, "B")).append(" Jefe final   ");
        mapa.append(Consola.color(Consola.ANSI_CYAN, ".")).append(" Vacía   ");
        mapa.append(Consola.color(Consola.ANSI_AMARILLO, "?")).append(" Oculta");
        return mapa.toString();
    }

    //Cuenta solo los monstruos normales que siguen vivos en el mapa
    public int contarMonstruosRestantes() {
        int restantes = 0;
        for (int fila = 1; fila <= FILAS; fila++) {
            for (int columna = 1; columna <= COLUMNAS; columna++) {
                Casilla casilla = getCasilla(fila, columna);
                if (casilla.tieneMonstruo() && !casilla.getJefeFinal()) {
                    restantes++;
                }
            }
        }
        return restantes;
    }

    private void inicializarCasillas() {
        for (int fila = 0; fila < FILAS; fila++) {
            for (int columna = 0; columna < COLUMNAS; columna++) {
                casillas[fila][columna] = new Casilla();
            }
        }
    }

        //Reserva inicio y final, luego reparte monstruos y objetos por huecos libres
    private void generarContenido() {
        colocarMonstruosAleatorios();
        colocarObjetosAleatorios();
        colocarCompiladorOscuro();
    }

    private void colocarMonstruosAleatorios() {
        for (int i = 0; i < MONSTRUOS_INICIALES; i++) {
            int[] posicionLibre = buscarPosicionLibre();
            getCasilla(posicionLibre[0], posicionLibre[1]).reservarMonstruo();
            monstruosGenerados++;
        }
    }

    private void colocarObjetosAleatorios() {
        colocarTipoEquipable(TiposEquipable.ARMA, ARMAS_INICIALES);
        colocarTipoEquipable(TiposEquipable.ESCUDO, ESCUDOS_INICIALES);
        colocarTipoEquipable(TiposEquipable.CONSUMIBLE, CONSUMIBLES_INICIALES);
    }

    private void colocarCompiladorOscuro() {
        Casilla casillaFinal = getCasilla(FILAS, COLUMNAS);
        casillaFinal.reservarMonstruo();
        casillaFinal.marcarComoJefeFinal();
    }

    private Monstruo crearCompiladorOscuro() {
        int poderBase = Math.max(6, monstruosGenerados * 2);
        return new Monstruo(
                "Compilador Oscuro",
                VIDA_COMPILADOR_OSCURO,
                poderBase,
                poderBase,
                poderBase,
                poderBase,
                monstruosGenerados + 1
        );
    }

    private void revelarSiExiste(int fila, int columna) {
        if (dentroLimites(fila, columna)) {
            getCasilla(fila, columna).revelar();
        }
    }

    private int[] buscarPosicionLibre() {
        while (true) {
            int fila = random.nextInt(FILAS) + 1;
            int columna = random.nextInt(COLUMNAS) + 1;
            Casilla casilla = getCasilla(fila, columna);

            //La salida y la meta se reservan para no ensuciar el flujo principal
            boolean inicio = fila == 1 && columna == 1;
            boolean finalMapa = fila == FILAS && columna == COLUMNAS;
            if (inicio || finalMapa || !casilla.estaVacia()) {
                continue;
            }
            return new int[]{fila, columna};
        }
    }

    //Selecciona monstruos cercanos al nivel del valiente y reserva unos pocos encuentros duros
    private Monstruo generarMonstruoParaNivel(int nivelValiente) {
        List<Monstruo> plantillas = daoMonstruo.listar().stream()
                .filter(Objects::nonNull)
                .toList();

        int nivelObjetivoMin = Math.max(1, nivelValiente - RANGO_NIVEL_MONSTRUO);
        int nivelObjetivoMax = nivelValiente + RANGO_NIVEL_MONSTRUO;
        if (random.nextDouble() < PROBABILIDAD_ENCUENTRO_DURO) {
            nivelObjetivoMax = nivelValiente + EXTRA_NIVEL_RARO;
        }
        final int nivelMinimo = nivelObjetivoMin;
        final int nivelMaximo = nivelObjetivoMax;

        List<Monstruo> candidatos = plantillas.stream()
                .filter(monstruo -> monstruo.getNivel() >= nivelMinimo && monstruo.getNivel() <= nivelMaximo)
                .toList();

        if (candidatos.isEmpty()) {
            Monstruo masCercano = plantillas.stream()
                    .min(Comparator.comparingInt(monstruo -> Math.abs(monstruo.getNivel() - nivelValiente)))
                    .orElseThrow();
            return daoMonstruo.buscarPorTipo(masCercano.getTipoMonstruo().name());
        }

        Monstruo plantilla = candidatos.get(random.nextInt(candidatos.size()));
        return daoMonstruo.buscarPorTipo(plantilla.getTipoMonstruo().name());
    }

    //Cada tipo de objeto se genera con su propia lógica de tier para no mezclar progresiones
    private void colocarTipoEquipable(TiposEquipable tipo, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            int[] posicionLibre = buscarPosicionLibre();
            getCasilla(posicionLibre[0], posicionLibre[1]).reservarObjeto(tipo);
        }
    }

    private Equipable generarEquipableParaNivel(TiposEquipable tipo, int nivelValiente) {
        List<Equipable> equipables = daoEquipable.listar().stream()
                .filter(Objects::nonNull)
                .filter(equipable -> equipable.getTipo() == tipo)
                .toList();

        if (equipables.isEmpty()) {
            return null;
        }

        if (tipo == TiposEquipable.CONSUMIBLE) {
            return elegirConsumiblePorNivel(equipables, nivelValiente);
        }
        return elegirEquipoPorNivel(equipables, nivelValiente);
    }

    private Equipable elegirEquipoPorNivel(List<Equipable> equipables, int nivelValiente) {
        int poderObjetivo = Math.min(8, nivelValiente + 2);
        int poderMinimo = Math.max(2, poderObjetivo - 1);
        int poderMaximo = Math.min(8, poderObjetivo + 1);

        if (random.nextDouble() < PROBABILIDAD_OBJETO_SUPERIOR) {
            poderMinimo = Math.min(8, poderObjetivo + 1);
            poderMaximo = Math.min(8, poderObjetivo + 2);
        }

        final int minimo = poderMinimo;
        final int maximo = poderMaximo;
        List<Equipable> candidatos = equipables.stream()
                .filter(equipable -> equipable.getPoder() >= minimo && equipable.getPoder() <= maximo)
                .toList();

        if (candidatos.isEmpty()) {
            return equipables.stream()
                    .min(Comparator.comparingInt(equipable -> Math.abs(equipable.getPoder() - poderObjetivo)))
                    .orElse(null);
        }
        return candidatos.get(random.nextInt(candidatos.size()));
    }

    private Equipable elegirConsumiblePorNivel(List<Equipable> equipables, int nivelValiente) {
        int poderObjetivo = poderConsumibleObjetivo(nivelValiente);
        if (random.nextDouble() < PROBABILIDAD_OBJETO_SUPERIOR) {
            poderObjetivo = poderConsumibleSuperior(poderObjetivo);
        }

        final int poderFinal = poderObjetivo;
        return equipables.stream()
                .filter(equipable -> equipable.getPoder() == poderFinal)
                .findFirst()
                .orElseGet(() -> equipables.stream()
                        .min(Comparator.comparingInt(equipable -> Math.abs(equipable.getPoder() - poderFinal)))
                        .orElse(null));
    }

    private int poderConsumibleObjetivo(int nivelValiente) {
        if (nivelValiente <= 2) {
            return 20;
        }
        if (nivelValiente <= 4) {
            return 45;
        }
        return 80;
    }

    private int poderConsumibleSuperior(int poderBase) {
        if (poderBase == 20) {
            return 45;
        }
        return 80;
    }

    private String formatearCasilla(int fila, int columna, int filaJugador, int columnaJugador) {
        return "[ " + simboloCasilla(fila, columna, filaJugador, columnaJugador) + " ] ";
    }

    private String simboloCasilla(int fila, int columna, int filaJugador, int columnaJugador) {
        if (fila == filaJugador && columna == columnaJugador) {
            return Consola.color(Consola.ANSI_VERDE, "J");
        }

        Casilla casilla = getCasilla(fila, columna);
        if (!casilla.getRevelada()) {
            return Consola.color(Consola.ANSI_AMARILLO, "?");
        }
        if (casilla.getJefeFinal() && casilla.tieneMonstruo()) {
            return Consola.color(Consola.ANSI_MAGENTA, "B");
        }
        if (casilla.tieneMonstruo()) {
            return Consola.color(Consola.ANSI_ROJO, "M");
        }
        if (casilla.tieneObjeto()) {
            TiposEquipable tipoObjeto = casilla.getObjeto() != null ? casilla.getObjeto().getTipo() : casilla.getTipoObjetoPendiente();
            if (tipoObjeto == TiposEquipable.CONSUMIBLE) {
                return Consola.color(Consola.ANSI_VERDE, "C");
            }
            return Consola.color(Consola.ANSI_AZUL, "O");
        }
        return Consola.color(Consola.ANSI_CYAN, ".");
    }
}
