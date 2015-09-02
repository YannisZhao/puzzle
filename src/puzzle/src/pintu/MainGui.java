package pintu;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import java.util.Locale;
import java.util.ResourceBundle;

public class MainGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PiecePane piecePane;
	private JMenuBar menuBar = null;
	private JMenu game = null;
	private JMenuItem playMenuItem = null;
	private JMenu imageMenu = null;
	private JMenuItem customize = null;
	private JRadioButtonMenuItem nineMenuItem;
	private JRadioButtonMenuItem thirtySixMenuItem;
	private JRadioButtonMenuItem eightyOneMenuItem;
	private ButtonGroup bg;
	private JMenu pieces;
	private JMenu settings;
	private JMenuItem bgMusicMenu;

	private ResourceBundle bundle;

	static MusicController musicController;

	/**
	 * 
	 */
	public void init() {
		// Load local language res file
		Locale locale = Locale.getDefault();
		bundle = ResourceBundle.getBundle("mess", locale);

		this.setIconImage(getToolkit().getImage("images/app/icon.jpg"));
		this.setTitle(bundle.getString("appTitle"));
		this.setLocation(290, 20);
		// this.setLocationRelativeTo(null);

		musicController = new MusicController();

		// Create Menu
		initMenu();

		piecePane = new PiecePane();
		piecePane.setPreferredSize(new Dimension(PiecePane.piecePaneLength,
				PiecePane.piecePaneLength));

		this.add(piecePane);
		this.pack();
		// this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initMenu() {
		menuBar = new JMenuBar();
		game = new JMenu("Game");
		playMenuItem = new JMenuItem("Play");
		menuBar.add(game);
		game.add(playMenuItem);
		playMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PiecePane.isPlay = true;
				piecePane.shuffle();
			}

		});

		// select iamge button listener
		ActionListener SelectImageListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = e.getActionCommand();

				if (name.equals("Customize")) {
					new ChooseImageDialog(MainGui.this, bundle.getString("dialogTitle"),true);
					if(ChooseImageDialog.confirmClip){
						restart();
					}
				} else {
					Piece.imageFileName="images/"+name;
					restart();
				}
			}

		};
		// select pieces menu listener
		ActionListener SelectPiecesListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				PiecePane.counts = Integer.parseInt(command);
				Piece.setPieceImageIcon(new ImageIcon[PiecePane.counts]);
				
				restart();
			}

		};
		imageMenu = new JMenu("Image");
		customize = new JMenuItem("Customize");
		customize.addActionListener(SelectImageListener);
		
		JMenuItem m;
		File[] files=new File("images/").listFiles();
		for(File f:files){
			if(f.isFile()){
				m=new JMenuItem(f.getName());
				m.addActionListener(SelectImageListener);
				imageMenu.add(m);
			}
		}

		nineMenuItem = new JRadioButtonMenuItem("9");
		nineMenuItem.addActionListener(SelectPiecesListener);
		thirtySixMenuItem = new JRadioButtonMenuItem("36");
		thirtySixMenuItem.addActionListener(SelectPiecesListener);
		eightyOneMenuItem = new JRadioButtonMenuItem("81");
		eightyOneMenuItem.addActionListener(SelectPiecesListener);
		//Set selected menu item
		if(PiecePane.counts==9){
			nineMenuItem.setSelected(true);
		}else if(PiecePane.counts==36){
			thirtySixMenuItem.setSelected(true);
		}else{
			eightyOneMenuItem.setSelected(true);
		}
		bg = new ButtonGroup();
		bg.add(nineMenuItem);
		bg.add(thirtySixMenuItem);
		bg.add(eightyOneMenuItem);
		pieces = new JMenu("Pieces");
		pieces.add(nineMenuItem);
		pieces.add(thirtySixMenuItem);
		pieces.add(eightyOneMenuItem);

		imageMenu.add(customize);
		imageMenu.addSeparator();
		imageMenu.add(pieces);
		menuBar.add(imageMenu);

		settings = new JMenu("Settings");
		bgMusicMenu = new JCheckBoxMenuItem("background music", true);
		bgMusicMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBoxMenuItem m = (JCheckBoxMenuItem) e.getSource();
				if (m.isSelected()) {
					musicController.backMusic.loop();
				} else {
					musicController.backMusic.stop();
				}
			}
		});
		settings.add(bgMusicMenu);
		menuBar.add(settings);

		this.setJMenuBar(menuBar);
	}
	
	public void restart() {
		//Dispose and restart MainGui
		musicController.backMusic.stop();
		MainGui.this.dispose();
		PiecePane.isPlay = false;
		new MainGui().init();
	}

	public MainGui() {

	}

	public static void main(String[] args) {
		new MainGui().init();
	}
}
