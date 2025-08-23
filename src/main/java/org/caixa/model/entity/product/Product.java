package org.caixa.model.entity.product;

import jakarta.persistence.*;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Entity
@Table(name = "PRODUTO")
@Data
@Schema(description = "Representa a entidade do produto")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CO_PRODUTO")
    @Schema(description = "Código do produto", example = "1")
    private Integer id;

    @Column(name = "NO_PRODUTO", nullable = false, length = 200)
    @Schema(description = "Nome do produto", example = "Produto 1")
    private String nome;

    @Column(name = "PC_TAXA_JUROS", nullable = false, precision = 10, scale = 9)
    @Schema(description = "Taxa de juros do produto", example = "0.05")
    private BigDecimal taxaJuros;

    @Column(name = "NU_MINIMO_MESES", nullable = false)
    @Schema(description = "Número mínimo de meses para o produto", example = "6")
    private Short minimoMeses;

    @Column(name = "NU_MAXIMO_MESES")
    @Schema(description = "Número máximo de meses para o produto", example = "24")
    private Short maximoMeses;

    @Column(name = "VR_MINIMO", nullable = false, precision = 18, scale = 2)
    @Schema(description = "Valor mínimo do produto", example = "1000.00")
    private BigDecimal valorMinimo;

    @Column(name = "VR_MAXIMO", precision = 18, scale = 2)
    @Schema(description = "Valor máximo do produto", example = "50000.00")
    private BigDecimal valorMaximo;
}
