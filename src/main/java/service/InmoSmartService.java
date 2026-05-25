package service;

import interfaces.OperacionesInmobiliarias;
import model.Comprador;
import model.EstadoInmueble;
import model.EstadoOferta;
import model.Inmueble;
import model.Oferta;
import model.Publicacion;
import model.TipoInmueble;
import model.TipoOperacion;
import model.Transaccion;
import model.Usuario;
import model.Vendedor;

import java.util.ArrayList;

/**
 * Clase principal de la logica de negocio de InmoSmart.
 * Aqui se manejan todas las listas de usuarios, inmuebles, ofertas y transacciones.
 * Implementa la interfaz OperacionesInmobiliarias.
 */
public class InmoSmartService implements OperacionesInmobiliarias {

    // Listas principales del sistema (uso de ArrayList)
    private ArrayList<Comprador> compradores;
    private ArrayList<Vendedor> vendedores;
    private ArrayList<Inmueble> inmuebles;
    private ArrayList<Publicacion> publicaciones;
    private ArrayList<Oferta> ofertas;
    private ArrayList<Transaccion> transacciones;
    private ArrayList<String> alertas;

    // Contadores para generar codigos automaticos
    private int contadorPublicacion;
    private int contadorOferta;
    private int contadorTransaccion;

    public InmoSmartService() {
        this.compradores = new ArrayList<>();
        this.vendedores = new ArrayList<>();
        this.inmuebles = new ArrayList<>();
        this.publicaciones = new ArrayList<>();
        this.ofertas = new ArrayList<>();
        this.transacciones = new ArrayList<>();
        this.alertas = new ArrayList<>();
        this.contadorPublicacion = 1;
        this.contadorOferta = 1;
        this.contadorTransaccion = 1;
    }

    // ===========================================================
    // REGISTRO DE USUARIOS
    // ===========================================================

