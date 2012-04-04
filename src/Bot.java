import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.*;
import java.io.*;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;

// http://www.popcap.com/games/free/bejeweled2

public class Bot {

	public static int getAverage(BufferedImage img, Rectangle rect)
	{
		int R = 0;
		int G = 0;
		int B = 0;

		for (int y = rect.y; y < rect.y + rect.height; ++y)
		{
			for (int x = rect.x; x < rect.x + rect.width; ++x)
			{
				int rgb = img.getRGB(x, y);
				// Skip all white or all black

				if (rgb == Color.black.getRGB() || rgb == Color.white.getRGB())
				{
					//System.out.println("Was White or Black");
					continue;
				}

			R += (rgb & 0x00FF0000) >> 16;
			G += (rgb & 0x0000FF00) >> 8;
			B += (rgb & 0x000000FF);
			}
		}

		R /= (rect.width * rect.height);
		G /= (rect.width * rect.height);
		B /= (rect.width * rect.height);

		int R_ = (int)R;
		int G_ = (int)G;
		int B_ = (int)B;

		int retVal = ((R_ & 0x000000FF) << 16) | ((G_ & 0x000000FF) << 8) | (B_ & 0x000000FF);

		return retVal;
	}

	enum COLORS
	{
		RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE, WHITE, HYPER_CUBE, INVALID
	}

	static Color WHITE = new Color(240, 240, 240);
	static Color BLUE = new Color(30, 130, 220);
	static Color RED = new Color(254, 92, 92);
	static Color ORANGE = new Color(240, 150, 55);
	static Color PURPLE = new Color(198, 25, 198);
	static Color GREEN = new Color(54, 220, 54);
	static Color YELLOW = new Color(214, 211, 58);

	public static boolean isColor(Color c, int r, int g, int b)
	{
		return 	Math.abs(c.getRed() - r) <= 40 &&
		Math.abs(c.getGreen() 	- g) <= 40 &&
		Math.abs(c.getBlue() 	- b) <= 45;
	}

	public static boolean white(int r, int g, int b)
	{
		int diff1 = Math.abs(r - g);
		int diff2 = Math.abs(r - b);
		int diff3 = Math.abs(g - b);

		if (diff1 < 10 && diff2 < 10 && diff3 < 10)
			return true;
		else
			return false;
	}

	public static boolean red(int r, int g, int b)
	{
		int diff = Math.abs(g - b);

		if (r > g && r > b && diff < 20)
		{
			return true;
		} else {
			return false;
		}
	}

	public static boolean blue(int r, int g, int b)
	{
		int diff = Math.abs(r - g);

		if (b > r && b > g && r < 100)
		{
			return true;
		} else {
			return false;
		}
	}

	public static boolean green(int r, int g, int b)
	{
		int diff = Math.abs(r - b);

		if (g > r && g > b && diff < 20)
		{
			return true;
		} else {
			return false;
		}
	}

	public static boolean yellow(int r, int g, int b)
	{
		int diff = Math.abs(r - g);

		if (r > b && g > b && diff < 30 && b < 125)
		{
			return true;
		} else {
			return false;
		}
	}

	public static boolean orange(int r, int g, int b)
	{
		int diff = Math.abs(r - g);

		if (r > b && g > b && r > g && diff >= 30 && b < 150)
		{
			return true;
		} else {
			return false;
		}
	}

	public static boolean hypercube(int r, int g, int b)
	{
		int diff = Math.abs(g - b);

		if (g > r && b > r && diff < 10 && r < 150 && g < 150 && b < 150)
		{
			return true;
		}

		return false;
	}

	public static boolean purple(int r, int g, int b)
	{
		int diff = Math.abs(r - b);

		if (r > g && b > g && g < 150 && diff < 30)
		{
			return true;
		} else {
			return false;
		}
	}

