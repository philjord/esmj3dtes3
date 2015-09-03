package esmj3dtes3.j3d.j3drecords.type;

import javax.media.j3d.BoundingLeaf;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Light;
import javax.media.j3d.PointLight;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import nif.NifJ3dHavokRoot;
import nif.NifToJ3d;
import nif.j3d.J3dNiAVObject;
import utils.ESConfig;
import utils.source.MediaSources;
import esmLoader.EsmFileLocations;
import esmj3d.j3d.j3drecords.type.J3dRECOType;
import esmj3dtes3.data.records.LIGH;

public class J3dLIGH extends J3dRECOType
{

	public J3dLIGH(LIGH ligh, boolean makePhys, MediaSources mediaSources)
	{
		super(ligh, ligh.MODL == null ? "" : ligh.MODL.model.str);

		J3dNiAVObject j3dNiAVObject = null;
		if (ligh.MODL != null)
		{

			String nifFileName = ligh.MODL.model.str;
			if (makePhys)
			{
				NifJ3dHavokRoot hr = NifToJ3d.loadHavok(nifFileName, mediaSources.getMeshSource());
				if (hr != null)
				{
					j3dNiAVObject = hr.getHavokRoot();
				}
			}
			else
			{
				if (nifFileName.length() > 0)
				{
					j3dNiAVObject = NifToJ3d.loadShapes(nifFileName, mediaSources.getMeshSource(), mediaSources.getTextureSource())
							.getVisualRoot();
				}

				if (EsmFileLocations.ESM_MAKE_J3D_POINTLIGHTS)
				{
					Light light = new PointLight(true, ligh.color, new Point3f(0, 0, 0), new Point3f(1, 0, 0));
					//	Light light = new SpotLight(true, ligh.color, new Point3f(0, 0, 0), new Point3f(1, ligh.falloffExponent, 0), new Vector3f(0, 0, -1),
					//			ligh.fieldOfView, ligh.fade);
					BoundingLeaf bl = new BoundingLeaf();
					bl.setRegion(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), ligh.radius * ESConfig.ES_TO_METERS_SCALE));
					light.setInfluencingBoundingLeaf(bl);
					addChild(bl);
					addChild(light);
				}

			}

			if (j3dNiAVObject != null)
			{
				addChild(j3dNiAVObject);
			}

		}

	}
}
