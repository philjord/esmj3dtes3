package esmj3dtes3.data.records;

import java.util.List;

import esmio.common.data.record.Record;
import esmio.common.data.record.Subrecord;
import esmj3d.data.shared.records.RECO;

public class BODY extends RECO
{

	public DATA DATA;

	public BODY(Record recordData)
	{
		super(recordData);

		List<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getSubrecordData();

			if (sr.getSubrecordType().equals("BYDT"))
			{
				DATA = new DATA(bs);
			}
			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}
		}
	}

	/*20: BODY =  1125 (    75,     92.73,    103)
			Body Parts
			BYDT = Body part data (4 bytes)
				byte Part
					0 = Head
					1 = Hair
					2 = Neck
					3 = Chest
					4 = Groin
					5 = Hand
					6 = Wrist
					7 = Forearm
					8 = Upperarm
					9 = Foot
					10 = Ankle
					11 = Knee
					12 = Upperleg
					13 = Clavicle
					14 = Tail
				byte Vampire
				byte Flags
					1 = Female
					2 = Playable
				byte PartType
					0 = Skin
					1 = Clothing
					2 = Armor*/

	public class DATA
	{
		int Part = -1;
		int Vampire = -1;
		int Flags = -1;
		int PartType = -1;

		public DATA(byte[] bs)
		{
			Part = bs[0];
			Vampire = bs[1];
			Flags = bs[2];
			PartType = bs[3];
		}
	}

}
