package es.dsw.events;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/* [8] - SOBRECARGA E IMPLEMENTACIÓN DE LOS EVENTOS DE LOGIN (Opcional)
 * 
 * Esta clase no es obligatoria. Puedes eliminarla y la seguridad seguirá funcionando. Solo si se desea capturar eventos de
 * autenticación.
 * 
 * Se declara un componente spring tal que al hacer uso de la java annotation @EventListener, se sobrecarga
 * el evento que captura el after login (Despues del login). Con estos dos eventos podemos capturar y aplicar negocio 
 * especifico tanto cuando el login es exitoso (onSuccess) como cuando el login es rechazado (onFailure).
 * 
 * Observe como se da pistas de usos o aplicaciones prácticas que podemos hacer al capturar estos eventos, como es en el caso
 * de onSuccess para actualizar e inicializar por ejemplo variables de sesión y despreocuparnos de esta tarea en el resto del 
 * código de las controladoras de la aplicación. Tambien por ejemplo, para incorporar logs de seguridad.
 * 
 * Pruebe a incluir breaks-points y depurar para comprobar como despues del proceso de autenticación se captura el resultado.
 *
 * DOCUMENTACIÓN: https://docs.spring.io/spring-security/reference/servlet/authentication/events.html
 */

@Component
public class AuthenticationEvents {
	
	//Método que captura el evento de autenticación cuando este ha sido exitoso.
	@EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {

		//Esta instrucción, es un ejemplo de como desde aquí, podemos actualizar o inicializar una variable de sesión de usuario denominada DataUser 
		//asignandole un objeto de tipo String con la string 'validado'. Dado que no nos encontramos en una controladora, para poder acceder a parámetros 
		//de la petición (request) o de sesión, es necesario recurrir a RequestContextHolder que nos permite acceder al contexto principal del hilo del usuario.
		//RequestAttributes.SCOPE_SESSION está indicando que deseamos acceder y modificar un parámetro correspondiente a una variable de sesión.
		
		//Es un lugar ideal para inicializar datos del usuario. Si pruebas desde las controladoras a capturar esta variable de sesión (DataUser), te la encontrarás
		//inicializada.
		RequestContextHolder.getRequestAttributes().setAttribute("DataUser", "validado", RequestAttributes.SCOPE_SESSION);
    }

	//Si el login del usuario, no resulta ser un éxito, con este método se captura el evento de login fallido. Imagine por ejemplo,
	//que pudiera ser útil registrar los intentos fallidos de logeo para realizar otras acciones de seguridad o notificaciones. (Ej: Cuando
	//en una aplicación bancaria nos logeamos erroneamente 3 veces).
    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {

    }
}
