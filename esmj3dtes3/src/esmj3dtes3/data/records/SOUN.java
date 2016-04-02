package esmj3dtes3.data.records;

import java.util.List;

import esmj3d.data.shared.records.GenericSOUN;
import esmj3dtes3.data.subrecords.DATA;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;

public class SOUN extends GenericSOUN
{

	public DATA DATA;

	public SOUN(Record recordData)
	{

		super(recordData);
		List<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getSubrecordData();

			if (sr.getSubrecordType().equals("DATA"))
			{
				DATA = new DATA(bs);
			}

		}
	}

}
