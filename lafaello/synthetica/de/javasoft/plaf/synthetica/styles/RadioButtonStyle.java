package de.javasoft.plaf.synthetica.styles;

import javax.swing.Icon;
import javax.swing.JRadioButton;
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
public class RadioButtonStyle extends StyleWrapper {

	public RadioButtonStyle(SynthStyle style) {
		synthStyle = style;
	}

	@Override
	public Icon getIcon(SynthContext sc, Object key) {
		if (((JRadioButton) sc.getComponent()).getIcon() != null)
			return null;
		int state = sc.getComponentState();
		Boolean b = (Boolean) sc.getComponent().getClientProperty(
				"Synthetica.MOUSE_OVER");
		boolean mouseOver = b != null ? b.booleanValue() : false;
		if ((state & 0x200) > 0 && mouseOver)
			return synthStyle.getIcon(new SynthContext(sc.getComponent(), sc
					.getRegion(), sc.getStyle(), state | 2), key);
		else
			return synthStyle.getIcon(sc, key);
	}
}
