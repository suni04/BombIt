package bombit;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *	A kezd�k�peny� megjelen�t�s��rt felel�s oszt�ly.
 */
public class StartScreen extends JComponent{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 720;
	private static final int HEIGHT = 600;
	
	private static BufferedImage startscreen;
	
	/**
	 *	Be�ll�tja a kezd�k�perny�t.
	 */
	StartScreen(){
		setSize(WIDTH, HEIGHT);
		setFocusable(true);
		repaint();
	}
	
	/**
	 * Bet�lti a kezd�k�perny�h�z tartoz� k�pet.
	 * @throws IOException	ha nem tal�lja a k�pet a megadott helyen
	 */
	public static void loadImage() throws IOException{
		String File = "pic/startscreen.png";
		startscreen = ImageIO.read(new File(File));
	}
	
	/**
	 * 	Kirajzolja a kezd�k�perny�t.
	 */
	@Override
	public void paintComponent(Graphics g){
		g.drawImage(startscreen, 0, 0, null);
	}

}
