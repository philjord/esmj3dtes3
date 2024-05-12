package esmj3dtes3.ai;

import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.Vector3f;

import esmj3d.ai.AIActor;
import esmj3d.ai.AIThinker;
import esmj3d.ai.PathGridInterface;
import esmj3d.data.shared.records.InstRECO;
import esmj3d.j3d.cell.AICellGeneral;
import esmj3d.j3d.j3drecords.inst.J3dRECOChaInst;
import esmj3d.physics.PhysicsSystemInterface;
import esmj3dtes3.data.records.NPC_;
import nif.character.NifCharacter;
import nif.character.NifCharacterTes3;
import nif.j3d.animation.J3dNiControllerSequence;
import nif.j3d.animation.J3dNiGeomMorpherController;
import tools3d.audio.SimpleSounds;
import utils.ESConfig;

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

		//fargoth instRECO.formId == 225743 ||instRECO.formId == 51640		
		//chargen boat guard 1
		//chargen dock guard

		//System.out.println("I'm thinking " + instRECO.NAMEref.str + " " + location);
		if (instRECO.NAMEref.equals("fargoth"))
		{
			fargothAct(charLocation, pgi, clientPhysicsSystem);
		}
		else if (instRECO.NAMEref.equals("chargen boat guard 1"))
		{
			chargenboatguard1Act(charLocation, pgi, clientPhysicsSystem);
		}
		else if (instRECO.NAMEref.equals("chargen dock guard"))
		{
			chargendockguardAct(charLocation, pgi, clientPhysicsSystem);
		}
		else if (instRECO.NAMEref.equals("chargen boat guard 2"))
		{
			chargenboatguard2Act(charLocation, pgi, clientPhysicsSystem);
		}
		else if (instRECO.NAMEref.equals("chargen name"))
		{
			chargennameAct(charLocation, pgi, clientPhysicsSystem);
		}

	}

	private static int chargennameStep = 0;//used by 2 ai
	private boolean chargennameInit = false;
	private long chargennameLastSpoke = 0;

	private void chargennameAct(Vector3f charLocation, PathGridInterface pgi, PhysicsSystemInterface clientPhysicsSystem)
	{
		J3dRECOChaInst visual = aiCellGeneral.getVisualActor(this);
		if (visual != null)
		{
			NifCharacterTes3 nc = (NifCharacterTes3) visual.getJ3dRECOType().getNifCharacter();
			if (System.currentTimeMillis() - initTime > 2000 && chargennameStep == 0)
			{
				//nc.playSound("Sound" + "\\Vo\\Misc\\chargenname1.mp3", 5, 1);
				SimpleSounds.playMp3("Sound" + "\\Vo\\Misc\\chargenname1.mp3", 0.15f);

				chargennameLastSpoke = System.currentTimeMillis();
				chargennameStep++;
			}
			else if (System.currentTimeMillis() - chargennameLastSpoke > 6000 && chargennameStep == 1)
			{
				//nc.playSound("Sound" + "\\Vo\\Misc\\chargenname2.mp3", 5, 1);
				SimpleSounds.playMp3("Sound" + "\\Vo\\Misc\\chargenname2.mp3", 0.15f);

				chargennameLastSpoke = System.currentTimeMillis();
				chargennameStep++;
			}
		}

	}

	private int chargenboatguard2Step = 0;
	private boolean chargenboatguard2Init = false;
	private long chargenboatguard2LastSpoke = 0;

	private void chargenboatguard2Act(Vector3f charLocation, PathGridInterface pgi, PhysicsSystemInterface clientPhysicsSystem)
	{
		if (!chargenboatguard2Init)
		{
			location.set(1, -0.1f, -15);
			yawPitch.set(Math.PI, 0);
			aiCellGeneral.setLocationForActor(this, location, yawPitch);

			updateLocation(clientPhysicsSystem);
			J3dRECOChaInst visual = aiCellGeneral.getVisualActor(this);
			NifCharacterTes3 nc = (NifCharacterTes3) visual.getJ3dRECOType().getNifCharacter();
			nc.setNoMorphs();
			chargenboatguard2Init = true;
		}

		updateFromVisualLocation(clientPhysicsSystem);
		doRotation(clientPhysicsSystem);
		aiCellGeneral.setLocationForActor(this, location, yawPitch);
		//only start after first chat by name guy
		if (chargennameStep >= 2)
		{

			J3dRECOChaInst visual = aiCellGeneral.getVisualActor(this);
			if (visual != null)
			{
				final NifCharacterTes3 nc = (NifCharacterTes3) visual.getJ3dRECOType().getNifCharacter();
				if (nc != null)
				{

					J3dNiControllerSequence cs = nc.getCurrentControllerSequence();
					if (chargenboatguard2Step == 0)
					{
						if (!cs.getFireName().equals("walkforward") || cs.isNotRunning())
						{
							//System.out.println("walkforward ai actor called " + instRECO.NAMEref);
							nc.startAnimation("walkforward", true);
						}
						Vector3f target = new Vector3f(1, -0.1f, -0);
						Vector3f dist = new Vector3f();
						dist.sub(target, location);

						if (dist.length() < 2f)
						{
							chargenboatguard2Step = 1;
							nc.startAnimation("idle2", true);
						}
					}
					else if (chargenboatguard2Step == 1)
					{

						final J3dNiGeomMorpherController morph = nc.getAllMorphs().get(0);

						morph.fireFrameName(morph.getAllMorphFrameNames()[1], true);

						//nc.playSound("Sound" + "\\Vo\\Misc\\chargenwalk1.mp3", 5, 1);
						SimpleSounds.playMp3("Sound" + "\\Vo\\Misc\\chargenwalk1.mp3", 0.15f);

						Thread t = new Thread() {
							@Override
							public void run()
							{
								try
								{
									Thread.sleep(4000);
								}
								catch (InterruptedException e)
								{
									e.printStackTrace();
								}
								morph.fireFrameName(morph.getAllMorphFrameNames()[0], false);
								chargenboatguard2Step = 3;
							}
						};
						t.start();
						chargenboatguard2Step = 2;

					}
					else if (chargenboatguard2Step == 3)
					{
						if (turnForMS == 0)
						{
							//System.out.println("turnleft ai actor called " + instRECO.NAMEref);
							nc.startAnimation("turnleft", true);

							turnStart = System.currentTimeMillis();
							turnForMS = 2000;
							turnPerMSRads = (float) (Math.PI / 2000f); //+- for direction
							chargenboatguard2Step = 4;
						}
					}
					else if (chargenboatguard2Step == 4)
					{
						if (turnForMS == 0)
						{
							if (!cs.getFireName().equals("walkforward") || cs.isNotRunning())
							{
								//System.out.println("walkforward ai actor called " + instRECO.NAMEref);
								nc.startAnimation("walkforward", true);
							}
							Thread t = new Thread() {
								@Override
								public void run()
								{
									try
									{
										Thread.sleep(6000);
									}
									catch (InterruptedException e)
									{
										e.printStackTrace();
									}
									nc.startAnimation("idle3", true);
									chargenboatguard2Step = 5;
								}
							};
							t.start();

						}
					}
				}
			}
		}

	}

	private int chargendockguardStep = 0;
	private boolean chargendockguardInit = false;
	private long chargendockguard1LastSpoke = 0;

	private void chargendockguardAct(Vector3f charLocation, PathGridInterface pgi, PhysicsSystemInterface clientPhysicsSystem)
	{

		//CharGenRaceNPC
		//	Say "Vo\Misc\CharGenDock1.wav" 

		//	;let then look around delay
		//	if  ( timer >= 1.5 )

		//		Say "Vo\Misc\CharGenDock2.wav" 

		if (!chargendockguardInit)
		{
			updateLocation(clientPhysicsSystem);
			J3dRECOChaInst visual = aiCellGeneral.getVisualActor(this);
			if (visual != null)
			{
				NifCharacterTes3 nc = (NifCharacterTes3) visual.getJ3dRECOType().getNifCharacter();
				nc.setNoMorphs();
			}
			chargendockguardInit = true;
		}

		Vector3f dist = new Vector3f(location);
		dist.sub(charLocation);
		if (dist.length() < 150 * ESConfig.ES_TO_METERS_SCALE)
		{
			if (chargendockguardStep == 0)
			{
				// ummmm.... geomorph his face to talking??
				// 1.4 till 2.0 of frame 1 (0,1,2) seems about right
				// but it's super slow, so maybe a speed factor??
				J3dRECOChaInst visual = aiCellGeneral.getVisualActor(this);
				if (visual != null)
				{
					NifCharacterTes3 nc = (NifCharacterTes3) visual.getJ3dRECOType().getNifCharacter();

					final J3dNiGeomMorpherController morph = nc.getAllMorphs().get(0);

					morph.fireFrameName(morph.getAllMorphFrameNames()[1], true);

					//nc.playSound("Sound" + "\\Vo\\Misc\\CharGenDock1.mp3", 5, 0);
					SimpleSounds.playMp3("Sound" + "\\Vo\\Misc\\CharGenDock1.mp3", 0.15f);

					chargenboatguard1LastSpoke = System.currentTimeMillis();
					Thread t = new Thread() {
						@Override
						public void run()
						{
							try
							{
								Thread.sleep(4000);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
							morph.fireFrameName(morph.getAllMorphFrameNames()[0], false);
						}
					};
					t.start();
				}
				chargendockguardStep = 1;
				chargendockguard1LastSpoke = System.currentTimeMillis();
			}
			else if (chargendockguardStep == 1 && System.currentTimeMillis() - chargendockguard1LastSpoke > 6000)
			{
				// ummmm.... geomorph his face to talking??
				// 1.4 till 2.0 of frame 1 (0,1,2) seems about right
				// but it's super slow, so maybe a speed factor??
				J3dRECOChaInst visual = aiCellGeneral.getVisualActor(this);

				if (visual != null)
				{
					NifCharacterTes3 nc = (NifCharacterTes3) visual.getJ3dRECOType().getNifCharacter();

					final J3dNiGeomMorpherController morph = nc.getAllMorphs().get(0);

					morph.fireFrameName(morph.getAllMorphFrameNames()[1], true);

					//nc.playSound("Sound" + "\\Vo\\Misc\\CharGenDock2.mp3", 5, 1);
					SimpleSounds.playMp3("Sound" + "\\Vo\\Misc\\CharGenDock2.mp3", 0.15f);

					chargendockguardStep = 2;
					Thread t = new Thread() {
						@Override
						public void run()
						{
							try
							{
								Thread.sleep(4000);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
							morph.fireFrameName(morph.getAllMorphFrameNames()[0], false);
						}
					};
					t.start();
				}
			}
		}

	}

	private boolean chargenboatguard1Init = false;
	private long chargenboatguard1LastSpoke = 0;

	private void chargenboatguard1Act(Vector3f charLocation, PathGridInterface pgi, PhysicsSystemInterface clientPhysicsSystem)
	{
		if (!chargenboatguard1Init)
		{
			updateLocation(clientPhysicsSystem);
			J3dRECOChaInst visual = aiCellGeneral.getVisualActor(this);
			if (visual != null)
			{
				NifCharacterTes3 nc = (NifCharacterTes3) visual.getJ3dRECOType().getNifCharacter();
				nc.setNoMorphs();
			}
			chargenboatguard1Init = true;
		}

		Vector3f dist = new Vector3f(location);
		dist.sub(charLocation);

		if (dist.length() < 180 * ESConfig.ES_TO_METERS_SCALE)
		{
			//Script: CharGenBoatNPC
			//	System.out.println("I'm thinking " + instRECO.NAMEref.str + " " + location);
			if (chargenboatguard1LastSpoke == 0)
			{

				// ummmm.... geomorph his face to talking??
				// 1.4 till 2.0 of frame 1 (0,1,2) seems about right
				// but it's super slow, so maybe a speed factor??
				J3dRECOChaInst visual = aiCellGeneral.getVisualActor(this);
				if (visual != null)
				{
					NifCharacterTes3 nc = (NifCharacterTes3) visual.getJ3dRECOType().getNifCharacter();

					final J3dNiGeomMorpherController morph = nc.getAllMorphs().get(0);

					morph.fireFrameName(morph.getAllMorphFrameNames()[1], true);

					//nc.playSound("Sound" + "\\Vo\\Misc\\CharGenBoat1.mp3", 5, 1);

					SimpleSounds.playMp3("Sound" + "\\Vo\\Misc\\CharGenBoat1.mp3", 0.15f);

					chargenboatguard1LastSpoke = System.currentTimeMillis();
					Thread t = new Thread() {
						@Override
						public void run()
						{
							try
							{
								Thread.sleep(4000);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
							morph.fireFrameName(morph.getAllMorphFrameNames()[0], false);
						}
					};
					t.start();
				}
			}
			else if (System.currentTimeMillis() - chargenboatguard1LastSpoke > 6000)
			{
				// ummmm.... geomorph his face to talking??
				// 1.4 till 2.0 of frame 1 (0,1,2) seems about right
				// but it's super slow, so maybe a speed factor??
				J3dRECOChaInst visual = aiCellGeneral.getVisualActor(this);
				if (visual != null)
				{
					NifCharacterTes3 nc = (NifCharacterTes3) visual.getJ3dRECOType().getNifCharacter();

					final J3dNiGeomMorpherController morph = nc.getAllMorphs().get(0);

					morph.fireFrameName(morph.getAllMorphFrameNames()[1], true);

					//nc.playSound("Sound" + "\\Vo\\Misc\\CharGenBoat2.mp3", 5, 1);
					SimpleSounds.playMp3("Sound" + "\\Vo\\Misc\\CharGenBoat2.mp3", 0.15f);

					chargenboatguard1LastSpoke = System.currentTimeMillis();
					Thread t = new Thread() {
						@Override
						public void run()
						{
							try
							{
								Thread.sleep(4000);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
							morph.fireFrameName(morph.getAllMorphFrameNames()[0], false);
						}
					};
					t.start();
				}
			}
		}

	}

	private int fargothStep = 0;
	private boolean fargothInit = false;

	private void fargothAct(Vector3f charLocation, PathGridInterface pgi, PhysicsSystemInterface clientPhysicsSystem)
	{
		if (!fargothInit)
		{
			location.set(-120, 2, 911);
			yawPitch.set(Math.PI, 0);
			aiCellGeneral.setLocationForActor(this, location, yawPitch);
			fargothInit = true;
		}

		// keep me moving, turning and grounded
		updateLocation(clientPhysicsSystem);
		if (System.currentTimeMillis() - initTime > 5000)
		{
			J3dRECOChaInst visual = aiCellGeneral.getVisualActor(this);
			if (visual != null)
			{
				NifCharacter nc = visual.getJ3dRECOType().getNifCharacter();
				if (nc != null)
				{
					J3dNiControllerSequence cs = nc.getCurrentControllerSequence();
					if (fargothStep < 2)
					{
						if (!cs.getFireName().equals("walkforward") && !cs.getFireName().equals("turnleft") || cs.isNotRunning())
						{
							//System.out.println("walkforward ai actor called " + instRECO.NAMEref);
							nc.startAnimation("walkforward", true);
							fargothStep++;
						}
					}
					else if (fargothStep >= 2)
					{
						if (!cs.getFireName().equals("walkforward") && !cs.getFireName().equals("turnleft") || cs.isNotRunning())
						{
							//System.out.println("turnleft ai actor called " + instRECO.NAMEref);
							nc.startAnimation("turnleft", true);
							fargothStep--;

							turnStart = System.currentTimeMillis();
							turnForMS = 1000;
							turnPerMSRads = (float) (Math.PI / 2000f); //+- for direction
						}
					}
				}
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
