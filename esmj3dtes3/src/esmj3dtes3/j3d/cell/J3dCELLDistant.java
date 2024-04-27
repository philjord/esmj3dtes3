package esmj3dtes3.j3d.cell;

import java.util.Iterator;
import java.util.List;

import esfilemanager.common.data.record.IRecordStore;
import esfilemanager.common.data.record.Record;
import esmj3d.j3d.cell.J3dCELLGeneral;
import esmj3d.j3d.j3drecords.inst.J3dRECOInst;
import esmj3dtes3.j3d.j3drecords.inst.J3dLANDFar;
import utils.source.MediaSources;

/**
 * tes3 has no lod or nothing so instead I'm gonna make a simple land for distants
 */
public class J3dCELLDistant extends J3dCELL
{

	public J3dCELLDistant(IRecordStore master, Record cellRecord, List<Record> children, boolean makePhys, MediaSources mediaSources)
	{
		super(master, cellRecord, makePhys, mediaSources);
		indexRecords(children);

	}

	private void indexRecords(List<Record> children)
	{
		for (Iterator<Record> i = children.iterator(); i.hasNext();)
		{
			while(J3dCELLGeneral.PAUSE_CELL_LOADING) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {					
				}
			}
			Record record = i.next();
			J3dRECOInst jri = (J3dRECOInst) makeJ3dRECOFar(record);			 
			addJ3dRECOInst(jri);

			if (jri instanceof J3dLANDFar)
			{
				//TODO: distant could have a simpler water?
				J3dLANDFar l = (J3dLANDFar) jri;
				float wl = getWaterLevel(cell.WHGT);
				if (wl > l.getLowestHeight())
				{
					addChild(makeWater(wl, J3dCELLPersistent.waterApp));
				}
			}
		}
	}
}
