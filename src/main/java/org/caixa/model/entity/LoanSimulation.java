package org.caixa.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "SIMULACAO")
@Schema(description = "Representa a entidade de simulação de empréstimo.")
public class LoanSimulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SIMULACAO")
    @Schema(description = "Identificador único da simulação.", example = "20180702")
    private Long idSimulacao;

    @Column(name = "VR_DESEJADO", nullable = false)
    @Schema(description = "O valor em dinheiro que o cliente deseja solicitar.", example = "900.00", required = true)
    private BigDecimal valorDesejado;

    @Column(name = "PRAZO", nullable = false)
    @Schema(description = "O número de meses para o pagamento do empréstimo.", example = "5", required = true)
    private Integer prazo;

    @Column(name = "VR_TOTAL_PARCELAS", nullable = false)
    @Schema(description = "O valor total a ser pago, incluindo juros e taxas.", example = "1243.28", required = true)
    private BigDecimal valorTotalParcelas;
}
