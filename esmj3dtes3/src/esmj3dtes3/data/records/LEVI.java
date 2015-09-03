package esmj3dtes3.data.records;

import java.util.ArrayList;

import tools.io.ESMByteConvert;
import esmLoader.common.data.record.Record;
import esmLoader.common.data.record.Subrecord;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.subrecords.DATA;

public class LEVI extends RECO
{
	public ZString EDID = null;

	public DATA DATA = null;

	public int numItems = 0;

	public ZString[] itemID;

	public int[] itemLevel;

	public LEVI(Record recordData)
	{
		super(recordData);

		ArrayList<Subrecord> subrecords = recordData.getSubrecords();
		Subrecord sr = subrecords.get(0);
		byte[] bs = sr.getData();

		if (sr.getType().equals("NAME"))
		{
			EDID = new ZString(bs);
		}
		sr = subrecords.get(1);
		bs = sr.getData();

		if (sr.getType().equals("DATA"))
		{
			DATA = new DATA(bs);
		}
		sr = subrecords.get(2);
		bs = sr.getData();
		if (sr.getType().equals("NNAM"))
		{

		}
		sr = subrecords.get(3);
		bs = sr.getData();
		if (sr.getType().equals("INDX"))
		{
			numItems = ESMByteConvert.extractInt(bs, 0);
		}
		itemID = new ZString[numItems];
		itemLevel = new int[numItems];
		for (int i = 0; i < numItems; i += 2)
		{
			sr = subrecords.get(4 + (i*2) + 0);
			bs = sr.getData();

			if (sr.getType().equals("INAM"))
			{
				itemID[i] = new ZString(bs);
			}
			sr = subrecords.get(4 + (i*2) + 1);
			bs = sr.getData();
			if (sr.getType().equals("INTV"))
			{
				itemLevel[i] = ESMByteConvert.extractShort(bs, 0);
			}
			else
			{
				System.out.println("unhandled : " + sr.getType() + " in record " + recordData + " in " + this);
			}

		}
	}

}
