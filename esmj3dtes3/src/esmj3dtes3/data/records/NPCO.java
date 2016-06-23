package esmj3dtes3.data.records;

import tools.io.ESMByteConvert;

public class NPCO
{
	public int count = 0;
	public String itemName = "";

	public NPCO(byte[] bs)
	{
		count = ESMByteConvert.extractInt(bs, 0);
		itemName = new String(bs, 4, 32).trim(); // careful this is probably a null character termination here
	}

}
