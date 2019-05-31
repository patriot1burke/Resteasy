package org.resteasy.azure.functions;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.specimpl.ResteasyUriInfo;
import org.jboss.resteasy.spi.ResteasyDeployment;

import javax.ws.rs.ext.RuntimeDelegate;
import java.util.Optional;

public class Function {
    protected static ResteasyDeployment deployment;

    static {
        deployment = new ResteasyDeploymentImpl();
        deployment.start();
        deployment.getRegistry().addPerRequestResource(HelloWorldResource.class);
    }

    public HttpResponseMessage run(
            @HttpTrigger(name="req") HttpRequestMessage<Optional<byte[]>> request,
            final ExecutionContext context) {
        return resteasyDispatch(request);
    }

    protected HttpResponseMessage resteasyDispatch(HttpRequestMessage<Optional<byte[]>> request) {
        MockHttpResponse httpResponse = new MockHttpResponse();
        ResteasyUriInfo uriInfo = new ResteasyUriInfo(request.getUri().toString(), request.getUri().getRawQuery(), "/api");
        AzureHttpRequest httpRequest = new AzureHttpRequest(uriInfo, (SynchronousDispatcher)deployment.getDispatcher(), httpResponse, request);
        deployment.getDispatcher().invoke(httpRequest, httpResponse);
        HttpResponseMessage.Builder responseBuilder = request.createResponseBuilder(HttpStatus.valueOf(httpResponse.getStatus()));
        httpResponse.getOutputHeaders().forEach((name, values) -> {
            values.forEach(o -> {
                RuntimeDelegate.HeaderDelegate delegate = deployment.getProviderFactory().getHeaderDelegate(o.getClass());
                if (delegate != null)
                {
                    responseBuilder.header(name, delegate.toString(o));
                }
                else
                {
                    responseBuilder.header(name, o.toString());
                }
            });
        });
        responseBuilder.body(httpResponse.getOutput());
        return responseBuilder.build();
    }
}
