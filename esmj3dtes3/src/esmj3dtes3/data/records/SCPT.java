package esmj3dtes3.data.records;

import java.util.ArrayList;

import esmj3d.data.shared.records.RECO;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;

public class SCPT extends RECO
{

	public SCPT(Record recordData)
	{
		super(recordData);
		ArrayList<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getData();

			if (sr.getType().equals("SCHD"))
			{
			}
			else if (sr.getType().equals("SCVR"))
			{

			}
			else if (sr.getType().equals("SCDT"))
			{

			}
			else if (sr.getType().equals("SCTX"))
			{

			}

			else
			{
				System.out.println("unhandled : " + sr.getType() + " in record " + recordData + " in " + this);
			}

		}
	}
}
