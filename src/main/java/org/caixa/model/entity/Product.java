package org.caixa.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "PRODUTO")
@Data
public class Product {

    @Id
    @Column(name = "CO_PRODUTO")
    private Long codigo;

    @Column(name = "NO_PRODUTO", nullable = false, length = 200)
    private String nome;

    @Column(name = "PC_TAXA_JUROS", nullable = false, precision = 10, scale = 9)
    private BigDecimal taxaJuros;

    @Column(name = "NU_MINIMO_MESES", nullable = false)
    private Short minimoMeses;

    @Column(name = "NU_MAXIMO_MESES")
    private Short maximoMeses;

    @Column(name = "VR_MINIMO", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorMinimo;

    @Column(name = "VR_MAXIMO", precision = 18, scale = 2)
    private BigDecimal valorMaximo;
}
