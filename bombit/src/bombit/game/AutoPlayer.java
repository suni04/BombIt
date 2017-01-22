package bombit.game;

import java.util.Random;

/**
 * A g�pi j�t�kost megval�s�t� oszt�ly.
 */
public class AutoPlayer extends Pony {
	private static final long serialVersionUID = 1L;

	int mode = 0;
	
	public AutoPlayer(int which_pony) {
		super(which_pony);
	}
	
	/**
	 * A mode-t�l f�gg�en vagy balra-le, vagy jobbra-fel l�p egyet,
	 * ezut�n valamilyen val�sz�n�s�ggel lerak egy bomb�t a p�ly�ra.
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
	 * Friss�ti a p�ni �llapot�t. Minden 12. tickben gener�l egy l�p�st.
	 */
	@Override
	public void update(GameComponent g) {
		if(g.getTicks() % 12 == 0){
			generateMove(g);
		}
		super.update(g);
	}
}
