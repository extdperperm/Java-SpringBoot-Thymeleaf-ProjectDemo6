package es.dsw.events;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

/* [9] - SOBRECARGA E IMPLEMENTACIÓN DEL EVENTO DE LOGOUT (Opcional) 
 * 
 *  Esta clase no es obligatoria. Puedes eliminarla y la seguridad seguirá funcionando. Solo si se deseas capturar eventos de
 *  fin de la autenticación (logout).
 */
@Component
public class LogoutEventListener implements ApplicationListener<LogoutSuccessEvent> {

    @Override
    public void onApplicationEvent(LogoutSuccessEvent event) {
        // Aquí puedes agregar la lógica que deseas ejecutar después de que el usuario haya cerrado sesión
        // Por ejemplo, puedes imprimir un mensaje en el registro o generar logs.
        System.out.println("El usuario ha cerrado sesión exitosamente."); 
    }
}