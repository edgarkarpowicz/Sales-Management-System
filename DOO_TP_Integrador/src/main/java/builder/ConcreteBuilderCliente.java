/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package builder;

import builder.SystemUsersBuilder;
import enums.Genero;
import java.util.List;
import modelos.Cliente;
import modelos.Domicilio;
import modelos.Persona;

/**
 *
 * @author edgar
 */
public class ConcreteBuilderCliente implements SystemUsersBuilder {

    private Cliente resultado;
    private Domicilio domicilio;
    private Persona persona;

    public ConcreteBuilderCliente() {
        this.resultado = null;
        this.domicilio = new Domicilio();
        this.persona = new Persona();
    }

    @Override
    public SystemUsersBuilder conNomCalle(String n) {
        domicilio.setNomCalle(n);
        return this;
    }

    @Override
    public SystemUsersBuilder conNumVivienda(int n) {
        domicilio.setNumVivienda(n);
        return this;
    }

    @Override
    public SystemUsersBuilder conNumDep(int d) {
        domicilio.setNumDepartamento(d);
        return this;
    }

    @Override
    public SystemUsersBuilder conCodPos(int c) {
        domicilio.setCodPostal(c);
        return this;
    }

    @Override
    public SystemUsersBuilder conLoc(String l) {
        domicilio.setLocalidad(l);
        return this;
    }

    @Override
    public SystemUsersBuilder conDep(String d) {
        domicilio.setDepartamento(d);
        return this;
    }

    @Override
    public SystemUsersBuilder conPais(String p) {
        domicilio.setPais(p);
        return this;
    }

    @Override
    public SystemUsersBuilder conNombre(String n) {
        persona.setNombre(n);
        return this;
    }

    @Override
    public SystemUsersBuilder conApellido(String a) {
        persona.setApellido(a);
        return this;
    }

    @Override
    public SystemUsersBuilder conNumDoc(String d) {
        persona.setNumDocumento(d);
        return this;
    }

    @Override
    public SystemUsersBuilder conTipoDoc(String t) {
        persona.setTipoDocumento(t);
        return this;
    }

    @Override
    public SystemUsersBuilder conCuit(String c) {
        persona.setCuit(c);
        return this;
    }

    @Override
    public SystemUsersBuilder conCondAfip(String ca) {
        persona.setCondicionAfip(ca);
        return this;
    }

    @Override
    public SystemUsersBuilder conGenero(Genero g) {
        persona.setGenero(g);
        return this;
    }

    @Override
    public SystemUsersBuilder conFechaNac(String f) {
        persona.setFechaNacimiento(f);
        return this;
    }

    @Override
    public SystemUsersBuilder conEmail(String e) {
        persona.setEmail(e);
        return this;
    }

    @Override
    public SystemUsersBuilder conTelefonos(List<String> t) {
        persona.setTelefonos(t);
        return this;
    }

    @Override
    public Cliente build() {
        return new Cliente(this);
    }

    @Override
    public SystemUsersBuilder reset() {
        this.resultado = null;
        this.persona = new Persona();
        this.domicilio = new Domicilio();
        return this;
    }

    public Cliente getResultado() {
        return resultado;
    }

    public void setResultado(Cliente resultado) {
        this.resultado = resultado;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

}
