package esmj3dtes3.data.records;

import java.util.ArrayList;

import esmLoader.common.data.record.Record;
import esmLoader.common.data.record.Subrecord;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.ZString;

public class BSGN extends RECO
{
	public ZString EDID = null;
	public ZString FULL = null;
	public ZString ICON = null;
	public ZString DESC = null;

	public BSGN(Record recordData)
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
				FULL = new ZString(bs);
			}
			else if (sr.getType().equals("TNAM"))
			{
				ICON = new ZString(bs);
			}
			else if (sr.getType().equals("DESC"))
			{
				DESC = new ZString(bs);
			}
			else if (sr.getType().equals("NPCS"))
			{

			}
			else
			{
				System.out.println("unhandled : " + sr.getType() + " in record " + recordData + " in " + this);
			}

		}
	}
}
