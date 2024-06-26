package esmj3dtes3.j3d.j3drecords.inst;

import esfilemanager.common.data.record.IRecordStore;
import esfilemanager.common.data.record.Record;
import esfilemanager.tes3.IRecordStoreTes3;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.j3d.j3drecords.inst.J3dRECOChaInst;
import esmj3d.j3d.j3drecords.inst.J3dRECODynInst;
import esmj3d.j3d.j3drecords.inst.J3dRECOInst;
import esmj3d.j3d.j3drecords.inst.J3dRECOStatInst;
import esmj3d.j3d.j3drecords.type.J3dCONT;
import esmj3d.j3d.j3drecords.type.J3dRECOTypeActionable;
import esmj3d.j3d.j3drecords.type.J3dRECOTypeCha;
import esmj3d.j3d.j3drecords.type.J3dRECOTypeDynamic;
import esmj3d.j3d.j3drecords.type.J3dRECOTypeStatic;
import esmj3dtes3.data.records.ACTI;
import esmj3dtes3.data.records.ALCH;
import esmj3dtes3.data.records.APPA;
import esmj3dtes3.data.records.ARMO;
import esmj3dtes3.data.records.BOOK;
import esmj3dtes3.data.records.CLOT;
import esmj3dtes3.data.records.CONT;
import esmj3dtes3.data.records.CREA;
import esmj3dtes3.data.records.DOOR;
import esmj3dtes3.data.records.INGR;
import esmj3dtes3.data.records.LEVC;
import esmj3dtes3.data.records.LIGH;
import esmj3dtes3.data.records.LOCK;
import esmj3dtes3.data.records.MISC;
import esmj3dtes3.data.records.NPC_;
import esmj3dtes3.data.records.PROB;
import esmj3dtes3.data.records.REFR;
import esmj3dtes3.data.records.REPA;
import esmj3dtes3.data.records.STAT;
import esmj3dtes3.data.records.WEAP;
import esmj3dtes3.j3d.cell.Tes3ModelSizes;
import esmj3dtes3.j3d.j3drecords.type.J3dCREA;
import esmj3dtes3.j3d.j3drecords.type.J3dLIGH;
import esmj3dtes3.j3d.j3drecords.type.J3dNPC_;
import esmj3dtes3.j3d.j3drecords.type.J3dPivotDOOR;
import utils.source.MediaSources;

public class J3dREFRFactory
{
	public static boolean DEBUG_FIRST_LIST_ITEM_ONLY = false;

	private static J3dRECODynInst makeJ3dRECODynInst(REFR refr, RECO reco, MODL modl, boolean makePhys, MediaSources mediaSources)
	{
		if (modl != null)
		{
			J3dRECODynInst j3dinst = new J3dRECODynInst(refr, true, true, makePhys);
			j3dinst.setJ3dRECOType(new J3dRECOTypeDynamic(reco, modl.model, makePhys, mediaSources));
			return j3dinst;
		}
		else
		{
			System.out.println("null modl there " + reco);
			return null;
		}
	}

	private static J3dRECOStatInst makeJ3dRECOActionInst(REFR refr, RECO reco, MODL modl, boolean makePhys, MediaSources mediaSources)
	{
		if (modl != null)
		{
			J3dRECOStatInst j3dinst = new J3dRECOStatInst(refr, true, true, makePhys);
			j3dinst.setJ3dRECOType(new J3dRECOTypeActionable(reco, modl.model, makePhys, mediaSources));
			return j3dinst;
		}
		else
		{
			System.out.println("null modl there " + reco);
			return null;
		}
	}

	public static J3dRECOStatInst makeJ3DReferFar(REFR refr, IRecordStore master, MediaSources mediaSources)
	{

		Record baseRecord = ((IRecordStoreTes3) master).getRecord(refr.NAMEref);

		if (baseRecord.getRecordType().equals("STAT"))
		{
			STAT stat = new STAT(baseRecord);

			if (stat.MODL != null)
			{
				float size = Tes3ModelSizes.getSize(stat.MODL.model, refr.getScale());
				J3dRECOStatInst j3dinst = new J3dRECOStatInstFar(refr, size);
				j3dinst.setJ3dRECOType(new J3dRECOTypeStatic(stat, stat.MODL.model, false, mediaSources));
				return j3dinst;
			}
		}
		else
		{
			System.out.println("Far REFR record type not converted to j3d " + baseRecord.getRecordType());
		}

		return null;
	}

