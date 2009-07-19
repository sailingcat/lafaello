package de.javasoft.plaf.synthetica.filechooser;

import java.awt.BorderLayout;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import de.javasoft.io.FileOperationEvent;
import de.javasoft.io.FileOperationListener;
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
public class FileOperationDialog extends JDialog implements
		FileOperationListener, Runnable {

	public static final int COPY_OPERATION = 1;

	public static final int MOVE_OPERATION = 2;

	public static final int DELETE_OPERATION = 3;

	private boolean abort;
	private JLabel folderName;
	private JLabel fileName;
	private String currentFolderName;
	private String currentFileName;
	private JProgressBar progressBar;
	String title;
	String imagePath;

	public FileOperationDialog(Dialog owner, int operation) {
		super(owner);
		title = "";
		imagePath = "";
		switch (operation) {
		case 1: // '\001'
			title = UIManager.getString("FileOperationDialog.copy.title");
			imagePath = UIManager
					.getString("Synthetica.fileOperationDialog.title.copyBackground");
			break;

		case 2: // '\002'
			title = UIManager.getString("FileOperationDialog.move.title");
			imagePath = UIManager
					.getString("Synthetica.fileOperationDialog.title.moveBackground");
			break;

		case 3: // '\003'
			title = UIManager.getString("FileOperationDialog.delete.title");
			imagePath = UIManager
					.getString("Synthetica.fileOperationDialog.title.deleteBackground");
			break;
		}
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
						.getInt("Synthetica.fileOperationDialog.title.xPos");
				int yPos = UIManager
						.getInt("Synthetica.fileOperationDialog.title.yPos");
				g2.drawString(title, xPos, yPos);
			}
		};
		int fontSize = UIManager
				.getInt("Synthetica.fileOperationDialog.title.size");
		topPanel.setFont(topPanel.getFont().deriveFont(1, fontSize));
		topPanel.setForeground(UIManager
				.getColor("Synthetica.fileOperationDialog.title.color"));
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
		gbc.insets = new Insets(0, 0, 2, 10);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.0D;
		gbc.fill = 0;
		JLabel folderLabel = new JLabel(UIManager
				.getString("FileOperationDialog.folder"));
		panel.add(folderLabel, gbc);
		gbc.insets = new Insets(0, 0, 2, 0);
		gbc.gridx = 1;
		gbc.weightx = 1.0D;
		gbc.fill = 2;
		folderName = new JLabel();
		panel.add(folderName, gbc);
		gbc.insets = new Insets(0, 0, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.0D;
		gbc.fill = 0;
		JLabel fileNameLabel = new JLabel(UIManager
				.getString("FileOperationDialog.file"));
		panel.add(fileNameLabel, gbc);
		gbc.insets = new Insets(0, 0, 10, 0);
		gbc.gridx = 1;
		gbc.weightx = 1.0D;
		gbc.fill = 2;
		fileName = new JLabel();
		panel.add(fileName, gbc);
		gbc.insets = new Insets(0, 0, 20, 0);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 3;
		progressBar = new JProgressBar(0, 50);
		panel.add(progressBar, gbc);
		gbc.anchor = 13;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.0D;
		gbc.fill = 0;
		JButton cancelButton = new JButton(UIManager
				.getString("FileOperationDialog.cancel"));
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				abort = true;
				dispose();
			}
		});
		panel.add(cancelButton, gbc);
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
		currentFolderName = evt.getFile().getParentFile().getName();
		currentFileName = evt.getFile().getName();
		EventQueue.invokeLater(this);
		return !abort;
	}

	public void run() {
		folderName.setText(currentFolderName);
		fileName.setText(currentFileName);
		progressBar.setValue((progressBar.getValue() + 1) % 50);
	}
}
