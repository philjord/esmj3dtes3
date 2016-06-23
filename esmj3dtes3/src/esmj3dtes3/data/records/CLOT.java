package esmj3dtes3.data.records;

import java.util.ArrayList;
import java.util.List;

import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.data.shared.subrecords.ZString;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;
import tools.io.ESMByteConvert;

public class CLOT extends RECO
{
	public ZString EDID;

	public LString FULL;

	public MODL MODL; // male worn (or if no sex)

	public ZString ICON; //male icon

	public DATA DATA;

	public PART[] parts;

	public ZString SCRI;

	public ZString ENAM;

	public CLOT(Record recordData)
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
			else if (sr.getSubrecordType().equals("CTDT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getSubrecordType().equals("ITEX"))
			{
				ICON = new ZString(bs);
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
					Subrecord sr2 = subrecords.get(i + 1);
					part.partNameF = new String(sr2.getSubrecordData());
					i++;//CAREFUL incrementing the loop counter!
				}
				partsAL.add(part);
			}
			else if (sr.getSubrecordType().equals("ENAM"))
			{
				ENAM = new ZString(bs);
			}
			else if (sr.getSubrecordType().equals("SCRI"))
			{
				SCRI = new ZString(bs);
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
		public int EnchantPts;

		public DATA(byte[] bs)
		{
			Type = ESMByteConvert.extractInt(bs, 0);
			Weight = ESMByteConvert.extractFloat(bs, 4);
			Value = ESMByteConvert.extractShort(bs, 8);
			EnchantPts = ESMByteConvert.extractShort(bs, 10);

		}
		/*
		Type
			0 = Pants
			1 = Shoes
			2 = Shirt
			3 = Belt
			4 = Robe
			5 = Right Glove
			6 = Left Glove
			7 = Skirt
			8 = Ring
			9 = Amulet
		float Weight
		short Value
		short EnchantPts
		 */
	}

	/**
	 * So I think parts are what parts is covered by it, so a robe will cover heaps 
	 * but a boot only covers 2 and a shoe covers 1
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
