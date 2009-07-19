package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
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
public class SplitPanePainter extends SynthPainter {

	private static SynthPainter instance = new SplitPanePainter();

	public static SynthPainter getInstance() {
		return instance;
	}

	private SplitPanePainter() {
	}

	@Override
	public void paintSplitPaneDividerForeground(SynthContext sc, Graphics g,
			int x, int y, int w, int h, int orientation) {
		Insets sInsets = (Insets) UIManager
				.get("Synthetica.splitPaneDivider.background.insets");
		Insets dInsets = sInsets;
		String gripPath = null;
		String imagePath = "Synthetica.splitPaneDivider";
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
		if ((sc.getComponentState() & 2) > 0)
			gripPath = (new StringBuilder(String.valueOf(gripPath))).append(
					".hover").toString();
		imagePath = (String) UIManager.get(imagePath);
		gripPath = (String) UIManager.get(gripPath);
		if (imagePath != null) {
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.draw();
		}
		if (gripPath != null) {
			Image image = (new ImageIcon(SyntheticaLookAndFeel.class
					.getResource(gripPath))).getImage();
			int gripWidth = image.getWidth(null);
			int gripHeight = image.getHeight(null);
			int xPos = x + (w - gripWidth) / 2;
			int yPos = y + (h - gripHeight) / 2;
			if (orientation == 1 && w - 2 < gripWidth)
				return;
			if (orientation == 0 && h - 2 < gripHeight)
				return;
			g.drawImage(image, xPos, yPos, null);
		}
	}
}
