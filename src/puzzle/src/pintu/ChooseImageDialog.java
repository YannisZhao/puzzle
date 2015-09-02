package pintu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

class ChooseImageDialog extends JDialog {

	/**
	 * @author candy
	 */
	private static final long serialVersionUID = 1L;

	static boolean confirmClip = false;

	static ImageIcon image;
	static File selectedFile;// selected image

	private JPanel chooseFilePanel;
	private JLabel selectFileLabel;
	private JTextField fileTextField;
	private JButton browseBtn;
	private ImagePreviewLabel preView;
	JPanel infoPane;
	JPanel imageInfoPane;
	JLabel imageWidthLabel;
	JLabel imageHeightLabel;

	private JRadioButton nineButton;
	private JRadioButton thirtySixButton;
	private JRadioButton eightyOneButton;

	private ButtonGroup group;

	private JPanel selectPiecesCount;

	private JButton clipButton;

	public ChooseImageDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		chooseFilePanel = new JPanel();
		selectFileLabel = new JLabel("Select image: ");
		fileTextField = new JTextField("Select image file...", 40);
		browseBtn = new JButton("Browse");
		browseBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"JPG & PNG Images", "jpg", "png");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(ChooseImageDialog.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					selectedFile = chooser.getSelectedFile();
					ChooseImageDialog.image = new ImageIcon(selectedFile
							.getAbsolutePath());
					int imageWidth = ChooseImageDialog.image.getIconWidth();
					int imageHeight = ChooseImageDialog.image.getIconHeight();
					if (imageWidth < PiecePane.piecePaneLength
							|| imageHeight < PiecePane.piecePaneLength) {
						ChooseImageDialog.image = null;
						JOptionPane.showMessageDialog(ChooseImageDialog.this,
								"Image cannot less than "
										+ PiecePane.piecePaneLength + "x"
										+ PiecePane.piecePaneLength);
						return;
					}
					fileTextField.setText(selectedFile.getAbsolutePath());
					preView.setText("");
					preView.repaint();
					// preView.setIcon(image);
					imageWidthLabel.setText("Image Width: "
							+ image.getIconWidth());
					imageHeightLabel.setText("Image Height: "
							+ image.getIconHeight());
				}

			}
		});
		chooseFilePanel.add(selectFileLabel);
		chooseFilePanel.add(fileTextField);
		chooseFilePanel.add(browseBtn);

		preView = new ImagePreviewLabel("Image preview", JLabel.HORIZONTAL);

		infoPane = new JPanel(new BorderLayout());
		imageInfoPane = new JPanel();
		imageInfoPane.setLayout(new BorderLayout());
		imageInfoPane.setBorder(new TitledBorder(new LineBorder(Color.gray),
				"Image info"));
		imageWidthLabel = new JLabel("Image Width:    0");
		imageHeightLabel = new JLabel("Image Height:   0");
		imageInfoPane.add(imageWidthLabel, BorderLayout.NORTH);
		imageInfoPane.add(imageHeightLabel);

		ActionListener selectPiecesListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (nineButton.isSelected()) {
					PiecePane.counts = 9;
				} else if (thirtySixButton.isSelected()) {
					PiecePane.counts = 36;
				} else {
					PiecePane.counts = 81;
				}
				preView.repaint();// Redraw the line
			}
		};
		nineButton = new JRadioButton("9");
		nineButton.setSelected(true);
		nineButton.addActionListener(selectPiecesListener);
		thirtySixButton = new JRadioButton("36");
		thirtySixButton.addActionListener(selectPiecesListener);
		eightyOneButton = new JRadioButton("81");
		eightyOneButton.addActionListener(selectPiecesListener);

		// Group the radio buttons.
		group = new ButtonGroup();
		group.add(nineButton);
		group.add(thirtySixButton);
		group.add(eightyOneButton);
		selectPiecesCount = new JPanel();
		selectPiecesCount.setBorder(new TitledBorder(
				new LineBorder(Color.gray), "Select Pieces Count"));
		selectPiecesCount.add(nineButton);
		selectPiecesCount.add(thirtySixButton);
		selectPiecesCount.add(eightyOneButton);

		infoPane.add(imageInfoPane, BorderLayout.NORTH);
		infoPane.add(selectPiecesCount);
		clipButton = new JButton("Clip");
		clipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (image != null) {
					// Copy image file
					if (preView.saveImage()) {
						JOptionPane.showMessageDialog(ChooseImageDialog.this,
								"File copy success!");
						Piece.imageFileName = "images/"+ChooseImageDialog.selectedFile
								.getName();
						confirmClip = true;
					} else {
						JOptionPane.showMessageDialog(ChooseImageDialog.this,
								"File copy failed!");
					}
					//
				}
			}
		});
		infoPane.add(clipButton, BorderLayout.SOUTH);

		this.setLayout(new BorderLayout(5, 5));
		this.add(chooseFilePanel, BorderLayout.NORTH);
		this.add(preView);
		JPanel west = new JPanel();
		west.setPreferredSize(new Dimension(10, 612));
		JPanel south = new JPanel();
		south.setPreferredSize(new Dimension(612, 10));
		this.add(west, BorderLayout.WEST);
		this.add(south, BorderLayout.SOUTH);
		this.add(infoPane, BorderLayout.EAST);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

}
