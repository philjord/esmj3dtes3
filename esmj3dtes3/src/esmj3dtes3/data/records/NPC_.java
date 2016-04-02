package esmj3dtes3.data.records;

import java.util.List;

import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.subrecords.DATA;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;
import tools.io.ESMByteConvert;

public class NPC_ extends RECO
{
	public ZString EDID = null;

	public LString FULL = null;

	public MODL MODL = null;

	public DATA DATA;

	public MODL BNAM;

	public MODL KNAM;

	public ZString RNAM;

	public int FLAG;

	public NPC_(Record recordData)
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
			else if (sr.getSubrecordType().equals("FNAM"))
			{
				FULL = new LString(bs);
			}
			else if (sr.getSubrecordType().equals("MODL"))
			{
				MODL = new MODL(bs);
			}
			else if (sr.getSubrecordType().equals("RNAM"))
			{
				RNAM = new ZString(bs);
			}
			else if (sr.getSubrecordType().equals("ANAM"))
			{

			}
			else if (sr.getSubrecordType().equals("BNAM"))
			{
				BNAM = new MODL(bs);
			}
			else if (sr.getSubrecordType().equals("CNAM"))
			{

			}
			else if (sr.getSubrecordType().equals("KNAM"))
			{
				KNAM = new MODL(bs);
			}
			else if (sr.getSubrecordType().equals("NPDT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getSubrecordType().equals("FLAG"))
			{
				FLAG = ESMByteConvert.extractInt(bs, 0);
			}
			else if (sr.getSubrecordType().equals("NPCO"))
			{

			}
			else if (sr.getSubrecordType().equals("NPCS"))
			{

			}
			else if (sr.getSubrecordType().equals("AIDT"))
			{

			}
			else if (sr.getSubrecordType().equals("AI_W"))
			{

			}
			else if (sr.getSubrecordType().equals("AI_T"))
			{

			}
			else if (sr.getSubrecordType().equals("AI_F"))
			{

			}
			else if (sr.getSubrecordType().equals("AI_E"))
			{

			}
			else if (sr.getSubrecordType().equals("AI_A"))
			{

			}
			else if (sr.getSubrecordType().equals("DODT"))
			{

			}
			else if (sr.getSubrecordType().equals("DNAM"))
			{

			}
			else if (sr.getSubrecordType().equals("XSCL"))
			{

			}
			else if (sr.getSubrecordType().equals("SCRI"))
			{

			}

			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}

		}
	}

}
