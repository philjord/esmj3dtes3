package esmj3dtes3.data.subrecords;

import tools.io.ESMByteConvert;

public class AIDT
{
	public byte aggression;

	public byte confidence;

	public byte energyLevel;

	public byte responsibility;

	public int aiFlags;

	public byte trainSkill;

	public byte trainLevel;

	public static int WEAPONS_MASK = 0x000001; // 	Weapons

	public static int ARMOR_MASK = 0x000002; // 	Armor

	public static int CLOTHING_MASK = 0x000004; // 	Clothing

	public static int BOOKS_MASK = 0x000008; // 	Books

	public static int INGREDIENTS_MASK = 0x000010; // 	Ingredients

	public static int LIGHTS_MASK = 0x000080; // 	Lights

	public static int APPARATUS_MASK = 0x000100; // 	Apparatus

	public static int MISCELLANEOUS_MASK = 0x000400; // 	Miscellaneous

	public static int SPELLS_MASK = 0x000800; // 	Spells

	public static int MAGIC_ITEMS_MASK = 0x001000; // 	Magic Items

	public static int POTIONS_MASK = 0x002000; // 	Potions

	public static int TRAINING_MASK = 0x004000; // 	Training

	public static int RECHARGE_MASK = 0x010000; // 	Recharge

	public static int REPAIR_MASK = 0x020000; // 	Repair

	public AIDT(byte[] bytes)
	{
		aggression = ESMByteConvert.extractByte(bytes, 0);
		confidence = ESMByteConvert.extractByte(bytes, 1);
		energyLevel = ESMByteConvert.extractByte(bytes, 2);
		responsibility = ESMByteConvert.extractByte(bytes, 3);
		aiFlags = ESMByteConvert.extractInt(bytes, 4);
		trainSkill = ESMByteConvert.extractByte(bytes, 8);
		trainLevel = ESMByteConvert.extractByte(bytes, 9);
		//		2 more bytes possibly flag possibly 4 byte align padding 
	}

	public boolean isWeapons()
	{
		return (aiFlags & WEAPONS_MASK) > 0;
	}

	public boolean isArmor()
	{
		return (aiFlags & ARMOR_MASK) > 0;
	}

	public boolean isClothing()
	{
		return (aiFlags & CLOTHING_MASK) > 0;
	}

	public boolean isBooks()
	{
		return (aiFlags & BOOKS_MASK) > 0;
	}

	public boolean isIngredients()
	{
		return (aiFlags & INGREDIENTS_MASK) > 0;
	}

	public boolean isLights()
	{
		return (aiFlags & LIGHTS_MASK) > 0;
	}

	public boolean isApparatus()
	{
		return (aiFlags & APPARATUS_MASK) > 0;
	}

	public boolean isMiscellaneous()
	{
		return (aiFlags & MISCELLANEOUS_MASK) > 0;
	}

	public boolean isSpells()
	{
		return (aiFlags & SPELLS_MASK) > 0;
	}

	public boolean isMagicItems()
	{
		return (aiFlags & MAGIC_ITEMS_MASK) > 0;
	}

	public boolean isPotions()
	{
		return (aiFlags & POTIONS_MASK) > 0;
	}

	public boolean isTraining()
	{
		return (aiFlags & TRAINING_MASK) > 0;
	}

	public boolean isRecharge()
	{
		return (aiFlags & RECHARGE_MASK) > 0;
	}

	public boolean isRepair()
	{
		return (aiFlags & REPAIR_MASK) > 0;
	}

}
