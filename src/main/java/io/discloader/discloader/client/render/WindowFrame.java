package io.discloader.discloader.client.render;

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import io.discloader.discloader.client.render.panel.LoadingPanel;
import io.discloader.discloader.common.DiscLoader;

/**
 * 
 * 
 * @author Perry Berman
 * 
 */
public class WindowFrame extends JFrame {
    private static final long serialVersionUID = -6329873205165995031L;
    public DiscLoader loader;

    public static final LoadingPanel loading = new LoadingPanel();

    protected ImageIcon createImageIcon(String path, String description) {
        URL imgURL = ClassLoader.getSystemResource(String.format("assets/discloader/%s.png", path.replace('.', '/')));
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Creates a new WindowFrame
     * 
     * @param loader The current instance of DiscLoader
     */
    public WindowFrame(DiscLoader loader) {
        this.loader = loader;
        this.setSize(960, 600);
        this.add(loading);
        this.setIconImage(createImageIcon("texture.gui.logos.floppy_disc", "icon").getImage());
        this.setTitle("DiscLoader");
        this.setBackground(new Color(0x2C2F33));
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowListener() {

            @Override
            public void windowActivated(WindowEvent arg0) {

            }

            @Override
            public void windowClosed(WindowEvent arg0) {
                System.out.println("Window Closed");
            }

            @Override
            public void windowClosing(WindowEvent arg0) {
                System.out.println("Window Closing");

            }

            @Override
            public void windowDeactivated(WindowEvent arg0) {

            }

            @Override
            public void windowDeiconified(WindowEvent arg0) {

            }

            @Override
            public void windowIconified(WindowEvent arg0) {

            }

            @Override
            public void windowOpened(WindowEvent arg0) {
                LoadingPanel.phasePanel.setBarColor();
                LoadingPanel.stagePanel.setBarColor();
                LoadingPanel.stepPanel.setBarColor();
                LoadingPanel.progressPanel.setBarColor();
                loading.repaint();
            }

        });
    }

}
