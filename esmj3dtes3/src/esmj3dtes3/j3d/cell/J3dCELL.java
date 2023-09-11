package esmj3dtes3.j3d.cell;

import java.util.ArrayList;
import java.util.List;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Quat4f;
import org.jogamp.vecmath.Vector3f;

import com.frostwire.util.SparseArray;

import esmio.common.data.record.IRecordStore;
import esmio.common.data.record.Record;
import esmio.tes3.IRecordStoreTes3;
import esmj3d.data.shared.records.LAND;
import esmj3d.j3d.BethRenderSettings;
import esmj3d.j3d.BethRenderSettings.UpdateListener;
import esmj3d.j3d.cell.J3dCELLGeneral;
import esmj3d.j3d.j3drecords.inst.J3dLAND;
import esmj3d.j3d.j3drecords.inst.J3dLANDFar;
import esmj3d.j3d.j3drecords.inst.J3dRECOChaInst;
import esmj3d.j3d.j3drecords.inst.J3dRECOInst;
import esmj3dtes3.data.records.CELL;
import esmj3dtes3.data.records.PGRD;
import esmj3dtes3.data.records.REFR;
import esmj3dtes3.j3d.j3drecords.inst.J3dREFRFactory;
import utils.source.MediaSources;

public abstract class J3dCELL extends J3dCELLGeneral implements UpdateListener
{

	public static boolean DO_DUMP = false;

	// Record all visuals that are made for characters
	// these will be picked up by the AI system and it will link them to the 
	// dynamics equivalent for moving and animating
	// the AI system is responsible for removing these from this hashmap when done
	private SparseArray<J3dRECOChaInst> allJ3dRECOChaInsts = new SparseArray<J3dRECOChaInst>();

	protected CELL cell;

	private ArrayList<J3dRECOInst> j3dRECOInsts = new ArrayList<J3dRECOInst>();

	private boolean showPathGrid = false;

	private PGRD pgrd;
	private BranchGroup j3dPGRDbg;

	public J3dCELL(IRecordStore master, Record cellRecord, List<Record> children, boolean makePhys, MediaSources mediaSources)
	{
		super(master, 0, children, makePhys, mediaSources);
		cell = new CELL(cellRecord);
		setCell(cell);

		BethRenderSettings.addUpdateListener(this);

		showPathGrid = BethRenderSettings.isShowPathGrid();
	}

	@Override
	public void renderSettingsUpdated()
	{
		if (!makePhys)
		{
			synchronized (j3dRECOInsts)
			{
				for (J3dRECOInst j3dRECOInst : j3dRECOInsts)
				{
					j3dRECOInst.renderSettingsUpdated();
				}

				updateShowPathGrid(BethRenderSettings.isShowPathGrid());
			}

		}
	}

	private void updateShowPathGrid(boolean newShowPathGrid)
	{
		if (pgrd != null)
		{
			if (showPathGrid != newShowPathGrid)
			{
				showPathGrid = BethRenderSettings.isShowPathGrid();

				if (showPathGrid)
				{
					j3dPGRDbg = new BranchGroup();
					j3dPGRDbg.setCapability(ALLOW_DETACH);

					TransformGroup transformGroup = new TransformGroup();
					Transform3D transform = new Transform3D();

					float landSize = J3dLAND.LAND_SIZE;
					//we don't use instCell.getTrans().z even if set
					// from corner not center
					Vector3f loc = new Vector3f((instCell.getTrans().x * landSize), 0, -(instCell.getTrans().y * landSize));
					transform.set(loc);

					transformGroup.setTransform(transform);

					J3dPGRD j3dPGRD = new J3dPGRD(pgrd);
					transformGroup.addChild(j3dPGRD);
					j3dPGRDbg.addChild(transformGroup);

					addChild(j3dPGRDbg);

				}
				else
				{
					// hide it
					j3dPGRDbg.detach();
					j3dPGRDbg = null;
				}
			}
		}
	}

	protected void addJ3dRECOInst(J3dRECOInst j3dRECOInst)
	{
		if (j3dRECOInst != null)
		{
			addChild((Node) j3dRECOInst);
			synchronized (j3dRECOInsts)
			{
				j3dRECOInsts.add(j3dRECOInst);
			}
		}
	}

	@Override
	public Node makeJ3dRECOFar(Record record)
	{
		J3dRECOInst ret = null;
		try
		{
			if (record.getRecordType().equals("REFR"))
			{
				ret = J3dREFRFactory.makeJ3DReferFar(new REFR(record), master, mediaSources);
			}
			else if (record.getRecordType().equals("LAND"))
			{
				J3dLANDFar j3dLAND;
				if (!makePhys)
				{
					j3dLAND = new J3dLANDFar(new LAND(record, true), master, mediaSources.getTextureSource());
					j3dLAND.setLocation(cellLocation, new Quat4f(0, 0, 0, 1));
					ret = j3dLAND;
				}
			}
			else
			{
				System.err.println("Far record not REFR " + record.getRecordType());
			}
		}
		catch (NullPointerException e)
		{
			if (e.getStackTrace().length > 0)
				System.out.println("J3dCELL.makeJ3dRECOFar " + cell.formId + " - null pointer making record " + record + " " + record.getRecordType()
						+ " in " + e.getStackTrace()[0]);
			else
				System.out.println("J3dCELL.makeJ3dRECOFar " + cell.formId + " - null pointer making record " + record + " " + record.getRecordType());

			if (record.getRecordType().equals("REFR"))
			{
				REFR refr = new REFR(record);
				Record baseRecord = master.getRecord(refr.NAME.formId);
				System.out.println("And it's a REFR with base of " + baseRecord.getRecordType());

				//e.printStackTrace();
			}
		}

		return (Node) ret;
	}

