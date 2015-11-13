package esmj3dtes3.data.records;

import java.util.ArrayList;

import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.subrecords.DATA;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;

public class ARMO extends RECO
{
	public ZString EDID;

	public LString FULL = null;

	public MODL MODL = null;

	public DATA DATA;

	public ZString ICON;

	public ARMO(Record recordData)
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
			else if (sr.getType().equals("MODL"))
			{
				MODL = new MODL(bs);
			}
			else if (sr.getType().equals("FNAM"))
			{
				FULL = new LString(bs);
			}
			else if (sr.getType().equals("AODT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getType().equals("ITEX"))
			{
				ICON = new ZString(bs);
			}
			else if (sr.getType().equals("INDX"))
			{

			}
			else if (sr.getType().equals("BNAM"))
			{
				//modely string
				//System.out.println("BNAM " + new String(bs));
			}
			else if (sr.getType().equals("CNAM"))
			{

			}
			else if (sr.getType().equals("SCRI"))
			{

			}
			else if (sr.getType().equals("ENAM"))
			{

			}

			else
			{
				System.out.println("unhandled : " + sr.getType() + " in record " + recordData + " in " + this);
			}
		}
	}

}
