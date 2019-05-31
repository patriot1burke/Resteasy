package org.resteasy.azure.functions;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/echo")
public class HelloWorldResource {
    @GET
    @Produces("text/plain")
    public String hello(@QueryParam("name") String name) {
        if (name == null) return "Please pass a name query parameter";
        return "Resteasy Hello " + name + "!";
    }

    @POST
    @Consumes("text/plain")
    public String postHello(String name) {
        return "Resteasy @POST hello " + name + "!";
    }
}
