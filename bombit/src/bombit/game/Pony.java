package bombit.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class Pony extends FieldElement implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final int NUM_OF_DIRS = 4;	//póni irányainak száma
	private static final int NUM_OF_PHASES = 4;	//póni animációs fázisainak száma
	private static final int SIZE_OF_TILES = Field.SIZE_OF_TILES;	//egy mezõ mérete
	private static final int FRAME_DELAY = 2;	//ennyi tick-ig tart egy animációs fázis
	private static final int ANIM_DURATION = NUM_OF_PHASES * FRAME_DELAY;	//ennyi ideig tart egy lépés
	
	private static BufferedImage pony1[][] = new BufferedImage[NUM_OF_DIRS][NUM_OF_PHASES];
	private static BufferedImage pony2[][] = new BufferedImage[NUM_OF_DIRS][NUM_OF_PHASES];
	
	protected int which_pony;	//megadja melyik pónit kell kirajzolni
	protected int dirx;			//megadja hogy jobbra(+1) vagy balra(-1) néz-e a póni
	protected int diry;			//megadja hogy elõre(+1) vagy hátra(-1) néz-e a póni
	
	protected int speed;	//ennyi pixelt tesz meg a póni egy tick alatt
	protected int timeOfSpeedPowerUp;	//ennyi idõ van vissza, mielõtt a sebesség visszaáll az eredeti értékre
	
	protected int bombReach;	//játékos bombáinak hatótávolsága
	protected int timeOfReachPowerUp;	//ennyi idõ van vissza, mielõtt a hatótávolság visszaáll az eredeti értékre
	
	protected int lives;	//játékos éleinek száma
	protected int bombs;	//játékos bombáinak száma
	
	protected boolean moving;	//mozgásban van-e a játékos
	protected int animTime;	//megadja az animáció jelenlegi állapotát
	
/**
 * Inicializálja a játékos tagváltozóit.
 * @param which_pony megadja hogy melyik póni képét kell betölteni
 */
	public Pony(int which_pony){
		super();
		this.which_pony = which_pony;
		
		//Alapból a póni elõrefelé néz
		dirx = 0;
		diry = 1;
		
		speed = 1;
		timeOfSpeedPowerUp = 0;
		
		bombReach = 1;
		timeOfReachPowerUp = 0;
		
		
		lives = 3;
		bombs = 20;
		
		moving = false;
		animTime = 0;
	}
	
	public void generateMove(GameComponent g){}
	
/**
 * Betölti a pónikhoz tartozó képeket.
 * @throws IOException nem találja a képeket a megadott helyen
 */
	public static void loadImages() throws IOException{
		BufferedImage spritesheet1 = ImageIO.read(new File("pic/ponies/pony1.png"));
		BufferedImage spritesheet2 = ImageIO.read(new File("pic/ponies/pony2.png"));
		for(int i = 0; i < NUM_OF_PHASES; ++i){
			for(int j = 0; j < NUM_OF_DIRS; ++j){
				pony1[j][i] = spritesheet1.getSubimage(i * SIZE_OF_TILES, j * SIZE_OF_TILES, SIZE_OF_TILES, SIZE_OF_TILES);
				pony2[j][i] = spritesheet2.getSubimage(i * SIZE_OF_TILES, j * SIZE_OF_TILES, SIZE_OF_TILES, SIZE_OF_TILES);
			}
		}
	}
	

/** 
 * Megnöveli a bombák számát.
 */
	private void pickUpBomb(){
		bombs++;
	}
	
/**
 * Megnöveli a bombák hatótávolságát, és az extra hatótávolság
 * lejártának idejét.
 */
	private void pickUpFlame(){
		timeOfReachPowerUp += 480;
		bombReach++;
	}
	
/**
 * Kétszeresére növeli a póni alapsebességét, tehát 2 pixelt tesz meg tick-enként.
 * Megnöveli az extra sebesség lejártának idejét.
 */
	private void pickUpSpeed(){
		timeOfSpeedPowerUp += 300;
		speed = 2;
	}
	
/**
 * Ha a játékos pozícióján van egy Power up akkor felveszi,
 * és a Power Up típusa szerint megváltoztatja a játékos állapotát (a megfelelõ függvény hivásával).
 * Ezután eltávolítja a felvett Power up-ot a pályáról. 
 * @param g A GameComponent amin a játékos van.
 */
	private void getPowerUp(GameComponent g){
		PowerUp powerup = g.isPowerUpThere(x, y);
		if(powerup != null){
			switch(powerup.getType()){
			case BOMB:
				pickUpBomb();
				break;
			case FLAME:
				pickUpFlame();
				break;
			case SPEED:
				pickUpSpeed();
				break;
			}
			g.removePowerUp(powerup);
		}
	}
	
/**	Megdögleszti a pónit, vagyis megállítja a játékot.
 * @param g A GameComponent amin a játékos van.
 */
	private void die(GameComponent g){
		g.setLoserPony(which_pony);
		g.stopGame();
	}
	
