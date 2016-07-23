package esmj3dtes3.j3d.character;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import esmj3dtes3.character.CharacterSheet;
import esmj3dtes3.data.records.ARMO;
import esmj3dtes3.data.records.CLOT;
import esmj3dtes3.data.records.NPCO;
import esmj3dtes3.data.records.WEAP;
import esmj3dtes3.j3d.j3drecords.type.J3dNPC_;
import esmmanager.common.data.record.IRecordStore;
import esmmanager.common.data.record.Record;
import esmmanager.tes3.IRecordStoreTes3;
import nif.character.AttachedParts;
import nif.character.AttachedParts.Part;
import nif.character.NifCharacterTes3;
import utils.ESConfig;
import utils.source.MediaSources;

public class CharacterAvatar extends BranchGroup
{
	private boolean firstPerson = true;

	private CharacterSheet characterSheet;

	private NifCharacterTes3 nifCharacter;

	private IRecordStoreTes3 recordStoreTes3;

	private static String nif = ".nif";

	private AttachedParts attachFileNames = new AttachedParts();

	public CharacterAvatar(CharacterSheet characterSheet, IRecordStore master, MediaSources mediaSources, boolean firstPerson)
	{
		this.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		this.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

		this.firstPerson = firstPerson;
		this.characterSheet = characterSheet;
		this.recordStoreTes3 = (IRecordStoreTes3) master;

		//SoundGen labels are just record indicators
		/*	Record rec2 = recordStoreTes3.getRecord("Land");
			if (rec2 != null && rec2.getRecordType().equals("SOUN"))
			{
				SOUN soun = new SOUN(rec2);
				soun.getClass();
			}
			else
			{
				System.out.println("Not a SOUN");
			}*/

		for (NPCO npco : characterSheet.getNPCOs())
		{
			Record rec = recordStoreTes3.getRecord(npco.itemName);
			if (rec != null)
			{
				if (rec.getRecordType().equals("ARMO"))
				{
					J3dNPC_.addARMO(new ARMO(rec), attachFileNames, recordStoreTes3, firstPerson);
				}
				else if (rec.getRecordType().equals("CLOT"))
				{
					J3dNPC_.addCLOT(new CLOT(rec), attachFileNames, recordStoreTes3, firstPerson);
				}
				else if (rec.getRecordType().equals("WEAP"))
				{
					J3dNPC_.addWEAP(new WEAP(rec), attachFileNames, recordStoreTes3);
				}
				else
				{
					System.out.println("what is this? <" + npco.itemName + ">" + rec.getRecordType());
				}
			}
			else
			{
				System.out.println("why is this item not found? <" + npco.itemName + ">");
			}

		}
		String skeletonNifFile = ESConfig.TES_MESH_PATH + "xbase_anim"
				+ (characterSheet.isBeast() ? "kna.nif" : firstPerson ? ".1st.nif" : (characterSheet.isFemale() ? "_female.nif" : ".nif"));

		addUndressedParts();

		nifCharacter = new NifCharacterTes3(skeletonNifFile, attachFileNames, mediaSources);

		//TODO: do I need a pelvis adjustment?
		TransformGroup tg = new TransformGroup();
		Vector3f v = new Vector3f();
		//v.y += 1.15f;
		Transform3D t = new Transform3D();
		t.set(v);
		tg.setTransform(t);
		tg.addChild(nifCharacter);

		addChild(tg);

	}