    public Comprador registrarComprador(String nombre, String identificacion, String telefono, String correo) {
        // Validacion simple de campos
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La identificacion no puede estar vacia");
        }
        // Validar que no exista comprador con la misma identificacion
        for (Comprador c : compradores) {
            if (c.getIdentificacion().equals(identificacion)) {
                throw new IllegalArgumentException("Ya existe un comprador con esa identificacion");
            }
        }
        int id = compradores.size() + 1;
        Comprador nuevo = new Comprador(id, nombre, identificacion, telefono, correo);
        compradores.add(nuevo);
        agregarAlerta("Nuevo comprador registrado: " + nombre);
        return nuevo;
    }

    public Vendedor registrarVendedor(String nombre, String identificacion, String telefono, String correo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La identificacion no puede estar vacia");
        }
        for (Vendedor v : vendedores) {
            if (v.getIdentificacion().equals(identificacion)) {
                throw new IllegalArgumentException("Ya existe un vendedor con esa identificacion");
            }
        }
        int id = vendedores.size() + 1;
        Vendedor nuevo = new Vendedor(id, nombre, identificacion, telefono, correo);
        vendedores.add(nuevo);
        agregarAlerta("Nuevo vendedor registrado: " + nombre);
        return nuevo;
    }

    // ===========================================================
    // PUBLICACION DE INMUEBLES
    // ===========================================================

    @Override
    public Publicacion publicarInmueble(Vendedor vendedor, Inmueble inmueble, String descripcion) {
        // Validar que el vendedor este registrado
        if (vendedor == null || !vendedores.contains(vendedor)) {
            throw new IllegalArgumentException("El vendedor no esta registrado");
        }
        if (inmueble == null) {
            throw new IllegalArgumentException("El inmueble no puede ser nulo");
        }
        if (inmueble.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        // Validar que no exista un inmueble con el mismo codigo
        for (Inmueble i : inmuebles) {
            if (i.getCodigo().equals(inmueble.getCodigo())) {
                throw new IllegalArgumentException("Ya existe un inmueble con ese codigo");
            }
        }

        inmuebles.add(inmueble);
        String codigoPub = "PUB-" + contadorPublicacion;
        contadorPublicacion++;
        Publicacion publicacion = new Publicacion(codigoPub, descripcion, inmueble);
        publicaciones.add(publicacion);

        // Sumar puntos al vendedor por publicar
        vendedor.sumarPuntos(10);
        agregarAlerta("Nueva publicacion del inmueble: " + inmueble.getCodigo());
        return publicacion;
    }

    // Metodo auxiliar para crear inmueble y publicar en un solo paso (usado desde UI)
    public Publicacion crearYPublicarInmueble(String codigo, TipoInmueble tipo, String direccion,
                                              String ciudad, double area, double precio,
                                              Vendedor vendedor, String descripcion) {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (area <= 0) {
            throw new IllegalArgumentException("El area debe ser mayor a 0");
        }
        Inmueble inmueble = new Inmueble(codigo, tipo, direccion, ciudad, area, precio, vendedor);
        return publicarInmueble(vendedor, inmueble, descripcion);
    }

    // ===========================================================
    // BUSQUEDA DE INMUEBLES
    // ===========================================================

    // Busqueda por ciudad
    public ArrayList<Inmueble> buscarPorCiudad(String ciudad) {
        ArrayList<Inmueble> resultado = new ArrayList<>();
        for (Inmueble i : inmuebles) {
            if (i.getCiudad().equalsIgnoreCase(ciudad) && i.getEstado() == EstadoInmueble.DISPONIBLE) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    // Busqueda por tipo
    public ArrayList<Inmueble> buscarPorTipo(TipoInmueble tipo) {
        ArrayList<Inmueble> resultado = new ArrayList<>();
        for (Inmueble i : inmuebles) {
            if (i.getTipo() == tipo && i.getEstado() == EstadoInmueble.DISPONIBLE) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    // Busqueda por rango de precio
    public ArrayList<Inmueble> buscarPorPrecio(double precioMin, double precioMax) {
        ArrayList<Inmueble> resultado = new ArrayList<>();
        for (Inmueble i : inmuebles) {
            if (i.getPrecio() >= precioMin && i.getPrecio() <= precioMax
                    && i.getEstado() == EstadoInmueble.DISPONIBLE) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    // Busqueda por area minima
    public ArrayList<Inmueble> buscarPorAreaMinima(double areaMinima) {
        ArrayList<Inmueble> resultado = new ArrayList<>();
        for (Inmueble i : inmuebles) {
            if (i.getArea() >= areaMinima && i.getEstado() == EstadoInmueble.DISPONIBLE) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    // Busqueda combinada con filtros opcionales (todos los parametros pueden ser null o 0)
    public ArrayList<Inmueble> buscarConFiltros(String ciudad, TipoInmueble tipo,
                                                double precioMax, double areaMinima) {
        ArrayList<Inmueble> resultado = new ArrayList<>();
        for (Inmueble i : inmuebles) {
            if (i.getEstado() != EstadoInmueble.DISPONIBLE) {
                continue;
            }
            boolean coincide = true;
            if (ciudad != null && !ciudad.trim().isEmpty()
                    && !i.getCiudad().equalsIgnoreCase(ciudad)) {
                coincide = false;
            }
            if (tipo != null && i.getTipo() != tipo) {
                coincide = false;
            }
            if (precioMax > 0 && i.getPrecio() > precioMax) {
                coincide = false;
            }
            if (areaMinima > 0 && i.getArea() < areaMinima) {
                coincide = false;
            }
            if (coincide) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    // ===========================================================
    // OFERTAS
    // ===========================================================

    @Override
    public Oferta realizarOferta(Comprador comprador, Inmueble inmueble, double valorOferta) {
        // Validaciones obligatorias
        if (comprador == null || !compradores.contains(comprador)) {
            throw new IllegalArgumentException("El comprador no esta registrado");
        }
        if (inmueble == null || !inmuebles.contains(inmueble)) {
            throw new IllegalArgumentException("El inmueble no existe");
        }
        if (inmueble.getEstado() != EstadoInmueble.DISPONIBLE) {
            throw new IllegalArgumentException("El inmueble no esta disponible");
        }
        if (valorOferta <= 0) {
            throw new IllegalArgumentException("El valor de la oferta debe ser mayor a 0");
        }

        String codigoOf = "OF-" + contadorOferta;
        contadorOferta++;
        Oferta oferta = new Oferta(codigoOf, comprador, inmueble, valorOferta);
        ofertas.add(oferta);

        // Sumar puntos al comprador por realizar oferta
        comprador.sumarPuntos(5);
        agregarAlerta("Nueva oferta " + codigoOf + " sobre inmueble " + inmueble.getCodigo());
        return oferta;
    }

    // Acepta una oferta y rechaza las demas del mismo inmueble
    public void aceptarOferta(Oferta oferta) {
        if (oferta == null) {
            throw new IllegalArgumentException("La oferta no puede ser nula");
        }
        if (oferta.getEstado() != EstadoOferta.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden aceptar ofertas pendientes");
        }
        oferta.setEstado(EstadoOferta.ACEPTADA);
        // Reservamos el inmueble
        oferta.getInmueble().setEstado(EstadoInmueble.RESERVADO);

        // Rechazar el resto de ofertas pendientes del mismo inmueble
        for (Oferta o : ofertas) {
            if (o != oferta && o.getInmueble().equals(oferta.getInmueble())
                    && o.getEstado() == EstadoOferta.PENDIENTE) {
                o.setEstado(EstadoOferta.RECHAZADA);
            }
        }
        agregarAlerta("Oferta " + oferta.getCodigoOferta() + " aceptada");
    }

    public void rechazarOferta(Oferta oferta) {
        if (oferta == null) {
            throw new IllegalArgumentException("La oferta no puede ser nula");
        }
        if (oferta.getEstado() != EstadoOferta.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden rechazar ofertas pendientes");
        }
        oferta.setEstado(EstadoOferta.RECHAZADA);
        agregarAlerta("Oferta " + oferta.getCodigoOferta() + " rechazada");
    }

    // ===========================================================
    // COMPRA O ARRIENDO
    // ===========================================================

    @Override
    public Transaccion comprarInmueble(Oferta oferta, TipoOperacion tipoOperacion) {
        if (oferta == null) {
            throw new IllegalArgumentException("La oferta no puede ser nula");
        }
        if (oferta.getEstado() != EstadoOferta.ACEPTADA) {
            throw new IllegalArgumentException("La oferta debe estar aceptada para concretar la operacion");
        }
        if (tipoOperacion == null) {
            throw new IllegalArgumentException("Debe especificar el tipo de operacion");
        }

        Inmueble inmueble = oferta.getInmueble();
        Comprador comprador = oferta.getComprador();
        Vendedor vendedor = inmueble.getVendedor();

        String codigoTx = "TX-" + contadorTransaccion;
        contadorTransaccion++;
        Transaccion transaccion = new Transaccion(codigoTx, comprador, vendedor,
                inmueble, oferta.getValorOferta(), tipoOperacion);
        transacciones.add(transaccion);

        // Actualizar estado del inmueble segun el tipo de operacion
        if (tipoOperacion == TipoOperacion.VENTA) {
            inmueble.setEstado(EstadoInmueble.VENDIDO);
        } else {
            inmueble.setEstado(EstadoInmueble.ARRENDADO);
        }

        // Sumar puntos por compra y por completar transaccion
        comprador.sumarPuntos(50);
        comprador.sumarPuntos(100);
        vendedor.sumarPuntos(100);

        agregarAlerta("Transaccion " + codigoTx + " completada (" + tipoOperacion + ")");
        return transaccion;
    }

    // ===========================================================
    // REPORTES
    // ===========================================================

    @Override
    public String generarReporte() {
        StringBuilder sb = new StringBuilder();
        sb.append("===============================================\n");
        sb.append("        REPORTE GENERAL - InmoSmart\n");
        sb.append("===============================================\n\n");

        sb.append("USUARIOS REGISTRADOS\n");
        sb.append("-----------------------------------------------\n");
        sb.append("Compradores: ").append(compradores.size()).append("\n");
        sb.append("Vendedores: ").append(vendedores.size()).append("\n\n");

        sb.append("INMUEBLES\n");
        sb.append("-----------------------------------------------\n");
        sb.append("Total inmuebles: ").append(inmuebles.size()).append("\n");
        int disponibles = 0, vendidos = 0, arrendados = 0, reservados = 0;
        for (Inmueble i : inmuebles) {
            if (i.getEstado() == EstadoInmueble.DISPONIBLE) disponibles++;
            else if (i.getEstado() == EstadoInmueble.VENDIDO) vendidos++;
            else if (i.getEstado() == EstadoInmueble.ARRENDADO) arrendados++;
            else if (i.getEstado() == EstadoInmueble.RESERVADO) reservados++;
        }
        sb.append("Disponibles: ").append(disponibles).append("\n");
        sb.append("Vendidos: ").append(vendidos).append("\n");
        sb.append("Arrendados: ").append(arrendados).append("\n");
        sb.append("Reservados: ").append(reservados).append("\n\n");

        sb.append("OFERTAS\n");
        sb.append("-----------------------------------------------\n");
        int pendientes = 0, aceptadas = 0, rechazadas = 0;
        for (Oferta o : ofertas) {
            if (o.getEstado() == EstadoOferta.PENDIENTE) pendientes++;
            else if (o.getEstado() == EstadoOferta.ACEPTADA) aceptadas++;
            else if (o.getEstado() == EstadoOferta.RECHAZADA) rechazadas++;
        }
        sb.append("Total ofertas: ").append(ofertas.size()).append("\n");
        sb.append("Pendientes: ").append(pendientes).append("\n");
        sb.append("Aceptadas: ").append(aceptadas).append("\n");
        sb.append("Rechazadas: ").append(rechazadas).append("\n\n");

        sb.append("TRANSACCIONES COMPLETADAS\n");
        sb.append("-----------------------------------------------\n");
        double totalVentas = 0;
        for (Transaccion t : transacciones) {
            totalVentas += t.getValorFinal();
        }
        sb.append("Total transacciones: ").append(transacciones.size()).append("\n");
        sb.append("Valor total movido: $").append(totalVentas).append("\n\n");

        sb.append("TOP 3 USUARIOS POR PUNTOS\n");
        sb.append("-----------------------------------------------\n");
        ArrayList<Usuario> ranking = ordenarUsuariosPorPuntos();
        int limite = Math.min(3, ranking.size());
        for (int i = 0; i < limite; i++) {
            Usuario u = ranking.get(i);
            sb.append((i + 1)).append(". ").append(u.getNombre())
                    .append(" - Puntos: ").append(u.getPuntos())
                    .append(" - Rango: ").append(u.obtenerRango()).append("\n");
        }

        return sb.toString();
    }

    // ===========================================================
    // SISTEMA DE RECOMENDACIONES
    // ===========================================================

    // Recomienda inmuebles basados en lo que ha buscado o el rango del comprador.
    // Logica sencilla: recomendar inmuebles del mismo tipo que la ultima oferta hecha,
    // o sino los mas baratos disponibles.
    public ArrayList<Inmueble> recomendarInmuebles(Comprador comprador) {
        ArrayList<Inmueble> recomendados = new ArrayList<>();
        if (comprador == null) {
            return recomendados;
        }

        // Buscar la ultima oferta del comprador para conocer sus preferencias
        TipoInmueble tipoPreferido = null;
        for (int i = ofertas.size() - 1; i >= 0; i--) {
            Oferta o = ofertas.get(i);
            if (o.getComprador().equals(comprador)) {
                tipoPreferido = o.getInmueble().getTipo();
                break;
            }
        }

        if (tipoPreferido != null) {
            // Recomendar inmuebles del mismo tipo y disponibles
            for (Inmueble i : inmuebles) {
                if (i.getTipo() == tipoPreferido && i.getEstado() == EstadoInmueble.DISPONIBLE) {
                    recomendados.add(i);
                }
            }
        } else {
            // Si no hay historia, recomendar los 3 mas baratos disponibles
            ArrayList<Inmueble> ordenados = ordenarInmueblesPorPrecio(getInmueblesDisponibles());
            int limite = Math.min(3, ordenados.size());
            for (int i = 0; i < limite; i++) {
                recomendados.add(ordenados.get(i));
            }
        }
        return recomendados;
    }

    // ===========================================================
    // METODO BURBUJA - ORDENAMIENTO
    // ===========================================================

    /**
     * Ordena una lista de inmuebles por precio de menor a mayor usando metodo BURBUJA.
     * El metodo burbuja recorre la lista varias veces e intercambia elementos
     * adyacentes que esten en orden incorrecto, hasta que la lista quede ordenada.
     */
    public ArrayList<Inmueble> ordenarInmueblesPorPrecio(ArrayList<Inmueble> lista) {
        ArrayList<Inmueble> copia = new ArrayList<>(lista);
        int n = copia.size();
        // Recorremos n-1 veces el arreglo
        for (int i = 0; i < n - 1; i++) {
            // En cada pasada el elemento mayor "burbujea" al final
            for (int j = 0; j < n - 1 - i; j++) {
                if (copia.get(j).getPrecio() > copia.get(j + 1).getPrecio()) {
                    // Intercambiar posiciones j y j+1
                    Inmueble temp = copia.get(j);
                    copia.set(j, copia.get(j + 1));
                    copia.set(j + 1, temp);
                }
            }
        }
        return copia;
    }

    /**
     * Ordena una lista combinada de usuarios por puntos de mayor a menor (ranking).
     * Tambien usa metodo BURBUJA pero comparando de forma inversa.
     */
    public ArrayList<Usuario> ordenarUsuariosPorPuntos() {
        ArrayList<Usuario> lista = new ArrayList<>();
        lista.addAll(compradores);
        lista.addAll(vendedores);

        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (lista.get(j).getPuntos() < lista.get(j + 1).getPuntos()) {
                    Usuario temp = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, temp);
                }
            }
        }
        return lista;
    }

    // ===========================================================
    // ALERTAS AUTOMATICAS
    // ===========================================================

    private void agregarAlerta(String mensaje) {
        alertas.add("[ALERTA] " + mensaje);
    }

    public ArrayList<String> getAlertas() {
        return alertas;
    }

    public void limpiarAlertas() {
        alertas.clear();
    }

    // ===========================================================
    // GETTERS DE LAS LISTAS
    // ===========================================================

    public ArrayList<Comprador> getCompradores() {
        return compradores;
    }

    public ArrayList<Vendedor> getVendedores() {
        return vendedores;
    }

    public ArrayList<Inmueble> getInmuebles() {
        return inmuebles;
    }

    public ArrayList<Inmueble> getInmueblesDisponibles() {
        ArrayList<Inmueble> disponibles = new ArrayList<>();
        for (Inmueble i : inmuebles) {
            if (i.getEstado() == EstadoInmueble.DISPONIBLE) {
                disponibles.add(i);
            }
        }
        return disponibles;
    }

    public ArrayList<Publicacion> getPublicaciones() {
        return publicaciones;
    }

    public ArrayList<Oferta> getOfertas() {
        return ofertas;
    }

    public ArrayList<Oferta> getOfertasPendientes() {
        ArrayList<Oferta> pendientes = new ArrayList<>();
        for (Oferta o : ofertas) {
            if (o.getEstado() == EstadoOferta.PENDIENTE) {
                pendientes.add(o);
            }
        }
        return pendientes;
    }

    public ArrayList<Oferta> getOfertasAceptadas() {
        ArrayList<Oferta> aceptadas = new ArrayList<>();
        for (Oferta o : ofertas) {
            if (o.getEstado() == EstadoOferta.ACEPTADA) {
                aceptadas.add(o);
            }
        }
        return aceptadas;
    }

    public ArrayList<Transaccion> getTransacciones() {
        return transacciones;
    }

    // Busca un comprador por su identificacion
    public Comprador buscarCompradorPorIdentificacion(String identificacion) {
        for (Comprador c : compradores) {
            if (c.getIdentificacion().equals(identificacion)) {
                return c;
            }
        }
        return null;
    }

    // Busca un vendedor por su identificacion
    public Vendedor buscarVendedorPorIdentificacion(String identificacion) {
        for (Vendedor v : vendedores) {
            if (v.getIdentificacion().equals(identificacion)) {
                return v;
            }
        }
        return null;
    }

    // Busca un inmueble por su codigo
    public Inmueble buscarInmueblePorCodigo(String codigo) {
        for (Inmueble i : inmuebles) {
            if (i.getCodigo().equals(codigo)) {
                return i;
            }
        }
        return null;
    }

    // Busca una oferta por su codigo
    public Oferta buscarOfertaPorCodigo(String codigo) {
        for (Oferta o : ofertas) {
            if (o.getCodigoOferta().equals(codigo)) {
                return o;
            }
        }
        return null;
    }
}
