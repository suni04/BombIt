package bombit.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Játékosok által letehetõ bomba,
 * ami egy idõ után felrobban.
 */
public class Bomb extends FieldElement {
	private static final long serialVersionUID = 1L;
	
	private static final int NUM_OF_BOMBS = 3;
	private static final int SIZE_OF_TILES = Field.SIZE_OF_TILES;
	private static final int FRAME_DELAY = 10;	//ennyi tick-ig tart egy animációs fázis
	
	private static BufferedImage bomb[] = new BufferedImage[NUM_OF_BOMBS];
	
	private int reach;  // ekkora a bomba hatótávolsága
	private int timeUntilExplosion;	//ennyi idõ után robban fel
	
	/**
	 * Inicializálja a bomba koordinátáit és a hatótávolságát.
	 * @param x	a bomba x koordinátája
	 * @param y	a bomba y koordinátája
	 * @param reach	a bomba hatótávolsága
	 */
	public Bomb(int x, int y, int reach){
		super(x, y);
		this.reach = reach;
		
		Random rand = new Random();
		timeUntilExplosion = rand.nextInt(90)+60;
	}
	
	/**
	 * Betölti a bomba animációjához tartozó képeket.
	 * @throws IOException	ha nem találja a képeket a megadott helyen
	 */
	public static void loadImages() throws IOException{
		String file;
//		Bomba képeinek betöltése
		for(int i = 0; i < NUM_OF_BOMBS; ++i){
			file = "pic/bomb/Bomb_f0" + i + ".png";
			bomb[i] = ImageIO.read(new File(file));
		}	
	}
	
	/**
	 * Mind a négy irányban a bomba hatótávolságában lángokat hoz létre.
	 * Ha az adott irányban felrobbanthatatlan falba ütközik akkor ott megáll a lángok terjedése.
	 * @param g a GameComponent amin a bomba van
	 */
	private void explode(GameComponent g){
		char map[][] = g.getField().getMap();
		
		g.addFlame(new Flame(g, x, y));
		for(int i = 1; i <= reach; i++){
			if(map[x+i][y] != '#'){
				g.addFlame(new Flame(g, x+i, y));
			} else 
				break;
		}
		for(int i = 1; i <= reach; i++){
			if(map[x-i][y] != '#'){
				g.addFlame(new Flame(g, x-i, y));
			} else
				break;
		}
		for(int i = 1; i <= reach; i++){
			if(map[x][y+i] != '#'){
				g.addFlame(new Flame(g, x, y+i));
			} else
				break;
		}
		for(int i = 1; i <= reach; i++){
			if(map[x][y-i] != '#'){
				g.addFlame(new Flame(g, x, y-i));
			} else
				break;
		}
	}

	/**
	 * Lépteti a bomba állapotát eggyel: csökkenti a robbanásig hátralevõ idõt,
	 * ha az lejárt akkor pedig meghívja az explode metódusát.
	 */
	@Override
	public void update(GameComponent g) {
		timeUntilExplosion--;
		if(timeUntilExplosion == 0){
			explode(g);
			g.removeBomb(this);
		}
	}
	
	/**
	 * Kiszámolja és kirajzolja a bomba aktuális animációs fázisát.
	 */
	@Override
	public void draw(Graphics g) {
		int phase = (timeUntilExplosion / FRAME_DELAY) % NUM_OF_BOMBS;
		g.drawImage(bomb[phase], x * SIZE_OF_TILES, y * SIZE_OF_TILES, null);
	}
}
