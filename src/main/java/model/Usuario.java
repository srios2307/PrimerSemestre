package model;

// Clase abstracta que representa a un usuario de InmoSmart.
// Los hijos son Comprador y Vendedor.
public abstract class Usuario {

    protected int id;
    protected String nombre;
    protected String identificacion;
    protected String telefono;
    protected String correo;
    protected int puntos;

    // Constructor
    public Usuario(int id, String nombre, String identificacion, String telefono, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.telefono = telefono;
        this.correo = correo;
        this.puntos = 0;
    }

    // Metodo abstracto: cada tipo de usuario calcula su beneficio diferente
    public abstract double calcularBeneficio();

    // Suma puntos al usuario por las acciones que realiza
    public void sumarPuntos(int cantidad) {
        if (cantidad > 0) {
            this.puntos += cantidad;
        }
    }

    // Obtiene el rango actual del usuario segun sus puntos
    public RangoUsuario obtenerRango() {
        RangoUsuario rangoActual = RangoUsuario.PRINCIPIANTE;
        // Recorremos todos los rangos para encontrar el mas alto que cumple
        for (RangoUsuario rango : RangoUsuario.values()) {
            if (this.puntos >= rango.getPuntosMinimos()) {
                rangoActual = rango;
            }
        }
        return rangoActual;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    @Override
    public String toString() {
        return "ID: " + id
                + " | Nombre: " + nombre
                + " | Identificacion: " + identificacion
                + " | Telefono: " + telefono
                + " | Correo: " + correo
                + " | Puntos: " + puntos
                + " | Rango: " + obtenerRango();
    }
}
