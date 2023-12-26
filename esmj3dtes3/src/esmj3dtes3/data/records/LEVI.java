package esmj3dtes3.data.records;

import java.util.List;

import esfilemanager.common.data.record.Record;
import esfilemanager.common.data.record.Subrecord;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.subrecords.DATA;
import tools.io.ESMByteConvert;

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

		List<Subrecord> subrecords = recordData.getSubrecords();
		Subrecord sr = subrecords.get(0);
		byte[] bs = sr.getSubrecordData();

		if (sr.getSubrecordType().equals("NAME"))
		{
			EDID = new ZString(bs);
		}
		sr = subrecords.get(1);
		bs = sr.getSubrecordData();

		if (sr.getSubrecordType().equals("DATA"))
		{
			DATA = new DATA(bs);
		}
		sr = subrecords.get(2);
		bs = sr.getSubrecordData();
		if (sr.getSubrecordType().equals("NNAM"))
		{

		}
		sr = subrecords.get(3);
		bs = sr.getSubrecordData();
		if (sr.getSubrecordType().equals("INDX"))
		{
			numItems = ESMByteConvert.extractInt(bs, 0);
		}
		itemID = new ZString[numItems];
		itemLevel = new int[numItems];
		for (int i = 0; i < numItems; i++)
		{
			sr = subrecords.get(4 + (i * 2) + 0);
			bs = sr.getSubrecordData();

			if (sr.getSubrecordType().equals("INAM"))
			{
				itemID[i] = new ZString(bs);
			}
			sr = subrecords.get(4 + (i * 2) + 1);
			bs = sr.getSubrecordData();
			if (sr.getSubrecordType().equals("INTV"))
			{
				itemLevel[i] = ESMByteConvert.extractShort(bs, 0);
			}
			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}

		}
	}

}
