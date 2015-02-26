/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.listeners;

import java.io.File;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Yehia Farag
 */
public class ContexListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
         System.gc();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        String path = sce.getServletContext().getInitParameter("fileFolder");
        File appFolder = new File(path);
        for (File f2 : appFolder.listFiles()) {
            if (f2.getName().endsWith(".png")) {
                f2.delete();
            }

        }
        String apppath = sce.getServletContext().getRealPath("/");
        File warFolder = new File(apppath);
        for (File f : warFolder.listFiles()) {
            if (f.getName().endsWith(".txt")) {
                f.delete();
            }
            else if(f.isDirectory()){
                cleanFolder(f);
                f.delete();             
        
        }

        }
        System.out.println("good bye :-)");
    }
    private void cleanFolder(File folder){
    for (File f : folder.listFiles()) {
                f.delete();
    }
    }
}
