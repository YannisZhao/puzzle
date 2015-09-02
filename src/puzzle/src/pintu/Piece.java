package pintu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

class Piece extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static String imageFileName = "images/Desert.jpg";
	static ImageIcon image;// Target image
	static ImageIcon[] pieceImageIcon = new ImageIcon[PiecePane.counts];

	public static void setPieceImageIcon(ImageIcon[] pieceImageIcon) {
		Piece.pieceImageIcon = pieceImageIcon;
	}

	private int length;

	public int getX() {
		return getBounds().x;
	}

	public int getY() {
		return getBounds().y;
	}

	public Point getLocation() {
		return new Point(getX(), getY());
	}

	public int getLength() {
		return length;
	}

	// default size
	public Piece(int length, int index) {
		image = new ImageIcon(imageFileName);
		this.length = length;
		//		System.out.println(this.index);
		this.setSize(length, length);
		this.setLocation((index % (int) Math.sqrt(PiecePane.counts)) * length,
				(index / (int) Math.sqrt(PiecePane.counts)) * length);
		this.setBackground(Color.WHITE);
		
		if (index == PiecePane.counts - 1) {
			pieceImageIcon[index] = new ImageIcon("");
		} else {
			pieceImageIcon[index] = getImageIcon(length, index);
		}
		this.setIcon(pieceImageIcon[index]);
		// this.repaint();
	}

	private ImageIcon getImageIcon(int length, int index) {
		BufferedImage bufferedImage = new BufferedImage(length, length,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedImage.getGraphics();
		if (Piece.image != null) {
			// System.out.println("drawing..." + index);
			int newX = (index % (int) Math.sqrt(PiecePane.counts)) * length;
			int newY = (index / (int) Math.sqrt(PiecePane.counts)) * length;
			g.drawImage(image.getImage(), 0, 0, length, length, newX, newY,
					newX + length, newY + length, this);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				ImageIO.write(
						bufferedImage,
						imageFileName.substring(imageFileName.indexOf(".") + 1),
						byteArrayOutputStream);
				return new ImageIcon(byteArrayOutputStream.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		return null;

	}

	// Move method
	public void move(Direction dir) {

		switch (dir) {
		case UP:
			this.setLocation(getX(), getY() - length);
			break;
		case DOWN:
			this.setLocation(getX(), getY() + length);
			break;
		case LEFT:
			this.setLocation(getX() - length, getY());
			break;
		case RIGHT:
			this.setLocation(getX() + length, getY());
			break;
		}
	}

}
