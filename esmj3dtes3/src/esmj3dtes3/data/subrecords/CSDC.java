package esmj3dtes3.data.subrecords;

import tools.io.ESMByteConvert;

public class CSDC
{
	public byte chance;

	public CSDC(byte[] bytes)
	{
		chance = ESMByteConvert.extractByte(bytes, 0);
	}
}
