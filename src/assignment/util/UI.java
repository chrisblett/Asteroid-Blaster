package assignment.util;

import java.awt.Font;
import java.awt.Graphics2D;

public class UI {
	
	// Since text starts drawing from the bottom left, 
	// it makes more sense to centre, then allow user to adjust accordingly.
	public static void drawCenteredText(String text, Font f, Graphics2D g, int yOffset) {
		g.setFont(f);
		g.drawString(text, 
					 Constants.FRAME_WIDTH / 2 - g.getFontMetrics().stringWidth(text) / 2, 
					 Constants.FRAME_HEIGHT / 2 + yOffset);
	}
}
