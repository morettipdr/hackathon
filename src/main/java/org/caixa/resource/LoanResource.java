package org.caixa.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.caixa.model.dto.loan.LoanRequestDTO;
import org.caixa.service.LoanService;

@Path("/simulacao")
public class LoanResource {

    @Inject
    LoanService loanService;

    @POST
    public Response requestLoan(LoanRequestDTO request) {
        return Response.ok(loanService.simulateLoan(request)).build();
    }

    @GET
    public Response getAllLoans() {
        return Response.ok(loanService.getAllLoans()).build();
    }
}
