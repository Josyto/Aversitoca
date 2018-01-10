package aversitoca.aversitoca;

/**
 * Created by Marcos Muñoz on 10/01/2018.
 */

public class Decimo {

    private String numero;
    private String sorteo;
    private String premio;
    private int foto;
    private int id;

    public Decimo(String numero, String sorteo, String premio, int foto,int id) {
        this.numero = numero;
        this.sorteo = sorteo;
        this.premio = premio;
        this.foto = foto;
        this.id = id;
    }

    public Decimo() {

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
