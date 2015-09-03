package esmj3dtes3.data.subrecords;

import tools.io.ESMByteConvert;

public class HCLR
{
	public byte red;

	public byte green;

	public byte blue;

	public byte custom;

	public HCLR(byte[] bytes)
	{
		red = ESMByteConvert.extractByte(bytes, 0);
		green = ESMByteConvert.extractByte(bytes, 0);
		blue = ESMByteConvert.extractByte(bytes, 0);
		custom = ESMByteConvert.extractByte(bytes, 0);
	}
}
