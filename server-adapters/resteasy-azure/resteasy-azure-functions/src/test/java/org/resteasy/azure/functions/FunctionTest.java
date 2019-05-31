package org.resteasy.azure.functions;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Unit test for Function class.
 */
public class FunctionTest {
    /**
     * Unit test for HttpTriggerJava method.
     */
    @Test
    public void testHttpTriggerJava() throws Exception {
        // Setup
        @SuppressWarnings("unchecked")
        final HttpRequestMessage<Optional<byte[]>> req = Mockito.mock(HttpRequestMessage.class);

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", "Azure");
        Mockito.doReturn(queryParams).when(req).getQueryParameters();
        Mockito.doReturn(URI.create("http://foo.com/api/echo?name=Bill")).when(req).getUri();
        Mockito.doReturn(HttpMethod.GET).when(req).getHttpMethod();
        Mockito.doReturn(new HashMap<String, String>()).when(req).getHeaders();
        Mockito.doReturn(Optional.ofNullable((byte[])null)).when(req).getBody();

        final Optional<String> queryBody = Optional.empty();
        Mockito.doReturn(queryBody).when(req).getBody();

        Mockito.doAnswer(new Answer<HttpResponseMessage.Builder>() {
            @Override
            public HttpResponseMessage.Builder answer(InvocationOnMock invocation) {
                HttpStatus status = (HttpStatus) invocation.getArguments()[0];
                return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
            }
        }).when(req).createResponseBuilder(Mockito.any(HttpStatus.class));

        final ExecutionContext context = Mockito.mock(ExecutionContext.class);
        Mockito.doReturn(Logger.getGlobal()).when(context).getLogger();

        // Invoke
        final HttpResponseMessage ret = new Function().run(req, context);

        // Verify
        assertEquals(ret.getStatus(), HttpStatus.OK);
        //assertEquals("Hello Bill!", new String((byte[])ret.getBody()));
    }
}