/**
 * Beállítja hogy merre nézzen a póni.
 * Megnézi hogy a kívánt irányba el tud-e mozdulni a póni,
 * ha igen akkor megváltoztatja a pozícióját.
 * 
 * A pályát leíró tömbben:
 * 		# = fal
 * 		X = felrobbantható fal.
 * 
 * @param g A GameComponent amin a játékos van.
 * @param dirx	Az x irány amibe el akar mozdulni a játékos.
 * @param diry	Az y irány amibe el akar mozdulni a játékos.
 * @return	true Ha a játékos elmozdult a kívánt irányba.
 */
	public boolean move(GameComponent g, int dirx, int diry){
		this.dirx = dirx;
		this.diry = diry;
		char map[][] = g.getField().getMap();
		if (map[x + dirx][y + diry] != '#' && map[x + dirx][y + diry] != 'X') {
			x += dirx;
			y += diry;
			return true;
		}
		return false;
	}
	
/**
 * Megnézi hogy a játékos pozícióján van-e már bomba,
 * ha nincsen akkor elhelyez ott egy bombát.
 * (Két bomba nem lehet egymáson!)
 * 
 * @param g A GameComponent amin a játékos van.
 * @return	true Ha sikerült lerakni a bombát a pályára.
 */
	public boolean plantBomb(GameComponent g){
		if(bombs > 0 && !g.isBombThere(x, y)){
			bombs--;
			g.addBomb(this);
			return true;
		}
		return false;
	}	
	
	/**
	 * Csökkenti a játékos életeinek a számát. Ha elfogytak az életei,
	 * akkor megöli a játékost.
	 * 
	 * @param g A GameComponent amin a játékos van.
	 */
	public void decreaseLives(GameComponent g){
		lives--;
		if(lives <= 0)
			die(g);
	}
	
	/**
	 * Visszaadja a póni irányvektorának az x komponensét.
	 * @return a póni irányvektorának az x komponense.
	 */
	public int getdirX(){
		return this.dirx;
	}
	
	/**
	 * Visszaadja a póni irányvektorának az y komponensét.
	 * @return a póni irányvektorának az y komponense.
	 */
	public int getdirY(){
		return this.diry;
	}

	/**
	 * Visszaadja a játékos bombáinak a hatótávolságát.
	 * @return a játékos bombáinak hatótávolságát.
	 */
	public int getReach(){
		return this.bombReach;
	}
	
	/**
	 * Elindítja a játékos mozgását, ha még nincsen mozgásban.
	 * 
	 * @param g A GameComponent amin a játékos van.
	 * @param dirx Az x irány amibe el akar mozdulni a játékos.
	 * @param diry Az y irány amibe el akar mozdulni a játékos.
	 */
	public void startMove(GameComponent g, int dirx,int diry){
		if(!moving && move(g, dirx, diry)) {
			moving = true;
		}
	}
	
/**
 * Lépteti a póni animációját, frissíti a képességeinek a lejárati idejét,
 * ellenõrzi hogy lángba lépett-e és felveszi a power-up-ot az aktuális mezõrõl.
 * @param g A GameComponent, amin a játékos van.
 */
	@Override
	public void update(GameComponent g) {
		if(moving){
			animTime += speed;
			if(animTime >= ANIM_DURATION){
				getPowerUp(g);
				if(g.isFlameThere(x, y)){
					decreaseLives(g);
				}
				moving = false;
				animTime = 0;
			}
		}
		if(timeOfReachPowerUp > 0){
			timeOfReachPowerUp--;
		} else {
			bombReach = 1;
		}
		if(timeOfSpeedPowerUp > 0){
			timeOfSpeedPowerUp--;
		} else {
			speed = 1;
		}
		
		
	}

/**
 * Kiszámolja a póni aktuális animációs fázisát és kirajzolja
 * a megfelelõ póni képeit.
 * 
 * @param g A GameComponent amin a játékos van.
 */
	@Override
	public void draw(Graphics g) {
		int phase = animTime / FRAME_DELAY;
		int fromX, fromY;
		if(moving){
			fromX = (x-dirx) * SIZE_OF_TILES;
			fromY = (y-diry) * SIZE_OF_TILES;
			
		} else {
			fromX = x * SIZE_OF_TILES;
			fromY = y * SIZE_OF_TILES;
		}
		
		BufferedImage pony[][] = null;
		switch (which_pony) {
			case 1: pony = pony1; break;
			case 2: pony = pony2; break;
		}
		
		int delta = animTime * SIZE_OF_TILES / ANIM_DURATION;
		switch(dirx){
		case 1:
			g.drawImage(pony[0][phase], fromX + delta, fromY, null);
			break;
		case -1:
			g.drawImage(pony[2][phase], fromX - delta, fromY, null);
			break;
		}
		
		switch(diry){
		case 1:
			g.drawImage(pony[1][phase], fromX, fromY + delta, null);
			break;
		case -1:
			g.drawImage(pony[3][phase], fromX, fromY - delta, null);
			break;
		}
	}
}
