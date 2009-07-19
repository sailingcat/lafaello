package de.javasoft.plaf.synthetica.painter;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

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
public class ImagePainter {
	private class Colorizer extends RGBImageFilter {

		int a;

		int r;
		int g;
		int b;

		public Colorizer(int alpha, int r, int g, int b) {
			super();
			canFilterIndexColorModel = true;
			a = alpha;
			this.r = r;
			this.g = g;
			this.b = b;
		}

		@Override
		public int filterRGB(int x, int y, int rgb) {
			Color c = new Color(rgb, true);
			int red = Math.min(
					Math.max(c.getRed() + (c.getRed() * r) / 100, 0), 255);
			int green = Math.min(Math.max(c.getGreen() + (c.getGreen() * g)
					/ 100, 0), 255);
			int blue = Math.min(Math.max(c.getBlue() + (c.getBlue() * b) / 100,
					0), 255);
			int alpha = Math.min(Math.max(c.getAlpha() + (c.getAlpha() * a)
					/ 100, 0), 255);
			return (new Color(red, green, blue, alpha)).getRGB();
		}
	}

	public static final int STRETCHED = 0;

	public static final int TILED = 1;

	private Graphics g;

	private int x;

	private int y;

	private int w;

	private int h;

	private int iw;

	private int ih;

	private Image image;

	private Insets sInsets;

	private Insets dInsets;

	private int xPolicy;

	private int yPolicy;

	private static long initDuration = -1L;

	private static int initOperations = 0;
	private static long paintDuration = 0L;
	private static int paintOperations = 0;
	private static HashMap imageCache = new HashMap();
	private static boolean debug = System.getProperty("synthetica.debug") != null;