	public static COLORS getColor(int avg)
	{
		int r = (avg & 0x00FF0000) >> 16;
		int g = (avg & 0x0000FF00) >> 8;
		int b = (avg & 0x000000FF);

		if (white(r, g, b))
		{
			return COLORS.WHITE;
		} 
		else if (blue(r, g, b))
		{
			return COLORS.BLUE;
		}
		else if (red(r, g, b))
		{
			return COLORS.RED;
		}
		else if (orange(r, g, b))
		{
			return COLORS.ORANGE;
		}
		else if (purple(r, g, b))
		{
			return COLORS.PURPLE;
		} 
		else if (green(r, g, b))
		{
			return COLORS.GREEN;
		}
		else if (yellow(r, g, b))
		{
			return COLORS.YELLOW;
		} 
		else if (hypercube(r, g, b))
		{
			return COLORS.HYPER_CUBE;
		}
		else 
		{
			return COLORS.INVALID;
		}
	}

	public static boolean createGameBoard(COLORS[][] array, int[][] colors)
	{
		for (int y = 0; y < 8; ++y)
		{
			for (int x = 0; x < 8; ++x)
			{
				int c = colors[x][y];
				int r = (c & 0x00FF0000) >> 16;
			int g = (c & 0x0000FF00) >> 8;
		int b = (c & 0x000000FF);

		if (white(r, g, b))
		{
			array[x][y] = COLORS.WHITE;
		} 
		else if (blue(r, g, b))
		{
			array[x][y] = COLORS.BLUE;
		}
		else if (red(r, g, b))
		{
			array[x][y] = COLORS.RED;
		}
		else if (orange(r, g, b))
		{
			array[x][y] = COLORS.ORANGE;
		}
		else if (purple(r, g, b))
		{
			array[x][y] = COLORS.PURPLE;
		} 
		else if (green(r, g, b))
		{
			array[x][y] = COLORS.GREEN;
		}
		else if (yellow(r, g, b))
		{
			array[x][y] = COLORS.YELLOW;
		} 
		else if (hypercube(r, g, b))
		{
			array[x][y] = COLORS.HYPER_CUBE;
		}
		else 
		{
			System.out.println("Unidentified color: (" + x + "," + y + ")");
			System.out.println("R: " + r + " G: " + g + " B: " + b);
			array[x][y] = COLORS.INVALID;
			//return false;
		}
			}
		}

		return true;
	}

	public static int[] getRGB(int value)
	{
		int[] v = new int[3];
		v[0] = (value & 0x00FF0000) >> 16;
		v[1] = (value& 0x0000FF00) >> 8;
		v[2] = (value & 0x000000FF);

		return v;
	}

	public static void printRGB(int v[])
	{
		System.out.println("R: " + v[0] + " G: " + v[1] + " B: " + v[2]);
	}

	/* WHITE - 240, 240, 240
	 * BLUE	 - 030, 130, 220
	 * RED	 - 254, 092, 092
	 * ORANGE- 243, 152, 053
	 * PURPLE- 198, 025, 198
	 * GREEN - 054, 220, 054
	 * YELLOW- 214, 211, 058
	 */

	public static int averageRGB(int rgb1, int rgb2)
	{
		int[] c1 = getRGB(rgb1);
		int[] c2 = getRGB(rgb2);

		c1[0] = (c1[0] + c2[0]) / 2;
		c1[1] = (c1[1] + c2[1]) / 2;
		c1[2] = (c1[2] + c2[2]) / 2;

		int returnVal = 0;
		returnVal |= (c1[0] << 16) & 0x00FF0000;
		returnVal |= (c1[1] << 8) & 0x0000FF00;
		returnVal |= (c1[2]) & 0x000000FF;

		return returnVal;
	}

	static int X_OFFSET = 31;
	static int Y_OFFSET = 27;
	static int X_WIDTH = 84;
	static int Y_HEIGHT = 84;

