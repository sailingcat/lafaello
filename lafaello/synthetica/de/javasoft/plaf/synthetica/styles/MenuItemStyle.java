package de.javasoft.plaf.synthetica.styles;

import java.awt.Font;
import java.util.WeakHashMap;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
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
public class MenuItemStyle extends StyleWrapper {

	private static WeakHashMap items = new WeakHashMap();

	private static MenuItemStyle instance = new MenuItemStyle();

	public static MenuItemStyle getStyle(SynthStyle style, JComponent c,
			Region region) {
		instance.setStyle(style);
		JMenuItem mi = (JMenuItem) c;
		mi.setOpaque(false);
		return instance;
	}

	private MenuItemStyle() {
	}

	@Override
	public Font getFont(SynthContext context) {
		JMenuItem item = (JMenuItem) context.getComponent();
		String text = item.getText();
		if ((context.getComponentState() & 2) > 0 && !items.containsKey(item)) {
			int index = text.toLowerCase().indexOf("<html>");
			if (index == -1)
				return synthStyle.getFont(context);
			String color = Integer.toHexString(synthStyle.getColor(context,
					ColorType.TEXT_FOREGROUND).getRGB());
			String htmlColor = (new StringBuilder("color=#")).append(
					color.substring(2)).toString();
			items.put(item, htmlColor);
			String html = text.substring(index, index + 6);
			item.setText(text.replaceFirst(html, (new StringBuilder(
					"<html><font ")).append(htmlColor).append(">").toString()));
		} else if ((context.getComponentState() & 8) > 0) {
			int index = text.toLowerCase().indexOf("<html>");
			if (index == -1 || items.containsKey(item))
				return synthStyle.getFont(context);
			String color = Integer.toHexString(synthStyle.getColor(context,
					ColorType.TEXT_FOREGROUND).getRGB());
			String htmlColor = (new StringBuilder("color=#")).append(
					color.substring(2)).toString();
			items.put(item, htmlColor);
			String html = text.substring(index, index + 6);
			item.setText(text.replaceFirst(html, (new StringBuilder(
					"<html><font ")).append(htmlColor).append(">").toString()));
		} else if ((context.getComponentState() & 2) == 0
				&& items.containsKey(item)) {
			String htmlColor = (String) items.get(item);
			item.setText(text.replaceFirst((new StringBuilder("<html><font "))
					.append(htmlColor).append(">").toString(), "<html>"));
			items.remove(item);
		}
		return synthStyle.getFont(context);
	}

	@Override
	public Icon getIcon(SynthContext sc, Object key) {
		JMenuItem mi = (JMenuItem) sc.getComponent();
		if (((mi instanceof JRadioButtonMenuItem) || (mi instanceof JCheckBoxMenuItem))
				&& mi.isSelected() && !mi.isEnabled()) {
			SynthContext sc1 = new SynthContext(mi, sc.getRegion(), sc
					.getStyle(), 520);
			return synthStyle.getIcon(sc1, key);
		} else {
			return synthStyle.getIcon(sc, key);
		}
	}

}
