package de.javasoft.io;

import java.io.File;
import java.util.EventObject;

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
public class FileOperationEvent extends EventObject {

	private static final long serialVersionUID = 0x9d22921846156f90L;

	public static final int CREATE = 1;

	public static final int COPY = 2;

	public static final int DELETE = 3;
	public static final int PROPERTIES = 4;
	private File file;
	private int fileOperation;

	public FileOperationEvent(Object source, File file, int fileOperation) {
		super(source);
		this.file = file;
		this.fileOperation = fileOperation;
	}

	public File getFile() {
		return file;
	}

	public int getOperation() {
		return fileOperation;
	}
}
