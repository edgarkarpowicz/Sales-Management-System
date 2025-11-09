package modelos;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import enums.Genero;

import static org.junit.Assert.*;

public class PersonaTest {

    private Persona persona;
    private Domicilio domicilio;

    @Before
    public void setUp() {
        // Ahora creamos un Domicilio real, usando el constructor de Domicilio.java:
        domicilio = new Domicilio(
            "Av. Siempre Viva",  // nomCalle
            123,                 // numVivienda
            4,                   // numDepartamento
            5000,                // codPostal
            "Springfield",       // localidad
            "Capital",           // departamento
            "Argentina"          // pais
        );

        // Valores iniciales para el constructor completo
        persona = new Persona(
            "Juan",                   // nombre
            "Pérez",                  // apellido
            "12345678",               // numDocumento
            "DNI",                    // tipoDocumento
            "20-12345678-9",          // cuit
            "Responsable Inscripto",  // condicionAfip
            Genero.Masculino,         // género
            "1990-01-01",             // fechaNacimiento
            domicilio,            // domicilio
            "juan@ejemplo.com",       // email
            Arrays.asList("1234-5678", "8765-4321") // teléfonos
        );
    }

    @Test
    public void testGettersIniciales() {
        assertEquals("Juan", persona.getNombre());
        assertEquals("Pérez", persona.getApellido());
        assertEquals("12345678", persona.getNumDocumento());
        assertEquals("DNI", persona.getTipoDocumento());
        assertEquals("20-12345678-9", persona.getCuit());
        assertEquals("Responsable Inscripto", persona.getCondicionAfip());
        assertEquals(Genero.Masculino, persona.getGenero());
        assertEquals("Masculino", persona.getGeneroStr());
        assertEquals("1990-01-01", persona.getFechaNacimiento());
        
        // Verificamos cada campo de Domicilio que guardamos:
        Domicilio d = persona.getDomicilio();
        assertNotNull(d);
        assertEquals("Av. Siempre Viva", d.getNomCalle());
        assertEquals(123, d.getNumVivienda());
        assertEquals(4, d.getNumDepartamento());
        assertEquals(5000, d.getCodPostal());
        assertEquals("Springfield", d.getLocalidad());
        assertEquals("Capital", d.getDepartamento());
        assertEquals("Argentina", d.getPais());
        
        assertEquals("juan@ejemplo.com", persona.getEmail());
        List<String> telefonos = persona.getTelefonos();
        assertEquals(2, telefonos.size());
        assertTrue(telefonos.contains("1234-5678"));
        assertTrue(telefonos.contains("8765-4321"));
    }

    @Test
    public void testSettersSimples() {
        // Nombre y apellido
        persona.setNombre("María");
        persona.setApellido("Gómez");
        assertEquals("María", persona.getNombre());
        assertEquals("Gómez", persona.getApellido());

        // Documento y tipo
        persona.setNumDocumento("87654321");
        persona.setTipoDocumento("LC");
        assertEquals("87654321", persona.getNumDocumento());
        assertEquals("LC", persona.getTipoDocumento());

        // CUIT y condición AFIP
        persona.setCuit("23-87654321-0");
        persona.setCondicionAfip("Monotributo");
        assertEquals("23-87654321-0", persona.getCuit());
        assertEquals("Monotributo", persona.getCondicionAfip());

        // Fecha de nacimiento
        persona.setFechaNacimiento("1985-12-31");
        assertEquals("1985-12-31", persona.getFechaNacimiento());

        // Email
        persona.setEmail("maria@ejemplo.org");
        assertEquals("maria@ejemplo.org", persona.getEmail());

        // Teléfonos
        persona.setTelefonos(Arrays.asList("1111-2222"));
        List<String> tel = persona.getTelefonos();
        assertEquals(1, tel.size());
        assertEquals("1111-2222", tel.get(0));
    }

    @Test
    public void testSetYGetDomicilio() {
        // Cambiamos el domicilio a uno distinto
        Domicilio otroDom = new Domicilio(
            "Calle Falsa",  // nomCalle
            742,            // numVivienda
            1,              // numDepartamento
            1000,           // codPostal
            "Shelbyville",  // localidad
            "Capital",      // departamento
            "Argentina"     // pais
        );
        persona.setDomicilio(otroDom);
        Domicilio d2 = persona.getDomicilio();
        assertNotNull(d2);
        assertEquals("Calle Falsa", d2.getNomCalle());
        assertEquals(742, d2.getNumVivienda());
        assertEquals(1, d2.getNumDepartamento());
        assertEquals(1000, d2.getCodPostal());
        assertEquals("Shelbyville", d2.getLocalidad());
        assertEquals("Capital", d2.getDepartamento());
        assertEquals("Argentina", d2.getPais());
    }

    @Test
    public void testSetGenero_porString_valido() throws Exception {
        // Pongo el género como String (coincidente con algún valor del enum)
        persona.setGenero("Femenino");
        assertEquals(Genero.Femenino, persona.getGenero());
        assertEquals("Femenino", persona.getGeneroStr());
    }

    @Test
    public void testSetGenero_porString_invalido_lanzaException() {
        try {
            persona.setGenero("OTRO_NO_EXISTE");
            fail("Debe lanzar excepción si el String no coincide con ningún valor de enum Genero");
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("No se pudo convertir"));
        }
    }
}
