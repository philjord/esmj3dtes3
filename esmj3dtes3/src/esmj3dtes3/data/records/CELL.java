package esmj3dtes3.data.records;

import java.util.List;

import esfilemanager.common.data.record.Record;
import esfilemanager.common.data.record.Subrecord;
import esmj3d.data.shared.records.InstRECO;
import esmj3d.data.shared.subrecords.ZString;
import tools.io.ESMByteConvert;

public class CELL extends InstRECO// note not from CommonCELL
{
	public ZString EDID;

	public int flags = 0;

	public boolean interior = false;

	public float WHGT = 0;

	public CELL(Record recordData)
	{
		super(recordData);

		List<Subrecord> subrecords = recordData.getSubrecords();

		// heaps of FRMR at the end so just do the top few
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
			flags = ESMByteConvert.extractInt(bs, 0);
			interior = (flags & 0x1) == 1;
			x = ESMByteConvert.extractInt(bs, 4);
			y = ESMByteConvert.extractInt(bs, 8);
		}

		for (int i = 2; i < subrecords.size(); i++)
		{
			sr = subrecords.get(i);
			bs = sr.getSubrecordData();
			if (sr.getSubrecordType().equals("RGNN"))
			{

			}
			else if (sr.getSubrecordType().equals("NAM0"))
			{

			}
			else if (sr.getSubrecordType().equals("NAM5"))
			{

			}
			else if (sr.getSubrecordType().equals("WHGT"))
			{
				WHGT = ESMByteConvert.extractFloat(bs, 0);
			}
			else if (sr.getSubrecordType().equals("AMBI"))
			{

			}
			else if (sr.getSubrecordType().equals("FRMR"))
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
