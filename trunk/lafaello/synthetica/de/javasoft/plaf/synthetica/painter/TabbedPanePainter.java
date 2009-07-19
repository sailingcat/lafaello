package de.javasoft.plaf.synthetica.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.WeakHashMap;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthPainter;

import de.javasoft.plaf.synthetica.StyleFactory;
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
public class TabbedPanePainter extends SynthPainter {

	private static SynthPainter instance = new TabbedPanePainter();

	private static WeakHashMap translucentTabbedPanes = new WeakHashMap();

	public static SynthPainter getInstance() {
		return instance;
	}

	private TabbedPanePainter() {
	}

	private void drawContentBorderLine4SelectedTab(JTabbedPane tabbedPane,
			Graphics g, int x, int y, int w, int h) {
		int p = tabbedPane.getTabPlacement();
		String imagePath;
		if (p == 1 || p == 3)
			imagePath = (String) UIManager
					.get("Synthetica.tabbedPane.tab.x.image.selected");
		else
			imagePath = (String) UIManager
					.get("Synthetica.tabbedPane.tab.y.image.selected");
		if (imagePath == null)
			return;
		Image image = (new ImageIcon(SyntheticaLookAndFeel.class
				.getResource(imagePath))).getImage();
		int rdb = 0;
		if (UIManager
				.getBoolean("Synthetica.tabbedPane.tab.removeDoubleBorder")) {
			boolean rightBorderTab = false;
			int placement = tabbedPane.getTabPlacement();
			int borderTab = isBorderTab(tabbedPane, tabbedPane
					.getSelectedIndex());
			if ((placement == 1 || placement == 3) && borderTab == 1)
				rightBorderTab = true;
			if (!rightBorderTab)
				rdb = 1;
		}
		int selectedTab = tabbedPane.getSelectedIndex();
		if (selectedTab == -1)
			return;
		Rectangle r = tabbedPane.getBoundsAt(selectedTab);
		Insets sInsets = (Insets) ((Insets) UIManager
				.get("Synthetica.tabbedPane.tab.insets.selected")).clone();
		Insets dInsets = new Insets(0, 0, 0, 0);
		if (p == 1) {
			sInsets.top = image.getHeight(null) - sInsets.bottom - 1;
			dInsets.left = sInsets.left;
			dInsets.right = sInsets.right - rdb;
			x = r.x;
			w = r.width + rdb;
			h = 1;
		} else if (p == 3) {
			sInsets.bottom = image.getHeight(null) - sInsets.top - 1;
			dInsets.left = sInsets.left;
			dInsets.right = sInsets.right - rdb;
			x = r.x;
			y = (y + h) - 1;
			w = r.width + rdb;
			h = 1;
		} else if (p == 2) {
			sInsets.left = image.getWidth(null) - sInsets.right - 1;
			dInsets.top = sInsets.top;
			dInsets.bottom = sInsets.bottom - rdb;
			x = r.x + r.width;
			y = r.y;
			w = 1;
			h = r.height + rdb;
		} else if (p == 4) {
			sInsets.right = image.getWidth(null) - sInsets.left - 1;
			dInsets.top = sInsets.top;
			dInsets.bottom = sInsets.bottom - rdb;
			x = r.x - 1;
			y = r.y;
			w = 1;
			h = r.height + rdb;
		}
		ImagePainter imagePainter = new ImagePainter(g, x, y, w, h, imagePath,
				sInsets, dInsets, 0, 0);
		if (p == 1 || p == 3) {
			imagePainter.drawLeft();
			imagePainter.drawRight();
		} else {
			imagePainter.drawTopCenter();
			imagePainter.drawBottomCenter();
		}
		imagePainter.drawCenter();
	}

