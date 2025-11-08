package migp.modelo;

import migp.datos.datosJuego.DaoEquipable;
import migp.modelo.enums.TiposValiente;

import java.util.ArrayList;
import java.util.List;

public class Valiente {

    private TiposValiente tipoValiente;
    private int vida;
    private int fuerza;
    private int defensa;
    private int habilidad;
    private int velocidad;
    private String arma;
    private String escudo;
    private int nivel;

    private List<Monstruo> victorias;       //Guarda historial de monstruos derrotados

    public Valiente(TiposValiente tipoValiente, int vida, int fuerza, int defensa, int habilidad, int velocidad, String arma, String escudo, int nivel){
        this.tipoValiente = tipoValiente;
        this.vida=vida;
        this.fuerza=fuerza;
        this.defensa=defensa;
        this.habilidad=habilidad;
        this.velocidad=velocidad;
        this.arma=arma;
        this.escudo=escudo;
        this.nivel=nivel;
        this.victorias= new ArrayList<>();
    }

    public TiposValiente getTipoValiente() {
        return tipoValiente;
    }

    public int getVida() {
        return vida;
    }

    public int getFuerza() {
        return fuerza;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getHabilidad() {
        return habilidad;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public int getNivel() {
        return nivel;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public void setArma(String arma) {
        this.arma = arma;
    }

    public void setEscudo(String escudo) {
        this.escudo = escudo;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setHabilidad(int habilidad) {
        this.habilidad = habilidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    @Override
    public String toString() {
        return "Valiente{" +
                "tipo='" + tipoValiente + '\'' +
                ", vida=" + vida +
                ", fuerza=" + fuerza +
                ", defensa=" + defensa +
                ", habilidad=" + habilidad +
                ", velocidad=" + velocidad +
                ", arma= " +arma +
                ", escudo= " +escudo+
                ", nivel=" + nivel +
                '}';
    }

    public void atacar(Monstruo enemigo, int danoExtra){
        //Vida-(fuerza+poderArma)
        int ataque=fuerza+danoExtra;
        DaoEquipable dao = new DaoEquipable();
        Equipable arma = dao.buscarPorTipo(this.arma);
        if(arma!=null){
            ataque+=arma.getPoder();
        }
        System.out.printf("%s atacó a %s\n\n", tipoValiente, enemigo.getTipo());
        enemigo.recibirDaño(ataque);
    }

    public void recibirDano(int cantidad){
        if(this.vida-cantidad<0){
            this.vida=0;
        }else {
            this.vida-=cantidad;
        }
    }

    public void usarHabilidadEspecial(Monstruo enemigo){
        switch (tipoValiente){
            case GUERRERO -> {
                //Golpe fuerte quita 60% más sobre fuerza
                int danoExtra = Math.round(fuerza*0.6f);
                System.out.println(tipoValiente+" utilizó Golpe Mortal! (daño extra: "+danoExtra+")\n");
                atacar(enemigo, danoExtra);
            }
            case PALADIN ->{
                //Quita 60% y sube la defensa 40%
                int danoExtra = Math.round(-fuerza*0.40f);
                System.out.println(tipoValiente+" utilizó escudo sacro!\n");
                atacar(enemigo, danoExtra);
                this.defensa*=1.4f;
                System.out.println("Defensa de "+tipoValiente+" aumentada 40%\n");
            }
            case MAGO ->{
                //Quita 50% y reduce ataque enemigo 30%
                int danoExtra = Math.round(-fuerza*0.50f);
                System.out.println(tipoValiente+" utilizo bola de escarcha!\n");
                atacar(enemigo, danoExtra);
                int ataqueReducido = Math.round(enemigo.getFuerza()*0.7f);
                enemigo.setFuerza(ataqueReducido);
                System.out.println("Ataque "+enemigo+" reducido 30%\n");
            }
            case PICARO ->{
                //Quita 40% y aplica veneno que quita 6vida/turno
                int danoExtra = Math.round(-fuerza*0.6f);
                System.out.println(tipoValiente+" utilizó daga del pantano!");
                atacar(enemigo, danoExtra);
                enemigo.aplicarVeneno();
                System.out.println(tipoValiente+" envenedado! (6vida/turno)");
            }
        }
    }

    public void subirNivel(){
        this.nivel++;
        this.vida+=10;
        this.fuerza++;
        this.defensa++;
        this.habilidad++;
        this.velocidad++;
    }
}
