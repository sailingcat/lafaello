package de.javasoft.plaf.synthetica.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.CellRendererPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.table.JTableHeader;

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
public class LabelPainter extends SynthPainter {

	private static SynthPainter instance = new LabelPainter();

	public static SynthPainter getInstance() {
		return instance;
	}

	private LabelPainter() {
	}

	@Override
	public void paintLabelBackground(SynthContext sc, Graphics g, int x, int y,
			int w, int h) {
		JLabel label = (JLabel) sc.getComponent();
		Component parent = label.getParent();
		if (label.getName() != null && label.getName().startsWith("ComboBox."))
			label.setOpaque(true);
		if (label.getName() != null
				&& label.getName().startsWith("TableHeader.")) {
			JTableHeader header = (JTableHeader) parent.getParent();
			label.setFont(header.getFont());
			Color color = (Color) UIManager
					.get("Synthetica.tableHeader.gridColor");
			g.setColor(color);
			g.drawLine((x + w) - 1, y, (x + w) - 1, (y + h) - 1);
			return;
		}
		if (!(parent instanceof CellRendererPane)
				|| !(parent.getParent() instanceof JComboBox)) {
			super.paintLabelBackground(sc, g, x, y, w, h);
			return;
		}
		JComboBox cb = (JComboBox) parent.getParent();
		Color cbBackground = cb.getBackground();
		Color textColor = null;
		Color backColor = null;
		if (!cb.isEnabled()) {
			textColor = UIManager
					.getColor("Synthetica.comboBox.disabled.textColor");
			backColor = UIManager
					.getColor("Synthetica.comboBox.disabled.backgroundColor");
		} else if (cb.hasFocus()) {
			textColor = UIManager
					.getColor("Synthetica.comboBox.focused.textColor");
			if (UIManager.get("Synthetica.comboBox.border.locked") == null)
				backColor = UIManager
						.getColor("Synthetica.comboBox.focused.backgroundColor");
			else
				backColor = cbBackground;
		} else if (!cb.isEditable()
				&& (cbBackground instanceof ColorUIResource)) {
			textColor = UIManager
					.getColor("Synthetica.comboBox.locked.textColor");
			backColor = UIManager
					.getColor("Synthetica.comboBox.locked.backgroundColor");
		} else if (!cb.isEditable()
				&& !(cbBackground instanceof ColorUIResource))
			backColor = cbBackground;
		label.setForeground(textColor);
		String imagePath = "Synthetica.comboBox.border.locked";
		if (cb.isEnabled()
				&& !cb.isEditable()
				&& (String) UIManager.get(imagePath) != null
				&& (cbBackground == null || (cbBackground instanceof ColorUIResource))) {
			imagePath = (String) UIManager.get(imagePath);
			Insets sInsets = (Insets) UIManager
					.get("Synthetica.comboBox.border.insets");
			Insets dInsets = new Insets(0, 0, 0, 0);
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.drawCenter();
			return;
		} else {
			g.setColor(backColor);
			g.fillRect(x, y, w, h);
			return;
		}
	}

}
