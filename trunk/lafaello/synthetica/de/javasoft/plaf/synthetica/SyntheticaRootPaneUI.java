package de.javasoft.plaf.synthetica;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;

import de.javasoft.plaf.synthetica.painter.ImagePainter;

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
public class SyntheticaRootPaneUI extends BasicRootPaneUI {
	private class MouseInputHandler implements MouseInputListener {

		private int windowAction;

		private int dragXOffset;

		private int dragYOffset;

		private Dimension dragDimension;

		private int resizeType;

		private Frame frame;

		private Dialog dialog;

		private final PrivilegedExceptionAction getLocationAction = new PrivilegedExceptionAction() {

			public Object run() throws HeadlessException {
				return MouseInfo.getPointerInfo().getLocation();
			}
		};

		MouseInputHandler() {
			super();
			frame = null;
			dialog = null;
			if (window instanceof Frame)
				frame = (Frame) window;
			else if (window instanceof Dialog)
				dialog = (Dialog) window;
		}

		private boolean isDialogResizable() {
			return dialog != null && dialog.isResizable();
		}

		private boolean isFrameResizable() {
			return frame != null && frame.isResizable()
					&& (frame.getExtendedState() & 6) == 0;
		}

		private boolean isWindowResizable() {
			return isFrameResizable() || isDialogResizable();
		}

		public void mouseClicked(MouseEvent evt) {
			if (frame == null)
				return;
			Point convertedPoint = SwingUtilities.convertPoint(window, evt
					.getPoint(), titlePane);
			if (titlePane != null && titlePane.contains(convertedPoint)
					&& evt.getClickCount() == 2
					&& (evt.getModifiers() & 0x10) == 16) {
				int state = frame.getExtendedState();
				if (frame.isResizable() && isFrameResizable()) {
					setMaximizedBounds(frame);
					frame.setExtendedState(state | 6);
				} else if (frame.isResizable() && !isFrameResizable())
					frame.setExtendedState(state ^ 6);
			}
		}

		public void mouseDragged(MouseEvent evt) {
			if (windowAction == 1)
				try {
					Point windowPt = (Point) AccessController
							.doPrivileged(getLocationAction);
					windowPt.x -= dragXOffset;
					windowPt.y -= dragYOffset;
					window.setLocation(windowPt);
				} catch (PrivilegedActionException privilegedactionexception) {
				}
			else if (windowAction == 2) {
				Point pt = evt.getPoint();
				Dimension min = window.getMinimumSize();
				Rectangle bounds = window.getBounds();
				Rectangle startBounds = new Rectangle(bounds);
				if (resizeType == 11 || resizeType == 7 || resizeType == 5)
					bounds.width = Math.max(min.width,
							(dragDimension.width + pt.x) - dragXOffset);
				if (resizeType == 9 || resizeType == 4 || resizeType == 5)
					bounds.height = Math.max(min.height,
							(dragDimension.height + pt.y) - dragYOffset);
				if (resizeType == 8 || resizeType == 6 || resizeType == 7) {
					bounds.height = Math.max(min.height, (bounds.height - pt.y)
							+ dragYOffset);
					if (bounds.height != min.height)
						bounds.y += pt.y - dragYOffset;
				}
				if (resizeType == 10 || resizeType == 6 || resizeType == 4) {
					bounds.width = Math.max(min.width, (bounds.width - pt.x)
							+ dragXOffset);
					if (bounds.width != min.width)
						bounds.x += pt.x - dragXOffset;
				}
				if (!bounds.equals(startBounds))
					window.setBounds(bounds);
			}
		}

		public void mouseEntered(MouseEvent evt) {
			mouseMoved(evt);
		}

		public void mouseExited(MouseEvent evt) {
			window.setCursor(Cursor.getDefaultCursor());
		}

		public void mouseMoved(MouseEvent evt) {
			if (rootPane.getWindowDecorationStyle() == 0)
				return;
			int cursor = position2Cursor(window, evt.getX(), evt.getY());
			if (cursor != 0 && isWindowResizable())
				window.setCursor(Cursor.getPredefinedCursor(cursor));
			else
				window.setCursor(Cursor.getDefaultCursor());
		}

