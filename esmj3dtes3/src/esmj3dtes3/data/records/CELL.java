package esmj3dtes3.data.records;

import java.util.ArrayList;

import tools.io.ESMByteConvert;
import esmLoader.common.data.record.Record;
import esmLoader.common.data.record.Subrecord;
import esmj3d.data.shared.records.InstRECO;
import esmj3d.data.shared.subrecords.ZString;

public class CELL extends InstRECO// note not from CommonCELL
{
	public ZString EDID;

	public int flags = 0;

	public boolean interior = false;

	public int WHGT = 0;

	public CELL(Record recordData)
	{
		super(recordData);

		ArrayList<Subrecord> subrecords = recordData.getSubrecords();

		// heaps of FRMR at the end so just do the top few
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
			flags = ESMByteConvert.extractInt(bs, 0);
			interior = (flags & 0x1) == 1;
			x = ESMByteConvert.extractInt(bs, 4);
			y = ESMByteConvert.extractInt(bs, 8);
		}

		for (int i = 2; i < subrecords.size(); i++)
		{
			sr = subrecords.get(i);
			bs = sr.getData();
			if (sr.getType().equals("RGNN"))
			{

			}
			else if (sr.getType().equals("NAM0"))
			{

			}
			else if (sr.getType().equals("NAM5"))
			{

			}
			else if (sr.getType().equals("WHGT"))
			{
				WHGT = ESMByteConvert.extractInt(bs, 0);
			}
			else if (sr.getType().equals("AMBI"))
			{

			}
			else if (sr.getType().equals("FRMR"))
			{ //ignore all others REFRs data
				break;
			}

		}

	}

	public String showDetails()
	{
		return "CELL : (" + formId + "|" + Integer.toHexString(formId) + ") " + EDID.str + " : " + EDID.str;
	}

}
