package esmj3dtes3.data.records;

import java.util.ArrayList;

import tools.io.ESMByteConvert;
import esmLoader.common.data.record.Record;
import esmLoader.common.data.record.Subrecord;
import esmj3d.data.shared.records.InstRECO;
import esmj3dtes3.data.subrecords.DATA;

/**
 * Note no location data for these ones
 * @author philip
 *
 */
public class LAND extends InstRECO
{
	public int landX = 0;

	public int landY = 0;

	public DATA DATA = null;

	public byte[] VNML = null;

	public byte[] VCLR = null;

	//VCLR (12675 bytes) optional
	//Vertex color array, looks like another RBG image 65x65 pixels in size.

	public byte[] VHGT = null;

	public short[] VTEXs;

	//VTEX (512 bytes) optional
	//A 16x16 array of short texture indices (from a LTEX record I think).

	public LAND(Record recordData)
	{
		super(recordData);

		ArrayList<Short> VTEXsv = new ArrayList<Short>();

		ArrayList<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getData();

			if (sr.getType().equals("INTV"))
			{
				landX = ESMByteConvert.extractInt(bs, 0);
				landY = ESMByteConvert.extractInt(bs, 4);
			}
			else if (sr.getType().equals("DATA"))
			{
				DATA = new DATA(bs);
			}
			else if (sr.getType().equals("VNML"))
			{
				VNML = bs;
			}
			else if (sr.getType().equals("VHGT"))
			{
				VHGT = bs;
			}
			else if (sr.getType().equals("VCLR"))
			{
				VCLR = bs;
			}
			else if (sr.getType().equals("VTEX"))
			{
				for (int f = 0; f < bs.length; f += 2)
				{
					VTEXsv.add((short) ESMByteConvert.extractShort(bs, f));
				}
			}
			else
			{
				System.out.println("unhandled : " + sr.getType() + " in record " + recordData + " in " + this);
			}

		}

		VTEXs = new short[VTEXsv.size()];
		for (int j = 0; j < VTEXsv.size(); j++)
		{
			VTEXs[j] = VTEXsv.get(j);
		}

	}

	public String showDetails()
	{
		return "LAND : (" + formId + "|" + Integer.toHexString(formId) + ") ";
	}
}
