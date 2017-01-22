package bombit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import bombit.game.GameComponent;

/**
 * A program fõablaka. Ebben zajlik a játék.
 *
 */
public class Frame extends JFrame{
	private static final long serialVersionUID = 1L;
	private final int FRAME_WIDTH = 726;
	private final int FRAME_HEIGHT = 650;
	JMenuBar menuBar;
	JMenu newGame, game;
	JMenuItem save, load, pause, player1, player2;
	GameComponent f;
	StartScreen sc;
	
/**
 * Létrehozza és inicializálja a frame-et és a menüt,
 * ezután kirajzolja a kezdõképernyõt.
 */
	Frame(){
		this.sc = new StartScreen();
		this.f = new GameComponent(false);
		initFrame();
		initMenu();
		add(sc);
		setResizable(false);
		setVisible(true);
	}
	
/**
 * Beállítja az ablak méretét, címét és a képernyõ
 * közepére igazítja az ablakot.
 */
	private void initFrame(){
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setTitle("Bomb It!");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
/**
 * Létrehozza a menüsort és annak elemeit sorrendben.
 * A menüpontokhoz beállítja az elvégzendõ mûveleteket.
 */
	private void initMenu(){
		//Menüsor
		menuBar = new JMenuBar();
				
		//1. menüpont: Új játék
		newGame = new JMenu("Új játék");
		player1 = new JMenuItem("Egy játékos mód");
		player1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Frame.this.remove(Frame.this.f);
				Frame.this.remove(Frame.this.sc);
				Frame.this.f = new GameComponent(true);
				Frame.this.add(Frame.this.f);
				f.requestFocusInWindow();
			}
		});
		player2 = new JMenuItem("Két játékos mód");
		player2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Frame.this.remove(Frame.this.f);
				Frame.this.remove(Frame.this.sc);
				Frame.this.f = new GameComponent(false);
				Frame.this.add(Frame.this.f);
				f.requestFocusInWindow();
			}
		});
		newGame.add(player1);
		newGame.add(player2);
		
		//2. menüpont: Játék
		//Szünet menüpont
		game = new JMenu("Játék");
		pause = new JMenuItem("Szünet");
		pause.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(f.isPaused()){
					pause.setText("Szünet");
					f.continueGame();
				}
				else{
					pause.setText("Folytatás");
					f.pauseGame();
				}
			}
		});
		game.add(pause);
		
		//Mentés menüpont
		save = new JMenuItem("Mentés");
		save.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Frame.this.repaint();
					FileOutputStream f = new FileOutputStream(new File("saves/save.ser"));
					ObjectOutputStream out = new ObjectOutputStream(f);
					out.writeObject(Frame.this.f);
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		game.add(save);
				
		//Betöltés menüpont
		load = new JMenuItem("Betöltés");
		load.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String file = JOptionPane.showInputDialog(Frame.this, "Add meg a fájl elérési útvonalát:");
					if(file != null){
						FileInputStream f = new FileInputStream(new File(file));
						ObjectInputStream in = new ObjectInputStream(f);
						GameComponent g = (GameComponent)in.readObject();
						in.close();
						
						Frame.this.remove(Frame.this.sc);
						Frame.this.remove(Frame.this.f);
						
						Frame.this.f = g;
						
						Frame.this.f.startTime();
						Frame.this.add(Frame.this.f);
						Frame.this.f.requestFocusInWindow();
					}
				} catch (IOException | ClassNotFoundException e1) {
					System.out.println("Nincs ilyen fájl!");
				}
			}
		});
		game.add(load);
		
		menuBar.add(newGame);
		menuBar.add(game);
		this.setJMenuBar(menuBar);
	}
}
