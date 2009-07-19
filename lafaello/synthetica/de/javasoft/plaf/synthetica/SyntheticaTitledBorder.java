package de.javasoft.plaf.synthetica;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.UIManager;
import javax.swing.border.Border;

import de.javasoft.plaf.synthetica.painter.ImagePainter;

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
public class SyntheticaTitledBorder implements Border {

	public SyntheticaTitledBorder() {
	}

	public Insets getBorderInsets(Component arg0) {
		Insets sInsets = (Insets) UIManager
				.get("Synthetica.titledBorder.insets");
		return sInsets;
	}

	public boolean isBorderOpaque() {
		return false;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		String imagePath = "Synthetica.titledBorder";
		imagePath = (String) UIManager.get(imagePath);
		Insets sInsets = (Insets) UIManager
				.get("Synthetica.titledBorder.insets");
		Insets dInsets = sInsets;
		ImagePainter imagePainter = new ImagePainter(g, x, y, w, h, imagePath,
				sInsets, dInsets, 0, 0);
		imagePainter.drawBorder();
	}
}
