package bombit.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 *	A pálya egy adott pontján megjelenõ csomag, amit a játékosok
 *	felvehetnek és különbözõ képességeket nyerhetnek vele: gyorsaság, bomba hatótávolság megnövelése,
 *	bombák számának növelése.
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
	 * Inicializálja a powerup koordinátáit és a típusát.
	 * @param x	a power up x koordinátája
	 * @param y a power up y koordinátája
	 * @param type	a power up típusa
	 */
	public PowerUp(int x, int y, PowerUpType type){
		super(x, y);
		this.type = type;
	}
	
	/**
	 * Visszaadja a powerup típusát.
	 * @return a power up típusa
	 */
	public PowerUpType getType(){
		return type;
	}

	@Override
	public void update(GameComponent g) {
		
	}

	/**
	 * Kirajzolja a megfelelõ power-up képet.
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
	 * Betölti a powerup-ok kirajzolásához szükséges képeket.
	 * @throws IOException ha nem találja a képet a megadott helyen
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