	/*Referenced Object Data Grouping
	FRMR = Object Index (starts at 1) (4 bytes, long)
		This is used to uniquely identify objects in the cell.  For new files the
		index starts at 1 and is incremented for each new object added.  For modified
		objects the index is kept the same.			
	NAME = Object ID string
	XSCL = Scale (4 bytes, float) Static
	DELE = (4 byte long) Indicates that the reference is deleted.
	DODT = XYZ Pos, XYZ Rotation of exit (24 bytes, Door objects)
		float XPos
		float YPos
		float ZPos
		float XRotate
		float YRotate
		float ZRotate
	DNAM = Door exit name (Door objects)
	FLTV = Follows the DNAM optionally, lock level (long)
	KNAM = Door key
	TNAM = Trap name
	UNAM = Reference Blocked (1 byte, 00?), only occurs once in MORROWIND.ESM
	ANAM = Owner ID string
	BNAM = Global variable/rank ID string
	INTV = Number of uses ( 4 bytes, long, 1 default), occurs even for objects that don't use it
	NAM9 = ? (4 bytes, long, 0x00000001)
	XSOL = Soul Extra Data (ID string of creature)
	DATA = Ref Position Data (24 bytes)
		float XPos
		float YPos
		float ZPos
		float XRotate
		float YRotate
		float ZRotate
		*/

	@Override
	public J3dRECOInst makeJ3dRECO(Record record)
	{
		J3dRECOInst ret = null;
		try
		{
			if (record.getRecordType().equals("REFR"))
			{

				REFR refr = new REFR(record);
				Record baseRecord = ((IRecordStoreTes3) master).getRecord(refr.NAMEref.str);

				// it might be in the cache still used by AI system
				if (baseRecord.getRecordType().equals("NPC_") || baseRecord.getRecordType().equals("CREA")
						|| baseRecord.getRecordType().equals("LEVC"))
				{
					ret = allJ3dRECOChaInsts.get(refr.formId);
				}

				if (ret == null)
				{
					ret = J3dREFRFactory.makeJ3DRefer(refr, makePhys, master, mediaSources);

					// Place for dumping recos
					if (DO_DUMP)
						ret = checkDump(ret);

					// make some AI for CHA
					if (ret instanceof J3dRECOChaInst)
					{
						allJ3dRECOChaInsts.put(refr.formId, (J3dRECOChaInst) ret);
					}
				}
			}
			else if (record.getRecordType().equals("PGRD"))
			{
				// always grab it for showing later
				pgrd = new PGRD(record);
			}
			else if (record.getRecordType().equals("LAND"))
			{
				J3dLAND j3dLAND;

				if (makePhys)
				{
					j3dLAND = new J3dLAND(new LAND(record, true));
				}
				else
				{
					j3dLAND = new J3dLAND(new LAND(record, true), master, mediaSources.getTextureSource());
				}
				j3dLAND.setLocation(cellLocation, new Quat4f(0, 0, 0, 1));
				ret = j3dLAND;
			}
			else
			{
				System.out.println("makeJ3dRECO in J3dCELL Record type not converted to j3d " + record.getRecordType());
			}
		}
		catch (NullPointerException e)
		{
			if (e.getStackTrace().length > 0)
				System.out.println("J3dCELL.makeJ3dRECO " + cell.formId + " - null pointer making record " + record + " " + record.getRecordType()
						+ " in " + e.getStackTrace()[0]);
			else
				System.out.println("J3dCELL.makeJ3dRECO " + cell.formId + " - null pointer making record " + record + " " + record.getRecordType());

			if (record.getRecordType().equals("REFR"))
			{
				REFR refr = new REFR(record);
				Record baseRecord = ((IRecordStoreTes3) master).getRecord(refr.NAMEref.str);
				System.out.println("And it's a REFR with base of " + baseRecord.getRecordType());
				//e.printStackTrace();
			}
		}

		if (ret != null)
		{
			j3dRECOs.put(ret.getRecordId(), ret);
		}
		return ret;
	}

	private static J3dRECOInst checkDump(J3dRECOInst ret)
	{
		if (ret != null)
		{				
			Transform3D t = new Transform3D();
			ret.getLocation(t);
			Vector3f v = new Vector3f();
			t.get(v);

			Vector3f dist = new Vector3f();

			//sedya neen stuff
			dist.sub(v, new Vector3f(-148, 3, 896));
			if (dist.length() < 10)
				return null;

			dist.sub(v, new Vector3f(-160, 0, 901));
			if (dist.length() < 10)
				return null;

			dist.sub(v, new Vector3f(-185, 1, 907));
			if (dist.length() < 10)
				return null;

			dist.sub(v, new Vector3f(-177, 1, 894));
			if (dist.length() < 10)
				return null;

			dist.sub(v, new Vector3f(-149, 5, 863));
			if (dist.length() < 10)
				return null;

			dist.sub(v, new Vector3f(-140, 4, 861));
			if (dist.length() < 10)
				return null;

			dist.sub(v, new Vector3f(-141, 2, 911));
			if (dist.length() < 9)
				return null;
			
			 
		} 
		return ret;
	}
}
