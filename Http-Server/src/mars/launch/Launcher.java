package mars.launch;


import mars.gui.ServerOptions;
import mars.http.HTTPServer;
import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.scene.chart.PieChart;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import mars.cli.CommandLineInterface;
import mars.gui.GuiLauncher;
import mars.utils.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Stavros Skourtis
 */
public class Launcher {
    public static void main(String args[]) throws IOException{
        HTTPServer server = new HTTPServer();
        server.start();
        Logger.init();
        
        if (GraphicsEnvironment.isHeadless()) {
            CommandLineInterface.run();
        } else {
            new GuiLauncher();
        }
        
        
    }
}
