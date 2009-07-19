package de.javasoft.plaf.synthetica.styles;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;

import de.javasoft.plaf.synthetica.painter.SyntheticaPainter;

public abstract class StyleWrapper extends SynthStyle {

	protected SynthStyle synthStyle;

	public StyleWrapper() {
	}

	public StyleWrapper(SynthStyle synthStyle) {
		this.synthStyle = synthStyle;
	}

	@Override
	public Object get(SynthContext sc, Object key) {
		return synthStyle.get(sc, key);
	}

	@Override
	public boolean getBoolean(SynthContext sc, Object key, boolean defaultValue) {
		return synthStyle.getBoolean(sc, key, defaultValue);
	}

	@Override
	public Color getColor(SynthContext sc, ColorType type) {
		return synthStyle.getColor(sc, type);
	}

	@Override
	public Color getColorForState(SynthContext sc, ColorType type) {
		return synthStyle.getColor(sc, type);
	}

	@Override
	public Font getFont(SynthContext sc) {
		return synthStyle.getFont(sc);
	}

	@Override
	public Font getFontForState(SynthContext sc) {
		return synthStyle.getFont(sc);
	}

	@Override
	public SynthGraphicsUtils getGraphicsUtils(SynthContext sc) {
		return synthStyle.getGraphicsUtils(sc);
	}

	@Override
	public Icon getIcon(SynthContext sc, Object key) {
		return synthStyle.getIcon(sc, key);
	}

	@Override
	public Insets getInsets(SynthContext sc, Insets insets) {
		return synthStyle.getInsets(sc, insets);
	}

	@Override
	public int getInt(SynthContext sc, Object key, int defaultValue) {
		return synthStyle.getInt(sc, key, defaultValue);
	}

	@Override
	public SynthPainter getPainter(SynthContext sc) {
		return SyntheticaPainter.getInstance();
	}

	@Override
	public String getString(SynthContext sc, Object key, String defaultValue) {
		return synthStyle.getString(sc, key, defaultValue);
	}

	@Override
	public void installDefaults(SynthContext sc) {
		synthStyle.installDefaults(sc);
	}

	@Override
	public boolean isOpaque(SynthContext sc) {
		return synthStyle.isOpaque(sc);
	}

	void setStyle(SynthStyle style) {
		synthStyle = style;
	}

	@Override
	public void uninstallDefaults(SynthContext sc) {
		synthStyle.uninstallDefaults(sc);
	}
}
