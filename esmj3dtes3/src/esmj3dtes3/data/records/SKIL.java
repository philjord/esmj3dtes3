package esmj3dtes3.data.records;

import java.util.List;

import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.subrecords.DATA;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;

public class SKIL extends RECO
{

	public ZString EDID = null;

	public DATA DATA;

	public ZString DESC = null;

	public SKIL(Record recordData)
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
			else if (sr.getSubrecordType().equals("SKDT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getSubrecordType().equals("DESC"))
			{
				DESC = new ZString(bs);
			}

			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}

		}
	}
}
