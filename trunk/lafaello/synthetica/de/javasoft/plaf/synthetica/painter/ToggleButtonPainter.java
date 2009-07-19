package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JToggleButton;
import javax.swing.JToolBar;
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
public class ToggleButtonPainter extends SynthPainter {

	private static SynthPainter instance = new ToggleButtonPainter();

	public static SynthPainter getInstance() {
		return instance;
	}

	private ToggleButtonPainter() {
	}

	@Override
	public void paintToggleButtonBackground(SynthContext context, Graphics g,
			int x, int y, int w, int h) {
		JToggleButton button = (JToggleButton) context.getComponent();
		java.awt.Container parent = button.getParent();
		boolean hover = (context.getComponentState() & 2) > 0;
		boolean selected = (context.getComponentState() & 0x200) > 0;
		if ((parent instanceof JToolBar)
				&& !UIManager
						.getBoolean("Synthetica.toolBar.buttons.paintBorder")
				&& !hover && !selected)
			return;
		if (button.isBorderPainted() || button.getText() == null
				|| button.getText().length() == 0) {
			if (!button.isBorderPainted() && !hover && !selected)
				return;
			Insets sInsets = (Insets) UIManager
					.get("Synthetica.toggleButton.border.insets");
			Insets dInsets = sInsets;
			String imagePath = "Synthetica.toggleButton";
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".border").toString();
			if (selected)
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".selected").toString();
			if (!button.isEnabled())
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".disabled").toString();
			if (hover) {
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".hover").toString();
				if (parent instanceof JToolBar)
					imagePath = "Synthetica.toolBar.button.border.hover";
			}
			imagePath = (String) UIManager.get(imagePath);
			if (imagePath == null)
				return;
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.draw();
		}
	}

}
