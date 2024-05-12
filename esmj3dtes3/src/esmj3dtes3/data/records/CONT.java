package esmj3dtes3.data.records;

import java.util.List;

import esfilemanager.common.data.record.Record;
import esfilemanager.common.data.record.Subrecord;
import esmj3d.data.shared.records.GenericCONT;
import esmj3d.data.shared.subrecords.FormID;
import esmj3d.data.shared.subrecords.LString;

import esmj3dtes3.data.subrecords.DATA;

public class CONT extends GenericCONT
{
	public DATA DATA;

	public FormID SCRI;

	public byte[] MNAM = null;

	public CONT(Record recordData)
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
			else if (sr.getSubrecordType().equals("CNDT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getSubrecordType().equals("FLAG"))
			{
				SCRI = new FormID(bs);
			}
			else if (sr.getSubrecordType().equals("NPCO"))
			{

			}
			else
			{
				//System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}

		}
	}

}
