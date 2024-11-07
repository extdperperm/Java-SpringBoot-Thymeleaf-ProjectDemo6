package es.dsw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


import static org.springframework.security.config.Customizer.withDefaults;

/*
 * [3] - CONFIGURACIÓN DE SPRING SECURITY (Obligatorio)
 *
 *        @author Daniel Pérez Pérez
 * 
 * En esta clase, se configuran los filtros de seguridad en el método securityFilterChain y se cargan los usuarios en memoria de aplicación con
 * el método userDetailsService. Se configura una segurida básica basada en usuario y contraseña.
 * 
 * Los métodos de esta clase, se ejecutarán una única vez al arrancar la aplicación. Si usted desea cargar los usuarios desde una base de datos,
 * deberá modificar el contenido del método userDetailsService.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	//Se declara el objeto InMemoryUserDetailsManager que contendrá a los usuarios cargados en memoria de aplicación como estático para
	//permitir desde las controladoras, añadir usuarios. Es decir, en el método de esta clase userDetailsService se añaden los usuarios
	//al arrancar la aplicación, pero ¿Y si queremos añadir nuevos usuarios durante la ejecución de la aplicación?, pues en este caso
	//con el método NewUserInMemory permitimos añadir nuevos usuarios.
	private static InMemoryUserDetailsManager InMemory;
	
	public static void NewUserInMemory(UserDetails objUser) {
		if (!InMemory.userExists(objUser.getUsername())) {
			 InMemory.createUser(objUser);
		}
	}
	
	/*
	 * [4] - CONFIGURACIÓN DE LOS FILTROS DE SEGURIDAD
	 * 
	 * En este método se especificará como se comportará la seguridad de la aplicación en cuanto el acceso a sus recursos estáticos y
	 * los mapeos a sus controladoras. También se definirá que regiones o areas son accesibles a uno u otros roles de usuario así como
	 * el mapeo que contendrá el formulario de login y el comportamiento tras el login.
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//DOCUMENTACIÓN DE REFERENCIA:
		//https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html
		
		//Se parte del objeto HttpSecurity y se empieza a definir las reglas.
		http//.csrf(csrf -> csrf.disable())//Si se descomenta esta línea, no se requiere token csrf cada vez que debas realizar envío de datos al servidor. Se recomienda no realizar csrf.disable() 
	    //FILTROS
		.authorizeHttpRequests((authorize) -> authorize
			    //Se indican todas las rutas relativas a las cuales se permitirá el acceso a todos los clientes (aunque no estén autenticados). 
				//Se indica los mapeos en los que tendremos los recursos estáticos (.js, css etc)
				.requestMatchers("/img/**").permitAll() //Acceso permitido de todos los clientes a la ruta /img y todas las que cuelguen de ella de forma recursiva.
				.requestMatchers("/styles/**").permitAll() //Acceso permitido de todos los clientes a la ruta /styles y todas las que cuelguen de ella de forma recursiva.
				//Se indican los mapeos a controladoras por las que permitiremos a todos los usuarios navegar.
				.requestMatchers("/ayuda").permitAll() //Acceso permitido de todos los clientes a la ruta /ayuda que en este caso se corresponde con el mapeo de una controladora. (ver MainController.java)
				//Se indican los mapeos a todas las rutas de las controladoras que se permitirá el acceso según los roles del usuario.
				.requestMatchers("/admin/**").hasRole("administrador") //Solo los usuarios con rol administrador podrá acceder a /admin/ y todos los mapeos que cuelguen de este.
			        //Nota: Con hasAnyRole puedes indicar los roles que permites acceso a un mismo mapeo. Si aplicas sobre el mismo mapeo más de una regla CUIDADO el orden importa.
				.anyRequest().authenticated() //Finalmente se indica que para el resto de mapeos, se requerirá autenticación. Es decir, para el resto, se impide el acceso sin autenticación.
				//Nota: Observa que los dobles ** indican esa ruta y las derivadas de ella de forma recursiva. Un * indica en el caso de directorios, solo a esa carpeta.
		)
		.httpBasic(withDefaults()) //Aquí se define que el tipo de autenticación es el básico, basado en usuario y contraseña.
		.formLogin(form -> form    //Se utilizará formulario login.
				.loginPage("/loggin") //Se indica el mapeo a controladora que contiene el formulario de autenticación. Es importante que veas el código html de loggin.
				.loginProcessingUrl("/logginprocess") //Se indica el mapeo invocado donde se procesa la autenticación. IMPORTANTE: No tiene por que existir esta controladora /logginprocess ni su vista. Si no se indica este dato, el mapeo es el indicado en .loginPage.
				//.defaultSuccessUrl("/ayuda", true) //Si descomentas esta línea, entonces siempre tras cada autenticación exitosa de usuario lo redirigirá a la ruta relativa /ayuda. Es interesante redirigir despues de logear cuando queremos que siempre el usuario vaya a un sitio concreto, sino lo devolverá a el mapeo que intentaba acceder el usuario.
				.permitAll() //Se debe permitir obviamente que el usuario acceda al formulario del login.	
			)
		//DOCUMENTACIÓN DE REFERENCIA:
		//https://docs.spring.io/spring-security/reference/servlet/authentication/logout.html
		//Con esta siguiente línea, se indica que cuando el usuario invoque un logout lo redireccione a /loggin (donde estará el formulario). Si deseas que lo redirija a otro mapeo, modifiquelo.
		.logout((logout) -> logout.logoutSuccessUrl("/loggin").permitAll());
		
		return http.build();
	}

	/*
	 * [5] - CARGA DE LOS USUARIOS PERMITIDOS EN MEMORIA DE APLICACIÓN
	 * 
	 * En este método se deben cargar los usuarios que podrán logearse contra esta aplicación. Si tienes los usuarios en una tabla en base de datos,
	 * deberas acceder a los datos y cargar cada usuario en un objeto UserDetails que deberas cargar en un objeto InMemoryUserDetailsManager.
	 */
	@Bean
	InMemoryUserDetailsManager userDetailsService() {
	
		
    	//Esta java annotation únicamente está deshabilitando los warning producto de usar User.withDefaultPasswordEncoder. Realmente dicho método no está deprecate, sino que por seguridad
    	//se recuerda al desarrollador, que esta forma de crear usuarios no garantiza que las contraseñas no estén a la vista del desarrollador o administrador de este código. 
    	//Por cada usuario, se debe repetir el siguiente código:
    	
		//Usuario 1
		@SuppressWarnings("deprecation")
		UserDetails user1 = User.withDefaultPasswordEncoder()
		    .username("pepito") //Nombre de usuario
            .password("1234")   //Password
            .roles("usuario", "administrador") //Roles
            .build();
		
		//Usuario 2
		@SuppressWarnings("deprecation")
		UserDetails user2 = User.withDefaultPasswordEncoder()
		    .username("pepita")
            .password("5678")
            .roles("usuario")
            .build();
		
		
        //Se crea un objeto InMemoryUserDetailsManager que nos permitirá cargar los usuarios en memoria de aplicación.
        InMemory = new InMemoryUserDetailsManager();//new InMemoryUserDetailsManager(user);
        
        //Se cargan los usuarios.
        InMemory.createUser(user1);
        InMemory.createUser(user2);
       
        //Se devuelve a el modulo de Spring Security el descriptor del objeto InMemoryUserDetailsManager para que surta efecto las modificaciones.
        return InMemory;
		
	}
	
	
   
    



}
