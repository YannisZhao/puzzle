package pintu;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class PiecePane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static int piecePaneLength = 612;

	private Cursor cursor;

	static boolean isPlay = false;

	static int counts = 9; // Default pieces number

	private Piece pieces[] = new Piece[counts];;

	private ActionListener clickListener;

	public PiecePane() {
		this.setLayout(null);

		cursor = getToolkit().createCustomCursor(
				new ImageIcon("images/app/hand.png").getImage(),
				new Point(0, 0), "hand");
		setCursor(cursor);

		clickListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				MainGui.musicController.clickMusic.play();
				
				Piece piece = (Piece) e.getSource();

				if (!isPlay) {// Game not start,no action
					return;
				}

				// If blank piece,do nothing
				if (piece == pieces[PiecePane.counts - 1]) {
					return;
				}
				// move
				Direction dir = getDirection(piece);
				// System.out.println(dir);
				if (dir == Direction.UP) {
					piece.move(Direction.UP);
					pieces[PiecePane.counts - 1].move(Direction.DOWN);
				} else if (dir == Direction.DOWN) {
					piece.move(Direction.DOWN);
					pieces[PiecePane.counts - 1].move(Direction.UP);
				} else if (dir == Direction.LEFT) {
					piece.move(Direction.LEFT);
					pieces[PiecePane.counts - 1].move(Direction.RIGHT);
				} else if (dir == Direction.RIGHT) {
					piece.move(Direction.RIGHT);
					pieces[PiecePane.counts - 1].move(Direction.LEFT);
				} else {
					return;
				}

				// judge weather matched
				if (isMatched()) {
					isPlay = false;
					JOptionPane.showMessageDialog(null, "You win!");
				}
			}

		};

		createPiece();

	}

	private void createPiece() {
//		System.out.println(pieces.length);
		int pieceLength = PiecePane.piecePaneLength
				/ (int) Math.sqrt(PiecePane.counts);
		for (int i = 0; i < PiecePane.counts; i++) {

			pieces[i] = new Piece(pieceLength, i);

			pieces[i].addActionListener(clickListener);
			this.add(pieces[i]);
		}

	}
	
	private Direction getDirection(Piece piece) {
		int blankX = pieces[counts - 1].getX();// blank piece's x position
		int blankY = pieces[PiecePane.counts - 1].getY();// blank piece's y
															// position
		int length = piece.getLength();// length of piece
		// Get Driection
		// System.out.println(length);
		if (piece == (Piece) this.getComponentAt(new Point(blankX, blankY
				+ length))) {
			return Direction.UP;
		} else if (piece == (Piece) this.getComponentAt(new Point(blankX,
				blankY - length))) {
			return Direction.DOWN;
		} else if (piece == (Piece) this.getComponentAt(new Point(blankX
				+ length, blankY))) {
			return Direction.LEFT;
		} else if (piece == (Piece) this.getComponentAt(new Point(blankX
				- length, blankY))) {
			return Direction.RIGHT;
		} else {
			return null;
		}

	}

	private boolean isMatched() {
		int originalX = 0;
		int originalY = 0;
		int currentX = 0;
		int currentY = 0;
		int length = pieces[0].getLength();

		for (int i = 0; i < PiecePane.counts; i++) {
			originalX = (i % 3) * length;
			originalY = (i / 3) * length;
			currentX = pieces[i].getX();
			currentY = pieces[i].getY();
			// System.out.println(originalX+":"+originalY+"\t"+currentX+":"+currentY);
			// current position is not original position
			if (originalX != currentX || originalY != currentY) {
				return false;
			}
		}
		return true;
	}

	public void shuffle() {
		List<Integer> indexList = new ArrayList<>();
		for (int i = 0; i < PiecePane.counts; i++) {
			indexList.add(i);
		}
		Collections.shuffle(indexList);
		int i = 0;
		int length = pieces[0].getLength();
		for (int index : indexList) {
			pieces[i].setLocation((index % (int) Math.sqrt(PiecePane.counts)) * length,
					(index / (int) Math.sqrt(PiecePane.counts)) * length);
			i++;
		}
	}

}
