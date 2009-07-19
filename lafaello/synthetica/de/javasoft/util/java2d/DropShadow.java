package de.javasoft.util.java2d;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBufferInt;
import java.awt.image.Kernel;

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
public class DropShadow {

	private boolean highQuality;

	private BufferedImage shadow;

	private BufferedImage originalImage;

	private float angle;

	private int distance;

	private int shadowSize;

	private float shadowOpacity;

	private Color shadowColor;

	private int distance_x;

	private int distance_y;

	protected DropShadow() {
		highQuality = true;
		shadow = null;
		originalImage = null;
		angle = 45F;
		distance = -5;
		shadowSize = 5;
		shadowOpacity = 0.8F;
		shadowColor = new Color(0);
		distance_x = 0;
		distance_y = 0;
		computeShadowPosition();
	}

	public DropShadow(BufferedImage image) {
		this();
		setImage(image);
	}

	private void applyShadow(BufferedImage image) {
		int dstWidth = image.getWidth();
		int dstHeight = image.getHeight();
		int left = shadowSize - 1 >> 1;
		int right = shadowSize - left;
		int xStart = left;
		int xStop = dstWidth - right;
		int yStart = left;
		int yStop = dstHeight - right;
		int shadowRgb = shadowColor.getRGB() & 0xffffff;
		int aHistory[] = new int[shadowSize];
		int historyIdx = 0;
		int dataBuffer[] = ((DataBufferInt) image.getRaster().getDataBuffer())
				.getData();
		int lastPixelOffset = right * dstWidth;
		float sumDivider = shadowOpacity / shadowSize;
		int y = 0;
		for (int bufferOffset = 0; y < dstHeight; bufferOffset = ++y * dstWidth) {
			int aSum = 0;
			historyIdx = 0;
			for (int x = 0; x < shadowSize;) {
				int a = dataBuffer[bufferOffset] >>> 24;
				aHistory[x] = a;
				aSum += a;
				x++;
				bufferOffset++;
			}

			bufferOffset -= right;
			for (int x = xStart; x < xStop;) {
				int a = (int) (aSum * sumDivider);
				dataBuffer[bufferOffset] = a << 24 | shadowRgb;
				aSum -= aHistory[historyIdx];
				a = dataBuffer[bufferOffset + right] >>> 24;
				aHistory[historyIdx] = a;
				aSum += a;
				if (++historyIdx >= shadowSize)
					historyIdx -= shadowSize;
				x++;
				bufferOffset++;
			}

		}

		int x = 0;
		for (int bufferOffset = 0; x < dstWidth; bufferOffset = ++x) {
			int aSum = 0;
			historyIdx = 0;
			for (int y1 = 0; y1 < shadowSize;) {
				int a = dataBuffer[bufferOffset] >>> 24;
				aHistory[y1] = a;
				aSum += a;
				y1++;
				bufferOffset += dstWidth;
			}

			bufferOffset -= lastPixelOffset;
			for (int z = yStart; z < yStop;) {
				int a = (int) (aSum * sumDivider);
				dataBuffer[bufferOffset] = a << 24 | shadowRgb;
				aSum -= aHistory[historyIdx];
				a = dataBuffer[bufferOffset + lastPixelOffset] >>> 24;
				aHistory[historyIdx] = a;
				aSum += a;
				if (++historyIdx >= shadowSize)
					historyIdx -= shadowSize;
				z++;
				bufferOffset += dstWidth;
			}

		}

	}

	private void computeShadowPosition() {
		double angleRadians = Math.toRadians(angle);
		distance_x = (int) (Math.cos(angleRadians) * distance);
		distance_y = (int) (Math.sin(angleRadians) * distance);
	}

	private BufferedImage createDropShadow(BufferedImage image) {
		BufferedImage subject = prepareImage(image);
		if (highQuality) {
			BufferedImage shadow = new BufferedImage(subject.getWidth(),
					subject.getHeight(), 2);
			BufferedImage shadowMask = createShadowMask(subject);
			getLinearBlurOp(shadowSize).filter(shadowMask, shadow);
			return shadow;
		} else {
			applyShadow(subject);
			return subject;
		}
	}

	private BufferedImage createShadowMask(BufferedImage image) {
		BufferedImage mask = new BufferedImage(image.getWidth(), image
				.getHeight(), 2);
		Graphics2D g2d = mask.createGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.setComposite(AlphaComposite.getInstance(5, shadowOpacity));
		g2d.setColor(shadowColor);
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
		g2d.dispose();
		return mask;
	}

	public float getAngle() {
		return angle;
	}

	public int getDistance() {
		return distance;
	}

	public boolean getHighQuality() {
		return highQuality;
	}

	public BufferedImage getImage() {
		return originalImage;
	}

	private ConvolveOp getLinearBlurOp(int size) {
		float data[] = new float[size * size];
		float value = 1.0F / (size * size);
		for (int i = 0; i < data.length; i++)
			data[i] = value;

		return new ConvolveOp(new Kernel(size, size, data));
	}

	public Color getShadowColor() {
		return shadowColor;
	}

	public float getShadowOpacity() {
		return shadowOpacity;
	}

	public int getShadowSize() {
		return shadowSize;
	}

	public void paint(Graphics g, int x, int y) {
		paintShadow(g, x, y);
		if (originalImage != null)
			g.drawImage(originalImage, x, y, null);
	}

	public void paintShadow(Graphics g, int x, int y) {
		if (shadow != null)
			g.drawImage(shadow, x + distance_x, y + distance_y, null);
	}

	private BufferedImage prepareImage(BufferedImage image) {
		BufferedImage image_ = new BufferedImage(image.getWidth() + shadowSize
				* 2, image.getHeight() + shadowSize * 2, 2);
		Graphics2D g2 = image_.createGraphics();
		g2.drawImage(image, null, shadowSize, shadowSize);
		g2.dispose();
		return image_;
	}

	private void refreshShadow() {
		if (originalImage != null)
			shadow = createDropShadow(originalImage);
	}

	public void setAngle(float angle) {
		this.angle = angle;
		computeShadowPosition();
	}

	public void setDistance(int distance) {
		this.distance = distance;
		computeShadowPosition();
	}

	public void setImage(BufferedImage image) {
		originalImage = image;
		refreshShadow();
	}

	public void setQuality(boolean highQuality) {
		this.highQuality = highQuality;
		refreshShadow();
	}

	public void setShadowColor(Color shadowColor) {
		this.shadowColor = shadowColor;
		refreshShadow();
	}

	public void setShadowOpacity(float shadowOpacity) {
		this.shadowOpacity = shadowOpacity;
		refreshShadow();
	}

	public void setShadowSize(int shadowSize) {
		this.shadowSize = shadowSize;
		refreshShadow();
	}
}
