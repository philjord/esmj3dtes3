package esmj3dtes3.data.records;

import java.util.List;

import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.subrecords.DATA;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;
import tools.io.ESMByteConvert;

public class LEVC extends RECO
{
	public ZString EDID = null;

	public DATA DATA = null;

	public int numChars = 0;

	public ZString[] charID;

	public int[] charLevel;

	public LEVC(Record recordData)
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
			numChars = ESMByteConvert.extractInt(bs, 0);
		}
		charID = new ZString[numChars];
		charLevel = new int[numChars];
		for (int i = 0; i < numChars; i++)
		{
			sr = subrecords.get(4 + (i * 2) + 0);
			bs = sr.getSubrecordData();

			if (sr.getSubrecordType().equals("CNAM"))
			{
				charID[i] = new ZString(bs);
			}
			sr = subrecords.get(4 + (i * 2) + 1);
			bs = sr.getSubrecordData();
			if (sr.getSubrecordType().equals("INTV"))
			{
				charLevel[i] = ESMByteConvert.extractShort(bs, 0);
			}
			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}

		}
	}
}
