package esmj3dtes3.data.records;

import java.util.List;

import esmio.common.data.record.Record;
import esmio.common.data.record.Subrecord;
import esmj3d.data.shared.records.CommonLIGH;
import esmj3d.data.shared.records.LAND.DATA;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.data.shared.subrecords.ZString;
import tools.io.ESMByteConvert;
import utils.ESConfig;

public class LIGH extends CommonLIGH
{
	public ZString EDID;

	public LString FULL;

	public DATA DATA;

	public ZString ICON;

	public ZString SNAM;

	public LIGH(Record recordData)
	{
		super(recordData);
		List<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getSubrecordData();

			if (sr.getSubrecordType().equals("NAME"))
			{
				EDID = new ZString(bs);
			}
			else if (sr.getSubrecordType().equals("FNAM"))
			{
				FULL = new LString(bs);
			}
			else if (sr.getSubrecordType().equals("LHDT"))
			{
				//System.out.println("" + ESMByteConvert.extractFloat(bs, 0)); weight
				//value = ESMByteConvert.extractInt(bs, 4);// value  
				//System.out.println("" + ESMByteConvert.extractInt(bs, 8)); time

				radius = ESMByteConvert.extractInt(bs, 12);  
				color.x = ESMByteConvert.extractUnsignedByte(bs, 16);
				color.y = ESMByteConvert.extractUnsignedByte(bs, 17);
				color.z = ESMByteConvert.extractUnsignedByte(bs, 18);

				//color.a = ESMByteConvert.extractUnsignedByte(bs, 19);
				//long  flags = ESMByteConvert.extractInt(bs, 20);
				//0x0001 = Dynamic
				//0x0002 = Can Carry
				//0x0004 = Negative
				//0x0008 = Flicker
				//0x0010 = Fire
				//0x0020 = Off Default
				//0x0040 = Flicker Slow
				//0x0080 = Pulse
				//0x0100 = Pulse Slow

				//https://imdoingitwrong.wordpress.com/2011/01/31/light-attenuation/
				// lin co = 2/r
				// quad co = 1/r^2

				float r = radius * ESConfig.ES_TO_METERS_SCALE;
				this.fade = 2f / r;
				this.falloffExponent = 1f / (r * r);
			}
			else if (sr.getSubrecordType().equals("SCPT"))
			{

			}
			else if (sr.getSubrecordType().equals("ITEX"))
			{
				ICON = new ZString(bs);
			}
			else if (sr.getSubrecordType().equals("MODL"))
			{
				MODL = new MODL(bs);
			}
			else if (sr.getSubrecordType().equals("SNAM"))
			{
				SNAM = new ZString(bs);
			}
			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}
		}
	}

}
