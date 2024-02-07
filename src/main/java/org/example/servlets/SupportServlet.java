package org.example.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ApplicationContext;
import org.example.service.SupportService;

import java.io.IOException;

public class SupportServlet extends HttpServlet {
    private SupportService supportService;


    @Override
    public void init() {
        ApplicationContext context = new ApplicationContext();
        this.supportService = context.getInstance(SupportService.class);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write(supportService.getSupportPhrase());
    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String newSupportPhrase = req.getReader().readLine();
        if ("text/plain".equals(req.getContentType())) {
            supportService.addSupportPhrase(newSupportPhrase);
        } else if (req.getContentType() == null || newSupportPhrase == null || newSupportPhrase.isBlank()) {
            resp.sendError(HttpServletResponse.SC_LENGTH_REQUIRED);
        } else {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

    }

    public SupportService getSupportService() {
        return supportService;
    }

}
