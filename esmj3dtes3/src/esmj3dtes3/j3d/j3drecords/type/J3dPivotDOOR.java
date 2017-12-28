package esmj3dtes3.j3d.j3drecords.type;

import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;

import esmio.common.data.record.Record;
import esmio.tes3.IRecordStoreTes3;
import esmj3d.j3d.BethRenderSettings;
import esmj3d.j3d.j3drecords.Doorable;
import esmj3d.j3d.j3drecords.type.J3dRECOType;
import esmj3dtes3.data.records.DOOR;
import esmj3dtes3.data.records.SOUN;
import nif.NifToJ3d;
import tools3d.utils.TimedRunnableBehavior;
import tools3d.utils.scenegraph.Fadable;
import utils.source.MediaSources;

public class J3dPivotDOOR extends J3dRECOType implements Doorable
{
	private boolean isOpen = false;

	private boolean outlineSetOn = false;

	private Color3f outlineColor = new Color3f(1.0f, 0.5f, 0f);

	private Alpha alpha;

	private TimedRunnableBehavior pivotBehavior = new TimedRunnableBehavior(10);

	private TransformGroup doorPivot = new TransformGroup();

	private DOOR door;

	private SOUN openSOUN;

	private SOUN closeSOUN;

	public J3dPivotDOOR(DOOR door, boolean makePhys, MediaSources mediaSources, IRecordStoreTes3 master)
	{
		super(door, door.MODL.model.str, mediaSources);
		this.door = door;

		this.setCapability(Group.ALLOW_CHILDREN_WRITE);
		this.setCapability(Group.ALLOW_CHILDREN_EXTEND);

		if (makePhys)
		{
			j3dNiAVObject = NifToJ3d.loadHavok(door.MODL.model.str, mediaSources.getMeshSource()).getHavokRoot();
		}
		else
		{
			j3dNiAVObject = NifToJ3d.loadShapes(door.MODL.model.str, mediaSources.getMeshSource(), mediaSources.getTextureSource())
					.getVisualRoot();
		}

		if (j3dNiAVObject != null)
		{
			//prep for possible outlines later
			if (j3dNiAVObject instanceof Fadable && !makePhys)
			{
				((Fadable) j3dNiAVObject).setOutline(outlineColor);
				if (!BethRenderSettings.isOutlineDoors())
					((Fadable) j3dNiAVObject).setOutline(null);
			}

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

		Record openSoundRecord = master.getRecord(door.SNAM.str);
		if (openSoundRecord != null)
		{
			openSOUN = new SOUN(openSoundRecord);
		}
		Record closeSoundRecord = master.getRecord(door.ANAM.str);
		if (closeSoundRecord != null)
		{
			closeSOUN = new SOUN(closeSoundRecord);
		}

	}

	@Override
	public void renderSettingsUpdated()
	{
		super.renderSettingsUpdated();
		if (j3dNiAVObject != null)
		{
			if (j3dNiAVObject instanceof Fadable)
			{
				Color3f c = BethRenderSettings.isOutlineDoors() || outlineSetOn ? outlineColor : null;
				((Fadable) j3dNiAVObject).setOutline(c);
			}
		}
	}

	@Override
	public void setOutlined(boolean b)
	{
		outlineSetOn = b;
		if (j3dNiAVObject != null)
		{
			if (j3dNiAVObject instanceof Fadable)
			{
				Color3f c = BethRenderSettings.isOutlineDoors() || outlineSetOn ? outlineColor : null;
				((Fadable) j3dNiAVObject).setOutline(c);
			}
		}
	}

	@Override
	public void toggleOpen()
	{
		isOpen = !isOpen;
		animateDoor();
	}

	public void setOpen(boolean isOpen)
	{
		this.isOpen = isOpen;
		animateDoor();
	}

	@Override
	public boolean isOpen()
	{
		return isOpen;
	}

	/**
	 * called after open is set
	 */

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
				a = isOpen ? a : (Math.PI / 2f) - a;
				t.rotY(a);
				doorPivot.setTransform(t);
			}
		};
		pivotBehavior.start(50, callback);
		playSound();
	}

	@Override
	public void playBothSounds()
	{
		// doesn't work because this gets detached immedately on a move
		Thread t = new Thread() {
			@Override
			public void run()
			{
				if (openSOUN != null)
				{
					if (openSOUN.Volume == -1)
						playBackgroundSound("Sound\\" + openSOUN.FNAM.str, 1, 1.0f);
					else
						System.err.println("Pivot door sound volume not -1!! " + openSOUN);
				}
				try
				{
					Thread.sleep(2000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				if (closeSOUN != null)
				{
					if (closeSOUN.Volume == -1)
						playBackgroundSound("Sound\\" + closeSOUN.FNAM.str, 1, 1.0f);
					else
						System.err.println("Pivot door sound volume not -1!! " + closeSOUN);
				}

			}
		};
		t.start();
	}

	public void playSound()
	{
		//notice reversed because value is set straight away
		if (!isOpen)
		{
			if (closeSOUN != null)
			{
				if (closeSOUN.Volume == -1)
					playBackgroundSound("Sound\\" + closeSOUN.FNAM.str, 1, 1.0f);
				else
					System.err.println("Pivot door sound volume not -1!! " + closeSOUN);
			}
		}
		else
		{
			if (openSOUN != null)
			{
				if (openSOUN.Volume == -1)
					playBackgroundSound("Sound\\" + openSOUN.FNAM.str, 1, 1.0f);
				else
					System.err.println("Pivot door sound volume not -1!! " + openSOUN);
			}
		}

	}

	@Override
	public String getDoorName()
	{
		if (door.FULL != null)
			return door.FULL.str;
		return "";

	}

}
