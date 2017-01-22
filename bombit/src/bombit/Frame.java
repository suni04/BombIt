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
 * A program f�ablaka. Ebben zajlik a j�t�k.
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
 * L�trehozza �s inicializ�lja a frame-et �s a men�t,
 * ezut�n kirajzolja a kezd�k�perny�t.
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
 * Be�ll�tja az ablak m�ret�t, c�m�t �s a k�perny�
 * k�zep�re igaz�tja az ablakot.
 */
	private void initFrame(){
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setTitle("Bomb It!");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
/**
 * L�trehozza a men�sort �s annak elemeit sorrendben.
 * A men�pontokhoz be�ll�tja az elv�gzend� m�veleteket.
 */
	private void initMenu(){
		//Men�sor
		menuBar = new JMenuBar();
				
		//1. men�pont: �j j�t�k
		newGame = new JMenu("�j j�t�k");
		player1 = new JMenuItem("Egy j�t�kos m�d");
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
		player2 = new JMenuItem("K�t j�t�kos m�d");
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
		
		//2. men�pont: J�t�k
		//Sz�net men�pont
		game = new JMenu("J�t�k");
		pause = new JMenuItem("Sz�net");
		pause.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(f.isPaused()){
					pause.setText("Sz�net");
					f.continueGame();
				}
				else{
					pause.setText("Folytat�s");
					f.pauseGame();
				}
			}
		});
		game.add(pause);
		
		//Ment�s men�pont
		save = new JMenuItem("Ment�s");
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
				
		//Bet�lt�s men�pont
		load = new JMenuItem("Bet�lt�s");
		load.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String file = JOptionPane.showInputDialog(Frame.this, "Add meg a f�jl el�r�si �tvonal�t:");
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
					System.out.println("Nincs ilyen f�jl!");
				}
			}
		});
		game.add(load);
		
		menuBar.add(newGame);
		menuBar.add(game);
		this.setJMenuBar(menuBar);
	}
}
