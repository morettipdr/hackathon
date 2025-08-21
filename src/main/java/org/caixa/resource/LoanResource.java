package org.caixa.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.caixa.model.dto.loan.LoanFilterDTO;
import org.caixa.model.dto.loan.LoanRequestDTO;
import org.caixa.model.dto.loan.LoanResponseDTO;
import org.caixa.model.dto.loan.LoanSummaryDTO;
import org.caixa.service.LoanService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/simulacao")
public class LoanResource {

    @Inject
    LoanService loanService;

    @POST
    @Operation(
        summary = "Solicita uma nova simulação de empréstimo",
        description = "Envia os dados para um novo empréstimo e retorna o resultado da simulação, como o valor das parcelas e o total a ser pago."
    )
    @RequestBody(
        description = "Objeto contendo os dados necessários para a simulação.",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = LoanRequestDTO.class)
        )
    )
    @APIResponse(
        responseCode = "200",
        description = "Simulação calculada com sucesso.",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = LoanResponseDTO.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Requisição inválida. Ocorre quando o corpo da requisição é nulo ou algum campo obrigatório não foi preenchido corretamente."
    )
    @APIResponse(
            responseCode = "500",
            description = "Erro interno no servidor."
    )

    public Response requestLoan(@NotNull(message = "Corpo de entrada não pode ser nulo") @Valid LoanRequestDTO request) {
        return Response.ok(loanService.simulateLoan(request)).build();
    }

    @GET
        @Operation(
        summary = "Lista todas as simulações de empréstimo",
        description = "Retorna uma lista com todas as simulações de empréstimo que foram realizadas e armazenadas."
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de simulações retornada com sucesso.",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(type = SchemaType.ARRAY, implementation = LoanSummaryDTO.class)
        )
    )
    @APIResponse(
        responseCode = "500",
        description = "Erro interno no servidor."
    )
    public Response getAllLoans(@BeanParam @Valid LoanFilterDTO loanFilterDTO) {
        return Response.ok(loanService.getAllLoanSimulations(loanFilterDTO)).build();
    }
}
