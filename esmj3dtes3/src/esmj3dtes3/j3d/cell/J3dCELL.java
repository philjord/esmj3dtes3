package esmj3dtes3.j3d.cell;

import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Node;

import esmj3d.data.shared.records.LAND;
import esmj3d.j3d.BethRenderSettings;
import esmj3d.j3d.BethRenderSettings.UpdateListener;
import esmj3d.j3d.cell.J3dCELLGeneral;
import esmj3d.j3d.j3drecords.inst.J3dLAND;
import esmj3d.j3d.j3drecords.inst.J3dLANDFar;
import esmj3d.j3d.j3drecords.inst.J3dRECOInst;
import esmj3dtes3.data.records.CELL;
import esmj3dtes3.data.records.REFR;
import esmj3dtes3.j3d.j3drecords.inst.J3dREFRFactory;
import esmmanager.common.data.record.IRecordStore;
import esmmanager.common.data.record.Record;
import esmmanager.tes3.IRecordStoreTes3;
import utils.source.MediaSources;

public abstract class J3dCELL extends J3dCELLGeneral implements UpdateListener
{
	protected CELL cell;

	private ArrayList<J3dRECOInst> j3dRECOInsts = new ArrayList<J3dRECOInst>();

	public J3dCELL(IRecordStore master, Record cellRecord, List<Record> children, boolean makePhys, MediaSources mediaSources)
	{
		super(master, 0, children, makePhys, mediaSources);
		cell = new CELL(cellRecord);
		setCell(cell);

		BethRenderSettings.addUpdateListener(this);
	}

	@Override
	public void renderSettingsUpdated()
	{
		if (!makePhys)
		{
			for (J3dRECOInst j3dRECOInst : j3dRECOInsts)
			{
				j3dRECOInst.renderSettingsUpdated();
			}
		}
	}

	protected void addJ3dRECOInst(J3dRECOInst j3dRECOInst)
	{
		if (j3dRECOInst != null)
		{
			addChild((Node) j3dRECOInst);
			j3dRECOInsts.add(j3dRECOInst);
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
					j3dLAND.setLocation(cellLocation);
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
				System.out.println("J3dCELL " + cell.formId + " - null pointer making record " + record + " " + record.getRecordType()
						+ " in " + e.getStackTrace()[0]);
			else
				System.out.println("J3dCELL " + cell.formId + " - null pointer making record " + record + " " + record.getRecordType());

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

	public J3dRECOInst makeJ3dRECO(Record record)
	{
		J3dRECOInst ret = null;
		try
		{
			if (record.getRecordType().equals("REFR"))
			{
				ret = J3dREFRFactory.makeJ3DRefer(new REFR(record), makePhys, master, mediaSources);
			}
			//TODO: these are now just plain REFR's consider this
			/*else if (record.getRecordType().equals("ACRE"))
			{
				if (!makePhys)
				{
					ret = new J3dACRE(new ACRE(record), master, mediaSources);
				}
			}
			else if (record.getRecordType().equals("ACHR"))
			{
				if (!makePhys)
				{
					ret = new J3dACHR(new ACHR(record), master, mediaSources);
				}
			}*/
			else if (record.getRecordType().equals("PGRD"))
			{
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
				j3dLAND.setLocation(cellLocation);
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
				System.out.println("J3dCELL " + cell.formId + " - null pointer making record " + record + " " + record.getRecordType()
						+ " in " + e.getStackTrace()[0]);
			else
				System.out.println("J3dCELL " + cell.formId + " - null pointer making record " + record + " " + record.getRecordType());

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
}
