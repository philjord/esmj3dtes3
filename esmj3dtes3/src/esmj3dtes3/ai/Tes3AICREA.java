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
import esmj3dtes3.data.records.CREA;
import esmj3dtes3.j3d.j3drecords.type.J3dCREA;
import nif.character.NifCharacterTes3;
import nif.j3d.animation.J3dNiControllerSequence;

public class Tes3AICREA extends Tes3AI implements AIActor, AIThinker
{
	private CREA crea;

	private boolean updated = false;

	//TODO: react and set local variable to true
	public static boolean beHitByAxe = false;
	public static boolean beHitBySpell = false;

	public boolean reactedToBeHitByAxe = false;
	public boolean reactedToBeHitBySpell = false;

	public Tes3AICREA(InstRECO instRECO, CREA crea, AICellGeneral aiCellGeneral)
	{
		super(instRECO, aiCellGeneral);
		this.crea = crea;
		setLocation(instRECO);
	}

	public static boolean combatDemo = false;

	@Override
	public void act(Vector3f charLocation, PathGridInterface pgi, PhysicsSystemInterface clientPhysicsSystem)
	{
		if (!updated)
		{
			updateLocation(clientPhysicsSystem);
			updated = true;
		}
		
		if (combatDemo)
		{
			// turn to face him, note angle doesn't take current into account in any way
			float angle = getAngle(charLocation);
			float diffToYaw = (float) (angle - yawPitch.getYaw());

			if (diffToYaw > Math.PI / 16)
			{
				if (turnPerMSRads > 0 || turnForMS <= 0)
				{
					turnStart = System.currentTimeMillis();
					turnPerMSRads = (float) (-Math.PI / 2000f); //+- for direction
					turnForMS = (long) Math.abs(diffToYaw / turnPerMSRads);
				}

			}
			else if (diffToYaw < -Math.PI / 16)
			{
				if (turnPerMSRads < 0 || turnForMS <= 0)
				{
					turnStart = System.currentTimeMillis();
					turnPerMSRads = (float) (Math.PI / 2000f); //+- for direction
					turnForMS = (long) Math.abs(diffToYaw / turnPerMSRads);
				}
			}
			else
			{
				turnStart = System.currentTimeMillis();
				turnForMS = 0;
				turnPerMSRads = 0;
			}

			// do the turns each act
			doRotation(clientPhysicsSystem);
			aiCellGeneral.setLocationForActor(this, location, yawPitch);

			if (!beHitByAxe)
			{
				// reset so we can react again		
				reactedToBeHitByAxe = false;
			}

			if (beHitByAxe && !reactedToBeHitByAxe)
			{
				// put this on creature
				//Cr\\cliffr\\scrm.wav
				//hit1 for the recoil

				J3dRECOChaInst visual = aiCellGeneral.getVisualActor(this);
				NifCharacterTes3 nc = (NifCharacterTes3) visual.getJ3dRECOType().getNifCharacter();
				if (nc != null)
				{
					//J3dNiControllerSequence cs = nc.getCurrentControllerSequence();
					nc.playSound("Sound" + "\\Cr\\atrost\\scrm.wav", 2, 1);
					nc.startAnimation("hit1", true);
				}
				reactedToBeHitByAxe = true;
			}

			if (!beHitBySpell)
			{
				// reset so we can react again		
				reactedToBeHitBySpell = false;
			}

			if (beHitBySpell && !reactedToBeHitBySpell)
			{
				J3dRECOChaInst visual = aiCellGeneral.getVisualActor(this);
				NifCharacterTes3 nc = (NifCharacterTes3) visual.getJ3dRECOType().getNifCharacter();
				if (nc != null)
				{
					//J3dNiControllerSequence cs = nc.getCurrentControllerSequence();
					nc.playSound("Sound" + "\\Cr\\atrost\\scrm.wav", 2, 1);
					//nc.playSound("Sound" + "\\Fx\\magic\\illuH.wav", 5, 1);
					//nc.startAnimation("hit2", true);
					((J3dCREA)visual.getJ3dRECOType()).makeEffect("\\m\\magic_hit_ill.nif","Sound" + "\\Fx\\magic\\illuH.wav", "hit2");
					// put this on creature
					//magic_hit_ill.nif  

				}
				reactedToBeHitBySpell = true;
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
