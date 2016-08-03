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
import esmj3dtes3.data.records.NPC_;
import nif.character.NifCharacter;
import nif.j3d.animation.J3dNiControllerSequence;

public class Tes3AINPC_ extends Tes3AI implements AIActor, AIThinker
{
	private NPC_ npc_;

	private long initTime = 0;

	public Tes3AINPC_(InstRECO instRECO, NPC_ npc_, AICellGeneral aiCellGeneral)
	{
		super(instRECO, aiCellGeneral);
		this.npc_ = npc_;
		initTime = System.currentTimeMillis();

		setLocation(instRECO);
	}

	@Override
	public void act(Vector3f charLocation, PathGridInterface pgi, PhysicsSystemInterface clientPhysicsSystem)
	{
		// keep me moving, turning and grounded
		updateLocation(clientPhysicsSystem);

		//if (instRECO.NAMEref.str.equals("teruise girvayne"))
		if (instRECO.formId == 225743)
		{
			if (System.currentTimeMillis() - initTime > 5000)
			{
				J3dRECOChaInst visual = aiCellGeneral.getVisualActor(this);
				NifCharacter nc = visual.getJ3dRECOType().getNifCharacter();
				if (nc != null)
				{
					J3dNiControllerSequence cs = nc.getCurrentControllerSequence();
					if (step <20)
					{
						if (!cs.getFireName().equals("walkforward") && !cs.getFireName().equals("turnleft") || cs.isNotRunning())
						{
							//System.out.println("walkforward ai actor called " + instRECO.NAMEref);
							nc.startAnimation("walkforward", true);
							step++;
						}
					}
					else if (step >=20)
					{
						if (!cs.getFireName().equals("walkforward") && !cs.getFireName().equals("turnleft") || cs.isNotRunning())
						{
							//System.out.println("turnleft ai actor called " + instRECO.NAMEref);
							nc.startAnimation("turnleft", true);
							step--;

							turnStart = System.currentTimeMillis();
							turnForMS = 1000;
							turnPerMSRads = (float) (Math.PI / 2000f); //+- for direction
						}
					}

				}
			}
		}
	}

	private int step = 0;

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
