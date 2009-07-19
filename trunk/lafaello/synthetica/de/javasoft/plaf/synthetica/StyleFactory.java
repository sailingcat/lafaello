package de.javasoft.plaf.synthetica;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;
import javax.swing.tree.TreeCellRenderer;

import sun.swing.plaf.synth.DefaultSynthStyle;
import de.javasoft.plaf.synthetica.styles.CheckBoxStyle;
import de.javasoft.plaf.synthetica.styles.FormattedTextFieldStyle;
import de.javasoft.plaf.synthetica.styles.ListStyle;
import de.javasoft.plaf.synthetica.styles.MenuItemStyle;
import de.javasoft.plaf.synthetica.styles.MenuStyle;
import de.javasoft.plaf.synthetica.styles.PopupMenuStyle;
import de.javasoft.plaf.synthetica.styles.RadioButtonStyle;
import de.javasoft.plaf.synthetica.styles.ScrollPaneStyle;
import de.javasoft.plaf.synthetica.styles.TableStyle;
import de.javasoft.plaf.synthetica.styles.TextFieldStyle;
import de.javasoft.plaf.synthetica.styles.ToolBarSeparatorStyle;
import de.javasoft.plaf.synthetica.styles.ToolBarStyle;

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
public class StyleFactory extends SynthStyleFactory {
	private class ComponentProperty {

		static final String OPAQUE = "OPAQUE";

		static final String INSETS = "INSETS";

		static final String BUTTON_MARGIN = "BUTTON_MARGIN";
		static final String TOOLBAR_SEPARATOR_SIZE = "TOOLBAR_SEPARATOR_SIZE";
		static final String PROPERTY_CHANGE_LISTENERS = "PROPERTY_CHANGE_LISTENERS";
		static final String COMPONENT_LISTENERS = "COMPONENT_LISTENERS";
		static final String MOUSE_LISTENERS = "MOUSE_LISTENERS";
		static final String MOUSE_MOTION_LISTENERS = "MOUSE_MOTION_LISTENERS";
		static final String CONTAINER_LISTENERS = "CONTAINER_LISTENERS";
		static final String TREE_CELL_RENDERER = "TREE_CELL_RENDERER";
		private WeakReference component;
		private String propertyName;
		private WeakReference value;
		private int componentHashCode;

		ComponentProperty(Component c, String propertyName, Object value) {
			super();
			component = new WeakReference(c);
			this.propertyName = propertyName;
			this.value = new WeakReference(value);
			componentHashCode = (new StringBuilder(String.valueOf(c.hashCode())))
					.append(propertyName).toString().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return componentHashCode == obj.hashCode();
		}

		@Override
		public int hashCode() {
			return componentHashCode;
		}
	}

	public class ComponentPropertyStore {

		private HashSet componentProperties;

		private boolean enabled;

		private Thread cleanerThread;

		ComponentPropertyStore() {
			super();
			enabled = true;
			componentProperties = new HashSet(500);
			cleanerThread = new Thread() {

				@Override
				public void run() {
					do {
						if (isInterrupted())
							break;
						synchronized (componentProperties) {
							for (Iterator it = componentProperties.iterator(); it
									.hasNext();) {
								ComponentProperty prop = (ComponentProperty) it
										.next();
								if (prop.component.get() == null)
									it.remove();
							}

						}
						if (isInterrupted())
							break;
						try {
							sleep(60000L);
						} catch (InterruptedException e) {
							interrupt();
						}
					} while (true);
				}
			};
			cleanerThread.start();
		}

		void restoreAllComponentProperties() {
			ComponentProperty cp;
			for (Iterator iterator = componentProperties.iterator(); iterator
					.hasNext(); restoreComponentProperty(cp))
				cp = (ComponentProperty) iterator.next();

			synchronized (componentProperties) {
				componentProperties.clear();
			}
			prepareMetalLAFSwitch = false;
		}

