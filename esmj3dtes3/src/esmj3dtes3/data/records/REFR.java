package esmj3dtes3.data.records;

import java.util.ArrayList;

import tools.io.ESMByteConvert;
import esmLoader.common.data.record.Record;
import esmLoader.common.data.record.Subrecord;
import esmj3d.data.shared.records.CommonREFR;
import esmj3d.data.shared.subrecords.ZString;

public class REFR extends CommonREFR
{
	public int FRMR = 0;

	public ZString NAMEref;

	public REFR(Record recordData)
	{
		super(recordData, false);

		ArrayList<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getData();

			if (sr.getType().equals("FRMR"))
			{
				FRMR = ESMByteConvert.extractInt(bs, 0);
			}
			else if (sr.getType().equals("NAME"))
			{
				NAMEref = new ZString(bs);
			}
			else if (sr.getType().equals("XSCL"))
			{
				scale = ESMByteConvert.extractFloat(bs, 0);
			}
			else if (sr.getType().equals("DATA"))
			{
				this.extractInstData(bs);
			}
		}

	}

}
