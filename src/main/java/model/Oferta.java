package model;

import java.time.LocalDate;

// Clase que representa una oferta hecha por un comprador sobre un inmueble.
public class Oferta {

    private String codigoOferta;
    private Comprador comprador;
    private Inmueble inmueble;
    private double valorOferta;
    private LocalDate fechaOferta;
    private EstadoOferta estado;

    public Oferta(String codigoOferta, Comprador comprador, Inmueble inmueble, double valorOferta) {
        this.codigoOferta = codigoOferta;
        this.comprador = comprador;
        this.inmueble = inmueble;
        this.valorOferta = valorOferta;
        this.fechaOferta = LocalDate.now();
        this.estado = EstadoOferta.PENDIENTE;
    }

    // Getters y setters
    public String getCodigoOferta() {
        return codigoOferta;
    }

    public void setCodigoOferta(String codigoOferta) {
        this.codigoOferta = codigoOferta;
    }

    public Comprador getComprador() {
        return comprador;
    }

    public void setComprador(Comprador comprador) {
        this.comprador = comprador;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public double getValorOferta() {
        return valorOferta;
    }

    public void setValorOferta(double valorOferta) {
        this.valorOferta = valorOferta;
    }

    public LocalDate getFechaOferta() {
        return fechaOferta;
    }

    public void setFechaOferta(LocalDate fechaOferta) {
        this.fechaOferta = fechaOferta;
    }

    public EstadoOferta getEstado() {
        return estado;
    }

    public void setEstado(EstadoOferta estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Codigo: " + codigoOferta
                + " | Comprador: " + comprador.getNombre()
                + " | Inmueble: " + inmueble.getCodigo()
                + " | Valor: $" + valorOferta
                + " | Fecha: " + fechaOferta
                + " | Estado: " + estado;
    }
}
