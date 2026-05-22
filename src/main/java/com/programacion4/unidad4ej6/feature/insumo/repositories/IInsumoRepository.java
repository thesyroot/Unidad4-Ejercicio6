package com.programacion4.unidad4ej6.feature.insumo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.programacion4.unidad4ej6.feature.insumo.models.Insumo;

import java.util.Optional;
import java.util.List;

@Repository
public interface IInsumoRepository extends CrudRepository<Insumo, Long> {

    boolean existsByCodigoInterno(String codigoInterno);

    Optional<Insumo> findByIdAndActivoTrue(Long id);

    Optional<Insumo> findByIdAndActivoFalse(Long id);

    List<Insumo> findAllByActivoTrue();
    
}
