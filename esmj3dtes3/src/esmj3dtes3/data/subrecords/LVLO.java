package esmj3dtes3.data.subrecords;

import tools.io.ESMByteConvert;

public class LVLO
{
	public int level;

	public int unknown1;

	public int itemFormId;

	public int count;

	public int unknown2;

	public LVLO(byte[] bytes)
	{
		level = ESMByteConvert.extractShort(bytes, 0);
		unknown1 = ESMByteConvert.extractShort(bytes, 2);
		itemFormId = ESMByteConvert.extractInt(bytes, 4);

		if (bytes.length == 12)
		{
			count = ESMByteConvert.extractShort(bytes, 8);
			unknown2 = ESMByteConvert.extractShort(bytes, 10);
		}
	}
}
