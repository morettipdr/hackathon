package org.caixa.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EndpointType {

    SIMULACAO("/simulacao"),
    VOLUME("/health/volume"),
    TELEMETRIA("/health/telemetria");

    private String descricao;
}
