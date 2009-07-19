package de.javasoft.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
public class FileUtils {

	public static boolean copy(File source, File destination,
			boolean recursive, boolean replaceAll, boolean move,
			FileOperationListener listener) throws IOException {
		boolean abort = false;
		if (!source.exists())
			throw new IOException((new StringBuilder(
					"Source directory not found: ")).append(
					source.getAbsolutePath()).toString());
		File files[];
		if (source.isDirectory() && (files = source.listFiles()) != null) {
			if (!destination.exists()) {
				if (listener != null)
					abort = !listener
							.processFileOperationEvent(new FileOperationEvent(
									FileUtils.class, destination, 1));
				if (abort)
					return !abort;
				destination.mkdir();
			}
			for (int i = 0; i < files.length; i++)
				if (files[i].isDirectory() && recursive) {
					File destinationFolder = new File(destination, files[i]
							.getName());
					abort = !copy(files[i], destinationFolder, recursive,
							replaceAll, move, listener);
					if (abort)
						return !abort;
				} else if (files[i].isFile()) {
					File destinationFile = new File(destination, files[i]
							.getName());
					if (replaceAll
							|| !destinationFile.exists()
							|| destinationFile.lastModified() < source
									.lastModified()) {
						abort = !copy(files[i], destinationFile, true, move,
								listener);
						if (abort)
							return !abort;
					}
				}

		} else if (replaceAll || !destination.exists()
				|| destination.lastModified() < source.lastModified()) {
			abort = !copy(source, destination, true, move, listener);
			if (abort)
				return !abort;
		}
		if (source.isDirectory() && recursive && move)
			abort = !delete(source, false, listener);
		return !abort;
	}

	public static boolean copy(File source, File destination,
			boolean preserveTimestamp, boolean move,
			FileOperationListener listener) throws FileNotFoundException,
			IOException {
		boolean abort = false;
		if (listener != null)
			abort = !listener.processFileOperationEvent(new FileOperationEvent(
					FileUtils.class, source, 2));
		if (abort)
			return !abort;
		FileInputStream fis = new FileInputStream(source);
		FileOutputStream fos = new FileOutputStream(destination);
		byte buffer[] = new byte[0x10000];
		for (int i = 0; (i = fis.read(buffer)) != -1;)
			fos.write(buffer, 0, i);

		fos.close();
		fis.close();
		if (preserveTimestamp)
			destination.setLastModified(source.lastModified());
		if (move) {
			abort = !listener.processFileOperationEvent(new FileOperationEvent(
					FileUtils.class, source, 3));
			if (abort)
				return !abort;
			source.delete();
		}
		return !abort;
	}

	public static boolean delete(File file, boolean recursive,
			FileOperationListener listener) {
		boolean abort = false;
		File files[];
		if (file.isDirectory() && (files = file.listFiles()) != null) {
			for (int i = 0; i < files.length; i++)
				if (files[i].isDirectory() && recursive) {
					abort = !delete(files[i], recursive, listener);
					if (abort)
						return !abort;
				} else {
					if (listener != null)
						abort = !listener
								.processFileOperationEvent(new FileOperationEvent(
										FileUtils.class, files[i], 3));
					if (abort)
						return !abort;
					files[i].delete();
				}

		}
		if (listener != null)
			abort = !listener.processFileOperationEvent(new FileOperationEvent(
					FileUtils.class, file, 3));
		if (abort) {
			return !abort;
		} else {
			file.delete();
			return !abort;
		}
	}

	public static boolean determineProperties(FileProperties props, File file,
			boolean recursive, FileOperationListener listener)
			throws IOException {
		boolean abort = false;
		if (!file.exists())
			throw new IOException((new StringBuilder("File not found: "))
					.append(file.getAbsolutePath()).toString());
		if (props.location == null)
			props.location = file.getParent();
		File files[];
		if (file.isDirectory() && recursive
				&& (files = file.listFiles()) != null && files.length > 0) {
			props.directories++;
			for (int i = 0; i < files.length; i++)
				if (files[i].isDirectory() && recursive) {
					abort = !determineProperties(props, files[i], recursive,
							listener);
					if (abort)
						return !abort;
				} else if (files[i].isFile()) {
					props.files++;
					props.size += files[i].length();
					if (listener != null)
						abort = !listener
								.processFileOperationEvent(new FileOperationEvent(
										props, file, 4));
					if (abort)
						return !abort;
				}

		} else {
			if (file.isDirectory()) {
				props.directories++;
			} else {
				props.files++;
				props.size += file.length();
			}
			props.lastModified = file.lastModified();
			if (listener != null)
				abort = !listener
						.processFileOperationEvent(new FileOperationEvent(
								props, file, 4));
		}
		return !abort;
	}

	public FileUtils() {
	}
}
