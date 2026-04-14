package com.programacion4.unidad4ej6.feature.insumo.services.impl.domain;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import com.programacion4.unidad4ej6.feature.insumo.services.interfaces.domain.IInsumoDeleteService;
import com.programacion4.unidad4ej6.feature.insumo.services.interfaces.commons.IInsumoFindByIdService;
import com.programacion4.unidad4ej6.feature.insumo.repositories.IInsumoRepository;
import com.programacion4.unidad4ej6.feature.insumo.models.Insumo;
import com.programacion4.unidad4ej6.feature.insumo.mappers.InsumoMapper;
import com.programacion4.unidad4ej6.feature.insumo.dtos.response.InsumoResponseDTO;

@Service
@AllArgsConstructor
public class InsumoDeleteService implements IInsumoDeleteService {
    
    private final IInsumoFindByIdService insumoFindByIdService;

    private final IInsumoRepository insumoRepository;

    @Override
    public InsumoResponseDTO deleteInsumo(Long id) {
        
        Insumo insumo = insumoFindByIdService.findByIdAndActivoTrue(id);

        insumo.changeStatus();

        insumoRepository.save(insumo);

        return InsumoMapper.toResponseDTO(insumo);
    }
}