		private void restoreComponentProperty(ComponentProperty cp) {
			JComponent c = (JComponent) cp.component.get();
			if (c == null)
				return;
			String propertyName = cp.propertyName;
			Object value = cp.value.get();
			if (propertyName.equals("OPAQUE") && prepareMetalLAFSwitch)
				setOpaqueDefault4Metal(c);
			else if (propertyName.equals("INSETS")) {
				Insets insets = (Insets) value;
				c.setBorder(new EmptyBorder(insets));
			} else if (propertyName.equals("BUTTON_MARGIN")) {
				Insets insets = (Insets) value;
				((AbstractButton) c).setMargin(insets);
			} else if (propertyName.equals("TOOLBAR_SEPARATOR_SIZE")) {
				Dimension dimension = (Dimension) value;
				if (dimension == null)
					dimension = new Dimension(10, 10);
				((javax.swing.JToolBar.Separator) c)
						.setSeparatorSize(dimension);
			} else if (propertyName.equals("TREE_CELL_RENDERER")) {
				TreeCellRenderer cr = (TreeCellRenderer) value;
				((JTree) c).setCellRenderer(cr);
			} else if (propertyName.equals("PROPERTY_CHANGE_LISTENERS")) {
				PropertyChangeListener listeners[] = c
						.getPropertyChangeListeners();
				for (int i = 0; i < listeners.length; i++)
					if (listeners[i].getClass().getName()
							.contains("synthetica"))
						c.removePropertyChangeListener(listeners[i]);

			} else if (propertyName.equals("COMPONENT_LISTENERS")) {
				java.awt.event.ComponentListener listeners[] = c
						.getComponentListeners();
				for (int i = 0; i < listeners.length; i++)
					if (listeners[i].getClass().getName()
							.contains("synthetica"))
						c.removeComponentListener(listeners[i]);

			} else if (propertyName.equals("CONTAINER_LISTENERS")) {
				ContainerListener listeners[] = c.getContainerListeners();
				for (int i = 0; i < listeners.length; i++)
					if (listeners[i].getClass().getName()
							.contains("synthetica"))
						c.removeContainerListener(listeners[i]);

			} else if (propertyName.equals("MOUSE_LISTENERS")) {
				MouseListener listeners[] = c.getMouseListeners();
				for (int i = 0; i < listeners.length; i++)
					if (listeners[i].getClass().getName()
							.contains("synthetica"))
						c.removeMouseListener(listeners[i]);

			} else if (propertyName.equals("MOUSE_MOTION_LISTENERS")) {
				java.awt.event.MouseMotionListener listeners[] = c
						.getMouseMotionListeners();
				for (int i = 0; i < listeners.length; i++)
					if (listeners[i].getClass().getName()
							.contains("synthetica"))
						c.removeMouseMotionListener(listeners[i]);

			}
		}

		public void storeComponentProperty(Component c, String propertyName) {
			if (!enabled)
				return;
			Object value = null;
			if (propertyName.equals("OPAQUE"))
				value = Boolean.valueOf(c.isOpaque());
			else if (propertyName.equals("INSETS"))
				value = ((JComponent) c).getInsets();
			else if (propertyName.equals("BUTTON_MARGIN"))
				value = ((AbstractButton) c).getMargin();
			else if (propertyName.equals("TOOLBAR_SEPARATOR_SIZE"))
				value = ((javax.swing.JToolBar.Separator) c).getSeparatorSize();
			else if (propertyName.equals("TREE_CELL_RENDERER"))
				value = ((JTree) c).getCellRenderer();
			ComponentProperty prop = new ComponentProperty(c, propertyName,
					value);
			if (!componentProperties.contains(prop))
				synchronized (componentProperties) {
					componentProperties.add(prop);
				}
		}
	}

	private SynthStyleFactory synthStyleFactory;

	private ComponentPropertyStore componentPropertyStore;

	private boolean prepareMetalLAFSwitch;

	public StyleFactory(SynthStyleFactory synthStyleFactory) {
		this.synthStyleFactory = null;
		prepareMetalLAFSwitch = false;
		this.synthStyleFactory = synthStyleFactory;
		componentPropertyStore = new ComponentPropertyStore();
	}

