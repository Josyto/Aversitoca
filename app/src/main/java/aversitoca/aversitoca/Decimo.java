package aversitoca.aversitoca;

/**
 * Created by Marcos Muñoz on 10/01/2018.
 */

public class Decimo {

    private String numero;
    private String sorteo;
    private String premio;
    private int foto;

    public Decimo(String numero, String sorteo, String premio, int foto) {
        this.numero = numero;
        this.sorteo = sorteo;
        this.premio = premio;
        this.foto = foto;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSorteo() {
        return sorteo;
    }

    public void setSorteo(String sorteo) {
        this.sorteo = sorteo;
    }

    public String getPremio() {
        return premio;
    }

    public void setPremio(String premio) {
        this.premio = premio;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
