# Configuracion Android de Google recibida - 2026-11-06

Se reemplazo app/google-services.json con el archivo actualizado del usuario. Proyecto pactogg, paquete com.pactogg.app; OAuth Android SHA-1 F0:37:60:4D:8A:A7:E0:18:EA:05:EB:43:8E:FA:E3:2C:C2:C2:AB:87 coincide con la firma debug consultada con keytool. Existe cliente OAuth web de tipo 3.

Se conecto el boton Continuar con Google con Credential Manager (seleccion explicita), googleid y el consumidor POST auth/google existente. No se incorpora Firebase Auth como custodio alterno: los tokens de sesion de PactoGG siguen en SessionManager y almacenamiento cifrado. El Client ID se lee del JSON para com.pactogg.app o de pactogg.googleWebClientId. NICKNAME_REQUIRED abre el formulario de apodo y consentimiento; cancelar el selector no envia una solicitud de autenticacion. No se registran ID tokens ni mensajes arbitrarios de Google.

Cliente web publico que debe aceptar el backend:
841778632932-0jnd246u1mvbl6n9djtthsdgvb0um6do.apps.googleusercontent.com

Dependencias fijadas: androidx.credentials 1.5.0 y googleid 1.1.1. Detekt directo paso tras incorporar el boton; pendiente compilar, ejecutar JVM/Android y probar Google con cuenta autorizada. La configuracion de firma es debug; una entrega firmada con otra clave requiere su propia huella. No hay prueba real de login ni de entrega push. El archivo Firebase por si solo no implementa recepcion de notificaciones. Backend y GitHub sin cambios.
