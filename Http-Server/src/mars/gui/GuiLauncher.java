/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mars.gui;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import mars.http.HTTPServer;
import mars.utils.Logger;

/**
 *
 * @author Phenom
 */
public class GuiLauncher {
    
    public GuiLauncher() throws IOException {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(About.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(About.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(About.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(About.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        final ServerOptions gui = new ServerOptions();
        final About guiAbout = new About();
        // java 1.6 and above supports System Tray applications
        if (SystemTray.isSupported()) {

            //Create the menu
            PopupMenu popup = new PopupMenu();
            Logger.init();

            //Create the Menu Items
            final MenuItem log = new MenuItem("       Log");
            final MenuItem options = new MenuItem("       Control Panel");
            final MenuItem about = new MenuItem("       About");
            final MenuItem exit = new MenuItem("       Exit");
            
            ActionListener listener = new ActionListener() {
                
                public void actionPerformed(ActionEvent e) {
                    gui.setVisible(!gui.isVisible());
                }
            };
            
            options.addActionListener(listener);
            
            about.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    guiAbout.setVisible(!guiAbout.isVisible());
                }
            });
            
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Exits application
                    Logger.dispose();
                    System.exit(0);
                }
            });
            
            log.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Desktop.getDesktop().edit(new File("log.txt"));
                    } catch (IOException ee) {
                        
                    }
                }
            });

            // add menu items to the menu
            popup.add(log);
            popup.add(options);
            popup.addSeparator();
            popup.add(about);
            popup.addSeparator();
            popup.add(exit);

            // get System tray
            SystemTray tray = SystemTray.getSystemTray();
            // get the tray icon's image
            Image image = Toolkit.getDefaultToolkit().getImage("img/icon.jpg");
            
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
            
        }
    }
}
