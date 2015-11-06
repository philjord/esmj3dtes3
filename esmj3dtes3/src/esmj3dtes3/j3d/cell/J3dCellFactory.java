package esmj3dtes3.j3d.cell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.DataFormatException;

import utils.ESMUtils;
import utils.source.MediaSources;
import esmj3d.j3d.cell.J3dICellFactory;
import esmj3d.j3d.cell.MorphingLandscape;
import esmj3dtes3.data.records.REFR;
import esmj3dtes3.data.records.STAT;
import esmj3dtes3.data.records.WRLD;
import esmmanager.common.PluginException;
import esmmanager.common.data.plugin.IMaster;
import esmmanager.common.data.plugin.PluginGroup;
import esmmanager.common.data.plugin.PluginRecord;
import esmmanager.common.data.record.IRecordStore;
import esmmanager.common.data.record.Record;

public class J3dCellFactory implements J3dICellFactory
{
	private IMaster esmManager;

	private IRecordStore recordStore;

	private MediaSources mediaSources;

	public J3dCellFactory()
	{

	}

	public void setSources(IMaster esmManager2, IRecordStore recordStore, MediaSources mediaSources)
	{
		this.esmManager = esmManager2;
		this.recordStore = recordStore;
		this.mediaSources = mediaSources;
	}

	@Override
	public String getLODWorldName(int worldFormId)
	{
		int formId = -1;
		WRLD wrld = getWRLD(worldFormId);
		// use parent first
		if (wrld.WNAM != null && wrld.WNAM.formId != -1)
		{
			formId = wrld.WNAM.formId;
		}
		else
		{
			formId = worldFormId;
		}
		return "" + formId;
	}

	@Override
	public MorphingLandscape makeLODLandscape(int lodX, int lodY, int scale, String lodWorldFormId)
	{
		throw new UnsupportedOperationException();
	}

