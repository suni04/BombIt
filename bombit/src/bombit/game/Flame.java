package bombit.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Egy bomba robban�s�nak a l�ngja, ami megsebzi a j�t�kosokat ha belemennek.
 * Miut�n kialszik, a hely�n egy powerUp jelenik meg.
 *
 */
public class Flame extends FieldElement {
	private static final long serialVersionUID = 1L;
	
	private static final int NUM_OF_FLAMES = 5; // ennyi anim�ci� f�zisa van
	private static final int FRAME_DELAY = 4;	//ennyi tick-ig tart egy anim�ci�s f�zis
	private static final int SIZE_OF_TILES = Field.SIZE_OF_TILES;
	
	private int timeToBurn = 60;
	
	private static BufferedImage flame[] = new BufferedImage[NUM_OF_FLAMES];
	
	/**
	 * Inicializ�lja a l�ng koordin�t�it. Ha felrobbanthat� fal volt a l�ng hely�n,
	 * akkor oda gener�l egy power up-ot. Ha a l�ng hely�n j�t�kos van, akkor annak
	 * a j�t�kosnak cs�kkenti eggyel az �let�t.
	 * @param g a GameComponent amin a l�ng van
	 * @param x	a l�ng x koordin�t�ja
	 * @param y a l�ng y koordin�t�ja
	 */
	public Flame(GameComponent g, int x, int y){
		super(x, y);
		char map[][] = g.getField().getMap();
		if (map[x][y] == 'X') {
			map[x][y] = '.';
			g.addPowerUp(generatePowerUp());
		}
		//Van-e j�t�kos a l�ng hely�n
		if(g.getPlayer(1).getX() == x && g.getPlayer(1).getY() == y){
			g.getPlayer(1).decreaseLives(g);
		}
		if(g.getPlayer(2).getX() == x && g.getPlayer(2).getY() == y){
			g.getPlayer(2).decreaseLives(g);
		}
	}
	
	/**
	 * Gener�l egy random power up objektumot.
	 * @return a gener�lt PowerUp objektum
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
	 * Bet�lti a l�ng anim�ci�j�hoz sz�ks�ges k�peket.
	 * @throws IOException ha nem tal�lja a k�peket a megadott helyen
	 */
	public static void loadImages() throws IOException{
		String file;
		
//		L�ng k�peinek bet�lt�se
		for(int i = 0; i < NUM_OF_FLAMES; ++i){
			file = "pic/flame/Flame_f0" + i + ".png";
			flame[i] = ImageIO.read(new File(file));
		}	
	}
	
	/**
	 * L�pteti a l�ng �llapot�t eggyel: cs�kkenti a kialv�sig h�tralev� id�t,
	 * ha az lej�rt akkor pedig elt�vol�tja a l�ngot a GameComponentr�l.
	 */
	@Override
	public void update(GameComponent g){
		timeToBurn--;
		if(timeToBurn == 0){			
			g.removeFlame(this);
		}
	}
	
	/**
	 * Kisz�molja �s kirajzolja a l�ng aktu�lis anim�ci�s f�zis�t.
	 */
	@Override
	public void draw(Graphics g) {
		int phase = (timeToBurn / FRAME_DELAY) % NUM_OF_FLAMES;
		g.drawImage(flame[phase], x * SIZE_OF_TILES, y * SIZE_OF_TILES, null);
	}
}
