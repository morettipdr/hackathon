package org.caixa.model.dto.health;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.jboss.resteasy.reactive.RestQuery;

import java.time.LocalDate;

@Getter
@Setter
public class VolumeRequestDTO {

    @NotNull
    @RestQuery
    private Long idProduto;

    @NotNull
    @RestQuery
    private LocalDate data;
}
