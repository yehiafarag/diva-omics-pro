/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.listeners;

import java.io.File;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import web.diva.server.model.Computing;

/**
 *
 * @author Yehia Farag
 */
public class DivaSessionListener implements HttpSessionListener {

    private Computing comp;
    @Override
    public void sessionCreated(HttpSessionEvent se) {        
        String path = se.getSession().getServletContext().getInitParameter("fileFolder");
        comp = new Computing(path);
        se.getSession().setAttribute("computing", comp);
        System.out.println("session start :-) ");
        System.gc();
       

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {        
         String path = se.getSession().getServletContext().getRealPath("/");
         File userFolder = new File(path, se.getSession().getId());
         if (userFolder.exists()) {
            for (File f : userFolder.listFiles()) {
                f.delete();
                System.out.println("remove " + f);
            }

            boolean test = userFolder.delete();
            System.err.println("file deleted " + test);
        }
        System.gc();
        System.out.println("session end :-(");
    }

}
