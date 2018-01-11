package aversitoca.aversitoca;

/**
 * Created by Marcos Muñoz on 10/01/2018.
 */

public class Decimo {

    private String numero, sorteo, premio, foto;
    private int id , comprobado, celebrado;

    public Decimo(String numero, String sorteo, String premio, String foto, int id, int comprobado, int celebrado) {
        this.numero = numero;
        this.sorteo = sorteo;
        this.premio = premio;
        this.foto = foto;
        this.id = id;
        this.comprobado = comprobado;
        this.celebrado = celebrado;
    }

    public Decimo() {

    }

    public String getNumero() {
        return this.numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSorteo() {
        return this.sorteo;
    }

    public void setSorteo(String sorteo) {
        this.sorteo = sorteo;
    }

    public String getPremio() {
        return this.premio;
    }

    public void setPremio(String premio) {
        this.premio = premio;
    }

    public String getFoto() {
        return this.foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComprobado() {
        return this.comprobado;
    }

    public void setComprobado(int comprobado) {
        this.comprobado = comprobado;
    }

    public int getCelebrado() {
        return this.celebrado;
    }

    public void setCelebrado(int celebrado) {
        this.celebrado = celebrado;
    }
}
