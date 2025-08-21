package org.caixa.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LoanType {
    SAC(1, "SAC"),
    PRICE(2, "PRICE");

    private Integer id;
    private String description;
}
