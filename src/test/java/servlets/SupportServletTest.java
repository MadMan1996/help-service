package servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.SupportService;
import org.example.servlets.SupportServlet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyInt;


@ExtendWith(MockitoExtension.class)
class SupportServletTest {

    @Mock
    private HttpServletResponse resp;
    @Mock
    private HttpServletRequest req;

    @Mock
    private SupportService supportService;
    @InjectMocks
    private SupportServlet supportServlet;

    @Test
    public void do_get_should_write_support_phrase_to_response() throws IOException {
        StringWriter respStringWriter = new StringWriter();
        PrintWriter respWriter = new PrintWriter(respStringWriter);
        String supportPhrase = "Test phrase";


        when(supportService.getSupportPhrase()).thenReturn(supportPhrase);
        when(resp.getWriter()).thenReturn(respWriter);

        supportServlet.doGet(req, resp);

        verify(resp, times(1)).getWriter();
        assertEquals(supportPhrase, respStringWriter.toString());

    }

    @Test
    public void do_post_should_add_new_phrase_from_req() throws IOException {
        String newSupportPhrase = "Everything will be fine!";
        BufferedReader reqReader = new BufferedReader(new StringReader(newSupportPhrase));

        when(req.getContentType()).thenReturn("text/plain");
        when(req.getReader()).thenReturn(reqReader);
        doNothing().when(supportService).addSupportPhrase(newSupportPhrase);

        supportServlet.doPost(req, resp);

        verify(req, times(1)).getReader();
        verify(supportService, times(1)).addSupportPhrase(newSupportPhrase);
    }

    @Test
    public void do_empty_post_should_rsponse_wtih_error() throws IOException {
        String newSupportPhrase = "";
        BufferedReader reqReader = new BufferedReader(new StringReader(newSupportPhrase));

        when(req.getContentType()).thenReturn(null);
        when(req.getReader()).thenReturn(reqReader);
        doNothing().when(resp).sendError(anyInt());

        supportServlet.doPost(req, resp);

        verify(resp, times(1)).sendError(HttpServletResponse.SC_LENGTH_REQUIRED);
    }

    @Test
    public void do_post_with_wrong_content_type_should_resopnse_wtih_error() throws IOException {
        String newSupportPhrase = "{ 'phrase' : 'new phrase'}";
        BufferedReader reqReader = new BufferedReader(new StringReader(newSupportPhrase));

        when(req.getContentType()).thenReturn("application/json");
        when(req.getReader()).thenReturn(reqReader);
        doNothing().when(resp).sendError(anyInt());

        supportServlet.doPost(req, resp);

        verify(resp, times(1)).sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
    }

    @Test
    void init_should_initialize_support_service() {
        SupportServlet servlet = new SupportServlet();

        servlet.init();

        assertNotNull(servlet.getSupportService());

    }

}