package esmj3dtes3.j3d.j3drecords.type;

import esfilemanager.common.data.record.Record;
import esfilemanager.tes3.IRecordStoreTes3;
import esmj3d.j3d.j3drecords.type.J3dGeneralLIGH;
import esmj3dtes3.data.records.LIGH;
import esmj3dtes3.data.records.SOUN;
import utils.source.MediaSources;

public class J3dLIGH extends J3dGeneralLIGH
{
	public J3dLIGH(LIGH ligh, boolean makePhys, MediaSources mediaSources, IRecordStoreTes3 master)
	{
		super(ligh, makePhys, mediaSources);

		if (!makePhys)
		{
			if (ligh.SNAM != null)
			{
				Record soundRecord = master.getRecord(ligh.SNAM.str);
				if (soundRecord != null)
				{
					SOUN soun = new SOUN(soundRecord);
					J3dSOUN j3dSOUN = new J3dSOUN(soun, mediaSources);
					addChild(j3dSOUN);
				}
			}
		}
	}
}
