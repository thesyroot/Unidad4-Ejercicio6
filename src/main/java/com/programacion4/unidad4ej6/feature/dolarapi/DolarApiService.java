package com.programacion4.unidad4ej6.feature.dolarapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class DolarApiService {

    private static final String API_URL = "https://dolarapi.com/v1/dolares/oficial";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DolarApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public DolarApiService(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public Double getDolarOficialVenta() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error al consultar API de dolar: HTTP " + response.statusCode());
            }

            DolarApiResponse dolarResponse = objectMapper.readValue(response.body(), DolarApiResponse.class);
            return dolarResponse.getVenta();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener cotizacion del dolar oficial: " + e.getMessage(), e);
        }
    }
}
