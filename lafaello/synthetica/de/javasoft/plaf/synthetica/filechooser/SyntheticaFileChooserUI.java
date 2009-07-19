package de.javasoft.plaf.synthetica.filechooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.security.AccessControlException;
import java.security.AccessController;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import sun.awt.shell.ShellFolder;
import sun.security.action.GetPropertyAction;
import sun.swing.plaf.synth.DefaultSynthStyle;
import de.javasoft.io.FileProperties;
import de.javasoft.io.FileUtils;
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
public class SyntheticaFileChooserUI extends BasicFileChooserUI implements
		ActionListener {
	private class DirectoryComboBoxModel extends AbstractListModel implements
			ComboBoxModel {

		private ArrayList directories;

		private ArrayList depths;

		private File selectedDirectory;

		public DirectoryComboBoxModel() {
			super();
			directories = new ArrayList();
			depths = new ArrayList();
			selectedDirectory = null;
			File dir = getFileChooser().getCurrentDirectory();
			if (dir != null)
				addItem(dir);
		}

		private void addItem(File directory) {
			if (directory == null)
				return;
			directories.clear();
			depths.clear();
			File baseFolders[] = (File[]) ShellFolder
					.get("fileChooserComboBoxFolders");
			directories.addAll((Collection) Arrays.asList(baseFolders));
			try {
				directory = directory.getCanonicalFile();
			} catch (IOException ioexception) {
			}
			try {
				directory = ShellFolder.getShellFolder(directory);
			} catch (IOException ioexception1) {
			}
			File f = directory;
			ArrayList path = new ArrayList();
			do
				path.add(f);
			while ((f = f.getParentFile()) != null);
			ArrayList dirList = new ArrayList();
			for (int i = 0; i < path.size(); i++) {
				f = (File) path.get(i);
				if (!directories.contains(f))
					continue;
				for (Iterator it = directories.iterator(); it.hasNext();) {
					File dir = (File) (File) it.next();
					if (dir.equals(f)) {
						for (int j = i; j >= 0; j--) {
							dirList.add((File) path.get(j));
							depths.add(new Integer(path.size() - 1 - j));
						}

					} else {
						dirList.add(dir);
						File parent = dir;
						int parents;
						for (parents = 0; (parent = parent.getParentFile()) != null; parents++)
							;
						depths.add(new Integer(parents));
					}
				}

				break;
			}

			directories = dirList;
			setSelectedItem(directory);
		}

		public int getDepth(int index) {
			return index < 0 || index >= depths.size() ? 0 : ((Integer) depths
					.get(index)).intValue();
		}

		public Object getElementAt(int index) {
			return directories.get(index);
		}

		public Object getSelectedItem() {
			return selectedDirectory;
		}

		public int getSize() {
			return directories.size();
		}

		public void setSelectedItem(Object selectedDirectory) {
			this.selectedDirectory = (File) selectedDirectory;
			fireContentsChanged(this, -1, -1);
		}
	}

	private class FilePane extends JPanel implements PropertyChangeListener {
		private class DetailsTableCellRenderer extends DefaultTableCellRenderer {

			private DateFormat dateFormat;

			DetailsTableCellRenderer(JFileChooser fc) {
				super();
				dateFormat = DateFormat.getDateTimeInstance(3, 3, fc
						.getLocale());
			}

			public Insets getInsets(Insets i) {
				i = super.getInsets(i);
				i.left += 4;
				i.right += 4;
				return i;
			}

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				if (column == table.convertColumnIndexToView(sortColumn))
					setBackground(UIManager
							.getColor("Synthetica.fileChooser.tableView.sortColumnColor"));
				else
					setBackground(Color.WHITE);
				return super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);
			}

			public void setBounds(int x, int y, int width, int height) {
				super.setBounds(x, y, width, height);
			}

			public void setValue(Object value) {
				setIcon(null);
				setHorizontalAlignment(2);
				if (value instanceof File) {
					File file = (File) value;
					String fileName = getFileChooser().getName(file);
					setText(fileName);
					Icon icon = null;
					if (useSystemFileIcons) {
						FileSystemView fsv = FileSystemView.getFileSystemView();
						icon = fsv.getSystemIcon(file);
					} else {
						icon = getFileChooser().getIcon(file);
					}
					setIcon(icon);
					if (cutBuffer.contains(value))
						setIcon(createBrightIcon(icon));
				} else if (value instanceof Date)
					setText(value != null ? dateFormat.format((Date) value)
							: "");
				else if (value instanceof Long) {
					long size = ((Long) value).longValue();
					if (size == -1L) {
						setText("");
						return;
					}
					size /= 1024L;
					String len = "0";
					if (size < 1024L) {
						if (size == 0L)
							size = 1L;
						NumberFormat nf = NumberFormat
								.getInstance(getFileChooser().getLocale());
						len = (new StringBuilder(String
								.valueOf(nf.format(size)))).append(" KB")
								.toString();
					} else {
						size /= 1024L;
						NumberFormat nf = NumberFormat
								.getInstance(getFileChooser().getLocale());
						len = (new StringBuilder(String
								.valueOf(nf.format(size)))).append(" MB")
								.toString();
					}
					setHorizontalAlignment(4);
					setText(len);
				} else {
					super.setValue(value);
				}
			}
		}

		private class DetailsTableModel extends AbstractTableModel {
			private class _cls1 implements ListDataListener {

				public void contentsChanged(ListDataEvent e) {
					fireTableDataChanged();
				}

				public void intervalAdded(ListDataEvent e) {
					newAddedEntry = (File) listModel
							.getElementAt(e.getIndex0());
					fireTableDataChanged();
				}

				public void intervalRemoved(ListDataEvent e) {
					fireTableDataChanged();
				}

			}

			JFileChooser fileChooser;

			File newAddedEntry;

			ListModel listModel;

			DetailsTableModel(JFileChooser fc) {
				super();
				fileChooser = fc;
				listModel = getModel();
				getModel().addListDataListener(new _cls1());
			}

			public Class getColumnClass(int column) {
				switch (column) {
				case 0: // '\0'
					return File.class;

				case 3: // '\003'
					return Date.class;

				case 1: // '\001'
					return Long.class;

				case 2: // '\002'
				default:
					return super.getColumnClass(column);
				}
			}

			public int getColumnCount() {
				return 5;
			}

			public String getColumnName(int column) {
				return columnNames[column];
			}

			public int getRowCount() {
				return listModel.getSize();
			}

			public Object getValueAt(int row, int col) {
				File f = (File) listModel.getElementAt(row);
				switch (col) {
				case 0: // '\0'
					return f;

				case 1: // '\001'
					if (!f.exists() || f.isDirectory())
						return new Long(-1L);
					else
						return new Long(f.length());

				case 2: // '\002'
					if (!f.exists())
						return null;
					else
						return fileChooser.getFileSystemView()
								.getSystemTypeDescription(f);

				case 3: // '\003'
					if (!f.exists()
							|| fileChooser.getFileSystemView()
									.isFileSystemRoot(f)) {
						return null;
					} else {
						long time = f.lastModified();
						return time != 0L ? new Date(time) : null;
					}

				case 4: // '\004'
					if (!f.exists()
							|| fileChooser.getFileSystemView()
									.isFileSystemRoot(f))
						return null;
					String attributes = "";
					try {
						if (!f.canWrite())
							attributes = (new StringBuilder(String
									.valueOf(attributes))).append("R")
									.toString();
					} catch (AccessControlException accesscontrolexception) {
					}
					if (f.isHidden())
						attributes = (new StringBuilder(String
								.valueOf(attributes))).append("H").toString();
					return attributes;
				}
				return null;
			}

			public boolean isCellEditable(int row, int column) {
				return column == 0 && editFile != null;
			}

			public void setValueAt(Object obj, int i, int j) {
			}
		}

		private class DetailsTableSortModel extends AbstractTableModel
				implements TableModelListener {
			private class Row implements Comparable {

				public int index;

				Row() {
					super();
				}

				public int compareTo(Object obj) {
					File f1 = (File) model.getValueAt(index, 0);
					File f2 = (File) model.getValueAt(((Row) obj).index, 0);
					if (f1 == null || f2 == null)
						return 0;
					if (f1.isDirectory() && f2.isFile())
						return -1 * sortOrder;
					if (f1.isFile() && f2.isDirectory())
						return 1 * sortOrder;
					if (sortColumn == 0) {
						int i = f1.getName().toLowerCase().compareTo(
								f2.getName().toLowerCase());
						return i * sortOrder;
					}
					if (sortColumn == 3) {
						long i = f1.lastModified() - f2.lastModified();
						return (i >= 0L ? i <= 0L ? 0 : -1 : 1) * sortOrder;
					}
					if (sortColumn == 1) {
						long i = f1.length() - f2.length();
						return (i >= 0L ? i <= 0L ? 0 : -1 : 1) * sortOrder;
					}
					if (sortColumn == 2) {
						String s1 = getFileChooser().getFileSystemView()
								.getSystemTypeDescription(f1);
						String s2 = getFileChooser().getFileSystemView()
								.getSystemTypeDescription(f2);
						return s1.compareTo(s2) * sortOrder;
					}
					if (sortColumn == 4) {
						String attr1 = "";
						try {
							if (!f1.canWrite())
								attr1 = (new StringBuilder(String
										.valueOf(attr1))).append("R")
										.toString();
						} catch (AccessControlException accesscontrolexception) {
						}
						if (f1.isHidden())
							attr1 = (new StringBuilder(String.valueOf(attr1)))
									.append("H").toString();
						String attr2 = "";
						try {
							if (!f2.canWrite())
								attr2 = (new StringBuilder(String
										.valueOf(attr2))).append("R")
										.toString();
						} catch (AccessControlException accesscontrolexception1) {
						}
						if (f2.isHidden())
							attr2 = (new StringBuilder(String.valueOf(attr2)))
									.append("H").toString();
						return attr1.compareTo(attr2) * sortOrder;
					} else {
						return f1.toString().compareTo(f2.toString())
								* sortOrder;
					}
				}
			}

			TableModel model;

			Row rows[];

			DetailsTableSortModel(TableModel model) {
				super();
				this.model = model;
				model.addTableModelListener(this);
				reinit();
			}

			public Class getColumnClass(int column) {
				return model.getColumnClass(column);
			}

			public int getColumnCount() {
				return model.getColumnCount();
			}

			public String getColumnName(int column) {
				return model.getColumnName(column);
			}

			public int getRowCount() {
				return model.getRowCount();
			}

			public Object getValueAt(int row, int col) {
				if (table != null && rows.length == 0)
					return null;
				else
					return model.getValueAt(rows[row].index, col);
			}

			public boolean isCellEditable(int row, int column) {
				return model.isCellEditable(row, column);
			}

			public void reinit() {
				rows = new Row[model.getRowCount()];
				for (int i = 0; i < rows.length; i++) {
					rows[i] = new Row();
					rows[i].index = i;
				}

				sort();
			}

			public void setValueAt(Object value, int row, int col) {
				model.setValueAt(value, row, col);
			}

			public void sort() {
				Cursor cursor = table.getTableHeader().getCursor();
				table.getTableHeader().setCursor(null);
				getFileChooser().setCursor(Cursor.getPredefinedCursor(3));
				Arrays.sort(rows);
				fireTableDataChanged();
				getFileChooser().setCursor(Cursor.getPredefinedCursor(0));
				table.getTableHeader().setCursor(cursor);
			}

			public void tableChanged(TableModelEvent e) {
				reinit();
				File file = ((DetailsTableModel) model).newAddedEntry;
				if (file != null && newCreatedFile) {
					for (int i = 0; i < getRowCount(); i++) {
						if (!file.equals(getValueAt(i, 0)))
							continue;
						filePane.editIndex = i;
						filePane.editFileName();
						break;
					}

					((DetailsTableModel) model).newAddedEntry = null;
					newCreatedFile = false;
				}
			}
		}

		private class FilePaneMouseListener implements MouseListener {

			private class _cls2 extends Thread {

				private class LocalThread implements Runnable {

					public void run() {
						editFileName();
					}
				}

				public void run() {
					try {
						Thread.sleep(250L);
					} catch (InterruptedException interruptedexception) {
					}
					if (singleClick)
						SwingUtilities.invokeLater(new LocalThread());
				}

			}

			private MouseListener doubleClickListener;

			private boolean singleClick;

			public FilePaneMouseListener(JList list) {
				super();
				doubleClickListener = createDoubleClickListener(
						getFileChooser(), list);
			}

			public FilePaneMouseListener(JTable table) {
				super();
				doubleClickListener = createDoubleClickListener(
						getFileChooser(), list);
			}

			private int loc2IndexFileList(JList list, Point point) {
				int index = list.locationToIndex(point);
				if (index != -1 && !pointIsInActualBounds(list, index, point))
					index = -1;
				return index;
			}

			public void mouseClicked(MouseEvent evt) {
				JComponent source = (JComponent) evt.getSource();
				int index;
				if (source instanceof JList)
					index = loc2IndexFileList((JList) source, evt.getPoint());
				else if (source instanceof JTable)
					index = ((JTable) source).rowAtPoint(evt.getPoint());
				else if (source instanceof JTableHeader) {
					int column = ((JTableHeader) source).columnAtPoint(evt
							.getPoint());
					column = table.convertColumnIndexToModel(column);
					if (column == sortColumn) {
						if (sortOrder == 1)
							sortOrder = -1;
						else
							sortOrder = 1;
					} else {
						sortOrder = 1;
						sortColumn = column;
					}
					detailsTableSortModel.sort();
					viewPanel.repaint();
					return;
				} else {
					return;
				}
				if (index >= 0
						&& list.getSelectionModel().isSelectedIndex(index)
						&& (source instanceof JTable)) {
					Rectangle rect = list.getCellBounds(index, index);
					evt = new MouseEvent(list, evt.getID(), evt.getWhen(), evt
							.getModifiers(), rect.x, rect.y, evt
							.getClickCount(), evt.isPopupTrigger(), evt
							.getButton());
				}
				if (index >= 0 && SwingUtilities.isLeftMouseButton(evt))
					if (evt.getClickCount() == 1) {
						singleClick = true;
						if (editIndex == index && editFile == null)
							(new _cls2()).start();
						else if (editFile == null)
							editIndex = index;
					} else if (evt.getClickCount() != 1) {
						singleClick = false;
						cancelEditFileName();
					}
				doubleClickListener.mouseClicked(evt);
			}

			public void mouseEntered(MouseEvent evt) {
				doubleClickListener.mouseEntered(evt);
			}

			public void mouseExited(MouseEvent evt) {
				if (evt.getSource() instanceof JList)
					doubleClickListener.mouseExited(evt);
			}

			public void mousePressed(MouseEvent evt) {
				if (evt.getSource() instanceof JList)
					doubleClickListener.mousePressed(evt);
			}

			public void mouseReleased(MouseEvent evt) {
				if (evt.getSource() instanceof JList)
					doubleClickListener.mouseReleased(evt);
			}

			private boolean pointIsInActualBounds(JList list, int index,
					Point point) {
				ListCellRenderer renderer = list.getCellRenderer();
				ListModel dataModel = list.getModel();
				Object value = dataModel.getElementAt(index);
				Component item = renderer.getListCellRendererComponent(list,
						value, index, false, false);
				Dimension itemSize = item.getPreferredSize();
				Rectangle cellBounds = list.getCellBounds(index, index);
				if (!item.getComponentOrientation().isLeftToRight())
					cellBounds.x += cellBounds.width - itemSize.width;
				cellBounds.width = itemSize.width;
				cellBounds.height = itemSize.height;
				return cellBounds.contains(point);
			}

		}

		private class ListRenderer extends DefaultListCellRenderer {

			ListRenderer() {
				super();
			}

			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);
				File file = (File) value;
				String fileName = getFileChooser().getName(file);
				setText(fileName);
				Icon icon = null;
				if (useSystemFileIcons) {
					FileSystemView fsv = FileSystemView.getFileSystemView();
					icon = fsv.getSystemIcon(file);
				} else {
					icon = getFileChooser().getIcon(file);
				}
				setIcon(icon);
				if (cutBuffer.contains(value))
					setIcon(createBrightIcon(icon));
				return this;
			}
		}

		public class TableHeaderRenderer implements TableCellRenderer {

			private final String ARROW_UP = UIManager
					.getString("Synthetica.fileChooser.tableView.arrowUp");

			private final String ARROW_DOWN = UIManager
					.getString("Synthetica.fileChooser.tableView.arrowDown");
			private final Color gridColor = UIManager
					.getColor("Synthetica.tableHeader.gridColor");
			TableCellRenderer renderer;

			public TableHeaderRenderer(JTable table) {
				super();
				renderer = table.getTableHeader().getDefaultRenderer();
			}

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JPanel panel = new JPanel(new BorderLayout());
				JLabel textLabel = new JLabel();
				textLabel.setText(value != null ? value.toString() : "");
				textLabel.setBorder(new EmptyBorder(1, 4, 1, 4));
				textLabel.setFont(table.getTableHeader().getFont());
				textLabel.setForeground(table.getTableHeader().getForeground());
				if (fileSizeHeaderText.equals(value))
					textLabel.setHorizontalAlignment(4);
				JPanel iconPanel = new JPanel(new BorderLayout());
				iconPanel.setOpaque(false);
				JLabel iconLabel = new JLabel();
				String arrowPath = ARROW_UP;
				if (sortOrder == -1)
					arrowPath = ARROW_DOWN;
				ImageIcon icon = new ImageIcon(SyntheticaLookAndFeel.class
						.getResource(arrowPath));
				iconLabel.setIcon(icon);
				if (column == table.convertColumnIndexToView(sortColumn))
					iconPanel.add(iconLabel);
				JPanel separator = new JPanel();
				separator.setBackground(gridColor);
				separator.setPreferredSize(new Dimension(1, separator
						.getPreferredSize().height));
				if (column != table.getColumnCount() - 1)
					iconPanel.add(separator, "East");
				panel.add(textLabel);
				panel.add(iconPanel, "East");
				panel.setOpaque(false);
				return panel;
			}
		}

		private int COLUMN_WIDTHS[] = { 175, 60, 100, 100, 30 };

		private int sortColumn;

		private int sortOrder;

		private int view;

		private JPanel viewPanel;

		private JPanel listPanel;

		private JPanel tablePanel;

		private JList list;

		private JTable table;

		private DetailsTableSortModel detailsTableSortModel;

		private String fileSizeHeaderText;

		private File editFile;
		private JTextField editTextField;
		private boolean newCreatedFile;
		private int editX;
		private int editIndex;
		String columnNames[];

		public FilePane() {
			super(new BorderLayout());
			sortColumn = 0;
			sortOrder = 1;
			view = -1;
			editX = 20;
			editIndex = -1;
			editTextField = new JTextField();
			editTextField.addFocusListener(new FocusListener() {

				public void focusGained(FocusEvent focusevent) {
				}

				public void focusLost(FocusEvent e) {
					applyEditFileName();
				}
			});
			editTextField.addKeyListener(new KeyListener() {

				public void keyPressed(KeyEvent keyevent) {
				}

				public void keyReleased(KeyEvent keyevent) {
				}

				public void keyTyped(KeyEvent e) {
					if (e.getKeyChar() == '\033')
						cancelEditFileName();
					else if (e.getKeyChar() == '\n')
						applyEditFileName();
				}
			});
			listPanel = createListPanel();
			tablePanel = createTablePanel();
			viewPanel = new JPanel(new BorderLayout());
			add(viewPanel);
			getFileChooser().registerKeyboardAction(
					SyntheticaFileChooserUI.this, "deleteAction",
					KeyStroke.getKeyStroke(127, 0), 1);
		}

		private void applyEditFileName() {
			if (editFile == null)
				return;
			JFileChooser fc = getFileChooser();
			String displayName = fc.getName(editFile);
			String fileName = editFile.getName();
			String newDisplayName = editTextField.getText().trim();
			String newFileName = newDisplayName;
			if (!newDisplayName.equals(displayName)) {
				int l1 = fileName.length();
				int l2 = displayName.length();
				if (l1 > l2 && fileName.charAt(l2) == '.')
					newFileName = (new StringBuilder(String
							.valueOf(newDisplayName))).append(
							fileName.substring(l2)).toString();
				FileSystemView fsv = fc.getFileSystemView();
				File newFile = fsv.createFileObject(editFile.getParentFile(),
						newFileName);
				if (!newFile.exists() && editFile.renameTo(newFile)) {
					final Object selected;
					if (editFile.equals(list.getSelectedValue()))
						selected = newFile;
					else
						selected = list.getSelectedValue();
					getModel().validateFileCache();
					(new Thread() {

						class LocalThread implements Runnable {

							public void run() {
								list.setSelectedValue(selected, true);
							}
						}

						public void run() {
							try {
								Thread.sleep(350L);
							} catch (InterruptedException interruptedexception) {
							}
							SwingUtilities.invokeLater(new LocalThread());
						}

					}).start();
				}
			}
			cancelEditFileName();
		}

		private void cancelEditFileName() {
			editIndex = -1;
			if (editFile == null)
				return;
			editFile = null;
			if (view == 0)
				list.remove(editTextField);
			else
				table.editingCanceled(new ChangeEvent(table));
			repaint();
		}

		private ImageIcon createBrightIcon(Icon icon) {
			BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon
					.getIconHeight(), 2);
			icon.paintIcon(null, bi.getGraphics(), 0, 0);
			ImageIcon ii = new ImageIcon(bi);
			ii = new ImageIcon(createImage(new FilteredImageSource(bi
					.getSource(), new RGBImageFilter() {

				public int filterRGB(int x, int y, int rgb) {
					return 0x60ffffff & rgb;
				}
			})));
			return ii;
		}

		public JPanel createListPanel() {
			JPanel p = new JPanel(new BorderLayout());
			JFileChooser fileChooser = getFileChooser();
			list = new JList();
			list.setCellRenderer(new ListRenderer());
			list.setLayoutOrientation(1);
			list.setVisibleRowCount(-1);
			list.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {

						public void valueChanged(ListSelectionEvent evt) {
							if (((ListSelectionModel) evt.getSource())
									.isSelectionEmpty())
								editIndex = -1;
						}
					});
			list.setModel(new ListModel() {

				public void addListDataListener(ListDataListener l) {
					getModel().addListDataListener(l);
				}

				public Object getElementAt(int index) {
					return detailsTableSortModel.getValueAt(index, 0);
				}

				public int getSize() {
					return detailsTableSortModel.getRowCount();
				}

				public void removeListDataListener(ListDataListener l) {
					getModel().removeListDataListener(l);
				}
			});
			list.addMouseListener(new FilePaneMouseListener(list));
			list.addListSelectionListener(((BasicFileChooserUI) fileChooser
					.getUI()).createListSelectionListener(fileChooser));
			list.setSelectionMode(0);
			list.addKeyListener(new FileSelectHandler());
			JScrollPane scrollPane = new JScrollPane(list);
			p.setPreferredSize(new Dimension(400, 205));
			p.add(scrollPane, "Center");
			return p;
		}

		public JPanel createTablePanel() {
			JPanel p = new JPanel(new BorderLayout());
			JFileChooser fileChooser = getFileChooser();
			java.util.Locale locale = fileChooser.getLocale();
			String fileNameHeaderText = UIManager.getString(
					"FileChooser.fileNameHeaderText", locale);
			fileSizeHeaderText = UIManager.getString(
					"FileChooser.fileSizeHeaderText", locale);
			String fileTypeHeaderText = UIManager.getString(
					"FileChooser.fileTypeHeaderText", locale);
			String fileDateHeaderText = UIManager.getString(
					"FileChooser.fileDateHeaderText", locale);
			String fileAttrHeaderText = UIManager.getString(
					"FileChooser.fileAttrHeaderText", locale);
			columnNames = (new String[] { fileNameHeaderText,
					fileSizeHeaderText, fileTypeHeaderText, fileDateHeaderText,
					fileAttrHeaderText });
			table = new JTable();
			detailsTableSortModel = new DetailsTableSortModel(
					new DetailsTableModel(fileChooser));
			table.setSelectionModel(list.getSelectionModel());
			table.setModel(detailsTableSortModel);
			table
					.setComponentOrientation(fileChooser
							.getComponentOrientation());
			table.setShowGrid(false);
			table.setIntercellSpacing(new Dimension(0, 0));
			int rowHeight = table.getRowHeight();
			Icon icon = null;
			FileSystemView fsv = FileSystemView.getFileSystemView();
			icon = fsv.getSystemIcon(fsv.getHomeDirectory());
			if (rowHeight <= icon.getIconHeight())
				table.setRowHeight(icon.getIconHeight() + 1);
			table.getTableHeader().setDefaultRenderer(
					new TableHeaderRenderer(table));
			table.getTableHeader().addMouseListener(
					new FilePaneMouseListener(table));
			TableColumnModel columnModel = table.getColumnModel();
			TableColumn columns[] = new TableColumn[5];
			for (int i = 0; i < 5; i++) {
				columns[i] = columnModel.getColumn(i);
				columns[i].setPreferredWidth(COLUMN_WIDTHS[i]);
			}

			String osName = (String) (String) AccessController
					.doPrivileged(new GetPropertyAction("os.name"));
			if (osName == null || !osName.startsWith("Windows")) {
				columnModel.removeColumn(columns[2]);
				columnModel.removeColumn(columns[4]);
			}
			TableCellRenderer cellRenderer = new DetailsTableCellRenderer(
					fileChooser);
			table.setDefaultRenderer(File.class, cellRenderer);
			table.setDefaultRenderer(Date.class, cellRenderer);
			table.setDefaultRenderer(Object.class, cellRenderer);
			columns[0].setCellEditor(new DefaultCellEditor(editTextField));
			table.getActionMap().put("NO_ACTION_KEY", new AbstractAction() {

				public void actionPerformed(ActionEvent actionevent) {
				}
			});
			table.getInputMap(1).put(KeyStroke.getKeyStroke(27, 0),
					"NO_ACTION_KEY");
			table.addMouseListener(new FilePaneMouseListener(table));
			table.addKeyListener(new FileSelectHandler());
			JScrollPane scrollpane = new JScrollPane(table);
			scrollpane.setComponentOrientation(fileChooser
					.getComponentOrientation());
			scrollpane.getViewport().setBackground(table.getBackground());
			p.add(scrollpane, "Center");
			return p;
		}

		private void editFileName() {
			if (editIndex < 0)
				return;
			editFile = (File) detailsTableSortModel.getValueAt(editIndex, 0);
			File currentDir = getFileChooser().getCurrentDirectory();
			if (!currentDir.canWrite()
					|| !editFile.canWrite()
					|| readOnly
					|| getFileChooser().getFileSystemView().isFileSystemRoot(
							editFile)) {
				cancelEditFileName();
				return;
			}
			Rectangle r = null;
			if (view == 0) {
				r = list.getCellBounds(editIndex, editIndex);
				list.add(editTextField);
			}
			ComponentOrientation orientation = list.getComponentOrientation();
			editTextField.setComponentOrientation(orientation);
			if (view == 0) {
				if (orientation.isLeftToRight())
					editTextField.setBounds(editX + r.x, r.y, r.width - editX,
							r.height);
				else
					editTextField
							.setBounds(r.x, r.y, r.width - editX, r.height);
			} else {
				table.editCellAt(editIndex, table.convertColumnIndexToView(0));
			}
			editTextField.setText(editFile.getName());
			editTextField.requestFocus();
			editTextField.selectAll();
		}

		public JPopupMenu getComponentPopupMenu() {
			JPopupMenu popupMenu = getFileChooser().getComponentPopupMenu();
			if (popupMenu != null)
				return popupMenu;
			java.util.Locale locale = getFileChooser().getLocale();
			JFileChooser fc = getFileChooser();
			File currentDir = fc.getCurrentDirectory();
			String approveText = "";
			if (fc.getDialogType() == 0)
				approveText = UIManager.getString(
						"FileChooser.contextMenu.open", locale);
			else if (fc.getDialogType() == 1)
				approveText = UIManager.getString(
						"FileChooser.contextMenu.save", locale);
			JMenuItem approveItem = new JMenuItem((new StringBuilder(
					"<html><b>")).append(approveText).append("</b>").toString());
			approveItem.addActionListener(SyntheticaFileChooserUI.this);
			approveItem.setActionCommand("approveAction");
			if (fc.getSelectedFile() == null && !isDirectorySelected())
				approveItem.setEnabled(false);
			else if (fc.getDialogType() != 0 && isDirectorySelected())
				approveItem.setEnabled(false);
			JRadioButtonMenuItem listViewItem = new JRadioButtonMenuItem(
					UIManager.getString("FileChooser.contextMenu.listView",
							locale));
			if (view == 0)
				listViewItem.setSelected(true);
			listViewItem.addActionListener(SyntheticaFileChooserUI.this);
			listViewItem.setActionCommand("view.listAction");
			JRadioButtonMenuItem tableViewItem = new JRadioButtonMenuItem(
					UIManager.getString("FileChooser.contextMenu.detailsView",
							locale));
			if (view == 1)
				tableViewItem.setSelected(true);
			tableViewItem.addActionListener(SyntheticaFileChooserUI.this);
			tableViewItem.setActionCommand("view.detailsAction");
			JMenu viewMenu = new JMenu(UIManager.getString(
					"FileChooser.contextMenu.view", locale));
			viewMenu.add(listViewItem);
			viewMenu.add(tableViewItem);
			JRadioButtonMenuItem orderByNameItem = new JRadioButtonMenuItem(
					UIManager.getString("FileChooser.contextMenu.orderByName",
							locale));
			orderByNameItem.addActionListener(SyntheticaFileChooserUI.this);
			orderByNameItem.setActionCommand("orderBy.nameAction");
			if (sortColumn == 0)
				orderByNameItem.setSelected(true);
			JRadioButtonMenuItem orderBySizeItem = new JRadioButtonMenuItem(
					UIManager.getString("FileChooser.contextMenu.orderBySize",
							locale));
			orderBySizeItem.addActionListener(SyntheticaFileChooserUI.this);
			orderBySizeItem.setActionCommand("orderBy.sizeAction");
			if (sortColumn == 1)
				orderBySizeItem.setSelected(true);
			JRadioButtonMenuItem orderByDateItem = new JRadioButtonMenuItem(
					UIManager.getString("FileChooser.contextMenu.orderByDate",
							locale));
			orderByDateItem.addActionListener(SyntheticaFileChooserUI.this);
			orderByDateItem.setActionCommand("orderBy.dateAction");
			if (sortColumn == 3)
				orderByDateItem.setSelected(true);
			JMenu orderByMenu = new JMenu(UIManager.getString(
					"FileChooser.contextMenu.orderBy", locale));
			orderByMenu.add(orderByNameItem);
			orderByMenu.add(orderBySizeItem);
			orderByMenu.add(orderByDateItem);
			JMenuItem refreshItem = new JMenuItem(UIManager.getString(
					"FileChooser.contextMenu.refresh", locale));
			refreshItem.addActionListener(SyntheticaFileChooserUI.this);
			refreshItem.setActionCommand("refreshAction");
			JMenuItem newFolderItem = new JMenuItem(UIManager.getString(
					"FileChooser.contextMenu.newFolder", locale));
			newFolderItem.addActionListener(SyntheticaFileChooserUI.this);
			newFolderItem.setActionCommand("newFolderAction");
			if (!currentDir.canWrite())
				newFolderItem.setEnabled(false);
			JMenuItem cutItem = new JMenuItem(UIManager.getString(
					"FileChooser.contextMenu.cut", locale));
			cutItem.addActionListener(SyntheticaFileChooserUI.this);
			cutItem.setActionCommand("cutAction");
			if (list.getSelectionModel().isSelectionEmpty())
				cutItem.setEnabled(false);
			JMenuItem copyItem = new JMenuItem(UIManager.getString(
					"FileChooser.contextMenu.copy", locale));
			copyItem.addActionListener(SyntheticaFileChooserUI.this);
			copyItem.setActionCommand("copyAction");
			if (list.getSelectionModel().isSelectionEmpty())
				copyItem.setEnabled(false);
			JMenuItem pasteItem = new JMenuItem(UIManager.getString(
					"FileChooser.contextMenu.paste", locale));
			pasteItem.addActionListener(SyntheticaFileChooserUI.this);
			pasteItem.setActionCommand("pasteAction");
			if (cutBuffer.isEmpty() && copyBuffer.isEmpty()
					|| !currentDir.canWrite())
				pasteItem.setEnabled(false);
			JMenuItem deleteItem = new JMenuItem(UIManager.getString(
					"FileChooser.contextMenu.delete", locale));
			deleteItem.addActionListener(SyntheticaFileChooserUI.this);
			deleteItem.setActionCommand("deleteAction");
			if (list.getSelectionModel().isSelectionEmpty()
					|| !currentDir.canWrite())
				deleteItem.setEnabled(false);
			JMenuItem renameItem = new JMenuItem(UIManager.getString(
					"FileChooser.contextMenu.rename", locale));
			renameItem.addActionListener(SyntheticaFileChooserUI.this);
			renameItem.setActionCommand("renameAction");
			if (fc.getSelectedFile() == null && !isDirectorySelected()
					|| !currentDir.canWrite())
				renameItem.setEnabled(false);
			JMenuItem propertiesItem = new JMenuItem(UIManager.getString(
					"FileChooser.contextMenu.properties", locale));
			propertiesItem.addActionListener(SyntheticaFileChooserUI.this);
			propertiesItem.setActionCommand("propertiesAction");
			if (list.getSelectionModel().isSelectionEmpty())
				propertiesItem.setEnabled(false);
			JPopupMenu contextMenu = new JPopupMenu();
			if (fc.getDialogType() != 2) {
				contextMenu.add(approveItem);
				contextMenu.addSeparator();
			}
			contextMenu.add(viewMenu);
			contextMenu.addSeparator();
			contextMenu.add(orderByMenu);
			contextMenu.add(refreshItem);
			contextMenu.addSeparator();
			contextMenu.add(newFolderItem);
			contextMenu.addSeparator();
			contextMenu.add(cutItem);
			contextMenu.add(copyItem);
			contextMenu.add(pasteItem);
			contextMenu.addSeparator();
			contextMenu.add(deleteItem);
			contextMenu.add(renameItem);
			contextMenu.addSeparator();
			contextMenu.add(propertiesItem);
			return contextMenu;
		}

		private int getView() {
			return view;
		}

		public void propertyChange(PropertyChangeEvent evt) {
			String name = evt.getPropertyName();
			if (name.equals("MultiSelectionEnabledChangedProperty"))
				if (getFileChooser().isMultiSelectionEnabled()) {
					list.setSelectionMode(2);
					table.setSelectionMode(2);
				} else {
					list.setSelectionMode(0);
					table.setSelectionMode(0);
				}
		}

		private void setInheritPopupMenu(Container container, boolean b) {
			int n = container.getComponentCount();
			if (container instanceof JComponent)
				((JComponent) container).setInheritsPopupMenu(b);
			for (int i = 0; i < n; i++)
				setInheritPopupMenu((Container) container.getComponent(i), b);

		}

		private void setView(int viewType) {
			if (view == viewType)
				return;
			firePropertyChange("SET_VIEW", view, viewType);
			view = viewType;
			viewPanel.removeAll();
			if (view == 0)
				viewPanel.add(listPanel);
			else
				viewPanel.add(tablePanel);
			setInheritPopupMenu(viewPanel, true);
			actionPerformed(new ActionEvent(this, 0, "orderBy.nameAction"));
			revalidate();
			viewPanel.repaint();
		}
	}

	protected class FileSelectHandler implements KeyListener {

		protected FileSelectHandler() {
			super();
		}

		private int getNextFileIndex(JComponent c, char prefix, int startIndex,
				javax.swing.text.Position.Bias bias) {
			int maxIndex = -1;
			if (c instanceof JList)
				maxIndex = ((JList) c).getModel().getSize();
			else if (c instanceof JTable)
				maxIndex = ((JTable) c).getModel().getRowCount();
			if (startIndex < 0 || startIndex >= maxIndex)
				return -1;
			boolean backwards = bias == javax.swing.text.Position.Bias.Backward;
			int i = startIndex;
			while (backwards ? i >= 0 : i < maxIndex) {
				String filename = null;
				if (c instanceof JList) {
					ListModel model = ((JList) c).getModel();
					filename = getFileChooser().getName(
							(File) model.getElementAt(i));
				} else if (c instanceof JTable) {
					TableModel model = ((JTable) c).getModel();
					filename = getFileChooser().getName(
							(File) model.getValueAt(i, 0));
				}
				if (filename.regionMatches(true, 0, (new StringBuilder(String
						.valueOf(prefix))).toString(), 0, 1))
					return i;
				i += backwards ? -1 : 1;
			}
			return -1;
		}

		public void keyPressed(KeyEvent keyevent) {
		}

		public void keyReleased(KeyEvent keyevent) {
		}

		public void keyTyped(KeyEvent e) {
			if (e.isAltDown() || e.isControlDown() || e.isMetaDown())
				return;
			char c = e.getKeyChar();
			JComponent src = (JComponent) e.getSource();
			int startIndex = -1;
			if (src instanceof JList)
				startIndex = ((JList) src).getLeadSelectionIndex() + 1;
			else if (src instanceof JTable)
				startIndex = ((JTable) src).getSelectedRow() + 1;
			int index = getNextFileIndex(src, c, startIndex,
					javax.swing.text.Position.Bias.Forward);
			if (index < 0)
				index = getNextFileIndex(src, c, 0,
						javax.swing.text.Position.Bias.Forward);
			if (index >= 0)
				if (src instanceof JList) {
					((JList) src).setSelectedIndex(index);
					((JList) src).ensureIndexIsVisible(index);
				} else if (src instanceof JTable) {
					((JTable) src).setRowSelectionInterval(index, index);
					((JTable) src).scrollRectToVisible(((JTable) src)
							.getCellRect(index, 0, true));
				}
		}
	}

	protected class FilterComboBoxModel extends AbstractListModel implements
			ComboBoxModel, PropertyChangeListener {

		protected FileFilter filters[];

		protected FilterComboBoxModel() {
			super();
			filters = getFileChooser().getChoosableFileFilters();
		}

		public Object getElementAt(int index) {
			if (filters.length > 0)
				return filters[index];
			else
				return null;
		}

		public Object getSelectedItem() {
			return getFileChooser().getFileFilter();
		}

		public int getSize() {
			return filters.length;
		}

		public void propertyChange(PropertyChangeEvent evt) {
			String name = evt.getPropertyName();
			if (name.equals("ChoosableFileFilterChangedProperty")) {
				filters = (FileFilter[]) evt.getNewValue();
				fireContentsChanged(this, -1, -1);
			} else if (name.equals("fileFilterChanged"))
				fireContentsChanged(this, -1, -1);
		}

		public void setSelectedItem(Object filter) {
			if (filter != null) {
				getFileChooser().setFileFilter((FileFilter) filter);
				setFileName(null);
				fireContentsChanged(this, -1, -1);
			}
		}
	}

	private class IndentIcon implements Icon {

		private Icon icon;

		private int depth;

		private int indent;

		IndentIcon() {
			super();
			icon = null;
			depth = 0;
			indent = 10;
		}

		public int getIconHeight() {
			return icon.getIconHeight();
		}

		public int getIconWidth() {
			return icon.getIconWidth() + depth * indent;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			if (c.getComponentOrientation().isLeftToRight())
				icon.paintIcon(c, g, x + depth * indent, y);
			else
				icon.paintIcon(c, g, x, y);
		}
	}

	private static final int LIST_VIEW = 0;

	private static final int TABLE_VIEW = 1;

	public static ComponentUI createUI(JComponent c) {
		return new SyntheticaFileChooserUI((JFileChooser) c);
	}

	private JButton newFolderButton;

	private JComboBox directoryComboBox;

	private JLabel lookInLabel;

	private JButton approveButton;

	private JButton cancelButton;

	private Box controlButtonPanel;

	private JTextField fileNameTextField;

	private FilePane filePane;

	private boolean readOnly;

	private HashSet cutBuffer;

	private HashSet copyBuffer;

	private boolean useSystemFileIcons;

	public SyntheticaFileChooserUI(JFileChooser fileChooser) {
		super(fileChooser);
		cutBuffer = new HashSet();
		copyBuffer = new HashSet();
		useSystemFileIcons = SyntheticaLookAndFeel.getUseSystemFileIcons();
	}

	public void actionPerformed(ActionEvent evt) {
		String actionCommand = evt.getActionCommand();
		if (actionCommand.startsWith("directoryComboBox.select")) {
			directoryComboBox.hidePopup();
			getFileChooser().setCurrentDirectory(
					(File) directoryComboBox.getSelectedItem());
		} else if (actionCommand.startsWith("upFolderAction"))
			getChangeToParentDirectoryAction().actionPerformed(evt);
		else if (actionCommand.startsWith("goHomeAction"))
			getGoHomeAction().actionPerformed(evt);
		else if (actionCommand.startsWith("newFolderAction")) {
			filePane.newCreatedFile = true;
			getNewFolderAction().actionPerformed(evt);
		} else if (actionCommand.startsWith("view.")) {
			if (actionCommand.endsWith("listAction"))
				filePane.setView(0);
			else if (actionCommand.endsWith("detailsAction"))
				filePane.setView(1);
		} else if (actionCommand.startsWith("approveAction"))
			getApproveSelectionAction().actionPerformed(
					new ActionEvent(this, 0, ""));
		else if (actionCommand.startsWith("orderBy.")) {
			if (filePane == null)
				return;
			if (actionCommand.endsWith("nameAction")) {
				if (filePane.sortColumn == 0 && filePane.sortOrder == 1)
					return;
				filePane.sortColumn = 0;
			} else if (actionCommand.endsWith("sizeAction")) {
				if (filePane.sortColumn == 1 && filePane.sortOrder == 1)
					return;
				filePane.sortColumn = 1;
			} else if (actionCommand.endsWith("dateAction")) {
				if (filePane.sortColumn == 3 && filePane.sortOrder == 1)
					return;
				filePane.sortColumn = 3;
			}
			filePane.sortOrder = 1;
			filePane.detailsTableSortModel.sort();
			filePane.viewPanel.repaint();
		} else if (actionCommand.startsWith("refreshAction"))
			rescanCurrentDirectory(getFileChooser());
		else if (actionCommand.startsWith("cutAction")) {
			fillFileBuffer(cutBuffer);
			copyBuffer.clear();
			filePane.list.getSelectionModel().clearSelection();
			filePane.repaint();
		} else if (actionCommand.startsWith("copyAction")) {
			fillFileBuffer(copyBuffer);
			if (!cutBuffer.isEmpty()) {
				cutBuffer.clear();
				filePane.repaint();
			}
		} else if (actionCommand.startsWith("pasteAction")) {
			int fileOperation = 0;
			final HashSet fileBuffer;
			if (!cutBuffer.isEmpty()) {
				fileBuffer = (HashSet) cutBuffer.clone();
				fileOperation = 2;
			} else {
				fileBuffer = (HashSet) copyBuffer.clone();
				fileOperation = 1;
			}
			final File currentDirectory = getFileChooser()
					.getCurrentDirectory();
			final FileOperationDialog dialog = new FileOperationDialog(
					getDialog(), fileOperation);
			dialog.setVisible(true);
			(new Thread() {

				public void run() {
					Iterator it = fileBuffer.iterator();
					while (it.hasNext()) {
						File source = (File) (File) it.next();
						File destination = new File(currentDirectory, source
								.getName());
						if (!source.exists()) {
							it.remove();
							continue;
						}
						if (!cutBuffer.isEmpty() && source.equals(destination)) {
							it.remove();
							continue;
						}
						if (source.equals(destination)) {
							int copyNumber = 0;
							String filename;
							for (; destination.exists(); destination = new File(
									destination.getParentFile(), filename)) {
								String copyNumberString = ++copyNumber != 1 ? (new StringBuilder(
										"(")).append(copyNumber).append(") ")
										.toString()
										: "";
								String copyString = UIManager.getString(
										"FileChooser.copyAction.copyFilename",
										getFileChooser().getLocale());
								filename = MessageFormat.format(copyString,
										new Object[] { copyNumberString,
												source.getName() });
							}

						}
						try {
							boolean abort = !FileUtils.copy(source,
									destination, true, true, copyBuffer
											.isEmpty(), dialog);
							if (abort)
								break;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					dialog.dispose();
					rescanCurrentDirectory(getFileChooser());
					filePane.repaint();
				}

			}).start();
			if (!cutBuffer.isEmpty())
				cutBuffer.clear();
		} else if (actionCommand.startsWith("deleteAction")) {
			if (filePane.list.getSelectionModel().isSelectionEmpty())
				return;
			String confirmMessage = UIManager
					.getString("FileChooser.deleteAction.confirmMessage");
			String confirmDialogTitle = UIManager
					.getString("FileChooser.deleteAction.confirmDialogTitle");
			JOptionPane opane = new JOptionPane();
			javax.swing.plaf.synth.SynthStyle style = SynthLookAndFeel
					.getStyleFactory().getStyle(opane, Region.OPTION_PANE);
			SynthContext sc = new SynthContext(opane, Region.OPTION_PANE,
					style, 1024);
			Icon stopIcon = ((DefaultSynthStyle) style).getIcon(sc,
					"OptionPane.stopIcon");
			int result = JOptionPane.showConfirmDialog(getFileChooser(),
					confirmMessage, confirmDialogTitle, 2, 3, stopIcon);
			if (result != 0)
				return;
			final HashSet deleteBuffer = new HashSet();
			fillFileBuffer(deleteBuffer);
			final FileOperationDialog dialog = new FileOperationDialog(
					getDialog(), 3);
			dialog.setVisible(true);
			(new Thread() {

				public void run() {
					for (Iterator it = deleteBuffer.iterator(); it.hasNext(); it
							.remove()) {
						File file = (File) (File) it.next();
						boolean abort = !FileUtils.delete(file, true, dialog);
						if (abort)
							break;
					}

					dialog.dispose();
					rescanCurrentDirectory(getFileChooser());
					filePane.repaint();
				}
			}).start();
		} else if (actionCommand.startsWith("propertiesAction")) {
			if (filePane.list.getSelectionModel().isSelectionEmpty())
				return;
			final HashSet buffer = new HashSet();
			fillFileBuffer(buffer);
			final FilePropertiesDialog dialog = new FilePropertiesDialog(
					getDialog());
			final FileProperties props = new FileProperties();
			dialog.setVisible(true);
			(new Thread() {

				public void run() {
					for (Iterator it = buffer.iterator(); it.hasNext();) {
						File file = (File) (File) it.next();
						boolean abort = false;
						try {
							abort = !FileUtils.determineProperties(props, file,
									true, dialog);
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (abort)
							break;
					}

					dialog.refresh();
				}

			}).start();
		} else if (actionCommand.startsWith("renameAction"))
			filePane.editFileName();
	}

	public PropertyChangeListener createPropertyChangeListener(JFileChooser fc) {
		return new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				String s = evt.getPropertyName();
				if (s.equals("SelectedFileChangedProperty"))
					doSelectedFileChanged(evt);
				else if (s.equals("SelectedFilesChangedProperty"))
					doSelectedFilesChanged(evt);
				else if (s.equals("directoryChanged"))
					doDirectoryChanged(evt);
				else if (!s.equals("fileFilterChanged")
						&& !s.equals("fileSelectionChanged"))
					if (s.equals("AccessoryChangedProperty"))
						doAccessoryChanged(evt);
					else if (s.equals("ApproveButtonTextChangedProperty")
							|| s
									.equals("ApproveButtonToolTipTextChangedProperty"))
						doApproveButtonTextChanged(evt);
					else if (s.equals("DialogTypeChangedProperty"))
						doDialogTypeChanged(evt);
					else if (!s.equals("ApproveButtonMnemonicChangedProperty"))
						if (s.equals("ControlButtonsAreShownChangedProperty")) {
							if (getFileChooser().getControlButtonsAreShown())
								controlButtonPanel.setVisible(true);
							else
								controlButtonPanel.setVisible(false);
						} else if (s.equals("componentOrientation")) {
							ComponentOrientation o = (ComponentOrientation) evt
									.getNewValue();
							JFileChooser cc = (JFileChooser) evt.getSource();
							if (o != (ComponentOrientation) evt.getOldValue())
								cc.applyComponentOrientation(o);
						} else if (s != "FileChooser.useShellFolder"
								&& s.equals("ancestor")
								&& evt.getOldValue() == null)
							evt.getNewValue();
			}
		};
	}

	private void doAccessoryChanged(PropertyChangeEvent e) {
		if (getAccessoryPanel() == null)
			return;
		JComponent accessory = (JComponent) e.getOldValue();
		if (accessory != null)
			getAccessoryPanel().remove(accessory);
		accessory = (JComponent) e.getNewValue();
		if (accessory != null)
			getAccessoryPanel().add(accessory, "Center");
	}

	private void doApproveButtonTextChanged(PropertyChangeEvent e) {
		JFileChooser fc = getFileChooser();
		approveButton.setText(getApproveButtonText(fc));
		approveButton.setToolTipText(getApproveButtonToolTipText(fc));
	}

	private void doDialogTypeChanged(PropertyChangeEvent e) {
		JFileChooser fc = getFileChooser();
		doApproveButtonTextChanged(e);
		if (fc.getDialogType() == 0)
			lookInLabel.setText(UIManager.getString(
					"FileChooser.lookInLabelText", fc.getLocale()));
		else if (fc.getDialogType() == 1)
			lookInLabel.setText(UIManager.getString(
					"FileChooser.saveInLabelText", fc.getLocale()));
	}

	private void doDirectoryChanged(PropertyChangeEvent e) {
		JFileChooser fc = getFileChooser();
		File currentDirectory = fc.getCurrentDirectory();
		if (currentDirectory.canWrite())
			newFolderButton.setEnabled(true);
		else
			newFolderButton.setEnabled(false);
		((DirectoryComboBoxModel) directoryComboBox.getModel())
				.addItem(currentDirectory);
	}

	private void doSelectedFileChanged(PropertyChangeEvent evt) {
		File file = (File) evt.getNewValue();
		if (file != null) {
			JFileChooser fc = getFileChooser();
			if (fc.isDirectorySelectionEnabled() || !file.isDirectory())
				setFileName(file.getName());
		}
	}

	private void doSelectedFilesChanged(PropertyChangeEvent evt) {
		File files[] = (File[]) evt.getNewValue();
		if (files != null && files.length > 1) {
			StringBuilder fileName = new StringBuilder();
			for (int i = 0; i < files.length; i++) {
				fileName.append("\"");
				fileName.append(files[i].getName());
				fileName.append("\" ");
			}

			setFileName(fileName.toString().trim());
		}
	}

	private synchronized void fillFileBuffer(Collection buffer) {
		buffer.clear();
		ListSelectionModel selected = filePane.list.getSelectionModel();
		TableModel model = filePane.detailsTableSortModel;
		for (int i = 0; i < model.getRowCount(); i++) {
			File file = (File) model.getValueAt(i, 0);
			if (selected.isSelectedIndex(i))
				buffer.add(file);
		}

	}

	private Dialog getDialog() {
		for (Container parent = getFileChooser().getParent(); parent != null; parent = parent
				.getParent())
			if (parent instanceof Dialog)
				return (Dialog) parent;

		return null;
	}

	public String getFileName() {
		return fileNameTextField.getText();
	}

	public void installComponents(final JFileChooser fc) {
		int xGap = 10;
		int yGap = 10;
		java.util.Locale locale = fc.getLocale();
		fc.setLayout(new BorderLayout(0, yGap));
		fc.setBorder(new EmptyBorder(yGap, xGap, yGap, xGap));
		JPanel northPanel = new JPanel(new BorderLayout(xGap, 0));
		lookInLabel = new JLabel(UIManager.getString(
				"FileChooser.lookInLabelText", locale));
		lookInLabel.setDisplayedMnemonic(UIManager
				.getInt("FileChooser.lookInLabelMnemonic"));
		northPanel.add(lookInLabel, "West");
		directoryComboBox = new JComboBox();
		final DirectoryComboBoxModel directoryComboBoxModel = new DirectoryComboBoxModel();
		directoryComboBox.setModel(directoryComboBoxModel);
		directoryComboBox.setRenderer(new DefaultListCellRenderer() {

			IndentIcon ii = new IndentIcon();

			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);
				if (value == null) {
					setText("");
					return this;
				}
				File directory = (File) value;
				setText(fc.getName(directory));
				if (useSystemFileIcons) {
					FileSystemView fsv = FileSystemView.getFileSystemView();
					ii.icon = fsv.getSystemIcon(directory);
				}
				if (ii.icon == null || !useSystemFileIcons)
					ii.icon = getFileChooser().getIcon(directory);
				ii.depth = directoryComboBoxModel.getDepth(index);
				setIcon(ii);
				return this;
			}
		});
		directoryComboBox.addActionListener(this);
		directoryComboBox.setActionCommand("directoryComboBox.select");
		directoryComboBox.setMaximumRowCount(15);
		directoryComboBox.putClientProperty("JComboBox.isTableCellEditor",
				Boolean.TRUE);
		northPanel.add(directoryComboBox);
		Box iconButtonPanel = new Box(2);
		iconButtonPanel.add(Box.createHorizontalStrut(xGap));
		boolean paintBorder = UIManager
				.getBoolean("Synthetica.extendedFileChooser.actionButtons.paintBorder");
		JButton upFolderButton = new JButton(upFolderIcon);
		upFolderButton.setToolTipText(UIManager.getString(
				"FileChooser.upFolderToolTipText", locale));
		upFolderButton.setMargin(new Insets(0, 0, 0, 0));
		if (!paintBorder)
			upFolderButton.setBorderPainted(false);
		upFolderButton.addActionListener(this);
		upFolderButton.setActionCommand("upFolderAction");
		iconButtonPanel.add(upFolderButton);
		JButton goHomeButton = new JButton(homeFolderIcon);
		goHomeButton.setToolTipText(UIManager.getString(
				"FileChooser.homeFolderToolTipText", locale));
		goHomeButton.setMargin(new Insets(0, 0, 0, 0));
		if (!paintBorder)
			goHomeButton.setBorderPainted(false);
		goHomeButton.addActionListener(this);
		goHomeButton.setActionCommand("goHomeAction");
		iconButtonPanel.add(goHomeButton);
		readOnly = UIManager.getBoolean("FileChooser.readOnly");
		if (!readOnly) {
			newFolderButton = new JButton(newFolderIcon);
			newFolderButton.setToolTipText(UIManager.getString(
					"FileChooser.newFolderToolTipText", locale));
			newFolderButton.setMargin(new Insets(0, 0, 0, 0));
			if (!paintBorder)
				newFolderButton.setBorderPainted(false);
			newFolderButton.addActionListener(this);
			newFolderButton.setActionCommand("newFolderAction");
			iconButtonPanel.add(newFolderButton);
		}
		iconButtonPanel.add(Box.createHorizontalStrut(xGap * 2));
		ButtonGroup viewButtonGroup = new ButtonGroup();
		final JToggleButton listViewButton = new JToggleButton(listViewIcon);
		listViewButton.setToolTipText(UIManager.getString(
				"FileChooser.listViewButtonToolTipText", locale));
		listViewButton.setMargin(new Insets(0, 0, 0, 0));
		if (!paintBorder)
			listViewButton.setBorderPainted(false);
		listViewButton.addActionListener(this);
		listViewButton.setActionCommand("view.listAction");
		viewButtonGroup.add(listViewButton);
		iconButtonPanel.add(listViewButton);
		final JToggleButton detailsViewButton = new JToggleButton(
				detailsViewIcon);
		detailsViewButton.setToolTipText(UIManager.getString(
				"FileChooser.detailsViewButtonToolTipText", locale));
		detailsViewButton.setMargin(new Insets(0, 0, 0, 0));
		if (!paintBorder)
			detailsViewButton.setBorderPainted(false);
		detailsViewButton.addActionListener(this);
		detailsViewButton.setActionCommand("view.detailsAction");
		viewButtonGroup.add(detailsViewButton);
		iconButtonPanel.add(detailsViewButton);
		northPanel.add(iconButtonPanel, "East");
		fc.add(northPanel, "North");
		filePane = new FilePane();
		filePane.addPropertyChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("SET_VIEW")) {
					int view = ((Integer) evt.getNewValue()).intValue();
					if (view == 0)
						listViewButton.setSelected(true);
					else if (view == 1)
						detailsViewButton.setSelected(true);
				}
			}
		});
		filePane.setView(0);
		fc.addPropertyChangeListener(filePane);
		fc.add(filePane, "Center");
		JComponent accessory = fc.getAccessory();
		if (accessory != null)
			getAccessoryPanel().add(accessory);
		fc.add(getAccessoryPanel(), "After");
		JPanel southPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = 17;
		gbc.weighty = 0.0D;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0, 0, yGap / 2, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0D;
		gbc.fill = 0;
		JLabel fileNameLabel = new JLabel(UIManager.getString(
				"FileChooser.fileNameLabelText", locale));
		fileNameLabel.setBorder(new EmptyBorder(0, 0, 0, xGap));
		southPanel.add(fileNameLabel, gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0D;
		gbc.fill = 2;
		fileNameTextField = new JTextField();
		southPanel.add(fileNameTextField, gbc);
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.0D;
		gbc.fill = 0;
		JLabel filesOfTypeLabel = new JLabel(UIManager.getString(
				"FileChooser.filesOfTypeLabelText", locale));
		filesOfTypeLabel.setBorder(new EmptyBorder(0, 0, 0, xGap));
		southPanel.add(filesOfTypeLabel, gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0D;
		gbc.fill = 2;
		JComboBox filterComboBox = new JComboBox();
		filterComboBox.setRenderer(new DefaultListCellRenderer() {

			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);
				if (value != null && (value instanceof FileFilter))
					setText(((FileFilter) value).getDescription());
				return this;
			}
		});
		FilterComboBoxModel filterComboBoxModel = new FilterComboBoxModel();
		filterComboBox.setModel(filterComboBoxModel);
		fc.addPropertyChangeListener(filterComboBoxModel);
		southPanel.add(filterComboBox, gbc);
		gbc.insets = new Insets(yGap * 2, 0, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0D;
		gbc.fill = 2;
		approveButton = new JButton(getApproveButtonText(fc));
		cancelButton = new JButton(cancelButtonText);
		int width = Math.max(approveButton.getPreferredSize().width,
				cancelButton.getPreferredSize().width);
		int height = Math.max(approveButton.getPreferredSize().height,
				cancelButton.getPreferredSize().height);
		approveButton.setPreferredSize(new Dimension(width, height));
		approveButton.setToolTipText(getApproveButtonToolTipText(fc));
		approveButton.addActionListener(getApproveSelectionAction());
		cancelButton.setPreferredSize(new Dimension(width, height));
		cancelButton.setToolTipText(cancelButtonToolTipText);
		cancelButton.addActionListener(getCancelSelectionAction());
		controlButtonPanel = new Box(2);
		controlButtonPanel.add(Box.createHorizontalGlue());
		controlButtonPanel.add(approveButton);
		controlButtonPanel.add(Box.createHorizontalStrut(xGap));
		controlButtonPanel.add(cancelButton);
		southPanel.add(controlButtonPanel, gbc);
		fc.add(southPanel, "South");
	}

	public void installUI(JComponent c) {
		super.installUI(c);
		if (SyntheticaLookAndFeel.getRememberFileChooserPreferences()) {
			String title = "";
			Frame frames[] = Frame.getFrames();
			for (int i = 0; i < frames.length; i++) {
				Frame frame = frames[i];
				if (!(frame instanceof JFrame))
					continue;
				title = frame.getTitle() == null ? "" : frame.getTitle();
				break;
			}

			int maxLen = title.length() <= 50 ? title.length() : 50;
			String path = title.substring(0, maxLen);
			final Preferences sysPrefs = Preferences.userRoot().node(
					(new StringBuilder(String.valueOf(path))).append(".")
							.append("SyntheticaFileChooser").toString());
			final JFileChooser fc = getFileChooser();
			(new Thread() {

				public void run() {
					Dialog dialog;
					for (dialog = null; (dialog = getDialog()) == null
							|| !dialog.isShowing();)
						try {
							sleep(10L);
						} catch (InterruptedException interruptedexception) {
						}

					int xPos = sysPrefs.getInt("xPos", dialog.getLocation().x);
					int yPos = sysPrefs.getInt("yPos", dialog.getLocation().y);
					int width = sysPrefs
							.getInt("width", dialog.getSize().width);
					int height = sysPrefs.getInt("height",
							dialog.getSize().height);
					dialog.setBounds(xPos, yPos, width, height);
					filePane.setView(sysPrefs
							.getInt("view", filePane.getView()));
					filePane.sortColumn = sysPrefs.getInt("sortColumn",
							filePane.sortColumn);
					filePane.sortOrder = sysPrefs.getInt("sortOrder",
							filePane.sortOrder);
					File dir = new File(sysPrefs.get(
							(new StringBuilder("directory")).append(
									fc.getDialogType()).toString(), fc
									.getCurrentDirectory().getAbsolutePath()));
					fc.setCurrentDirectory(dir);
					getDialog().addWindowListener(new WindowAdapter() {

						public void windowClosed(WindowEvent e) {
							Dialog dialog = getDialog();
							int xPos = dialog.getLocation().x;
							int yPos = dialog.getLocation().y;
							int width = dialog.getSize().width;
							int height = dialog.getSize().height;
							sysPrefs.putInt("xPos", xPos);
							sysPrefs.putInt("yPos", yPos);
							sysPrefs.putInt("width", width);
							sysPrefs.putInt("height", height);
							sysPrefs.putInt("view", filePane.getView());
							sysPrefs.putInt("sortColumn", filePane.sortColumn);
							sysPrefs.putInt("sortOrder", filePane.sortOrder);
							sysPrefs.put((new StringBuilder("directory"))
									.append(fc.getDialogType()).toString(), fc
									.getCurrentDirectory().getAbsolutePath());
						}
					});
				}
			}).start();
		}
	}

	public void rescanCurrentDirectory(JFileChooser fc) {
		getModel().validateFileCache();
	}

	public void setFileName(String filename) {
		fileNameTextField.setText(filename);
	}

}
