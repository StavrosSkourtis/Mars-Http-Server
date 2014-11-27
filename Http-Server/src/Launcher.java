
import java.awt.AWTException;
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
        
        ServerOptions gui= new ServerOptions();
        HTTPServer server = new HTTPServer();
       
        // java 1.6 and above supports System Tray applications
        if(SystemTray.isSupported()){
            
            //Create the menu
            PopupMenu popup = new PopupMenu();

            
            //Create the Menu Items
            MenuItem restart =    new MenuItem("       Restart");
            MenuItem pauseStart = new MenuItem("       Pause");
            MenuItem log =        new MenuItem("       Log");
            MenuItem options =    new MenuItem("       Options");
            MenuItem about  =     new MenuItem("       About");
            MenuItem exit =       new MenuItem("       Exit");
            
            
            //Add Listeners
            // We need this one for double clicking on tray and when options is clicked
            // starts options gui
            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gui.setVisible(!gui.isVisible());
                }
            };
            options.addActionListener(listener);
            exit.addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Exits application
                    System.exit(0);
                }
            });
            
            // add menu items to the menu
            popup.add(pauseStart);
            popup.add(restart);
            popup.addSeparator();
            popup.add(log);
            popup.add(options);
            popup.addSeparator();
            popup.add(about);
            popup.addSeparator();
            popup.add(exit);
                   
            // get System tray
            SystemTray tray = SystemTray.getSystemTray();
            // get the tray icon's image
            Image image = Toolkit.getDefaultToolkit().getImage("img/icon.gif");
            
            BufferedImage trayIconImage = ImageIO.read(new FileInputStream("img/icon.png"));
            int trayIconWidth = new TrayIcon(trayIconImage).getSize().width;
 
            // create the tray icon
            TrayIcon icon = new TrayIcon(trayIconImage.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH), "HTTP Server", popup);
            // add the listener that start the options gui
            icon.addActionListener(listener);
            
            // add the tray icon to system tray
            try {
                tray.add(icon);
            } catch (AWTException e) {
                System.err.println(e);
            }
            
            // Start the server thread
            server.start();
        }
            
    }
}
