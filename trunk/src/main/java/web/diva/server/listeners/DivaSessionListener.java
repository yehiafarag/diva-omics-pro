/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.listeners;

import java.io.File;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import web.diva.server.model.Computing;

/**
 *
 * @author Yehia Farag
 */
public class DivaSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        
        String path = se.getSession().getServletContext().getInitParameter("fileFolder");
        Computing comp = new Computing(path);
        se.getSession().setAttribute("computing", comp);
        System.out.println("session start :-) ");
        System.gc();
       

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.gc();
        System.out.println("session end :-(");
    }

}
