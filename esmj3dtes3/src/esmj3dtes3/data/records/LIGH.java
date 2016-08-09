package esmj3dtes3.data.records;

import java.util.List;

import esmj3d.data.shared.records.CommonLIGH;
import esmj3d.data.shared.records.LAND.DATA;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.data.shared.subrecords.ZString;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;
import tools.io.ESMByteConvert;

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
				fade = ESMByteConvert.extractInt(bs, 4);// value fade?
				//System.out.println("" + ESMByteConvert.extractInt(bs, 8)); time

				radius = ESMByteConvert.extractInt(bs, 12);
				color.x = ESMByteConvert.extractUnsignedByte(bs, 16);
				color.y = ESMByteConvert.extractUnsignedByte(bs, 17);
				color.z = ESMByteConvert.extractUnsignedByte(bs, 18);
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
			else if (sr.getSubrecordType().equals("SCRI"))
			{

			}
			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}
		}
	}

}
