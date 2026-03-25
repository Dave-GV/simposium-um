## Confguracion de credenciales
Para mantener nuestras credenciales seguras y permitir que cada desarrollador tenga su propia configuración local (como contraseñas de base de datos), este proyecto utiliza variables de entorno mediante un archivo `.env`. 

El proyecto ya está configurado para leer este archivo automáticamente al arrancar, sin importar si usas IntelliJ, VS Code, Eclipse o la terminal.

### Pasos para levantar el proyecto por primera vez:

1. **Crea tu archivo local:** Genera un archivo `.env` en la carpeta raiz del proyecto y confugro su cotenido tomando como bae el `.env.example` .

Nota
El `.env.example` contiene las variables que la aplicación necesita para funcionar, pero con valores ficticios.