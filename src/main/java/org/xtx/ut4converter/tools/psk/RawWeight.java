package org.xtx.ut4converter.tools.psk;

import org.xtx.ut4converter.tools.BinUtils;
import org.xtx.ut4converter.tools.psk.PSKStaticMesh.BinReadWrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 
 * @author XtremeXp
 *
 */
class RawWeight implements BinReadWrite {

	private static final Logger logger = LoggerFactory.getLogger(RawWeight.class);

	protected static final int DATA_SIZE = 12;
	
	private float weight;
	private int pointIndex;
	private int boneIndex;

	public RawWeight(ByteBuffer bf) {
		try {
			read(bf);
		} catch (Exception e) {
			logger.error("Error reading RawWeight", e);
		}
	}

	public void read(ByteBuffer bf) {
		weight = bf.getFloat();
		pointIndex = bf.getInt();
		boneIndex = bf.getInt();
	}

	public void write(FileOutputStream bos) throws IOException {
		BinUtils.writeFloat(bos, weight);
		BinUtils.writeInt(bos, pointIndex);
		BinUtils.writeInt(bos, boneIndex);
	}
}
