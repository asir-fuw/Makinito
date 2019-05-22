package fvarrui.makinito.ui.resources;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Icons {
	
	public static final Image LOGO_SMALL = loadIcon("cpu-32.png").getImage();
	public static final Image LOGO_BIG = loadIcon("cpu-256.png").getImage();
	public static final ImageIcon OPEN = loadIcon("open.gif");
	public static final ImageIcon PAUSE = loadIcon("pause-24.png");
	public static final ImageIcon PLAY = loadIcon("play-24.png");
	public static final ImageIcon STEP = loadIcon("step-24.png");
	public static final ImageIcon NEXT = loadIcon("next-24.png");
	public static final ImageIcon RELOAD = loadIcon("reload-24.png");

	public static ImageIcon loadIcon(String name) {
		return new ImageIcon(Icons.class.getResource("/icons/" + name));
	}
	
}
