package de.javasoft.plaf.synthetica.styles;

import java.awt.Color;
import java.awt.Font;
import java.util.WeakHashMap;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.UIManager;
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
public class MenuStyle extends StyleWrapper {

	private static WeakHashMap menus = new WeakHashMap();

	private static MenuStyle instance = new MenuStyle();

	public static MenuStyle getStyle(SynthStyle style, JComponent c,
			Region region) {
		instance.setStyle(style);
		((JMenu) c).setOpaque(false);
		return instance;
	}

	private MenuStyle() {
	}

	@Override
	public Color getColor(SynthContext sc, ColorType type) {
		JMenu m = (JMenu) sc.getComponent();
		Boolean b = (Boolean) m.getClientProperty("Synthetica.MOUSE_OVER");
		boolean mouseOver = b != null ? b.booleanValue() : false;
		boolean selected = (sc.getComponentState() & 0x200) > 0;
		boolean enabled = (sc.getComponentState() & 1) > 0;
		if (m.isTopLevelMenu() && type.equals(ColorType.TEXT_FOREGROUND)) {
			String colorKey = "Synthetica.menu.toplevel.textColor";
			if (!enabled)
				colorKey = (new StringBuilder(String.valueOf(colorKey)))
						.append(".disabled").toString();
			else if (selected || mouseOver)
				colorKey = (new StringBuilder(String.valueOf(colorKey)))
						.append(".selected").toString();
			Color color = UIManager.getColor(colorKey);
			if (color != null)
				return color;
		}
		return synthStyle.getColor(sc, type);
	}

	@Override
	public Font getFont(SynthContext context) {
		JMenu menu = (JMenu) context.getComponent();
		String text = menu.getText();
		if ((context.getComponentState() & 0x200) > 0
				&& !menus.containsKey(menu)) {
			int index = text.toLowerCase().indexOf("<html>");
			if (index == -1)
				return synthStyle.getFont(context);
			String color = Integer.toHexString(synthStyle.getColor(context,
					ColorType.TEXT_FOREGROUND).getRGB());
			String htmlColor = (new StringBuilder("color=#")).append(
					color.substring(2)).toString();
			menus.put(menu, htmlColor);
			String html = text.substring(index, index + 6);
			menu.setText(text.replaceFirst(html, (new StringBuilder(
					"<html><font ")).append(htmlColor).append(">").toString()));
		} else if ((context.getComponentState() & 8) > 0) {
			int index = text.toLowerCase().indexOf("<html>");
			if (index == -1 || menus.containsKey(menu))
				return synthStyle.getFont(context);
			String color = Integer.toHexString(synthStyle.getColor(context,
					ColorType.TEXT_FOREGROUND).getRGB());
			String htmlColor = (new StringBuilder("color=#")).append(
					color.substring(2)).toString();
			menus.put(menu, htmlColor);
			String html = text.substring(index, index + 6);
			menu.setText(text.replaceFirst(html, (new StringBuilder(
					"<html><font ")).append(htmlColor).append(">").toString()));
		} else if ((context.getComponentState() & 0x200) == 0
				&& menus.containsKey(menu)) {
			String htmlColor = (String) menus.get(menu);
			menu.setText(text.replaceFirst((new StringBuilder("<html><font "))
					.append(htmlColor).append(">").toString(), "<html>"));
			menus.remove(menu);
		}
		return synthStyle.getFont(context);
	}

}
