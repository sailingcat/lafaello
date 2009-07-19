package de.javasoft.plaf.synthetica.styles;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.plaf.synth.Region;
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
public class PopupMenuStyle extends StyleWrapper {

	private static PopupMenuStyle instance = new PopupMenuStyle();

	public static PopupMenuStyle getStyle(SynthStyle style, JComponent c,
			Region region) {
		instance.setStyle(style);
		final JPopupMenu popup = (JPopupMenu) c;
		popup.setOpaque(false);
		popup.addPropertyChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				String name = evt.getPropertyName();
				if ("visible".equals(name)) {
					JPanel panel = (JPanel) popup.getParent();
					if (panel != null && panel.isOpaque())
						panel.setOpaque(false);
				}
			}
		});
		return instance;
	}

	private PopupMenuStyle() {
	}

}
