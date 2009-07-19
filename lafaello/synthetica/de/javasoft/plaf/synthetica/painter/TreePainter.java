package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import java.util.WeakHashMap;

import javax.swing.JTree;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.tree.DefaultTreeCellRenderer;

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
public class TreePainter extends SynthPainter {

	private static SynthPainter instance = new TreePainter();

	private static WeakHashMap trees = new WeakHashMap();

	public static SynthPainter getInstance() {
		return instance;
	}

	private TreePainter() {
	}

	@Override
	public void paintTreeBackground(SynthContext context, Graphics g, int x,
			int y, int w, int h) {
		JTree tree = (JTree) context.getComponent();
		if (!trees.containsKey(tree)) {
			trees.put(tree, null);
			javax.swing.tree.TreeCellRenderer cellRenderer = tree
					.getCellRenderer();
			if ((cellRenderer instanceof DefaultTreeCellRenderer)
					&& cellRenderer.getClass().getName().startsWith(
							"javax.swing.plaf.synth.Synth")) {
				DefaultTreeCellRenderer defaultRenderer = (DefaultTreeCellRenderer) cellRenderer;
				DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
				if (defaultRenderer.getLeafIcon() == null)
					renderer.setLeafIcon(null);
				if (defaultRenderer.getOpenIcon() == null)
					renderer.setOpenIcon(null);
				if (defaultRenderer.getClosedIcon() == null)
					renderer.setClosedIcon(null);
				tree.setCellRenderer(renderer);
			}
		}
	}

	public void reinitialize() {
		trees = new WeakHashMap();
	}

}
