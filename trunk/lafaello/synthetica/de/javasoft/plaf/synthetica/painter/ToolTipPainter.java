package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.WeakHashMap;

import javax.swing.JPanel;
import javax.swing.JToolTip;
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
public class ToolTipPainter extends SynthPainter {

	private static SynthPainter instance = new ToolTipPainter();

	private static WeakHashMap opaqued = new WeakHashMap();

	public static SynthPainter getInstance() {
		return instance;
	}

	private ToolTipPainter() {
	}

	@Override
	public void paintToolTipBackground(SynthContext context, Graphics g, int x,
			int y, int w, int h) {
		JToolTip toolTip = (JToolTip) context.getComponent();
		JPanel panel = (JPanel) toolTip.getParent();
		if (!opaqued.containsKey(toolTip)) {
			opaqued.put(toolTip, null);
			toolTip.setOpaque(false);
			panel.setOpaque(false);
			panel.repaint();
		}
		BufferedImage background = (BufferedImage) panel
				.getClientProperty("POPUP_BACKGROUND");
		if (background != null)
			g.drawImage(background, x, y, null);
		String imagePath = (String) UIManager
				.get("Synthetica.toolTip.background");
		Insets sInsets = (Insets) UIManager
				.get("Synthetica.toolTip.background.insets");
		Insets dInsets = sInsets;
		ImagePainter imagePainter = new ImagePainter(g, x, y, w, h, imagePath,
				sInsets, dInsets, 0, 0);
		imagePainter.draw();
	}

}
