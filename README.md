# Comparación SOAP vs REST — Suma de dos números

Proyecto para el curso de Desarrollo Web. Implementa la misma operación
(sumar dos números) expuesta de dos formas distintas: como servicio SOAP
(con contrato WSDL) y como servicio REST (JSON sobre HTTP).

## Requisitos del proyecto

- **Java**: JDK 25 (LTS)
- **IDE utilizado**: IntelliJ IDEA
- **Gestor de dependencias**: Maven
- **Librerías usadas**:
  - `com.sun.xml.ws:jaxws-rt` (4.0.3) — implementación de JAX-WS para el servicio SOAP
  - `org.json:json` (20250107) — manejo de JSON para el servicio REST

## Estructura del proyecto

```text
comparacion-soap-rest-Naser/
├── pom.xml
├── README.md
├── .gitignore
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── umg/
│                   ├── soap/
│                   │   ├── SumaServicio.java      # Lógica del servicio SOAP (JAX-WS, Code First)
│                   │   └── PublicadorSoap.java    # Publica/arranca el servicio SOAP
│                   └── rest/
│                       └── ServidorRest.java      # Servicio REST con HttpServer del JDK
└── wsdl/
    └── servicio.wsdl              # Copia de referencia del WSDL generado
```

## Cómo ejecutar el proyecto

1. Clonar el repositorio y abrirlo en IntelliJ IDEA como proyecto Maven (o `mvn install` desde consola).
2. Verificar que el proyecto usa JDK 25.

### Levantar el servicio SOAP
Correr la clase `com.umg.soap.PublicadorSoap` (clic derecho → Run).
Queda escuchando en:
```
http://localhost:8081/ws/suma
```

### Levantar el servicio REST
Correr la clase `com.umg.rest.ServidorRest` (clic derecho → Run).
Queda escuchando en:
```
http://localhost:8082/suma
```

> Ambos servicios pueden correr al mismo tiempo (usan puertos distintos) o por separado.

## Cómo consumir el servicio SOAP

1. Con `PublicadorSoap` corriendo, el WSDL está disponible en:
   ```
   http://localhost:8081/ws/suma?wsdl
   ```
2. Probado con **SoapUI**:
   - `File → New SOAP Project` → pegar la URL del WSDL de arriba.
   - Abrir `sumar → Request 1`, completar los valores de `a` y `b` en el XML de la petición.
   - Enviar y ver la respuesta.

Ejemplo de petición SOAP enviada:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://soap.umg.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:sumar>
         <a>5</a>
         <b>3</b>
      </soap:sumar>
   </soapenv:Body>
</soapenv:Envelope>
```

Respuesta recibida:
```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <ns2:sumarResponse xmlns:ns2="http://soap.umg.com/">
         <return>8</return>
      </ns2:sumarResponse>
   </S:Body>
</S:Envelope>
```

Imagen de referencia (prueba en SoapUI):

<img width="956" height="555" alt="Prueba SOAP en SoapUI" src="https://github.com/user-attachments/assets/8c19aec5-5ef3-4dc8-823f-c5af114038c8" />

## Cómo consumir el servicio REST

Con `ServidorRest` corriendo, probado con `curl`:

```bash
curl -X POST http://localhost:8082/suma -H "Content-Type: application/json" -d "{\"a\": 5, \"b\": 3}"
```

Respuesta exitosa (`200 OK`):
```json
{"resultado":8}
```

Si se envían datos incompletos o inválidos (`400 Bad Request`):
```json
{"error":"Datos inválidos, se esperaba {\"a\":numero,\"b\":numero}"}
```

Si se usa un método distinto a `POST` (`405 Method Not Allowed`):
```json
{"error":"Método no permitido, usa POST"}
```

Imagen de referencia (prueba con curl, caso exitoso):

<img width="856" height="253" alt="Prueba REST con curl" src="https://github.com/user-attachments/assets/c4eb9df8-112c-46e4-b6c0-f5975639d379" />

## Pruebas realizadas

### SOAP
- WSDL generado, visto en el navegador
- Prueba en SoapUI (petición `a=5, b=3` → respuesta `8`)

<img width="950" height="562" alt="WSDL visto en el navegador" src="https://github.com/user-attachments/assets/4e5242c9-03d0-4f5b-8aef-11a86262bc7d" />

<img width="956" height="555" alt="Prueba SoapUI a=5 b=3" src="https://github.com/user-attachments/assets/4a850d93-d924-426c-9810-8da862340c12" />

### REST
- `POST /suma` con datos válidos → `200 OK`
- `POST /suma` con datos incompletos → `400 Bad Request`
- `GET /suma` → `405 Method Not Allowed`

<img width="856" height="253" alt="Pruebas curl: exitosa, error 400 y error 405" src="https://github.com/user-attachments/assets/72022c0d-294d-4e52-b24e-b675fecce576" />

## Comparación entre SOAP y REST

Desde el momento de empezar a construir ambos servicios ya se notaban diferencias
importantes. Para SOAP necesitamos una librería externa robusta (JAX-WS) que se
encarga de todo el manejo del XML y de generar el WSDL automáticamente; para REST,
en cambio, el servidor HTTP en sí ya viene incluido en el propio Java, y solo
agregamos una librería pequeña para leer y armar JSON. SOAP es más formal y hay
que tener más cuidado con la estructura del XML, aunque la librería que usamos
facilitó bastante ese trabajo. Curiosamente, en cuestión de código, terminamos
escribiendo más para REST que para SOAP: en SOAP las anotaciones hacen la mayor
parte del trabajo y el código queda muy corto, mientras que en REST tuvimos que
leer el cuerpo de la petición y armar el JSON manualmente. Aun así, sentimos que
REST es más útil cuando se quiere entender a fondo qué está pasando en el código,
porque no depende de "magia" oculta de una librería. Usaríamos SOAP en sistemas
más estrictos y que requieran mayor seguridad y formalidad, como aplicaciones
bancarias, y usaríamos REST para servicios de uso más cotidiano y ágil, como el
inicio de sesión con cuentas de Google en aplicaciones web y móviles.

## Autor

Naser Martinez — Ingeniería en Sistemas de Información, UMG Campus Jutiapa, 8vo ciclo (2026).
