package esmj3dtes3.data.records;

import java.util.ArrayList;

import esmLoader.common.data.record.Record;
import esmLoader.common.data.record.Subrecord;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.subrecords.DATA;

public class ALCH extends RECO
{
	public ZString EDID;

	public LString FULL;

	public DATA DATA;

	public MODL MODL;

	public ZString ICON;

	public ALCH(Record recordData)
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
			else if (sr.getType().equals("MODL"))
			{
				MODL = new MODL(bs);
			}
			else if (sr.getType().equals("FNAM"))
			{
				FULL = new LString(bs);
			}
			else if (sr.getType().equals("ALDT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getType().equals("ENAM"))
			{

			}
			else if (sr.getType().equals("ITEX"))
			{
				ICON = new ZString(bs);
			}
			else if (sr.getType().equals("SCRI"))
			{

			}
			else if (sr.getType().equals("TEXT"))
			{

			}
			else
			{
				System.out.println("unhandled : " + sr.getType() + " in record " + recordData + " in " + this);
			}
		}
	}

	public String showDetails()
	{
		return "ALCH : (" + formId + "|" + Integer.toHexString(formId) + ") " + EDID.str;
	}

}
