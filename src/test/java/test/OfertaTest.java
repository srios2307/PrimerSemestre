package test;

import model.Comprador;
import model.EstadoInmueble;
import model.EstadoOferta;
import model.Inmueble;
import model.Oferta;
import model.TipoInmueble;
import model.TipoOperacion;
import model.Transaccion;
import model.Vendedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InmoSmartService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias para ofertas, aceptacion, rechazo y transacciones.
 */
public class OfertaTest {

    private InmoSmartService servicio;
    private Vendedor vendedor;
    private Comprador comprador;
    private Inmueble inmueble;

    @BeforeEach
    public void setUp() {
        servicio = new InmoSmartService();
        vendedor = servicio.registrarVendedor("Ana", "V001", "311", "ana@mail.com");
        comprador = servicio.registrarComprador("Juan", "C001", "300", "juan@mail.com");
        servicio.crearYPublicarInmueble("INM-001", TipoInmueble.CASA, "Calle 1",
                "Medellin", 120, 200000000, vendedor, "Casa bonita");
        inmueble = servicio.buscarInmueblePorCodigo("INM-001");
    }

    @Test
    public void testRealizarOfertaExitosa() {
        Oferta o = servicio.realizarOferta(comprador, inmueble, 180000000);
        assertNotNull(o);
        assertEquals(EstadoOferta.PENDIENTE, o.getEstado());
        assertEquals(180000000, o.getValorOferta(), 0.001);
        assertEquals(1, servicio.getOfertas().size());
    }

    @Test
    public void testRealizarOfertaSumaPuntosAlComprador() {
        int puntosAntes = comprador.getPuntos();
        servicio.realizarOferta(comprador, inmueble, 180000000);
        assertEquals(puntosAntes + 5, comprador.getPuntos());
    }

    @Test
    public void testRealizarOfertaConValorCeroLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.realizarOferta(comprador, inmueble, 0));
    }

    @Test
    public void testRealizarOfertaConValorNegativoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.realizarOferta(comprador, inmueble, -100));
    }

    @Test
    public void testRealizarOfertaConCompradorNoRegistradoLanzaExcepcion() {
        Comprador externo = new Comprador(99, "Fake", "X999", "000", "fake@mail.com");
        assertThrows(IllegalArgumentException.class,
                () -> servicio.realizarOferta(externo, inmueble, 100000000));
    }

    @Test
    public void testRealizarOfertaSobreInmuebleNoExistenteLanzaExcepcion() {
        Inmueble fake = new Inmueble("INM-FAKE", TipoInmueble.CASA, "X", "Y",
                100, 100000000, vendedor);
        assertThrows(IllegalArgumentException.class,
                () -> servicio.realizarOferta(comprador, fake, 100000000));
    }

    @Test
    public void testNoSePuedeOfertarSobreInmuebleVendido() {
        inmueble.setEstado(EstadoInmueble.VENDIDO);
        assertThrows(IllegalArgumentException.class,
                () -> servicio.realizarOferta(comprador, inmueble, 100000000));
    }

    @Test
    public void testAceptarOferta() {
        Oferta o = servicio.realizarOferta(comprador, inmueble, 180000000);
        servicio.aceptarOferta(o);
        assertEquals(EstadoOferta.ACEPTADA, o.getEstado());
        assertEquals(EstadoInmueble.RESERVADO, inmueble.getEstado());
    }

    @Test
    public void testAceptarOfertaRechazaLasOtras() {
        Comprador c2 = servicio.registrarComprador("Maria", "C002", "301", "maria@mail.com");
        Oferta o1 = servicio.realizarOferta(comprador, inmueble, 180000000);
        Oferta o2 = servicio.realizarOferta(c2, inmueble, 170000000);
        servicio.aceptarOferta(o1);
        assertEquals(EstadoOferta.ACEPTADA, o1.getEstado());
        assertEquals(EstadoOferta.RECHAZADA, o2.getEstado());
    }

    @Test
    public void testRechazarOferta() {
        Oferta o = servicio.realizarOferta(comprador, inmueble, 180000000);
        servicio.rechazarOferta(o);
        assertEquals(EstadoOferta.RECHAZADA, o.getEstado());
    }

    @Test
    public void testNoSePuedeAceptarOfertaYaProcesada() {
        Oferta o = servicio.realizarOferta(comprador, inmueble, 180000000);
        servicio.rechazarOferta(o);
        assertThrows(IllegalArgumentException.class,
                () -> servicio.aceptarOferta(o));
    }

    @Test
    public void testComprarInmuebleGeneraTransaccion() {
        Oferta o = servicio.realizarOferta(comprador, inmueble, 180000000);
        servicio.aceptarOferta(o);
        Transaccion tx = servicio.comprarInmueble(o, TipoOperacion.VENTA);
        assertNotNull(tx);
        assertEquals(EstadoInmueble.VENDIDO, inmueble.getEstado());
        assertEquals(TipoOperacion.VENTA, tx.getTipoOperacion());
        assertEquals(180000000, tx.getValorFinal(), 0.001);
    }

    @Test
    public void testArrendarInmuebleCambiaEstado() {
        Oferta o = servicio.realizarOferta(comprador, inmueble, 5000000);
        servicio.aceptarOferta(o);
        servicio.comprarInmueble(o, TipoOperacion.ARRIENDO);
        assertEquals(EstadoInmueble.ARRENDADO, inmueble.getEstado());
    }

    @Test
    public void testTransaccionSumaPuntosAComprador() {
        int puntosAntes = comprador.getPuntos();
        Oferta o = servicio.realizarOferta(comprador, inmueble, 180000000);
        servicio.aceptarOferta(o);
        servicio.comprarInmueble(o, TipoOperacion.VENTA);
        // 5 (oferta) + 50 (compra) + 100 (transaccion) = 155
        assertEquals(puntosAntes + 155, comprador.getPuntos());
    }

    @Test
    public void testTransaccionSumaPuntosAVendedor() {
        int puntosAntes = vendedor.getPuntos();
        Oferta o = servicio.realizarOferta(comprador, inmueble, 180000000);
        servicio.aceptarOferta(o);
        servicio.comprarInmueble(o, TipoOperacion.VENTA);
        // +100 por completar transaccion
        assertEquals(puntosAntes + 100, vendedor.getPuntos());
    }

    @Test
    public void testNoSePuedeComprarSinAceptarOferta() {
        Oferta o = servicio.realizarOferta(comprador, inmueble, 180000000);
        assertThrows(IllegalArgumentException.class,
                () -> servicio.comprarInmueble(o, TipoOperacion.VENTA));
    }

    @Test
    public void testToStringOfertaContieneCodigo() {
        Oferta o = servicio.realizarOferta(comprador, inmueble, 180000000);
        assertTrue(o.toString().contains(o.getCodigoOferta()));
    }
}
