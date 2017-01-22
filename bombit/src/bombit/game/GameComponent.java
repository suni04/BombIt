package bombit.game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 *	Egy olyan JComponent ami tartalmazza a j�t�k elemeit �s felel�s azok
 *	kirajzol�s��rt, vez�rl�s��rt �s l�ptet�s��rt.
 */
public class GameComponent extends JComponent implements Serializable{
	private static final long serialVersionUID = 1L;

	private static final int TICK_TIME = 16;	//ennyi miliszekundum egy tick
	private static final int SIZE_OF_TILES = Field.SIZE_OF_TILES;	//egy mez� m�rete
	
	private static BufferedImage gameover;	// a j�t�k v�g�n kirajzoland� k�p
	private static BufferedImage loser1, loser2;
	private static BufferedImage winner1, winner2;
	int loserpony; //megmondja hogy melyik p�ni a vesztes

	private Field field;	//p�lya
	private Timer timer;
	private int ticks;	//eltelt tick-ek sz�ma

	private Pony p1;	//1.j�t�kos
	private Pony p2;	//2.j�t�kos
	private ArrayList<Bomb> bombs;	//a p�ly�n lev� bomb�k
	private ArrayList<Flame> flames;	//a p�ly�n lev� l�ngok
	private ArrayList<PowerUp> powerups;	//a p�ly�n elhelyezked� Power up-ok

	private boolean game;	//true, amikor zajlik a j�t�k
	private boolean paused;	//true, amikor sz�neteltetve van a j�t�k
	private boolean autoPlayer;	//true, ha egy j�t�kos m�d van

/**
 * Inicializ�lja a komponenst �s a j�t�k elemeit.
 * L�trehozza �s elind�tja a timert ami a j�t�kot m�k�dteti.
 * 
 * @param autoPlayer true, ha a felhaszn�l� a g�p ellen j�tszik (1 j�t�kos m�d).
 */
	public GameComponent(boolean autoPlayer) {
		game = true;
		paused = false;
		this.autoPlayer = autoPlayer;
		ticks = 0;

		this.field = new Field(chooseMap());
		this.bombs = new ArrayList<Bomb>();
		this.flames = new ArrayList<Flame>();
		this.powerups = new ArrayList<PowerUp>();
		initPlayers(autoPlayer);

		setSize(field.getX() * Field.SIZE_OF_TILES, field.getY() * Field.SIZE_OF_TILES);
		addKeyListener(new fieldKeyAdapter());
		setFocusable(true);

		startTime();
	}

/**
 * V�laszt egy random sz�mot (1 - 3), ami megadja az elind�tand� p�lya sz�m�t.
 * 
 * @return p�lya sorsz�ma
 */
	private int chooseMap() {
		Random rand = new Random();
		int n = rand.nextInt(3) + 1;
		return n;
	}
	
	/**
	 * Inicializ�lja a j�t�kosokat.
	 * @param autoPlayer megadja hogy a felhaszn�l� a g�p ellen j�tszik-e
	 */
	private void initPlayers(boolean autoPlayer) {
		if (autoPlayer) {
			p1 = new Pony(1);
			p2 = new AutoPlayer(2);
		} else {
			p1 = new Pony(1);
			p2 = new Pony(2);
		}
		Point start1 = field.getStartPos1();
		Point start2 = field.getStartPos2();
		p1.setXY(start1.x, start1.y);
		p2.setXY(start2.x, start2.y);
	}

/**
 * Visszaadja a megadott sz�m� Pony-t.
 * @param n Megadja a j�t�kos sz�m�t
 * @return a megadott sz�m� Pony
 */
	public Pony getPlayer(int n) {
		switch (n) {
		case 1:
			return p1;
		case 2:
			return p2;
		default:
			return null;
		}
	}
	
	/**
	 * Bet�lti a j�t�khoz sz�ks�ges k�peket.
	 * @throws IOException ha nem tal�lja a k�peket a megadott helyen
	 */
	public static void loadImages() throws IOException{
		//Gameover k�p bet�lt�se
		String file = "pic/gameover.png";
		gameover = ImageIO.read(new File(file));
		
		//Nyertes p�ni k�pek bet�lt�se
		file = "pic/ponies/winner1.png";
		winner1 = ImageIO.read(new File(file));
		file = "pic/ponies/winner2.png";
		winner2 = ImageIO.read(new File(file));
		
		//Vesztes p�ni k�pek bet�lt�se
		file = "pic/ponies/loser1.png";
		loser1 = ImageIO.read(new File(file));
		file = "pic/ponies/loser2.png";
		loser2 = ImageIO.read(new File(file));
	}
	
