package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import java.awt.Insets;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
public class ArrowButtonPainter extends SynthPainter {

	private static SynthPainter instance = new ArrowButtonPainter();

	public static SynthPainter getInstance() {
		return instance;
	}

	private ArrowButtonPainter() {
	}

	@Override
	public void paintArrowButtonBackground(SynthContext context, Graphics g,
			int x, int y, int w, int h) {
		JButton arrowButton = (JButton) context.getComponent();
		String name = arrowButton.getName();
		java.awt.Component parent = arrowButton.getParent();
		String imagePath = "Synthetica.arrowButton";
		if (name != null && name.startsWith("SplitPaneDivider."))
			return;
		if (w <= 12 || h <= 12) {
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".8x8.background").toString();
			if ((context.getComponentState() & 4) > 0)
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".pressed").toString();
			else if ((context.getComponentState() & 8) > 0)
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".disabled").toString();
			else if ((context.getComponentState() & 2) > 0)
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".hover").toString();
			imagePath = (String) UIManager.get(imagePath);
			Insets sInsets = (Insets) UIManager
					.get("Synthetica.arrowButton.8x8.background.insets");
			Insets dInsets = sInsets;
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.draw();
			return;
		}
		if (parent instanceof JComboBox) {
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".x.background").toString();
			if ((context.getComponentState() & 4) > 0)
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".pressed").toString();
			else if ((context.getComponentState() & 8) > 0)
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".disabled").toString();
			else if ((context.getComponentState() & 2) > 0)
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".hover").toString();
			imagePath = (String) UIManager.get(imagePath);
			Insets sInsets = (Insets) UIManager
					.get("Synthetica.arrowButton.x.background.insets");
			Insets dInsets = sInsets;
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.draw();
			return;
		}
		int direction = 0;
		try {
			Class clazz = Class
					.forName("javax.swing.plaf.synth.SynthArrowButton");
			Field field = clazz.getDeclaredField("direction");
			field.setAccessible(true);
			direction = (int) field.getLong(arrowButton);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Insets sInsets = null;
		if (direction == 3 || direction == 7) {
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".x").toString();
			sInsets = (Insets) UIManager
					.get("Synthetica.arrowButton.x.background.insets");
		} else {
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".y").toString();
			sInsets = (Insets) UIManager
					.get("Synthetica.arrowButton.y.background.insets");
		}
		imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
				".background").toString();
		if ((context.getComponentState() & 4) > 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".pressed").toString();
		else if ((context.getComponentState() & 8) > 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".disabled").toString();
		else if ((context.getComponentState() & 2) > 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".hover").toString();
		imagePath = (String) UIManager.get(imagePath);
		Insets dInsets = sInsets;
		ImagePainter imagePainter = new ImagePainter(g, x, y, w, h, imagePath,
				sInsets, dInsets, 0, 0);
		imagePainter.draw();
	}

	@Override
	public void paintArrowButtonForeground(SynthContext context, Graphics g,
			int x, int y, int w, int h, int direction) {
		String imagePath = "Synthetica.arrow";
		String size = "";
		if (w < 16 || h < 16) {
			size = ".8x8";
			x += (w - 8) / 2;
			y += ((h - 8) + 1) / 2;
			w = 8;
			h = 8;
		} else if (w != 16 || h != 16) {
			x += (w - 16) / 2;
			y += ((h - 16) + 1) / 2;
			w = 16;
			h = 16;
		}
		imagePath = (new StringBuilder(String.valueOf(imagePath))).append(size)
				.toString();
		if (direction == 1)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".up").toString();
		else if (direction == 5)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".down").toString();
		else if (direction == 7)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".left").toString();
		else if (direction == 3)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".right").toString();
		Insets sInsets = new Insets(0, 0, 0, 0);
		Insets dInsets = new Insets(0, 0, 0, 0);
		if ((context.getComponentState() & 2) > 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".hover").toString();
		else if ((context.getComponentState() & 4) > 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".pressed").toString();
		else if ((context.getComponentState() & 8) > 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".disabled").toString();
		imagePath = (String) UIManager.get(imagePath);
		ImagePainter imagePainter = new ImagePainter(g, x, y, w, h, imagePath,
				sInsets, dInsets, 0, 0);
		imagePainter.draw();
	}

}
