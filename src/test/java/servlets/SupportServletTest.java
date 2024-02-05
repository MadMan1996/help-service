package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import util.DataStore;

import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyInt;


@ExtendWith(MockitoExtension.class)
class SupportServletTest {
    private SupportServlet supportServlet;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private HttpServletRequest req;
    @Mock
    private DataStore dataStore;

    @BeforeEach
    public void init() {
        this.supportServlet = new SupportServlet();
    }

    @Test
    public void doGetTest() throws ServletException, IOException {
        StringWriter respStringWriter = new StringWriter();
        PrintWriter respWriter = new PrintWriter(respStringWriter);
        String supportPhrase = "Test phrase";

        try(MockedStatic<DataStore> mockedDataStore = mockStatic(DataStore.class)){
            mockedDataStore.when(DataStore::getInstance).thenReturn(dataStore);
            when(dataStore.getRandomSupportPhrase()).thenReturn(supportPhrase);
            when(resp.getWriter()).thenReturn(respWriter);

            supportServlet.doGet(req, resp);

            verify(resp, times(1)).getWriter();
            assertEquals(supportPhrase, respStringWriter.toString());
        }
    }

    @Test
    public void doPostTest() throws ServletException, IOException {
        String newSupportPhrase = "Everything will be fine!";
        BufferedReader reqReader = new BufferedReader(new StringReader(newSupportPhrase));

        when(req.getContentType()).thenReturn("text/plain");
        when(req.getReader()).thenReturn(reqReader);

        Set<String> beforePost = DataStore.getInstance().getSupportPhrases();

        supportServlet.doPost(req, resp);

        Set<String> afterPost = DataStore.getInstance().getSupportPhrases();
        afterPost.removeAll(beforePost);

        verify(req, times(1)).getReader();
        assertEquals(1, afterPost.size());
        assertTrue(afterPost.contains(newSupportPhrase));
    }

    @Test
    public void doEmptyPostTest() throws ServletException, IOException {
        String newSupportPhrase = "";
        BufferedReader reqReader = new BufferedReader(new StringReader(newSupportPhrase));

        when(req.getContentType()).thenReturn(null);
        when(req.getReader()).thenReturn(reqReader);
        doNothing().when(resp).sendError(anyInt());

        supportServlet.doPost(req, resp);

        verify(resp, times(1)).sendError(HttpServletResponse.SC_LENGTH_REQUIRED);
    }

    @Test
    public void doPostWithWrongContentTypeTest() throws ServletException, IOException {
        String newSupportPhrase = "{ 'phrase' : 'new phrase'}";
        BufferedReader reqReader = new BufferedReader(new StringReader(newSupportPhrase));

        when(req.getContentType()).thenReturn("application/json");
        when(req.getReader()).thenReturn(reqReader);
        doNothing().when(resp).sendError(anyInt());

        supportServlet.doPost(req, resp);

        verify(resp, times(1)).sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
    }
}