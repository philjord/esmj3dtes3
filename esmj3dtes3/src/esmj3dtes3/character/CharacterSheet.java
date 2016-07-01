package esmj3dtes3.character;

import java.util.ArrayList;

import esmj3dtes3.data.records.NPCO;

public class CharacterSheet
{
	private boolean female = false;

	private String raceName = ""; 

	private boolean isBeast = false;

	private String headNif = "";

	private String hairNif = "";

	private ArrayList<NPCO> NPCOs = new ArrayList<NPCO>();

	public CharacterSheet(boolean female, String raceName, String headNif, String hairNif, ArrayList<NPCO> NPCOs)
	{
		this.female = female;
		this.raceName = raceName;
		isBeast = raceName.equals("Argonian") || raceName.equals("Khajiit");

		this.NPCOs = NPCOs;
		this.headNif = headNif;
		this.hairNif = hairNif;
	}

	public boolean isFemale()
	{
		return female;
	}

	public String getRaceName()
	{
		return raceName;
	}

	public boolean isBeast()
	{
		return isBeast;
	}

	public String getHeadNif()
	{
		return headNif;
	}

	public String getHairNif()
	{
		return hairNif;
	}

	public ArrayList<NPCO> getNPCOs()
	{
		return NPCOs;
	}

}
