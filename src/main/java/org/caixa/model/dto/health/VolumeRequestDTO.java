package org.caixa.model.dto.health;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.RestQuery;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Entrada para o endpoint de volume")
public class VolumeRequestDTO {

    @NotNull
    @RestQuery
    @Schema(description = "Id do produto que se deseja visualizar o volume")
    private Long idProduto;

    @NotNull
    @RestQuery
    @Schema(description = "Data desejada para visualizar o volume")
    private LocalDate data;
}
