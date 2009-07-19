package de.javasoft.plaf.synthetica.painter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
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
public class ComboBoxPainter extends SynthPainter {

	private static SynthPainter instance = new ComboBoxPainter();

	public static SynthPainter getInstance() {
		return instance;
	}

	private ComboBoxPainter() {
	}

	@Override
	public void paintComboBoxBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		JComboBox cb = (JComboBox) sc.getComponent();
		Color background = cb.getBackground();
		boolean opaque = cb.isOpaque();
		String imagePath = "Synthetica.comboBox.border.locked";
		Insets sInsets = (Insets) UIManager
				.get("Synthetica.comboBox.border.insets");
		if (cb.isEnabled()
				&& !cb.isEditable()
				&& UIManager.get(imagePath) != null
				&& (background == null || (background instanceof ColorUIResource))) {
			imagePath = (String) UIManager.get(imagePath);
			Insets dInsets = sInsets;
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.draw();
			return;
		}
		int borderSize = 1;
		int x1 = x + borderSize;
		int y1 = y + borderSize;
		int w1 = w - borderSize * 2;
		int h1 = h - borderSize * 2;
		if (!cb.isEnabled() && opaque) {
			Color color = UIManager
					.getColor("Synthetica.comboBox.disabledColor");
			g.setColor(color);
			g.fillRect(x1, y1, w1, h1);
		} else if ((sc.getComponentState() & 0x100) > 0
				&& opaque
				&& (background == null || (background instanceof ColorUIResource))) {
			Color color = UIManager.getColor("Synthetica.comboBox.focused");
			g.setColor(color);
			g.fillRect(x1, y1, w1, h1);
		} else if (!cb.isEditable() && (background instanceof ColorUIResource)
				&& opaque) {
			Color color = UIManager.getColor("Synthetica.comboBox.lockedColor");
			g.setColor(color);
			g.fillRect(x1, y1, w1, h1);
		} else if (cb.isEditable()
				&& (background == null || (background instanceof ColorUIResource))
				&& opaque) {
			Color color = new ColorUIResource(Color.WHITE);
			g.setColor(color);
			g.fillRect(x1, y1, w1, h1);
		}
		if (cb.hasFocus() && cb.isEnabled() && !cb.isEditable()
				&& UIManager.get(imagePath) != null && background != null
				&& !(background instanceof ColorUIResource)) {
			imagePath = (String) UIManager.get(imagePath);
			Insets dInsets = sInsets;
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.drawBorder();
			return;
		} else {
			return;
		}
	}

	@Override
	public void paintComboBoxBorder(SynthContext sc, Graphics g, int x, int y,
			int w, int h) {
		JComboBox cb = (JComboBox) sc.getComponent();
		Color background = cb.getBackground();
		String imagePath = "Synthetica.comboBox.border";
		if (!cb.isEditable()
				&& (String) UIManager.get((new StringBuilder(String
						.valueOf(imagePath))).append(".locked").toString()) != null
				&& (background == null || (background instanceof ColorUIResource))) {
			return;
		} else {
			imagePath = (String) UIManager.get(imagePath);
			Insets sInsets = (Insets) UIManager
					.get("Synthetica.comboBox.border.insets");
			Insets dInsets = sInsets;
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.draw();
			return;
		}
	}

}
