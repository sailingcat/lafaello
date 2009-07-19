package de.javasoft.plaf.synthetica.painter;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JTextArea;
import javax.swing.JViewport;
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
public class TextAreaPainter extends SynthPainter {

	private static SynthPainter instance = new TextAreaPainter();

	public static SynthPainter getInstance() {
		return instance;
	}

	private TextAreaPainter() {
	}

	@Override
	public void paintTextAreaBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		JTextArea textArea = (JTextArea) sc.getComponent();
		Color background = textArea.getBackground();
		if (background == null || (background instanceof ColorUIResource))
			textArea.setBackground(new ColorUIResource(Color.WHITE));
		boolean hasViewportParent = textArea.getParent() instanceof JViewport;
		boolean noBackgroundImage = UIManager
				.get("Synthetica.textArea.border.locked") == null;
		if (!textArea.isEditable()
				&& (background == null || (background instanceof ColorUIResource))
				&& noBackgroundImage && hasViewportParent) {
			Color color = UIManager.getColor("Synthetica.textArea.lockedColor");
			g.setColor(color);
			g.fillRect(x, y, w, h);
		} else if (!textArea.isEnabled()
				&& (background == null || (background instanceof ColorUIResource))
				&& noBackgroundImage && hasViewportParent) {
			Color color = UIManager
					.getColor("Synthetica.textArea.disabledColor");
			g.setColor(color);
			g.fillRect(x, y, w, h);
		}
	}

}