	public static J3dRECOInst makeJ3DRefer(REFR refr, boolean makePhys, IRecordStore master, MediaSources mediaSources)
	{

		Record baseRecord = ((IRecordStoreTes3) master).getRecord(refr.NAMEref);

		if (baseRecord.getRecordType().equals("NPC_"))
		{
			NPC_ npc_ = new NPC_(baseRecord);
			J3dRECOChaInst j3dinst = new J3dRECOChaInst(refr);
			j3dinst.setJ3dRECOType(new J3dNPC_(npc_, master, makePhys, mediaSources));
			return j3dinst;

		}
		else if (baseRecord.getRecordType().equals("CREA"))
		{
			CREA crea = new CREA(baseRecord);
			J3dRECOChaInst j3dinst = new J3dRECOChaInst(refr);
			j3dinst.setJ3dRECOType(new J3dCREA(crea, master, makePhys, mediaSources));
			return j3dinst;

		}
		else if (baseRecord.getRecordType().equals("LEVC"))
		{
			// it is in fact a pointer across to another leveled creature (LVLC)
			LEVC lvlc = new LEVC(baseRecord);
			J3dRECOChaInst j3dinst = new J3dRECOChaInst(refr);
			j3dinst.setJ3dRECOType(makeLVLC(lvlc, master, makePhys, mediaSources));
			return j3dinst;

		}
		else if (baseRecord.getRecordType().equals("STAT"))
		{
			STAT stat = new STAT(baseRecord);

			if (stat.MODL != null)
			{

				/*	if (stat.MODL.model.toLowerCase().contains("ex_dae_claw_01.nif"))
					{
						System.out.println("Howdy howdy!");
					}*/

				if (Tes3ModelSizes.distant(stat.MODL.model, refr.getScale()))
				{
					//J3dRECOStatInst j3dinst = new J3dRECOStatInst(refr, false, makePhys);
					//j3dinst.setJ3dRECOType(new J3dRECOTypeStatic(stat, stat.MODL.model, makePhys, mediaSources));
					//return j3dinst;
					float size = Tes3ModelSizes.getSize(stat.MODL.model, refr.getScale());
					J3dRECOStatInst j3dinst = new J3dRECOStatInstFar(refr, size);
					j3dinst.setJ3dRECOType(new J3dRECOTypeStatic(stat, stat.MODL.model, false, mediaSources));
					return j3dinst;
				}
				else
				{
					J3dRECOStatInst j3dinst = new J3dRECOStatInst(refr, true, true, makePhys);
					j3dinst.setJ3dRECOType(new J3dRECOTypeStatic(stat, stat.MODL.model, makePhys, mediaSources));
					return j3dinst;
				}
			}

		}
		else if (baseRecord.getRecordType().equals("APPA"))
		{
			APPA appa = new APPA(baseRecord);
			return makeJ3dRECODynInst(refr, appa, appa.MODL, makePhys, mediaSources);
		}
		else if (baseRecord.getRecordType().equals("REPA"))
		{
			REPA repa = new REPA(baseRecord);
			return makeJ3dRECODynInst(refr, repa, repa.MODL, makePhys, mediaSources);
		}
		else if (baseRecord.getRecordType().equals("PROB"))
		{
			PROB prob = new PROB(baseRecord);
			return makeJ3dRECODynInst(refr, prob, prob.MODL, makePhys, mediaSources);
		}
		else if (baseRecord.getRecordType().equals("MISC"))
		{
			MISC misc = new MISC(baseRecord);
			return makeJ3dRECODynInst(refr, misc, misc.MODL, makePhys, mediaSources);
		}
		else if (baseRecord.getRecordType().equals("CONT"))
		{
			return new J3dRECOStatInst(refr, new J3dCONT(new CONT(baseRecord), makePhys, mediaSources), true, true, makePhys);
		}
		else if (baseRecord.getRecordType().equals("CLOT"))
		{
			CLOT clot = new CLOT(baseRecord);
			return makeJ3dRECODynInst(refr, clot, clot.MODL, makePhys, mediaSources);
		}
		else if (baseRecord.getRecordType().equals("BOOK"))
		{
			BOOK book = new BOOK(baseRecord);
			return makeJ3dRECODynInst(refr, book, book.MODL, makePhys, mediaSources);
		}
		else if (baseRecord.getRecordType().equals("ALCH"))
		{
			ALCH alch = new ALCH(baseRecord);
			return makeJ3dRECODynInst(refr, alch, alch.MODL, makePhys, mediaSources);
		}
		else if (baseRecord.getRecordType().equals("INGR"))
		{
			INGR ingr = new INGR(baseRecord);
			return makeJ3dRECODynInst(refr, ingr, ingr.MODL, makePhys, mediaSources);
		}
		else if (baseRecord.getRecordType().equals("ACTI"))
		{
			ACTI acti = new ACTI(baseRecord);
			return makeJ3dRECOActionInst(refr, acti, acti.MODL, makePhys, mediaSources);
		}
		else if (baseRecord.getRecordType().equals("WEAP"))
		{
			WEAP weap = new WEAP(baseRecord);
			return makeJ3dRECODynInst(refr, weap, weap.MODL, makePhys, mediaSources);
		}
		else if (baseRecord.getRecordType().equals("ARMO"))
		{
			ARMO armo = new ARMO(baseRecord);
			return makeJ3dRECODynInst(refr, armo, armo.MODL, makePhys, mediaSources);
		}
		else if (baseRecord.getRecordType().equals("LOCK"))
		{
			LOCK lock = new LOCK(baseRecord);
			return makeJ3dRECODynInst(refr, lock, lock.MODL, makePhys, mediaSources);
		}
		else if (baseRecord.getRecordType().equals("PROB"))
		{
			PROB prob = new PROB(baseRecord);
			return makeJ3dRECODynInst(refr, prob, prob.MODL, makePhys, mediaSources);
		}

		else if (baseRecord.getRecordType().equals("DOOR"))
		{
			//TODO: other markers
			DOOR door = new DOOR(baseRecord);
			if (door.MODL.model.startsWith("Marker_"))
				return null;
			return new J3dRECOStatInst(refr, new J3dPivotDOOR(door, makePhys, mediaSources, (IRecordStoreTes3) master), true, true, makePhys);
		}
		else if (baseRecord.getRecordType().equals("LIGH"))
		{
			LIGH ligh = new LIGH(baseRecord);
			return new J3dRECOStatInst(refr, new J3dLIGH(ligh, makePhys, mediaSources, (IRecordStoreTes3) master), true, true, makePhys);
		}
		else if (baseRecord.getRecordType().equals("SOUN"))
		{
			// looks like these never appear as refr, but as part of lights etc
		}
		else if (baseRecord.getRecordType().equals("SNDG"))
		{
			//TODO: sound generator
			System.out.println("SNDG?SNDGSNDGSNDGSNDGSNDGSNDGSNDGSNDG");
		}
		else if (baseRecord.getRecordType().equals("SBSP"))
		{
			//SBSP sbsp = new SBSP(baseRecord);
			System.out.println("SBSP?SBSPSBSPSBSPSBSP does this ever happen??");
		}

		else
		{
			System.out.println("REFR record type not converted to j3d " + baseRecord.getRecordType());
		}

		return null;
	}

