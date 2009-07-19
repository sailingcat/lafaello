package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;

import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

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
public class SyntheticaPainter extends SynthPainter {

	private static SynthPainter instance;

	public static SynthPainter getInstance() {
		if (instance == null)
			instance = new SyntheticaPainter();
		return instance;
	}

	public SyntheticaPainter() {
		if (instance == null)
			instance = this;
	}

	@Override
	public void paintArrowButtonBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		ArrowButtonPainter.getInstance().paintArrowButtonBackground(sc, g, x,
				y, w, h);
	}

	@Override
	public void paintArrowButtonForeground(SynthContext sc, Graphics g, int x,
			int y, int w, int h, int direction) {
		ArrowButtonPainter.getInstance().paintArrowButtonForeground(sc, g, x,
				y, w, h, direction);
	}

	@Override
	public void paintButtonBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		ButtonPainter.getInstance().paintButtonBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintCheckBoxBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		CheckBoxPainter.getInstance()
				.paintCheckBoxBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintCheckBoxBorder(SynthContext sc, Graphics g, int x, int y,
			int w, int h) {
		CheckBoxPainter.getInstance().paintCheckBoxBorder(sc, g, x, y, w, h);
	}

	@Override
	public void paintCheckBoxMenuItemBackground(SynthContext sc, Graphics g,
			int x, int y, int w, int h) {
		MenuPainter.getInstance().paintCheckBoxMenuItemBackground(sc, g, x, y,
				w, h);
	}

	@Override
	public void paintComboBoxBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		ComboBoxPainter.getInstance()
				.paintComboBoxBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintComboBoxBorder(SynthContext sc, Graphics g, int x, int y,
			int w, int h) {
		ComboBoxPainter.getInstance().paintComboBoxBorder(sc, g, x, y, w, h);
	}

	@Override
	public void paintDesktopPaneBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		DesktopPanePainter.getInstance().paintDesktopPaneBackground(sc, g, x,
				y, w, h);
	}

	@Override
	public void paintEditorPaneBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		EditorPanePainter.getInstance().paintEditorPaneBackground(sc, g, x, y,
				w, h);
	}

	@Override
	public void paintFormattedTextFieldBackground(SynthContext sc, Graphics g,
			int x, int y, int w, int h) {
		FormattedTextFieldPainter.getInstance()
				.paintFormattedTextFieldBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintFormattedTextFieldBorder(SynthContext sc, Graphics g,
			int x, int y, int w, int h) {
		FormattedTextFieldPainter.getInstance().paintFormattedTextFieldBorder(
				sc, g, x, y, w, h);
	}

	@Override
	public void paintInternalFrameBorder(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		InternalFramePainter.getInstance().paintInternalFrameBorder(sc, g, x,
				y, w, h);
	}

	@Override
	public void paintInternalFrameTitlePaneBackground(SynthContext sc,
			Graphics g, int x, int y, int w, int h) {
		InternalFramePainter.getInstance()
				.paintInternalFrameTitlePaneBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintInternalFrameTitlePaneBorder(SynthContext sc, Graphics g,
			int x, int y, int w, int h) {
		InternalFramePainter.getInstance().paintInternalFrameTitlePaneBorder(
				sc, g, x, y, w, h);
	}

	@Override
	public void paintLabelBackground(SynthContext sc, Graphics g, int x, int y,
			int w, int h) {
		LabelPainter.getInstance().paintLabelBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintMenuBackground(SynthContext sc, Graphics g, int x, int y,
			int w, int h) {
		MenuPainter.getInstance().paintMenuBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintMenuBarBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		MenuPainter.getInstance().paintMenuBarBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintMenuItemBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		MenuPainter.getInstance().paintMenuItemBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintPanelBackground(SynthContext sc, Graphics g, int x, int y,
			int w, int h) {
		PanelPainter.getInstance().paintPanelBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintPasswordFieldBackground(SynthContext sc, Graphics g,
			int x, int y, int w, int h) {
		PasswordFieldPainter.getInstance().paintPasswordFieldBackground(sc, g,
				x, y, w, h);
	}

	@Override
	public void paintPasswordFieldBorder(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		PasswordFieldPainter.getInstance().paintPasswordFieldBorder(sc, g, x,
				y, w, h);
	}

	@Override
	public void paintPopupMenuBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		MenuPainter.getInstance().paintPopupMenuBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintProgressBarBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		ProgressBarPainter.getInstance().paintProgressBarBackground(sc, g, x,
				y, w, h);
	}

	@Override
	public void paintProgressBarForeground(SynthContext sc, Graphics g, int x,
			int y, int w, int h, int direction) {
		ProgressBarPainter.getInstance().paintProgressBarForeground(sc, g, x,
				y, w, h, direction);
	}

	@Override
	public void paintRadioButtonBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		RadioButtonPainter.getInstance().paintRadioButtonBackground(sc, g, x,
				y, w, h);
	}

	@Override
	public void paintRadioButtonBorder(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		RadioButtonPainter.getInstance().paintRadioButtonBorder(sc, g, x, y, w,
				h);
	}

	@Override
	public void paintRadioButtonMenuItemBackground(SynthContext sc, Graphics g,
			int x, int y, int w, int h) {
		MenuPainter.getInstance().paintRadioButtonMenuItemBackground(sc, g, x,
				y, w, h);
	}

	@Override
	public void paintScrollBarThumbBackground(SynthContext sc, Graphics g,
			int x, int y, int w, int h, int orientation) {
		ScrollBarPainter.getInstance().paintScrollBarThumbBackground(sc, g, x,
				y, w, h, orientation);
	}

	@Override
	public void paintScrollBarTrackBackground(SynthContext sc, Graphics g,
			int x, int y, int w, int h) {
		ScrollBarPainter.getInstance().paintScrollBarTrackBackground(sc, g, x,
				y, w, h);
	}

	@Override
	public void paintScrollPaneBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		ScrollPanePainter.getInstance().paintScrollPaneBackground(sc, g, x, y,
				w, h);
	}

	@Override
	public void paintScrollPaneBorder(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		ScrollPanePainter.getInstance()
				.paintScrollPaneBorder(sc, g, x, y, w, h);
	}

	@Override
	public void paintSeparatorForeground(SynthContext sc, Graphics g, int x,
			int y, int w, int h, int orientation) {
		ToolBarPainter.getInstance().paintSeparatorForeground(sc, g, x, y, w,
				h, orientation);
	}

	@Override
	public void paintSplitPaneDividerForeground(SynthContext sc, Graphics g,
			int x, int y, int w, int h, int orientation) {
		SplitPanePainter.getInstance().paintSplitPaneDividerForeground(sc, g,
				x, y, w, h, orientation);
	}

	@Override
	public void paintTabbedPaneContentBorder(SynthContext sc, Graphics g,
			int x, int y, int w, int h) {
		TabbedPanePainter.getInstance().paintTabbedPaneContentBorder(sc, g, x,
				y, w, h);
	}

	@Override
	public void paintTabbedPaneTabAreaBorder(SynthContext sc, Graphics g,
			int x, int y, int w, int h) {
		TabbedPanePainter.getInstance().paintTabbedPaneTabAreaBorder(sc, g, x,
				y, w, h);
	}

	@Override
	public void paintTabbedPaneTabBackground(SynthContext sc, Graphics g,
			int x, int y, int w, int h, int tabIndex) {
		TabbedPanePainter.getInstance().paintTabbedPaneTabBackground(sc, g, x,
				y, w, h, tabIndex);
	}

	@Override
	public void paintTableBackground(SynthContext sc, Graphics g, int x, int y,
			int w, int h) {
		TablePainter.getInstance().paintTableBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintTableBorder(SynthContext sc, Graphics g, int x, int y,
			int w, int h) {
		TablePainter.getInstance().paintTableBorder(sc, g, x, y, w, h);
	}

	@Override
	public void paintTextAreaBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		TextAreaPainter.getInstance()
				.paintTextAreaBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintTextFieldBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		TextFieldPainter.getInstance().paintTextFieldBackground(sc, g, x, y, w,
				h);
	}

	@Override
	public void paintTextFieldBorder(SynthContext sc, Graphics g, int x, int y,
			int w, int h) {
		TextFieldPainter.getInstance().paintTextFieldBorder(sc, g, x, y, w, h);
	}

	@Override
	public void paintTextPaneBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		TextPanePainter.getInstance()
				.paintTextPaneBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintToggleButtonBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		ToggleButtonPainter.getInstance().paintToggleButtonBackground(sc, g, x,
				y, w, h);
	}

	@Override
	public void paintToolBarBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		ToolBarPainter.getInstance().paintToolBarBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintToolTipBackground(SynthContext sc, Graphics g, int x,
			int y, int w, int h) {
		ToolTipPainter.getInstance().paintToolTipBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintTreeBackground(SynthContext sc, Graphics g, int x, int y,
			int w, int h) {
		TreePainter.getInstance().paintTreeBackground(sc, g, x, y, w, h);
	}

	@Override
	public void paintTreeCellBackground(SynthContext synthcontext, Graphics g1,
			int i, int j, int k, int l) {
	}

	@Override
	public void paintTreeCellBorder(SynthContext synthcontext, Graphics g1,
			int i, int j, int k, int l) {
	}

	@Override
	public void paintTreeCellFocus(SynthContext synthcontext, Graphics g1,
			int i, int j, int k, int l) {
	}
}
