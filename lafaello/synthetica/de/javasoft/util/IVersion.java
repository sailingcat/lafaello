package de.javasoft.util;

public interface IVersion {

	public abstract int getBuild();

	public abstract int getMajor();

	public abstract int getMinor();

	public abstract int getRevision();

	public abstract String toString();
}
