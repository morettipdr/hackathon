package org.caixa.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.caixa.model.dto.health.TelemetryDTO;
import org.caixa.model.dto.health.VolumeDTO;

@Path("/health")
public class HealthResource {

    @GET
    @Path("/volume")
    public VolumeDTO getVolume() {
        VolumeDTO volume = new VolumeDTO();
        return volume;
    }

    @GET
    @Path("/telemetria")
    public TelemetryDTO getTelemetry() {
        TelemetryDTO telemetry = new TelemetryDTO();
        return telemetry;
    }

}
