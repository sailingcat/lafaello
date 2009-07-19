package de.javasoft.plaf.synthetica;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;

import de.javasoft.plaf.synthetica.painter.ImagePainter;

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
public class GraphicsUtils extends SynthGraphicsUtils {

	public GraphicsUtils() {
	}

	@Override
	public void drawLine(SynthContext context, Object paintKey, Graphics g,
			int x1, int y1, int x2, int y2) {
		if ((paintKey instanceof String)
				&& ((String) paintKey).startsWith("Tree.")) {
			java.awt.Color color = null;
			if (paintKey.equals("Tree.horizontalLine"))
				color = UIManager
						.getColor("Synthetica.tree.line.color.horizontal");
			else if (paintKey.equals("Tree.verticalLine"))
				color = UIManager
						.getColor("Synthetica.tree.line.color.vertical");
			if (color != null)
				g.setColor(color);
			String lineType = UIManager.getString("Synthetica.tree.line.type");
			if ("SOLID".equals(lineType))
				g.drawLine(x1, y1, x2, y2);
			else if ("DASHED".equals(lineType)) {
				int dashedLine = UIManager
						.getInt("Synthetica.tree.line.dashed.line");
				if (dashedLine <= 0)
					dashedLine = 2;
				int dashedSpace = UIManager
						.getInt("Synthetica.tree.line.dashed.space");
				if (dashedSpace <= 0)
					dashedSpace = 1;
				if (paintKey.equals("Tree.horizontalLine")) {
					for (int x = x1; x < x2; x += dashedLine + dashedSpace)
						g.drawLine(x, y1, Math.min((x + dashedLine) - 1, x2),
								y2);

				} else {
					for (int y = y1; y < y2; y += dashedLine + dashedSpace)
						g.drawLine(x1, y, x1, Math
								.min((y + dashedLine) - 1, y2));

				}
			}
			return;
		} else {
			super.drawLine(context, paintKey, g, x1, y1, x2, y2);
			return;
		}
	}

	@Override
	public String layoutText(SynthContext sc, FontMetrics fm, String text,
			Icon icon, int hAlign, int vAlign, int hTextPosition,
			int vTextPosition, Rectangle viewR, Rectangle iconR,
			Rectangle textR, int iconTextGap) {
		if (sc.getComponent() instanceof JTabbedPane) {
			if (UIManager
					.getBoolean("Synthetica.tabbedPane.tab.text.position.leading"))
				hTextPosition = 10;
			else
				hTextPosition = 11;
			SynthStyleFactory ssf = SynthLookAndFeel.getStyleFactory();
			SynthStyle ss = ssf.getStyle(sc.getComponent(), Region.TABBED_PANE);
			if (ss.get(sc, "TabbedPane.textIconGap") == null)
				iconTextGap = 4;
		}
		return super.layoutText(sc, fm, text, icon, hAlign, vAlign,
				hTextPosition, vTextPosition, viewR, iconR, textR, iconTextGap);
	}

	@Override
	public void paintText(SynthContext sc, Graphics g, String text, int x,
			int y, int mnemonicIndex) {
		javax.swing.JComponent c = sc.getComponent();
		if (sc.getRegion() == Region.BUTTON
				|| sc.getRegion() == Region.TOGGLE_BUTTON) {
			String imagePath = (String) UIManager
					.get("Synthetica.button.textBackground");
			if (imagePath != null) {
				Insets sInsets = (Insets) UIManager
						.get("Synthetica.button.textBackground.insets");
				Insets dInsets = sInsets;
				AbstractButton b = (AbstractButton) c;
				String label = b.getText();
				if (label != null && label.trim().length() > 0) {
					FontMetrics fm = b.getFontMetrics(b.getFont());
					int h = fm.getHeight();
					int w = fm.stringWidth(label);
					ImagePainter imagePainter = new ImagePainter(g, x - 2, y,
							w + 4, h, imagePath, sInsets, dInsets, 0, 0);
					imagePainter.draw();
				}
			}
		}
		if (SyntheticaLookAndFeel.getAntiAliasEnabled()
				|| UIManager.getBoolean("Synthetica.text.antialias")) {
			Graphics2D g2 = (Graphics2D) g;
			Object oldAAValue = g2
					.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			super.paintText(sc, g, text, x, y, mnemonicIndex);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					oldAAValue);
		} else {
			super.paintText(sc, g, text, x, y, mnemonicIndex);
		}
	}
}