	static int AREA_X = 45;
	static int AREA_Y = 14;
	static int AREA_WIDTH = 3;
	static int AREA_HEIGHT = 4;
	static int AREA_SECOND_Y = 54;
	public static void runGame(Rectangle rect, Robot robot)
	{
		JFrame frame = new JFrame("Debugger 2");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(150, 150);
		PaintPanel paintPanel = new PaintPanel();
		PaintPanel paintPanel2 = new PaintPanel();
		frame.setLayout(null);
		frame.getContentPane().setLayout(null);
		frame.add(paintPanel);
		frame.add(paintPanel2);
		paintPanel.setLocation(1, 1);
		paintPanel.setSize(AREA_WIDTH, AREA_HEIGHT);
		paintPanel2.setLocation(50, 1);
		paintPanel2.setSize(X_WIDTH, Y_HEIGHT);
		//frame.pack();
		frame.setVisible(true);

		// The magic numbers are 43x42 rectangle boxes
		// and start at (17,17) from the start x and y

		Rectangle boardRect = new Rectangle(rect.x + X_OFFSET, rect.y + Y_OFFSET, 
				X_WIDTH * 8, Y_HEIGHT * 8);

		BufferedImage imgBoard = robot.createScreenCapture(boardRect);

		Rectangle rArea = new Rectangle(AREA_X, AREA_Y, AREA_WIDTH, AREA_HEIGHT);

		// Determine what colors are where
		int colors[][] = new int[8][8];
		for (int y = 0; y < 8; ++y)
		{
			for (int x = 0; x < 8; ++x)
			{
				Rectangle avgRect = new Rectangle((x * X_WIDTH) + rArea.x, (y * Y_HEIGHT) + rArea.y, rArea.width, rArea.height);
				int rgb1 = getAverage(imgBoard, avgRect);

				avgRect.y += AREA_SECOND_Y;
				int rgb2 = getAverage(imgBoard, avgRect);

				colors[x][y] = averageRGB(rgb1, rgb2);

				COLORS color = getColor(colors[x][y]);
				frame.setTitle(color.name());
				if (color == COLORS.INVALID)
				{
					System.out.print("INVALID COLOR: (" + x + "," + y + ") ");
					printRGB(getRGB(colors[x][y]));
				}

				paintPanel.setImage(imgBoard.getSubimage(avgRect.x, avgRect.y, avgRect.width, avgRect.height));
				BufferedImage bi = imgBoard.getSubimage(x*X_WIDTH, y*Y_HEIGHT, X_WIDTH, Y_HEIGHT);
				Graphics g = bi.getGraphics();
				g.setColor(Color.RED);
				g.drawRect(rArea.x-1, rArea.y-1, rArea.width+1, rArea.height+1);
				g.drawRect(rArea.x-1, rArea.y-1 + AREA_SECOND_Y, rArea.width+1, rArea.height+1);
				paintPanel2.setImage(bi);
				try {
					System.in.read();
					System.in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Thread.sleep(1000);

				System.out.print(String.format("%09d ", colors[x][y]));
			}
			System.out.println();
		}

		System.out.println();

		// Now dump the contents and see what we have
		for (int y = 0; y < 8; ++y)
		{
			for (int x = 0; x < 8; ++x)
			{
				int rgb = colors[x][y];

				int r = (rgb & 0x00FF0000) >> 16;
			int g = (rgb & 0x0000FF00) >> 8;
			int b = (rgb & 0x000000FF);
			System.out.print(String.format("%03d|%03d|%03d ", r, g, b));
			}
			System.out.println();
		}

		System.out.println("Successfully created gameboard");

		while (true)
		{
			Point mouse_loc = MouseInfo.getPointerInfo().getLocation();
			if (mouse_loc.x > 0 && mouse_loc.y > 0 && mouse_loc.x != lastx || mouse_loc.y != lasty)
			{
				// The user moved the mouse so they probably want to click something
				// so lets wait until they're ready
				System.out.println("Mouse moved, pausing...");
				System.out.println("Press 'Enter' to resume or type 'q' to quit");
				try {

					if (System.in.read() == 'q')
					{
						break;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			int ten_tries = 10;

			convertImageToColors(rect, robot, colors);
			COLORS[][] gameBoard = new COLORS[8][8];
			while (!createGameBoard(gameBoard, colors))
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) { e.printStackTrace(); }	
				convertImageToColors(rect, robot, colors);
			}
			Rectangle move = findValidMove(gameBoard);

			System.out.println("Move: (" + move.x + "," + move.y + ") to (" + move.width + "," + move.height + ")");

			if (move.x < 0 && move.y < 0)
			{
				if (ten_tries == 0)
				{
					System.out.println("Found no moves!");
					break;
				} else {
					System.out.println("Try number: " + ten_tries);
					ten_tries--;
				}
			} else {
				ten_tries = 10;
			}

			performMove(boardRect, move, robot);

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static int lastx = -1;
	static int lasty = -1;

	public static void performMove(Rectangle baseRect, Rectangle move, Robot robot)
	{
		int xDiv2 = X_WIDTH / 2;
		int yDiv2 = Y_HEIGHT / 2;

		int sx = baseRect.x + (X_WIDTH * move.x) + xDiv2;
		int sy = baseRect.y + (Y_HEIGHT * move.y) + yDiv2;

		int ex = baseRect.x + (X_WIDTH * move.width) + xDiv2;
		int ey = baseRect.y + (Y_HEIGHT * move.height) + yDiv2;

		System.out.println("Coords: " + sx + " " + sy);
		System.out.println("End:    " + ex + " " + ey);

		robot.mouseMove(sx, sy);
		//robot.delay(60);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		//robot.delay(60);
		robot.mouseMove(ex, ey);
		//robot.delay(60);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);

		lastx = ex;
		lasty = ey;
	}

	public static void printColors(COLORS[][] colors)
	{
		for (int y = 0; y < 8; ++y)
		{
			for (int x = 0; x < 8; ++x)
			{
				System.out.print(String.format("%1s ", colors[x][y].name().substring(0, 1)));
			}
			System.out.println();
		}
	}

	public static boolean matches(COLORS[][] board)
	{
		for (int y = 7; y >= 0; --y)
		{
			for (int x = 0; x < 8; ++x)
			{
				COLORS curColor = board[x][y];
				if (curColor == COLORS.HYPER_CUBE)
				{
					return true;
				}

				if (curColor == COLORS.INVALID)
				{
					continue;
				}

				if (x < 6)
				{
					if (board[x+1][y] == curColor && board[x+2][y] == curColor)
					{
						System.out.println("-----------MY BOARD-----------");
						printColors(board);

						System.out.println("COLOR: " + curColor.name());
						System.out.println("Match from: (" + x + "," + y + 
								") to (" + (x+2) + "," + y + ")");
						return true;
					}
				}

				if (y > 1)
				{
					if (board[x][y-1] == curColor && board[x][y-2] == curColor)
					{
						System.out.println("-----------MY BOARD-----------");
						printColors(board);

						System.out.println("COLOR: " + curColor.name());
						System.out.println("Match from: (" + x + "," + y + 
								") to (" + x + "," + (y-2) + ")");
						return true;
					}
				}
			}
		}

		return false;
	}

	public static COLORS[][] deepCopy(COLORS[][] orig)
	{
		COLORS[][] retVal = new COLORS[8][8];

		for (int y = 0; y < 8; ++y)
		{
			for (int x = 0; x < 8; ++x)
			{
				retVal[x][y] = orig[x][y];
			}
		}

		return retVal;
	}

	public static boolean inRange(int x, int y, int min, int max)
	{
		return x >= min && x <= max && y >= min && y <= max;
	}

	public static Rectangle findValidMove(final COLORS[][] board)
	{
		Rectangle retVal = new Rectangle(-1, -1, -1, -1);

		printColors(board);

		boolean invalids = false;
		for (int y = 7; y >= 0; --y)
		{
			for (int x = 0; x < 8; ++x)
			{
				if (board[x][y] == COLORS.INVALID)
				{
					invalids = true;
				}

				// Try moving in 4 possible directions
				int tx = x - 1;
				int ty = y;

				Rectangle move = null;
				// Swap the two colors and then check if any are 3 in a row
				COLORS[][] m1 = deepCopy(board);
				if (inRange(tx, ty, 0, 7))
				{
					COLORS temp = m1[x][y];
					m1[x][y] = m1[tx][ty];
					m1[tx][ty] = temp;

					if (matches(m1))
					{
						System.out.println("In move set 1");
						return new Rectangle(x, y, tx, ty);
					}
				}

				tx = x + 1;
				ty = y;
				COLORS[][] m2 = deepCopy(board);
				if (inRange(tx, ty, 0, 7))
				{
					COLORS temp = m2[x][y];
					m2[x][y] = m2[tx][ty];
					m2[tx][ty] = temp;

					if (matches(m2))
					{
						System.out.println("BOARD FROM HERE");
						printColors(m2);
						System.out.println("In move set 2");
						return new Rectangle(x, y, tx, ty);
					}
				}

				tx = x;
				ty = y - 1;
				COLORS[][] m3 = deepCopy(board);
				if (inRange(tx, ty, 0, 7))
				{
					COLORS temp = m3[x][y];
					m3[x][y] = m3[tx][ty];
					m3[tx][ty] = temp;

					if (matches(m3))
					{
						System.out.println("In move set 3");
						return new Rectangle(x, y, tx, ty);
					}
				}

				tx = x;
				ty = y + 1;
				COLORS[][] m4 = deepCopy(board);
				if (inRange(tx, ty, 0, 7))
				{
					COLORS temp = m4[x][y];
					m4[x][y] = m4[tx][ty];
					m4[tx][ty] = temp;

					if (matches(m4))
					{
						System.out.println("In move set 4");
						return new Rectangle(x, y, tx, ty);
					}
				}

			}
		}

		if (invalids)
		{
			retVal.y = 1;
		}

		return retVal;
	}

	public static void convertImageToColors(Rectangle rect, Robot robot, int colors[][])
	{
		Rectangle boardRect = new Rectangle(rect.x + X_OFFSET, rect.y + Y_OFFSET, X_WIDTH * 8, Y_HEIGHT * 8);

		BufferedImage imgBoard = robot.createScreenCapture(boardRect);

		Rectangle rArea = new Rectangle(AREA_X, AREA_Y, AREA_WIDTH, AREA_HEIGHT);

		// Determine what colors are where
		for (int y = 0; y < 8; ++y)
		{
			for (int x = 0; x < 8; ++x)
			{
				Rectangle avgRect = new Rectangle((x * X_WIDTH) + rArea.x, (y * Y_HEIGHT) + rArea.y, rArea.width, rArea.height);
				int rgb1 = getAverage(imgBoard, avgRect);

				avgRect.y += AREA_SECOND_Y;
				int rgb2 = getAverage(imgBoard, avgRect);

				colors[x][y] = averageRGB(rgb1, rgb2);
			}
		}
	}

	public boolean checkAgainstReference(BufferedImage ref, BufferedImage bi, int x, int y)
	{
		for (int ry = 0; ry < ref.getHeight(); ++ry)
		{
			for (int rx = 0; rx < ref.getWidth(); ++rx)
			{
				int ref_rgb = ref.getRGB(rx, ry);
				if (ref_rgb == 1)
				{

				}

				int bi_rgb = bi.getRGB(x, y);
			}
		}

		return true;
	}

	public static String rgbToString(int RGB)
	{
		int R = (RGB & 0x00FF0000) >> 16;
			int G = (RGB & 0x0000FF00) >> 8;
			int B = (RGB & 0x000000FF);

			return "R: " + R + " G: " + G + " B: " + B;
	}



	public static Rectangle FindRectangle(BufferedImage reference, 
			Robot robot)
	{
		final BufferedImage bi =
			robot.createScreenCapture(
					new Rectangle(0, 0, 1920, 1200));

		Rectangle retVal = new Rectangle(-1, -1, -1, -1);

		int w = reference.getWidth();
		int h = reference.getHeight();

		System.out.println("Reference image is: " + w + "x" + h);
		// 26x26 rectangles
		/*
		Raster topLeft = reference.getData(
				new Rectangle(0, 0, 26, 26));
		Raster topRight = reference.getData(
				new Rectangle(w - 26, h - 26, 26, 26));*/

		// A little debugging JFrame
		JFrame frame = new JFrame("Debugger");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(60, 60);
		PaintPanel paintPanel = new PaintPanel();
		PaintPanel paintPanel2 = new PaintPanel();
		frame.setLayout(null);
		frame.getContentPane().setLayout(null);
		frame.add(paintPanel);
		frame.add(paintPanel2);

		paintPanel.setLocation(1, 1);
		paintPanel.setSize(20, 20);
		paintPanel2.setLocation(30, 1);
		paintPanel2.setSize(20, 20);
		//frame.getContentPane().add(paintPanel);
		frame.setVisible(true);

		// Get the average for the top left
		int IMG_START_X = 12;
		int IMG_START_Y = 7;
		int IMG_WIDTH = 18;
		int IMG_HEIGHT = 18;

		Rectangle rectTL = 
			new Rectangle(IMG_START_X, IMG_START_Y, IMG_WIDTH, IMG_HEIGHT);//IMG_START_X, IMG_START_Y, IMG_WIDTH, IMG_HEIGHT);
		Rectangle rectTR = new Rectangle(w - IMG_WIDTH - IMG_START_X, IMG_START_Y, IMG_WIDTH, IMG_HEIGHT);
		//w - IMG_WIDTH - IMG_START_X, IMG_START_Y, IMG_WIDTH, IMG_HEIGHT);

		//reference.getSubimage(rectTR.x, rectTR.y, rectTR.width, rectTR.height);
		paintPanel2.setImage(reference.getSubimage(rectTL.x, rectTL.y, rectTL.width, rectTL.height));

		int topLAvg = getAverage(reference, rectTL);
		int topRAvg = getAverage(reference, rectTR);

		System.out.println("Top L Avg: " + rgbToString(topLAvg));
		System.out.println("    As Integer: " + topLAvg);
		System.out.println("Top R Avg: " + rgbToString(topRAvg));
		System.out.println("    As Integer: " + topRAvg);

		System.out.println("Searching for coordinates...");

		for (int y = 0; y < bi.getHeight() - IMG_HEIGHT - 1; ++y)
		{
			for (int x = 0; x < bi.getWidth() - IMG_WIDTH - 1; ++x)
			{
				//System.out.println("At: " + x + " : " + y);

				Rectangle rect1 = new Rectangle(x, y, IMG_WIDTH, IMG_HEIGHT);
				//Rectangle rect2 = 
				//	new Rectangle(x + (w - IMG_WIDTH - 2 * IMG_START_X), y, IMG_WIDTH, IMG_HEIGHT);
				//x + w - IMG_WIDTH - IMG_START_X, y, IMG_WIDTH, IMG_HEIGHT);

				paintPanel.setImage(bi.getSubimage(rect1.x, rect1.y, rect1.width, rect1.height));

				int avg1 = getAverage(bi, rect1);
				//int avg2 = getAverage(bi, rect2);

				int diff1 = Math.abs(avg1 - topLAvg);
				//int diff2 = Math.abs(avg2 - topRAvg);

				//System.out.println("AVG: " + avg1 + ", " + avg2);
				//System.out.println("DIFF: " + diff1 + ", " + diff2);

				if (diff1 < 10)// && diff2 < 10)
				{
					System.out.println("Found coordinates:");
					System.out.println("AVG: " + avg1 + ", " );//+ avg2);
					System.out.println("DIFF: " + diff1 + ", ");// + diff2);

					retVal.x = x- IMG_START_X;
					retVal.y = y - IMG_START_Y;
					retVal.width = w;
					retVal.height = h;
					System.out.println(retVal);
					return retVal;
				}
				/*
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		}
		System.out.println("Couldn't find coordinates");

		return retVal;
	}

	public static void main(String[] args)
	{
		Robot robot = null;

		try {
			robot = new Robot();
		} catch (AWTException e) { e.printStackTrace(); }

		BufferedImage reference = null;
		try {
			reference = ImageIO.read(new File("reference.bmp"));
		} catch (IOException e) { e.printStackTrace(); }


		Rectangle coords = FindRectangle(reference, robot);
		if (coords.x >= 0)
		{
			System.out.println("Found: " + coords.toString());
			// Okay we found coordinates time to start our game
			runGame(coords, robot);
		}
	}
}
