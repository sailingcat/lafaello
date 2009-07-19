package de.javasoft.plaf.synthetica.painter;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.plaf.DimensionUIResource;
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
public class ToolBarPainter extends SynthPainter {

	private static SynthPainter instance = new ToolBarPainter();

	public static SynthPainter getInstance() {
		return instance;
	}

	private ToolBarPainter() {
	}

	@Override
	public void paintSeparatorForeground(SynthContext context, Graphics g,
			int x, int y, int w, int h, int orientation) {
		String imagePath = "Synthetica.toolBarSeparator.image";
		if (orientation == 1)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".y").toString();
		else
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".x").toString();
		imagePath = (String) UIManager.get(imagePath);
		if (imagePath == null)
			return;
		if (SyntheticaLookAndFeel.getToolbarSeparatorDimension() == null) {
			Image image = (new ImageIcon(SyntheticaLookAndFeel.class
					.getResource(imagePath))).getImage();
			int iWidth = image.getWidth(null);
			int iHeight = image.getHeight(null);
			javax.swing.JToolBar.Separator separator = (javax.swing.JToolBar.Separator) context
					.getComponent();
			int width = 0;
			int height = 0;
			Component components[] = separator.getParent().getComponents();
			for (int i = 0; i < components.length; i++) {
				Component c = components[i];
				if (!(c instanceof javax.swing.JToolBar.Separator)) {
					if (c.getMinimumSize().width > width)
						width = c.getMinimumSize().width;
					if (c.getMinimumSize().height > height)
						height = c.getMinimumSize().height;
				}
			}

			DimensionUIResource newSize = null;
			if (orientation == 1)
				newSize = new DimensionUIResource(iWidth, height);
			else
				newSize = new DimensionUIResource(width, iHeight);
			separator.setSeparatorSize(newSize);
			separator.revalidate();
		}
		Insets sInsets = (Insets) UIManager
				.get("Synthetica.toolBarSeparator.image.insets");
		Insets dInsets = sInsets;
		ImagePainter imagePainter = new ImagePainter(g, x, y, w, h, imagePath,
				sInsets, dInsets, 0, 0);
		imagePainter.draw();
	}

	@Override
	public void paintToolBarBackground(SynthContext context, Graphics g, int x,
			int y, int w, int h) {
		JToolBar toolBar = (JToolBar) context.getComponent();
		String imagePath = (String) UIManager
				.get("Synthetica.toolBar.background");
		if (imagePath != null && toolBar.isOpaque()) {
			Insets sInsets = (Insets) UIManager
					.get("Synthetica.toolBar.background.insets");
			Insets dInsets = sInsets;
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.draw();
		}
	}
}
