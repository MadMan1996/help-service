package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotation.Controller;
import org.example.annotation.RequestMapping;
import java.io.IOException;
@Controller
public interface SupportController {
    @RequestMapping(path = "/help-service/v1/support", methodType = "GET")
    void getSupport(HttpServletRequest req, HttpServletResponse res) throws IOException;

    @RequestMapping(path = "/help-service/v1/support", methodType = "POST")
    void addSupport(HttpServletRequest req, HttpServletResponse res) throws IOException;

}
