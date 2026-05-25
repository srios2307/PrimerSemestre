package model;

import java.time.LocalDate;

// Clase que representa una transaccion final (venta o arriendo).
public class Transaccion {

    private String codigoTransaccion;
    private Comprador comprador;
    private Vendedor vendedor;
    private Inmueble inmueble;
    private double valorFinal;
    private TipoOperacion tipoOperacion;
    private LocalDate fecha;

    public Transaccion(String codigoTransaccion, Comprador comprador, Vendedor vendedor,
                       Inmueble inmueble, double valorFinal, TipoOperacion tipoOperacion) {
        this.codigoTransaccion = codigoTransaccion;
        this.comprador = comprador;
        this.vendedor = vendedor;
        this.inmueble = inmueble;
        this.valorFinal = valorFinal;
        this.tipoOperacion = tipoOperacion;
        this.fecha = LocalDate.now();
    }

    // Getters y setters
    public String getCodigoTransaccion() {
        return codigoTransaccion;
    }

    public void setCodigoTransaccion(String codigoTransaccion) {
        this.codigoTransaccion = codigoTransaccion;
    }

    public Comprador getComprador() {
        return comprador;
    }

    public void setComprador(Comprador comprador) {
        this.comprador = comprador;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(TipoOperacion tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Codigo: " + codigoTransaccion
                + " | Comprador: " + comprador.getNombre()
                + " | Vendedor: " + vendedor.getNombre()
                + " | Inmueble: " + inmueble.getCodigo()
                + " | Valor: $" + valorFinal
                + " | Tipo: " + tipoOperacion
                + " | Fecha: " + fecha;
    }
}
