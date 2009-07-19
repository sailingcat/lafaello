package de.javasoft.plaf.synthetica.filechooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import de.javasoft.io.FileOperationEvent;
import de.javasoft.io.FileOperationListener;
import de.javasoft.io.FileProperties;
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
public class FilePropertiesDialog extends JDialog implements
		FileOperationListener, Runnable {

	private boolean abort;

	private long counter;

	private JLabel location;

	private JLabel files;

	private JLabel size;
	private JLabel date;
	private FileProperties props;
	private Color brightLabelColor;
	private Color labelColor;
	String title;
	String imagePath;

	public FilePropertiesDialog(Dialog owner) {
		super(owner);
		counter = 0L;
		title = "";
		imagePath = "";
		title = UIManager.getString("FilePropertiesDialog.title");
		imagePath = UIManager
				.getString("Synthetica.filePropertiesDialog.title.background");
		setTitle(title);
		setLayout(new BorderLayout());
		setAlwaysOnTop(true);
		setResizable(false);
		JPanel topPanel = new JPanel() {

			private Image background = (new ImageIcon(
					SyntheticaLookAndFeel.class.getResource(imagePath)))
					.getImage();

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(background.getWidth(null), background
						.getHeight(null));
			}

			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(background, 0, 0, null);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				int xPos = UIManager
						.getInt("Synthetica.filePropertiesDialog.title.xPos");
				int yPos = UIManager
						.getInt("Synthetica.filePropertiesDialog.title.yPos");
				g2.drawString(title, xPos, yPos);
			}
		};
		int fontSize = UIManager
				.getInt("Synthetica.filePropertiesDialog.title.size");
		topPanel.setFont(topPanel.getFont().deriveFont(1, fontSize));
		topPanel.setForeground(UIManager
				.getColor("Synthetica.filePropertiesDialog.title.color"));
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = 17;
		gbc.weightx = 0.0D;
		gbc.weighty = 0.0D;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		JPanel aniPanel = new JPanel(new BorderLayout());
		panel.add(aniPanel, gbc);
		gbc.insets = new Insets(0, 0, 10, 0);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0D;
		gbc.fill = 2;
		files = new JLabel("!");
		labelColor = files.getForeground();
		brightLabelColor = new Color(labelColor.getRGB() + 0x606060);
		files.setForeground(brightLabelColor);
		panel.add(files, gbc);
		gbc.insets = new Insets(0, 0, 2, 10);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.0D;
		gbc.fill = 0;
		JLabel locationLabel = new JLabel(UIManager
				.getString("FilePropertiesDialog.location"));
		panel.add(locationLabel, gbc);
		gbc.insets = new Insets(0, 0, 2, 0);
		gbc.gridx = 1;
		gbc.weightx = 1.0D;
		gbc.fill = 2;
		location = new JLabel();
		panel.add(location, gbc);
		gbc.insets = new Insets(0, 0, 2, 10);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.0D;
		gbc.fill = 0;
		JLabel sizeLabel = new JLabel(UIManager
				.getString("FilePropertiesDialog.size"));
		panel.add(sizeLabel, gbc);
		gbc.insets = new Insets(0, 0, 2, 0);
		gbc.gridx = 1;
		gbc.weightx = 1.0D;
		gbc.fill = 2;
		size = new JLabel();
		size.setForeground(brightLabelColor);
		panel.add(size, gbc);
		gbc.insets = new Insets(0, 0, 20, 10);
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.0D;
		gbc.fill = 0;
		JLabel dateLabel = new JLabel(UIManager
				.getString("FilePropertiesDialog.date"));
		panel.add(dateLabel, gbc);
		gbc.insets = new Insets(0, 0, 20, 0);
		gbc.gridx = 1;
		gbc.weightx = 1.0D;
		gbc.fill = 2;
		date = new JLabel();
		panel.add(date, gbc);
		gbc.anchor = 13;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 0.0D;
		gbc.fill = 0;
		JButton okButton = new JButton(UIManager
				.getString("FilePropertiesDialog.ok"));
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				abort = true;
				dispose();
			}
		});
		okButton.setMinimumSize(new Dimension(
				okButton.getPreferredSize().width * 2, okButton
						.getPreferredSize().height));
		okButton.setPreferredSize(okButton.getMinimumSize());
		panel.add(okButton, gbc);
		add(topPanel, "North");
		add(panel);
		pack();
		int x = owner.getLocation().x + owner.getSize().width / 2;
		int xLoc = x - getSize().width / 2;
		int y = owner.getLocation().y + owner.getSize().height / 2;
		int yLoc = y - getSize().height / 2;
		setLocation(xLoc, yLoc);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				abort = true;
			}
		});
	}

	public boolean processFileOperationEvent(FileOperationEvent evt) {
		counter++;
		props = (FileProperties) evt.getSource();
		if (counter % 100L == 0L || counter < 10L)
			EventQueue.invokeLater(this);
		return !abort;
	}

	public void refresh() {
		counter = 0L;
		files.setForeground(labelColor);
		size.setForeground(labelColor);
		run();
	}

	public void run() {
		NumberFormat nf = NumberFormat.getInstance();
		String filesFormat = UIManager
				.getString("FilePropertiesDialog.filesFormat");
		String numberOfDirectories = nf.format(props.directories);
		String numberOfFiles = nf.format(props.files);
		files.setText(MessageFormat.format(filesFormat, new Object[] {
				numberOfFiles, numberOfDirectories }));
		String sizeFormat = UIManager
				.getString("FilePropertiesDialog.sizeFormat");
		String sizeBytes = nf.format(props.size);
		nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		String sizeMB = nf.format(props.size / 1024D / 1024D);
		size.setText(MessageFormat.format(sizeFormat, new Object[] { sizeBytes,
				sizeMB }));
		location.setText(props.location);
		if (props.directories + props.files == 1L) {
			SimpleDateFormat df = new SimpleDateFormat(UIManager
					.getString("FilePropertiesDialog.dateFormat"));
			date.setText(df.format(new Date(props.lastModified)));
		} else {
			date.setText("---");
		}
	}
}
