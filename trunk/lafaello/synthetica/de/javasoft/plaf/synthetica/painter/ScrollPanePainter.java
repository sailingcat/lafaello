package de.javasoft.plaf.synthetica.painter;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.text.JTextComponent;

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
public class ScrollPanePainter extends SynthPainter {

	private static SynthPainter instance = new ScrollPanePainter();

	public static SynthPainter getInstance() {
		return instance;
	}

	private ScrollPanePainter() {
	}

	@Override
	public void paintScrollPaneBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		JScrollPane scrollPane = (JScrollPane) sc.getComponent();
		if (scrollPane.getViewport() == null
				|| scrollPane.getViewport().getView() == null)
			return;
		Component component = scrollPane.getViewport().getView();
		java.awt.Color background = component.getBackground();
		if (component instanceof JTextComponent) {
			JTextComponent text = (JTextComponent) component;
			String imagePath = "Synthetica.textField.border.locked";
			if (text instanceof JTextArea)
				imagePath = "Synthetica.textArea.border.locked";
			else if (text instanceof JEditorPane)
				imagePath = "Synthetica.editorPane.border.locked";
			if (text.isEnabled()
					&& !text.isEditable()
					&& UIManager.get(imagePath) != null
					&& (background == null || (background instanceof ColorUIResource))) {
				text.setOpaque(false);
				scrollPane.getViewport().setOpaque(false);
				imagePath = (String) UIManager.get(imagePath);
				Insets sInsets = (Insets) UIManager
						.get("Synthetica.textField.border.insets");
				Insets dInsets = sInsets;
				ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
						imagePath, sInsets, dInsets, 0, 0);
				imagePainter.draw();
				return;
			}
		}
	}

	@Override
	public void paintScrollPaneBorder(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		JScrollPane scrollPane = (JScrollPane) sc.getComponent();
		if (scrollPane.getViewport() == null
				|| scrollPane.getViewport().getView() == null)
			return;
		Component component = scrollPane.getViewport().getView();
		java.awt.Color background = component.getBackground();
		if (component instanceof JTextComponent) {
			JTextComponent text = (JTextComponent) component;
			String imagePath = "Synthetica.textField.border.locked";
			if (text instanceof JTextArea)
				imagePath = "Synthetica.textArea.border.locked";
			else if (text instanceof JEditorPane)
				imagePath = "Synthetica.editorPane.border.locked";
			if (text.isEnabled()
					&& !text.isEditable()
					&& UIManager.get(imagePath) != null
					&& (background == null || (background instanceof ColorUIResource)))
				return;
		}
		String imagePath = null;
		if (component.isEnabled())
			imagePath = (String) UIManager.get("Synthetica.scrollPane.border");
		else
			imagePath = (String) UIManager
					.get("Synthetica.scrollPane.border.disabled");
		Insets sInsets = (Insets) UIManager
				.get("Synthetica.scrollPane.border.insets");
		Insets dInsets = sInsets;
		ImagePainter imagePainter = new ImagePainter(g, x, y, w, h, imagePath,
				sInsets, dInsets, 0, 0);
		imagePainter.drawBorder();
	}

}
