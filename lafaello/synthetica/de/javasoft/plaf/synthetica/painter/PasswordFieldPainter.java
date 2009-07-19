package de.javasoft.plaf.synthetica.painter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JPasswordField;
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
public class PasswordFieldPainter extends SynthPainter {

	private static SynthPainter instance = new PasswordFieldPainter();

	public static SynthPainter getInstance() {
		return instance;
	}

	private PasswordFieldPainter() {
	}

	@Override
	public void paintPasswordFieldBackground(SynthContext sc, Graphics g,
			int x, int y, int w, int h) {
		JPasswordField passwordField = (JPasswordField) sc.getComponent();
		Color background = passwordField.getBackground();
		boolean opaque = passwordField.isOpaque();
		String imagePath = "Synthetica.passwordField.border.locked";
		if (!passwordField.isEditable()
				&& (String) UIManager.get(imagePath) != null
				&& opaque
				&& (background == null || (background instanceof ColorUIResource))) {
			imagePath = (String) UIManager.get(imagePath);
			Insets sInsets = (Insets) UIManager
					.get("Synthetica.passwordField.border.insets");
			Insets dInsets = sInsets;
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.draw();
			return;
		}
		if (background == null || (background instanceof ColorUIResource))
			passwordField.setBackground(new ColorUIResource(Color.WHITE));
		if (!passwordField.isEditable()
				&& (background instanceof ColorUIResource) && opaque) {
			Color lockedColor = UIManager
					.getColor("Synthetica.passwordField.lockedColor");
			g.setColor(lockedColor);
			g.fillRect(x, y, w, h);
		}
	}

	@Override
	public void paintPasswordFieldBorder(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		JPasswordField passwordField = (JPasswordField) sc.getComponent();
		if (passwordField.getParent() instanceof JComboBox)
			return;
		Color background = passwordField.getBackground();
		String imagePath = "Synthetica.passwordField.border";
		if ((sc.getComponentState() & 8) > 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".disabled").toString();
		else if (!passwordField.isEditable()
				&& (String) UIManager.get((new StringBuilder(String
						.valueOf(imagePath))).append(".locked").toString()) != null
				&& (background == null || (background instanceof ColorUIResource)))
			return;
		imagePath = (String) UIManager.get(imagePath);
		Insets sInsets = (Insets) UIManager
				.get("Synthetica.passwordField.border.insets");
		Insets dInsets = sInsets;
		ImagePainter imagePainter = new ImagePainter(g, x, y, w, h, imagePath,
				sInsets, dInsets, 0, 0);
		imagePainter.drawBorder();
	}

}
