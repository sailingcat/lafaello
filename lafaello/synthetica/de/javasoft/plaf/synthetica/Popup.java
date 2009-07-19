package de.javasoft.plaf.synthetica;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.apple.cocoa.application.NSApplication;
import com.apple.cocoa.application.NSWindow;
import com.apple.cocoa.foundation.NSArray;
import com.apple.cocoa.foundation.NSRect;

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
public class Popup extends javax.swing.Popup {

	public static final String POPUP_BACKGROUND = "POPUP_BACKGROUND";

	private static boolean isMacOSX = System.getProperty("os.name").indexOf(
			"Mac OS X") != -1;

	private Component contents;

	private int x;

	private int y;

	private javax.swing.Popup popup;
	private Container heavyWeightContainer;
	private static BufferedImage popupBackground;

	public Popup(Component owner, Component contents, int x, int y,
			javax.swing.Popup delegate) {
		this.contents = contents;
		popup = delegate;
		this.x = x;
		this.y = y;
		for (Container p = contents.getParent(); p != null; p = p.getParent()) {
			if (!(p instanceof JWindow) && !(p instanceof Panel)
					&& !(p instanceof Window))
				continue;
			heavyWeightContainer = p;
			break;
		}

	}

	private void disableMacShadow() {
		try {
			URLClassLoader sysloader = (URLClassLoader) ClassLoader
					.getSystemClassLoader();
			Class sysclass = URLClassLoader.class;
			Method method = sysclass.getDeclaredMethod("addURL",
					new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { (new File(
					"/System/Library/Java")).toURL() });
		} catch (Exception e) {
			throw new RuntimeException(
					"Could not add URL '/System/Library/Java' to system classloader");
		}
		NSApplication application = NSApplication.sharedApplication();
		NSArray windows = application.windows();
		Enumeration e = windows.reverseObjectEnumerator();
		Rectangle r = heavyWeightContainer.getBounds();
		while (e.hasMoreElements()) {
			NSWindow w = (NSWindow) (NSWindow) e.nextElement();
			NSRect nr = w.frame();
			if ((float) r.x == nr.x() && (float) r.width == nr.width()
					&& (float) r.height == nr.height()) {
				if (w.hasShadow())
					w.setHasShadow(false);
				break;
			}
		}
	}

	@Override
	public void hide() {
		Component parent = contents.getParent();
		popup.hide();
		if (heavyWeightContainer != null) {
			heavyWeightContainer = null;
			for (; parent != null; parent = parent.getParent())
				if (parent instanceof JFrame)
					((JFrame) parent).update(parent.getGraphics());

		}
		contents = null;
		popup = null;
	}

	@Override
	public void show() {
		if (heavyWeightContainer == null) {
			popup.show();
			return;
		} else {
			SwingUtilities.invokeLater(new Thread() {

				@Override
				public void run() {
					if (heavyWeightContainer == null)
						return;
					snapshot();
					((JComponent) contents.getParent()).putClientProperty(
							"POPUP_BACKGROUND", Popup.popupBackground);
					popup.show();
					if (Popup.isMacOSX
							&& !UIManager
									.getBoolean("Synthetica.popup.osShadow.enabled"))
						disableMacShadow();
				}
			});
			return;
		}
	}

	private void snapshot() {
		try {
			Robot robot = new Robot();
			Dimension size = heavyWeightContainer.getPreferredSize();
			Rectangle rect = new Rectangle(x, y, size.width, size.height);
			popupBackground = robot.createScreenCapture(rect);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
