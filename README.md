# QR-Authentication-Scheme

Prototipo de implementación de aplicaciones cliente y servidor para un esquema de autenticación basado en códigos QR. El esquema fue diseñado para ser utilizado en el control de acceso físico de instalaciones con entrada restringida a traves de dos funciones principales:

Fase de registro
![Fase de registro](https://i.ibb.co/9wRFrH8/Imagen1.png) 

Fase de solicitud de tokens
![Fase de solicitud de tokens](https://i.ibb.co/V9Ff1S8/Imagen2.png)

## Componentes
En este repositorio pueden ser encontrados dos de los componentes principales del esquema, estos son el servidor central y la aplicación cliente.

### Servidor:
Implementado en lenguaje Java, se encarga de prestar los procesos necesarios para la funcionalidad del sistema a traves la publicación de servicios API REST.

### Aplicación Móvil:
Desarrollada en Android Java (API 24), es la aplicación cliente utilizada por los usuarios para interactuar con el sistema y poder realizar el proceso de autenticación mediante la recepción de un código QR que se muestra en la pantalla del dispositivo y que será escaneado por el lector QR instalado en la puerta cerrada.

## Comenzando

_Estas instrucciones te permitirán obtener una copia del proyecto en funcionamiento en tu máquina local para propósitos de desarrollo y pruebas._

### Pre-requisitos

* Java 8
* MySQL (o el sistema de la base de datos que desee configurar)

Para la visualización del código del servidor se recomienda utilizar un IDE compatible con el framework Spring como las encontradas en la página https://spring.io/tools, así como también el uso de Android Studio para realizar el manejo de la aplicación móvil. 

Se requiere la creación de una base de datos en la que se almacenarán los datos del sistema. El acceso a esta debe ser configurado en el archivo application.properties del servidor así:

### Configuración y Despliegue

1. Configurar la conexión del servidor a la base de datos mediante el archivo [application.properties](Server/src/main/resources/application.properties) como indica su documentación. Cabe resaltar que la base de datos a conectar ya debe haber sido creada.
2. Crear el certificado digital del servidor, cambie el formato a .p12 y añádalo en la carpeta [keystore](Server/src/main/resources/keystore/). Posteriormente, en el archivo [application.properties](Server/src/main/resources/application.properties) ingrese la ruta del archivo del certificado como lo indica la documentación.
3. Inicie la ejecución del servidor.

Nota: Para agregar en la base de datos los usuarios necesarios de forma manual, utilice peticiones POST en las que se incluye la información del usuario en el cuerpo de estas. Estas peticiones son recibidas por el método addUser de la clase [RestUserController](Server/src/main/java/com/ajparedes/rest/RestUserController.java).

4. Modifique el archivo [network_security_config.xml](MobileApp/app/src/main/res/xml/network_security_config.xml) de tal forma que se indique el dominio del servidor al cual conectarse, junto con la fecha de expiración y valor del pin digest del certificado digital a aceptar.
5. Modifique el archivo [strings.xml](MobileApp/app/src/main/res/values/strings.xml) de tal forma que los strings "base_url" y "host" hagan referencia a la dirección de su servidor.
6. Agregue el certificado de su servidor en formato .cer en la carpeta [raw](MobileApp/app/src/main/res/raw/).
7. De acuerdo con nombre del certificado guardado en la carpeta raw, modifique la línea 91 del archivo [APIClientSingleton.java](MobileApp/app/src/main/java/com/ajparedes/qrscheme/APIClientSingleton.java).
8. Compilar e instalar la aplicación en el dispositivo móvil del usuario.

## Construido con

* [Spring Boot](https://spring.io/projects/spring-boot) - Framework java
* [Maven](https://maven.apache.org/) - Manejador de dependencias
* [Andorid](https://developer.android.com/) - Aplicación móvil

## Versión

Versión 1.0 - Enero 2020

## Autor

Andrea Paredes

## Licencia

Este proyecto está bajo la Licencia:

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE.md)
