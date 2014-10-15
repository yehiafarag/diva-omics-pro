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
        Computing comp = new Computing(se.getSession(),path);
        se.getSession().setAttribute("computing", comp);
        System.gc();

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

        HttpSession httpSession = se.getSession();
        String imgColorName = (String) httpSession.getAttribute("imgColorName");
        String lineChartImage = (String) httpSession.getAttribute("lineChartImage");
        String pcaChartImage = (String) httpSession.getAttribute("pcaChartImage");
        String hmImage = (String) httpSession.getAttribute("hmImage");
        System.out.println("attr is : " + imgColorName + "   " + lineChartImage + "   " + pcaChartImage + "  " + hmImage);
        String path = httpSession.getServletContext().getInitParameter("fileFolder");
        String textFile = httpSession.getServletContext().getInitParameter("textFile");
        File text = new File(se.getSession().getServletContext().getRealPath("/"), textFile + ".txt");
        File f1 = new File(path, imgColorName + ".png");
        if (f1.exists()) {
            f1.delete();
        }
        File f2 = new File(path, lineChartImage + ".png");
        if (f2.exists()) {
            f2.delete();
        }
        File f3 = new File(path, pcaChartImage + ".png");
        if (f3.exists()) {
            f3.delete();
        }
        File f4 = new File(path, hmImage + ".png");
        if (f4.exists()) {
            f4.delete();
        }
        if (text.exists()) {
            text.delete();
        }
        System.gc();

        System.out.println("good bye session :-)");

    }

}
