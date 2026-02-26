/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtx.ut4converter.tools;

import org.apache.commons.io.Charsets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author XtremeXp
 */
public class FileUtils {

	/**
	 * Validates that a file path does not attempt path traversal and stays within the allowed base directory.
	 * This helps prevent path traversal attacks.
	 * 
	 * @param file File to validate
	 * @param allowedBaseDir Base directory that file must be within (can be null to skip base check)
	 * @return true if path is safe, false otherwise
	 */
	public static boolean isPathSafe(File file, File allowedBaseDir) {
		if (file == null) {
			return false;
		}

		try {
			// Normalize and resolve the path to handle .. and . references
			Path normalizedPath = file.toPath().normalize().toAbsolutePath();
			
			// Check if path attempts to escape using .. beyond root
			if (normalizedPath.toString().contains("..")) {
				return false;
			}

			// If base directory is specified, ensure file is within it
			if (allowedBaseDir != null) {
				Path basePath = allowedBaseDir.toPath().normalize().toAbsolutePath();
				return normalizedPath.startsWith(basePath);
			}

			return true;
		} catch (Exception e) {
			// If any exception occurs during validation, consider path unsafe
			return false;
		}
	}

	/**
	 * Creates a safe File object by combining base directory and relative path.
	 * This method prevents path traversal by normalizing paths.
	 * 
	 * @param baseDir Base directory
	 * @param relativePath Relative path to append
	 * @return Safe File object within base directory
	 * @throws SecurityException if path traversal is detected
	 */
	public static File createSafeFile(File baseDir, String relativePath) throws SecurityException {
		if (baseDir == null || relativePath == null) {
			throw new IllegalArgumentException("Base directory and relative path must not be null");
		}

		File resultFile = new File(baseDir, relativePath);
		
		if (!isPathSafe(resultFile, baseDir)) {
			throw new SecurityException("Path traversal detected: " + relativePath);
		}

		return resultFile;
	}

	public static String getExtension(File file) {

		if (file == null) {
			return null;
		}

		String[] s = file.getName().split("\\.");

		if (s.length < 2) {
			return null;
		}

		return s[s.length - 1];
	}

	/**
	 * Changes the file extension. Uses secure path manipulation to prevent path traversal.
	 * 
	 * @param file File to change extension
	 * @param newExtension New extension (without dot)
	 * @return New File with changed extension
	 */
	public static File changeExtension(File file, String newExtension) {

		if (file == null || newExtension == null) {
			throw new IllegalArgumentException("File and extension must not be null");
		}

		String fileName = file.getName();
		String baseName;
		
		int lastDotIdx = fileName.lastIndexOf('.');
		if (lastDotIdx > 0) {
			// File has an extension
			baseName = fileName.substring(0, lastDotIdx);
		} else {
			// File has no extension
			baseName = fileName;
		}

		// Ensure newExtension doesn't start with a dot
		String cleanExtension = newExtension.startsWith(".") ? newExtension.substring(1) : newExtension;
		
		// Reconstruct path safely using parent directory
		File parentDir = file.getParentFile();
		String newFileName = baseName + "." + cleanExtension;
		
		return parentDir != null ? new File(parentDir, newFileName) : new File(newFileName);
	}

	public static Charset detectEncoding(File file) throws IOException {

		if (file == null || !file.exists()) {
			throw new IllegalArgumentException("File not specified or invalid file");
		}

		try (final BufferedReader bfr = new BufferedReader(new FileReader(file))) {

			String line = bfr.readLine();
			if (line.contains("\0")) {
				return StandardCharsets.UTF_16;
			} else {
				return StandardCharsets.UTF_8;
			}
		}
	}
}
