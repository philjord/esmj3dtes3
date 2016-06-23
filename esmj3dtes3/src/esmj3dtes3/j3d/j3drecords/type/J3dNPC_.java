package esmj3dtes3.j3d.j3drecords.type;

import java.util.ArrayList;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import esmj3d.j3d.BethRenderSettings;
import esmj3d.j3d.j3drecords.type.J3dRECOTypeCha;
import esmj3dtes3.data.records.ARMO;
import esmj3dtes3.data.records.CLOT;
import esmj3dtes3.data.records.NPC_;
import esmj3dtes3.data.records.WEAP;
import esmmanager.common.data.record.IRecordStore;
import esmmanager.common.data.record.Record;
import esmmanager.tes3.IRecordStoreTes3;
import nif.character.AttachedParts;
import nif.character.NifCharacterTes3;
import tools3d.utils.scenegraph.Fadable;
import utils.ESConfig;
import utils.source.MediaSources;

public class J3dNPC_ extends J3dRECOTypeCha
{
	private boolean female = false;

	private boolean isBeast = false;

	private NPC_ npc_;

	private AttachedParts skinFileNames = new AttachedParts();
	private AttachedParts attachFileNames = new AttachedParts();

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
		super(npc_);
		this.npc_ = npc_;

		female = ((npc_.FLAG & 0x1) != 0);
		isBeast = npc_.RNAM.str.equals("Argonian") || npc_.RNAM.str.equals("Khajiit");

		//Do the inventory items
		IRecordStoreTes3 recordStoreTes3 = (IRecordStoreTes3) master;

		for (int i = 0; i < npc_.NPCOs.length; i++)
		{
			//System.out.println("item " + npc_.NPCOs[i].count + " " + npc_.NPCOs[i].itemName);
			Record rec = recordStoreTes3.getRecord(npc_.NPCOs[i].itemName);
			if (rec != null)
			{
				if (rec.getRecordType().equals("ARMO"))
				{
					addARMO(new ARMO(rec));
				}
				else if (rec.getRecordType().equals("CLOT"))
				{
					addCLOT(new CLOT(rec));
				}
				else if (rec.getRecordType().equals("WEAP"))
				{
					addWEAP(new WEAP(rec));
				}
			}
			else
			{
				System.out.println("why is this item not found? <" + npc_.NPCOs[i].itemName + ">");
			}

		}

		//kna takes priority I wager
		//TODO: why is the female base anim so tiny compared to the other 2?
		String skeletonNifFile = ESConfig.TES_MESH_PATH + "xbase_anim" + (isBeast ? "kna.nif" : (female ? "_female.nif" : ".nif"));

		addUndressedParts();

		// hassle!
		//if (npc_.MODL != null)
		{
			//skeletonNifFile = npc_.MODL.model.str;
			//skeletonNifFile = "r\\x" + skeletonNifFile.substring(2);
			//System.out.println("MODL " + skeletonNifFile);
		}

		// interface only accepts an array for skins for now
		ArrayList<String> skins = new ArrayList<String>();
		for (String nif : skinFileNames.parts.values())
			skins.add(nif);

		nifCharacter = new NifCharacterTes3(skeletonNifFile, skins, attachFileNames, mediaSources);

		//TODO: fix up npcs so not in floor
		TransformGroup tg = new TransformGroup();
		Vector3f v = new Vector3f();
		v.y += 1.15f;
		Transform3D t = new Transform3D();
		t.set(v);
		tg.setTransform(t);
		tg.addChild(nifCharacter);

		addChild(tg);

