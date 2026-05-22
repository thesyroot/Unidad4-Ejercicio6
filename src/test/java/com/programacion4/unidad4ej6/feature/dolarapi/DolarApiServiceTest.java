package com.programacion4.unidad4ej6.feature.dolarapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DolarApiServiceTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    private ObjectMapper objectMapper;
    private DolarApiService dolarApiService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        dolarApiService = new DolarApiService(httpClient, objectMapper);
    }

    @Test
    void getDolarOficialVenta_shouldReturnVentaValue_whenApiCallSuccessful() throws Exception {
        String jsonResponse = """
                {
                    "moneda": "USD",
                    "casa": "oficial",
                    "nombre": "Oficial",
                    "compra": 1375.0,
                    "venta": 1425.0,
                    "fechaActualizacion": "2026-05-22T17:02:00.000Z"
                }
                """;

        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(jsonResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        Double resultado = dolarApiService.getDolarOficialVenta();

        assertNotNull(resultado);
        assertEquals(1425.0, resultado, 0.001);
        verify(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    void getDolarOficialVenta_shouldThrowException_whenApiReturnsNon200() throws Exception {
        when(httpResponse.statusCode()).thenReturn(500);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        assertThrows(RuntimeException.class, () -> dolarApiService.getDolarOficialVenta());
    }

    @Test
    void getDolarOficialVenta_shouldThrowException_whenHttpClientFails() throws Exception {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new RuntimeException("Connection timeout"));

        assertThrows(RuntimeException.class, () -> dolarApiService.getDolarOficialVenta());
    }
}
