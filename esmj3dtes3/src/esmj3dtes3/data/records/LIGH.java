package esmj3dtes3.data.records;

import java.util.ArrayList;

import tools.io.ESMByteConvert;
import esmj3d.data.shared.records.CommonLIGH;
import esmj3d.data.shared.records.LAND.DATA;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.data.shared.subrecords.ZString;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;

public class LIGH extends CommonLIGH
{
	public ZString EDID;

	public LString FULL;

	public DATA DATA;

	public ZString ICON;

	public LIGH(Record recordData)
	{
		super(recordData);
		ArrayList<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getData();

			if (sr.getType().equals("NAME"))
			{
				EDID = new ZString(bs);
			}
			else if (sr.getType().equals("FNAM"))
			{
				FULL = new LString(bs);
			}
			else if (sr.getType().equals("LHDT"))
			{
				//System.out.println("" + ESMByteConvert.extractFloat(bs, 0)); weight
				fade = ESMByteConvert.extractInt(bs, 4);// value fade?
				//System.out.println("" + ESMByteConvert.extractInt(bs, 8)); time

				radius = ESMByteConvert.extractInt(bs, 12);
				color.x = ESMByteConvert.extractUnsignedByte(bs, 16);
				color.y = ESMByteConvert.extractUnsignedByte(bs, 17);
				color.z = ESMByteConvert.extractUnsignedByte(bs, 18);
			}
			else if (sr.getType().equals("SCPT"))
			{

			}
			else if (sr.getType().equals("ITEX"))
			{
				ICON = new ZString(bs);
			}
			else if (sr.getType().equals("MODL"))
			{
				MODL = new MODL(bs);
			}
			else if (sr.getType().equals("SNAM"))
			{

			}
			else if (sr.getType().equals("SCRI"))
			{

			}
			else
			{
				System.out.println("unhandled : " + sr.getType() + " in record " + recordData + " in " + this);
			}
		}
	}

}
