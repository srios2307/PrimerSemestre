package test;

import model.Comprador;
import model.RangoUsuario;
import model.Vendedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InmoSmartService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias para el registro y comportamiento de los usuarios.
 */
public class UsuarioTest {

    private InmoSmartService servicio;

    @BeforeEach
    public void setUp() {
        servicio = new InmoSmartService();
    }

    @Test
    public void testRegistrarCompradorExitoso() {
        Comprador c = servicio.registrarComprador("Juan Perez", "C001", "300", "juan@mail.com");
        assertNotNull(c);
        assertEquals("Juan Perez", c.getNombre());
        assertEquals(0, c.getPuntos());
        assertEquals(1, servicio.getCompradores().size());
    }

    @Test
    public void testRegistrarVendedorExitoso() {
        Vendedor v = servicio.registrarVendedor("Ana Lopez", "V001", "311", "ana@mail.com");
        assertNotNull(v);
        assertEquals("Ana Lopez", v.getNombre());
        assertEquals(1, servicio.getVendedores().size());
    }

    @Test
    public void testRegistrarCompradorConNombreVacioLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarComprador("", "C001", "300", "x@mail.com"));
    }

    @Test
    public void testRegistrarCompradorConIdentificacionRepetida() {
        servicio.registrarComprador("Juan Perez", "C001", "300", "juan@mail.com");
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarComprador("Otro", "C001", "301", "otro@mail.com"));
    }

    @Test
    public void testSumarPuntos() {
        Comprador c = servicio.registrarComprador("Juan", "C001", "300", "x@mail.com");
        c.sumarPuntos(50);
        c.sumarPuntos(25);
        assertEquals(75, c.getPuntos());
    }

    @Test
    public void testRangoPrincipiantePorDefecto() {
        Comprador c = servicio.registrarComprador("Juan", "C001", "300", "x@mail.com");
        assertEquals(RangoUsuario.PRINCIPIANTE, c.obtenerRango());
    }

    @Test
    public void testRangoInversionistaConSuficientesPuntos() {
        Comprador c = servicio.registrarComprador("Juan", "C001", "300", "x@mail.com");
        c.sumarPuntos(150);
        assertEquals(RangoUsuario.INVERSIONISTA, c.obtenerRango());
    }

    @Test
    public void testRangoMagnateInmobiliario() {
        Vendedor v = servicio.registrarVendedor("Ana", "V001", "311", "ana@mail.com");
        v.sumarPuntos(700);
        assertEquals(RangoUsuario.MAGNATE_INMOBILIARIO, v.obtenerRango());
    }

    @Test
    public void testCalcularBeneficioComprador() {
        Comprador c = servicio.registrarComprador("Juan", "C001", "300", "x@mail.com");
        c.sumarPuntos(100);
        // 100 * 1000 * 0.02 = 2000
        assertEquals(2000.0, c.calcularBeneficio(), 0.001);
    }

    @Test
    public void testCalcularBeneficioVendedor() {
        Vendedor v = servicio.registrarVendedor("Ana", "V001", "311", "ana@mail.com");
        v.sumarPuntos(100);
        // 100 * 1500 * 0.03 = 4500
        assertEquals(4500.0, v.calcularBeneficio(), 0.001);
    }

    @Test
    public void testToStringContieneNombre() {
        Comprador c = servicio.registrarComprador("Juan", "C001", "300", "x@mail.com");
        assertTrue(c.toString().contains("Juan"));
    }
}
