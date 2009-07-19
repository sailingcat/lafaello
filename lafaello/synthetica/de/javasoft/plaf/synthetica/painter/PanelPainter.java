package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;

import javax.swing.JPanel;
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
public class PanelPainter extends SynthPainter {

	private static SynthPainter instance = new PanelPainter();

	public static SynthPainter getInstance() {
		return instance;
	}

	private PanelPainter() {
	}

	@Override
	public void paintPanelBackground(SynthContext context, Graphics g, int x,
			int y, int w, int h) {
		JPanel panel = (JPanel) context.getComponent();
		java.awt.Color background = panel.getBackground();
		if (panel.isOpaque() && background != null
				&& !(background instanceof ColorUIResource)) {
			g.setColor(background);
			g.fillRect(x, y, w, h);
		}
	}

}
