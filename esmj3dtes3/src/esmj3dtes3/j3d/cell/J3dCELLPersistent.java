package esmj3dtes3.j3d.cell;

import java.util.Iterator;
import java.util.List;

import org.jogamp.java3d.Group;

import esfilemanager.common.data.record.IRecordStore;
import esfilemanager.common.data.record.Record;
import esmj3d.data.shared.records.CommonWRLD;
import esmj3d.j3d.cell.GridSpaces;
import esmj3d.j3d.cell.J3dICELLPersistent;
import esmj3d.j3d.water.WaterApp;
import esmj3dtes3.data.records.REFR;
import utils.source.MediaSources;

public class J3dCELLPersistent extends J3dCELL implements J3dICELLPersistent
{
	private GridSpaces gridSpaces = new GridSpaces(this);

	private CommonWRLD wrld;

	public static WaterApp waterApp; // the single water app used by all waters

	/**
	 * NOTE! one persisent ever attach at a time, one per entre world one per interior cell (but in fact 2 one phys, one vis)
	 * good place for singleton parts of scene grpahs to be attached
	 * CELL presistent is different from temp and distant as it's dynamic refs and achar can move away
	 * but they are still managed by the cell persistent itself, so we have this crazy grid space sub system to quickly 
	 * clip wandering things away
	 * 
	 * @param master
	 * @param cellRecord
	 * @param children
	 * @param makePhys
	 * @param recordToRECO
	 */

	public J3dCELLPersistent(CommonWRLD wrld, IRecordStore master, Record cellRecord, List<Record> children, boolean makePhys,
			MediaSources mediaSources)
	{
		super(master, cellRecord, makePhys, mediaSources);
		this.wrld = wrld;
		
		setCapability(Group.ALLOW_CHILDREN_WRITE);
		setCapability(Group.ALLOW_CHILDREN_EXTEND);
		
		addChild(gridSpaces);
		indexRecords(children);

		if (!makePhys)
		{
			if (waterApp == null)
			{
				//water00 to water31 are J3dNiFlipController style images put them all in
				int waterCount = 32;
				String[] waterTexs = new String[waterCount];
				for (int i = 0; i < waterCount; i++)
				{
					waterTexs[i] = "textures\\water\\water" + (i < 10 ? "0" : "") + i + ".dds";
				}
				waterApp = new WaterApp(waterTexs, mediaSources.getTextureSource());
			}
			else
			{
				waterApp.detach();

			}
			addChild(waterApp);
		}
	}

	private void indexRecords(List<Record> children)
	{
		//NOTE they can be 10'000 's of these records do not load up front!
		for (Iterator<Record> i = children.iterator(); i.hasNext();)
		{
			Record record = i.next();

			if (record.getRecordType().equals("REFR"))
			{
				REFR refr = new REFR(record);
				gridSpaces.sortOutBucket(refr, record);
			}
			else
			{
				System.out.println("CELL_PERSISTENT Record type not converted to j3d " + record.getRecordType());
			}
		}
	}

	@Override
	public GridSpaces getGridSpaces()
	{
		return gridSpaces;
	}

	@Override
	public CommonWRLD getWRLD()
	{
		return wrld;
	}
}