	private WRLD getWRLD(int formId)
	{
		try
		{
			PluginRecord record = esmManager.getWRLD(formId);
			WRLD wrld = new WRLD(new Record(record, -1));
			return wrld;
		}
		catch (DataFormatException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (PluginException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isWRLD(int formId)
	{
		return getWRLD(formId) != null;
	}

	@Override
	public J3dCELLPersistent makeBGWRLDPersistent(int formId, boolean makePhys)
	{
		WRLD wrld = getWRLD(formId);
		if (wrld != null)
		{
			try
			{
				PluginRecord cell = esmManager.getWRLD(formId);
				// no persistents I'm aware of, so just a new arraylist
				return new J3dCELLPersistent(wrld, recordStore, new Record(cell, -1), new ArrayList<Record>(), makePhys, mediaSources);
			}
			catch (DataFormatException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (PluginException e)
			{
				e.printStackTrace();
			}

			/*WRLDChildren children = esmManager.getWRLDChildren(formId);

			PluginRecord cell = children.getCell();
			if (cell != null)
			{
				PluginGroup cellChildren = children.getCellChildren();
				if (cellChildren != null)
				{
					return new J3dCELLPersistent(wrld, recordStore, new Record(cell, -1), ESMUtils.getChildren(cellChildren,
							PluginGroup.CELL_PERSISTENT), makePhys, mediaSources);
				}
			}*/

		}
		else
		{
			System.out.println("makeBGWRLDPersistent bad formId not wrld " + formId);
		}
		return null;
	}

	@Override
	public J3dCELLTemporary makeBGWRLDTemporary(int wrldFormId, int x, int y, boolean makePhys)
	{
		int cellId = esmManager.getWRLDExtBlockCELLId(wrldFormId, x, y);
		return makeBGWRLDTemporary(cellId, makePhys);
	}

	@Override
	public J3dCELLTemporary makeBGWRLDTemporary(int cellId, boolean makePhys)
	{
		if (cellId == -1)
			return null;

		try
		{

			PluginRecord record = esmManager.getWRLDExtBlockCELL(cellId);

			if (record != null)
			{
				PluginGroup cellChildren = esmManager.getWRLDExtBlockCELLChildren(record.getFormID());

				if (cellChildren != null)
				{
					return new J3dCELLTemporary(recordStore, new Record(record, -1), ESMUtils.getChildren(cellChildren,
							PluginGroup.CELL_TEMPORARY), makePhys, mediaSources);
				}
			}

		}
		catch (PluginException e1)
		{
			e1.printStackTrace();
		}
		catch (DataFormatException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		return null;
	}

	@Override
	public J3dCELLDistant makeBGWRLDDistant(int wrldFormId, int x, int y, boolean makePhys)
	{
		int cellId = esmManager.getWRLDExtBlockCELLId(wrldFormId, x, y);
		return makeBGWRLDDistant(cellId, makePhys);
	}

	@Override
	public J3dCELLDistant makeBGWRLDDistant(int cellId, boolean makePhys)
	{
		if (cellId == -1)
			return null;

		try
		{
			PluginRecord record = esmManager.getWRLDExtBlockCELL(cellId);
			if (record != null)
			{
				PluginGroup cellChildren = esmManager.getWRLDExtBlockCELLChildren(record.getFormID());
				if (cellChildren != null)
				{
					List<Record> records = ESMUtils.getChildren(cellChildren, PluginGroup.CELL_DISTANT);
					records.addAll(getDistantTemps(ESMUtils.getChildren(cellChildren, PluginGroup.CELL_TEMPORARY)));
					return new J3dCELLDistant(recordStore, new Record(record, -1), records, makePhys, mediaSources);
				}
			}
		}
		catch (PluginException e1)
		{
			e1.printStackTrace();
		}
		catch (DataFormatException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return null;
	}

	@Override
	public J3dCELLPersistent makeBGInteriorCELLPersistent(int cellId, boolean makePhys)
	{
		try
		{
			PluginRecord record = esmManager.getInteriorCELL(cellId);

			if (record != null)
			{
				PluginGroup cellChildren = esmManager.getInteriorCELLChildren(cellId);

				return new J3dCELLPersistent(null, recordStore, new Record(record, -1), ESMUtils.getChildren(cellChildren,
						PluginGroup.CELL_PERSISTENT), makePhys, mediaSources);
			}
		}
		catch (PluginException e1)
		{
			e1.printStackTrace();
		}
		catch (DataFormatException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return null;
	}

	@Override
	public J3dCELLTemporary makeBGInteriorCELLTemporary(int cellId, boolean makePhys)
	{

		try
		{
			PluginRecord record = esmManager.getInteriorCELL(cellId);

			if (record != null)
			{
				PluginGroup cellChildren = esmManager.getInteriorCELLChildren(cellId);

				return new J3dCELLTemporary(recordStore, new Record(record, -1), ESMUtils.getChildren(cellChildren,
						PluginGroup.CELL_TEMPORARY), makePhys, mediaSources);
			}
		}
		catch (PluginException e1)
		{
			e1.printStackTrace();
		}
		catch (DataFormatException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return null;
	}

	@Override
	public J3dCELLDistant makeBGInteriorCELLDistant(int cellId, boolean makePhys)
	{

		try
		{
			PluginRecord record = esmManager.getInteriorCELL(cellId);

			if (record != null)
			{
				PluginGroup cellChildren = esmManager.getInteriorCELLChildren(cellId);

				return new J3dCELLDistant(recordStore, new Record(record, -1),
						ESMUtils.getChildren(cellChildren, PluginGroup.CELL_DISTANT), makePhys, mediaSources);
			}
		}
		catch (PluginException e1)
		{
			e1.printStackTrace();
		}
		catch (DataFormatException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return null;
	}

	private Collection<Record> getDistantTemps(List<Record> children)
	{
		ArrayList<Record> ret = new ArrayList<Record>();

		for (Iterator<Record> i = children.iterator(); i.hasNext();)
		{
			Record record = i.next();
			if (record.getRecordType().equals("REFR"))
			{
				REFR refr = new REFR(record);
				Record baseRecord = recordStore.getRecord(refr.NAMEref.str);
				if (baseRecord.getRecordType().equals("STAT"))
				{
					STAT stat = new STAT(baseRecord);

					if (stat.MODL != null)
					{
						if (Tes3ModelSizes.distant(stat.MODL.model.str, refr.getScale()))
							ret.add(record);

					}

				}
			}
		}
		return ret;
	}

	@Override
	public String getMainESMFileName()
	{
		return esmManager.getName();
	}

}
