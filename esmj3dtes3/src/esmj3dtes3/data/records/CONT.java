package esmj3dtes3.data.records;

import java.util.ArrayList;

import esmj3d.data.shared.records.GenericCONT;
import esmj3d.data.shared.subrecords.FormID;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.subrecords.DATA;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;

public class CONT extends GenericCONT
{
	public DATA DATA;

	public FormID SCRI;

	public byte[] MNAM = null;

	public CONT(Record recordData)
	{

		super(recordData);

		ArrayList<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getData();

			if (sr.getType().equals("NAME"))
			{
				EDID = new ZString(bs);
			}
			else if (sr.getType().equals("FNAM"))
			{
				FULL = new LString(bs);
			}
			else if (sr.getType().equals("CNDT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getType().equals("FLAG"))
			{
				SCRI = new FormID(bs);
			}
			else if (sr.getType().equals("NPCO"))
			{

			}
			else
			{
				//System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}

		}
	}

}
