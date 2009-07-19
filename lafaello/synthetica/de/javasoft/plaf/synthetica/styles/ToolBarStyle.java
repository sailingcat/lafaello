package de.javasoft.plaf.synthetica.styles;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.UIManager;
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
public class ToolBarStyle extends StyleWrapper {

	private LayoutManager orgLayoutManager;

	private Insets orgMargin;

	private HashMap orgFillers;

	private HashMap orgXAlignments;

	private HashMap orgYAlignments;

	private PropertyChangeListener orientationListener;

	private ContainerListener containerListener;

	public ToolBarStyle(SynthStyle style) {
		synthStyle = style;
		orgFillers = new HashMap();
		orgXAlignments = new HashMap();
		orgYAlignments = new HashMap();
	}

	private boolean fillerIsGlue(javax.swing.Box.Filler filler) {
		Dimension zeroDim = new Dimension(0, 0);
		return filler.getMinimumSize().equals(zeroDim)
				&& filler.getPreferredSize().equals(zeroDim);
	}

	@Override
	public void installDefaults(SynthContext context) {
		synthStyle.installDefaults(context);
		final JToolBar tb = (JToolBar) context.getComponent();
		orgLayoutManager = tb.getLayout();
		orgMargin = tb.getMargin();
		orientationListener = new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				String name = evt.getPropertyName();
				if (name.equals("orientation"))
					setLayoutManager(tb);
			}
		};
		tb.addPropertyChangeListener(orientationListener);
		Component components[] = tb.getComponents();
		for (int i = 0; i < components.length; i++) {
			Component c = components[i];
			orgXAlignments.put(c, Float.valueOf(c.getAlignmentX()));
			orgYAlignments.put(c, Float.valueOf(c.getAlignmentY()));
			if (c instanceof javax.swing.Box.Filler)
				orgFillers.put(Integer.valueOf(i), c);
			((JComponent) c).setAlignmentX(0.5F);
			((JComponent) c).setAlignmentY(0.5F);
		}

		containerListener = new ContainerListener() {

			public void componentAdded(ContainerEvent e) {
				Component c = e.getChild();
				orgXAlignments.put(c, Float.valueOf(c.getAlignmentX()));
				orgYAlignments.put(c, Float.valueOf(c.getAlignmentY()));
				if (c instanceof javax.swing.Box.Filler)
					orgFillers.put(Integer
							.valueOf(tb.getComponents().length - 1), c);
				((JComponent) c).setAlignmentX(0.5F);
				((JComponent) c).setAlignmentY(0.5F);
			}

			public void componentRemoved(ContainerEvent containerevent) {
			}
		};
		tb.addContainerListener(containerListener);
		setLayoutManager(tb);
	}

	private void setLayoutManager(JToolBar tb) {
		tb.removeContainerListener(containerListener);
		Component components[] = tb.getComponents();
		LayoutManager lm = null;
		if (tb.getOrientation() == 0) {
			lm = new BoxLayout(tb, 2);
			for (int i = 0; i < components.length; i++)
				if ((components[i] instanceof javax.swing.Box.Filler)
						&& fillerIsGlue((javax.swing.Box.Filler) components[i])) {
					tb.remove(components[i]);
					tb.add(Box.createHorizontalGlue(), i);
				} else if ((components[i] instanceof javax.swing.Box.Filler)
						&& !fillerIsGlue((javax.swing.Box.Filler) components[i])) {
					tb.remove(components[i]);
					Component c = (Component) orgFillers
							.get(Integer.valueOf(i));
					Dimension size = c.getPreferredSize();
					int strut = Math.max(size.width, size.height);
					tb.add(Box.createHorizontalStrut(strut), i);
				}

			setMargin(tb);
			for (int i = 0; i < components.length; i++)
				((JComponent) components[i]).setAlignmentY(0.5F);

		} else {
			lm = new BoxLayout(tb, 3);
			for (int i = 0; i < components.length; i++)
				if ((components[i] instanceof javax.swing.Box.Filler)
						&& fillerIsGlue((javax.swing.Box.Filler) components[i])) {
					tb.remove(components[i]);
					tb.add(Box.createVerticalGlue(), i);
				} else if ((components[i] instanceof javax.swing.Box.Filler)
						&& !fillerIsGlue((javax.swing.Box.Filler) components[i])) {
					tb.remove(components[i]);
					Component c = (Component) orgFillers
							.get(Integer.valueOf(i));
					Dimension size = c.getPreferredSize();
					int strut = Math.max(size.width, size.height);
					tb.add(Box.createVerticalStrut(strut), i);
				}

			setMargin(tb);
			for (int i = 0; i < components.length; i++)
				((JComponent) components[i]).setAlignmentX(0.5F);

		}
		tb.setLayout(lm);
		tb.addContainerListener(containerListener);
	}

	private void setMargin(JToolBar tb) {
		Insets newMargin = null;
		if (tb.getOrientation() == 0)
			newMargin = UIManager
					.getInsets("Synthetica.toolBar.margin.horizontal");
		else
			newMargin = UIManager
					.getInsets("Synthetica.toolBar.margin.vertical");
		if (orgMargin != null && newMargin != null) {
			newMargin.bottom += orgMargin.bottom;
			newMargin.left += orgMargin.left;
			newMargin.top += orgMargin.top;
			newMargin.right += orgMargin.right;
			tb.setMargin(newMargin);
		}
	}

	@Override
	public void uninstallDefaults(SynthContext context) {
		synthStyle.uninstallDefaults(context);
		JToolBar tb = (JToolBar) context.getComponent();
		tb.removePropertyChangeListener(orientationListener);
		tb.removeContainerListener(containerListener);
		int index;
		Component filler;
		for (Iterator it = orgFillers.entrySet().iterator(); it.hasNext(); tb
				.add(filler, index)) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			index = ((Integer) entry.getKey()).intValue();
			filler = (Component) entry.getValue();
			tb.remove(index);
		}

		JComponent c;
		float x;
		for (Iterator it = orgXAlignments.entrySet().iterator(); it.hasNext(); c
				.setAlignmentX(x)) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			c = (JComponent) entry.getKey();
			x = ((Float) entry.getValue()).floatValue();
		}

		float y;
		for (Iterator it = orgYAlignments.entrySet().iterator(); it.hasNext(); c
				.setAlignmentY(y)) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			c = (JComponent) entry.getKey();
			y = ((Float) entry.getValue()).floatValue();
		}

		tb.setMargin(orgMargin);
		tb.setLayout(orgLayoutManager);
	}

}
