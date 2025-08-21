package org.caixa.model.entity.loan;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "SIMULACAO")
@Schema(description = "Representa a entidade de simulação de empréstimo.")
public class LoanSimulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CO_SIMULACAO")
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

    @Column(name = "TS_INCLUSAO_SIMULACAO", nullable = false)
    @Schema(description = "Data da inclusão da simulação.", example = "2023-10-01", required = true)
    private LocalDate dataInclusaoSimulacao;

    @Column(name = "CO_PRODUTO", nullable = false)
    @Schema(description = "Código do produto associado à simulação.", example = "1", required = true)
    private Long codigoProduto;

    @Column(name = "TX_JURO", nullable = false, precision = 10, scale = 4)
    @Schema(description = "Taxa de juros aplicada na simulação.", example = "1.99", required = true)
    private BigDecimal taxaJuro;

    @Column(name = "IC_TIPO_SIMULACAO", nullable = false)
    @Schema(description = "Indicador do tipo de simulação (1 - SAC, 2 - PRICE).", example = "1", required = true)
    private Integer tipoSimulacao;
}
