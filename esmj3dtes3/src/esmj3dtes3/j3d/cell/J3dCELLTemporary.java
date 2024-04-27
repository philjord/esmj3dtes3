package esmj3dtes3.j3d.cell;

import java.util.Iterator;
import java.util.List;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Group;

import esfilemanager.common.data.record.IRecordStore;
import esfilemanager.common.data.record.Record;
import esmj3d.j3d.cell.J3dCELLGeneral;
import esmj3d.j3d.j3drecords.inst.J3dLAND;
import esmj3d.j3d.j3drecords.inst.J3dRECOInst;
import tools3d.audio.SimpleSounds;
import utils.source.MediaSources;

public class J3dCELLTemporary extends J3dCELL
{

	public J3dCELLTemporary(IRecordStore master, Record cellRecord, List<Record> children, boolean makePhys, MediaSources mediaSources)
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
			J3dRECOInst jri = makeJ3dRECO(record);
			addJ3dRECOInst(jri);

			if (jri instanceof J3dLAND)
			{
				J3dLAND l = (J3dLAND) jri;
				float wl = getWaterLevel(cell.WHGT);
				if (wl > l.getLowestHeight())
				{
					Group water = makeWater(wl, J3dCELLPersistent.waterApp);
					BranchGroup soundBG = SimpleSounds
							.createPointSound(mediaSources.getSoundSource().getMediaContainer("Sound\\Fx\\envrn\\watr_wave.wav"), 10, -1);
					water.addChild(soundBG);
					addChild(water);

				}
			}
		}
	}
}
