package esmj3dtes3.data.records;

import java.util.ArrayList;

import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.subrecords.DATA;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;

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
		ArrayList<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getData();

			if (sr.getType().equals("NAME"))
			{
				EDID = new ZString(bs);
			}
			else if (sr.getType().equals("MEDT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getType().equals("ITEX"))
			{
				ICON = new ZString(bs);
			}
			else if (sr.getType().equals("PTEX"))
			{

			}
			else if (sr.getType().equals("CVFX"))
			{

			}
			else if (sr.getType().equals("BVFX"))
			{

			}
			else if (sr.getType().equals("HVFX"))
			{

			}
			else if (sr.getType().equals("AVFX"))
			{

			}
			else if (sr.getType().equals("DESC"))
			{
				DESC = new ZString(bs);
			}
			else if (sr.getType().equals("CSND"))
			{

			}
			else if (sr.getType().equals("BSND"))
			{

			}
			else if (sr.getType().equals("HSND"))
			{

			}
			else if (sr.getType().equals("ASND"))
			{

			}
			else
			{
				System.out.println("unhandled : " + sr.getType() + " in record " + recordData + " in " + this);
			}

		}
	}
}
