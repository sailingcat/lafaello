package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

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
public class CheckBoxPainter extends SynthPainter {

	private static SynthPainter instance = new CheckBoxPainter();

	public static SynthPainter getInstance() {
		return instance;
	}

	private CheckBoxPainter() {
	}

	@Override
	public void paintCheckBoxBackground(SynthContext context, Graphics g,
			int x, int y, int w, int h) {
		if ((context.getComponentState() & 0x100) > 0) {
			Insets sInsets = (Insets) UIManager
					.get("Synthetica.checkBox.focus.insets");
			Insets dInsets = sInsets;
			String imagePath = (String) UIManager
					.get("Synthetica.checkBox.focus");
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 1, 1);
			imagePainter.drawBorder();
		}
	}

	@Override
	public void paintCheckBoxBorder(SynthContext context, Graphics g, int x,
			int y, int w, int h) {
		JCheckBox cb = (JCheckBox) context.getComponent();
		if (cb.isBorderPainted()) {
			Insets sInsets = (Insets) UIManager
					.get("Synthetica.checkBox.border.insets");
			Insets dInsets = sInsets;
			String imagePath = (String) UIManager
					.get("Synthetica.checkBox.border");
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.drawBorder();
		}
	}

}
