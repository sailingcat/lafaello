package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;

/**
 * <p>
 * Lafaello Copyright (C) 2009 (based on GPL version 1.4 of Synthetica -
 * http://www.javasoft.de/jsf/public/products/synthetica)
 * </p>
 * 
 * <p>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * </p>
 * 
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * </p>
 * 
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see {@link http://www.gnu.org/licenses}.
 * </p>
 * 
 * @author lafaello@gmail.com
 * 
 */
public class ScrollBarPainter extends SynthPainter {

	private static SynthPainter instance = new ScrollBarPainter();

	public static SynthPainter getInstance() {
		return instance;
	}

	private ScrollBarPainter() {
	}

	@Override
	public void paintScrollBarThumbBackground(SynthContext context, Graphics g,
			int x, int y, int w, int h, int orientation) {
		Insets sInsets = (Insets) UIManager
				.get("Synthetica.scrollBarThumb.background.insets");
		Insets dInsets = sInsets;
		String gripPath = null;
		String imagePath = "Synthetica.scrollBarThumb";
		if (orientation == 0) {
			gripPath = (new StringBuilder(String.valueOf(imagePath))).append(
					".x.grip").toString();
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".x.background").toString();
		} else {
			gripPath = (new StringBuilder(String.valueOf(imagePath))).append(
					".y.grip").toString();
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".y.background").toString();
		}
		if ((context.getComponentState() & 2) > 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".hover").toString();
		imagePath = (String) UIManager.get(imagePath);
		ImagePainter imagePainter = new ImagePainter(g, x, y, w, h, imagePath,
				sInsets, dInsets, 0, 0);
		imagePainter.draw();
		gripPath = (String) UIManager.get(gripPath);
		if (gripPath == null)
			return;
		Image image = (new ImageIcon(SyntheticaLookAndFeel.class
				.getResource(gripPath))).getImage();
		int gripWidth = image.getWidth(null);
		int gripHeight = image.getHeight(null);
		int xPos = x + (w - gripWidth) / 2;
		int yPos = y + (h - gripHeight) / 2;
		if (orientation == 0 && w - 4 <= gripWidth)
			return;
		if (orientation == 1 && h - 4 <= gripHeight) {
			return;
		} else {
			g.drawImage(image, xPos, yPos, null);
			return;
		}
	}

	@Override
	public void paintScrollBarTrackBackground(SynthContext context, Graphics g,
			int x, int y, int w, int h) {
		JScrollBar scrollBar = (JScrollBar) context.getComponent();
		Insets sInsets = (Insets) UIManager
				.get("Synthetica.scrollBarTrack.background.insets");
		Insets dInsets = sInsets;
		String imagePath = "Synthetica.scrollBarTrack";
		if (scrollBar.getOrientation() == 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".x.background").toString();
		else
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".y.background").toString();
		imagePath = (String) UIManager.get(imagePath);
		ImagePainter imagePainter = new ImagePainter(g, x, y, w, h, imagePath,
				sInsets, dInsets, 0, 0);
		imagePainter.draw();
	}
}
