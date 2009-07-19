package de.javasoft.plaf.synthetica;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;

import de.javasoft.plaf.synthetica.painter.ImagePainter;
import de.javasoft.util.java2d.DropShadow;

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
public class SyntheticaTitlePane extends JPanel {

	private static final long serialVersionUID = 0xaa74455e0b6e7e41L;

	private JRootPane rootPane;

	private BasicRootPaneUI rootPaneUI;

	private Window window;

	private Frame frame;

	private Dialog dialog;

	private JButton menuButton;

	private JLabel titleLabel;

	private JButton toggleButton;

	private JButton iconifyButton;

	private JButton closeButton;

	private JPopupMenu systemMenu;

	private Action closeAction;

	private Action iconifyAction;

	private Action restoreAction;

	private Action maximizeAction;

	private WindowListener windowListener;

	private PropertyChangeListener propertyChangeListener;

	private boolean selected;

	public SyntheticaTitlePane(JRootPane root, BasicRootPaneUI ui) {
		selected = true;
		rootPane = root;
		rootPaneUI = ui;
		java.awt.Container parent = rootPane.getParent();
		window = (parent instanceof Window) ? (Window) parent : SwingUtilities
				.getWindowAncestor(parent);
		if (window instanceof Frame)
			frame = (Frame) window;
		else if (window instanceof Dialog)
			dialog = (Dialog) window;
		int xGap = 4;
		javax.swing.border.Border titleBorder = BorderFactory
				.createEmptyBorder(3, 0, 4, 0);
		setLayout(new BoxLayout(this, 2));
		add(Box.createHorizontalStrut(xGap));
		closeAction = new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				close();
			}
		};
		int decorationStyle = rootPane.getWindowDecorationStyle();
		if (decorationStyle == 1) {
			iconifyAction = new AbstractAction() {

				public void actionPerformed(ActionEvent e) {
					iconify();
				}
			};
			restoreAction = new AbstractAction() {

				public void actionPerformed(ActionEvent e) {
					restore();
				}
			};
			maximizeAction = new AbstractAction() {

				public void actionPerformed(ActionEvent e) {
					maximize();
				}
			};
			menuButton = createTitlePaneButton();
			menuButton.setIcon(getFrameIcon());
			frame.addPropertyChangeListener(new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					if ("iconImage".equals(evt.getPropertyName()))
						menuButton.setIcon(getFrameIcon());
				}
			});
			systemMenu = new JPopupMenu();
			addMenuItems(systemMenu);
			menuButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					systemMenu.show(SyntheticaTitlePane.this, 0, getHeight());
				}
			});
			add(menuButton);
			add(Box.createHorizontalStrut(xGap));
		}
		titleLabel = getTitle() == null ? new JLabel("") : new JLabel(" ");
		titleLabel.setBorder(titleBorder);
		add(titleLabel);
		add(Box.createHorizontalGlue());
		createButtons();
		if (decorationStyle == 1) {
			add(Box.createHorizontalStrut(xGap));
			add(iconifyButton);
			add(toggleButton);
			add(Box.createHorizontalStrut(xGap));
		}
		add(closeButton);
		add(Box.createHorizontalStrut(xGap));
		installListeners();
		setComponentsActiveState(window.isActive());
	}

	private void addMenuItems(JPopupMenu menu) {
		JMenuItem mi = menu.add(restoreAction);
		mi.setText(UIManager
				.getString("InternalFrameTitlePane.restoreButtonText"));
		mi.setMnemonic('R');
		mi.setEnabled(isFrameResizable());
		mi = menu.add(iconifyAction);
		mi.setText(UIManager
				.getString("InternalFrameTitlePane.minimizeButtonText"));
		mi.setMnemonic('n');
		if (Toolkit.getDefaultToolkit().isFrameStateSupported(6)) {
			mi = menu.add(maximizeAction);
			mi.setText(UIManager
					.getString("InternalFrameTitlePane.maximizeButtonText"));
			mi.setMnemonic('x');
			mi.setEnabled(isFrameResizable());
		}
		menu.addSeparator();
		mi = menu.add(closeAction);
		mi.setText(UIManager
				.getString("InternalFrameTitlePane.closeButtonText"));
		mi.setMnemonic('C');
	}

	private void close() {
		window.dispatchEvent(new WindowEvent(window, 201));
	}

	private void createButtons() {
		MouseAdapter listener = new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent evt) {
				JButton b = (JButton) evt.getSource();
				String name = (new StringBuilder("Synthetica.")).append(
						b.getName()).append("Icon.hover").toString();
				b.setIcon((Icon) b.getClientProperty(name));
			}

			@Override
			public void mouseExited(MouseEvent evt) {
				JButton b = (JButton) evt.getSource();
				String name = (new StringBuilder("Synthetica.")).append(
						b.getName()).append("Icon").toString();
				b.setIcon((Icon) b.getClientProperty(name));
			}
		};
		SynthStyle ss = SynthLookAndFeel.getStyle(rootPane, Region.ROOT_PANE);
		SynthContext sc = new SynthContext(rootPane, Region.ROOT_PANE, ss, 1024);
		Icon closeIcon = ss.getIcon(sc, "RootPane.closeIcon");
		Icon iconifyIcon = ss.getIcon(sc, "RootPane.iconifyIcon");
		Icon maximizeIcon = ss.getIcon(sc, "RootPane.maximizeIcon");
		Icon minimizeIcon = ss.getIcon(sc, "RootPane.minimizeIcon");
		sc = new SynthContext(rootPane, Region.ROOT_PANE, ss, 2);
		Icon closeIconHover = ss.getIcon(sc, "RootPane.closeIcon");
		Icon iconifyIconHover = ss.getIcon(sc, "RootPane.iconifyIcon");
		Icon maximizeIconHover = ss.getIcon(sc, "RootPane.maximizeIcon");
		Icon minimizeIconHover = ss.getIcon(sc, "RootPane.minimizeIcon");
		closeButton = createTitlePaneButton();
		closeButton.setName("close");
		closeButton.putClientProperty("Synthetica.closeIcon", closeIcon);
		closeButton.putClientProperty("Synthetica.closeIcon.hover",
				closeIconHover);
		closeButton.setAction(closeAction);
		closeButton.getAccessibleContext().setAccessibleName("Close");
		closeButton.setIcon(closeIcon);
		closeButton.addMouseListener(listener);
		if (rootPane.getWindowDecorationStyle() == 1) {
			iconifyButton = createTitlePaneButton();
			iconifyButton.setName("iconify");
			iconifyButton.putClientProperty("Synthetica.iconifyIcon",
					iconifyIcon);
			iconifyButton.putClientProperty("Synthetica.iconifyIcon.hover",
					iconifyIconHover);
			iconifyButton.setAction(iconifyAction);
			iconifyButton.getAccessibleContext().setAccessibleName("Iconify");
			iconifyButton.setIcon(iconifyIcon);
			iconifyButton.addMouseListener(listener);
			toggleButton = createTitlePaneButton();
			toggleButton.putClientProperty("Synthetica.maximizeIcon",
					maximizeIcon);
			toggleButton.putClientProperty("Synthetica.maximizeIcon.hover",
					maximizeIconHover);
			toggleButton.putClientProperty("Synthetica.minimizeIcon",
					minimizeIcon);
			toggleButton.putClientProperty("Synthetica.minimizeIcon.hover",
					minimizeIconHover);
			updateToggleButton();
			toggleButton.addMouseListener(listener);
		}
	}

	private JButton createTitlePaneButton() {
		JButton button = new JButton();
		button.setFocusPainted(false);
		button.setFocusable(false);
		button.setOpaque(false);
		button.setBorder(BorderFactory.createEmptyBorder());
		return button;
	}

	private Icon getFrameIcon() {
		Image image = frame == null ? null : frame.getIconImage();
		Icon icon = null;
		if (image != null) {
			if (UIManager
					.getBoolean("Synthetica.rootPane.titlePane.menuButton.useOriginalImageSize"))
				icon = new ImageIcon(image);
			else
				icon = new ImageIcon(image.getScaledInstance(16, 16, 4));
		} else {
			SynthStyle ss = SynthLookAndFeel.getStyle(rootPane,
					Region.ROOT_PANE);
			SynthContext sc = new SynthContext(rootPane, Region.ROOT_PANE, ss,
					1024);
			icon = ss.getIcon(sc, "RootPane.icon");
		}
		return icon;
	}

	@Override
	public JRootPane getRootPane() {
		return rootPane;
	}

	private String getTitle() {
		if (frame != null)
			return frame.getTitle();
		if (dialog != null)
			return dialog.getTitle();
		else
			return null;
	}

	private void iconify() {
		int state = frame.getExtendedState();
		frame.setExtendedState(state | 1);
	}

	private void installListeners() {
		windowListener = new WindowAdapter() {

			@Override
			public void windowActivated(WindowEvent ev) {
				setActive(true);
				selected = true;
			}

			@Override
			public void windowDeactivated(WindowEvent ev) {
				setActive(false);
				selected = false;
			}

			@Override
			public void windowStateChanged(WindowEvent e) {
				updateToggleButton();
			}
		};
		window.addWindowListener(windowListener);
		window.addWindowStateListener((WindowStateListener) windowListener);
		propertyChangeListener = new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent pce) {
				String name = pce.getPropertyName();
				if ("title".equals(name))
					titleLabel.setText(getTitle());
				else if ("resizable".equals(name)) {
					boolean resizable = ((Boolean) pce.getNewValue())
							.booleanValue();
					toggleButton.setEnabled(resizable);
					systemMenu.removeAll();
					addMenuItems(systemMenu);
				}
			}
		};
		window.addPropertyChangeListener(propertyChangeListener);
	}

	private boolean isFrameMaximized() {
		return frame != null && (frame.getExtendedState() & 6) == 6;
	}

	private boolean isFrameResizable() {
		return frame != null && frame.isResizable();
	}

	private boolean isSelected() {
		return window != null ? window.isActive() : true;
	}

	private void maximize() {
		int state = frame.getExtendedState();
		((SyntheticaRootPaneUI) rootPaneUI).setMaximizedBounds(frame);
		frame.setExtendedState(state | 6);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		String imagePath = "Synthetica.rootPane.titlePane.background";
		if (isSelected())
			imagePath = (new StringBuilder(String.valueOf(imagePath))).append(
					".selected").toString();
		imagePath = UIManager.getString(imagePath);
		java.awt.Insets insets = UIManager
				.getInsets("Synthetica.rootPane.titlePane.background.insets");
		int yPolicy = 0;
		if (UIManager
				.getBoolean("Synthetica.rootPane.titlePane.background.verticalTiled"))
			yPolicy = 1;
		ImagePainter iPainter = new ImagePainter(g, 0, 0, getWidth(),
				getHeight(), imagePath, insets, insets, 0, yPolicy);
		iPainter.draw();
		String title = getTitle();
		if (title == null || title.length() == 0)
			return;
		FontMetrics fm = getFontMetrics(getFont());
		int th = fm.getHeight();
		int tw = fm.stringWidth(title);
		int tx = menuButton == null ? 4 : menuButton.getWidth() + 8;
		int ty = (getSize().height - th) / 2;
		JInternalFrame iFrame = new JInternalFrame();
		SynthStyle ss = SynthLookAndFeel.getStyle(iFrame,
				Region.INTERNAL_FRAME_TITLE_PANE);
		int state = 1024;
		if (selected)
			state = 512;
		SynthContext sc = new SynthContext(iFrame,
				Region.INTERNAL_FRAME_TITLE_PANE, ss, state);
		if (UIManager.getBoolean("Synthetica.rootPane.titlePane.dropShadow")
				&& selected) {
			BufferedImage image = new BufferedImage(tw, th, 2);
			Graphics g2 = image.createGraphics();
			g2.setFont(getFont());
			g2.drawString(title, 0, fm.getAscent());
			DropShadow ds = new DropShadow(image);
			ds.paintShadow(g, tx, ty);
		}
		g.setColor(ss.getColor(sc, ColorType.FOREGROUND));
		ss.getGraphicsUtils(sc).paintText(sc, g, title, tx, ty, -1);
	}

	private void restore() {
		int state = frame.getExtendedState();
		if ((state & 1) == 1)
			frame.setExtendedState(state ^ 1);
		else
			frame.setExtendedState(state ^ 6);
	}

	private void setActive(boolean active) {
		setComponentsActiveState(active);
		getRootPane().repaint();
	}

	private void setComponentsActiveState(boolean active) {
		javax.swing.JComponent c = new JInternalFrame();
		SynthStyle ss = SynthLookAndFeel.getStyleFactory().getStyle(c,
				Region.INTERNAL_FRAME_TITLE_PANE);
		int state = 1024;
		if (active)
			state = 512;
		SynthContext sc = new SynthContext(c, Region.INTERNAL_FRAME_TITLE_PANE,
				ss, state);
		Font font = ss.getFont(sc);
		font = font.deriveFont(font.getStyle(), font.getSize());
		titleLabel.setFont(font);
		Color fg = ss.getColor(sc, ColorType.FOREGROUND);
		fg = new Color(fg.getRGB());
		titleLabel.setForeground(fg);
		closeButton.putClientProperty("paintActive", Boolean.valueOf(active));
		if (rootPane.getWindowDecorationStyle() == 1) {
			iconifyButton.putClientProperty("paintActive", Boolean
					.valueOf(active));
			toggleButton.putClientProperty("paintActive", Boolean
					.valueOf(active));
		}
	}

	private void updateToggleButton() {
		Icon icon = null;
		if (!isFrameMaximized()) {
			toggleButton.setAction(maximizeAction);
			toggleButton.getAccessibleContext().setAccessibleName("Maximize");
			toggleButton.setName("maximize");
			icon = (Icon) toggleButton
					.getClientProperty("Synthetica.maximizeIcon");
		} else {
			toggleButton.setAction(restoreAction);
			toggleButton.getAccessibleContext().setAccessibleName("Restore");
			toggleButton.setName("minimize");
			icon = (Icon) toggleButton
					.getClientProperty("Synthetica.minimizeIcon");
		}
		toggleButton.setIcon(icon);
		toggleButton.setEnabled(isFrameResizable());
	}

}
