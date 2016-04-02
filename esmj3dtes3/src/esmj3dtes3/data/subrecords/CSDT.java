package esmj3dtes3.data.subrecords;

import esmj3d.data.shared.subrecords.FormID;
import tools.io.ESMByteConvert;

public class CSDT
{
	public int soundType;

	public FormID csdi;

	public CSDC csdc;

	public CSDT(byte[] bytes)
	{
		soundType = ESMByteConvert.extractInt(bytes, 0);
	}
}
