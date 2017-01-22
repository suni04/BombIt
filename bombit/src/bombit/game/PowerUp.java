package bombit.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 *	A p�lya egy adott pontj�n megjelen� csomag, amit a j�t�kosok
 *	felvehetnek �s k�l�nb�z� k�pess�geket nyerhetnek vele: gyorsas�g, bomba hat�t�vols�g megn�vel�se,
 *	bomb�k sz�m�nak n�vel�se.
 */
public class PowerUp extends FieldElement{
	private static final long serialVersionUID = 1L;

	private static final int SIZE_OF_TILES = Field.SIZE_OF_TILES;
	
	public static enum PowerUpType{
		SPEED, FLAME, BOMB;
	}
	
	private static BufferedImage BombPowerUp, FlamePowerUp, SpeedPowerUp;
	
	private PowerUpType type;
	
	/**
	 * Inicializ�lja a powerup koordin�t�it �s a t�pus�t.
	 * @param x	a power up x koordin�t�ja
	 * @param y a power up y koordin�t�ja
	 * @param type	a power up t�pusa
	 */
	public PowerUp(int x, int y, PowerUpType type){
		super(x, y);
		this.type = type;
	}
	
	/**
	 * Visszaadja a powerup t�pus�t.
	 * @return a power up t�pusa
	 */
	public PowerUpType getType(){
		return type;
	}

	@Override
	public void update(GameComponent g) {
		
	}

	/**
	 * Kirajzolja a megfelel� power-up k�pet.
	 */
	@Override
	public void draw(Graphics g) {
		switch(type){
		case BOMB:
			g.drawImage(BombPowerUp, x * SIZE_OF_TILES, y * SIZE_OF_TILES, null);
			break;
		case FLAME:
			g.drawImage(FlamePowerUp, x * SIZE_OF_TILES, y * SIZE_OF_TILES, null);
			break;
		case SPEED:
			g.drawImage(SpeedPowerUp, x * SIZE_OF_TILES, y * SIZE_OF_TILES, null);
			break;
		}
	}

	/**
	 * Bet�lti a powerup-ok kirajzol�s�hoz sz�ks�ges k�peket.
	 * @throws IOException ha nem tal�lja a k�pet a megadott helyen
	 */
	public static void loadImages() throws IOException {
		String file;
		
		file = "pic/powerups/BombPowerup.png";
		BombPowerUp = ImageIO.read(new File(file));
		
		file = "pic/powerups/FlamePowerup.png";
		FlamePowerUp = ImageIO.read(new File(file));
		
		file = "pic/powerups/SpeedPowerup.png";
		SpeedPowerUp = ImageIO.read(new File(file));
		
	}

}
