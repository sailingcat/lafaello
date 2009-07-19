package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
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
public class ProgressBarPainter extends SynthPainter {

	private static SynthPainter instance = new ProgressBarPainter();

	public static SynthPainter getInstance() {
		return instance;
	}

	private ProgressBarPainter() {
	}

	@Override
	public void paintProgressBarBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		String imagePath = "Synthetica.progressBar";
		JProgressBar pb = (JProgressBar) sc.getComponent();
		int direction = pb.getOrientation();
		if (direction == 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".x").toString();
		else
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".y").toString();
		imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
				".background").toString();
		Insets sInsets = (Insets) UIManager.get((new StringBuilder(String
				.valueOf(imagePath))).append(".insets").toString());
		Insets dInsets = sInsets;
		if ((sc.getComponentState() & 8) > 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".disabled").toString();
		imagePath = (String) UIManager.get(imagePath);
		if (imagePath != null) {
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.draw();
		}
	}

	@Override
	public void paintProgressBarForeground(SynthContext context, Graphics g,
			int x, int y, int w, int h, int direction) {
		String imagePath = "Synthetica.progressBar";
		if (direction == 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".x").toString();
		else
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".y").toString();
		Insets sInsets = (Insets) UIManager.get((new StringBuilder(String
				.valueOf(imagePath))).append(".insets").toString());
		if (sInsets == null)
			sInsets = new Insets(0, 0, 0, 0);
		Insets dInsets = sInsets;
		if ((context.getComponentState() & 8) > 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".disabled").toString();
		imagePath = (String) UIManager.get(imagePath);
		if (direction == 0) {
			int minWidth = sInsets.left + sInsets.right;
			if (w < minWidth)
				return;
		} else {
			int minHeight = sInsets.top + sInsets.bottom;
			if (h < minHeight)
				return;
		}
		int width = w;
		int height = h;
		int fillPolicy = 0;
		if (!UIManager.getBoolean("Synthetica.progressBar.continuous")) {
			Image image = (new ImageIcon(SyntheticaLookAndFeel.class
					.getResource(imagePath))).getImage();
			int centerWidth = image.getWidth(null) - sInsets.left
					- sInsets.right;
			int centerHeight = image.getHeight(null) - sInsets.top
					- sInsets.bottom;
			width = ((w - sInsets.left - sInsets.right) / centerWidth)
					* centerWidth + sInsets.left + sInsets.right;
			height = ((h - sInsets.top - sInsets.bottom) / centerHeight)
					* centerHeight + sInsets.top + sInsets.bottom;
			fillPolicy = 1;
		}
		ImagePainter imagePainter = null;
		if (direction == 0) {
			imagePainter = new ImagePainter(g, x, y, width, h, imagePath,
					sInsets, dInsets, fillPolicy, 0);
			imagePainter.draw();
		} else {
			imagePainter = new ImagePainter(g, x, (y - height) + h, w, height,
					imagePath, sInsets, dInsets, 0, fillPolicy);
			imagePainter.draw();
		}
	}
}
