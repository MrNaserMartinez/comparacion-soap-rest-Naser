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

\`\`\`
comparacion-soap-rest-Naser/
├── pom.xml
├── README.md
├── .gitignore
├── src/main/java/com/umg/
│   ├── soap/
│   │   ├── SumaServicio.java      # Lógica del servicio SOAP (JAX-WS, Code First)
│   │   └── PublicadorSoap.java    # Publica/arranca el servicio SOAP
│   └── rest/
│       └── ServidorRest.java      # Servicio REST con HttpServer del JDK
└── wsdl/
    └── servicio.wsdl              # Copia de referencia del WSDL generado
\`\`\`

## Cómo ejecutar el proyecto

1. Clonar el repositorio y abrirlo en IntelliJ IDEA como proyecto Maven (o `mvn install` desde consola).
2. Verificar que el proyecto usa JDK 25.

### Levantar el servicio SOAP
Correr la clase `com.umg.soap.PublicadorSoap` (clic derecho → Run).
Queda escuchando en:
\`\`\`
http://localhost:8081/ws/suma
\`\`\`

### Levantar el servicio REST
Correr la clase `com.umg.rest.ServidorRest` (clic derecho → Run).
Queda escuchando en:
\`\`\`
http://localhost:8082/suma
\`\`\`

> Ambos servicios pueden correr al mismo tiempo (usan puertos distintos) o por separado.

## Cómo consumir el servicio SOAP

1. Con `PublicadorSoap` corriendo, el WSDL está disponible en:
   \`\`\`
   http://localhost:8081/ws/suma?wsdl
   \`\`\`
2. Probado con **SoapUI**:
   - `File → New SOAP Project` → pegar la URL del WSDL de arriba.
   - Abrir `sumar → Request 1`, completar los valores de `a` y `b` en el XML de la petición.
   - Enviar y ver la respuesta.

Ejemplo de petición SOAP enviada:
\`\`\`xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://soap.umg.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:sumar>
         <a>5</a>
         <b>3</b>
      </soap:sumar>
   </soapenv:Body>
</soapenv:Envelope>
\`\`\`

Respuesta recibida:
\`\`\`xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <ns2:sumarResponse xmlns:ns2="http://soap.umg.com/">
         <return>8</return>
      </ns2:sumarResponse>
   </S:Body>
</S:Envelope>
\`\`\`

Imagen de referencia en funcionamiento
<img width="956" height="555" alt="Captura de pantalla 2026-07-24 101110" src="https://github.com/user-attachments/assets/8c19aec5-5ef3-4dc8-823f-c5af114038c8" />


## Cómo consumir el servicio REST

Con `ServidorRest` corriendo, probado con `curl`:

\`\`\`bash
curl -X POST http://localhost:8082/suma -H "Content-Type: application/json" -d "{\"a\": 5, \"b\": 3}"
\`\`\`

Respuesta exitosa (`200 OK`):
\`\`\`json
{"resultado":8}
\`\`\`

Si se envían datos incompletos o inválidos (`400 Bad Request`):
\`\`\`json
{"error":"Datos inválidos, se esperaba {\"a\":numero,\"b\":numero}"}
\`\`\`

Si se usa un método distinto a `POST` (`405 Method Not Allowed`):
\`\`\`json
{"error":"Método no permitido, usa POST"}
\`\`\`


imagen de referencia:
<img width="856" height="253" alt="image" src="https://github.com/user-attachments/assets/c4eb9df8-112c-46e4-b6c0-f5975639d379" />
