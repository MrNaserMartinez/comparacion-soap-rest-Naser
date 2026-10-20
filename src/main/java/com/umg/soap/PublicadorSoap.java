package com.umg.soap;

import jakarta.xml.ws.Endpoint;

public class PublicadorSoap {

    public static void main(String[] args) {
        String url = "http://localhost:8081/ws/suma";

        Endpoint.publish(url, new SumaServicio());

            //Hola soy naser
        System.out.println("Servicio SOAP corriendo en: " + url);
        System.out.println("WSDL disponible en: " + url + "?wsdl");
    }
}