	private void addUndressedParts()
	{
		if (!firstPerson)
		{
			if (characterSheet.getHeadNif() != null)
			{
				// if head has been replaced by a full helmet already don't add hair
				if (!attachFileNames.hasPart(AttachedParts.Part.Head))
					addAttachNoSwap(AttachedParts.Part.Head, "b\\" + characterSheet.getHeadNif() + nif);
			}

			if (characterSheet.getHairNif() != null)
			{
				addAttachNoSwap(AttachedParts.Part.Hair, "b\\" + characterSheet.getHairNif() + nif);
			}
		}

		if (characterSheet.getRaceName() != null)
		{
			String pre = "b\\b_n_" + characterSheet.getRaceName() + "_" + (characterSheet.isFemale() ? "f" : "m") + "_";

			if (!firstPerson)
			{
				// feet and tails part of skins for beast races		 
				if (!characterSheet.getRaceName().equals("Argonian") && !characterSheet.getRaceName().equals("Khajiit"))
				{
					addAttachNoSwap(AttachedParts.Part.Right_Foot, pre + "foot" + nif);
					addAttachNoSwap(AttachedParts.Part.Left_Foot, pre + "foot" + nif);
				}

				addAttachNoSwap(AttachedParts.Part.Right_Ankle, pre + "ankle" + nif);
				addAttachNoSwap(AttachedParts.Part.Left_Ankle, pre + "ankle" + nif);

				if (!attachFileNames.hasPart(AttachedParts.Part.Skirt))
				{
					addAttachNoSwap(AttachedParts.Part.Right_Knee, pre + "knee" + nif);
					addAttachNoSwap(AttachedParts.Part.Left_Knee, pre + "knee" + nif);

					addAttachNoSwap(AttachedParts.Part.Right_Upper_Leg, pre + "upper leg" + nif);
					addAttachNoSwap(AttachedParts.Part.Left_Upper_Leg, pre + "upper leg" + nif);

					addAttachNoSwap(AttachedParts.Part.Groin, pre + "groin" + nif);
				}

				addAttachNoSwap(AttachedParts.Part.Neck, pre + "neck" + nif);
			}

			addAttachNoSwap(AttachedParts.Part.Right_Upper_Arm, pre + "upper arm" + nif);
			addAttachNoSwap(AttachedParts.Part.Left_Upper_Arm, pre + "upper arm" + nif);

			String fa = pre + "forearm" + nif;
			// argonian female forearms missing, odd?
			if (characterSheet.getRaceName().equals("Argonian") && characterSheet.isFemale())
				fa = "b\\b_n_Argonian_m_forearm" + nif;

			addAttachNoSwap(AttachedParts.Part.Right_Forearm, fa);
			addAttachNoSwap(AttachedParts.Part.Left_Forearm, fa);

			addAttachNoSwap(AttachedParts.Part.Right_Wrist, pre + "wrist" + nif);
			addAttachNoSwap(AttachedParts.Part.Left_Wrist, pre + "wrist" + nif);

			//also hands and beast feet?
			if (!firstPerson)
			{
				//also hands and beast feet?
				addAttachNoSwap(AttachedParts.Part.Chest, pre + "skins" + nif);
				addAttachNoSwap(AttachedParts.Part.Right_Hand, pre + "skins" + nif);
				addAttachNoSwap(AttachedParts.Part.Left_Hand, pre + "skins" + nif);

				// feet and tails part of skins for beast races		 
				if (!characterSheet.getRaceName().equals("Argonian") && !characterSheet.getRaceName().equals("Khajiit"))
				{
					addAttachNoSwap(AttachedParts.Part.Right_Foot, pre + "foot" + nif);
					addAttachNoSwap(AttachedParts.Part.Left_Foot, pre + "foot" + nif);
				}
				else
				{
					addAttachNoSwap(AttachedParts.Part.Right_Foot, pre + "skins" + nif);
					addAttachNoSwap(AttachedParts.Part.Left_Foot, pre + "skins" + nif);
					addAttachNoSwap(AttachedParts.Part.Tail, pre + "skins" + nif);
				}

			}
			else
			{
				// should be left hand and right hand
				if (characterSheet.getRaceName().equals("Breton"))// god dam!
					addAttachNoSwap(AttachedParts.Part.Chest, pre + "hand.1st" + nif);
				else
					addAttachNoSwap(AttachedParts.Part.Chest, pre + "hands.1st" + nif);
			}

		}

	}

	private void addAttachNoSwap(Part part, String nifFileName)
	{
		if (!attachFileNames.hasPart(part))
			attachFileNames.addPart(part, nifFileName);
	}

	public boolean isFirstPerson()
	{
		return firstPerson;
	}

	public void setFirstPerson(boolean firstPerson)
	{
		this.firstPerson = firstPerson;

		//TODO: rejig the model to be thrid person now
	}

	public void playAnimation(String animationName, boolean looping)
	{
		System.out.println("animationName " + animationName);
		nifCharacter.startAnimation(animationName, false);

		/*	
			J3dNiSequenceStreamHelper j3dNiSequenceStreamHelper = nifCharacter.getJ3dNiSequenceStreamHelper();
			J3dNiControllerSequenceTes3 seq = j3dNiSequenceStreamHelper.getSequence(animationName);
			if (seq != null)
			{
				if (!seq.isLive())
				{
					BranchGroup newKfBg = seq.getBranchGroup();
					addChild(newKfBg);
				}
		
				seq.fireSequence(!looping, 0);
			}
			else
			{
				System.out.println("Bad animation name " + animationName);
			}
		
			//for (String fireName : j3dNiSequenceStreamHelper.getAllSequences())
			//	System.out.println("Seq: " + fireName);
			 
			 */
	}
}
