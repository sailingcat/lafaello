package de.javasoft.plaf.synthetica.styles;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

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
public class ScrollPaneStyle extends StyleWrapper {

	private static ScrollPaneStyle instance = new ScrollPaneStyle();

	public static ScrollPaneStyle getStyle(SynthStyle style, JComponent c,
			Region region) {
		instance.setStyle(style);
		return instance;
	}

	private ScrollPaneStyle() {
	}

	@Override
	public Color getColor(SynthContext sc, ColorType type) {
		JScrollPane scrollPane = (JScrollPane) sc.getComponent();
		Component component = scrollPane.getViewport().getView();
		if (component == null)
			return synthStyle.getColor(sc, type);
		Color color = component.getBackground();
		if (component.getBackground() == null
				|| (component.getBackground() instanceof ColorUIResource))
			color = Color.WHITE;
		if ((component instanceof JEditorPane)
				&& (component.getBackground() instanceof ColorUIResource)) {
			JEditorPane editorPane = (JEditorPane) component;
			if (!editorPane.isEditable())
				color = UIManager.getColor("Synthetica.editorPane.lockedColor");
			if (!editorPane.isEnabled())
				color = UIManager
						.getColor("Synthetica.editorPane.disabledColor");
		} else if ((component instanceof JTextArea)
				&& (component.getBackground() instanceof ColorUIResource)) {
			JTextArea textArea = (JTextArea) component;
			if (!textArea.isEditable())
				color = UIManager.getColor("Synthetica.textArea.lockedColor");
			else if (!textArea.isEnabled())
				color = UIManager.getColor("Synthetica.textArea.disabledColor");
		}
		return new ColorUIResource(color);
	}

}
