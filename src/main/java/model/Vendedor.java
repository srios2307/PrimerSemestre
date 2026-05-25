package model;

// Clase que representa a un vendedor. Hereda de Usuario.
public class Vendedor extends Usuario {

    public Vendedor(int id, String nombre, String identificacion, String telefono, String correo) {
        super(id, nombre, identificacion, telefono, correo);
    }

    // El beneficio del vendedor es una comision del 3% sobre su actividad.
    // Esto representa la ganancia que acumula por sus ventas y publicaciones.
    @Override
    public double calcularBeneficio() {
        double valorActividad = this.puntos * 1500.0;
        double comision = valorActividad * 0.03;
        return comision;
    }

    @Override
    public String toString() {
        return "[VENDEDOR] " + super.toString()
                + " | Beneficio: $" + calcularBeneficio();
    }
}
