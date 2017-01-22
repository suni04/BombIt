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
 *	Egy olyan JComponent ami tartalmazza a játék elemeit és felelõs azok
 *	kirajzolásáért, vezérléséért és léptetéséért.
 */
public class GameComponent extends JComponent implements Serializable{
	private static final long serialVersionUID = 1L;

	private static final int TICK_TIME = 16;	//ennyi miliszekundum egy tick
	private static final int SIZE_OF_TILES = Field.SIZE_OF_TILES;	//egy mezõ mérete
	
	private static BufferedImage gameover;	// a játék végén kirajzolandó kép
	private static BufferedImage loser1, loser2;
	private static BufferedImage winner1, winner2;
	int loserpony; //megmondja hogy melyik póni a vesztes

	private Field field;	//pálya
	private Timer timer;
	private int ticks;	//eltelt tick-ek száma

	private Pony p1;	//1.játékos
	private Pony p2;	//2.játékos
	private ArrayList<Bomb> bombs;	//a pályán levõ bombák
	private ArrayList<Flame> flames;	//a pályán levõ lángok
	private ArrayList<PowerUp> powerups;	//a pályán elhelyezkedõ Power up-ok

	private boolean game;	//true, amikor zajlik a játék
	private boolean paused;	//true, amikor szüneteltetve van a játék
	private boolean autoPlayer;	//true, ha egy játékos mód van

/**
 * Inicializálja a komponenst és a játék elemeit.
 * Létrehozza és elindítja a timert ami a játékot mûködteti.
 * 
 * @param autoPlayer true, ha a felhasználó a gép ellen játszik (1 játékos mód).
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
 * Választ egy random számot (1 - 3), ami megadja az elindítandó pálya számát.
 * 
 * @return pálya sorszáma
 */
	private int chooseMap() {
		Random rand = new Random();
		int n = rand.nextInt(3) + 1;
		return n;
	}
	
	/**
	 * Inicializálja a játékosokat.
	 * @param autoPlayer megadja hogy a felhasználó a gép ellen játszik-e
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
 * Visszaadja a megadott számú Pony-t.
 * @param n Megadja a játékos számát
 * @return a megadott számú Pony
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
	 * Betölti a játékhoz szükséges képeket.
	 * @throws IOException ha nem találja a képeket a megadott helyen
	 */
	public static void loadImages() throws IOException{
		//Gameover kép betöltése
		String file = "pic/gameover.png";
		gameover = ImageIO.read(new File(file));
		
		//Nyertes póni képek betöltése
		file = "pic/ponies/winner1.png";
		winner1 = ImageIO.read(new File(file));
		file = "pic/ponies/winner2.png";
		winner2 = ImageIO.read(new File(file));
		
		//Vesztes póni képek betöltése
		file = "pic/ponies/loser1.png";
		loser1 = ImageIO.read(new File(file));
		file = "pic/ponies/loser2.png";
		loser2 = ImageIO.read(new File(file));
	}
	
	/**
	 * Hozzáadja a paraméterben kapott bombát, a bombákat tartalmazó listához.
	 * @param p a hozzáadandó bomba
	 */
	void addBomb(Pony p) {
		bombs.add(new Bomb(p.getX(), p.getY(), p.getReach()));
	}

	/**
	 * Eltávolítja a paraméterben kapott bombát, a bombákat tartalmazó listából.
	 * @param b az eltávolítandó bomba
	 */
	void removeBomb(Bomb b) {
		bombs.remove(b);
	}

	/**
	 * Hozzáadja a paraméterben kapott lángot, a lángokat tartalmazó listához.
	 * @param f a hozzáadandó láng
	 */
	void addFlame(Flame f) {
		flames.add(f);
	}

	/**
	 * Eltávolítja a paraméterben kapott lángot, a lángokat tartalmazó listából.
	 * @param f az eltávolítandó láng
	 */
	void removeFlame(Flame f) {
		flames.remove(f);
	}

	/**
	 * Hozzáadja a paraméterben kapott power up-ot, a power-up-okat tartalmazó listához.
	 * @param p a hozzáadandó power-up
	 */
	void addPowerUp(PowerUp p) {
		powerups.add(p);
	}

	/**
	 * Eltávolítja a paraméterben kapott power up-ot, a power up-okat tartalmazó listából.
	 * @param p az eltávolítandó power up
	 */
	void removePowerUp(PowerUp p) {
		powerups.remove(p);
	}

/**
 *	A két játékos inputjait kezelõ osztály: mozgás, bomba lerakás.
 */
	private class fieldKeyAdapter extends KeyAdapter implements Serializable {

