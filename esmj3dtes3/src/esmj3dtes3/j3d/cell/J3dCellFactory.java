package esmj3dtes3.j3d.cell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.DataFormatException;

import esmio.common.PluginException;
import esmio.common.data.plugin.PluginGroup;
import esmio.common.data.plugin.PluginRecord;
import esmio.common.data.record.Record;
import esmio.loader.IESMManager;
import esmio.utils.ESMUtils;
import esmj3d.j3d.cell.AIActorServices;
import esmj3d.j3d.cell.AICellGeneral;
import esmj3d.j3d.cell.J3dICellFactory;
import esmj3d.j3d.cell.MorphingLandscape;
import esmj3dtes3.data.records.REFR;
import esmj3dtes3.data.records.STAT;
import esmj3dtes3.data.records.WRLD;
import utils.source.MediaSources;

public class J3dCellFactory extends J3dICellFactory
{

	public J3dCellFactory()
	{

	}

	/**
	 * Note no persistent records loaded for now
	 */
	@Override
	public void setSources(IESMManager esmManager2, MediaSources mediaSources)
	{
		this.esmManager = esmManager2;
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
			WRLD wrld = new WRLD(record);
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
				return new J3dCELLPersistent(wrld, this, cell, new ArrayList<Record>(), makePhys, mediaSources);
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

		try
		{

			PluginRecord record = esmManager.getWRLDExtBlockCELL(wrldFormId, x, y);

			if (record != null)
			{
				PluginGroup cellChildren = esmManager.getWRLDExtBlockCELLChildren(wrldFormId, x, y);

				if (cellChildren != null)
				{
					return new J3dCELLTemporary(this, record, ESMUtils.getChildren(cellChildren, PluginGroup.CELL_TEMPORARY), makePhys,
							mediaSources);
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

		try
		{
			PluginRecord record = esmManager.getWRLDExtBlockCELL(wrldFormId, x, y);
			if (record != null)
			{
				PluginGroup cellChildren = esmManager.getWRLDExtBlockCELLChildren(wrldFormId, x, y);
				if (cellChildren != null)
				{
					List<Record> records = ESMUtils.getChildren(cellChildren, PluginGroup.CELL_DISTANT);
					records.addAll(getDistantTemps(ESMUtils.getChildren(cellChildren, PluginGroup.CELL_TEMPORARY)));
					return new J3dCELLDistant(this, record, records, makePhys, mediaSources);
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

				return new J3dCELLPersistent(null, this, record, ESMUtils.getChildren(cellChildren, PluginGroup.CELL_PERSISTENT), makePhys,
						mediaSources);
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

				return new J3dCELLTemporary(this, record, ESMUtils.getChildren(cellChildren, PluginGroup.CELL_TEMPORARY), makePhys,
						mediaSources);
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

				return new J3dCELLDistant(this, record, ESMUtils.getChildren(cellChildren, PluginGroup.CELL_DISTANT), makePhys,
						mediaSources);
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
				Record baseRecord = getRecord(refr.NAMEref.str);
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

	@Override
	public Record getParentWRLDLAND(int wrldFormId, int x, int y)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public AICellGeneral makeAICell(int cellId, AIActorServices aiActorLocator)
	{
		try
		{
			PluginRecord record = esmManager.getInteriorCELL(cellId);

			if (record != null)
			{
				PluginGroup cellChildren = esmManager.getInteriorCELLChildren(cellId);

				return new AICellTes3(this, record, ESMUtils.getChildren(cellChildren, PluginGroup.CELL_TEMPORARY), aiActorLocator);
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
	public AICellGeneral makeAICell(int wrldFormId, int x, int y, AIActorServices aiActorLocator)
	{
		try
		{
			PluginRecord record = esmManager.getWRLDExtBlockCELL(wrldFormId, x, y);

			if (record != null)
			{
				PluginGroup cellChildren = esmManager.getWRLDExtBlockCELLChildren(wrldFormId, x, y);

				if (cellChildren != null)
				{
					return new AICellTes3(this, record, wrldFormId, x, y, ESMUtils.getChildren(cellChildren, PluginGroup.CELL_TEMPORARY),
							aiActorLocator);
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

}
