package esmj3dtes3.data.records;

import java.util.List;

import esfilemanager.common.data.record.Record;
import esfilemanager.common.data.record.Subrecord;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.MODL;

import tools.io.ESMByteConvert;

public class CREA extends RECO
{
	

	public LString FULL = null;

	public MODL MODL = null;

	//data data
	public byte unknown1;

	public byte combat;

	public byte magic;

	public int Soul;

	public int health;

	public int unknown2;

	public int damage;

	public byte Str;

	public byte Int;

	public byte Wil;

	public byte Agi;

	public byte Spd;

	public byte End;

	public byte Per;

	public byte Luc;

	public float scale = 1;

	public int FLAG;

	public CREA(Record recordData)
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
			else if (sr.getSubrecordType().equals("MODL"))
			{
				MODL = new MODL(bs);
			}
			else if (sr.getSubrecordType().equals("NPDT"))
			{

			}
			else if (sr.getSubrecordType().equals("FLAG"))
			{
				FLAG = ESMByteConvert.extractInt(bs, 0);
			}
			else if (sr.getSubrecordType().equals("SCRI"))
			{

			}
			else if (sr.getSubrecordType().equals("NPCO"))
			{
				// many items
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
			else if (sr.getSubrecordType().equals("XSCL"))
			{
				scale = ESMByteConvert.extractFloat(bs, 0);
			}
			else if (sr.getSubrecordType().equals("CNAM"))
			{

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
