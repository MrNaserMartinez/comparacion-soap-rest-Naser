package com.umg.rest;

import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ServidorRest {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);

        server.createContext("/suma", exchange -> {

            // Solo aceptamos POST; cualquier otro método es un error 405
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                enviarRespuesta(exchange, 405, "{\"error\":\"Método no permitido, usa POST\"}");
                return;
            }

            try {
                // 1. Leer el cuerpo de la petición (viene como bytes)
                InputStream is = exchange.getRequestBody();
                String cuerpo = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                // 2. Convertir el texto JSON en un objeto que podamos leer
                JSONObject json = new JSONObject(cuerpo);
                int a = json.getInt("a");
                int b = json.getInt("b");

                // 3. Procesar (la misma operación que en SOAP)
                int resultado = a + b;

                // 4. Armar la respuesta en JSON
                JSONObject respuesta = new JSONObject();
                respuesta.put("resultado", resultado);

                enviarRespuesta(exchange, 200, respuesta.toString());

            } catch (Exception e) {
                // Si el JSON viene mal formado o faltan datos, respondemos 400
                enviarRespuesta(exchange, 400, "{\"error\":\"Datos inválidos, se esperaba {\\\"a\\\":numero,\\\"b\\\":numero}\"}");
            }
        });

        server.start();
        System.out.println("Servicio REST corriendo en: http://localhost:8082/suma");
    }

    private static void enviarRespuesta(com.sun.net.httpserver.HttpExchange exchange, int codigoHttp, String cuerpoJson) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = cuerpoJson.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(codigoHttp, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}