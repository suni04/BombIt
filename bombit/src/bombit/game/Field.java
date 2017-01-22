package bombit.game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

/**
 *	A pálya mátrixát tartalmazó osztály, amelynek elemei:
 *		- járható mezõk
 *		- felrobbantható fal
 *		- állandó fal
 */
public class Field implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int SIZE_OF_TILES = 40;	//egy mezõ mérete
	
	private int mapx, mapy;	//a pálya dimenziói
	private char map[][];	//a pályát tároló tömb
	private Point startPos1 = new Point();	//1.játékos kezdõpozíciója
	private Point startPos2 = new Point();	//2.játékos kezdõpozíciója
	
	private static BufferedImage BackgroundTile, ExplodableBlock, SolidBlock;
	
	/**
	 * Meghívja a pályát inicializáló initMap metódust,
	 * a paraméterben megadott száma pályára.
	 * @param n a pálya száma
	 */
	public Field(int n) {
		initMap(n);
	}

/**
 * Betölti a pályához tartozó képeket.
 * @throws IOException ha nem találja a képeket a megadott helyen
 */
	public static void loadImages() throws IOException{
		String file;
	
		file = "pic/tiles/BackgroundTile.png";
		BackgroundTile = ImageIO.read(new File(file));
		
		file = "pic/tiles/ExplodableBlock.png";
		ExplodableBlock = ImageIO.read(new File(file));
		
		file = "pic/tiles/SolidBlock.png";
		SolidBlock = ImageIO.read(new File(file));
	}
	
/**
 * Beolvassa a pályát.
 * @param n megadja a beolvasandó pálya számát
 */
	private void initMap(int n) {
		String filename = new String("maps/map" + n + ".txt");

		try {
			// Megnyitja a beolvasandó pályát tartalmazó fájlt.
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);

			// Beolvassa a pálya méreteit.
			String line = br.readLine();
			String s[] = new String[2];
			Integer x, y;
			s = line.split(" ");
			x = Integer.parseInt(s[0]);
			y = Integer.parseInt(s[1]);
			mapx = x;
			mapy = y;
			map = new char[mapx][mapy];

			// Beolvassa a játékosok kezdõpozícióit
			line = br.readLine();
			s = line.split(" ");
			x = Integer.parseInt(s[0]);
			y = Integer.parseInt(s[1]);
			startPos1.setLocation(x, y);

			line = br.readLine();
			s = line.split(" ");
			x = Integer.parseInt(s[0]);
			y = Integer.parseInt(s[1]);
			startPos2.setLocation(x, y);

			// Beolvassa a pályát.
			for (int i = 0; i < mapy; ++i) {
				line = br.readLine();
				for (int j = 0; j < mapx; ++j) {
					map[j][i] = line.charAt(j);
				}
			}

			// Fájl bezárása
			br.close();
			fr.close();
		} catch (IOException e) {
			System.out.println("Nincs ilyen fájl!");
		}
	}
	
	/**
	 * Visszaadja a pályát tartalmazó 2-dimenziós tömböt.
	 * @return a pályát tartalmazó tömb
	 */
	public char[][] getMap(){
		return map;
	}
	
	/**
	 * Visszaadja a pálya szélességét.
	 * @return a pálya szélessége
	 */
	public int getX(){
		return mapx;
	}
	
	/**
	 * Visszaadja a pálya magasságát.	
	 * @return a pálya magassága
	 */
	public int getY(){
		return mapy;
	}
	
	/**
	 * Visszaadja az 1. játékos kezdõpozícióját.
	 * @return az 1. játékos kezdõpozíciója
	 */
	public Point getStartPos1(){
		return startPos1;
	}
	
	/**
	 * Visszaadja az 2. játékos kezdõpozícióját.
	 * @return az 2. játékos kezdõpozíciója
	 */
	public Point getStartPos2(){
		return startPos2;
	}
	
/**
 * Kirajzolja a pályát a paraméterben megkapott Graphics objektumra.
 * A pályát leíró tömbben:
 *		# = fal
 *		X = felrobbantható fal
 *		. = mezõ
 * 
 * @param g a Graphics objektum amire a rajzolást végzi
 */
	public void drawMap(Graphics g){
		for(int i = 0; i < mapx; ++i){
			for(int j = 0; j < mapy; ++j){
				switch(map[i][j]){
				case '.':
					g.drawImage(BackgroundTile, i * SIZE_OF_TILES, j * SIZE_OF_TILES, null);
					break;
				case '#':
					g.drawImage(SolidBlock, i * SIZE_OF_TILES, j * SIZE_OF_TILES, null);
					break;
				case 'X':
					g.drawImage(ExplodableBlock, i * SIZE_OF_TILES, j * SIZE_OF_TILES, null);
					break;
				}
			}
		}
	}
}

