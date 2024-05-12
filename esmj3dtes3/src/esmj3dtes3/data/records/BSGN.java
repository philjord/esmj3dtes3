package esmj3dtes3.data.records;

import java.util.List;

import esfilemanager.common.data.record.Record;
import esfilemanager.common.data.record.Subrecord;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.ZString;


public class BSGN extends RECO
{
	
	public String FULL = null;
	public String ICON = null;
	public String DESC = null;

	public BSGN(Record recordData)
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
				FULL = ZString.toString(bs);
			}
			else if (sr.getSubrecordType().equals("TNAM"))
			{
				ICON = ZString.toString(bs);
			}
			else if (sr.getSubrecordType().equals("DESC"))
			{
				DESC = ZString.toString(bs);
			}
			else if (sr.getSubrecordType().equals("NPCS"))
			{

			}
			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}

		}
	}
}
