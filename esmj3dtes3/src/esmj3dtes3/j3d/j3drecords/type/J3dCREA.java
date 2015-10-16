package esmj3dtes3.j3d.j3drecords.type;

import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;

import nif.character.NifCharacterTes3;
import tools3d.utils.scenegraph.Fadable;
import utils.source.MediaSources;
import esmLoader.common.data.record.IRecordStore;
import esmj3d.j3d.BethRenderSettings;
import esmj3d.j3d.j3drecords.type.J3dRECOTypeCha;
import esmj3dtes3.data.records.CREA;

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
	 * non-x*.nif a set of time starts stop for each animationn  in an nitextkeyextradata
	 * 
	 * I presume non-x*.nif this is for editing and x*.kf/nif seperates are for havok?
	 * 
	 * So I need to make a tes3 animation system that use the skinning system, but has a seperate animation system
	 * 
	 * Both nifs They have geom morphs for biting etc and skin and bone data 
	 * 
	 * 
	 * xbalbelfish has a  bone system with older namings whereas fire atronach uses nearly the oblivion system
	 * test nodes only for skeleton then use taht in the nifs of tes3
	 * 
	 * @param crea
	 * @param master
	 * @param mediaSources
	 */

	public J3dCREA(CREA crea, IRecordStore master, MediaSources mediaSources)
	{
		super(crea);

		if (crea.MODL != null)
		{
			String nifFileName = crea.MODL.model.str;// both skel and skin

			// must insert x

			nifFileName = "r\\x" + nifFileName.substring(2);

			List<String> skinNifs = new ArrayList<String>();
			skinNifs.add(nifFileName);
			nifCharacter = new NifCharacterTes3(nifFileName, skinNifs, mediaSources);

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
