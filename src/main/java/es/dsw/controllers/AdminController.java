package es.dsw.controllers;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dsw.configuration.SecurityConfiguration;

/* [7] - CONTROLADORA PARA ROLES DE USUARIO ADMIN
 * 
 * Esta controladira se ha creado para que se compruebe, que tal como se indico en la clase de configuración SecurityAppConfig, solo los
 * usuarios con rol administrador tienen acceso. (Ver método securityFilterChain de la clase SecurityAppConfig.java).
 * 
 */
@Controller
@RequestMapping("/admin/")
public class AdminController {
	
	//Solo accesible por rol administrador
	@GetMapping(value = {"/ejemplo3"})
	public String ejem3() {
		
		return "/admin/ejemplo3";
	}
	
	//Solo accesible por rol administrador. Además este ejemplo muestra como podemos añadir en caliente nuevos usuarios
	//a la aplicación.
	@PostMapping(value = {"/newuser"})
	public String newuser(@RequestParam(name="user", defaultValue = "") String UserName,
						  @RequestParam(name="pass", defaultValue = "") String password,
						  Model objModel) {
		 
		objModel.addAttribute("ResultadoOperacion", false);
		
		if ((!UserName.trim().equals("")) && (!password.trim().equals(""))) {
			
			@SuppressWarnings("deprecation")
			UserDetails user = User.withDefaultPasswordEncoder()
			    .username(UserName) 
	            .password(password)   
	            .roles("usuario") 
	            .build();

			SecurityConfiguration.NewUserInMemory(user);
			
			objModel.addAttribute("ResultadoOperacion", true);
		}

        
        
		return "/admin/ejemplo8";
	}
}