		public void mousePressed(MouseEvent evt) {
			if (rootPane.getWindowDecorationStyle() == 0)
				return;
			window.toFront();
			Point windowPoint = evt.getPoint();
			Point titlePanePoint = SwingUtilities.convertPoint(window,
					windowPoint, titlePane);
			int cursor = position2Cursor(window, evt.getX(), evt.getY());
			if (cursor == 0
					&& titlePane != null
					&& titlePane.contains(titlePanePoint)
					&& (dialog != null || frame != null
							&& frame.getExtendedState() != 6)) {
				windowAction = 1;
				dragXOffset = windowPoint.x;
				dragYOffset = windowPoint.y;
			} else if (isWindowResizable()) {
				windowAction = 2;
				dragXOffset = windowPoint.x;
				dragYOffset = windowPoint.y;
				dragDimension = new Dimension(window.getWidth(), window
						.getHeight());
				resizeType = position2Cursor(window, windowPoint.x,
						windowPoint.y);
			}
		}

		public void mouseReleased(MouseEvent evt) {
			if (windowAction == 2 && !window.isValid()) {
				window.validate();
				rootPane.repaint();
			}
			windowAction = -1;
			window.setCursor(Cursor.getDefaultCursor());
		}

		private int position2Cursor(Window w, int x, int y) {
			Insets insets = rootPane.getBorder().getBorderInsets(rootPane);
			int ww = w.getWidth();
			int wh = w.getHeight();
			int nwCornerSize = insets.top + insets.left;
			int neCornerSize = insets.top + insets.right;
			int swCornerSize = insets.bottom + insets.left;
			int seCornerSize = insets.bottom + insets.right;
			if (x < nwCornerSize && y < nwCornerSize)
				return 6;
			if (x > ww - neCornerSize && y < neCornerSize)
				return 7;
			if (x < swCornerSize && y > wh - swCornerSize)
				return 4;
			if (x > ww - seCornerSize && y > wh - seCornerSize)
				return 5;
			if (x < insets.top)
				return 10;
			if (x > ww - insets.bottom)
				return 11;
			if (y < insets.left)
				return 8;
			return y <= wh - insets.right ? 0 : 9;
		}
	}

	private static class SyntheticaRootLayout implements LayoutManager2 {

		SyntheticaRootLayout() {
		}

		public void addLayoutComponent(Component component, Object obj) {
		}

		public void addLayoutComponent(String s, Component component) {
		}

		public float getLayoutAlignmentX(Container target) {
			return 0.0F;
		}

		public float getLayoutAlignmentY(Container target) {
			return 0.0F;
		}

		public void invalidateLayout(Container container) {
		}

		public void layoutContainer(Container parent) {
			JRootPane rootPane = (JRootPane) parent;
			Rectangle bounds = rootPane.getBounds();
			Insets insets = rootPane.getInsets();
			int width = bounds.width - insets.right - insets.left;
			int height = bounds.height - insets.top - insets.bottom;
			int nextY = 0;
			if (rootPane.getLayeredPane() != null)
				rootPane.getLayeredPane().setBounds(insets.left, insets.top,
						width, height);
			if (rootPane.getGlassPane() != null)
				rootPane.getGlassPane().setBounds(insets.left, insets.top,
						width, height);
			JComponent titlePane = ((SyntheticaRootPaneUI) rootPane.getUI()).titlePane;
			Dimension dimT = titlePane.getPreferredSize();
			if (dimT != null) {
				titlePane.setBounds(0, 0, width, dimT.height);
				nextY += dimT.height;
			}
			JMenuBar mBar = rootPane.getJMenuBar();
			if (mBar != null) {
				Dimension dimM = mBar.getPreferredSize();
				mBar.setBounds(0, nextY, width, dimM.height);
				nextY += dimM.height;
			}
			Container cPane = rootPane.getContentPane();
			if (cPane != null)
				cPane.setBounds(0, nextY, width, height >= nextY ? height
						- nextY : 0);
		}

