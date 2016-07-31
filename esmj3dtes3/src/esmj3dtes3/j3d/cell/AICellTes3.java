package esmj3dtes3.j3d.cell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import esmj3d.ai.AIActor;
import esmj3d.ai.PathGridInterface;
import esmj3d.data.shared.records.InstRECO;
import esmj3d.data.shared.records.RECO;
import esmj3d.data.shared.subrecords.ZString;
import esmj3d.j3d.cell.AIActorServices;
import esmj3d.j3d.cell.AICellGeneral;
import esmj3d.j3d.j3drecords.inst.J3dRECOChaInst;
import esmj3d.physics.PhysicsSystemInterface;
import esmj3dtes3.ai.Tes3AICREA;
import esmj3dtes3.ai.Tes3AINPC_;
import esmj3dtes3.ai.Tes3PathGrid;
import esmj3dtes3.data.records.CELL;
import esmj3dtes3.data.records.CREA;
import esmj3dtes3.data.records.LEVC;
import esmj3dtes3.data.records.NPC_;
import esmj3dtes3.data.records.PGRD;
import esmj3dtes3.data.records.REFR;
import esmmanager.common.data.record.IRecordStore;
import esmmanager.common.data.record.Record;
import esmmanager.tes3.IRecordStoreTes3;
import tools3d.utils.YawPitch;

public class AICellTes3 extends AICellGeneral
{
	private ArrayList<Tes3AICREA> creas = new ArrayList<Tes3AICREA>();
	private ArrayList<Tes3AINPC_> npcs = new ArrayList<Tes3AINPC_>();

	private int wrldFormId = -1;
	private int wrldX = -999;
	private int wrldY = -999;
	protected CELL cell;
	private PathGridInterface pgi;
	private AIActorServices aiActorServices;

	public AICellTes3(IRecordStore master, Record cellRecord, List<Record> children, AIActorServices aiActorServices)
	{
		super(master, cellRecord.getFormID(), children);
		this.aiActorServices = aiActorServices;
		cell = new CELL(cellRecord);
		setCell(cell);
		indexRecords();
		//System.out.println("cellRecord.getFormID() " + cellRecord.getFormID());
	}

	public AICellTes3(IRecordStore master, Record cellRecord, int wrldFormId, int wrldX, int wrldY, List<Record> children,
			AIActorServices aiActorLocator)
	{
		this(master, cellRecord, children, aiActorLocator);

		this.wrldFormId = wrldFormId;
		this.wrldX = wrldX;
		this.wrldY = wrldY;

		//System.out.println("cell loaded for AI " + wrldX + " " + wrldY);

	}

	private void indexRecords()
	{
		for (Iterator<Record> i = children.iterator(); i.hasNext();)
		{
			Record record = i.next();
			if (record.getRecordType().equals("REFR"))
			{
				REFR refr = new REFR(record);
				Record baseRecord = ((IRecordStoreTes3) master).getRecord(refr.NAMEref.str);

				if (baseRecord.getRecordType().equals("NPC_"))
				{
					NPC_ npc_ = new NPC_(baseRecord);
					Tes3AINPC_ tes3AINPC_ = new Tes3AINPC_(refr, npc_, this);
					npcs.add(tes3AINPC_);

				}
				else if (baseRecord.getRecordType().equals("CREA"))
				{
					CREA crea = new CREA(baseRecord);
					Tes3AICREA tes3AICREA = new Tes3AICREA(refr, crea, this);
					creas.add(tes3AICREA);
				}
				else if (baseRecord.getRecordType().equals("LEVC"))
				{
					// it is in fact a pointer across to another leveled creature (LVLC)
					//TODO: this and the visual physical system all need to be one thing so someone (this guy)
					// needs to own the process of creating it
					LEVC lvlc = new LEVC(baseRecord);
					RECO reco = makeLVLC(lvlc, master);
					if (reco instanceof NPC_)
					{
						NPC_ npc_ = new NPC_(baseRecord);
						Tes3AINPC_ tes3AINPC_ = new Tes3AINPC_(refr, npc_, this);
						npcs.add(tes3AINPC_);
					}
					else if (reco instanceof CREA)
					{
						CREA crea = (CREA) reco;
						Tes3AICREA tes3AICREA = new Tes3AICREA(refr, crea, this);
						creas.add(tes3AICREA);
					}

				}
			}
			else if (record.getRecordType().equals("PGRD"))
			{
				// always grab it for showing later
				pgi = new Tes3PathGrid(new PGRD(record));
			}
		}
	}

	private static RECO makeLVLC(LEVC lvlc, IRecordStore master)
	{
		// TODO: randomly picked for now
		ZString[] LVLOs = lvlc.charID;

		int idx = (int) (Math.random() * LVLOs.length);
		idx = idx == LVLOs.length ? 0 : idx;

		Record baseRecord = ((IRecordStoreTes3) master).getRecord(LVLOs[idx].str);

		if (baseRecord.getRecordType().equals("LEVC"))
		{
			// it is in fact a pointer across to another leveled creature (LVLC)
			LEVC lvlc2 = new LEVC(baseRecord);
			return makeLVLC(lvlc2, master);
		}
		else if (baseRecord.getRecordType().equals("CREA"))
		{
			CREA crea = new CREA(baseRecord);
			return crea;
		}
		else if (baseRecord.getRecordType().equals("NPC_"))
		{
			NPC_ npc_ = new NPC_(baseRecord);
			return npc_;
		}
		else
		{
			System.out.println("LEVC record type not converted to j3d " + baseRecord.getRecordType());
		}

		return null;
	}

	@Override
	public void unloadCell()
	{
		//System.out.println("CEll unloaded from AI " + wrldX + " " + wrldY);
		creas.clear();
		npcs.clear();
	}

	@Override
	public void doAllThoughts(Vector3f charLocation, PhysicsSystemInterface physics)
	{
		for (Tes3AICREA crea : creas)
		{
			crea.think(charLocation, pgi, physics);
		}
		for (Tes3AINPC_ npc : npcs)
		{
			npc.think(charLocation, pgi, physics);
		}
	}

	@Override
	public void doAllActions(Vector3f charLocation, PhysicsSystemInterface physics)
	{
		for (Tes3AICREA crea : creas)
		{
			crea.act(charLocation, pgi, physics);
		}
		for (Tes3AINPC_ npc : npcs)
		{
			npc.act(charLocation, pgi, physics);
		}
	}

	@Override
	public void setLocationForActor(AIActor aiActor, Vector3f location, YawPitch yawPitch)
	{
		aiActorServices.setLocationForActor(aiActor, location, yawPitch);
	}

	@Override
	public J3dRECOChaInst getVisualActor(AIActor aiActor)
	{
		return aiActorServices.getVisualActor(aiActor);
	}

	@Override
	public J3dRECOChaInst getPhysicalActor(AIActor aiActor)
	{
		return aiActorServices.getPhysicalActor(aiActor);
	}
}