	/**
	 * Hozz�adja a param�terben kapott bomb�t, a bomb�kat tartalmaz� list�hoz.
	 * @param p a hozz�adand� bomba
	 */
	void addBomb(Pony p) {
		bombs.add(new Bomb(p.getX(), p.getY(), p.getReach()));
	}

	/**
	 * Elt�vol�tja a param�terben kapott bomb�t, a bomb�kat tartalmaz� list�b�l.
	 * @param b az elt�vol�tand� bomba
	 */
	void removeBomb(Bomb b) {
		bombs.remove(b);
	}

	/**
	 * Hozz�adja a param�terben kapott l�ngot, a l�ngokat tartalmaz� list�hoz.
	 * @param f a hozz�adand� l�ng
	 */
	void addFlame(Flame f) {
		flames.add(f);
	}

	/**
	 * Elt�vol�tja a param�terben kapott l�ngot, a l�ngokat tartalmaz� list�b�l.
	 * @param f az elt�vol�tand� l�ng
	 */
	void removeFlame(Flame f) {
		flames.remove(f);
	}

	/**
	 * Hozz�adja a param�terben kapott power up-ot, a power-up-okat tartalmaz� list�hoz.
	 * @param p a hozz�adand� power-up
	 */
	void addPowerUp(PowerUp p) {
		powerups.add(p);
	}

	/**
	 * Elt�vol�tja a param�terben kapott power up-ot, a power up-okat tartalmaz� list�b�l.
	 * @param p az elt�vol�tand� power up
	 */
	void removePowerUp(PowerUp p) {
		powerups.remove(p);
	}

/**
 *	A k�t j�t�kos inputjait kezel� oszt�ly: mozg�s, bomba lerak�s.
 */
	private class fieldKeyAdapter extends KeyAdapter implements Serializable {

	private static final long serialVersionUID = 1L;
	
		/**
		 * Kezeli a gomblenyom�sokat �s megh�vja a megfelel� vez�rl�f�ggv�nyeket a Pony-ban.
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			if(!paused){
				int key = e.getKeyCode();
	
				switch (key) {
				// Els� j�t�kos inputjai
				case KeyEvent.VK_W:
					p1.startMove(GameComponent.this, 0, -1);
					break;
				case KeyEvent.VK_S:
					p1.startMove(GameComponent.this, 0, 1);
					break;
				case KeyEvent.VK_A:
					p1.startMove(GameComponent.this, -1, 0);
					break;
				case KeyEvent.VK_D:
					p1.startMove(GameComponent.this, 1, 0);
					break;
				case KeyEvent.VK_SPACE:
					p1.plantBomb(GameComponent.this);
					break;
	
				// M�sodik j�t�kos inputjai
				case KeyEvent.VK_UP:
					if(!autoPlayer)
						p2.startMove(GameComponent.this, 0, -1);
					break;
				case KeyEvent.VK_DOWN:
					if(!autoPlayer)
						p2.startMove(GameComponent.this, 0, 1);
					break;
				case KeyEvent.VK_LEFT:
					if(!autoPlayer)
						p2.startMove(GameComponent.this, -1, 0);
					break;
				case KeyEvent.VK_RIGHT:
					if(!autoPlayer)
						p2.startMove(GameComponent.this, 1, 0);
					break;
				case KeyEvent.VK_ENTER:
					if(!autoPlayer)
						p2.plantBomb(GameComponent.this);
					break;
				}
			}
		}
	}

	private class tickListener implements ActionListener, Serializable{
		private static final long serialVersionUID = 1L;
		
		/**
		 * A timer �ltal gener�lt esem�ny hat�s�ra, friss�ti a j�t�k
		 * �llapot�t �s �jrakirajzolja.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			ticks++;
			update();
			repaint();
		}
	}

/**
 * Updateli a j�t�k elemeinek �llapot�t.
 */
	public void update() {
		if(!paused){
			// Bomb�k updatel�se
			for (int i = 0; i < bombs.size(); i++)
				bombs.get(i).update(this);
			
			// L�ngok updatel�se
			for (int i = 0; i < flames.size(); i++)
				flames.get(i).update(this);
			
			//J�t�kosok updatel�se
			p1.update(this);
			p2.update(this);
		}
	}

/**
 * Sorban kirajzolja a j�t�k elemeit. Ha j�t�k v�ge van, akkor pedig
 * kirajzolja a gy�ztes �s a vesztes p�nit, valamint a 'GAME OVER' feliratot.
 */
	@Override
	public void paintComponent(Graphics g) {
		field.drawMap(g);

		for (int i = 0; i < bombs.size(); i++)
			bombs.get(i).draw(g);

		p1.draw(g);
		p2.draw(g);

		for (int i = 0; i < powerups.size(); i++)
			powerups.get(i).draw(g);

		for (int i = 0; i < flames.size(); i++)
			flames.get(i).draw(g);

		//Ha v�ge a j�t�knak akkor kirajzolja a 'Game Over' feliratot.
		if (!game) {
			int gameover_x = (field.getX() * SIZE_OF_TILES - gameover.getWidth()) / 2;
			int gameover_y = (field.getY() * SIZE_OF_TILES - gameover.getHeight()) / 2;
			g.drawImage(gameover, gameover_x, gameover_y, null);
			
			// Kirajzolja a gy�ztes �s vesztes p�nikat
			int pony1_x = (field.getX() * SIZE_OF_TILES) / 2 - loser1.getWidth() - 50;
			int pony1_y = (field.getY() * SIZE_OF_TILES + gameover.getHeight()) / 2;
			
			int pony2_x = (field.getX() * SIZE_OF_TILES) / 2 + 50;
			int pony2_y = (field.getX() * SIZE_OF_TILES + gameover.getHeight()) / 2;
			if(loserpony == 1){
				g.drawImage(loser1, pony2_x, pony2_y, null);
				g.drawImage(winner2, pony1_x, pony1_y, null);
			} else {
				g.drawImage(loser2, pony2_x, pony2_y, null);
				g.drawImage(winner1, pony1_x, pony1_y, null);
			}
			timer.stop();
		}
	}
	/**
	 * Visszaadja a GameComponent-hez tartoz� Field-et.
	 * @return a GameComponent-hez tartoz�  Field
	 */
	public Field getField() {
		return field;
	}
	
