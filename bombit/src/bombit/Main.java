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
	 * A program belépési pontja. Betölti a mûködéshez szükséges
	 * képeket, majd létrehozza a frame-et amiben a játék zajlik.
	 * @param args a parancssori argumentumok
	 */
	public static void main(String args[]){
		try {
//			Betölti az összes képet ami a játékhoz szükséges
			GameComponent.loadImages();
			Bomb.loadImages();
			Flame.loadImages();
			Pony.loadImages();
			Field.loadImages();
			PowerUp.loadImages();
			StartScreen.loadImage();
		} catch (IOException e) {
			System.out.println("Nem sikerült betölteni a képeket!");
		}
		
//		Létrehozza a frame-et amiben a játék fut
		new Frame();
	}
}
