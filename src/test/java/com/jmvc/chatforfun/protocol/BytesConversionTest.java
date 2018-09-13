package com.jmvc.chatforfun.protocol;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author JMVC
 *
 */
class BytesConversionTest {
	
	private static final String BIG_ENDIAN = "be";
	private static final String LITTLE_ENDIAN = "le";
	
	@Test
	void test()
	{
		byte bytes[] = new byte[4];
		int numberBE = 0xFF000000 | 0x00BB0000 | 0x0000BD00 | 0x000000AC;
		int numberLE = 0xAC000000 | 0x00BD0000 | 0x0000BB00 | 0x000000FF;
		int result = 0;
		
		/* Big Endian integer to bytes Test */
		ProtocolUtils.int32ToBytes(numberBE, bytes, BIG_ENDIAN);

		assertTrue((byte)0xFF == bytes[0]);
		assertTrue((byte)0xBB == bytes[1]);
		assertTrue((byte)0xBD == bytes[2]);
		assertTrue((byte)0xAC == bytes[3]);
			
		
		/* Little Endian integer to bytes Test */
		ProtocolUtils.int32ToBytes(numberBE, bytes, LITTLE_ENDIAN);

		assertTrue((byte)0xFF == bytes[3]);
		assertTrue((byte)0xBB == bytes[2]);
		assertTrue((byte)0xBD == bytes[1]);
		assertTrue((byte)0xAC == bytes[0]);
		
		/* Big Endian bytes to integer Test */		
		result = ProtocolUtils.bytesToInt32(bytes, BIG_ENDIAN);
		
		assertTrue(numberLE == result);
		
		/* Little Endian bytes to integer Test */		
		result = ProtocolUtils.bytesToInt32(bytes, LITTLE_ENDIAN);
		
		assertTrue(numberBE == result);
	}

}
