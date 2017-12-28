package esmj3dtes3.data.records;

import java.util.List;

import esmio.common.data.record.Record;
import esmio.common.data.record.Subrecord;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.subrecords.DATA;

public class MGEF extends RECO
{

	public ZString EDID = null;

	public MODL MODL = null;

	public DATA DATA;

	public ZString ICON;
	
	public ZString DESC = null;

	public MGEF(Record recordData)
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
			else if (sr.getSubrecordType().equals("MEDT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getSubrecordType().equals("ITEX"))
			{
				ICON = new ZString(bs);
			}
			else if (sr.getSubrecordType().equals("PTEX"))
			{

			}
			else if (sr.getSubrecordType().equals("CVFX"))
			{

			}
			else if (sr.getSubrecordType().equals("BVFX"))
			{

			}
			else if (sr.getSubrecordType().equals("HVFX"))
			{

			}
			else if (sr.getSubrecordType().equals("AVFX"))
			{

			}
			else if (sr.getSubrecordType().equals("DESC"))
			{
				DESC = new ZString(bs);
			}
			else if (sr.getSubrecordType().equals("CSND"))
			{

			}
			else if (sr.getSubrecordType().equals("BSND"))
			{

			}
			else if (sr.getSubrecordType().equals("HSND"))
			{

			}
			else if (sr.getSubrecordType().equals("ASND"))
			{

			}
			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}

		}
	}
}
