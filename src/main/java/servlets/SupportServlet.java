package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DataStore;

import java.io.IOException;

public class SupportServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write(DataStore.getRandomSupportPhrase());
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newSupportPhrase = req.getReader().readLine();
        if ("text/plain".equals(req.getContentType())) {
            DataStore.addSupportPhrase(newSupportPhrase);
        } else if (req.getContentType() == null || newSupportPhrase == null || newSupportPhrase.isBlank()) {
            resp.sendError(HttpServletResponse.SC_LENGTH_REQUIRED);
        } else {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

    }
}
