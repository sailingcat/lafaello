package de.javasoft.plaf.synthetica.painter;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.WeakHashMap;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;

import de.javasoft.util.java2d.DropShadow;

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
public class InternalFramePainter extends SynthPainter {

	private static SynthPainter instance = new InternalFramePainter();

	private static WeakHashMap opaqued = new WeakHashMap();

	public static SynthPainter getInstance() {
		return instance;
	}

	private InternalFramePainter() {
	}

	@Override
	public void paintInternalFrameBorder(SynthContext context, Graphics g,
			int x, int y, int w, int h) {
		JInternalFrame frame = (JInternalFrame) context.getComponent();
		String imagePath = "Synthetica.internalFrame.border";
		if (frame.isSelected())
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".selected").toString();
		imagePath = (String) UIManager.get(imagePath);
		Insets sInsets = (Insets) UIManager
				.get("Synthetica.internalFrame.border.insets");
		Insets dInsets = sInsets;
		ImagePainter imagePainter = new ImagePainter(g, x, y, w, h, imagePath,
				sInsets, dInsets, 0, 0);
		imagePainter.drawBorder();
	}

	@Override
	public void paintInternalFrameTitlePaneBackground(SynthContext sc,
			Graphics g, int x, int y, int w, int h) {
		BasicInternalFrameTitlePane pane = (BasicInternalFrameTitlePane) sc
				.getComponent();
		Component parent = pane.getParent();
		boolean selected = (sc.getComponentState() & 0x200) > 0;
		setIcons(pane, sc);
		if (!opaqued.containsKey(pane)) {
			opaqued.put(pane, null);
			Component components[] = pane.getComponents();
			for (int i = 0; i < components.length; i++) {
				Component comp = components[i];
				if (comp instanceof JButton) {
					JButton b = (JButton) comp;
					b.setOpaque(false);
				}
			}

		}
		String title = "";
		if (parent instanceof JInternalFrame) {
			title = ((JInternalFrame) parent).getTitle();
		} else {
			javax.swing.JInternalFrame.JDesktopIcon di = (javax.swing.JInternalFrame.JDesktopIcon) parent;
			title = (new StringBuilder(String.valueOf(di.getInternalFrame()
					.getTitle().substring(0, 3)))).append("...").toString();
		}
		String imagePath = "Synthetica.internalFrameTitlePane.background";
		if (selected)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".selected").toString();
		imagePath = (String) UIManager.get(imagePath);
		Insets sInsets = (Insets) UIManager
				.get("Synthetica.internalFrameTitlePane.background.insets");
		Insets dInsets = sInsets;
		ImagePainter imagePainter = new ImagePainter(g, x, y, w, h, imagePath,
				sInsets, dInsets, 0, 0);
		imagePainter.draw();
		FontMetrics fm = pane.getFontMetrics(pane.getFont());
		int th = fm.getHeight();
		int tw = fm.stringWidth(title);
		int tx = 26;
		int ty = (pane.getSize().height - th) / 2;
		if (UIManager
				.getBoolean("Synthetica.internalFrame.titlePane.dropShadow")
				&& selected && tw > 0 && th > 0) {
			BufferedImage image = new BufferedImage(tw, th, 2);
			Graphics g2 = image.createGraphics();
			g2.setFont(g.getFont());
			g2.drawString(title, 0, fm.getAscent());
			DropShadow ds = new DropShadow(image);
			ds.paintShadow(g, tx, ty);
		}
		g.setColor(sc.getStyle().getColor(sc, ColorType.FOREGROUND));
		sc.getStyle().getGraphicsUtils(sc).paintText(sc, g, title, tx, ty, -1);
	}

	@Override
	public void paintInternalFrameTitlePaneBorder(SynthContext synthcontext,
			Graphics g1, int i, int j, int k, int l) {
	}

	private void setIcons(BasicInternalFrameTitlePane pane, SynthContext sc) {
		String hoverButton = (String) pane
				.getClientProperty("Synthetica.MOUSE_OVER");
		boolean hover = hoverButton != null;
		SynthStyle style = sc.getStyle();
		SynthContext scHover = new SynthContext(pane, sc.getRegion(), style, 2);
		java.awt.Container parent = pane.getParent();
		String key = "InternalFrameTitlePane.iconifyIcon";
		javax.swing.Icon iconifyIcon = style.getIcon(sc, key);
		if (hover && hoverButton.endsWith("iconifyButton"))
			iconifyIcon = style.getIcon(scHover, key);
		key = "InternalFrameTitlePane.maximizeIcon";
		javax.swing.Icon maxIcon = style.getIcon(sc, key);
		if (hover && hoverButton.endsWith("maximizeButton"))
			maxIcon = style.getIcon(scHover, key);
		key = "InternalFrameTitlePane.closeIcon";
		javax.swing.Icon closeIcon = style.getIcon(sc, key);
		if (hover && hoverButton.endsWith("closeButton"))
			closeIcon = style.getIcon(scHover, key);
		key = "InternalFrameTitlePane.minimizeIcon";
		javax.swing.Icon minIcon = style.getIcon(sc, key);
		if ((parent instanceof JInternalFrame) && hover
				&& hoverButton.endsWith("maximizeButton"))
			minIcon = style.getIcon(scHover, key);
		else if ((parent instanceof javax.swing.JInternalFrame.JDesktopIcon)
				&& hover && hoverButton.endsWith("iconifyButton"))
			minIcon = style.getIcon(scHover, key);
		JButton iconButton = null;
		JButton maxButton = null;
		JButton closeButton = null;
		Component components[] = pane.getComponents();
		for (int i = 0; i < components.length; i++) {
			Component c = components[i];
			if (c instanceof JButton) {
				String name = c.getName();
				if (name.endsWith("closeButton"))
					closeButton = (JButton) c;
				if (name.endsWith("iconifyButton"))
					iconButton = (JButton) c;
				if (name.endsWith("maximizeButton"))
					maxButton = (JButton) c;
			}
		}

		if (parent instanceof JInternalFrame) {
			JInternalFrame frame = (JInternalFrame) parent;
			if (frame.isMaximum()) {
				if (iconButton != null)
					iconButton.setIcon(iconifyIcon);
				if (maxButton != null)
					maxButton.setIcon(minIcon);
			} else if (frame.isIcon()) {
				if (iconButton != null)
					iconButton.setIcon(minIcon);
				if (maxButton != null)
					maxButton.setIcon(maxIcon);
			} else {
				if (iconButton != null)
					iconButton.setIcon(iconifyIcon);
				if (maxButton != null)
					maxButton.setIcon(maxIcon);
			}
		} else if (parent instanceof javax.swing.JInternalFrame.JDesktopIcon) {
			if (iconButton != null)
				iconButton.setIcon(minIcon);
			if (maxButton != null)
				maxButton.setIcon(maxIcon);
		}
		if (closeButton != null)
			closeButton.setIcon(closeIcon);
	}

}
