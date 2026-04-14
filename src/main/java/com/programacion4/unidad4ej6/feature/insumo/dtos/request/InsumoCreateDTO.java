package com.programacion4.unidad4ej6.feature.insumo.dtos.request;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsumoCreateDTO {

    @NotBlank(message = "El nombre es requerido")
    @Min(value = 3, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El código interno es requerido")
    @NotNull(message = "El código interno es requerido")
    @Pattern(regexp = "^LUMI-\\d{4}$", message = "El código interno debe seguir el formato LUMI-XXXX (donde X son números)")
    private String codigoInterno;

    @Positive(message = "El precio debe ser mayor a 0")
    @NotNull(message = "El precio es requerido")
    private BigDecimal precio;
}
