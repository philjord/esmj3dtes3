package esmj3dtes3.data.records;

import java.util.ArrayList;

import esmj3d.data.shared.records.RECO;
import esmj3dtes3.data.subrecords.DATA;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;

public class BODY extends RECO
{

	public DATA DATA;

	public BODY(Record recordData)
	{
		super(recordData);

		ArrayList<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getData();

			if (sr.getType().equals("BYDT"))
			{
				DATA = new DATA(bs);
			}

			else
			{
				System.out.println("unhandled : " + sr.getType() + " in record " + recordData + " in " + this);
			}
		}
	}

}