	private static final long serialVersionUID = 1L;
	
		/**
		 * Kezeli a gomblenyomásokat és meghívja a megfelelõ vezérlõfüggvényeket a Pony-ban.
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			if(!paused){
				int key = e.getKeyCode();
	
				switch (key) {
				// Elsõ játékos inputjai
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
	
				// Második játékos inputjai
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
		 * A timer által generált esemény hatására, frissíti a játék
		 * állapotát és újrakirajzolja.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			ticks++;
			update();
			repaint();
		}
	}

/**
 * Updateli a játék elemeinek állapotát.
 */
	public void update() {
		if(!paused){
			// Bombák updatelése
			for (int i = 0; i < bombs.size(); i++)
				bombs.get(i).update(this);
			
			// Lángok updatelése
			for (int i = 0; i < flames.size(); i++)
				flames.get(i).update(this);
			
			//Játékosok updatelése
			p1.update(this);
			p2.update(this);
		}
	}

/**
 * Sorban kirajzolja a játék elemeit. Ha játék vége van, akkor pedig
 * kirajzolja a gyõztes és a vesztes pónit, valamint a 'GAME OVER' feliratot.
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

		//Ha vége a játéknak akkor kirajzolja a 'Game Over' feliratot.
		if (!game) {
			int gameover_x = (field.getX() * SIZE_OF_TILES - gameover.getWidth()) / 2;
			int gameover_y = (field.getY() * SIZE_OF_TILES - gameover.getHeight()) / 2;
			g.drawImage(gameover, gameover_x, gameover_y, null);
			
			// Kirajzolja a gyõztes és vesztes pónikat
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
	 * Visszaadja a GameComponent-hez tartozó Field-et.
	 * @return a GameComponent-hez tartozó  Field
	 */
	public Field getField() {
		return field;
	}
	
	/**
	 * Visszaadja az eltelt tick-ek számát.
	 * @return az eltelt tick-ek száma
	 */
	public int getTicks() {
		return ticks;
	}

/**
 * Megadja hogy a paraméterben kapott koordinátán van-e éppen bomba
 * 
 * @param x x koordináta
 * @param y y koordináta 
 * @return true Ha a kapott koordinátán épp bomba van.
 */
	public boolean isBombThere(int x, int y) {
		for (int i = 0; i < bombs.size(); i++) {
			if (bombs.get(i).getX() == x && bombs.get(i).getY() == y)
				return true;
		}
		return false;
	}

/**
 * Megadja hogy a paraméterben kapott koordinátán van-e éppen Power up.
 *
 *@param x x koordináta
 *@param y y koordináta
 * @return true Ha a kapott koordinátán épp van Power ups.
 */
	public PowerUp isPowerUpThere(int x, int y) {
		for (int i = 0; i < powerups.size(); i++) {
			if (powerups.get(i).getX() == x && powerups.get(i).getY() == y)
				return powerups.get(i);
		}
		return null;
	}
	
/**
 * Megadja hogy a paraméterben kapott koordinátán van-e éppen láng.
 *
 * @param x x koordináta
 * @param y y koordináta
 * @return true Ha a kapott koordinátán épp van láng.
 */
	public boolean isFlameThere(int x, int y){
		for (int i = 0; i < flames.size(); i++) {
			if (flames.get(i).getX() == x && flames.get(i).getY() == y)
				return true;
		}
		return false;
	}
	
	/**
	 * Visszaadja, hogy a játék éppen meg van-e állítva.
	 * @return true Ha a játék éppen szünetel
	 */
	public boolean isPaused(){
		return paused;
	}
	
	/**
	 * Szünetelteti a játékot.
	 */
	public void pauseGame(){
		paused = true;
	}
	
	/**
	 * Folytatja a játékot.
	 */
	public void continueGame(){
		paused = false;
	}

	/**
	 * Teljesen leállítja a játékot.
	 */
	public void stopGame() {
		game = false;
	}
	
	/**
	 * Beállítja a vesztes pónit.
	 * @param loser a vesztes póni száma
	 */
	public void setLoserPony(int loser){
		loserpony = loser;
	}
	
	/**
	 * Elindítja a játék timer-ét.
	 */
	public void startTime(){
		timer = new Timer(TICK_TIME, new tickListener());
		timer.start();
	}
}
