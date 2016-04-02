package esmj3dtes3.data.records;

import java.util.List;

import esmj3d.data.shared.records.RECO;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;

public class SCPT extends RECO
{

	public SCPT(Record recordData)
	{
		super(recordData);
		List<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getSubrecordData();

			if (sr.getSubrecordType().equals("SCHD"))
			{
			}
			else if (sr.getSubrecordType().equals("SCVR"))
			{

			}
			else if (sr.getSubrecordType().equals("SCDT"))
			{

			}
			else if (sr.getSubrecordType().equals("SCTX"))
			{

			}

			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}

		}
	}
}
