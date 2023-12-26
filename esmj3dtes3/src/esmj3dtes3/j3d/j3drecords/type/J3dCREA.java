package esmj3dtes3.j3d.j3drecords.type;

import java.util.ArrayList;
import java.util.List;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3f;

import esfilemanager.common.data.record.IRecordStore;
import esmj3d.j3d.BethRenderSettings;
import esmj3d.j3d.J3dEffectNode;
import esmj3d.j3d.j3drecords.type.J3dRECOTypeCha;
import esmj3dtes3.data.records.CREA;
import nif.character.AttachedParts;
import nif.character.NifCharacterTes3;
import nif.j3d.J3dNiAVObject;
import nif.j3d.animation.J3dNiGeomMorpherController;
import tools3d.audio.SimpleSounds;
import tools3d.utils.scenegraph.Fadable;
import utils.source.MediaSources;

public class J3dCREA extends J3dRECOTypeCha
{
	/**52 x*.kf
	 * 53 non x*.nif (golden saint.nif is extra)
	 * 52 x*.nif
	 * the kf file appear to be in the same folder same name but both have x prefix
	 * kf files have an odd format
	 * 
	 * r/xbabelfish.nif 
	 * 
	 *
	 *  x*.KF =  a set of time starts stop for each animationn  in an nitextkeyextradata which then points to ... *  
	 *  a set of animation track to bone (by order) stringextradata (in nextextradata style)
	 *  then one long animation track for each bone, done.(in nextcontroller style)
	 *  
	 *  x*.nif has bones and skins and geomorphs where is animation timings?
	 *  
	 * non-x*.nif file is animations as well, same format but no bone map as each track is
	 * directly off the bone it controls
	 * 
	 * non-x*.nif a set of time starts stop for each animation  in an nitextkeyextradata
	 * 
	 * I presume non-x*.nif this is for editing and x*.kf/nif separates are for havok?
	 * 
	 * So I need to make a tes3 animation system that use the skinning system, but has a seperate animation system
	 * 
	 * Both nifs They have geom morphs for biting etc and skin and bone data 
	 * 
	 * 
	 * xbalbelfish has a  bone system with older namings whereas fire atronach uses nearly the oblivion system
	 * test nodes only for skeleton then use that in the nifs of tes3
	 * 
	 * @param crea
	 * @param master
	 * @param makePhys 
	 * @param mediaSources
	 */

	public J3dCREA(CREA crea, IRecordStore master, boolean makePhys, MediaSources mediaSources)
	{
		super(crea, makePhys);
		if (!makePhys)
		{
			if (crea.MODL != null)
			{
				String nifFileName = crea.MODL.model.str;// both skel and skin

				// must insert x

				nifFileName = "r\\x" + nifFileName.substring(2);

				List<String> skinNifs = new ArrayList<String>();
				skinNifs.add(nifFileName);

				AttachedParts attachFileNames = new AttachedParts();
				attachFileNames.addPart(AttachedParts.Part.Root, nifFileName);

				nifCharacter = new NifCharacterTes3(nifFileName, attachFileNames, mediaSources);
			
				// for the effects below
				nifCharacter.setCapability(BranchGroup.ALLOW_DETACH);
				nifCharacter.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
				nifCharacter.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
				
				
				if (crea.scale == 1)
				{
					addChild(nifCharacter);
				}
				else
				{
					TransformGroup scaler = new TransformGroup();
					Transform3D t = new Transform3D();
					t.setScale(crea.scale);
					scaler.setTransform(t);
					addChild(scaler);
					scaler.addChild(nifCharacter);
				}

				setOutline(new Color3f(1.0f, 1.0f, 0f));
				if (!BethRenderSettings.isOutlineChars())
					((Fadable) nifCharacter).setOutline(null);
			}
			else
			{
				//CREA has no NIFs like the will o the wisp (but it has skeleton with particles effects)
				// let's do these later shall we
			}
		}

	}
	
	
	public void makeEffect(String nif, String wav, final String animation)
	{
		J3dEffectNode jen = new J3dEffectNode(nif, mediaSources);

		for (J3dNiAVObject j3dNiAVObject : jen.nvr.getNiToJ3dData().j3dNiAVObjectValues())
		{
			if (j3dNiAVObject.getJ3dNiTimeController() != null
					&& j3dNiAVObject.getJ3dNiTimeController() instanceof J3dNiGeomMorpherController)
			{
				((J3dNiGeomMorpherController) j3dNiAVObject.getJ3dNiTimeController()).fireFrameName("Frame_1", false);
			}
		}

		TransformGroup tg = new TransformGroup();
		Transform3D t = new Transform3D();
	//	t.setTranslation(new Vector3f(0, -0.9f, -0.0f));
		tg.setTransform(t);
		tg.addChild(jen);

		final BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.addChild(tg);

		BranchGroup soundBG = SimpleSounds.createPointSound(mediaSources.getSoundSource().getMediaContainer(wav), 10, 1);
		bg.addChild(soundBG);

		Thread t2 = new Thread() {
			@Override
			public void run()
			{
				try
				{
					nifCharacter.startAnimation(animation, true);
					Thread.sleep(800);
					nifCharacter.addChild(bg);
					Thread.sleep(2000);
					nifCharacter.removeChild(bg);
					nifCharacter.startAnimation("idle", true);
				}
				catch (InterruptedException e)
				{

					e.printStackTrace();
				}
			}
		};
		t2.start();

	}
	
}
