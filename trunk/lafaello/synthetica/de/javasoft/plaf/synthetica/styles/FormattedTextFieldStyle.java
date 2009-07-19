package de.javasoft.plaf.synthetica.styles;

import java.awt.Insets;

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
public class FormattedTextFieldStyle extends StyleWrapper {

	public FormattedTextFieldStyle(SynthStyle style) {
		synthStyle = style;
	}

	@Override
	public Insets getInsets(SynthContext context, Insets insets) {
		Insets ins = synthStyle.getInsets(context, insets);
		String cName = context.getComponent().getName();
		if ("Spinner.formattedTextField".equals(cName))
			return new Insets(0, 0, 0, ins.right);
		else
			return ins;
	}
}
