package model;

// Clase que representa a un comprador. Hereda de Usuario.
public class Comprador extends Usuario {

    public Comprador(int id, String nombre, String identificacion, String telefono, String correo) {
        super(id, nombre, identificacion, telefono, correo);
    }

    // El beneficio del comprador es un descuento del 2% segun sus puntos.
    // Esto simula un "cupon de descuento" acumulado por su actividad.
    @Override
    public double calcularBeneficio() {
        double poderCompra = this.puntos * 1000.0;
        double descuento = poderCompra * 0.02;
        return descuento;
    }

    @Override
    public String toString() {
        return "[COMPRADOR] " + super.toString()
                + " | Beneficio: $" + calcularBeneficio();
    }
}