	private int isBorderTab(JTabbedPane tabbedPane, int tabIndex) {
		if (tabIndex < 0)
			return -1;
		boolean left = false;
		boolean right = false;
		boolean top = false;
		boolean bottom = false;
		if (tabbedPane.getTabPlacement() == 1
				|| tabbedPane.getTabPlacement() == 3) {
			TreeMap x1Tabs = new TreeMap(new Comparator() {

				public int compare(Object o1, Object o2) {
					Rectangle r1 = (Rectangle) o1;
					Rectangle r2 = (Rectangle) o2;
					if (r1.x > r2.x)
						return 1;
					return r1.x >= r2.x ? 0 : -1;
				}
			});
			TreeMap x2Tabs = new TreeMap(new Comparator() {

				public int compare(Object o1, Object o2) {
					Rectangle r1 = (Rectangle) o1;
					Rectangle r2 = (Rectangle) o2;
					if (r1.x + r1.width < r2.x + r2.width)
						return 1;
					return r1.x + r1.width <= r2.x + r2.width ? 0 : -1;
				}
			});
			for (int i = 0; i < tabbedPane.getTabCount(); i++)
				x1Tabs.put(tabbedPane.getBoundsAt(i), new Integer(i));

			if (x1Tabs.headMap(tabbedPane.getBoundsAt(tabIndex)).size() == 0)
				left = true;
			for (int i = 0; i < tabbedPane.getTabCount(); i++)
				x2Tabs.put(tabbedPane.getBoundsAt(i), new Integer(i));

			if (x2Tabs.headMap(tabbedPane.getBoundsAt(tabIndex)).size() == 0)
				right = true;
			if (left && right)
				return 2;
			if (left)
				return 0;
			if (right)
				return 1;
		} else {
			TreeMap y1Tabs = new TreeMap(new Comparator() {

				public int compare(Object o1, Object o2) {
					Rectangle r1 = (Rectangle) o1;
					Rectangle r2 = (Rectangle) o2;
					if (r1.y > r2.y)
						return 1;
					return r1.y >= r2.y ? 0 : -1;
				}

			});
			TreeMap y2Tabs = new TreeMap(new Comparator() {

				public int compare(Object o1, Object o2) {
					Rectangle r1 = (Rectangle) o1;
					Rectangle r2 = (Rectangle) o2;
					if (r1.y + r1.height < r2.y + r2.height)
						return 1;
					return r1.y + r1.height <= r2.y + r2.height ? 0 : -1;
				}
			});
			for (int i = 0; i < tabbedPane.getTabCount(); i++)
				y1Tabs.put(tabbedPane.getBoundsAt(i), new Integer(i));

			if (y1Tabs.headMap(tabbedPane.getBoundsAt(tabIndex)).size() == 0)
				top = true;
			for (int i = 0; i < tabbedPane.getTabCount(); i++)
				y2Tabs.put(tabbedPane.getBoundsAt(i), new Integer(i));

			if (y2Tabs.headMap(tabbedPane.getBoundsAt(tabIndex)).size() == 0)
				bottom = true;
			if (top && bottom)
				return 5;
			if (top)
				return 3;
			if (bottom)
				return 4;
		}
		return -1;
	}

	@Override
	public void paintTabbedPaneContentBorder(SynthContext sc, Graphics g,
			int x, int y, int w, int h) {
		g.setColor((Color) UIManager
				.get("Synthetica.tabbedPane.hideBorderColor"));
		JTabbedPane tabbedPane = (JTabbedPane) sc.getComponent();
		if (sc.getStyle().isOpaque(sc)
				&& !translucentTabbedPanes.containsKey(tabbedPane)) {
			translucentTabbedPanes.put(tabbedPane, null);
			Component components[] = tabbedPane.getComponents();
			Component acomponent[] = components;
			int j = 0;
			for (int k = acomponent.length; j < k; j++) {
				Component c = acomponent[j];
				if ((c instanceof Container) && !(c instanceof Window)
						&& !(c instanceof JRootPane))
					setComponentsTranslucent(sc, (Container) c);
			}

		}
		String imagePath = (String) UIManager
				.get("Synthetica.tabbedPane.contentBorder.image");
		Insets sInsets = (Insets) UIManager
				.get("Synthetica.tabbedPane.contentBorder.image.sourceInsets");
		if (sInsets == null)
			sInsets = new Insets(0, 0, 0, 0);
		Insets dInsets = (Insets) sInsets.clone();
		int tbsw = 0;
		int tbsh = 0;
		boolean widthComplete = false;
		boolean isLeftToRight = tabbedPane.getComponentOrientation()
				.isLeftToRight();
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			Rectangle r = tabbedPane.getBoundsAt(i);
			if (!widthComplete)
				tbsw += r.width;
			tbsh += r.height;
			if (isLeftToRight
					&& (isBorderTab(tabbedPane, i) == 1 || isBorderTab(
							tabbedPane, i) == 2))
				widthComplete = true;
			else if (!isLeftToRight
					&& (isBorderTab(tabbedPane, i) == 0 || isBorderTab(
							tabbedPane, i) == 2))
				widthComplete = true;
		}

