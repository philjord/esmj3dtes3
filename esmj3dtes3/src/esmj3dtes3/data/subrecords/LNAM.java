package esmj3dtes3.data.subrecords;

import tools.io.ESMByteConvert;

public class LNAM
{
	public float hairLength;

	public LNAM(byte[] bytes)
	{
		hairLength = ESMByteConvert.extractFloat(bytes, 0);
	}
}
