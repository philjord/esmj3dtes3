package esmj3dtes3.ai;

import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3f;

import esmj3d.ai.AIActor;
import esmj3d.ai.AIThinker;
import esmj3d.data.shared.records.InstRECO;
import esmj3d.j3d.cell.AICellGeneral;
import esmj3d.j3d.j3drecords.inst.J3dRECOChaInst;
import esmj3d.physics.PhysicsSystemInterface;
import esmj3d.physics.RayIntersectResult;
import esmj3dtes3.data.records.REFR;
import tools3d.utils.YawPitch;

public abstract class Tes3AI implements AIActor, AIThinker
{
	protected REFR instRECO;

	protected AICellGeneral aiCellGeneral;

	protected Vector3f location = new Vector3f();
	protected YawPitch yawPitch = new YawPitch();

	// to be cleared after any x/z move so act can ensure accuracy
	private boolean groundChecked = false;

	public Tes3AI(InstRECO instRECO, AICellGeneral aiCellGeneral)
	{
		this.instRECO = (REFR) instRECO;
		this.aiCellGeneral = aiCellGeneral;
	}

	// lets see if visual disagrees with ai and if so let's assume animations have translated us
	protected void updateLocation(PhysicsSystemInterface clientPhysicsSystem)
	{
		// notice settle is after update from vis as it take proprity
		boolean updated = updateFromVisualLocation(clientPhysicsSystem);
		updated |= settleOnGround(clientPhysicsSystem);
		updated |= doRotation(clientPhysicsSystem);
		if (updated)
		{
			aiCellGeneral.setLocationForActor(this, location, yawPitch);
		}
	}

	private static Vector3f zeroLoc = new Vector3f(0, 0, 0);

	private Vector3f groundTestUpper = new Vector3f();
	private Vector3f groundTestLower = new Vector3f();
	private Vector3f adjustedHit = new Vector3f();

	public int noGroundFoundCount = 0;

	private boolean settleOnGround(PhysicsSystemInterface clientPhysicsSystem)
	{
		if (!groundChecked || noGroundFoundCount <= 10)
		{

			if (noGroundFoundCount == 10)
			{
				System.out.println(
						"noGroundFoundCount for  " + instRECO.NAMEref.str + " at " + location + " cell " + aiCellGeneral.getCellLocation());
			}

			// first check no overhead(in case of floors etc
			groundTestUpper.set(location);
			groundTestUpper.y += 0;
			groundTestLower.set(location);
			groundTestLower.y -= 20;
			RayIntersectResult result = clientPhysicsSystem.findRayIntersectResult(groundTestUpper, groundTestLower, instRECO.formId);

			if (result != null && result.hitPointWorld.equals(zeroLoc))
			{
				// let's see if we are a bit under ground and try again
				groundTestUpper.y += 5;

				result = clientPhysicsSystem.findRayIntersectResult(groundTestUpper, groundTestLower, instRECO.formId);

				if (result.hitPointWorld.equals(zeroLoc))
				{
					result = null;// indicate no solution
					noGroundFoundCount++;// but we'll try a few more times
				}
			}
			else
			{
				//probably just the physic system not loaded up yet
				//System.out.println("null result; can't find the ground for  " + instRECO.NAMEref.str + " at " + location + " cell "
				//		+ aiCellGeneral.getCellLocation());
			}

			if (result != null)
			{
				//TODO: possibly take height of character into account

				adjustedHit.set(result.hitPointWorld.x, result.hitPointWorld.y, result.hitPointWorld.z);
				if (location.epsilonEquals(adjustedHit, 0.05f))
				{
					groundChecked = true;
				}
				else
				{

					setLocation(adjustedHit.x, adjustedHit.y, adjustedHit.z, (float) yawPitch.getYaw(), (float) yawPitch.getPitch());
					return true;
					//note groundChecked not set true in case more adjustment required
				}
			}

		}

		return false;
	}

	// lets see if visual disagrees with ai and if so let's assume animations have translated us
	private boolean updateFromVisualLocation(PhysicsSystemInterface clientPhysicsSystem)
	{
		J3dRECOChaInst visual = aiCellGeneral.getVisualActor(this);
		if (visual != null)
		{
			Transform3D t = new Transform3D();
			visual.getLocation(t);
			Vector3f v = new Vector3f();
			t.get(v);
			if (!location.epsilonEquals(v, 0.05f))
			{
				groundChecked = false;
				location.set(v);
				return true;
			}
		}
		else
		{
			//System.out.println("Why you null visual??");
		}
		return false;

	}

	protected long turnStart = 0;
	protected long turnForMS = 0;
	protected float turnPerMSRads = 0; //+- for direction

	private boolean doRotation(PhysicsSystemInterface clientPhysicsSystem)
	{
		// need to turn myself and then send that through to everyone!
		if (turnStart > 0 && turnForMS > 0)
		{
			long turningTimeMS = System.currentTimeMillis() - turnStart;
			if (turningTimeMS > turnForMS)
				turningTimeMS = turnForMS;

			float turnRads = turnPerMSRads * turningTimeMS;
			yawPitch.setYaw(yawPitch.getYaw() + turnRads);

			turnStart = System.currentTimeMillis();
			turnForMS -= turningTimeMS;
			return true;
		}
		return false;
	}
}