	public ImagePainter(Graphics g, int x, int y, int w, int h,
			String imagePath, Insets sourceInsets, Insets destinationInsets,
			int xPolicy, int yPolicy) {
		sInsets = new Insets(0, 0, 0, 0);
		dInsets = new Insets(0, 0, 0, 0);
		if (!debug && imagePath == null)
			return;
		if (debug && initDuration == -1L) {
			Frame frames[] = Frame.getFrames();
			for (int i = 0; i < frames.length; i++)
				if (frames[i] instanceof JFrame)
					((JFrame) frames[i]).getRootPane().registerKeyboardAction(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									System.out.println((new StringBuilder(
											"inits: ")).append(
											ImagePainter.initOperations)
											.append(" in ").append(
													ImagePainter.initDuration)
											.append(" ms").toString());
									System.out.println((new StringBuilder(
											"paint: ")).append(
											ImagePainter.paintOperations)
											.append(" in ").append(
													ImagePainter.paintDuration)
											.append(" ms").toString());
									ImagePainter.initDuration = 0L;
									ImagePainter.initOperations = 0;
									ImagePainter.paintDuration = 0L;
									ImagePainter.paintOperations = 0;
									Frame frames[] = Frame.getFrames();
									for (int i = 0; i < frames.length; i++) {
										Frame frame = frames[i];
										if ((frame instanceof JFrame)
												&& ((JFrame) frame)
														.getRootPane() != null) {
											frame.setSize(640, 550);
											frame.validate();
											((JFrame) frame).getRootPane()
													.repaint();
										}
									}

								}
							}, "durationAction",
							KeyStroke.getKeyStroke(123, 10), 1);

		}
		long start = System.currentTimeMillis();
		this.g = g;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		image = (Image) imageCache.get(imagePath);
		if (image == null) {
			ImageIcon ii = new ImageIcon(SyntheticaLookAndFeel.class
					.getResource(imagePath));
			image = ii.getImage();
			imageCache.put(imagePath, image);
		}
		iw = image.getWidth(null);
		ih = image.getHeight(null);
		sInsets = sourceInsets;
		dInsets = destinationInsets;
		this.xPolicy = xPolicy;
		this.yPolicy = yPolicy;
		if (debug) {
			long stop = System.currentTimeMillis();
			long duration = stop - start;
			initDuration += duration;
			initOperations++;
		}
	}

	public ImagePainter(JComponent c, int red, int green, int blue, Graphics g,
			int x, int y, int w, int h, String imagePath, Insets sourceInsets,
			Insets destinationInsets, int xPolicy, int yPolicy) {
		this(g, x, y, w, h, imagePath, sourceInsets, destinationInsets,
				xPolicy, yPolicy);
		if (!debug && imagePath == null)
			return;
		Image image = (Image) imageCache.get(imagePath);
		if (image == null || !(image instanceof BufferedImage))
			try {
				image = ImageIO.read(SyntheticaLookAndFeel.class
						.getResource(imagePath));
				imageCache.put(imagePath, image);
			} catch (Exception e) {
				e.printStackTrace();
			}
		this.image = c.createImage(new FilteredImageSource(image.getSource(),
				new Colorizer(0, red, green, blue)));
	}

	public void draw() {
		drawBorder();
		drawCenter();
	}

	public void drawBorder() {
		drawTopLeft();
		drawTopCenter();
		drawTopRight();
		drawLeft();
		drawRight();
		drawBottomLeft();
		drawBottomCenter();
		drawBottomRight();
	}

	public void drawBottomCenter() {
		drawImage(image, g, x + dInsets.left, (y + h) - dInsets.bottom, (x + w)
				- dInsets.right, y + h, sInsets.left, ih - sInsets.bottom, iw
				- sInsets.right, ih, xPolicy, yPolicy);
	}

	public void drawBottomLeft() {
		drawImage(image, g, x, (y + h) - dInsets.bottom, x + dInsets.left, y
				+ h, 0, ih - sInsets.bottom, sInsets.left, ih, 0, 0);
	}

	public void drawBottomRight() {
		drawImage(image, g, (x + w) - dInsets.right, (y + h) - dInsets.bottom,
				x + w, y + h, iw - sInsets.right, ih - sInsets.bottom, iw, ih,
				0, 0);
	}

	public void drawCenter() {
		drawImage(image, g, x + dInsets.left, y + dInsets.top, (x + w)
				- dInsets.right, (y + h) - dInsets.bottom, sInsets.left,
				sInsets.top, iw - sInsets.right, ih - sInsets.bottom, xPolicy,
				yPolicy);
	}

	private void drawImage(Image image, Graphics g, int dx1, int dy1, int dx2,
			int dy2, int sx1, int sy1, int sx2, int sy2, int xPolicy,
			int yPolicy) {
		if (image == null)
			return;
		long start = System.currentTimeMillis();
		if (xPolicy == 0 && yPolicy == 0) {
			g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
		} else {
			int dx = dx1;
			int deltaX = sx2 - sx1;
			int deltaY = sy2 - sy1;
			for (; dy1 < dy2; dy1 += deltaY) {
				while (dx1 < dx2) {
					int dx2_ = dx2;
					if (xPolicy == 1)
						dx2_ = Math.min(dx2, dx1 + deltaX);
					int dy2_ = dy2;
					if (yPolicy == 1)
						dy2_ = Math.min(dy2, dy1 + deltaY);
					int sx2_ = xPolicy != 1 ? sx2 : Math.min(sx2, sx1
							+ (dx2_ - dx1));
					int sy2_ = yPolicy != 1 ? sy2 : Math.min(sy2, sy1
							+ (dy2_ - dy1));
					g.drawImage(image, dx1, dy1, dx2_, dy2_, sx1, sy1, sx2_,
							sy2_, null);
					if (xPolicy == 0)
						break;
					dx1 += deltaX;
				}
				if (yPolicy == 0)
					break;
				dx1 = dx;
			}

		}
		long stop = System.currentTimeMillis();
		long duration = stop - start;
		if (debug && duration > 100L)
			System.out.println((new StringBuilder("Paint performance lack: "))
					.append(duration).append(" ms ").append(findImage(image))
					.toString());
		paintDuration += duration;
		paintOperations++;
	}

	public void drawLeft() {
		drawImage(image, g, x, y + dInsets.top, x + dInsets.left, (y + h)
				- dInsets.bottom, 0, sInsets.top, sInsets.left, ih
				- sInsets.bottom, xPolicy, yPolicy);
	}

	public void drawRight() {
		drawImage(image, g, (x + w) - dInsets.right, y + dInsets.top, x + w,
				(y + h) - dInsets.bottom, iw - sInsets.right, sInsets.top, iw,
				ih - sInsets.bottom, xPolicy, yPolicy);
	}

	public void drawTopCenter() {
		drawImage(image, g, x + dInsets.left, y, (x + w) - dInsets.right, y
				+ dInsets.top, sInsets.left, 0, iw - sInsets.right,
				sInsets.top, xPolicy, yPolicy);
	}

	public void drawTopLeft() {
		drawImage(image, g, x, y, x + dInsets.left, y + dInsets.top, 0, 0,
				sInsets.left, sInsets.top, 0, 0);
	}

	public void drawTopRight() {
		drawImage(image, g, (x + w) - dInsets.right, y, x + w, y + dInsets.top,
				iw - sInsets.right, 0, iw, sInsets.top, 0, 0);
	}

	private String findImage(Image image) {
		String path = null;
		Set entries = imageCache.entrySet();
		for (Iterator it = entries.iterator(); it.hasNext();) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			if (entry.getValue() == image) {
				path = (String) entry.getKey();
				break;
			}
		}

		return path;
	}
}
