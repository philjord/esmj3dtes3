package esmj3dtes3.data.subrecords;

import tools.io.ESMByteConvert;

public class FNAM_b
{
	public int faceRace;

	public FNAM_b(byte[] bytes)
	{
		faceRace = ESMByteConvert.extractShort(bytes, 0);
	}
}
