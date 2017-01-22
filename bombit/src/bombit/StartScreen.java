package bombit;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *	A kezdõképenyõ megjelenítéséért felelõs osztály.
 */
public class StartScreen extends JComponent{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 720;
	private static final int HEIGHT = 600;
	
	private static BufferedImage startscreen;
	
	/**
	 *	Beállítja a kezdõképernyõt.
	 */
	StartScreen(){
		setSize(WIDTH, HEIGHT);
		setFocusable(true);
		repaint();
	}
	
	/**
	 * Betölti a kezdõképernyõhöz tartozó képet.
	 * @throws IOException	ha nem találja a képet a megadott helyen
	 */
	public static void loadImage() throws IOException{
		String File = "pic/startscreen.png";
		startscreen = ImageIO.read(new File(File));
	}
	
	/**
	 * 	Kirajzolja a kezdõképernyõt.
	 */
	@Override
	public void paintComponent(Graphics g){
		g.drawImage(startscreen, 0, 0, null);
	}

}
