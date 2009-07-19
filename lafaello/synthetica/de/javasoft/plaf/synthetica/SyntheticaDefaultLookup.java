package de.javasoft.plaf.synthetica;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.synth.SynthContext;

import sun.swing.DefaultLookup;
import sun.swing.plaf.synth.SynthUI;

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
public class SyntheticaDefaultLookup extends DefaultLookup {

	public SyntheticaDefaultLookup() {
	}

	@Override
	public Object getDefault(JComponent c, ComponentUI ui, String key) {
		if ("ToggleButton.focusInputMap".equals(key)
				|| "RadioButton.focusInputMap".equals(key)
				|| "CheckBox.focusInputMap".equals(key))
			return LookAndFeel.makeInputMap(new Object[] { "SPACE", "pressed",
					"released SPACE", "released" });
		if ("Button.defaultButtonFollowsFocus".equals(key))
			return Boolean.valueOf(false);
		if ("SplitPane.oneTouchButtonOffset".equals(key)
				&& UIManager
						.getBoolean("Syntetica.splitPane.centerOneTouchButtons")
				&& (ui instanceof SynthUI)) {
			JSplitPane sp = (JSplitPane) c;
			int size = 0;
			if (sp.getOrientation() == 0)
				size = sp.getWidth();
			else
				size = sp.getHeight();
			SynthContext sc = ((SynthUI) ui).getContext(c);
			int buttonSize = ((Integer) sc.getStyle().get(sc,
					"SplitPane.oneTouchButtonSize")).intValue();
			int spSize = ((Integer) sc.getStyle().get(sc, "SplitPane.size"))
					.intValue();
			return Integer.valueOf(size / 2 - buttonSize - spSize);
		}
		if (ui instanceof SynthUI) {
			SynthContext context = ((SynthUI) ui).getContext(c);
			Object value = context.getStyle().get(context, key);
			return value;
		} else {
			return super.getDefault(c, ui, key);
		}
	}
}
