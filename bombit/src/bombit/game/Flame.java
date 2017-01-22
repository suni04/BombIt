package bombit.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Egy bomba robbanásának a lángja, ami megsebzi a játékosokat ha belemennek.
 * Miután kialszik, a helyén egy powerUp jelenik meg.
 *
 */
public class Flame extends FieldElement {
	private static final long serialVersionUID = 1L;
	
	private static final int NUM_OF_FLAMES = 5; // ennyi animáció fázisa van
	private static final int FRAME_DELAY = 4;	//ennyi tick-ig tart egy animációs fázis
	private static final int SIZE_OF_TILES = Field.SIZE_OF_TILES;
	
	private int timeToBurn = 60;
	
	private static BufferedImage flame[] = new BufferedImage[NUM_OF_FLAMES];
	
	/**
	 * Inicializálja a láng koordinátáit. Ha felrobbantható fal volt a láng helyén,
	 * akkor oda generál egy power up-ot. Ha a láng helyén játékos van, akkor annak
	 * a játékosnak csökkenti eggyel az életét.
	 * @param g a GameComponent amin a láng van
	 * @param x	a láng x koordinátája
	 * @param y a láng y koordinátája
	 */
	public Flame(GameComponent g, int x, int y){
		super(x, y);
		char map[][] = g.getField().getMap();
		if (map[x][y] == 'X') {
			map[x][y] = '.';
			g.addPowerUp(generatePowerUp());
		}
		//Van-e játékos a láng helyén
		if(g.getPlayer(1).getX() == x && g.getPlayer(1).getY() == y){
			g.getPlayer(1).decreaseLives(g);
		}
		if(g.getPlayer(2).getX() == x && g.getPlayer(2).getY() == y){
			g.getPlayer(2).decreaseLives(g);
		}
	}
	
	/**
	 * Generál egy random power up objektumot.
	 * @return a generált PowerUp objektum
	 */
	public PowerUp generatePowerUp(){
		Random rand = new Random();
		int n= rand.nextInt(10)+1;
		if(n <= 5){
			return new PowerUp(getX(), getY(), PowerUp.PowerUpType.BOMB);
		}
		if(n > 5 && n <= 8){
			return new PowerUp(getX(), getY(), PowerUp.PowerUpType.FLAME);
		}
		return new PowerUp(getX(), getY(), PowerUp.PowerUpType.SPEED);
	}
	
	/**
	 * Betölti a láng animációjához szükséges képeket.
	 * @throws IOException ha nem találja a képeket a megadott helyen
	 */
	public static void loadImages() throws IOException{
		String file;
		
//		Láng képeinek betöltése
		for(int i = 0; i < NUM_OF_FLAMES; ++i){
			file = "pic/flame/Flame_f0" + i + ".png";
			flame[i] = ImageIO.read(new File(file));
		}	
	}
	
	/**
	 * Lépteti a láng állapotát eggyel: csökkenti a kialvásig hátralevõ idõt,
	 * ha az lejárt akkor pedig eltávolítja a lángot a GameComponentrõl.
	 */
	@Override
	public void update(GameComponent g){
		timeToBurn--;
		if(timeToBurn == 0){			
			g.removeFlame(this);
		}
	}
	
	/**
	 * Kiszámolja és kirajzolja a láng aktuális animációs fázisát.
	 */
	@Override
	public void draw(Graphics g) {
		int phase = (timeToBurn / FRAME_DELAY) % NUM_OF_FLAMES;
		g.drawImage(flame[phase], x * SIZE_OF_TILES, y * SIZE_OF_TILES, null);
	}
}
