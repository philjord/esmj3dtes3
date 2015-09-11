package esmj3dtes3.j3d.j3drecords.type;

import nif.NifToJ3d;
import nif.j3d.J3dNiAVObject;
import utils.source.MediaSources;
import esmLoader.common.data.record.IRecordStore;
import esmj3d.j3d.j3drecords.type.J3dRECOType;
import esmj3dtes3.data.records.NPC_;

public class J3dNPC_ extends J3dRECOType
{
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

		String nif = ".nif";
		if (npc_.BNAM != null)
		{

			String nifFileName = "b\\" + npc_.BNAM.model.str + nif;

			if (nifFileName.length() > 0)
			{
				J3dNiAVObject j3dNiAVObject = NifToJ3d.loadShapes(nifFileName, mediaSources.getMeshSource(),
						mediaSources.getTextureSource()).getVisualRoot();
				if (j3dNiAVObject != null)
				{
					addChild(j3dNiAVObject);
				}
			}

		}

		if (npc_.KNAM != null)
		{

			String nifFileName = "b\\" + npc_.KNAM.model.str + nif;

			if (nifFileName.length() > 0)
			{
				J3dNiAVObject j3dNiAVObject = NifToJ3d.loadShapes(nifFileName, mediaSources.getMeshSource(),
						mediaSources.getTextureSource()).getVisualRoot();
				if (j3dNiAVObject != null)
				{
					addChild(j3dNiAVObject);
				}
			}

		}
		if (npc_.RNAM != null)
		{
			String[] bodyparts = new String[]
			{ "ankle", "foot", "forearm", "groin", "knee", "neck", "skins", "upper arm", "upper leg", "wrist" };

			String pre = "b\\b_n_" + npc_.RNAM + "_" + (((npc_.FLAG & 0x1) != 0) ? "f" : "m") + "_";

			for (String bp : bodyparts)
			{

				String nifFileName = pre + bp + nif;

				if (nifFileName.length() > 0)
				{
					J3dNiAVObject j3dNiAVObject = NifToJ3d.loadShapes(nifFileName, mediaSources.getMeshSource(),
							mediaSources.getTextureSource()).getVisualRoot();
					if (j3dNiAVObject != null)
					{
						addChild(j3dNiAVObject);
					}
				}

			}
		}
	}

}
