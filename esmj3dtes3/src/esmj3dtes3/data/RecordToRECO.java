package esmj3dtes3.data;

import java.util.HashSet;
import java.util.List;

import esmj3d.data.shared.records.LAND;
import esmj3d.data.shared.records.CommonLIGH;
import esmj3d.data.shared.records.LTEX;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.ZString;
import esmj3dtes3.data.records.ACTI;
import esmj3dtes3.data.records.ALCH;
import esmj3dtes3.data.records.APPA;
import esmj3dtes3.data.records.ARMO;
import esmj3dtes3.data.records.BOOK;
import esmj3dtes3.data.records.BSGN;
import esmj3dtes3.data.records.CELL;
import esmj3dtes3.data.records.CLAS;
import esmj3dtes3.data.records.CLOT;
import esmj3dtes3.data.records.CONT;
import esmj3dtes3.data.records.CREA;
import esmj3dtes3.data.records.DIAL;
import esmj3dtes3.data.records.DOOR;
import esmj3dtes3.data.records.ENCH;
import esmj3dtes3.data.records.FACT;
import esmj3dtes3.data.records.GLOB;
import esmj3dtes3.data.records.GMST;
import esmj3dtes3.data.records.INGR;
import esmj3dtes3.data.records.LEVC;
import esmj3dtes3.data.records.LEVI;
import esmj3dtes3.data.records.MGEF;
import esmj3dtes3.data.records.MISC;
import esmj3dtes3.data.records.NPC_;
import esmj3dtes3.data.records.RACE;
import esmj3dtes3.data.records.REFR;
import esmj3dtes3.data.records.REGN;
import esmj3dtes3.data.records.SCPT;
import esmj3dtes3.data.records.SKIL;
import esmj3dtes3.data.records.SOUN;
import esmj3dtes3.data.records.SPEL;
import esmj3dtes3.data.records.STAT;
import esmj3dtes3.data.records.WEAP;
import esmmanager.common.data.record.IRecordStore;
import esmmanager.common.data.record.Record;

public class RecordToRECO
{
	//NOTE testing only
	public static void makeRECOsForCELL(IRecordStore master, Record cellRecord, List<Record> children)
	{
		new esmj3dtes3.data.records.CELL(cellRecord);

		for (Record record : children)
		{
			try
			{
				if (record.getRecordType().equals("REFR"))
				{
					REFR refr = new REFR(record);
					makeREFR(refr, master);
				}
				else if (record.getRecordType().equals("LAND"))
				{
					LAND land = new LAND(record);
				}
				else if (record.getRecordType().equals("PGRD"))
				{

				}
				else if (record.getRecordType().equals("PGRE"))
				{

				}
				else if (record.getRecordType().equals("NAVM"))
				{

				}
				else
				{
					System.out.println("Record type not converted to RECO " + record.getRecordType());
				}
			}
			catch (NullPointerException e)
			{
				// probably just a bum pointer in the ESM
			}

		}
	}

	private static RECO makeREFR(REFR refr, IRecordStore master)
	{
		Record baseRecord = master.getRecord(refr.NAME.formId);

		RECO reco = makeRECO(baseRecord);
		if (reco instanceof LEVC)
		{
			reco = makeLVLC((LEVC) reco, master);
		}

		return reco;

	}

	private static RECO makeLVLC(LEVC lvlc, IRecordStore master)
	{
		// TODO: randomly picked for now
		ZString[] LVLOs = lvlc.charID;
		if (LVLOs.length > 0)
		{
			int idx = (int) (Math.random() * LVLOs.length);
			idx = idx == LVLOs.length ? 0 : idx;

			Record baseRecord = master.getRecord(LVLOs[idx].str);

			if (baseRecord.getRecordType().equals("CREA"))
			{
				return new CREA(baseRecord);
			}
			else if (baseRecord.getRecordType().equals("LVLC"))
			{
				// it is in fact a pointer across to another leveled creature (LVLC)
				return new LEVC(baseRecord);
			}
			else if (baseRecord.getRecordType().equals("NPC_"))
			{
				// it is in fact a pointer across to another leveled creature (LVLC)
				return new NPC_(baseRecord);
			}
			else
			{
				System.out.println("LVLC record type not converted to j3d " + baseRecord.getRecordType());
			}
		}

		return null;
	}

	public static RECO makeLVLI(LEVI lvli, IRecordStore master)
	{
		// TODO: randomly picked for now
		ZString[] LVLOs = lvli.itemID;

		int idx = (int) (Math.random() * LVLOs.length);
		idx = idx == LVLOs.length ? 0 : idx;

		Record baseRecord = master.getRecord(LVLOs[idx].str);

		if (baseRecord.getRecordType().equals("NPC_"))
		{
			// it is in fact a pointer across to another leveled creature (LVLC)
			return new NPC_(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("LVLI"))
		{
			// it is in fact a pointer across to another leveled character (LVLN)
			makeLVLI(new LEVI(baseRecord), master);
		}
		else
		{
			System.out.println("LVLI record type not converted to j3d " + baseRecord.getRecordType());
		}

		return null;
	}

	public static RECO makeRECO(Record baseRecord)
	{

		if (baseRecord.getRecordType().equals("GMST"))
		{
			return new GMST(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("GLOB"))
		{
			return new GLOB(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("CLAS"))
		{
			return new CLAS(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("FACT"))
		{
			return new FACT(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("RACE"))
		{
			return new RACE(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("SOUN"))
		{
			return new SOUN(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("SKIL"))
		{
			return new SKIL(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("MGEF"))
		{
			return new MGEF(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("SCPT"))
		{
			return new SCPT(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("LTEX"))
		{
			return new LTEX(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("ENCH"))
		{
			return new ENCH(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("SPEL"))
		{
			return new SPEL(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("BSGN"))
		{
			return new BSGN(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("ACTI"))
		{
			return new ACTI(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("APPA"))
		{
			return new APPA(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("ARMO"))
		{
			return new ARMO(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("BOOK"))
		{
			return new BOOK(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("CLOT"))
		{
			return new CLOT(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("CONT"))
		{
			return new CONT(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("DOOR"))
		{
			return new DOOR(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("INGR"))
		{
			return new INGR(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("LIGH"))
		{
			return new CommonLIGH(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("MISC"))
		{
			return new MISC(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("STAT"))
		{
			return new STAT(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("WEAP"))
		{
			return new WEAP(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("NPC_"))
		{
			return new NPC_(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("CREA"))
		{
			return new CREA(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("ALCH"))
		{
			return new ALCH(baseRecord);
		}

		else if (baseRecord.getRecordType().equals("REGN"))
		{
			return new REGN(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("DIAL"))
		{
			return new DIAL(baseRecord);
		}

		//********************* special cases below, called for testing only
		else if (baseRecord.getRecordType().equals("LVLI"))
		{
			return new LEVI(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("LVLC"))
		{
			return new LEVC(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("CELL"))
		{
			return new CELL(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("GRUP"))
		{
			//return new GRUP(baseRecord);
		}
		else if (baseRecord.getRecordType().equals("REFR"))
		{
			return new REFR(baseRecord);
		}
		else
		{
			if (!constructorsShowen.contains(baseRecord.getRecordType()))
			{
				System.out.println("else if (baseRecord.getRecordType().equals(\"" + baseRecord.getRecordType() + "\"))");
				System.out.println("{");
				System.out.println("return new " + baseRecord.getRecordType() + "(baseRecord);");
				System.out.println("}");
				constructorsShowen.add(baseRecord.getRecordType());
			}
		}
		return null;
	}

	private static HashSet<String> constructorsShowen = new HashSet<String>();

}