	/** TODO: Note does not bother with teh ACRE or ACHR system, but it should
	 * 
	 * @param lvlc
	 * @param master
	 * @param makePhys 
	 * @param meshSource
	 * @param textureSource
	 * @param soundSource
	 * @return
	 */
	private static J3dRECOTypeCha makeLVLC(LEVC lvlc, IRecordStore master, boolean makePhys, MediaSources mediaSources)
	{
		// TODO: randomly picked for now
		String[] LVLOs = lvlc.charID;

		int idx = (int) (Math.random() * LVLOs.length);
		idx = idx == LVLOs.length ? 0 : idx;

		if (DEBUG_FIRST_LIST_ITEM_ONLY)
			idx = 0;

		Record baseRecord = ((IRecordStoreTes3) master).getRecord(LVLOs[idx]);

		if (baseRecord.getRecordType().equals("LEVC"))
		{
			// it is in fact a pointer across to another leveled creature (LVLC)
			LEVC lvlc2 = new LEVC(baseRecord);
			return makeLVLC(lvlc2, master, makePhys, mediaSources);
		}
		else if (baseRecord.getRecordType().equals("CREA"))
		{
			CREA crea = new CREA(baseRecord);
			return new J3dCREA(crea, master, makePhys, mediaSources);
		}
		else if (baseRecord.getRecordType().equals("NPC_"))
		{
			NPC_ npc_ = new NPC_(baseRecord);
			return new J3dNPC_(npc_, master, makePhys, mediaSources);
		}
		else
		{
			System.out.println("LEVC record type not converted to j3d " + baseRecord.getRecordType());
		}

		return null;
	}
}