		public Dimension maximumLayoutSize(Container target) {
			Insets insets = target.getInsets();
			JRootPane root = (JRootPane) target;
			JComponent titlePane = ((SyntheticaRootPaneUI) root.getUI()).titlePane;
			Dimension dimC = new Dimension(0, 0);
			if (root.getContentPane() != null)
				dimC = root.getContentPane().getMaximumSize();
			else
				dimC = root.getSize();
			dimC = dimC != null ? dimC : new Dimension(0x7fffffff, 0x7fffffff);
			Dimension dimM = new Dimension(0, 0);
			if (root.getJMenuBar() != null)
				dimM = root.getJMenuBar().getMaximumSize();
			dimM = dimM != null ? dimM : new Dimension(0x7fffffff, 0x7fffffff);
			Dimension dimT = titlePane.getMaximumSize();
			dimT = dimT != null ? dimT : new Dimension(0x7fffffff, 0x7fffffff);
			int width = Math.max(dimC.width, Math.max(dimM.width, dimT.width));
			if (width != 0x7fffffff)
				width += insets.left + insets.right;
			int height = Math.max(dimC.height, Math.max(dimM.height,
					dimT.height));
			if (height != 0x7fffffff)
				height += insets.top + insets.bottom;
			return new Dimension(width, height);
		}

		public Dimension minimumLayoutSize(Container parent) {
			Insets insets = parent.getInsets();
			JRootPane root = (JRootPane) parent;
			JComponent titlePane = ((SyntheticaRootPaneUI) root.getUI()).titlePane;
			Dimension dimC = new Dimension(0, 0);
			if (root.getContentPane() != null)
				dimC = root.getContentPane().getMinimumSize();
			else
				dimC = root.getSize();
			dimC = dimC != null ? dimC : new Dimension(0, 0);
			Dimension dimM = new Dimension(0, 0);
			if (root.getJMenuBar() != null)
				dimM = root.getJMenuBar().getMinimumSize();
			dimM = dimM != null ? dimM : new Dimension(0, 0);
			Dimension dimT = titlePane.getMinimumSize();
			dimT = dimT != null ? dimT : new Dimension(0, 0);
			int width = Math.max(dimC.width, Math.max(dimM.width, dimT.width))
					+ insets.left + insets.right;
			int height = dimC.height + dimM.height + dimT.height + insets.top
					+ insets.bottom;
			return new Dimension(width, height);
		}

		public Dimension preferredLayoutSize(Container parent) {
			Insets insets = parent.getInsets();
			JRootPane root = (JRootPane) parent;
			JComponent titlePane = ((SyntheticaRootPaneUI) root.getUI()).titlePane;
			Dimension dimC = new Dimension(0, 0);
			if (root.getContentPane() != null)
				dimC = root.getContentPane().getPreferredSize();
			else
				dimC = root.getSize();
			dimC = dimC != null ? dimC : new Dimension(0, 0);
			Dimension dimM = new Dimension(0, 0);
			if (root.getJMenuBar() != null)
				dimM = root.getJMenuBar().getPreferredSize();
			dimM = dimM != null ? dimM : new Dimension(0, 0);
			Dimension dimT = titlePane.getPreferredSize();
			dimT = dimT != null ? dimT : new Dimension(0, 0);
			int width = Math.max(dimC.width, Math.max(dimM.width, dimT.width))
					+ insets.left + insets.right;
			int height = dimC.height + dimM.height + dimT.height + insets.top
					+ insets.bottom;
			return new Dimension(width, height);
		}

		public void removeLayoutComponent(Component component) {
		}
	}

	public static ComponentUI createUI(JComponent c) {
		return new SyntheticaRootPaneUI();
	}

	private Window window;

	private JRootPane rootPane;

	private LayoutManager layoutManager;

	private LayoutManager oldLayoutManager;

	private MouseInputListener mouseInputListener;

	private JComponent titlePane;

	public SyntheticaRootPaneUI() {
	}

