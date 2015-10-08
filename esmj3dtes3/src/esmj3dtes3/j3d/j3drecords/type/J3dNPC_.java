package esmj3dtes3.j3d.j3drecords.type;

import java.util.ArrayList;

import nif.character.NifCharacterTes3;
import utils.ESConfig;
import utils.source.MediaSources;
import esmLoader.common.data.record.IRecordStore;
import esmj3d.data.shared.subrecords.MODL;
import esmj3d.j3d.j3drecords.type.J3dRECOType;
import esmj3dtes3.data.records.NPC_;
import esmj3dtes3.data.records.ARMO;
import esmj3dtes3.data.records.CLOT;
import esmj3dtes3.data.records.WEAP;

public class J3dNPC_ extends J3dRECOType
{
	private String helmetStr = null;

	private String headStr = null;

	private String hairStr = null;

	private String upperStr = null;

	private String lowerStr = null;

	private String handStr = null;

	private String footStr = null;

	private String shieldStr = null;

	private String weapStr = null;

	private boolean female = false;

	private NifCharacterTes3 nifCharacter;

	/**
	    MODL = Animation file
		RNAM = Race Name	}
		ANAM = Faction name	} Required, even if empty
		BNAM = Head model	}
		CNAM = Class name
		KNAM = Hair model	}
		
		
		NPCO = NPC item (36 bytes, occurs 0+ times)
			long	Count	  Number of the item
			char	Name[32]  The ID of the item	
			
	 * @param npc_
	 * @param master
	 * @param mediaSources
	 */
	public J3dNPC_(NPC_ npc_, IRecordStore master, MediaSources mediaSources)
	{
		super(npc_, null);
		female = ((npc_.FLAG & 0x1) != 0);
		ArrayList<String> fileNames = new ArrayList<String>();

		String nif = ".nif";
		if (npc_.BNAM != null)
		{
			headStr = "b\\" + npc_.BNAM.model.str + nif;
			fileNames.add(headStr);
		}

		if (npc_.KNAM != null)
		{
			hairStr = "b\\" + npc_.KNAM.model.str + nif;
			fileNames.add(hairStr);
		}

		//TODO: many NPCO 
		//NPC item (36 bytes, occurs 0+ times)
		//long	Count	  Number of the item
		//char	Name[32]  The ID of the item	

		if (npc_.RNAM != null)
		{
			//nif Meshes\b\b_n_Argonian_f_foot.nif not found
			//nif Meshes\b\b_n_Khajiit_m_foot.nif
			//nif Meshes\b\b_n_Khajiit_f_foot.nif
			String[] bodyparts = new String[]
			{ "ankle", "foot", "forearm", "groin", "knee", "neck", "skins", "upper arm", "upper leg", "wrist" };

			String pre = "b\\b_n_" + npc_.RNAM.str + "_" + (female ? "f" : "m") + "_";

			for (String bp : bodyparts)
			{
				// feet tails part of skins for for beast race
				// f_forearm_Argonian also missing but not m version!
				if ((bp.equals("foot") && (npc_.RNAM.str.equals("Argonian") || npc_.RNAM.str.equals("Khajiit")))
						|| (bp.equals("forearm") && npc_.RNAM.str.equals("Argonian") && female))
					continue;

				String nifFileName = pre + bp + nif;

				fileNames.add(nifFileName);

			}

		}

		boolean kna = (npc_.RNAM.str.equals("Argonian") || npc_.RNAM.str.equals("Khajiit"));
		//kna takes priority I wager
		String skeletonNifFile = ESConfig.TES_MESH_PATH + "xbase_anim" + (kna ? "kna.nif" : (female ? "_female.nif" : ".nif"));

		// don't use as it points to the non x version of the files, which is a hassle
		//if (npc_.MODL != null)
		{
			//skeletonNifFile = npc_.MODL.model.str;
			//System.out.println("MODL " + skeletonNifFile);
		}

		nifCharacter = new NifCharacterTes3(skeletonNifFile, fileNames, mediaSources);
		addChild(nifCharacter);
	}
	
	
	private void addCLOT(CLOT clot, IRecordStore master)
	{
		MODL m = clot.MODL;
	/*	if (female && clot.MOD3 != null)
		{
			m = clot.MOD3;
		}
		helmetStr = clot.BMDT.isHair() ? m.model.str : helmetStr;
		upperStr = clot.BMDT.isUpperBody() ? m.model.str : upperStr;
		lowerStr = clot.BMDT.isLowerBody() ? m.model.str : lowerStr;
		handStr = clot.BMDT.isHand() ? m.model.str : handStr;
		footStr = clot.BMDT.isFoot() ? m.model.str : footStr;*/

	}

	private void addARMO(ARMO armo, IRecordStore master)
	{

		MODL m = armo.MODL;
		/*if (female && armo.MOD3 != null)
		{
			m = armo.MOD3;
		}
		helmetStr = armo.BMDT.isHair() ? m.model.str : helmetStr;
		upperStr = armo.BMDT.isUpperBody() ? m.model.str : upperStr;
		lowerStr = armo.BMDT.isLowerBody() ? m.model.str : lowerStr;
		handStr = armo.BMDT.isHand() ? m.model.str : handStr;
		footStr = armo.BMDT.isFoot() ? m.model.str : footStr;
		shieldStr = armo.BMDT.isShield() ? m.model.str : shieldStr;*/
	}

	private void addWEAP(WEAP weap, IRecordStore master)
	{
		weapStr = weap.MODL.model.str;
	}
}
