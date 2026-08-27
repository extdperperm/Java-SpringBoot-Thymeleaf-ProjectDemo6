package es.dsw.controllers;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

// [6] - CONTROLADORA PRINCIPAL

@Controller
public class MainController {
	
	//El mapeo por defecto es /index y si te fijas en los filtros de seguridad, al no aparecer ninguna excepción sobre este mapeo, se considerará
	//que se requiere que el usuario esté al menos autenticado. Si depuras, comprobarás que nada más acceder al usuario a la raiz /, al no estar autenticado
	//se le redirigirá a la controladora indicada en .loginPage("/loggin") del método securityFilterChain de la clase SecurityConfiguration.java
	@GetMapping(value= {"/", "/index"})
	public String index() {
		return "index";
	}
	
	//Si analizas los filtros de seguridad, se permite el acceso a este mapeo a todos los clientes con o sin autenticación.
	@GetMapping(value= {"/ayuda"})
	public String ayuda() {
		return "ayuda";
	}
	
	//Mapeo que devolverá la vista con el formulario (.loginPage("/loggin") del método securityFilterChain de la clase SecurityConfiguration.java)
	@GetMapping(value= {"/loggin"})
	public String loggin() {
		return "loggin";
	}
	
	//Este mapeo es solo accesible para clientes autenticados. Se muestra como podemos acceder desde las controladoras
	//a los datos de autentificación de los usuarios haciendo uso del objeto de tipo Authentication.
	@GetMapping(value = {"/ejemplo4"})
	public String ejem4(Model objModel, Authentication authentication) {
		String Roles = "";
		
		//Si el usuario dispone de roles se procede a obtenerlos.
		if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
			 GrantedAuthority auxRol;
			 //Se obtiene la colección de roles del usuario
			 Collection<? extends GrantedAuthority> objRoles = authentication.getAuthorities();
			 //Se prepara para iterar la colección
			 Iterator<? extends GrantedAuthority> iterator = objRoles.iterator();
			 
		        //Se itera recorriendo todos los roles que pudiera disponer el usuario
		        while (iterator.hasNext()) {
		        	auxRol = iterator.next();
		        	Roles = Roles + auxRol.getAuthority() + ", ";
		        } 
		}
		
		//Se obtienen datos de autenticación como el nombre. Todos los datos se asignan al modelo de spring
		//para mostrar la información en las vistas.
		objModel.addAttribute("Nombre", authentication.getName());
		objModel.addAttribute("Roles", Roles);
		
		return "/authenticated/ejemplo4";
	}

	//Este mapeo es solo accesible a usuarios autenticados y muestra como podemos realizar un autologin desde el código (sin intervención
	//del usuario). 
	@GetMapping(value = {"/ejemplo5"})
	public String ejem5(HttpServletRequest request, Model objModel) {

		//Por defecto consideramos que no habrá error y el autologin se ejecutará correctamente y cargamos el resultado en un atributo request del modelo.
		objModel.addAttribute("Resultado", "Autologin realizado correctamente");
		try {
			 //Forzamos un logout dado que estamos en una región de código donde solo acceden los usuarios autenticados y lo que pretendemos es volver
			 //a realizar una autenticación pero en este caso de forma automática (vía código). Si no lo hacemos en este caso, se producirá un error al
			 //detectar que otro usuario esta logeado.
			 request.logout();
			 //Se fuerza el login, en este caso siempre del usuario pepita.
			 request.login("pepita", "5678");
		    } catch (ServletException e) {
		    	objModel.addAttribute("Resultado", "Error al realizar el autologin. +Info: " + e.getMessage());
		      }
		
		//Se carga en el modelo de spring los datos de la autenticación para mostrarlos en la vista.
		objModel.addAttribute("Nombre", "pepita");
		objModel.addAttribute("Roles", "5678");
		
		return "ejemplo5";
	}
}
