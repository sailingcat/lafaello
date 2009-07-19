package de.javasoft.plaf.synthetica.styles;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

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
public class ToolBarSeparatorStyle extends StyleWrapper {

	private static ToolBarSeparatorStyle instance = new ToolBarSeparatorStyle();

	public static ToolBarSeparatorStyle getStyle(SynthStyle style,
			JComponent c, Region region) {
		instance.setStyle(style);
		return instance;
	}

	private ToolBarSeparatorStyle() {
	}

	@Override
	public Object get(SynthContext sc, Object key) {
		if (key.equals("ToolBar.separatorSize")) {
			Dimension d = SyntheticaLookAndFeel.getToolbarSeparatorDimension();
			if (d != null)
				return new DimensionUIResource(d.width, d.height);
		}
		return synthStyle.get(sc, key);
	}

}
