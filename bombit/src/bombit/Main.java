package bombit;

import java.io.IOException;

import bombit.game.Bomb;
import bombit.game.Field;
import bombit.game.Flame;
import bombit.game.GameComponent;
import bombit.game.Pony;
import bombit.game.PowerUp;


public class Main {
	/**
	 * A program bel�p�si pontja. Bet�lti a m�k�d�shez sz�ks�ges
	 * k�peket, majd l�trehozza a frame-et amiben a j�t�k zajlik.
	 * @param args a parancssori argumentumok
	 */
	public static void main(String args[]){
		try {
//			Bet�lti az �sszes k�pet ami a j�t�khoz sz�ks�ges
			GameComponent.loadImages();
			Bomb.loadImages();
			Flame.loadImages();
			Pony.loadImages();
			Field.loadImages();
			PowerUp.loadImages();
			StartScreen.loadImage();
		} catch (IOException e) {
			System.out.println("Nem siker�lt bet�lteni a k�peket!");
		}
		
//		L�trehozza a frame-et amiben a j�t�k fut
		new Frame();
	}
}