	/**
	 * Visszaadja az eltelt tick-ek sz�m�t.
	 * @return az eltelt tick-ek sz�ma
	 */
	public int getTicks() {
		return ticks;
	}

/**
 * Megadja hogy a param�terben kapott koordin�t�n van-e �ppen bomba
 * 
 * @param x x koordin�ta
 * @param y y koordin�ta 
 * @return true Ha a kapott koordin�t�n �pp bomba van.
 */
	public boolean isBombThere(int x, int y) {
		for (int i = 0; i < bombs.size(); i++) {
			if (bombs.get(i).getX() == x && bombs.get(i).getY() == y)
				return true;
		}
		return false;
	}

/**
 * Megadja hogy a param�terben kapott koordin�t�n van-e �ppen Power up.
 *
 *@param x x koordin�ta
 *@param y y koordin�ta
 * @return true Ha a kapott koordin�t�n �pp van Power ups.
 */
	public PowerUp isPowerUpThere(int x, int y) {
		for (int i = 0; i < powerups.size(); i++) {
			if (powerups.get(i).getX() == x && powerups.get(i).getY() == y)
				return powerups.get(i);
		}
		return null;
	}
	
/**
 * Megadja hogy a param�terben kapott koordin�t�n van-e �ppen l�ng.
 *
 * @param x x koordin�ta
 * @param y y koordin�ta
 * @return true Ha a kapott koordin�t�n �pp van l�ng.
 */
	public boolean isFlameThere(int x, int y){
		for (int i = 0; i < flames.size(); i++) {
			if (flames.get(i).getX() == x && flames.get(i).getY() == y)
				return true;
		}
		return false;
	}
	
	/**
	 * Visszaadja, hogy a j�t�k �ppen meg van-e �ll�tva.
	 * @return true Ha a j�t�k �ppen sz�netel
	 */
	public boolean isPaused(){
		return paused;
	}
	
	/**
	 * Sz�netelteti a j�t�kot.
	 */
	public void pauseGame(){
		paused = true;
	}
	
	/**
	 * Folytatja a j�t�kot.
	 */
	public void continueGame(){
		paused = false;
	}

	/**
	 * Teljesen le�ll�tja a j�t�kot.
	 */
	public void stopGame() {
		game = false;
	}
	
	/**
	 * Be�ll�tja a vesztes p�nit.
	 * @param loser a vesztes p�ni sz�ma
	 */
	public void setLoserPony(int loser){
		loserpony = loser;
	}
	
	/**
	 * Elind�tja a j�t�k timer-�t.
	 */
	public void startTime(){
		timer = new Timer(TICK_TIME, new tickListener());
		timer.start();
	}
}