	void installBorder(JRootPane root) {
		int style = root.getWindowDecorationStyle();
		if (style != 0)
			root.setBorder(new Border() {

				public Insets getBorderInsets(Component c) {
					Insets insets = (Insets) UIManager
							.get("Synthetica.rootPane.border.size");
					if (insets == null)
						insets = (Insets) UIManager
								.get("Synthetica.rootPane.border.insets");
					return insets;
				}

				public boolean isBorderOpaque() {
					return false;
				}

				public void paintBorder(Component c, Graphics g, int x, int y,
						int w, int h) {
					String imagePath = "Synthetica.rootPane.border";
					if (window.isActive())
						imagePath = (new StringBuilder(String
								.valueOf(imagePath))).append(".selected")
								.toString();
					imagePath = (String) UIManager.get(imagePath);
					Insets sInsets = (Insets) UIManager
							.get("Synthetica.rootPane.border.insets");
					Insets dInsets = sInsets;
					ImagePainter iPainter = new ImagePainter(g, x, y, w, h,
							imagePath, sInsets, dInsets, 0, 0);
					iPainter.drawBorder();
				}
			});
	}

	private void installClientDecorations(JRootPane root) {
		JComponent titlePane = new SyntheticaTitlePane(root, this);
		setTitlePane(root, titlePane);
		installBorder(root);
		installWindowListeners(root, root.getParent());
		installLayout(root);
	}

	private void installLayout(JRootPane root) {
		if (layoutManager == null)
			layoutManager = new SyntheticaRootLayout();
		oldLayoutManager = root.getLayout();
		root.setLayout(layoutManager);
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		rootPane = (JRootPane) c;
		if (rootPane.getWindowDecorationStyle() != 0)
			installClientDecorations(rootPane);
	}

	private void installWindowListeners(JRootPane root, Component parent) {
		window = (parent instanceof Window) ? (Window) parent : SwingUtilities
				.getWindowAncestor(parent);
		if (window != null) {
			if (mouseInputListener == null)
				mouseInputListener = new MouseInputHandler();
			window.addMouseListener(mouseInputListener);
			window.addMouseMotionListener(mouseInputListener);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);
		String propertyName = e.getPropertyName();
		if (propertyName == null)
			return;
		if (propertyName.equals("windowDecorationStyle")) {
			uninstallClientDecorations(rootPane);
			if (rootPane.getWindowDecorationStyle() != 0)
				installClientDecorations(rootPane);
		} else if (propertyName.equals("ancestor")) {
			uninstallWindowListeners(rootPane);
			if (rootPane.getWindowDecorationStyle() != 0)
				installWindowListeners(rootPane, rootPane.getParent());
		}
	}

	public void setMaximizedBounds(Frame frame) {
		GraphicsConfiguration gc = frame.getGraphicsConfiguration();
		Rectangle screenBounds = gc.getBounds();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
		Insets insets = rootPane.getBorder().getBorderInsets(rootPane);
		Rectangle maxBounds = new Rectangle(
				(screenBounds.x + screenInsets.left) - insets.left,
				(screenBounds.y + screenInsets.top) - insets.right,
				screenBounds.width
						- ((screenInsets.left + screenInsets.right)
								- insets.left - insets.right),
				screenBounds.height
						- ((screenInsets.top + screenInsets.bottom)
								- insets.top - insets.bottom));
		frame.setMaximizedBounds(maxBounds);
	}

	private void setTitlePane(JRootPane root, JComponent titlePane) {
		JLayeredPane layeredPane = root.getLayeredPane();
		if (this.titlePane != null) {
			this.titlePane.setVisible(false);
			layeredPane.remove(this.titlePane);
		}
		if (titlePane != null) {
			layeredPane.add(titlePane, JLayeredPane.FRAME_CONTENT_LAYER);
			titlePane.setVisible(true);
		}
		this.titlePane = titlePane;
	}

	private void uninstallBorder(JRootPane root) {
		root.setBorder(null);
	}

	private void uninstallClientDecorations(JRootPane root) {
		setTitlePane(root, null);
		uninstallBorder(root);
		uninstallWindowListeners(root);
		uninstallLayout(root);
	}

	private void uninstallLayout(JRootPane root) {
		if (oldLayoutManager != null)
			root.setLayout(oldLayoutManager);
		oldLayoutManager = null;
		layoutManager = null;
	}

	@Override
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
		uninstallClientDecorations(rootPane);
		rootPane = null;
	}

	private void uninstallWindowListeners(JRootPane root) {
		if (window != null) {
			window.removeMouseListener(mouseInputListener);
			window.removeMouseMotionListener(mouseInputListener);
		}
		mouseInputListener = null;
		window = null;
	}

}
