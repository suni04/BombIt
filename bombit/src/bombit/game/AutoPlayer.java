package bombit.game;

import java.util.Random;

/**
 * A gépi játékost megvalósító osztály.
 */
public class AutoPlayer extends Pony {
	private static final long serialVersionUID = 1L;

	int mode = 0;
	
	public AutoPlayer(int which_pony) {
		super(which_pony);
	}
	
	/**
	 * A mode-tól függõen vagy balra-le, vagy jobbra-fel lép egyet,
	 * ezután valamilyen valószínûséggel lerak egy bombát a pályára.
	 */
	public void generateMove(GameComponent g){
		int dirx, diry;
		Random rand = new Random();
		if(mode == 0){
			if(rand.nextBoolean()){
				dirx = -1;
				diry = 0;
			} else{
				dirx = 0;
				diry = 1;
			}
		} else{
			if(rand.nextBoolean()){
				dirx = 1;
				diry = 0;
			} else{
				dirx = 0;
				diry = -1;
			}
		}
		
		startMove(g, dirx, diry);
		
		if(rand.nextInt(30) == 0){
			plantBomb(g);
			mode = 1 - mode;
		}
	}

	/**
	 * Frissíti a póni állapotát. Minden 12. tickben generál egy lépést.
	 */
	@Override
	public void update(GameComponent g) {
		if(g.getTicks() % 12 == 0){
			generateMove(g);
		}
		super.update(g);
	}
}
