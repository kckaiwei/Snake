import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	private final int board_Width = 300;
	private final int board_Height = 300;
	private final int dot_Size = 10;
	private final int all_Dots = (board_Width * board_Height)
			/ (dot_Size * dot_Size);
	private final int rand_Pos = 29;
	private final int delay = 140;

	private final int x[] = new int[all_Dots];
	private final int y[] = new int[all_Dots];

	private int dots;
	private int apple_Y;
	private int apple_X;
	private int cake_Y;
	private int cake_X;
	private int score = 0;
	private int cakeCounter = 5;

	private Image bodyImage, appleImage, cakeImage, headImage;

	private boolean leftDirection = false;
	private boolean rightDirection = false;
	private boolean upDirection = false;
	private boolean downDirection = false;
	private boolean inGame = true;
	private boolean cakeExist;

	private Timer timer;

	private void loadImages() {

		ImageIcon iIB = new ImageIcon("src/data/bodyImage.png");
		bodyImage = iIB.getImage();

		ImageIcon iIA = new ImageIcon("src/data/appleImage.png");
		appleImage = iIA.getImage();

		ImageIcon iIH = new ImageIcon("src/data/headImage.png");
		headImage = iIH.getImage();

		ImageIcon iIC = new ImageIcon("src/data/cakeImage.png");
		cakeImage = iIC.getImage();

	}

	private void initGame() {

		dots = 3;
		// draw dots
		for (int z = 0; z < dots; z++) {
			x[z] = 50 - z * 10;
			y[z] = 50;
		}

		placeApple();
		placeCake();

		timer = new Timer(delay, this);
		timer.start();

	}

	public Board() {
		addKeyListener(new keyListener());
		setBackground(Color.black);
		setFocusable(true);

		setPreferredSize(new Dimension(board_Width, board_Height));
		loadImages();
		initGame();

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);
	}

	private void doDrawing(Graphics g) {
		if (inGame) {
			g.drawImage(appleImage, apple_X, apple_Y, this);
			g.drawImage(cakeImage, cake_X, cake_Y, this);
			for (int z = 0; z < dots; z++) {
				if (z == 0) {
					g.drawImage(headImage, x[z], y[z], this);
				} else {
					g.drawImage(bodyImage, x[z], y[z], this);
				}
			}

			Toolkit.getDefaultToolkit().sync();
		} else {
			gameOver(g);
		}
	}

	private void gameOver(Graphics g) {
		String msg = "Game Over";
		String scoreMsg = "Your Score:" + score;
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = getFontMetrics(small);

		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(msg, (board_Width - metr.stringWidth(msg)) / 2,
				board_Height / 2);
		g.drawString(scoreMsg, (board_Width - metr.stringWidth(msg)) / 2,
				(board_Height / 2) + 40);

	}

	private void checkApple() {
		if ((x[0] == apple_X) && (y[0] == apple_Y)) {
			dots++;
			score += 100;
			cakeCounter++;
			placeApple();
		}
	}

	private void checkCake() {
		if ((x[0] == cake_X) && (y[0] == cake_Y)) {
			if (dots >= 2) {
				dots--;
			}
			score += 200;
			cakeExist = false;
			placeCake();
		}
	}

	private void move() {
		for (int z = dots; z > 0; z--) {
			x[z] = x[(z - 1)];
			y[z] = y[(z - 1)];
		}

		if (leftDirection) {
			x[0] -= dot_Size;
		}
		if (rightDirection) {
			x[0] += dot_Size;
		}
		if (upDirection) {
			y[0] -= dot_Size;
		}
		if (downDirection) {
			y[0] += dot_Size;
		}
	}

	private void checkCollision() {
		for (int z = dots; z > 0; z--) {
			if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
				inGame = false;
			}
		}

		if (y[0] >= board_Height) {
			inGame = false;
		}

		if (y[0] < 0) {
			inGame = false;
		}

		if (x[0] >= board_Width) {
			inGame = false;
		}
		if (x[0] < 0) {
			inGame = false;
		}
		if (!inGame) {
			timer.stop();
		}
	}

	private void placeApple() {
		int r = (int) (Math.random() * rand_Pos);
		apple_X = ((r * dot_Size));

		r = (int) (Math.random() * rand_Pos);
		apple_Y = ((r * dot_Size));
	}

	private void placeCake() {

//		if (cakeCounter >= 5) {
			int cR = (int) (Math.random() * rand_Pos);
			cake_X = ((cR * dot_Size));

			cR = (int) (Math.random() * rand_Pos);
			cake_Y = (cR * dot_Size);

			if (cake_X == apple_X && cake_Y == apple_Y) {
				placeCake();
			}
			cakeCounter = 0;
			cakeExist = true;
//		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (inGame) {
			checkApple();
			checkCake();

			checkCollision();
			move();
		}
		repaint();
	}

	private class keyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
				leftDirection = true;
				upDirection = false;
				downDirection = false;
			}

			if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
				rightDirection = true;
				upDirection = false;
				downDirection = false;
			}

			if ((key == KeyEvent.VK_UP) && (!downDirection)) {
				upDirection = true;
				rightDirection = false;
				leftDirection = false;
			}

			if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
				downDirection = true;
				leftDirection = false;
				rightDirection = false;
			}

		}
	}

}
