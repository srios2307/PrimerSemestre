package test;

import model.EstadoInmueble;
import model.Inmueble;
import model.Publicacion;
import model.TipoInmueble;
import model.Vendedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InmoSmartService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Pruebas unitarias para la publicacion y busqueda de inmuebles.
 */
public class InmuebleTest {

    private InmoSmartService servicio;
    private Vendedor vendedor;

    @BeforeEach
    public void setUp() {
        servicio = new InmoSmartService();
        vendedor = servicio.registrarVendedor("Ana", "V001", "311", "ana@mail.com");
    }

    @Test
    public void testPublicarInmuebleExitoso() {
        Publicacion p = servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA,
                "Calle 1", "Medellin", 120, 200000000, vendedor, "Casa bonita");
        assertNotNull(p);
        assertEquals("INM-001", p.getInmueble().getCodigo());
        assertEquals(1, servicio.getInmuebles().size());
        assertEquals(1, servicio.getPublicaciones().size());
    }

    @Test
    public void testInmuebleQuedaDisponible() {
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 200000000, vendedor, "X");
        Inmueble i = servicio.buscarInmueblePorCodigo("INM-001");
        assertEquals(EstadoInmueble.DISPONIBLE, i.getEstado());
    }

    @Test
    public void testPublicarSumaPuntosAlVendedor() {
        int puntosAntes = vendedor.getPuntos();
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 200000000, vendedor, "X");
        assertEquals(puntosAntes + 10, vendedor.getPuntos());
    }

    @Test
    public void testPublicarConPrecioCeroLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                        "Medellin", 120, 0, vendedor, "X"));
    }

    @Test
    public void testPublicarConAreaNegativaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                        "Medellin", -5, 200000000, vendedor, "X"));
    }

    @Test
    public void testPublicarConCodigoRepetidoLanzaExcepcion() {
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 200000000, vendedor, "X");
        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearYPublicarInmueble("INM-001", TipoInmueble.APARTAMENTO,
                        "Otra", "Bogota", 80, 150000000, vendedor, "Y"));
    }

    @Test
    public void testBuscarPorCiudad() {
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 200000000, vendedor, "X");
        servicio.crearYPublicarInmueble("INM-002", TipoInmueble.APARTAMENTO, "Calle 2",
                "Bogota", 80, 150000000, vendedor, "Y");
        ArrayList<Inmueble> resultado = servicio.buscarPorCiudad("Medellin");
        assertEquals(1, resultado.size());
    }

    @Test
    public void testBuscarPorTipo() {
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 200000000, vendedor, "X");
        servicio.crearYPublicarInmueble("INM-002", TipoInmueble.APARTAMENTO, "Calle 2",
                "Bogota", 80, 150000000, vendedor, "Y");
        ArrayList<Inmueble> resultado = servicio.buscarPorTipo(TipoInmueble.APARTAMENTO);
        assertEquals(1, resultado.size());
        assertEquals("INM-002", resultado.get(0).getCodigo());
    }

    @Test
    public void testBuscarPorRangoDePrecio() {
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 200000000, vendedor, "X");
        servicio.crearYPublicarInmueble("INM-002", TipoInmueble.APARTAMENTO, "Calle 2",
                "Bogota", 80, 150000000, vendedor, "Y");
        ArrayList<Inmueble> resultado = servicio.buscarPorPrecio(100000000, 180000000);
        assertEquals(1, resultado.size());
        assertEquals("INM-002", resultado.get(0).getCodigo());
    }

    @Test
    public void testBuscarPorAreaMinima() {
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 200000000, vendedor, "X");
        servicio.crearYPublicarInmueble("INM-002", TipoInmueble.APARTAMENTO, "Calle 2",
                "Bogota", 80, 150000000, vendedor, "Y");
        ArrayList<Inmueble> resultado = servicio.buscarPorAreaMinima(100);
        assertEquals(1, resultado.size());
        assertEquals("INM-001", resultado.get(0).getCodigo());
    }

    @Test
    public void testOrdenamientoBurbujaPorPrecio() {
        servicio.crearYPublicarInmueble("INM-A", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 300000000, vendedor, "X");
        servicio.crearYPublicarInmueble("INM-B", TipoInmueble.APARTAMENTO, "Calle 2",
                "Bogota", 80, 100000000, vendedor, "Y");
        servicio.crearYPublicarInmueble("INM-C", TipoInmueble.LOCAL, "Calle 3",
                "Cali", 90, 200000000, vendedor, "Z");
        ArrayList<Inmueble> ordenados = servicio.ordenarInmueblesPorPrecio(servicio.getInmuebles());
        // Verificar que esta ordenado de menor a mayor
        assertEquals("INM-B", ordenados.get(0).getCodigo());
        assertEquals("INM-C", ordenados.get(1).getCodigo());
        assertEquals("INM-A", ordenados.get(2).getCodigo());
        // Validar el orden general
        assertTrue(ordenados.get(0).getPrecio() <= ordenados.get(1).getPrecio());
        assertTrue(ordenados.get(1).getPrecio() <= ordenados.get(2).getPrecio());
    }

    @Test
    public void testBuscarConFiltrosCombinados() {
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 200000000, vendedor, "X");
        servicio.crearYPublicarInmueble("INM-002", TipoInmueble.CASA, "Calle 2",
                "Medellin", 90, 150000000, vendedor, "Y");
        ArrayList<Inmueble> resultado = servicio.buscarConFiltros(
                "Medellin", TipoInmueble.CASA, 0, 100);
        assertEquals(1, resultado.size());
        assertEquals("INM-001", resultado.get(0).getCodigo());
    }

    @Test
    public void testInmuebleVendidoNoApareceEnBusqueda() {
        Inmueble i = new Inmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 200000000, vendedor);
        servicio.publicarInmueble(vendedor, i, "X");
        i.setEstado(EstadoInmueble.VENDIDO);
        ArrayList<Inmueble> resultado = servicio.buscarPorCiudad("Medellin");
        assertFalse(resultado.contains(i));
    }
}
