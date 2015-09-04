package esmj3dtes3.data.records;

import java.util.ArrayList;

import esmLoader.common.data.record.Record;
import esmLoader.common.data.record.Subrecord;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.subrecords.DATA;

public class NPC_ extends RECO
{
	public ZString EDID = null;

	public LString FULL = null;

	public MODL MODL = null;

	public DATA DATA;

	public NPC_(Record recordData)
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
			else if (sr.getType().equals("MODL"))
			{
				MODL = new MODL(bs);
			}
			else if (sr.getType().equals("RNAM"))
			{

			}
			else if (sr.getType().equals("ANAM"))
			{

			}
			else if (sr.getType().equals("BNAM"))
			{

			}
			else if (sr.getType().equals("CNAM"))
			{

			}
			else if (sr.getType().equals("KNAM"))
			{

			}
			else if (sr.getType().equals("NPDT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getType().equals("FLAG"))
			{

			}
			else if (sr.getType().equals("NPCO"))
			{

			}
			else if (sr.getType().equals("NPCS"))
			{

			}
			else if (sr.getType().equals("AIDT"))
			{

			}
			else if (sr.getType().equals("AI_W"))
			{

			}
			else if (sr.getType().equals("AI_T"))
			{

			}
			else if (sr.getType().equals("AI_F"))
			{

			}
			else if (sr.getType().equals("AI_E"))
			{

			}
			else if (sr.getType().equals("AI_A"))
			{

			}
			else if (sr.getType().equals("DODT"))
			{

			}
			else if (sr.getType().equals("DNAM"))
			{

			}
			else if (sr.getType().equals("XSCL"))
			{

			}
			else if (sr.getType().equals("SCRI"))
			{

			}

			else
			{
				System.out.println("unhandled : " + sr.getType() + " in record " + recordData + " in " + this);
			}

		}
	}

}
