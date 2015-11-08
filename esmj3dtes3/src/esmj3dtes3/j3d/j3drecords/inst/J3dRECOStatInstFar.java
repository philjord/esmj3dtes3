package esmj3dtes3.j3d.j3drecords.inst;

import javax.media.j3d.BranchGroup;

import esmj3d.data.shared.records.InstRECO;
import esmj3d.j3d.j3drecords.inst.J3dRECOStatInst;
import esmj3d.j3d.j3drecords.type.J3dRECOType;

public class J3dRECOStatInstFar extends J3dRECOStatInst
{
	private float size = 1;
	
	private int sizeMultiplierForFade = 35;

	public J3dRECOStatInstFar(InstRECO instRECO, float size)
	{
		super(instRECO, true, false);
		this.size = size;
	}

	@Override
	public void renderSettingsUpdated()
	{
		super.renderSettingsUpdated();
		//sizeMultiplierForFade = ...
		if (dl != null)
		{
			dl.setDistance(0, size * sizeMultiplierForFade);
		}
	}

	public void setJ3dRECOType(J3dRECOType j3dRECOType)
	{
		super.setJ3dRECOType(j3dRECOType, new BranchGroup());
		if (dl != null)
		{
			dl.setDistance(0, size * sizeMultiplierForFade);
		}
	}

}
