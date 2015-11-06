package esmj3dtes3.data.records;

import java.util.ArrayList;

import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.subrecords.DATA;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;

public class RACE extends RECO
{
	public ZString EDID = null;

	public LString FULL;

	public DATA DATA;

	public ZString DESC = null;

	public RACE(Record recordData)
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
			else if (sr.getType().equals("RADT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getType().equals("NPCS"))
			{

			}
			else if (sr.getType().equals("DESC"))
			{
				DESC = new ZString(bs);
			}

			else
			{
				System.out.println("unhandled : " + sr.getType() + " in record " + recordData + " in " + this);
			}

		}
	}

}
