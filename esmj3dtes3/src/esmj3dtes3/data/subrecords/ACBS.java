package esmj3dtes3.data.subrecords;

import tools.io.ESMByteConvert;

public class ACBS
{
	public int flags;

	public int baseSpell;

	public int fatigue;

	public int barterGold;

	public int level;

	public int calcMin;

	public int calcMax;

	public static int FEMALE_MASK = 0x000001; // 	Female

	public static int ESSENTIAL_MASK = 0x000002; // 	Essential

	public static int WEAPON_AND_SHIELD_MASK = 0x000004; // 	Weapon & Shield

	public static int RESPAWN_MASK = 0x000008; //	Respawn

	public static int AUTO_CALC_STATS_MASK = 0x000010; // 	Auto-calc stats

	public static int PC_LEVEL_OFFSET_MASK = 0x000080; // 	PC Level Offset

	public static int NO_LOW_LEV_PRO_MASK = 0x000200; // 	No Low Level Processing

	public static int NO_RUMORS_MASK = 0x002000; // 	No Rumors

	public static int SUMMONABLE_MASK = 0x004000; // 	Summonable

	public static int NO_PERSUASION_MASK = 0x008000; // 	No Persuasion// for NPC only 

	public static int NO_HEAD_MASK = 0x008000; // 	No Head // for CREA only

	public static int NO_RIGHT_ARM_MASK = 0x010000; //	No Right Arm

	public static int NO_LEFT_ARM_MASK = 0x020000; //	No Left Arm

	public static int NO_COMBAT_IN_WATER_MASK = 0x040000; // 	No Combat in Water

	public static int NO_SHADOW_MASK = 0x080000; // 	No Shadow

	public static int CAN_CORPSE_CHECK_MASK = 0x100000; // 	Can Corpse Check // for NPC only

	public static int NO_CORPSE_CHECK_MASK = 0x100000; //	NO Corpse Check // for CREA only

	public ACBS(byte[] bytes)
	{
		flags = ESMByteConvert.extractInt(bytes, 0);
		baseSpell = ESMByteConvert.extractShort(bytes, 4);
		fatigue = ESMByteConvert.extractShort(bytes, 6);
		barterGold = ESMByteConvert.extractShort(bytes, 8);
		level = ESMByteConvert.extractShort(bytes, 10);
		calcMin = ESMByteConvert.extractShort(bytes, 12);
		calcMax = ESMByteConvert.extractShort(bytes, 14);
	}

	public boolean isFemale()
	{
		return (flags & FEMALE_MASK) > 0;
	}

	public boolean isEssential()
	{
		return (flags & ESSENTIAL_MASK) > 0;
	}

	public boolean isWeaponAndShield()
	{
		return (flags & WEAPON_AND_SHIELD_MASK) > 0;
	}

	public boolean isRespawn()
	{
		return (flags & RESPAWN_MASK) > 0;
	}

	public boolean isAutoCalcStats()
	{
		return (flags & AUTO_CALC_STATS_MASK) > 0;
	}

	public boolean isPCLevelOffset()
	{
		return (flags & PC_LEVEL_OFFSET_MASK) > 0;
	}

	public boolean isNoLowLevelProcessing()
	{
		return (flags & NO_LOW_LEV_PRO_MASK) > 0;
	}

	public boolean isNoRumors()
	{
		return (flags & NO_RUMORS_MASK) > 0;
	}

	public boolean isSummonable()
	{
		return (flags & SUMMONABLE_MASK) > 0;
	}

	/**
	 * For NPC_ only
	 * @return
	 */
	public boolean isNoPersuasion()
	{
		return (flags & NO_PERSUASION_MASK) > 0;
	}

	/**
	 * For CREA only
	 * @return
	 */
	public boolean isNoHead()
	{
		return (flags & NO_HEAD_MASK) > 0;
	}

	public boolean isNoRightArm()
	{
		return (flags & NO_RIGHT_ARM_MASK) > 0;
	}

	public boolean isNoLeftArm()
	{
		return (flags & NO_LEFT_ARM_MASK) > 0;
	}

	public boolean isNoCombatInWater()
	{
		return (flags & NO_COMBAT_IN_WATER_MASK) > 0;
	}

	public boolean isNoShadow()
	{
		return (flags & NO_SHADOW_MASK) > 0;
	}

	/**
	 * For NPC_ only
	 * @return
	 */
	public boolean isCanCorpseCheck()
	{
		return (flags & CAN_CORPSE_CHECK_MASK) > 0;
	}

	/**
	 * For CREA only
	 * @return
	 */
	public boolean isNoCorpseCheck()
	{
		return (flags & NO_CORPSE_CHECK_MASK) > 0;
	}
}
