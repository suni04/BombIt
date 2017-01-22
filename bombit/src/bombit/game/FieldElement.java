package bombit.game;

import java.awt.Graphics;
import java.io.Serializable;
/**
 * A játékhoz tartozó elemek õsosztálya.
 */
public abstract class FieldElement implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected int x;
	protected int y;
		
	public FieldElement() {
		this.x = 0;
		this.y = 0;
	}
	
	/**
	 * Inicializálja az objektum x, y koordinátáit.
	 * @param x x koordináta
	 * @param y y koordináta
	 */
	public FieldElement(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Visszaadja az x koordinátáját az objektumnak.
	 * @return a FieldElement x koordinátája
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Visszaadja az y koordinátáját az objektumnak.
	 * @return a FieldElement y koordinátája
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Beállítja a FieldElement koordinátáit, a megadott
	 * koordinátákra.
	 * @param x x koordináta
	 * @param y y koordináta
	 */
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Lépteti a játékelem állapotát.
	 * @param g a játékelemet tartalmazó GameComponent
	 */
	public abstract void update(GameComponent g);
	
	/**
	 * Kirajzolja a játékelem aktuális állapotát.
	 * @param g	a játékelement tartalmazó GameComponent
	 */
	public abstract void draw(Graphics g); 
}
