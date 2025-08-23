package org.caixa.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.caixa.model.dto.health.VolumeRequestDTO;
import org.caixa.service.HealthService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/health")
@Tag(name = "Saúde da Aplicação", description = "Endpoints para verificação de saúde e métricas da aplicação.")
public class HealthResource {

    @Inject
    HealthService healthService;

    @GET
    @Path("/volume")
    @Operation(
        summary = "Consulta o volume de transações",
        description = "Retorna a contagem de transações com base nos filtros de sistema e período informados."
    )
    @APIResponse(
        responseCode = "200",
        description = "Volume de transações retornado com sucesso.",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Object.class)) // Substitua 'Object.class' pela sua classe de resposta
    )
    @APIResponse(
        responseCode = "400",
        description = "Parâmetros inválidos.",
        content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getVolume(@BeanParam @Valid VolumeRequestDTO volumeRequestDTO) {
        return Response.ok().entity(healthService.getVolume(volumeRequestDTO)).build();
    }

    @GET
    @Path("/telemetria")
    @Operation(
        summary = "Consulta dados de telemetria",
        description = "Retorna informações de telemetria e métricas gerais da aplicação, como status de serviços externos e uso de recursos."
    )
    @APIResponse(
        responseCode = "200",
        description = "Dados de telemetria retornados com sucesso.",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Object.class)) // Substitua 'Object.class' pela sua classe de resposta
    )
    @APIResponse(
        responseCode = "500",
        description = "Erro interno ao buscar os dados de telemetria.",
        content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getTelemetry() {
        return Response.ok().entity(healthService.getTelemetry()).build();
    }
}