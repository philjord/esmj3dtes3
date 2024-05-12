package esmj3dtes3.data.records;

import java.util.ArrayList;
import java.util.List;

import esfilemanager.common.data.record.Record;
import esfilemanager.common.data.record.Subrecord;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.data.shared.subrecords.ZString;
import tools.io.ESMByteConvert;

public class ARMO extends RECO
{
	

	public LString FULL = null;

	public MODL MODL = null;

	public DATA DATA;

	public String ICON;

	public PART[] parts;

	public String SCRI;

	public String ENAM;

	public ARMO(Record recordData)
	{

		super(recordData);

		ArrayList<PART> partsAL = new ArrayList<PART>();

		List<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getSubrecordData();

			if (sr.getSubrecordType().equals("NAME"))
			{
				setEDID(bs);
			}
			else if (sr.getSubrecordType().equals("MODL"))
			{
				MODL = new MODL(bs);
			}
			else if (sr.getSubrecordType().equals("FNAM"))
			{
				FULL = new LString(bs);
			}
			else if (sr.getSubrecordType().equals("AODT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getSubrecordType().equals("ITEX"))
			{
				ICON = ZString.toString(bs);
			}
			else if (sr.getSubrecordType().equals("INDX"))
			{
				PART part = new PART();
				part.index = bs[0];
				if (subrecords.size() > i + 1 && subrecords.get(i + 1).getSubrecordType().equals("BNAM"))
				{
					Subrecord sr2 = subrecords.get(i + 1);
					part.partName = new String(sr2.getSubrecordData());
					i++;//CAREFUL incrementing the loop counter!
				}
				//TODO: I think it's index then BNAM and/or CNAM not just or as shown here
				if (subrecords.size() > i + 1 && subrecords.get(i + 1).getSubrecordType().equals("CNAM"))
				{
					System.out.println("CNAM seen");//only in CLOT not ARMO
					Subrecord sr2 = subrecords.get(i + 1);
					part.partNameF = new String(sr2.getSubrecordData());
					i++;//CAREFUL incrementing the loop counter!
				}
				partsAL.add(part);
			}
			else if (sr.getSubrecordType().equals("SCRI"))
			{
				SCRI = ZString.toString(bs);
			}
			else if (sr.getSubrecordType().equals("ENAM"))
			{
				ENAM = ZString.toString(bs);
			}

			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}
		}

		parts = new PART[partsAL.size()];
		partsAL.toArray(parts);
	}

	public class DATA
	{
		public int Type;
		public float Weight;
		public int Value;
		public int Health;
		public int EnchantPts;
		public int Armour;

		public DATA(byte[] bs)
		{
			Type = ESMByteConvert.extractInt(bs, 0);
			Weight = ESMByteConvert.extractFloat(bs, 4);
			Value = ESMByteConvert.extractInt(bs, 8);
			Health = ESMByteConvert.extractInt(bs, 12);
			EnchantPts = ESMByteConvert.extractInt(bs, 16);
			Armour = ESMByteConvert.extractInt(bs, 20);
		}
		/*
		long  Type
			0 = Helmet
			1 = Cuirass
			2 = L. Pauldron
			3 = R. Pauldron
			4 = Greaves
			5 = Boots
			6 = L. Gauntlet
			7 = R. Gauntlet
			8 = Shield
			9 = L. Bracer
			10 = R. Bracer
		float Weight
		long  Value
		long  Health
		long  EnchantPts
		long  Armour
		 */
	}

	/**
	 * So I think parts are what parts is covered by it, so a robe will cover heaps but a ring covers only?
	 * I notice ring and amulet are part of cloth type so may one of each type as well?
	 * A gauntlet covers left hand 7 and left wrist 9.
	 * Anyway only one item(the most recent can be equipped on a part
	 *
	 */
	public class PART
	{

		public int index = -1;
		public String partName = "";// very optional and pointless, unless these parts are used to make the file name?
		public String partNameF = "";// even more optional and non-existant

		/*INDX = Body Part Index (1 byte)
		0 = Head
		1 = Hair
		2 = Neck
		3 = Cuirass
		4 = Groin
		5 = Skirt
		6 = Right Hand
		7 = Left Hand
		8 = Right Wrist
		9 = Left Wrist
		10 = Shield
		11 = Right Forearm
		12 = Left Forearm
		13 = Right Upper Arm
		14 = Left Upper Arm
		15 = Right Foot
		16 = Left Foot
		17 = Right Ankle
		18 = Left Ankle
		19 = Right Knee
		20 = Left Knee
		21 = Right Upper Leg
		22 = Left Upper Leg
		23 = Right Pauldron
		24 = Left Pauldron
		25 = Weapon
		26 = Tail*/
	}

}
