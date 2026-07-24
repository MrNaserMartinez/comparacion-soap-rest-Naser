package com.umg.soap;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService
public class SumaServicio {
    @WebMethod
    public int sumar(@WebParam(name = "a") int a, @WebParam(name = "b") int b) {
        return a + b;
    }
}