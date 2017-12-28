package esmj3dtes3.data.records;

import java.util.List;

import esmio.common.data.record.Record;
import esmio.common.data.record.Subrecord;
import esmj3d.data.shared.records.GenericSOUN;

public class SOUN extends GenericSOUN
{

	public byte Volume = 0;// (0=0.00, 255=1.00)
	public byte MinRange = 0;
	public byte MaxRange = 0;

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
				Volume = bs[0];
				MinRange = bs[1];
				MaxRange = bs[2];
			}

		}
	}

}
