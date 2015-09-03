package esmj3dtes3.j3d.cell;

import java.util.Iterator;
import java.util.List;

import javax.media.j3d.Node;

import utils.source.MediaSources;
import esmLoader.common.data.record.IRecordStore;
import esmLoader.common.data.record.Record;

public class J3dCELLDistant extends J3dCELL
{
	//Notice this distant has it's own children list(defined by DISTANT type children)
	public J3dCELLDistant(IRecordStore master, Record cellRecord, List<Record> children, boolean makePhys, MediaSources mediaSources)
	{
		super(master, cellRecord, children, makePhys, mediaSources);
		indexRecords();
	}

	private void indexRecords()
	{
		if (makePhys == true)
			System.out.println("Hahahaha distant physics!");

		for (Iterator<Record> i = children.iterator(); i.hasNext();)
		{
			Record record = i.next();
			Node n = makeJ3dRECOFar(record);
			addChild(n);
		}
	}
}
