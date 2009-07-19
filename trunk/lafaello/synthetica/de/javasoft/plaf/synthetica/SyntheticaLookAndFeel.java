package de.javasoft.plaf.synthetica;

import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;

import sun.swing.DefaultLookup;
import sun.swing.plaf.synth.DefaultSynthStyle;
import de.javasoft.plaf.synthetica.painter.MenuPainter;
import de.javasoft.plaf.synthetica.painter.TabbedPanePainter;
import de.javasoft.plaf.synthetica.painter.TreePainter;
import de.javasoft.util.IVersion;

public abstract class SyntheticaLookAndFeel extends SynthLookAndFeel {
	private class Version implements IVersion {

		private int major;

		private int minor;

		private int revision;

		private int build;

		public Version() {
			super();
			ResourceBundle rb = getResourceBundle("SyntheticaStandardLookAndFeelVersion");
			major = Integer.parseInt(rb.getString("major"));
			minor = Integer.parseInt(rb.getString("minor"));
			revision = Integer.parseInt(rb.getString("revision"));
			build = Integer.parseInt(rb.getString("build"));
		}

		public int getBuild() {
			return build;
		}

		public int getMajor() {
			return major;
		}

		public int getMinor() {
			return minor;
		}

		public int getRevision() {
			return revision;
		}

		public String toString() {
			return (new StringBuilder(String.valueOf(major))).append(".")
					.append(minor).append(".").append(revision).append(
							" Build ").append(build).toString();
		}
	}

	private static String fontName;

	private static int fontSize;

	private static boolean antiAliasEnabled;

	private static Dimension toolbarSeparatorDimension;

	private static boolean decorated = true;

	private static boolean extendedFileChooserEnabled = true;

	private static boolean rememberFileChooserPreferences = true;

	private static boolean useSystemFileIcons = true;

	private static boolean defaultsCompatibilityMode = true;

	public static boolean getAntiAliasEnabled() {
		return antiAliasEnabled;
	}

	public static boolean getDefaultsCompatibilityMode() {
		return defaultsCompatibilityMode;
	}

	public static boolean getExtendedFileChooserEnabled() {
		return extendedFileChooserEnabled;
	}

	public static String getFontName() {
		return fontName;
	}

	public static int getFontSize() {
		return fontSize;
	}

	public static boolean getRememberFileChooserPreferences() {
		return rememberFileChooserPreferences;
	}

	public static Dimension getToolbarSeparatorDimension() {
		return toolbarSeparatorDimension;
	}

	public static boolean getUseSystemFileIcons() {
		return useSystemFileIcons;
	}

	public static void setAntiAliasEnabled(boolean value) {
		antiAliasEnabled = value;
	}

	public static void setDefaultsCompatibilityMode(boolean value) {
		defaultsCompatibilityMode = value;
	}

	public static void setExtendedFileChooserEnabled(boolean value) {
		extendedFileChooserEnabled = value;
	}

	public static void setFont(String name, int size) {
		fontName = name;
		fontSize = size;
	}

	public static void setRememberFileChooserPreferences(boolean value) {
		rememberFileChooserPreferences = value;
	}

	public static void setToolbarSeparatorDimension(Dimension dim) {
		toolbarSeparatorDimension = dim;
	}

	public static void setUseSystemFileIcons(boolean value) {
		useSystemFileIcons = value;
	}

	public static void setWindowsDecorated(boolean decorated) {
		decorated = decorated;
	}

	private PropertyChangeListener lafChangeListener;

	private UIDefaults orgDefaults;

	private static boolean debug = System.getProperty("synthetica.debug") != null;

	private static final boolean NOSTYLE = false;

