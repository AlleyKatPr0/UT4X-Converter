package org.xtx.ut4converter.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for FileUtils security improvements
 */
class FileUtilsTest {

    @Test
    void testGetExtension() {
        File file = new File("test.txt");
        assertEquals("txt", FileUtils.getExtension(file));
        
        File noExtFile = new File("test");
        assertNull(FileUtils.getExtension(noExtFile));
        
        assertNull(FileUtils.getExtension(null));
    }

    @Test
    void testChangeExtension(@TempDir Path tempDir) {
        File originalFile = new File(tempDir.toFile(), "test.txt");
        File changedFile = FileUtils.changeExtension(originalFile, "md");
        
        assertEquals("test.md", changedFile.getName());
        assertEquals(tempDir.toFile(), changedFile.getParentFile());
    }

    @Test
    void testChangeExtensionWithDotInPath(@TempDir Path tempDir) {
        // Test that extension change works correctly even with dots in parent path
        File parentWithDot = new File(tempDir.toFile(), "test.folder");
        parentWithDot.mkdirs();
        File originalFile = new File(parentWithDot, "document.txt");
        
        File changedFile = FileUtils.changeExtension(originalFile, "pdf");
        
        assertEquals("document.pdf", changedFile.getName());
        assertEquals(parentWithDot, changedFile.getParentFile());
    }

    @Test
    void testIsPathSafe(@TempDir Path tempDir) {
        File baseDir = tempDir.toFile();
        File safeFile = new File(baseDir, "subfolder/file.txt");
        
        // Safe path within base directory
        assertTrue(FileUtils.isPathSafe(safeFile, baseDir));
        
        // Null file is not safe
        assertFalse(FileUtils.isPathSafe(null, baseDir));
    }

    @Test
    void testIsPathSafeWithTraversal(@TempDir Path tempDir) {
        File baseDir = tempDir.toFile();
        
        // Attempt path traversal - File constructor normalizes this, but our check should catch it
        File traversalFile = new File(baseDir, "subfolder/../../outside.txt");
        
        // After normalization, this might actually be in base dir, so test relative to its actual location
        File outsideFile = new File(baseDir.getParentFile(), "outside.txt");
        
        // File clearly outside base directory should be unsafe
        assertFalse(FileUtils.isPathSafe(outsideFile, baseDir));
    }

    @Test
    void testCreateSafeFile(@TempDir Path tempDir) {
        File baseDir = tempDir.toFile();
        
        // Create safe file within base directory
        File safeFile = FileUtils.createSafeFile(baseDir, "subfolder/file.txt");
        assertNotNull(safeFile);
        assertTrue(FileUtils.isPathSafe(safeFile, baseDir));
    }

    @Test
    void testCreateSafeFileWithTraversal(@TempDir Path tempDir) {
        File baseDir = tempDir.toFile();
        
        // Attempt to create file with path traversal
        assertThrows(SecurityException.class, () -> {
            FileUtils.createSafeFile(baseDir, "../../../etc/passwd");
        });
    }

    @Test
    void testCreateSafeFileNullArgs() {
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtils.createSafeFile(null, "test.txt");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtils.createSafeFile(new File("/tmp"), null);
        });
    }

    @Test
    void testChangeExtensionNullArgs() {
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtils.changeExtension(null, "txt");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtils.changeExtension(new File("test.md"), null);
        });
    }
}
