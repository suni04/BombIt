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
 *	A p�lya m�trix�t tartalmaz� oszt�ly, amelynek elemei:
 *		- j�rhat� mez�k
 *		- felrobbanthat� fal
 *		- �lland� fal
 */
public class Field implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int SIZE_OF_TILES = 40;	//egy mez� m�rete
	
	private int mapx, mapy;	//a p�lya dimenzi�i
	private char map[][];	//a p�ly�t t�rol� t�mb
	private Point startPos1 = new Point();	//1.j�t�kos kezd�poz�ci�ja
	private Point startPos2 = new Point();	//2.j�t�kos kezd�poz�ci�ja
	
	private static BufferedImage BackgroundTile, ExplodableBlock, SolidBlock;
	
	/**
	 * Megh�vja a p�ly�t inicializ�l� initMap met�dust,
	 * a param�terben megadott sz�ma p�ly�ra.
	 * @param n a p�lya sz�ma
	 */
	public Field(int n) {
		initMap(n);
	}

/**
 * Bet�lti a p�ly�hoz tartoz� k�peket.
 * @throws IOException ha nem tal�lja a k�peket a megadott helyen
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
 * Beolvassa a p�ly�t.
 * @param n megadja a beolvasand� p�lya sz�m�t
 */
	private void initMap(int n) {
		String filename = new String("maps/map" + n + ".txt");

		try {
			// Megnyitja a beolvasand� p�ly�t tartalmaz� f�jlt.
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);

			// Beolvassa a p�lya m�reteit.
			String line = br.readLine();
			String s[] = new String[2];
			Integer x, y;
			s = line.split(" ");
			x = Integer.parseInt(s[0]);
			y = Integer.parseInt(s[1]);
			mapx = x;
			mapy = y;
			map = new char[mapx][mapy];

			// Beolvassa a j�t�kosok kezd�poz�ci�it
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

			// Beolvassa a p�ly�t.
			for (int i = 0; i < mapy; ++i) {
				line = br.readLine();
				for (int j = 0; j < mapx; ++j) {
					map[j][i] = line.charAt(j);
				}
			}

			// F�jl bez�r�sa
			br.close();
			fr.close();
		} catch (IOException e) {
			System.out.println("Nincs ilyen f�jl!");
		}
	}
	
	/**
	 * Visszaadja a p�ly�t tartalmaz� 2-dimenzi�s t�mb�t.
	 * @return a p�ly�t tartalmaz� t�mb
	 */
	public char[][] getMap(){
		return map;
	}
	
	/**
	 * Visszaadja a p�lya sz�less�g�t.
	 * @return a p�lya sz�less�ge
	 */
	public int getX(){
		return mapx;
	}
	
	/**
	 * Visszaadja a p�lya magass�g�t.	
	 * @return a p�lya magass�ga
	 */
	public int getY(){
		return mapy;
	}
	
	/**
	 * Visszaadja az 1. j�t�kos kezd�poz�ci�j�t.
	 * @return az 1. j�t�kos kezd�poz�ci�ja
	 */
	public Point getStartPos1(){
		return startPos1;
	}
	
	/**
	 * Visszaadja az 2. j�t�kos kezd�poz�ci�j�t.
	 * @return az 2. j�t�kos kezd�poz�ci�ja
	 */
	public Point getStartPos2(){
		return startPos2;
	}
	
/**
 * Kirajzolja a p�ly�t a param�terben megkapott Graphics objektumra.
 * A p�ly�t le�r� t�mbben:
 *		# = fal
 *		X = felrobbanthat� fal
 *		. = mez�
 * 
 * @param g a Graphics objektum amire a rajzol�st v�gzi
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