	public SyntheticaLookAndFeel(String fileName) throws ParseException {
		long start = System.currentTimeMillis();
		Class clazz = SyntheticaLookAndFeel.class;
		load(clazz.getResourceAsStream(fileName), clazz);
		try {
			String syntheticaFileName = "Synthetica.xml";
			load(clazz.getResourceAsStream((new StringBuilder("/")).append(
					syntheticaFileName).toString()), clazz);
			if (debug)
				System.out.println((new StringBuilder("[Info] Found '"))
						.append(syntheticaFileName).append(
								"' configuration file.").toString());
		} catch (IllegalArgumentException illegalargumentexception) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		String className = getClass().getName();
		try {
			String syntheticaFileName = (new StringBuilder(String
					.valueOf(className
							.substring(className.lastIndexOf(".") + 1))))
					.append(".xml").toString();
			load(clazz.getResourceAsStream((new StringBuilder("/")).append(
					syntheticaFileName).toString()), clazz);
			if (debug)
				System.out.println((new StringBuilder("[Info] Found '"))
						.append(syntheticaFileName).append(
								"' configuration file.").toString());
		} catch (IllegalArgumentException illegalargumentexception1) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		long stop = System.currentTimeMillis();
		if (System.getProperty("synthetica.loadTime") != null)
			System.out.println((new StringBuilder("Time for loading LAF: "))
					.append(stop - start).append("ms").toString());
		if (System.getProperty("synthetica.blockLAFChange") != null) {
			System.out.println("LAF switchings will be blocked!");
			blockLAFChange();
		}
		if (debug)
			System.out.println("Synthetica debug mode is enabled!");
	}

	private void addResourceBundleToDefaults(String name, UIDefaults defaults) {
		ResourceBundle resBundle = getResourceBundle(name);
		String key;
		String value;
		for (Enumeration enumeration = resBundle.getKeys(); enumeration
				.hasMoreElements(); defaults.put(key, value)) {
			key = (String) (String) enumeration.nextElement();
			value = resBundle.getString(key);
		}

	}

