package interfaces;

import model.Comprador;
import model.Inmueble;
import model.Oferta;
import model.Publicacion;
import model.TipoOperacion;
import model.Transaccion;
import model.Vendedor;

// Interfaz que define las operaciones inmobiliarias principales
// que debe implementar el servicio de InmoSmart.
public interface OperacionesInmobiliarias {

    // Publica un inmueble en la plataforma y devuelve la publicacion creada
    Publicacion publicarInmueble(Vendedor vendedor, Inmueble inmueble, String descripcion);

    // Realiza una oferta sobre un inmueble disponible
    Oferta realizarOferta(Comprador comprador, Inmueble inmueble, double valorOferta);

    // Concreta la compra o arriendo de un inmueble (despues de aceptar la oferta)
    Transaccion comprarInmueble(Oferta oferta, TipoOperacion tipoOperacion);

    // Genera un reporte de texto con la actividad de la plataforma
    String generarReporte();
}
