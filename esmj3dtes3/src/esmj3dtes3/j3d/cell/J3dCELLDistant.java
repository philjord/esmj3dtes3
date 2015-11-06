package esmj3dtes3.j3d.cell;

import java.util.Iterator;
import java.util.List;

import javax.media.j3d.Node;

import utils.source.MediaSources;
import esmj3d.j3d.j3drecords.inst.J3dLANDFar;
import esmmanager.common.data.record.IRecordStore;
import esmmanager.common.data.record.Record;

public class J3dCELLDistant extends J3dCELL
{
	//tes3 has no lod on nothing so instead I'm gonna make a simple land for distants
	public J3dCELLDistant(IRecordStore master, Record cellRecord, List<Record> children, boolean makePhys, MediaSources mediaSources)
	{
		super(master, cellRecord, children, makePhys, mediaSources);
		indexRecords();

	}

	private void indexRecords()
	{
		if (makePhys == true)
		{
			System.out.println("Hahahaha distant physics!");
			new Throwable().printStackTrace();
		}

		for (Iterator<Record> i = children.iterator(); i.hasNext();)
		{
			Record record = i.next();
			Node n = makeJ3dRECOFar(record);
			addChild(n);

			if (n instanceof J3dLANDFar)
			{
				//TODO: distant could have a simpler water?
				J3dLANDFar l = (J3dLANDFar) n;
				float wl = getWaterLevel(cell.WHGT);
				if (wl > l.getLowestHeight())
				{
					addChild(makeWater(wl, J3dCELLPersistent.waterApp));
				}
			}
		}
	}
}
