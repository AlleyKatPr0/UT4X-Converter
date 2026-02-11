package org.xtx.ut4converter.export;

import org.xtx.ut4converter.MapConverter;
import org.xtx.ut4converter.tools.Installation;
import org.xtx.ut4converter.ucore.UPackage;
import org.xtx.ut4converter.ucore.UPackageRessource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * Simple texture extractor once i had done with UT3 converter probably compiled
 * from partial delphi package unit sources
 * "<a href="http://www.acordero.org/projects/unreal-tournament-package-delphi-library/">...</a>"
 * but can't find my original sources ..
 * This is the ONLY one working for texture extraction from Unreal 2
 *
 * @author XtremeXp
 *
 */
public class SimpleTextureExtractor extends UTPackageExtractor {

	public SimpleTextureExtractor(MapConverter mapConverter) {
		super(mapConverter);
	}

	/**
	 * Get command for exporting texture package with this extractor
	 *
	 * @param texturePackageFile UE1/2 texture package to export
	 * @param outputFolder       Output folder
	 * @return Command array (for secure ProcessBuilder usage)
	 */
	public static String[] getCommandArray(final File texturePackageFile, final File outputFolder) {
		return new String[]{
			Installation.getExtractTextures().getAbsolutePath(),
			texturePackageFile.getAbsolutePath(),
			outputFolder.getAbsolutePath()
		};
	}

	/**
	 * @deprecated Use getCommandArray() instead to prevent command injection
	 */
	@Deprecated
	public static String getCommand(final File texturePackageFile, final File outputFolder) {
		return "\"" + Installation.getExtractTextures().getAbsolutePath() + "\"  \"" + texturePackageFile + "\" \"" + outputFolder + "\"";
	}

	@Override
	public Set<File> extract(UPackageRessource ressource, boolean forceExport, boolean perfectMatchOnly) throws IOException, InterruptedException {

		// Ressource ever extracted, we skip ...
		if ((!forceExport && ressource.isExported()) || ressource.getUnrealPackage().getName().equals("null") || (!forceExport && ressource.getUnrealPackage().isExported())) {
			return null;
		}

		String[] commandArray = getCommandArray(
			ressource.getUnrealPackage().getFileContainer(mapConverter), 
			mapConverter.getTempExportFolder()
		);

		List<String> logLines = new ArrayList<>();

		logger.log(Level.INFO, "Exporting " + ressource.getUnrealPackage().getFileContainer(mapConverter).getName() + " with " + getName());

		Installation.executeProcess(commandArray, logLines, logger, Level.FINE, null);

		ressource.getUnrealPackage().setExported(true);

		for (String logLine : logLines) {

			logger.log(Level.FINE, logLine);

			/*
			 * Analyzing package Glands.utx... Extracting texture
			 * GrssFlorU08J012... OK Extracting texture MetlWall_U06B441_new...
			 * OK
			 */
			if (logLine.trim().startsWith("Extracting")) {
				parseRessourceExported(logLine, ressource.getUnrealPackage());
			}

		}

		return null;
	}

	private void parseRessourceExported(String logLine, UPackage unrealPackage) {

		// Add bounds checking to prevent ArrayIndexOutOfBoundsException
		String[] textureParts = logLine.split("texture ");
		if (textureParts.length < 2) {
			logger.log(Level.WARNING, "Could not parse texture name from log line: " + logLine);
			return;
		}
		
		String[] nameParts = textureParts[1].split("\\.");
		if (nameParts.length < 1) {
			logger.log(Level.WARNING, "Could not parse texture name from: " + textureParts[1]);
			return;
		}
		
		String name = nameParts[0];
		// not sharing group info unfortunately ..

		File exportedFile = new File(mapConverter.getTempExportFolder().getAbsolutePath() + File.separator + name + ".bmp");

		name = unrealPackage.getName() + "." + name;

		UPackageRessource uRessource = unrealPackage.findRessource(name);

		if (uRessource != null) {
			uRessource.getExportInfo().setExportedFile(exportedFile);
			// uRessource.parseNameAndGroup(ressourceName); // for texture db
			// that don't have group we retrieve the group ...
		} else {
			final List<File> exportedFiles = new ArrayList<>();
			exportedFiles.add(exportedFile);
			new UPackageRessource(mapConverter, name, unrealPackage, exportedFiles, this);
		}
	}

	@Override
	public File getExporterPath() {
		return Installation.getExtractTextures();
	}

	@Override
	public String getName() {
		return "Simple Texture Extractor";
	}

	@Override
	public boolean supportLinux() {
		return false;
	}

}
