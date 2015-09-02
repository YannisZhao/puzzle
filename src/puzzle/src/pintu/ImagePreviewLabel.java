package pintu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class ImagePreviewLabel extends JLabel {

	/**
	 * Author Yannis Zhao
	 */
	private static final long serialVersionUID = 1L;
	
	private BufferedImage targetImage;// Image will be saved
	private Image image;// Image will be shown

	private Point originalPos = new Point(0, 0);
	private Point previousPos = new Point(0, 0);
	private Point currentPos = new Point(0, 0);
	private Point offsetPos = new Point(0, 0);

	private boolean isFirstDraw = true;

	int length = PiecePane.piecePaneLength;

	public ImagePreviewLabel(String string, int alignment) {
		super(string, alignment);

		targetImage = new BufferedImage(length, length,
				BufferedImage.TYPE_INT_RGB);

		setPreferredSize(new Dimension(length, length));
		// setBorder(new LineBorder(Color.GREEN));
		setAlignmentX(CENTER_ALIGNMENT);

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				previousPos = e.getPoint();
				// System.out.println("pressed:" + offsetPos);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				offsetPos.x = 0;
				offsetPos.y = 0;
				// System.out.println("Release" + offsetPos);
			}
		});
		this.addMouseMotionListener(new MouseAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				currentPos = e.getPoint();
				// System.out.println("Cur:" + currentPos);
				// System.out.println("Pre:" + previousPos);
				offsetPos.x = currentPos.x - previousPos.x;
				offsetPos.y = currentPos.y - previousPos.y;
				previousPos = currentPos;
				// System.out.println(offsetPos.x + "," + offsetPos.y);
				ImagePreviewLabel.this.repaint();
			}

		});
	}

	@Override
	public void paint(Graphics g) {
		// g.clearRect(//clear component
		// 0,
		// 0,
		// ImagePreviewLabel.preferredSize.width,
		// ImagePreviewLabel.preferredSize.height);
		super.paint(g);
		moveImage(g);
		drawLine(this, PiecePane.counts, g);

	}

	private void moveImage(Graphics g) {
		image = null;

		if (ChooseImageDialog.image != null) {
			image = ChooseImageDialog.image.getImage();
		} else {
			return;
		}

		if (isFirstDraw && image != null) {
			// Show image
			g.drawImage(image, 0, 0, length, length, 0, 0, length, length, this);
			isFirstDraw = false;
		}

		originalPos.x -= offsetPos.x;
		originalPos.y -= offsetPos.y;

		// Can move
		if (originalPos.x > 0 && originalPos.y > 0
				&& image.getWidth(this) - originalPos.x > length
				&& image.getHeight(this) - originalPos.y > length
				&& image != null) {

			g.drawImage(image, 0, 0, length, length, originalPos.x,
					originalPos.y, length + originalPos.x, length
							+ originalPos.y, this);
		} else {// roll
			originalPos.x += offsetPos.x;
			originalPos.y += offsetPos.y;

			g.drawImage(image, 0, 0, length, length, originalPos.x,
					originalPos.y, length + originalPos.x, length
							+ originalPos.y, this);
		}
	}

	private void drawLine(JComponent c, int counts, Graphics g) {

		int stepLength = (int) (length / Math.sqrt(counts));

		// draw horizontal line
		for (int i = 0; i <= (int) Math.sqrt(counts); i++) {
			g.drawLine(0, i * stepLength, length, i * stepLength);
		}

		// draw vertical line
		for (int j = 0; j <= (int) Math.sqrt(counts); j++) {
			g.drawLine(j * stepLength, 0, j * stepLength, length);
		}
	}

	public boolean saveImage() {
		Graphics g;
		g = targetImage.getGraphics();

		g.drawImage(image, 0, 0, length, length, originalPos.x, originalPos.y,
				length + originalPos.x, length + originalPos.y, this);

		String fileName = ChooseImageDialog.selectedFile.getName();
		try {
			ImageIO.write(targetImage,
					fileName.substring(fileName.indexOf(".") + 1), new File(
							"images/" + fileName));
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
