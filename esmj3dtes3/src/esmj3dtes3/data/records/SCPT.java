package esmj3dtes3.data.records;

import java.util.List;

import esmj3d.data.shared.records.RECO;
import esmmanager.common.data.record.Record;
import esmmanager.common.data.record.Subrecord;
import tools.io.ESMByteConvert;

public class SCPT extends RECO
{
	public SCHD SCHD;

	public String SCVR;

	public byte[] SCDT;

	public String scriptText;
	/*
	SCPT =   631 (   100,   1248.95,   9966)
	Script
	SCHD = Script Header (52 bytes)
		char Name[32]
		long NumShorts
		long NumLongs
		long NumFloats
		long ScriptDataSize
		long LocalVarSize
	SCVR = List of all the local script variables seperated by '\0' NULL characters.
	SCDT = The compiled script data
	SCTX = Script text
	 */

	public SCPT(Record recordData)
	{
		super(recordData);
		List<Subrecord> subrecords = recordData.getSubrecords();
		for (int i = 0; i < subrecords.size(); i++)
		{
			Subrecord sr = subrecords.get(i);
			byte[] bs = sr.getSubrecordData();

			if (sr.getSubrecordType().equals("SCHD"))
			{
				SCHD = new SCHD(bs);
			}
			else if (sr.getSubrecordType().equals("SCVR"))
			{
				SCVR = new String(bs);
			}
			else if (sr.getSubrecordType().equals("SCDT"))
			{
				SCDT = bs;
			}
			else if (sr.getSubrecordType().equals("SCTX"))
			{
				scriptText = new String(bs);
			}
			else
			{
				System.out.println("unhandled : " + sr.getSubrecordType() + " in record " + recordData + " in " + this);
			}

		}
	}

	public class SCHD
	{
		public String Name;
		public int NumShorts;
		public int NumLongs;
		public int NumFloats;
		public int ScriptDataSize;
		public int LocalVarSize;

		public SCHD(byte[] bs)
		{
			Name = new String(bs, 0, 32);
			NumShorts = ESMByteConvert.extractInt(bs, 32);
			NumLongs = ESMByteConvert.extractInt(bs, 36);
			NumFloats = ESMByteConvert.extractInt(bs, 40);
			ScriptDataSize = ESMByteConvert.extractInt(bs, 44);
			LocalVarSize = ESMByteConvert.extractInt(bs, 48);
		}
	}
}
