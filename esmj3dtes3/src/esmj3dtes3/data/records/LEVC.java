package esmj3dtes3.data.records;

import java.util.List;

import esfilemanager.common.data.record.Record;
import esfilemanager.common.data.record.Subrecord;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.subrecords.DATA;
import tools.io.ESMByteConvert;

public class LEVC extends RECO
{
	

	public DATA DATA = null;

	public int numChars = 0;

	public String[] charID;

	public int[] charLevel;

	public LEVC(Record recordData)
	{
		super(recordData);

		List<Subrecord> subrecords = recordData.getSubrecords();

		Subrecord sr = subrecords.get(0);
		byte[] bs = sr.getSubrecordData();

		if (sr.getSubrecordType().equals("NAME"))
		{
			setEDID(bs);
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
		charID = new String[numChars];
		charLevel = new int[numChars];
		for (int i = 0; i < numChars; i++)
		{
			sr = subrecords.get(4 + (i * 2) + 0);
			bs = sr.getSubrecordData();

			if (sr.getSubrecordType().equals("CNAM"))
			{
				charID[i] = ZString.toString(bs);
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
