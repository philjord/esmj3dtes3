package esmj3dtes3.j3d.j3drecords.type;

import nif.NifToJ3d;
import nif.j3d.J3dNiAVObject;
import utils.source.MediaSources;
import esmLoader.common.data.record.IRecordStore;
import esmj3d.j3d.j3drecords.type.J3dRECOType;
import esmj3dtes3.data.records.CREA;

public class J3dCREA extends J3dRECOType
{
/**
 * the kf file appear to be in the same folder same name but both have x prefix
 * kf files have an odd format
 * 
 * r/xbabelfish.nif 
 * 
 * KF = A set of time starts stop for each animation
 * a set of animation track to bone (by order) then one long animation track for each bone, done.
 * 
 * So I need to make a tes3 animation system that use the skinning system, but has a seperate animation system
 * 
 * They have geom morphs for biting etc and skin and bone data 
 * 
 * Also embedded in the nif file is animations as well, same format
 * @param crea
 * @param master
 * @param mediaSources
 */
	public J3dCREA(CREA crea, IRecordStore master, MediaSources mediaSources)
	{
		super(crea, null);

		J3dNiAVObject j3dNiAVObject = null;
		if (crea.MODL != null)
		{

			String nifFileName = crea.MODL.model.str;

			if (nifFileName.length() > 0)
			{
				j3dNiAVObject = NifToJ3d.loadShapes(nifFileName, mediaSources.getMeshSource(), mediaSources.getTextureSource())
						.getVisualRoot();
			}

			if (j3dNiAVObject != null)
			{
				addChild(j3dNiAVObject);
			}
		}
	}

}
