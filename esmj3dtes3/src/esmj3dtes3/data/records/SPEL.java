package esmj3dtes3.data.records;

import java.util.List;

import esfilemanager.common.data.record.Record;
import esfilemanager.common.data.record.Subrecord;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.MODL;

import esmj3dtes3.data.subrecords.DATA;

public class SPEL extends RECO
{

	

	public LString FULL;

	public MODL MODL = null;

	public DATA DATA;

	public SPEL(Record recordData)
	{
		super(recordData);
		List<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getSubrecordData();

			if (sr.getSubrecordType().equals("NAME"))
			{
				setEDID(bs);
			}
			else if (sr.getSubrecordType().equals("FNAM"))
			{
				FULL = new LString(bs);
			}
			else if (sr.getSubrecordType().equals("SPDT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getSubrecordType().equals("ENAM"))
			{

			}

			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}

		}
	}
}
