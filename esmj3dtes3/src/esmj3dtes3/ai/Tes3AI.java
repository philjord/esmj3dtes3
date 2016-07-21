package esmj3dtes3.ai;

import javax.vecmath.Vector3f;

import esmj3d.ai.AIActor;
import esmj3d.ai.AIThinker;
import esmj3d.data.shared.records.InstRECO;
import esmj3d.j3d.cell.AICellGeneral;
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

	private static Vector3f zeroLoc = new Vector3f(0, 0, 0);

	private Vector3f groundTestUpper = new Vector3f();
	private Vector3f groundTestLower = new Vector3f();
	private Vector3f adjustedHit = new Vector3f();

	public int noGroundFoundCount = 0;

	protected void settleOnGround(PhysicsSystemInterface clientPhysicsSystem)
	{
		if (!groundChecked || noGroundFoundCount > 10)
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
					aiCellGeneral.setLocationForActor(this, location, yawPitch);

					//note groundChecked not set true in case more adjustment required
				}
			}

		}
	}
}
