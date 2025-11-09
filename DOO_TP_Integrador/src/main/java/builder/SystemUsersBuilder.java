/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package builder;

import enums.Genero;
import java.util.List;
import modelos.Cliente;
import modelos.Domicilio;
import modelos.Persona;

/**
 *
 * @author edgar
 */
public interface SystemUsersBuilder {
    SystemUsersBuilder conNomCalle(String n);
    SystemUsersBuilder conNumVivienda(int n);
    SystemUsersBuilder conNumDep(int d);
    SystemUsersBuilder conCodPos(int c);
    SystemUsersBuilder conLoc(String l);
    SystemUsersBuilder conDep(String d);
    SystemUsersBuilder conPais(String p);
    SystemUsersBuilder conNombre(String n);
    SystemUsersBuilder conApellido(String a);
    SystemUsersBuilder conNumDoc(String d);
    SystemUsersBuilder conTipoDoc(String t);
    SystemUsersBuilder conCuit(String c);
    SystemUsersBuilder conCondAfip(String ca);
    SystemUsersBuilder conGenero(Genero g);
    SystemUsersBuilder conFechaNac(String f);
    SystemUsersBuilder conEmail(String e);
    SystemUsersBuilder conTelefonos(List<String> t);

    Cliente build();
    
    SystemUsersBuilder reset();
}
