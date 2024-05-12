package esmj3dtes3.data.records;

import java.util.List;

import esfilemanager.common.data.record.Record;
import esfilemanager.common.data.record.Subrecord;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.LString;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.data.shared.subrecords.ZString;
import tools.io.ESMByteConvert;

public class WEAP extends RECO
{
	

	public MODL MODL;

	public LString FULL;

	public DATA DATA;

	public String ICON;

	public String ENAM;

	public String SCRI;

	public WEAP(Record recordData)
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
			else if (sr.getSubrecordType().equals("MODL"))
			{
				MODL = new MODL(bs);
			}
			else if (sr.getSubrecordType().equals("FNAM"))
			{
				FULL = new LString(bs);
			}
			else if (sr.getSubrecordType().equals("WPDT"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getSubrecordType().equals("ITEX"))
			{
				ICON = ZString.toString(bs);
			}
			else if (sr.getSubrecordType().equals("ENAM"))
			{
				ENAM = ZString.toString(bs);
			}
			else if (sr.getSubrecordType().equals("SCRI"))
			{
				SCRI = ZString.toString(bs);
			}

			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}
		}
	}

	public class DATA
	{

		public float Weight;
		public int Value;
		public int Type;
		public int Health;
		public float Speed;
		public float Reach;
		public int EnchantPts;
		public byte ChopMin;
		public byte ChopMax;
		public byte SlashMin;
		public byte SlashMax;
		public byte ThrustMin;
		public byte ThrustMax;
		public int Flags;

		public DATA(byte[] bs)
		{
			Weight = ESMByteConvert.extractFloat(bs, 0);
			Value = ESMByteConvert.extractInt(bs, 4);
			Type = ESMByteConvert.extractShort(bs, 8);
			Health = ESMByteConvert.extractInt(bs, 10);
			Speed = ESMByteConvert.extractFloat(bs, 12);
			Reach = ESMByteConvert.extractFloat(bs, 16);
			EnchantPts = ESMByteConvert.extractShort(bs, 20);
			ChopMin = bs[22];
			ChopMax = bs[23];
			SlashMin = bs[24];
			SlashMax = bs[25];
			ThrustMin = bs[26];
			ThrustMax = bs[27];
			Flags = ESMByteConvert.extractInt(bs, 28);

		}
		/*
		float Weight
		long  Value
		short Type? (0 to 13)
			0 = ShortBladeOneHand
			1 = LongBladeOneHand
			2 = LongBladeTwoClose
			3 = BluntOneHand
			4 = BluntTwoClose
			5 = BluntTwoWide
			6 = SpearTwoWide
			7 = AxeOneHand
			8 = AxeTwoHand
			9 = MarksmanBow
			10 = MarksmanCrossbow
			11 = MarksmanThrown
			12 = Arrow
			13 = Bolt
		short Health
		float Speed
		float Reach
		short EnchantPts
		byte  ChopMin
		byte  ChopMax
		byte  SlashMin
		byte  SlashMax
		byte  ThrustMin
		byte  ThrustMax
		long  Flags (0 to 1)
			0 = ?
			1 = Ignore Normal Weapon Resistance?
		 */
	}
}
