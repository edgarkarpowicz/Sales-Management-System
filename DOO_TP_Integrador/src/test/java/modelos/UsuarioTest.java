package modelos;

import enums.Genero;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class UsuarioTest {

    private Usuario usuario;
    private Domicilio domicilioInicial;

    @Before
    public void setUp() {
         // Ahora instanciamos también un Domicilio real:
        domicilioInicial = new Domicilio(
            "Calle 123",   // nomCalle
            10,            // numVivienda
            2,             // numDepartamento
            5000,          // codPostal
            "Ciudad",      // localidad
            "Provincia",   // departamento
            "Argentina"    // pais
        );

        // Construimos el Usuario pasando por el super constructor de Persona
        usuario = new Usuario(
            "Laura",                   // nombre
            "Ramírez",                 // apellido
            "87654321",                // numDocumento
            "DNI",                     // tipoDocumento
            "27-87654321-0",           // cuit
            "Monotributo",             // condicionAfip
            Genero.Femenino,           // género
            "1988-07-15",              // fechaNacimiento
            domicilioInicial,             // domicilio
            "laura@ejemplo.com",       // email
            Arrays.asList("4242-4242") // teléfonos
        );
    }

    @Test
    public void testGetCargo_devuelveNull() {
        // Según la implementación actual, getCargo() siempre retorna null.
        assertNull("getCargo() debe devolver null en la clase Usuario tal como está igualado",
                   usuario.getCargo());
    }

    @Test
    public void testLogin_getYSet() {
        // Al inicio login es null
        assertNull("Inicialmente login es null", usuario.getLogin());

        // Seteamos y comprobamos
        usuario.setLogin("laura123");
        assertEquals("El getter debe devolver el mismo login seteado",
                     "laura123", usuario.getLogin());
    }

    @Test
    public void testPassword_getYSet() {
        // Al inicio password es null
        assertNull("Inicialmente password es null", usuario.getPassword());

        // Seteamos y comprobamos
        usuario.setPassword("P@ssw0rd");
        assertEquals("El getter debe devolver la contraseña seteada",
                     "P@ssw0rd", usuario.getPassword());
    }

    @Test
    public void testFechaUltAcceso_getYSet() {
        // Al inicio fechaUltAcceso es null
        assertNull("Inicialmente fechaUltAcceso es null", usuario.getFechaUltAcceso());

        // Seteamos y comprobamos
        usuario.setFechaUltAcceso("2025-06-05 10:30");
        assertEquals("El getter debe devolver exactamente la fecha seteada",
                     "2025-06-05 10:30", usuario.getFechaUltAcceso());
    }

    @Test
    public void testConstructorHeredaCamposDePersona() {
        // Verificamos que los campos heredados de Persona están bien
        assertEquals("Laura", usuario.getNombre());
        assertEquals("Ramírez", usuario.getApellido());
        assertEquals("87654321", usuario.getNumDocumento());
        assertEquals("DNI", usuario.getTipoDocumento());
        assertEquals("27-87654321-0", usuario.getCuit());
        assertEquals("Monotributo", usuario.getCondicionAfip());
        assertEquals(Genero.Femenino, usuario.getGenero());
        assertEquals("Femenino", usuario.getGeneroStr());
        assertEquals("1988-07-15", usuario.getFechaNacimiento());
        
        // Comprobamos cada campo de domicilio heredado:
        Domicilio d = usuario.getDomicilio();
        assertNotNull(d);
        assertEquals("Calle 123", d.getNomCalle());
        assertEquals(10, d.getNumVivienda());
        assertEquals(2, d.getNumDepartamento());
        assertEquals(5000, d.getCodPostal());
        assertEquals("Ciudad", d.getLocalidad());
        assertEquals("Provincia", d.getDepartamento());
        assertEquals("Argentina", d.getPais());

        assertEquals("laura@ejemplo.com", usuario.getEmail());
        assertEquals(1, usuario.getTelefonos().size());
        assertEquals("4242-4242", usuario.getTelefonos().get(0));
    }

    @Test
    public void testSettersDePersona_enUsuario() throws Exception {
        // Cambiamos y verificamos algunos campos heredados desde Persona
        usuario.setNombre("Lucía");
        usuario.setApellido("González");
        usuario.setNumDocumento("11223344");
        usuario.setTipoDocumento("LC");
        usuario.setCuit("23-11223344-1");
        usuario.setCondicionAfip("Responsable Inscripto");
        assertEquals("Lucía", usuario.getNombre());
        assertEquals("González", usuario.getApellido());
        assertEquals("11223344", usuario.getNumDocumento());
        assertEquals("LC", usuario.getTipoDocumento());
        assertEquals("23-11223344-1", usuario.getCuit());
        assertEquals("Responsable Inscripto", usuario.getCondicionAfip());

        // Probamos setGenero(String) heredado
        usuario.setGenero("Masculino");
        assertEquals(Genero.Masculino, usuario.getGenero());
        assertEquals("Masculino", usuario.getGeneroStr());

        // Testeamos algún caso inválido para setGenero(String)
        try {
            usuario.setGenero("INEXISTENTE");
            fail("Debe lanzar Exception si el String no coincide con algún valor enum Genero");
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("No se pudo convertir"));
        }

        // Cambiamos fecha de nacimiento y email
        usuario.setFechaNacimiento("2000-01-01");
        usuario.setEmail("lucia@ejemplo.org");
        assertEquals("2000-01-01", usuario.getFechaNacimiento());
        assertEquals("lucia@ejemplo.org", usuario.getEmail());

         // Cambiamos domicilio a uno distinto
        Domicilio nuevoDom = new Domicilio(
            "Calle Nueva",  // nomCalle
            55,             // numVivienda
            3,              // numDepartamento
            1000,           // codPostal
            "OtraCiudad",   // localidad
            "OtraProv",     // departamento
            "Argentina"     // pais
        );
        usuario.setDomicilio(nuevoDom);
        Domicilio d2 = usuario.getDomicilio();
        assertNotNull(d2);
        assertEquals("Calle Nueva", d2.getNomCalle());
        assertEquals(55, d2.getNumVivienda());
        assertEquals(3, d2.getNumDepartamento());
        assertEquals(1000, d2.getCodPostal());
        assertEquals("OtraCiudad", d2.getLocalidad());
        assertEquals("OtraProv", d2.getDepartamento());
        assertEquals("Argentina", d2.getPais());

        // Cambiamos lista de teléfonos
        usuario.setTelefonos(Arrays.asList("9999-0000", "3333-2222"));
        assertEquals(2, usuario.getTelefonos().size());
        assertTrue(usuario.getTelefonos().contains("9999-0000"));
    }
}