	private boolean eventListenerExist(EventListener listeners[]) {
		EventListener aeventlistener[] = listeners;
		int i = 0;
		for (int j = aeventlistener.length; i < j; i++) {
			EventListener el = aeventlistener[i];
			if (el.getClass().getName().startsWith(getClass().getName()))
				return true;
		}

		return false;
	}

	public ComponentPropertyStore getComponentPropertyStore() {
		return componentPropertyStore;
	}

	@Override
	public SynthStyle getStyle(JComponent c, Region region) {
		SynthStyle style = synthStyleFactory.getStyle(c, region);
		String name = c.getName();
		if (SyntheticaLookAndFeel.getFontName() != null) {
			int fontStyle = ((DefaultSynthStyle) style).getStateInfo(0)
					.getFont().getStyle();
			Font font = new FontUIResource(SyntheticaLookAndFeel.getFontName(),
					fontStyle, SyntheticaLookAndFeel.getFontSize());
			((DefaultSynthStyle) style).getStateInfo(0).setFont(font);
		} else {
			Font font = ((DefaultSynthStyle) style).getStateInfo(0).getFont();
			SyntheticaLookAndFeel.setFont(font.getName(), font.getSize());
		}
		if (region == Region.ARROW_BUTTON) {
			componentPropertyStore.storeComponentProperty(c, "OPAQUE");
			if (name != null && name.startsWith("Spinner.")) {
				int width = UIManager
						.getInt("Synthetica.spinner.arrowButton.width");
				if (width > 0)
					c.setPreferredSize(new Dimension(width, c
							.getPreferredSize().height));
			}
		} else if (region == Region.BUTTON)
			componentPropertyStore.storeComponentProperty(c, "BUTTON_MARGIN");
		else if (region == Region.INTERNAL_FRAME_TITLE_PANE) {
			componentPropertyStore.storeComponentProperty(c,
					"PROPERTY_CHANGE_LISTENERS");
			if (!eventListenerExist(c.getPropertyChangeListeners()))
				c.addPropertyChangeListener(new PropertyChangeListener() {

					public void propertyChange(PropertyChangeEvent evt) {
						if ("ancestor".equals(evt.getPropertyName())) {
							final JComponent c = (JComponent) evt.getSource();
							Component components[] = c.getComponents();
							for (int i = 0; i < components.length; i++) {
								Component comp = components[i];
								if (comp instanceof JButton) {
									final String name = comp.getName();
									boolean addListener = true;
									MouseListener listeners[] = comp
											.getMouseListeners();
									for (int j = 0; j < listeners.length; j++) {
										if (!listeners[j].getClass().getName()
												.contains("synthetica"))
											continue;
										addListener = false;
										break;
									}

									if (addListener) {
										componentPropertyStore
												.storeComponentProperty(comp,
														"MOUSE_LISTENERS");
										comp
												.addMouseListener(new MouseAdapter() {

													@Override
													public void mouseEntered(
															MouseEvent evt) {
														c
																.putClientProperty(
																		"Synthetica.MOUSE_OVER",
																		name);
													}

													@Override
													public void mouseExited(
															MouseEvent evt) {
														c
																.putClientProperty(
																		"Synthetica.MOUSE_OVER",
																		null);
													}

													@Override
													public void mouseReleased(
															final MouseEvent evt) {
														if (c
																.getClientProperty("Synthetica.MOUSE_OVER") == null) {
															return;
														} else {
															(new Thread() {
																@Override
																public void run() {
																	try {
																		sleep(100L);
																	} catch (InterruptedException interruptedexception) {
																	}
																	JButton b = (JButton) evt
																			.getSource();
																	b
																			.dispatchEvent(new MouseEvent(
																					b,
																					505,
																					evt
																							.getWhen(),
																					evt
																							.getModifiers(),
																					evt
																							.getX(),
																					evt
																							.getY(),
																					evt
																							.getClickCount(),
																					evt
																							.isPopupTrigger()));
																}
															}).start();
															return;
														}
													}
												});
									}
								}
							}

						}
					}
				});
		} else if (region == Region.CHECK_BOX) {
			componentPropertyStore.storeComponentProperty(c, "MOUSE_LISTENERS");
			final JCheckBox cb = (JCheckBox) c;
			if (!eventListenerExist(c.getMouseListeners()))
				c.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseEntered(MouseEvent arg0) {
						cb.putClientProperty("Synthetica.MOUSE_OVER", Boolean
								.valueOf(true));
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
						cb.putClientProperty("Synthetica.MOUSE_OVER", Boolean
								.valueOf(false));
					}
				});
			style = new CheckBoxStyle(style);
		} else if (region == Region.FORMATTED_TEXT_FIELD)
			style = new FormattedTextFieldStyle(style);
		else if (region == Region.INTERNAL_FRAME) {
			componentPropertyStore.storeComponentProperty(c, "OPAQUE");
			c.setOpaque(false);
			componentPropertyStore.storeComponentProperty(c,
					"PROPERTY_CHANGE_LISTENERS");
			if (!eventListenerExist(c.getPropertyChangeListeners()))
				c.addPropertyChangeListener(new PropertyChangeListener() {

					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals("frameIcon")) {
							JInternalFrame frame = (JInternalFrame) evt
									.getSource();
							try {
								Component comps[] = frame.getComponents();
								BasicInternalFrameTitlePane titlePane = null;
								for (int i = 0; i < comps.length; i++) {
									if (!"InternalFrame.northPane"
											.equals(comps[i].getName()))
										continue;
									titlePane = (BasicInternalFrameTitlePane) comps[i];
									break;
								}

								if (titlePane != null) {
									Class clazz = Class
											.forName("javax.swing.plaf.synth.SynthInternalFrameTitlePane");
									Field field = clazz
											.getDeclaredField("menuButton");
									field.setAccessible(true);
									JButton button = null;
									button = (JButton) field.get(titlePane);
									button.setIcon(frame.getFrameIcon());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
		} else if (region == Region.LIST)
			style = ListStyle.getStyle(style, c, region);
		else if (region == Region.SCROLL_PANE)
			style = ScrollPaneStyle.getStyle(style, c, region);
		else if (region == Region.SPLIT_PANE_DIVIDER) {
			if (UIManager
					.getBoolean("Syntetica.splitPane.centerOneTouchButtons")
					&& !eventListenerExist(c.getComponentListeners())) {
				componentPropertyStore.storeComponentProperty(c,
						"COMPONENT_LISTENERS");
				c.addComponentListener(new ComponentAdapter() {

					@Override
					public void componentResized(ComponentEvent e) {
						((JSplitPane) e.getSource()).updateUI();
						super.componentResized(e);
					}
				});
			}
		} else if (region == Region.MENU) {
			componentPropertyStore.storeComponentProperty(c, "OPAQUE");
			final DefaultSynthStyle fStyle = (DefaultSynthStyle) style;
			final JMenu menu = (JMenu) c;
			final MouseListener listener = new MouseAdapter() {

				@Override
				public void mouseEntered(MouseEvent evt) {
					JMenu m = (JMenu) evt.getSource();
					m.putClientProperty("Synthetica.MOUSE_OVER", Boolean
							.valueOf(true));
					Color c = fStyle.getColor(m, Region.MENU, 512,
							ColorType.TEXT_FOREGROUND);
					m.setForeground(new Color(c.getRGB()));
					m.repaint();
				}

				@Override
				public void mouseExited(MouseEvent evt) {
					JMenu m = (JMenu) evt.getSource();
					m.putClientProperty("Synthetica.MOUSE_OVER", Boolean
							.valueOf(false));
					Color c = fStyle.getColor(m, Region.MENU, 1024,
							ColorType.TEXT_FOREGROUND);
					m.setForeground(c);
					m.repaint();
				}
			};
			if (menu.isTopLevelMenu()) {
				if (menu.isEnabled()
						&& !eventListenerExist(menu.getMouseListeners())) {
					componentPropertyStore.storeComponentProperty(c,
							"MOUSE_LISTENERS");
					menu.addMouseListener(listener);
				}
			} else if (!eventListenerExist(c.getPropertyChangeListeners())) {
				componentPropertyStore.storeComponentProperty(c,
						"PROPERTY_CHANGE_LISTENERS");
				c.addPropertyChangeListener(new PropertyChangeListener() {

					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals("ancestor")) {
							if (!menu.isEnabled() || !menu.isTopLevelMenu())
								return;
							componentPropertyStore.storeComponentProperty(menu,
									"MOUSE_LISTENERS");
							menu.addMouseListener(listener);
						}
					}
				});
			}
			style = MenuStyle.getStyle(style, c, region);
		} else if (region == Region.MENU_ITEM)
			style = MenuItemStyle.getStyle(style, c, region);
		else if (region == Region.RADIO_BUTTON_MENU_ITEM)
			style = MenuItemStyle.getStyle(style, c, region);
		else if (region == Region.CHECK_BOX_MENU_ITEM)
			style = MenuItemStyle.getStyle(style, c, region);
		else if (region == Region.TABLE) {
			JTable table = (JTable) c;
			if (table.getDefaultRenderer(Icon.class) == null)
				table.setDefaultRenderer(Icon.class, table
						.getDefaultRenderer(ImageIcon.class));
			style = TableStyle.getStyle(style, c, region);
		} else if (region == Region.TABBED_PANE_TAB) {
			if (!eventListenerExist(c.getMouseMotionListeners())) {
				componentPropertyStore.storeComponentProperty(c,
						"MOUSE_MOTION_LISTENERS");
				c.addMouseMotionListener(new MouseMotionAdapter() {

					@Override
					public void mouseMoved(MouseEvent evt) {
						JTabbedPane tp = (JTabbedPane) evt.getSource();
						Integer iHover = (Integer) tp
								.getClientProperty("Synthetica.MOUSE_OVER");
						int hover = iHover != null ? iHover.intValue() : -1;
						int newHover = -1;
						int tabCount = tp.getTabCount();
						for (int i = 0; i < tabCount; i++) {
							if (!tp.getBoundsAt(i).contains(evt.getPoint()))
								continue;
							newHover = i;
							break;
						}

						if (hover != newHover) {
							tp.putClientProperty("Synthetica.MOUSE_OVER",
									Integer.valueOf(newHover));
							if (hover >= 0)
								tp.repaint(tp.getBoundsAt(hover));
							if (newHover >= 0)
								tp.repaint(tp.getBoundsAt(newHover));
						}
					}
				});
			}
			if (!eventListenerExist(c.getMouseListeners())) {
				componentPropertyStore.storeComponentProperty(c,
						"MOUSE_LISTENERS");
				c.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseEntered(MouseEvent evt) {
						JTabbedPane tp = (JTabbedPane) evt.getSource();
						tp
								.dispatchEvent(new MouseEvent(tp, 503, evt
										.getWhen(), evt.getModifiers(), evt
										.getX(), evt.getY(), evt
										.getClickCount(), evt.isPopupTrigger()));
					}

					@Override
					public void mouseExited(MouseEvent evt) {
						JTabbedPane tp = (JTabbedPane) evt.getSource();
						Integer iHover = (Integer) tp
								.getClientProperty("Synthetica.MOUSE_OVER");
						int hover = iHover != null ? iHover.intValue() : -1;
						if (hover != -1) {
							tp.putClientProperty("Synthetica.MOUSE_OVER",
									Integer.valueOf(-1));
							tp.repaint();
						}
					}
				});
			}
			JTabbedPane tabbedPane = (JTabbedPane) c;
			if (!eventListenerExist(tabbedPane.getContainerListeners())) {
				componentPropertyStore.storeComponentProperty(c,
						"CONTAINER_LISTENERS");
				storeContainerComponentProperties(tabbedPane, "OPAQUE");
				tabbedPane.addContainerListener(new ContainerListener() {

					public void componentAdded(ContainerEvent e) {
						storeContainerComponentProperties(e.getContainer(),
								"OPAQUE");
					}

					public void componentRemoved(ContainerEvent containerevent) {
					}
				});
			}
		} else if (region == Region.TEXT_FIELD)
			style = new TextFieldStyle(style);
		else if (region == Region.TOGGLE_BUTTON)
			componentPropertyStore.storeComponentProperty(c, "BUTTON_MARGIN");
		else if (region == Region.TOOL_BAR) {
			componentPropertyStore.storeComponentProperty(c, "OPAQUE");
			style = new ToolBarStyle(style);
		} else if (region == Region.TOOL_BAR_SEPARATOR) {
			componentPropertyStore.storeComponentProperty(c, "OPAQUE");
			componentPropertyStore.storeComponentProperty(c,
					"TOOLBAR_SEPARATOR_SIZE");
			style = ToolBarSeparatorStyle.getStyle(style, c, region);
		} else if (region == Region.TOOL_TIP)
			componentPropertyStore.storeComponentProperty(c, "OPAQUE");
		else if (region == Region.TREE)
			componentPropertyStore.storeComponentProperty(c,
					"TREE_CELL_RENDERER");
		else if (region == Region.PANEL)
			componentPropertyStore.storeComponentProperty(c, "OPAQUE");
		else if (region == Region.POPUP_MENU) {
			componentPropertyStore.storeComponentProperty(c, "OPAQUE");
			componentPropertyStore.storeComponentProperty(c,
					"PROPERTY_CHANGE_LISTENERS");
			style = PopupMenuStyle.getStyle(style, c, region);
		} else if (region == Region.POPUP_MENU_SEPARATOR) {
			componentPropertyStore.storeComponentProperty(c, "OPAQUE");
			c.setOpaque(true);
		} else if (region == Region.RADIO_BUTTON) {
			componentPropertyStore.storeComponentProperty(c, "MOUSE_LISTENERS");
			final JRadioButton rb = (JRadioButton) c;
			c.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseEntered(MouseEvent arg0) {
					rb.putClientProperty("Synthetica.MOUSE_OVER", Boolean
							.valueOf(true));
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					rb.putClientProperty("Synthetica.MOUSE_OVER", Boolean
							.valueOf(false));
				}
			});
			style = new RadioButtonStyle(style);
		}
		return style;
	}

	private boolean isGlassPane(JPanel c) {
		return (c.getParent() instanceof JRootPane)
				&& ((JRootPane) c.getParent()).getGlassPane() == c;
	}

	public void prepareMetalLAFSwitch() {
		prepareMetalLAFSwitch = true;
	}

	void restoreAllComponentProperties() {
		componentPropertyStore.restoreAllComponentProperties();
		componentPropertyStore.cleanerThread.interrupt();
	}

	private void setOpaqueDefault4Metal(JComponent c) {
		if ((c instanceof JLabel) || (c instanceof JInternalFrame)
				|| (c instanceof JTabbedPane) || (c instanceof JSeparator)
				|| (c instanceof JMenu) || (c instanceof JSlider)
				|| (c instanceof JPanel) && isGlassPane((JPanel) c))
			c.setOpaque(false);
		else
			c.setOpaque(true);
	}

	private void storeContainerComponentProperties(Container container,
			String property) {
		if (container instanceof JComponent)
			componentPropertyStore.storeComponentProperty(container, property);
		Component components[] = container.getComponents();
		Component acomponent[] = components;
		int i = 0;
		for (int j = acomponent.length; i < j; i++) {
			Component c = acomponent[i];
			if ((c instanceof Container) && !(c instanceof Window)
					&& !(c instanceof JRootPane))
				storeContainerComponentProperties((Container) c, property);
		}

	}
}
