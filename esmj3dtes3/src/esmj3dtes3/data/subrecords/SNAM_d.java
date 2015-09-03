package esmj3dtes3.data.subrecords;

import tools.io.ESMByteConvert;

public class SNAM_d
{
	public int sound;

	public SNAM_d(byte[] bytes)
	{
		sound = ESMByteConvert.extractInt(bytes, 0);
	}
}
