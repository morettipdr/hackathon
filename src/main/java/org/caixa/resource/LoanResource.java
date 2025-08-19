package org.caixa.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.caixa.model.dto.LoanRequestDTO;
import org.caixa.service.LoanService;

@Path("/loan")
public class LoanResource {

    @Inject
    LoanService loanService;

    @POST
    public Response requestLoan(LoanRequestDTO request) {
        return Response.ok(loanService.simulateLoan(request)).build();
    }
}
