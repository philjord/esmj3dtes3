package esmj3dtes3.data.subrecords;

import tools.io.ESMByteConvert;

public class SNAM
{
	public int factionFormId;

	public byte rank;

	public SNAM(byte[] bytes)
	{
		factionFormId = ESMByteConvert.extractInt(bytes, 0);
		rank = ESMByteConvert.extractByte(bytes, 4);
		//3 more bytes possibly flag possibly 4 byte align padding 
	}
}
