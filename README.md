# Coupon Service
Se construye API REST que contiene 2 endpoints:
1. Servicio que calcula los items para comprar con cupón.
2. Servicio que obtiene los 5 items favoritos más comprados.

## Instrucciones para levantar la app.


### Consideraciones 
1. Variables de entorno, se deben crear en la máquina donde se despliegue, si es en local se pueden reemplazar los valores en el archivo application.yml.
    ```
    API_ITEMS_URL=<url_mercadolibre>
    MYSQL_HOST=<localhost>
    MYSQL_USER=<user>
    MYSQL_PWD=<password>
    ```
2. Se debe tener instalado un docker o servidor de MySQL.
3. Se debe ejecutar el script de BD, para tener la copia local.
   ```
   CREATE DATABASE coupons;
   USE coupons;
   CREATE TABLE favorite_items (
   id INT(6) AUTO_INCREMENT PRIMARY KEY,
   item VARCHAR(50) NOT NULL COMMENT 'Item name purchased with coupons',
   quantity INT(6) NOT NULL COMMENT 'Quantity purchased per item'
   ) COMMENT 'Table that saves the number of items purchased with coupons';

4. Actualizar el archivo **application.yml**, con el usuario y clave generados.
   ```
   username: ${MYSQL_USER:user}
   password: ${MYSQL_PWD:password}
   

### Pasos a seguir para desplegar aplicación (comandos)

* Descargar o clonar el proyecto https://github.com/jaimetellezb/coupon.git.
```
git clone https://github.com/jaimetellezb/coupon.git
```
* Ejecutar los siguientes comandos para compilar el proyecto:
  * ***./gradlew clean***
  * ***./gradlew build***
* Ejecutar el comando ***./gradlew bootRun*** para levantar la aplicación.
* Para ejecutar pruebas usar el comando **./gradlew test**.

### Pasos a seguir para desplegar aplicación (IDE)

Si le es más cómodo desde un IDE por ejemplo IntelliJ, puede seguir las siguientes instrucciones:

* Al clonar el proyecto, abrir el IDE y dar clic en _File/Open..._
* Luego ir al directorio donde está el proyecto y dar clic en _Ok_
* Al abrirlo el IDE empezará la compilación y descarga de dependencias o librerías del proyecto.
* Una vez termine la compilación, puede al archivo CouponApplication.java, ubicado en _src/main/java/com.meli.coupon_
* Dar clic derecho _Run_


## Pruebas en ambiente local

Para este paso es importante tener una herramienta para probar servicios REST, como puede ser Postman, Insomnia y otro de su preferencia.
En esta ocasión se utiliza Postman.

1. #### Servicio para calcular y obtener los items que el usuario va a comprar de acuerdo a un monto obtenido en un cupón


##### Caso exitoso

   * Importar el siguiente curl, desde la opción _File/Import/Raw text_.
   ```
    curl --location --request POST 'http://localhost:8080/coupon' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "item_ids": [
    "MLA844702270",
    "MLA844702274",
    "MLA844702281",
    "MLA844702289",
    "MLA844702297"],
    "amount": 1200
    }'
   ```
   * Al ejecutarlo el resultado exitoso es:
   ```
     {
      "item_ids": [
        "MLA844702289",
        "MLA844702270"
      ],
      "total": 1200.0
     }
   ```
##### Caso cuando un item id no existe

* Ejecutar el curl:
```
curl --location --request POST 'http://localhost:8080/coupon' \
--header 'Content-Type: application/json' \
--data-raw '{
    "item_ids": [
        "MLA844702270",
        "MLA8447022747"],
    "amount": 1200
}'
```
* da como resultado:
```
{
    "timestamp": "2022-05-30T09:22:40.021+00:00",
    "message": "Not Found",
    "details": "404 Not Found: \"{\"message\":\"Item with id MLA8447022747 not found\",\"error\":\"not_found\",\"status\":404,\"cause\":[]}\""
}
```

##### Caso cuando la url no existe

* Ejecutar el curl:
```
curl --location --request POST 'http://localhost:8080/coupon3' \
--header 'Content-Type: application/json' \
--data-raw '{
    "item_ids": [
        "MLA844702270",
        "MLA8447022747"],
    "amount": 1200
}'
```
* da como resultado:
```
{
    "timestamp": "2022-05-30T09:26:45.625+00:00",
    "status": 404,
    "error": "Not Found",
    "path": "/coupon3"
}
```

2. #### Servicio para obtener los 5 items favoritos que más se han comprado.


##### Caso exitoso

* Importar el siguiente curl, desde la opción _File/Import/Raw text_.
   ```
    curl --location --request GET 'http://localhost:8080/coupon/favorites?limit=5'
   ```
* Al ejecutarlo el resultado exitoso con los datos probados es:
   ```
    [
      {
        "MLA844702270": 16,
        "ML1": 10,
        "ML2": 8,
        "MLA844702289": 8,
        "ML3": 6
      }
    ]
   ```

## Documentación OpenAPI
Después de levantar la app, ingresar a la url: http://localhost:8080/swagger-ui/index.html.


## URL API 

### LOCAL
* Items a comprar: http://localhost:8080/coupon
* Items favoritos: http://localhost:8080/coupon/favorites?limit=5
* Precio de items: https://api.mercadolibre.com/items/

### HEROKU
* Items a comprar: http://localhost:8080/coupon
* Items favoritos: http://localhost:8080/coupon/favorites?limit=5
* Precio de items: https://api.mercadolibre.com/items/

**NOTA:** Con Heroku en la cuenta gratuita cuando no se usa el servicio por más de 30 minutos, apagan la instancia, luego cuando se hace una petición se vuelve a encender y se demora alrededor de un minuto en estar disponible.
Luego de eso debería seguir funcionando con normalidad siempre y cuando tenga peticiones constantes.


