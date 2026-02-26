package org.xtx.ut4converter.t3d;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xtx.ut4converter.MapConverter;
import org.xtx.ut4converter.UTGames;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class T3DEndToEndTest {

    @Test
    void testReadConvertWriteSimpleActor() throws IOException, URISyntaxException {
        final File t3d = new File(Objects.requireNonNull(T3DEndToEndTest.class.getResource("/meshes/Cube256.t3d")).toURI());
        final MapConverter mc = T3DTestUtils.getMapConverterInstance(UTGames.UTGame.UT99, UTGames.UTGame.UT4);

        final File out = File.createTempFile("cube", "out");
        T3DLevelConvertor lc = new T3DLevelConvertor(t3d, out, mc, true);
        lc.setNoUi(true);
        lc.readConvertAndWrite();

        Assertions.assertFalse(lc.getConvertedActors().isEmpty());

        out.delete();
    }
}
