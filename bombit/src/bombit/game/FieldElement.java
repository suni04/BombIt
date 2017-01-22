package bombit.game;

import java.awt.Graphics;
import java.io.Serializable;
/**
 * A j�t�khoz tartoz� elemek �soszt�lya.
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
	 * Inicializ�lja az objektum x, y koordin�t�it.
	 * @param x x koordin�ta
	 * @param y y koordin�ta
	 */
	public FieldElement(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Visszaadja az x koordin�t�j�t az objektumnak.
	 * @return a FieldElement x koordin�t�ja
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Visszaadja az y koordin�t�j�t az objektumnak.
	 * @return a FieldElement y koordin�t�ja
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Be�ll�tja a FieldElement koordin�t�it, a megadott
	 * koordin�t�kra.
	 * @param x x koordin�ta
	 * @param y y koordin�ta
	 */
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * L�pteti a j�t�kelem �llapot�t.
	 * @param g a j�t�kelemet tartalmaz� GameComponent
	 */
	public abstract void update(GameComponent g);
	
	/**
	 * Kirajzolja a j�t�kelem aktu�lis �llapot�t.
	 * @param g	a j�t�kelement tartalmaz� GameComponent
	 */
	public abstract void draw(Graphics g); 
}
