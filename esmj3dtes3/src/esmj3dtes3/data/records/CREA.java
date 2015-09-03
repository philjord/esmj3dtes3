package esmj3dtes3.data.records;

import java.util.ArrayList;

import esmLoader.common.data.record.Record;
import esmLoader.common.data.record.Subrecord;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.data.shared.subrecords.ZString;

public class CREA extends RECO
{
	public ZString EDID = null;

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

	public CREA(Record recordData)
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
			else if (sr.getType().equals("NPDT"))
			{
				
			}
			else if (sr.getType().equals("FLAG"))
			{
				 
			}
			else if (sr.getType().equals("SCRI"))
			{
				 
			}
			else if (sr.getType().equals("NPCO"))
			{
				 // many items
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
			else if (sr.getType().equals("XSCL"))
			{
				 
			}
			else if (sr.getType().equals("CNAM"))
			{
				 
			}
			else if (sr.getType().equals("NPCS"))
			{
				 
			}
			
			else
			{
				System.out.println("unhandled : " + sr.getType() + " in record " + recordData + " in " + this);
			}

			

		}
	}

	
}
