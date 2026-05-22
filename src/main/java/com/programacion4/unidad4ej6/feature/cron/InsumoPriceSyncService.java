package com.programacion4.unidad4ej6.feature.cron;

import com.programacion4.unidad4ej6.feature.dolarapi.DolarApiService;
import com.programacion4.unidad4ej6.feature.insumo.models.Insumo;
import com.programacion4.unidad4ej6.feature.insumo.repositories.IInsumoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InsumoPriceSyncService {

    private static final Logger log = LoggerFactory.getLogger(InsumoPriceSyncService.class);
    private static final double DELTA = 0.001;

    private final IInsumoRepository insumoRepository;
    private final DolarApiService dolarApiService;

    public InsumoPriceSyncService(IInsumoRepository insumoRepository, DolarApiService dolarApiService) {
        this.insumoRepository = insumoRepository;
        this.dolarApiService = dolarApiService;
    }

    @Scheduled(cron = "0 0 * * * 1-5")
    public void syncPrices() {
        log.info("Iniciando sincronizacion de precios de insumos con dolar oficial");

        try {
            Double dolarOficial = dolarApiService.getDolarOficialVenta();
            log.info("Dolar oficial obtenido: {} (venta)", dolarOficial);

            List<Insumo> insumos = insumoRepository.findAllByActivoTrue();
            int actualizados = 0;

            for (Insumo insumo : insumos) {
                if (Math.abs(insumo.getValorDolarReferencia() - dolarOficial) > DELTA) {
                    insumo.setValorDolarReferencia(dolarOficial);
                    insumo.setPrecioEnPesos(insumo.getPrecioEnDolares() * dolarOficial);
                    insumoRepository.save(insumo);
                    actualizados++;
                    log.info("Insumo {} actualizado: nuevo precioEnPesos = {}", insumo.getId(), insumo.getPrecioEnPesos());
                }
            }

            log.info("Sincronizacion completada. {} insumos actualizados de {}", actualizados, insumos.size());
        } catch (Exception e) {
            log.error("Error en sincronizacion de precios: {}", e.getMessage(), e);
        }
    }
}
