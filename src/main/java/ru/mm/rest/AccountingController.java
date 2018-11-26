package ru.mm.rest;

import ru.mm.common.Account;
import ru.mm.exception.AccountException;
import ru.mm.service.AccountService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/accounting")
public class AccountingController {

    private AccountService accountService;

    @Inject
    public AccountingController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Get account by id
     *
     * @param id long
     * @return account or error
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id) {
        try {
            return Response.ok(accountService.get(id)).build();
        } catch (SQLException | AccountException e) {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN_TYPE).entity(e.getMessage()).build();
        }

    }

    /**
     * Create an account
     *
     * @param account account entity (only balance will be the same)
     * @return created account in xml or error
     */
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Account account) {
        try {
            return Response.ok(accountService.create(account)).build();
        } catch (SQLException | AccountException e) {
            return Response.serverError().type(MediaType.TEXT_HTML_TYPE).entity(e.getMessage()).build();
        }
    }

    /**
     * Money transfer between accounts
     *
     * @param sourceId source account id (from)
     * @param targetId target account id (to)
     * @param amount   amount of money to transfer
     * @return response entity ok or error
     */
    @PUT
    @Path("/{sourceId}/{targetId}/{amount}")
    @Produces(MediaType.TEXT_HTML)
    public Response transfer(@PathParam("sourceId") Long sourceId,
                             @PathParam("targetId") Long targetId,
                             @PathParam("amount") Long amount) {
        try {
            accountService.transfer(sourceId, targetId, amount);
            return Response.ok().build();
        } catch (SQLException | AccountException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }
}
