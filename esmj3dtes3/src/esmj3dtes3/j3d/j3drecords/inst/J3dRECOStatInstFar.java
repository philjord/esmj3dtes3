package esmj3dtes3.j3d.j3drecords.inst;

import org.jogamp.java3d.BranchGroup;

import esmj3d.data.shared.records.InstRECO;
import esmj3d.j3d.BethRenderSettings;
import esmj3d.j3d.j3drecords.inst.J3dRECOStatInst;
import esmj3d.j3d.j3drecords.type.J3dRECOType;

public class J3dRECOStatInstFar extends J3dRECOStatInst
{
	private float size = 1;

	public static float FAR_FADE_MULTIPLY = 0.1f;// a size 10 (small ish) fades at the same rate as near objects

	public J3dRECOStatInstFar(InstRECO instRECO, float size)
	{
		super(instRECO, true, true, false);
		this.size = size;
	}

	@Override
	public void renderSettingsUpdated()
	{
		super.renderSettingsUpdated();
		if (dl != null)
		{
			dl.setDistance(0, BethRenderSettings.getObjectFade() * (size * FAR_FADE_MULTIPLY));
		}
		if (j3dRECOType != null)
		{
			j3dRECOType.renderSettingsUpdated();
		}
	}

	@Override
	public void setJ3dRECOType(J3dRECOType j3dRECOType)
	{
		BranchGroup blank = new BranchGroup();
		super.setJ3dRECOType(j3dRECOType, blank);
		if (dl != null)
		{
			dl.setDistance(0, BethRenderSettings.getObjectFade() * (size * FAR_FADE_MULTIPLY));
		}
	}

}
