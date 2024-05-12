package esmj3dtes3.data.records;

import java.util.List;

import esfilemanager.common.data.record.Record;
import esfilemanager.common.data.record.Subrecord;
import esmj3d.data.shared.records.CommonREFR;
import esmj3d.data.shared.subrecords.XTEL;
import esmj3d.data.shared.subrecords.ZString;
import tools.io.ESMByteConvert;

public class REFR extends CommonREFR
{
	public int FRMR = 0;

	public String NAMEref;

	public String DNAM;

	//loaded by commonREFR for us into xtel
	/*DODT = XYZ Pos, XYZ Rotation of exit (24 bytes, Door objects)
			float XPos
			float YPos
			float ZPos
			float XRotate
			float YRotate
			float ZRotate
		DNAM = Door exit name (Door objects)*/

	public REFR(Record recordData)
	{
		super(recordData, false);

		List<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getSubrecordData();

			if (sr.getSubrecordType().equals("FRMR"))
			{
				FRMR = ESMByteConvert.extractInt(bs, 0);
			}
			else if (sr.getSubrecordType().equals("NAME"))
			{
				NAMEref = ZString.toString(bs);
			}
			else if (sr.getSubrecordType().equals("DNAM"))
			{
				DNAM = ZString.toString(bs);
			}
			else if (sr.getSubrecordType().equals("DODT"))
			{//noe load data into xtel
				XTEL = new XTEL(bs);
			}
			else if (sr.getSubrecordType().equals("XSCL"))
			{
				scale = ESMByteConvert.extractFloat(bs, 0);
			}
			else if (sr.getSubrecordType().equals("DATA"))
			{
				this.extractInstData(bs);
			}

		}

	}

	@Override
	public String toString()
	{
		return super.showDetails() +  " : " + (NAMEref != null ? NAMEref : "");
	}
}
