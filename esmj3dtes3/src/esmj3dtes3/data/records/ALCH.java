package esmj3dtes3.data.records;

import java.util.List;

import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.subrecords.DATA;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;

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

		List<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getSubrecordData();

			if (sr.getSubrecordType().equals("NAME"))
			{
				EDID = new ZString(bs);
			}
			else if (sr.getSubrecordType().equals("MODL"))
			{
				MODL = new MODL(bs);
			}
			else if (sr.getSubrecordType().equals("FNAM"))
			{
				FULL = new LString(bs);
			}
			else if (sr.getSubrecordType().equals("ALDT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getSubrecordType().equals("ENAM"))
			{

			}
			else if (sr.getSubrecordType().equals("ITEX"))
			{
				ICON = new ZString(bs);
			}
			else if (sr.getSubrecordType().equals("SCRI"))
			{

			}
			else if (sr.getSubrecordType().equals("TEXT"))
			{

			}
			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}
		}
	}

	public String showDetails()
	{
		return "ALCH : (" + formId + "|" + Integer.toHexString(formId) + ") " + EDID.str;
	}

}
