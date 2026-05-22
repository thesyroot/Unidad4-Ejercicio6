package com.programacion4.unidad4ej6.feature.cron;

import com.programacion4.unidad4ej6.feature.dolarapi.DolarApiService;
import com.programacion4.unidad4ej6.feature.insumo.models.Insumo;
import com.programacion4.unidad4ej6.feature.insumo.repositories.IInsumoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsumoPriceSyncServiceTest {

    @Mock
    private IInsumoRepository insumoRepository;

    @Mock
    private DolarApiService dolarApiService;

    @Captor
    private ArgumentCaptor<Insumo> insumoCaptor;

    private InsumoPriceSyncService syncService;

    @BeforeEach
    void setUp() {
        syncService = new InsumoPriceSyncService(insumoRepository, dolarApiService);
    }

    @Test
    void syncPrices_shouldUpdateInsumos_whenDolarDiffers() {
        Insumo insumo1 = Insumo.builder()
                .id(1L)
                .nombre("Insumo A")
                .codigoInterno("LUMI-0001")
                .precioEnDolares(100.0)
                .valorDolarReferencia(1400.0)
                .precioEnPesos(140000.0)
                .activo(true)
                .stockActual(10L)
                .build();

        Insumo insumo2 = Insumo.builder()
                .id(2L)
                .nombre("Insumo B")
                .codigoInterno("LUMI-0002")
                .precioEnDolares(50.0)
                .valorDolarReferencia(1400.0)
                .precioEnPesos(70000.0)
                .activo(true)
                .stockActual(5L)
                .build();

        when(dolarApiService.getDolarOficialVenta()).thenReturn(1425.0);
        when(insumoRepository.findAllByActivoTrue()).thenReturn(List.of(insumo1, insumo2));

        syncService.syncPrices();

        verify(insumoRepository, times(2)).save(insumoCaptor.capture());
        List<Insumo> savedInsumos = insumoCaptor.getAllValues();

        Insumo saved1 = savedInsumos.get(0);
        assertEquals(1425.0, saved1.getValorDolarReferencia(), 0.001);
        assertEquals(142500.0, saved1.getPrecioEnPesos(), 0.001);

        Insumo saved2 = savedInsumos.get(1);
        assertEquals(1425.0, saved2.getValorDolarReferencia(), 0.001);
        assertEquals(71250.0, saved2.getPrecioEnPesos(), 0.001);
    }

    @Test
    void syncPrices_shouldNotUpdateInsumos_whenDolarIsSame() {
        Insumo insumo = Insumo.builder()
                .id(1L)
                .nombre("Insumo A")
                .codigoInterno("LUMI-0001")
                .precioEnDolares(100.0)
                .valorDolarReferencia(1425.0)
                .precioEnPesos(142500.0)
                .activo(true)
                .stockActual(10L)
                .build();

        when(dolarApiService.getDolarOficialVenta()).thenReturn(1425.0);
        when(insumoRepository.findAllByActivoTrue()).thenReturn(List.of(insumo));

        syncService.syncPrices();

        verify(insumoRepository, never()).save(any());
    }

    @Test
    void syncPrices_shouldHandleEmptyInsumoList() {
        when(dolarApiService.getDolarOficialVenta()).thenReturn(1425.0);
        when(insumoRepository.findAllByActivoTrue()).thenReturn(List.of());

        syncService.syncPrices();

        verify(insumoRepository, never()).save(any());
    }

    @Test
    void syncPrices_shouldHandleApiErrorGracefully() {
        when(dolarApiService.getDolarOficialVenta())
                .thenThrow(new RuntimeException("API error"));

        syncService.syncPrices();

        verify(insumoRepository, never()).save(any());
    }

    @Test
    void syncPrices_shouldOnlyUpdateInsumosWithDifferentDolarValue() {
        Insumo insumo1 = Insumo.builder()
                .id(1L)
                .nombre("Insumo A")
                .codigoInterno("LUMI-0001")
                .precioEnDolares(100.0)
                .valorDolarReferencia(1400.0)
                .precioEnPesos(140000.0)
                .activo(true)
                .stockActual(10L)
                .build();

        Insumo insumo2 = Insumo.builder()
                .id(2L)
                .nombre("Insumo B")
                .codigoInterno("LUMI-0002")
                .precioEnDolares(50.0)
                .valorDolarReferencia(1425.0)
                .precioEnPesos(71250.0)
                .activo(true)
                .stockActual(5L)
                .build();

        when(dolarApiService.getDolarOficialVenta()).thenReturn(1425.0);
        when(insumoRepository.findAllByActivoTrue()).thenReturn(List.of(insumo1, insumo2));

        syncService.syncPrices();

        verify(insumoRepository, times(1)).save(any());
    }
}
