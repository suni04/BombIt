package bombit.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * J�t�kosok �ltal letehet� bomba,
 * ami egy id� ut�n felrobban.
 */
public class Bomb extends FieldElement {
	private static final long serialVersionUID = 1L;
	
	private static final int NUM_OF_BOMBS = 3;
	private static final int SIZE_OF_TILES = Field.SIZE_OF_TILES;
	private static final int FRAME_DELAY = 10;	//ennyi tick-ig tart egy anim�ci�s f�zis
	
	private static BufferedImage bomb[] = new BufferedImage[NUM_OF_BOMBS];
	
	private int reach;  // ekkora a bomba hat�t�vols�ga
	private int timeUntilExplosion;	//ennyi id� ut�n robban fel
	
	/**
	 * Inicializ�lja a bomba koordin�t�it �s a hat�t�vols�g�t.
	 * @param x	a bomba x koordin�t�ja
	 * @param y	a bomba y koordin�t�ja
	 * @param reach	a bomba hat�t�vols�ga
	 */
	public Bomb(int x, int y, int reach){
		super(x, y);
		this.reach = reach;
		
		Random rand = new Random();
		timeUntilExplosion = rand.nextInt(90)+60;
	}
	
	/**
	 * Bet�lti a bomba anim�ci�j�hoz tartoz� k�peket.
	 * @throws IOException	ha nem tal�lja a k�peket a megadott helyen
	 */
	public static void loadImages() throws IOException{
		String file;
//		Bomba k�peinek bet�lt�se
		for(int i = 0; i < NUM_OF_BOMBS; ++i){
			file = "pic/bomb/Bomb_f0" + i + ".png";
			bomb[i] = ImageIO.read(new File(file));
		}	
	}
	
	/**
	 * Mind a n�gy ir�nyban a bomba hat�t�vols�g�ban l�ngokat hoz l�tre.
	 * Ha az adott ir�nyban felrobbanthatatlan falba �tk�zik akkor ott meg�ll a l�ngok terjed�se.
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
	 * L�pteti a bomba �llapot�t eggyel: cs�kkenti a robban�sig h�tralev� id�t,
	 * ha az lej�rt akkor pedig megh�vja az explode met�dus�t.
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
	 * Kisz�molja �s kirajzolja a bomba aktu�lis anim�ci�s f�zis�t.
	 */
	@Override
	public void draw(Graphics g) {
		int phase = (timeUntilExplosion / FRAME_DELAY) % NUM_OF_BOMBS;
		g.drawImage(bomb[phase], x * SIZE_OF_TILES, y * SIZE_OF_TILES, null);
	}
}
