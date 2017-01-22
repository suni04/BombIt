package bombit.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class Pony extends FieldElement implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final int NUM_OF_DIRS = 4;	//p�ni ir�nyainak sz�ma
	private static final int NUM_OF_PHASES = 4;	//p�ni anim�ci�s f�zisainak sz�ma
	private static final int SIZE_OF_TILES = Field.SIZE_OF_TILES;	//egy mez� m�rete
	private static final int FRAME_DELAY = 2;	//ennyi tick-ig tart egy anim�ci�s f�zis
	private static final int ANIM_DURATION = NUM_OF_PHASES * FRAME_DELAY;	//ennyi ideig tart egy l�p�s
	
	private static BufferedImage pony1[][] = new BufferedImage[NUM_OF_DIRS][NUM_OF_PHASES];
	private static BufferedImage pony2[][] = new BufferedImage[NUM_OF_DIRS][NUM_OF_PHASES];
	
	protected int which_pony;	//megadja melyik p�nit kell kirajzolni
	protected int dirx;			//megadja hogy jobbra(+1) vagy balra(-1) n�z-e a p�ni
	protected int diry;			//megadja hogy el�re(+1) vagy h�tra(-1) n�z-e a p�ni
	
	protected int speed;	//ennyi pixelt tesz meg a p�ni egy tick alatt
	protected int timeOfSpeedPowerUp;	//ennyi id� van vissza, miel�tt a sebess�g vissza�ll az eredeti �rt�kre
	
	protected int bombReach;	//j�t�kos bomb�inak hat�t�vols�ga
	protected int timeOfReachPowerUp;	//ennyi id� van vissza, miel�tt a hat�t�vols�g vissza�ll az eredeti �rt�kre
	
	protected int lives;	//j�t�kos �leinek sz�ma
	protected int bombs;	//j�t�kos bomb�inak sz�ma
	
	protected boolean moving;	//mozg�sban van-e a j�t�kos
	protected int animTime;	//megadja az anim�ci� jelenlegi �llapot�t
	
/**
 * Inicializ�lja a j�t�kos tagv�ltoz�it.
 * @param which_pony megadja hogy melyik p�ni k�p�t kell bet�lteni
 */
	public Pony(int which_pony){
		super();
		this.which_pony = which_pony;
		
		//Alapb�l a p�ni el�refel� n�z
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
 * Bet�lti a p�nikhoz tartoz� k�peket.
 * @throws IOException nem tal�lja a k�peket a megadott helyen
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
 * Megn�veli a bomb�k sz�m�t.
 */
	private void pickUpBomb(){
		bombs++;
	}
	
/**
 * Megn�veli a bomb�k hat�t�vols�g�t, �s az extra hat�t�vols�g
 * lej�rt�nak idej�t.
 */
	private void pickUpFlame(){
		timeOfReachPowerUp += 480;
		bombReach++;
	}
	
/**
 * K�tszeres�re n�veli a p�ni alapsebess�g�t, teh�t 2 pixelt tesz meg tick-enk�nt.
 * Megn�veli az extra sebess�g lej�rt�nak idej�t.
 */
	private void pickUpSpeed(){
		timeOfSpeedPowerUp += 300;
		speed = 2;
	}
	
/**
 * Ha a j�t�kos poz�ci�j�n van egy Power up akkor felveszi,
 * �s a Power Up t�pusa szerint megv�ltoztatja a j�t�kos �llapot�t (a megfelel� f�ggv�ny hiv�s�val).
 * Ezut�n elt�vol�tja a felvett Power up-ot a p�ly�r�l. 
 * @param g A GameComponent amin a j�t�kos van.
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
	
/**	Megd�gleszti a p�nit, vagyis meg�ll�tja a j�t�kot.
 * @param g A GameComponent amin a j�t�kos van.
 */
	private void die(GameComponent g){
		g.setLoserPony(which_pony);
		g.stopGame();
	}
	
/**
 * Be�ll�tja hogy merre n�zzen a p�ni.
 * Megn�zi hogy a k�v�nt ir�nyba el tud-e mozdulni a p�ni,
 * ha igen akkor megv�ltoztatja a poz�ci�j�t.
 * 
 * A p�ly�t le�r� t�mbben:
 * 		# = fal
 * 		X = felrobbanthat� fal.
 * 
 * @param g A GameComponent amin a j�t�kos van.
 * @param dirx	Az x ir�ny amibe el akar mozdulni a j�t�kos.
 * @param diry	Az y ir�ny amibe el akar mozdulni a j�t�kos.
 * @return	true Ha a j�t�kos elmozdult a k�v�nt ir�nyba.
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
 * Megn�zi hogy a j�t�kos poz�ci�j�n van-e m�r bomba,
 * ha nincsen akkor elhelyez ott egy bomb�t.
 * (K�t bomba nem lehet egym�son!)
 * 
 * @param g A GameComponent amin a j�t�kos van.
 * @return	true Ha siker�lt lerakni a bomb�t a p�ly�ra.
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
	 * Cs�kkenti a j�t�kos �leteinek a sz�m�t. Ha elfogytak az �letei,
	 * akkor meg�li a j�t�kost.
	 * 
	 * @param g A GameComponent amin a j�t�kos van.
	 */
	public void decreaseLives(GameComponent g){
		lives--;
		if(lives <= 0)
			die(g);
	}
	
	/**
	 * Visszaadja a p�ni ir�nyvektor�nak az x komponens�t.
	 * @return a p�ni ir�nyvektor�nak az x komponense.
	 */
	public int getdirX(){
		return this.dirx;
	}
	
	/**
	 * Visszaadja a p�ni ir�nyvektor�nak az y komponens�t.
	 * @return a p�ni ir�nyvektor�nak az y komponense.
	 */
	public int getdirY(){
		return this.diry;
	}

	/**
	 * Visszaadja a j�t�kos bomb�inak a hat�t�vols�g�t.
	 * @return a j�t�kos bomb�inak hat�t�vols�g�t.
	 */
	public int getReach(){
		return this.bombReach;
	}
	
	/**
	 * Elind�tja a j�t�kos mozg�s�t, ha m�g nincsen mozg�sban.
	 * 
	 * @param g A GameComponent amin a j�t�kos van.
	 * @param dirx Az x ir�ny amibe el akar mozdulni a j�t�kos.
	 * @param diry Az y ir�ny amibe el akar mozdulni a j�t�kos.
	 */
	public void startMove(GameComponent g, int dirx,int diry){
		if(!moving && move(g, dirx, diry)) {
			moving = true;
		}
	}
	
/**
 * L�pteti a p�ni anim�ci�j�t, friss�ti a k�pess�geinek a lej�rati idej�t,
 * ellen�rzi hogy l�ngba l�pett-e �s felveszi a power-up-ot az aktu�lis mez�r�l.
 * @param g A GameComponent, amin a j�t�kos van.
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
 * Kisz�molja a p�ni aktu�lis anim�ci�s f�zis�t �s kirajzolja
 * a megfelel� p�ni k�peit.
 * 
 * @param g A GameComponent amin a j�t�kos van.
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
