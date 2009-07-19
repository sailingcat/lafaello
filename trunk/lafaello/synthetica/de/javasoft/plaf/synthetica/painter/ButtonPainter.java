package de.javasoft.plaf.synthetica.painter;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.WeakHashMap;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

import de.javasoft.plaf.synthetica.SyntheticaTitlePane;

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
public class ButtonPainter extends SynthPainter {

	private static SynthPainter instance = new ButtonPainter();

	private static WeakHashMap buttons = new WeakHashMap();

	public static SynthPainter getInstance() {
		return instance;
	}

	private ButtonPainter() {
	}

	private boolean isToolBarComponent(Component c) {
		java.awt.Container parent = c.getParent();
		if (parent instanceof JToolBar)
			return true;
		if (parent != null)
			return isToolBarComponent(((parent)));
		else
			return false;
	}

	@Override
	public void paintButtonBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		JButton button = (JButton) sc.getComponent();
		java.awt.Container parent = button.getParent();
		boolean hover = (sc.getComponentState() & 2) > 0;
		boolean pressed = (sc.getComponentState() & 4) > 0;
		if (parent != null) {
			if ((parent instanceof BasicInternalFrameTitlePane)
					&& !UIManager
							.getBoolean("Synthetica.internalFrame.titlePane.buttons.paintBorder"))
				return;
			if ((parent instanceof SyntheticaTitlePane)
					&& !UIManager
							.getBoolean("Synthetica.rootPane.titlePane.buttons.paintBorder"))
				return;
			if (isToolBarComponent(button)) {
				Insets margin = (Insets) sc.getStyle().get(sc, "Button.margin");
				Insets bMargin = button.getMargin();
				if (bMargin == null)
					return;
				if ((bMargin.equals(margin) || !buttons.containsKey(button)
						&& (button.getText() == null || "".equals(button
								.getText().trim())))
						&& bMargin.equals(margin))
					button
							.setMargin(new Insets(bMargin.bottom
									- margin.bottom,
									bMargin.left - margin.left, bMargin.top
											- margin.top, bMargin.right
											- margin.right));
				buttons.put(button, null);
				boolean paintBorder = UIManager
						.getBoolean("Synthetica.toolBar.buttons.paintBorder");
				if (!paintBorder
						&& !hover
						&& (!pressed || !UIManager
								.getBoolean("Synthetica.toolBar.button.pressed.paintBorder")))
					return;
			}
		}
		if (button.isBorderPainted() || button.getText() == null
				|| button.getText().length() == 0) {
			if (!button.isBorderPainted() && !hover)
				return;
			Insets sInsets = (Insets) UIManager
					.get("Synthetica.button.border.insets");
			Insets dInsets = sInsets;
			String imagePath = "Synthetica.button";
			if (w <= 12 || h <= 12) {
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".12x12").toString();
				sInsets = (Insets) UIManager
						.get("Synthetica.button.12x12.border.insets");
				dInsets = sInsets;
			}
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".border").toString();
			if ((sc.getComponentState() & 4) > 0)
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".pressed").toString();
			else if (!button.isEnabled())
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".disabled").toString();
			else if (hover) {
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".hover").toString();
				if (isToolBarComponent(button))
					imagePath = "Synthetica.toolBar.button.border.hover";
			}
			imagePath = (String) UIManager.get(imagePath);
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.draw();
		}
	}

	@Override
	public void paintButtonBorder(SynthContext synthcontext, Graphics g1,
			int i, int j, int k, int l) {
	}

}