		ImagePainter imagePainter = new ImagePainter(g, x, y, w, h, imagePath,
				sInsets, dInsets, 0, 0);
		imagePainter.drawTopLeft();
		imagePainter.drawTopCenter();
		imagePainter.drawTopRight();
		imagePainter.drawLeft();
		imagePainter.drawBottomLeft();
		imagePainter.drawBottomCenter();
		imagePainter.drawBottomRight();
		imagePainter.drawRight();
		drawContentBorderLine4SelectedTab(tabbedPane, g, x, y, w, h);
	}

	@Override
	public void paintTabbedPaneTabAreaBorder(SynthContext synthcontext,
			Graphics g1, int i, int j, int k, int l) {
	}

	@Override
	public void paintTabbedPaneTabBackground(SynthContext sc, Graphics g,
			int x, int y, int w, int h, int tabIndex) {
		JTabbedPane tabbedPane = (JTabbedPane) sc.getComponent();
		Integer iHover = (Integer) tabbedPane
				.getClientProperty("Synthetica.MOUSE_OVER");
		int hover = iHover != null ? iHover.intValue() : -1;
		int borderTab = isBorderTab(tabbedPane, tabIndex);
		int placement = ((JTabbedPane) sc.getComponent()).getTabPlacement();
		int state = sc.getComponentState();
		String imagePath;
		Insets sInsets;
		if ((state & 0x200) > 0) {
			sInsets = (Insets) UIManager
					.get("Synthetica.tabbedPane.tab.insets.selected");
			if (placement == 1 || placement == 3)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.x.image.selected");
			else
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.y.image.selected");
		} else if ((state & 8) > 0) {
			sInsets = (Insets) UIManager
					.get("Synthetica.tabbedPane.tab.insets.disabled");
			if (borderTab == 0)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.left.image.disabled");
			else if (borderTab == 1)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.right.image.disabled");
			else if (borderTab == 2)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.leftRight.image.disabled");
			else if (borderTab == 3)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.top.image.disabled");
			else if (borderTab == 4)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.bottom.image.disabled");
			else if (borderTab == 5)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.topBottom.image.disabled");
			else if (placement == 1 || placement == 3)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.middle.image.disabled");
			else
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.center.image.disabled");
		} else {
			sInsets = (Insets) UIManager
					.get("Synthetica.tabbedPane.tab.insets");
			if (borderTab == 0)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.left.image");
			else if (borderTab == 1)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.right.image");
			else if (borderTab == 2)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.leftRight.image");
			else if (borderTab == 3)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.top.image");
			else if (borderTab == 4)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.bottom.image");
			else if (borderTab == 5)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.topBottom.image");
			else if (placement == 1 || placement == 3)
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.middle.image");
			else
				imagePath = (String) UIManager
						.get("Synthetica.tabbedPane.tab.center.image");
		}
		if (placement == 3)
			sInsets = new Insets(sInsets.bottom, sInsets.left, sInsets.top,
					sInsets.right);
		if (placement == 4)
			sInsets = new Insets(sInsets.top, sInsets.right, sInsets.bottom,
					sInsets.left);
		if (sInsets == null)
			sInsets = new Insets(0, 0, 0, 0);
		Insets dInsets = (Insets) sInsets.clone();
		if (UIManager
				.getBoolean("Synthetica.tabbedPane.tab.removeDoubleBorder"))
			if ((placement == 1 || placement == 3)
					&& (borderTab == 0 || borderTab == -1))
				dInsets.right = 0;
			else if ((placement == 2 || placement == 4)
					&& (borderTab == 3 || borderTab == -1))
				dInsets.bottom = 0;
		if (placement == 1)
			dInsets.bottom = 0;
		else if (placement == 2)
			dInsets.right = 0;
		else if (placement == 3)
			dInsets.top = 0;
		else if (placement == 4)
			dInsets.left = 0;
		ImagePainter imagePainter;
		if (tabIndex == hover && tabbedPane.isEnabledAt(tabIndex)
				&& tabbedPane.getSelectedIndex() != hover) {
			int red = UIManager.getInt("Synthetica.tabbedPane.tab.hover.red");
			int green = UIManager
					.getInt("Synthetica.tabbedPane.tab.hover.green");
			int blue = UIManager.getInt("Synthetica.tabbedPane.tab.hover.blue");
			imagePainter = new ImagePainter(tabbedPane, red, green, blue, g, x,
					y, w, h, imagePath, sInsets, dInsets, 0, 0);
		} else {
			imagePainter = new ImagePainter(g, x, y, w, h, imagePath, sInsets,
					dInsets, 0, 0);
		}
		if (placement != 3) {
			imagePainter.drawTopLeft();
			imagePainter.drawTopCenter();
			imagePainter.drawTopRight();
		}
		if (placement != 4)
			imagePainter.drawLeft();
		if (placement != 1) {
			imagePainter.drawBottomLeft();
			imagePainter.drawBottomCenter();
			imagePainter.drawBottomRight();
		}
		if (placement != 2)
			imagePainter.drawRight();
		imagePainter.drawCenter();
	}

	public void reinitialize() {
		translucentTabbedPanes = new WeakHashMap();
	}

	private void setComponentsTranslucent(SynthContext sc, Container container) {
		StyleFactory factory = (StyleFactory) SynthLookAndFeel
				.getStyleFactory();
		if ((container instanceof JPanel) || (container instanceof JScrollPane)
				|| (container instanceof JViewport)) {
			factory.getComponentPropertyStore().storeComponentProperty(
					container, "OPAQUE");
			((JComponent) container).setOpaque(false);
		}
		Component components[] = container.getComponents();
		Component acomponent[] = components;
		int i = 0;
		for (int j = acomponent.length; i < j; i++) {
			Component c = acomponent[i];
			if ((c instanceof Container) && !(c instanceof Window)
					&& !(c instanceof JRootPane))
				setComponentsTranslucent(sc, (Container) c);
			if (((c instanceof JRadioButton) || (c instanceof JCheckBox) || (c instanceof JTextArea)
					&& !(c.getParent() instanceof JViewport))
					&& (c.getBackground() == null || (c.getBackground() instanceof ColorUIResource))) {
				factory.getComponentPropertyStore().storeComponentProperty(c,
						"OPAQUE");
				((JComponent) c).setOpaque(false);
			}
		}

	}
}