		setOutline(new Color3f(1.0f, 1.0f, 0f));
		if (!BethRenderSettings.isOutlineChars())
			((Fadable) nifCharacter).setOutline(null);
	}

	private void addCLOT(CLOT clot)
	{
		//	System.out.println("It's an clot " + " " + clot.EDID.str);
		//	System.out.println("clot.MODL.model.str " + clot.MODL.model.str);//MODL is ground
		//	System.out.println(" " + clot.DATA.Type);

		// belts rings and amulets are not shown
		if (clot.DATA.Type != 3 && clot.DATA.Type != 8 && clot.DATA.Type != 9)
		{
			boolean hasOneNif = false;
			for (CLOT.PART p : clot.parts)
			{
				//for clot partname appears to give the actual nif file name, 
				// if shown and in the first slot
				// skinned and unskinned
				String nifFileName = "";// blank mean reserved but no nif file
				if (p.partName.length() > 0)
				{
					hasOneNif = true;
					nifFileName = "c\\" + p.partName + ".nif";
				}
				// feet and tails part of skins for beast races	
				boolean isBeast = npc_.RNAM.str.equals("Argonian") || npc_.RNAM.str.equals("Khajiit");
				if (AttachedParts.isSkinned(p.index, isBeast))
					skinFileNames.addPart(AttachedParts.getPartForLoc(p.index), nifFileName);
				else
					attachFileNames.addPart(AttachedParts.getPartForLoc(p.index), nifFileName);

			}
			if (!hasOneNif)
			{
				System.out.println("Arrgh no nif for clot clot.EDID.str=" + clot.EDID.str);
				System.out.println("clot.MODL.model.str " + clot.MODL.model.str);//MODL is ground
				System.out.println("type = " + clot.DATA.Type);
			}

			// Amulets rings and belts appear to have no worn nif file
			// thou the rings c_art_ring_denstagmer.nif could be except the normal ones have no textures
			// right hand has a bone called "Bip01 R Finger2" which might be where to attach rings?
			// I see nothing other than "Neck" bone for amulets
			// "Bip01 Spine" or "Bip01 Pelvis" would need to be the belt location
		}

	}

	private void addARMO(ARMO armo)
	{
		//	System.out.println("It's an armo " + " " + armo.EDID.str);
		//	System.out.println("armo.MODL.model.str " + armo.MODL.model.str);//MODL is ground
		//	System.out.println(" " + armo.DATA.Type);
		//	System.out.println("ITEX = Icon Filename, required " + armo.ICON.str);
		//	System.out.println("FULL " + armo.FULL.str);

		//armo.MODL.model.str a\A_Imperial_A_Gauntlet_GND.nif
		// appears in a file called  a_imperial_skins.nif
		// along with each hand and the curaiss
		// a_imperial_skirt is also in a boned and animated file
		// but others look to be in an attachments system e.g. a_imperial_ua_pauldron
		// left right gloves are separate armour entries
		// I have to separate them from each other and the curaiss

		//a_imperial_hands.1st.nif appear to be boned too obviously 

		boolean hasOneNif = false;
		for (ARMO.PART p : armo.parts)
		{
			//for armo partname appears to give the actual nif file name, 
			// if shown and in the first slot
			// skinned and unskinned
			String nifFileName = "";// blank mean reserved but no nif file
			if (p.partName.length() > 0)
			{
				hasOneNif = true;
				nifFileName = "a\\" + p.partName + ".nif";
			}

			// some known mismatches
			//a\imperial cuirass.nif a\a_imperial_gauntlet.nif both in a_imperial_skins.nif
			//a\imperial boot foot.nif in a_imperial_f_shoe.nif (f for foot, not female)

			if (p.partName.equals("imperial cuirass") || p.partName.equals("a_imperial_gauntlet"))
				nifFileName = "a\\a_imperial_skins.nif";
			if (p.partName.equals("imperial boot foot"))
				nifFileName = "a\\a_imperial_f_shoe.nif";
			if (p.partName.equals("imperial boot ankle"))
				nifFileName = "a\\a_imperial_a_boot.nif";
			if (p.partName.equals("imperial helmet"))
				nifFileName = "a\\a_imperial_m_helmet.nif";

			// feet and tails part of skins for beast races	

			if (AttachedParts.isSkinned(p.index, isBeast))
				skinFileNames.addPart(AttachedParts.getPartForLoc(p.index), nifFileName);
			else
				attachFileNames.addPart(AttachedParts.getPartForLoc(p.index), nifFileName);

		}
		if (!hasOneNif)
		{
			System.out.println("Arrgh no nif for armo.EDID.str=" + armo.EDID.str);
			System.out.println("armo.MODL.model.str " + armo.MODL.model.str);//MODL is ground
			System.out.println("type = " + armo.DATA.Type);
		}

	}

	private void addWEAP(WEAP weap)
	{
		/*weapStr = weap.MODL.model.str;
		System.out.println("It's an weap " + " " + weap.EDID.str);
		System.out.println("weap.MODL.model.str " + weap.MODL.model.str);//MODL is ground
		System.out.println(" " + weap.DATA.Type);
		System.out.println("ITEX = Icon Filename, required " + weap.ICON.str);
		System.out.println("FULL " + weap.FULL.str);*/

		// right hand has a bone called "Weapon Bone"

	}

	private void addUndressedParts()
	{

		String nif = ".nif";
		if (npc_.BNAM != null && !attachFileNames.hasPart(AttachedParts.Part.Head))
		{
			String headStr = "b\\" + npc_.BNAM.model.str + nif;
			attachFileNames.addPart(AttachedParts.Part.Head, headStr);
		}

		if (npc_.KNAM != null && !attachFileNames.hasPart(AttachedParts.Part.Hair))
		{
			String hairStr = "b\\" + npc_.KNAM.model.str + nif;
			attachFileNames.addPart(AttachedParts.Part.Hair, hairStr);
		}

		if (npc_.RNAM != null)
		{
			String pre = "b\\b_n_" + npc_.RNAM.str + "_" + (female ? "f" : "m") + "_";

			// feet and tails part of skins for beast races		 
			if (!npc_.RNAM.str.equals("Argonian") && !npc_.RNAM.str.equals("Khajiit"))
			{
				attachFileNames.addPartNoSwap(AttachedParts.Part.Right_Foot, pre + "foot" + nif);
				attachFileNames.addPartNoSwap(AttachedParts.Part.Left_Foot, pre + "foot" + nif);
			}

			attachFileNames.addPartNoSwap(AttachedParts.Part.Right_Ankle, pre + "ankle" + nif);
			attachFileNames.addPartNoSwap(AttachedParts.Part.Left_Ankle, pre + "ankle" + nif);

			if (!skinFileNames.hasPart(AttachedParts.Part.Skirt))
			{
				attachFileNames.addPartNoSwap(AttachedParts.Part.Right_Knee, pre + "knee" + nif);
				attachFileNames.addPartNoSwap(AttachedParts.Part.Left_Knee, pre + "knee" + nif);

				attachFileNames.addPartNoSwap(AttachedParts.Part.Right_Upper_Leg, pre + "upper leg" + nif);
				attachFileNames.addPartNoSwap(AttachedParts.Part.Left_Upper_Leg, pre + "upper leg" + nif);

				attachFileNames.addPartNoSwap(AttachedParts.Part.Groin, pre + "groin" + nif);
			}

			attachFileNames.addPartNoSwap(AttachedParts.Part.Neck, pre + "neck" + nif);

			attachFileNames.addPartNoSwap(AttachedParts.Part.Right_Upper_Arm, pre + "upper arm" + nif);
			attachFileNames.addPartNoSwap(AttachedParts.Part.Left_Upper_Arm, pre + "upper arm" + nif);

			String fa = pre + "forearm" + nif;
			// argonian female forearms missing, odd?
			if (npc_.RNAM.str.equals("Argonian") && female)
				fa = "b\\b_n_Argonian_m_forearm" + nif;

			attachFileNames.addPartNoSwap(AttachedParts.Part.Right_Forearm, fa);
			attachFileNames.addPartNoSwap(AttachedParts.Part.Left_Forearm, fa);

			attachFileNames.addPartNoSwap(AttachedParts.Part.Right_Wrist, pre + "wrist" + nif);
			attachFileNames.addPartNoSwap(AttachedParts.Part.Left_Wrist, pre + "wrist" + nif);

			//TODO: how the hell do I check this guy out? 
			//also hands and beast feet?
			if (!skinFileNames.hasPart(AttachedParts.Part.Chest))
			{
				skinFileNames.addPart(AttachedParts.Part.Chest, pre + "skins" + nif);
			}

		}
	}
}
