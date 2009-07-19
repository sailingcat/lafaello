package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.WeakHashMap;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.basic.BasicMenuUI;
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
public class MenuPainter extends SynthPainter {

	private static SynthPainter instance = new MenuPainter();

	private static WeakHashMap allMenus = new WeakHashMap();

	public static SynthPainter getInstance() {
		return instance;
	}

	private MenuPainter() {
	}

	@Override
	public void paintCheckBoxMenuItemBackground(SynthContext sc, Graphics g,
			int x, int y, int w, int h) {
		paintMenuItemBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintMenuBackground(SynthContext sc, Graphics g, int x, int y,
			int w, int h) {
		String imagePath = null;
		int state = sc.getComponentState();
		try {
			javax.swing.Icon arrow = sc.getStyle()
					.getIcon(sc, "Menu.arrowIcon");
			BasicMenuUI ui = (BasicMenuUI) ((JMenu) sc.getComponent()).getUI();
			Class clazz = Class
					.forName("javax.swing.plaf.basic.BasicMenuItemUI");
			Field field = clazz.getDeclaredField("arrowIcon");
			field.setAccessible(true);
			field.set(ui, arrow);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JMenu m = (JMenu) sc.getComponent();
		imagePath = "Synthetica.menu";
		Insets sInsets = null;
		if (m.isTopLevelMenu()) {
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".toplevel.background").toString();
			Boolean b = (Boolean) m.getClientProperty("Synthetica.MOUSE_OVER");
			boolean mouseOver = b != null ? b.booleanValue() : false;
			if ((state & 0x200) > 0 || mouseOver)
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".selected").toString();
			imagePath = (String) UIManager.get(imagePath);
			sInsets = (Insets) UIManager
					.get("Synthetica.menu.toplevel.background.insets");
		} else {
			if ((state & 8) > 0)
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".disabled").toString();
			else if ((state & 0x200) > 0)
				imagePath = (new StringBuilder(String.valueOf(imagePath)))
						.append(".hover").toString();
			imagePath = (String) UIManager.get(imagePath);
			sInsets = (Insets) UIManager.get("Synthetica.menu.insets");
		}
		if (imagePath != null) {
			Insets dInsets = sInsets;
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.draw();
		}
		if (!allMenus.containsKey(m)) {
			allMenus.put(m, null);
			boolean menuHasIcons = false;
			for (int i = 0; i < m.getMenuComponentCount(); i++) {
				java.awt.Component mc = m.getMenuComponent(i);
				if (!(mc instanceof JMenuItem)
						|| ((JMenuItem) mc).getIcon() == null
						&& !(mc instanceof JRadioButtonMenuItem)
						&& !(mc instanceof JCheckBoxMenuItem))
					continue;
				menuHasIcons = true;
				break;
			}

			if (menuHasIcons
					&& System.getProperty("java.version").startsWith("1.5.")) {
				int iconGap = ((Integer) UIManager
						.get("Synthetica.menu.iconGap")).intValue();
				for (int i = 0; i < m.getMenuComponentCount(); i++) {
					java.awt.Component mc = m.getMenuComponent(i);
					if (!(mc instanceof JRadioButtonMenuItem)
							&& !(mc instanceof JCheckBoxMenuItem)
							&& (mc instanceof JMenuItem)) {
						JMenuItem item = (JMenuItem) mc;
						Insets insets = item.getInsets();
						int leftGap = insets.left - item.getIconTextGap();
						if (item.getIcon() == null)
							leftGap += iconGap;
						item.setBorder(new BorderUIResource(BorderFactory
								.createEmptyBorder(insets.top, leftGap,
										insets.bottom, insets.right)));
					}
				}

			}
		}
	}

	@Override
	public void paintMenuBarBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		JMenuBar mb = (JMenuBar) sc.getComponent();
		String imagePath = "Synthetica.menuBar.background";
		java.awt.Container parent = mb.getRootPane().getParent();
		boolean active = true;
		if (parent instanceof Window)
			active = ((Window) parent).isActive();
		else if (parent instanceof JInternalFrame)
			active = ((JInternalFrame) parent).isSelected();
		if (active)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".active").toString();
		else
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".inactive").toString();
		imagePath = (String) UIManager.get(imagePath);
		if (imagePath != null) {
			Insets sInsets = new Insets(0, 0, 0, 0);
			Insets dInsets = sInsets;
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.draw();
		}
	}

	@Override
	public void paintMenuItemBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		String imagePath = "Synthetica.menuItem";
		int state = sc.getComponentState();
		if ((state & 8) > 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".disabled").toString();
		else if ((state & 2) > 0)
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".hover").toString();
		imagePath = (String) UIManager.get(imagePath);
		if (imagePath != null) {
			Insets sInsets = (Insets) UIManager
					.get("Synthetica.menuItem.insets");
			Insets dInsets = sInsets;
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 0);
			imagePainter.draw();
		}
	}

	@Override
	public void paintPopupMenuBackground(SynthContext context, Graphics g,
			int x, int y, int w, int h) {
		JPopupMenu popup = (JPopupMenu) context.getComponent();
		JPanel panel = (JPanel) popup.getParent();
		BufferedImage background = (BufferedImage) panel
				.getClientProperty("POPUP_BACKGROUND");
		if (background != null)
			g.drawImage(background, x, y, null);
		if (popup.getName() != null && popup.getName().startsWith("ComboPopup")) {
			return;
		} else {
			String imagePath = (String) UIManager
					.get("Synthetica.popupMenu.background");
			Insets sInsets = (Insets) UIManager
					.get("Synthetica.popupMenu.background.insets");
			Insets dInsets = sInsets;
			ImagePainter imagePainter = new ImagePainter(g, x, y, w, h,
					imagePath, sInsets, dInsets, 0, 1);
			imagePainter.draw();
			return;
		}
	}

	@Override
	public void paintRadioButtonMenuItemBackground(SynthContext sc, Graphics g,
			int x, int y, int w, int h) {
		paintMenuItemBackground(sc, g, x, y, w, h);
	}

	public void reinitialize() {
		allMenus = new WeakHashMap();
	}

}
