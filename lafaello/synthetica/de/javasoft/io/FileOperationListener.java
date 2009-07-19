package de.javasoft.io;

import java.util.EventListener;

public interface FileOperationListener extends EventListener {

	public abstract boolean processFileOperationEvent(
			FileOperationEvent fileoperationevent);
}