	private void blockLAFChange() {
		UIManager.addPropertyChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				Object newLAF = event.getNewValue();
				if (!(newLAF instanceof SyntheticaLookAndFeel))
					try {
						UIManager.setLookAndFeel(SyntheticaLookAndFeel.this);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		});
	}

	public UIDefaults getDefaults() {
		UIDefaults defaults = super.getDefaults();
		addResourceBundleToDefaults("synthetica", defaults);
		return defaults;
	}

	public String getDescription() {
		return "Synthetica - the extended Synth Look and Feel.";
	}

	public abstract String getID();

	public abstract String getName();

	private ResourceBundle getResourceBundle(String name) {
		return ResourceBundle.getBundle((new StringBuilder(
				"de/javasoft/plaf/synthetica/resourceBundles/")).append(name)
				.toString());
	}

	public boolean getSupportsWindowDecorations() {
		return true;
	}

	public IVersion getSyntheticaVersion() {
		return new Version();
	}

	public IVersion getVersion() {
		ResourceBundle rb = getResourceBundle((new StringBuilder(String
				.valueOf(getID()))).append("Version").toString());
		final int major = Integer.parseInt(rb.getString("major"));
		final int minor = Integer.parseInt(rb.getString("minor"));
		final int revision = Integer.parseInt(rb.getString("revision"));
		final int build = Integer.parseInt(rb.getString("build"));
		return new IVersion() {

			public int getBuild() {
				return build;
			}

			public int getMajor() {
				return major;
			}

			public int getMinor() {
				return minor;
			}

			public int getRevision() {
				return revision;
			}

			public String toString() {
				return (new StringBuilder(String.valueOf(major))).append(".")
						.append(minor).append(".").append(revision).append(
								" Build ").append(build).toString();
			}
		};
	}

	public void initialize() {
		super.initialize();
		orgDefaults = (UIDefaults) UIManager.getDefaults().clone();
		DefaultLookup.setDefaultLookup(new SyntheticaDefaultLookup());
		StyleFactory styleFactory = new StyleFactory(getStyleFactory());
		SynthLookAndFeel.setStyleFactory(styleFactory);
		PopupFactory.install();
		lafChangeListener = new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				reinit();
				installSyntheticaDefaults();
				if (SyntheticaLookAndFeel.defaultsCompatibilityMode)
					installCompatibilityDefaults();
			}
		};
		UIManager.addPropertyChangeListener(lafChangeListener);
	}

	private void installCompatibilityDefaults() {
		UIDefaults defaults = UIManager.getDefaults();
		initSystemColorDefaults(UIManager.getDefaults());
		Object uiDefaults[] = {
				"List.selectionForeground",
				new ColorUIResource(Color.white),
				"SplitPane.dividerSize",
				Integer.valueOf(8),
				"List.focusCellHighlightBorder",
				new javax.swing.plaf.BorderUIResource.LineBorderUIResource(
						defaults
								.getColor("Synthetica.list.focusCellHighlightBorder.color")),
				"Table.focusCellHighlightBorder",
				new javax.swing.plaf.BorderUIResource.LineBorderUIResource(
						defaults
								.getColor("Synthetica.table.focusCellHighlightBorder.color")),
				"Table.scrollPaneBorder",
				new javax.swing.plaf.BorderUIResource.LineBorderUIResource(
						defaults
								.getColor("Synthetica.table.scrollPane.border.color")),
				"TitledBorder.border",
				new BorderUIResource(new SyntheticaTitledBorder()),
				"RootPane.defaultButtonWindowKeyBindings",
				new Object[] { "ENTER", "press", "released ENTER", "release",
						"ctrl ENTER", "press", "ctrl released ENTER", "release" } };
		defaults.putDefaults(uiDefaults);
		SynthStyle ss = null;
		SynthContext sc = null;
		SynthStyleFactory ssf = getStyleFactory();
		String inKeys[] = (String[]) null;
		String keys[] = (String[]) null;
		java.awt.Font font = null;
		JButton button = new JButton();
		ss = ssf.getStyle(button, Region.BUTTON);
		font = ((DefaultSynthStyle) ss).getFont(button, Region.BUTTON, 1);
		defaults.put("Button.font", font);
		JLabel label = new JLabel();
		ss = ssf.getStyle(label, Region.LABEL);
		font = ((DefaultSynthStyle) ss).getFont(label, Region.LABEL, 1);
		defaults.put("Label.font", font);
		Color labelForeground = ((DefaultSynthStyle) ss).getColor(label,
				Region.LABEL, 1024, ColorType.TEXT_FOREGROUND);
		defaults.put("Label.foreground", labelForeground);
		JPanel panel = new JPanel();
		sc = new SynthContext(panel, Region.PANEL, ss, 1024);
		Color labelBackground = ss.getColor(sc, ColorType.BACKGROUND);
		defaults.put("Label.background", labelBackground);
		JList list = new JList();
		ss = ssf.getStyle(list, Region.LIST);
		sc = new SynthContext(list, Region.LIST, ss, 1024);
		Color listBackground = ss.getColor(sc, ColorType.TEXT_BACKGROUND);
		defaults.put("List.background", listBackground);
		Color listForeground = ss.getColor(sc, ColorType.TEXT_FOREGROUND);
		defaults.put("List.foreground", listForeground);
		sc = new SynthContext(list, Region.LIST, ss, 512);
		listBackground = ss.getColor(sc, ColorType.TEXT_BACKGROUND);
		defaults.put("List.selectionBackground", listBackground);
		listForeground = ss.getColor(sc, ColorType.TEXT_FOREGROUND);
		defaults.put("List.selectionForeground", listForeground);
		JTable table = new JTable();
		ss = ssf.getStyle(table, Region.TABLE);
		sc = new SynthContext(table, Region.TABLE, ss, 1024);
		Color tableBackground = ss.getColor(sc, ColorType.BACKGROUND);
		defaults.put("Table.background", tableBackground);
		Color tableForeground = ss.getColor(sc, ColorType.FOREGROUND);
		defaults.put("Table.foreground", tableForeground);
		sc = new SynthContext(table, Region.TABLE, ss, 512);
		tableBackground = ss.getColor(sc, ColorType.TEXT_BACKGROUND);
		defaults.put("Table.selectionBackground", tableBackground);
		tableForeground = ss.getColor(sc, ColorType.TEXT_FOREGROUND);
		defaults.put("Table.selectionForeground", tableForeground);
		JTree tree = new JTree();
		ss = ssf.getStyle(tree, Region.TREE);
		font = ((DefaultSynthStyle) ss).getFont(tree, Region.TREE, 1);
		defaults.put("Tree.font", font);
		sc = new SynthContext(tree, Region.TREE, ss, 1024);
		keys = (new String[] { "Tree.expandedIcon", "Tree.collapsedIcon" });
		putIcons2Defaults(defaults, keys, keys, ss, sc);
		defaults.put("Tree.rowHeight", ss.get(sc, "Tree.rowHeight"));
		defaults
				.put("Tree.leftChildIndent", ss.get(sc, "Tree.leftChildIndent"));
		defaults.put("Tree.rightChildIndent", ss.get(sc,
				"Tree.rightChildIndent"));
		ss = ssf.getStyle(tree, Region.TREE_CELL);
		sc = new SynthContext(tree, Region.TREE_CELL, ss, 512);
		defaults.put("Tree.selectionForeground", ss.getColor(sc,
				ColorType.TEXT_FOREGROUND));
		defaults.put("Tree.selectionBackground", ss.getColor(sc,
				ColorType.TEXT_BACKGROUND));
		JInternalFrame iFrame = new JInternalFrame();
		ss = ssf.getStyle(iFrame, Region.INTERNAL_FRAME_TITLE_PANE);
		Color iFrameForeground = ((DefaultSynthStyle) ss).getColor(iFrame,
				Region.INTERNAL_FRAME_TITLE_PANE, 512, ColorType.FOREGROUND);
		defaults.put("InternalFrame.activeTitleForeground", iFrameForeground);
		iFrameForeground = ((DefaultSynthStyle) ss).getColor(iFrame,
				Region.INTERNAL_FRAME_TITLE_PANE, 1024, ColorType.FOREGROUND);
		defaults.put("InternalFrame.inactiveTitleForeground", iFrameForeground);
		Color iFrameBackground = ((DefaultSynthStyle) ss).getColor(iFrame,
				Region.INTERNAL_FRAME_TITLE_PANE, 512, ColorType.BACKGROUND);
		defaults.put("InternalFrame.activeTitleBackground", iFrameBackground);
		sc = new SynthContext(iFrame, Region.INTERNAL_FRAME_TITLE_PANE, ss,
				1024);
		inKeys = (new String[] { "InternalFrameTitlePane.closeIcon",
				"InternalFrameTitlePane.maximizeIcon",
				"InternalFrameTitlePane.minimizeIcon",
				"InternalFrameTitlePane.iconifyIcon" });
		keys = (new String[] { "InternalFrame.closeIcon",
				"InternalFrame.maximizeIcon", "InternalFrame.minimizeIcon",
				"InternalFrame.iconifyIcon" });
		putIcons2Defaults(defaults, inKeys, keys, ss, sc);
		ss = ssf.getStyle(iFrame, Region.INTERNAL_FRAME);
		sc = new SynthContext(iFrame, Region.INTERNAL_FRAME, ss, 1024);
		keys = (new String[] { "InternalFrame.icon" });
		putIcons2Defaults(defaults, keys, keys, ss, sc);
		JOptionPane oPane = new JOptionPane();
		ss = ssf.getStyle(oPane, Region.OPTION_PANE);
		sc = new SynthContext(oPane, Region.OPTION_PANE, ss, 1024);
		keys = (new String[] { "OptionPane.informationIcon",
				"OptionPane.questionIcon", "OptionPane.warningIcon",
				"OptionPane.errorIcon" });
		putIcons2Defaults(defaults, keys, keys, ss, sc);
		JCheckBox checkBox = new JCheckBox();
		ss = ssf.getStyle(checkBox, Region.CHECK_BOX);
		sc = new SynthContext(checkBox, Region.CHECK_BOX, ss, 1024);
		keys = (new String[] { "CheckBox.icon" });
		putIcons2Defaults(defaults, keys, keys, ss, sc);
		JRadioButton radioButton = new JRadioButton();
		ss = ssf.getStyle(radioButton, Region.RADIO_BUTTON);
		sc = new SynthContext(radioButton, Region.RADIO_BUTTON, ss, 1024);
		keys = (new String[] { "RadioButton.icon" });
		putIcons2Defaults(defaults, keys, keys, ss, sc);
		JTabbedPane tPane = new JTabbedPane();
		ss = ssf.getStyle(tPane, Region.TABBED_PANE_TAB_AREA);
		sc = new SynthContext(tPane, Region.TABBED_PANE_TAB_AREA, ss, 1024);
		defaults.put("TabbedPane.tabAreaInsets", ss.getInsets(sc, null));
		ss = ssf.getStyle(tPane, Region.TABBED_PANE_TAB);
		sc = new SynthContext(tPane, Region.TABBED_PANE_TAB, ss, 1024);
		defaults.put("TabbedPane.tabInsets", ss.getInsets(sc, null));
		ss = ssf.getStyle(tPane, Region.TABBED_PANE_TAB);
		sc = new SynthContext(tPane, Region.TABBED_PANE_TAB, ss, 512);
		defaults.put("TabbedPane.selectedTabPadInsets", ss.getInsets(sc, null));
		ss = ssf.getStyle(tPane, Region.TABBED_PANE_CONTENT);
		sc = new SynthContext(tPane, Region.TABBED_PANE_CONTENT, ss, 1024);
		defaults.put("TabbedPane.contentBorderInsets", ss.getInsets(sc, null));
		defaults.put("TabbedPane.shadow", Color.GRAY);
	}

	private void installSyntheticaDefaults() {
		UIDefaults defaults = UIManager.getDefaults();
		if (UIManager.getBoolean("Synthetica.window.decoration"))
			defaults.put("RootPaneUI",
					"de.javasoft.plaf.synthetica.SyntheticaRootPaneUI");
		else
			decorated = false;
		JFrame.setDefaultLookAndFeelDecorated(decorated);
		JDialog.setDefaultLookAndFeelDecorated(decorated);
		extendedFileChooserEnabled = UIManager
				.getBoolean("Synthetica.extendedFileChooser.enabled");
		if (extendedFileChooserEnabled)
			defaults
					.put("FileChooserUI",
							"de.javasoft.plaf.synthetica.filechooser.SyntheticaFileChooserUI");
		else
			defaults.put("FileChooserUI",
					"javax.swing.plaf.metal.MetalFileChooserUI");
		rememberFileChooserPreferences = UIManager
				.getBoolean("Synthetica.extendedFileChooser.rememberPreferences");
		useSystemFileIcons = UIManager
				.getBoolean("Synthetica.extendedFileChooser.useSystemFileIcons");
		defaults.put("DropDownButtonUI",
				"de.javasoft.plaf.synthetica.DropDownButtonUI");
	}

	private void putIcons2Defaults(UIDefaults defaults, String inKeys[],
			String keys[], SynthStyle ss, SynthContext sc) {
		for (int i = 0; i < inKeys.length; i++) {
			javax.swing.Icon icon = ss.getIcon(sc, inKeys[i]);
			defaults.put(keys[i], icon);
		}

	}

	private void reinit() {
		((MenuPainter) MenuPainter.getInstance()).reinitialize();
		((TreePainter) TreePainter.getInstance()).reinitialize();
		((TabbedPanePainter) TabbedPanePainter.getInstance()).reinitialize();
	}

	public void uninitialize() {
		((StyleFactory) SynthLookAndFeel.getStyleFactory())
				.restoreAllComponentProperties();
		UIDefaults defaults = UIManager.getDefaults();
		defaults.clear();
		java.util.Map.Entry es;
		for (Iterator iterator = orgDefaults.entrySet().iterator(); iterator
				.hasNext(); defaults.put(es.getKey(), es.getValue()))
			es = (java.util.Map.Entry) iterator.next();

		UIManager.removePropertyChangeListener(lafChangeListener);
		super.uninitialize();
	}
}
