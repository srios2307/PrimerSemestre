package model;

// Clase que representa un inmueble publicado en la plataforma.
public class Inmueble {

    private String codigo;
    private TipoInmueble tipo;
    private String direccion;
    private String ciudad;
    private double area;
    private double precio;
    private EstadoInmueble estado;
    private Vendedor vendedor;

    public Inmueble(String codigo, TipoInmueble tipo, String direccion, String ciudad,
                    double area, double precio, Vendedor vendedor) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.area = area;
        this.precio = precio;
        this.estado = EstadoInmueble.DISPONIBLE; // por defecto disponible
        this.vendedor = vendedor;
    }

    // Getters y setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TipoInmueble getTipo() {
        return tipo;
    }

    public void setTipo(TipoInmueble tipo) {
        this.tipo = tipo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public EstadoInmueble getEstado() {
        return estado;
    }

    public void setEstado(EstadoInmueble estado) {
        this.estado = estado;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    @Override
    public String toString() {
        return "Codigo: " + codigo
                + " | Tipo: " + tipo
                + " | Direccion: " + direccion
                + " | Ciudad: " + ciudad
                + " | Area: " + area + " m2"
                + " | Precio: $" + precio
                + " | Estado: " + estado
                + " | Vendedor: " + (vendedor != null ? vendedor.getNombre() : "Sin asignar");
    }
}
