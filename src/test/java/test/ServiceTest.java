package test;

import model.Comprador;
import model.Inmueble;
import model.Oferta;
import model.TipoInmueble;
import model.TipoOperacion;
import model.Usuario;
import model.Vendedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InmoSmartService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias para el servicio principal:
 * generacion de reportes, recomendaciones, alertas y ordenamiento de usuarios.
 */
public class ServiceTest {

    private InmoSmartService servicio;
    private Vendedor vendedor;
    private Comprador comprador;

    @BeforeEach
    public void setUp() {
        servicio = new InmoSmartService();
        vendedor = servicio.registrarVendedor("Ana", "V001", "311", "ana@mail.com");
        comprador = servicio.registrarComprador("Juan", "C001", "300", "juan@mail.com");
    }

    @Test
    public void testGenerarReporteNoEsNulo() {
        String reporte = servicio.generarReporte();
        assertNotNull(reporte);
        assertFalse(reporte.isEmpty());
    }

    @Test
    public void testReporteIncluyeUsuariosRegistrados() {
        String reporte = servicio.generarReporte();
        assertTrue(reporte.contains("Compradores: 1"));
        assertTrue(reporte.contains("Vendedores: 1"));
    }

    @Test
    public void testReporteIncluyeInmueblesPublicados() {
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 200000000, vendedor, "X");
        String reporte = servicio.generarReporte();
        assertTrue(reporte.contains("Total inmuebles: 1"));
    }

    @Test
    public void testReporteIncluyeRanking() {
        String reporte = servicio.generarReporte();
        assertTrue(reporte.contains("TOP 3 USUARIOS"));
    }

    @Test
    public void testOrdenarUsuariosPorPuntosMayorAMenor() {
        comprador.sumarPuntos(200);
        vendedor.sumarPuntos(500);
        ArrayList<Usuario> ranking = servicio.ordenarUsuariosPorPuntos();
        assertEquals(vendedor.getNombre(), ranking.get(0).getNombre());
        assertEquals(comprador.getNombre(), ranking.get(1).getNombre());
        // Validar orden general
        assertTrue(ranking.get(0).getPuntos() >= ranking.get(1).getPuntos());
    }

    @Test
    public void testRecomendacionesParaCompradorSinHistoria() {
        // Sin ofertas previas, debe recomendar los mas baratos
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 300000000, vendedor, "X");
        servicio.crearYPublicarInmueble("INM-002", TipoInmueble.APARTAMENTO, "Calle 2",
                "Bogota", 80, 100000000, vendedor, "Y");
        ArrayList<Inmueble> recomendados = servicio.recomendarInmuebles(comprador);
        assertFalse(recomendados.isEmpty());
        // El primero debe ser el mas barato (INM-002)
        assertEquals("INM-002", recomendados.get(0).getCodigo());
    }

    @Test
    public void testRecomendacionesBasadasEnHistoria() {
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 300000000, vendedor, "X");
        servicio.crearYPublicarInmueble("INM-002", TipoInmueble.APARTAMENTO, "Calle 2",
                "Bogota", 80, 100000000, vendedor, "Y");
        servicio.crearYPublicarInmueble("INM-003", TipoInmueble.APARTAMENTO, "Calle 3",
                "Cali", 90, 120000000, vendedor, "Z");

        // El comprador hace una oferta sobre un APARTAMENTO
        Inmueble inm = servicio.buscarInmueblePorCodigo("INM-002");
        servicio.realizarOferta(comprador, inm, 90000000);

        ArrayList<Inmueble> recomendados = servicio.recomendarInmuebles(comprador);
        // Deben ser todos del tipo APARTAMENTO
        for (Inmueble i : recomendados) {
            assertEquals(TipoInmueble.APARTAMENTO, i.getTipo());
        }
    }

    @Test
    public void testAlertasSeGeneranAlRegistrar() {
        // Despues del setUp ya hay 2 alertas (1 vendedor + 1 comprador)
        assertEquals(2, servicio.getAlertas().size());
    }

    @Test
    public void testAlertasAlPublicarYOfertar() {
        int alertasAntes = servicio.getAlertas().size();
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 200000000, vendedor, "X");
        Inmueble inm = servicio.buscarInmueblePorCodigo("INM-001");
        servicio.realizarOferta(comprador, inm, 180000000);
        // Se agregaron 2 alertas mas: publicacion + oferta
        assertEquals(alertasAntes + 2, servicio.getAlertas().size());
    }

    @Test
    public void testLimpiarAlertas() {
        servicio.limpiarAlertas();
        assertTrue(servicio.getAlertas().isEmpty());
    }

    @Test
    public void testBuscarCompradorPorIdentificacion() {
        Comprador c = servicio.buscarCompradorPorIdentificacion("C001");
        assertNotNull(c);
        assertEquals("Juan", c.getNombre());
    }

    @Test
    public void testBuscarCompradorNoExistenteRetornaNull() {
        Comprador c = servicio.buscarCompradorPorIdentificacion("INEXISTENTE");
        assertNull(c);
    }

    @Test
    public void testGetInmueblesDisponiblesSoloLosDisponibles() {
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 200000000, vendedor, "X");
        Inmueble inm = servicio.buscarInmueblePorCodigo("INM-001");
        Oferta o = servicio.realizarOferta(comprador, inm, 180000000);
        servicio.aceptarOferta(o);
        servicio.comprarInmueble(o, TipoOperacion.VENTA);

        // Ya esta vendido, no debe aparecer como disponible
        assertEquals(0, servicio.getInmueblesDisponibles().size());
    }

    @Test
    public void testFlujoCompletoIntegrado() {
        // Publicar inmueble
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 200000000, vendedor, "X");
        // Crear oferta
        Inmueble inm = servicio.buscarInmueblePorCodigo("INM-001");
        Oferta o = servicio.realizarOferta(comprador, inm, 180000000);
        // Aceptar oferta
        servicio.aceptarOferta(o);
        // Concretar transaccion
        servicio.comprarInmueble(o, TipoOperacion.VENTA);

        // Validaciones finales
        assertEquals(1, servicio.getTransacciones().size());
        assertEquals(10 + 100, vendedor.getPuntos());  // publicar + transaccion
        assertEquals(5 + 50 + 100, comprador.getPuntos()); // oferta + compra + transaccion
    }
}
