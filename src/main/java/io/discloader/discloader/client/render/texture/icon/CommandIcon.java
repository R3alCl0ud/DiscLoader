package io.discloader.discloader.client.render.texture.icon;

import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.client.registry.TextureRegistry;
import io.discloader.discloader.client.render.texture.AbstractTexture;
import io.discloader.discloader.client.render.util.IIcon;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 *
 */
public class CommandIcon extends AbstractTexture implements IIcon {

    public CommandIcon(Command command) {
        this.setIconName(command.getTextureName());
    }

    @Override
    public ImageIcon getImageIcon() {
        return this.createImageIcon(this.getIconName());
    }

    @Override
    public Image getImage() {
        return this.getImageIcon().getImage();
    }

    public String getNamespace() {
        return this.getIconName().substring(0, this.getIconName().indexOf(':'));
    }

    public String getNamespaceTexture() {
        return this.getIconName().substring(this.getIconName().indexOf(':') + 1);
    }

    public File getFile() {
        String path = String.format("assets/%s/texture/icon/commands/%s.png", this.getNamespace(), this.getNamespaceTexture().replace('.', '/'));
        if (TextureRegistry.resourceHandler.resources.containsKey(path)) {
            return TextureRegistry.resourceHandler.resources.get(path);
        }
        File file = new File(ClassLoader.getSystemResource(path).getFile());
        if (file.exists() && file.isFile()) {
            return file;
        }
        return DLUtil.MissingTexture;
    }

    protected ImageIcon createImageIcon(String path) {
        URL imgURL = null;
        try {
            imgURL = this.getFile().toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            imgURL = ClassLoader.getSystemResource(String.format("assets/%s/texture/icon/commands/%s.png", this.getNamespace(), this.getNamespaceTexture().replace('.', '/')));
        }
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            try {
                imgURL = DLUtil.MissingTexture.toURI().toURL();
                return new ImageIcon(imgURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
