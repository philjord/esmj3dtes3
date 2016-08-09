package esmj3dtes3.j3d.j3drecords.type;

import esmj3d.j3d.j3drecords.type.J3dRECOType;
import esmj3dtes3.data.records.SOUN;
import utils.ESConfig;
import utils.source.MediaSources;

/**Notice not from J3dGeneralSOUN at this point as that's a bit odd
 * This is for looping sounds, single play are handled in each type directly
 * @author phil
 *
 */
public class J3dSOUN extends J3dRECOType
{
	public J3dSOUN(SOUN soun, MediaSources mediaSources)
	{
		super(soun, null, mediaSources);
		if (soun.FNAM != null)
		{
			//TODO: min range as well
			playSound("Sound\\" + soun.FNAM.str, soun.MaxRange * ESConfig.ES_TO_METERS_SCALE, -1, soun.Volume / 255f);
		}
	}

	@Override
	public void setOutlined(boolean b)
	{
		// hahaha, though possibly...
	}
}
