import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaBlueMoonLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaGreenDreamLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaSilverMoonLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel;

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
public class TestIt {

	public static void main(String[] args) throws ParseException, Throwable {
		Class[] lafs = new Class[] { SyntheticaStandardLookAndFeel.class,
				SyntheticaBlackMoonLookAndFeel.class,
				SyntheticaBlackStarLookAndFeel.class,
				SyntheticaBlueIceLookAndFeel.class,
				SyntheticaBlueMoonLookAndFeel.class,
				SyntheticaGreenDreamLookAndFeel.class,
				SyntheticaSilverMoonLookAndFeel.class };

		System.err.println("Synthetica -> "
				+ new SyntheticaStandardLookAndFeel().getVersion());

		List<LookAndFeelInfo> installed = new ArrayList<LookAndFeelInfo>(Arrays
				.asList(UIManager.getInstalledLookAndFeels()));
		for (Class<SyntheticaLookAndFeel> clazz : lafs) {
			installed.add(new LookAndFeelInfo(clazz.getSimpleName(), clazz
					.getName()));
		}
		UIManager.setInstalledLookAndFeels(installed
				.toArray(new LookAndFeelInfo[0]));

		// showJTree();

		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingSet2.main(null);
	}
}
