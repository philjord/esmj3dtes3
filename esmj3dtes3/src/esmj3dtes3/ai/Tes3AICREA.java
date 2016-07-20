package esmj3dtes3.ai;

import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3f;

import esmj3d.ai.AIActor;
import esmj3d.ai.AIThinker;
import esmj3d.ai.PathGridInterface;
import esmj3d.data.shared.records.InstRECO;
import esmj3d.j3d.cell.AICellGeneral;
import esmj3d.j3d.j3drecords.inst.J3dRECOChaInst;
import esmj3d.physics.PhysicsSystemInterface;
import esmj3d.physics.RayIntersectResult;
import esmj3dtes3.data.records.CREA;
import esmj3dtes3.data.records.REFR;
import tools3d.utils.YawPitch;

public class Tes3AICREA implements AIActor, AIThinker
{
	private REFR instRECO;
	private CREA crea;
	private AICellGeneral aiCellGeneral;

	private Vector3f location = new Vector3f();
	private YawPitch yawPitch = new YawPitch();

	// to be cleared after any x/z move so act can ensure accuracy
	private boolean groundChecked = false;

	public Tes3AICREA(InstRECO instRECO, CREA crea, AICellGeneral aiCellGeneral)
	{
		this.instRECO = (REFR) instRECO;
		this.crea = crea;
		this.aiCellGeneral = aiCellGeneral;
		setLocation(instRECO);
	}

	@Override
	public void act(Vector3f charLocation, PathGridInterface pgi, PhysicsSystemInterface clientPhysicsSystem)
	{
		if (!groundChecked)
		{

			// first check no overhead(in case of floors etc
			Vector3f groundTestUpper = new Vector3f(location);
			groundTestUpper.y += 0;
			Vector3f groundTestLower = new Vector3f(location);
			groundTestLower.y -= 20;
			RayIntersectResult result = clientPhysicsSystem.findRayIntersectResult(groundTestUpper, groundTestLower, instRECO.formId);

			if (result != null && result.hitPointWorld.equals(new Vector3f(0, 0, 0)))
			{
				// let's see if we are a bit under ground and try again
				groundTestUpper.y += 5;

				result = clientPhysicsSystem.findRayIntersectResult(groundTestUpper, groundTestLower, instRECO.formId);

				if (result.hitPointWorld.equals(new Vector3f(0, 0, 0)))
				{
					System.out.println("0,0,0; can't find the ground for  " + instRECO.NAMEref.str + " at " + location + " cell "
							+ aiCellGeneral.getCellLocation());
					result = null;// indicate no solution
				}
			}

			if (result != null)
			{
				//TODO: possibly take height of character into account

				Vector3f adjustedHit = new Vector3f(result.hitPointWorld.x, result.hitPointWorld.y, result.hitPointWorld.z);
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
			else
			{
				//probably just the physic system not loaded up yet
				//	System.out.println("null result; can't find the ground for  " + instRECO.NAMEref.str + " at " + location + " cell "
				//			+ aiCellGeneral.getCellLocation());
			}
		}
	}

	@Override
	public void think(Vector3f charLocation, PathGridInterface pgi, PhysicsSystemInterface clientPhysicsSystem)
	{

	}

	/**
	 * Note in J3d format not esm
	 * @param x
	 * @param y
	 * @param z
	 * @param rx
	 * @param ry
	 * @param rz
	 * @param scale
	 */
	@Override
	public void setLocation(float x, float y, float z, float yaw, float pitch)
	{
		location.set(x, y, z);
		yawPitch.set(yaw, pitch);
	}

	public void setLocation(InstRECO ir)
	{
		Vector3f t = ir.getTrans();
		Vector3f er = ir.getEulerRot();
		Transform3D t2 = J3dRECOChaInst.toJ3d(t.x, t.y, t.z, er.x, er.y, er.z, ir.getScale());
		t2.get(location);
		yawPitch.set(t2);

	}

	@Override
	public int getActorFormId()
	{
		return instRECO.formId;
	}
}
