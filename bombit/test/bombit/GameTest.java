package bombit;

import org.junit.Assert;
import org.junit.Test;

import bombit.game.Field;
import bombit.game.GameComponent;
import bombit.game.Pony;


public class GameTest {
	//Póni metódusainak tesztelése
	@Test
	public void testGetX(){
		Pony p = new Pony(1);
		Assert.assertEquals(0, p.getX());
	}
	
	@Test
	public void testGetY(){
		Pony p = new Pony(1);
		Assert.assertEquals(0, p.getY());
	}
	
	@Test
	public void testGetDirx(){
		Pony p = new Pony(1);
		Assert.assertEquals(0, p.getdirX());
	}
	
	@Test
	public void testGetDiry(){
		Pony p = new Pony(1);
		Assert.assertEquals(1, p.getdirY());
	}
	
	@Test
	public void testMove(){
		Pony p = new Pony(1);
		p.move(new GameComponent(false), 1, 0);
		Assert.assertEquals(1, p.getdirX());
		Assert.assertEquals(0, p.getdirY());
		Assert.assertEquals(0, p.getX());
		Assert.assertEquals(0, p.getY());
	}
	
	@Test
	public void testGetReach(){
		Pony p = new Pony(1);
		Assert.assertEquals(1, p.getReach());
	}
	
	//Field osztály metódusainak tesztelése
	@Test
	public void testFieldGetX(){
		Field f = new Field(1);
		Assert.assertEquals(18, f.getX());
	}
	
	@Test
	public void testFieldGetY(){
		Field f = new Field(1);
		Assert.assertEquals(15, f.getY());
	}
	
	//GameComponent osztály metódusainak tesztelése
	@Test
	public void testGCompisBombThere(){
		GameComponent g = new GameComponent(false);
		Assert.assertEquals(false, g.isBombThere(1, 1));
	}
	
	@Test
	public void testGCompisFlameThere(){
		GameComponent g = new GameComponent(false);
		Assert.assertEquals(false, g.isFlameThere(1, 1));
	}
}
