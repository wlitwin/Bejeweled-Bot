import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class PaintPanel extends JPanel
{
	public Image img;

	public void setImage(Image img)
	{
		this.img = img;
		this.repaint();
	}
	
	public void paint(Graphics g)
	{
		if (img == null)
		{
			g.fillRect(0, 0, getWidth(), getHeight());
		} else {
			g.drawImage(img, 0, 0, null);
		}
	}
}