package com.thecookiezen.hasingleton.boundary;

import com.thecookiezen.hasingleton.control.HAServiceActivator;
import com.thecookiezen.hasingleton.control.ServiceRegistryWrapper;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceController;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/healthcheck")
public class HealthCheckResource {

    private static final String UNDEFINED = "undefined";
    private String nodeName = UNDEFINED;

    @PostConstruct
    public void init() {
        nodeName = System.getProperty("jboss.node.name");
    }

    @GET
    public Response check() {
        final ServiceController<?> requiredService = ServiceRegistryWrapper.getServiceRegistry()
                .getRequiredService(HAServiceActivator.SINGLETON_SERVICE_NAME);
        final Service<?> service = requiredService.getService();
        final String masterNodeName = (String) service.getValue();
        return nodeName.equals(masterNodeName) ? Response.ok().build() : Response.status(Response.Status.GONE).build();
    }
}
