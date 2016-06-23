package esmj3dtes3.j3d.j3drecords.type;

import javax.media.j3d.Alpha;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import esmj3d.data.shared.records.GenericDOOR;
import esmj3d.j3d.j3drecords.type.J3dDOOR;
import tools3d.utils.TimedRunnableBehavior;
import utils.source.MediaSources;

public class J3dPivotDOOR extends J3dDOOR
{
	private Alpha alpha;

	private TimedRunnableBehavior pivotBehavior = new TimedRunnableBehavior(10);

	private TransformGroup doorPivot = new TransformGroup();

	public J3dPivotDOOR(GenericDOOR reco, boolean makePhys, MediaSources mediaSources)
	{
		super(reco, makePhys, mediaSources, true);

		if (j3dNiAVObject != null)
		{
			//TES3 has no animations for doors, so I'ma just pivot around Y!
			if (j3dNiAVObject.getJ3dNiControllerManager() == null)
			{
				doorPivot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
				doorPivot.addChild(j3dNiAVObject);
				addChild(doorPivot);

				doorPivot.addChild(pivotBehavior);
			}
			else
			{
				addChild(j3dNiAVObject);
			}
			fireIdle();
		}

	}
 

	/**
	 * called after open is set
	 */
	@Override
	protected void animateDoor()
	{
		//wow TES3 door have no animation, they look like they just artificially pivot around 
		alpha = new Alpha(1, 500);
		alpha.setStartTime(System.currentTimeMillis());

		Runnable callback = new Runnable() {
			@Override
			public void run()
			{
				Transform3D t = new Transform3D();
				double a = (Math.PI / 2f) * alpha.value();
				a = isOpen() ? a : (Math.PI / 2f) - a;
				t.rotY(a);
				doorPivot.setTransform(t);				
			}
		};
		pivotBehavior.start(50, callback);
	}

}
