package esmj3dtes3.data.subrecords;

import tools.io.ESMByteConvert;

public class BMDT
{

	public static int HEAD_MASK = 0x00000001; //= Head

	public static int HAIR_MASK = 0x00000002; //= Hair

	public static int UPPER_BODY_MASK = 0x00000004; //= Upper Body

	public static int LOWER_BODY_MASK = 0x00000008; //= Lower Body

	public static int HAND_MASK = 0x00000010; //= Hand

	public static int FOOT_MASK = 0x00000020; //= Foot

	public static int RIGHT_RING_MASK = 0x00000040; //= Right Ring

	public static int LEFT_RING_MASK = 0x00000080; //= Left Ring

	public static int AMULET_MASK = 0x00000100; //= Amulet

	public static int WEAPON_MASK = 0x00000200; //= Weapon

	public static int BACK_WEAPON_MASK = 0x00000400; //= Back Weapon

	public static int SIDE_WEAPON_MASK = 0x00000800; //= Side Weapon

	public static int QUIVER_MASK = 0x00001000; //= Quiver

	public static int SHIELD_MASK = 0x00002000; //= Shield

	public static int TORCH_MASK = 0x00004000; //= Torch

	public static int TAIL_MASK = 0x00008000; //= Tail

	/*0x00010000 = Hide Rings 
	0x00020000 = Hide Amulet 
	0x00400000 = Non-Playable (opposite to checkbox in the CS) 
	0xCD000000 = Unknown default value*/

	public int flags;

	public BMDT(byte[] bytes)
	{
		flags = ESMByteConvert.extractInt(bytes, 0);
	}

	public boolean isHead()
	{
		return (flags & HEAD_MASK) > 0;
	}

	public boolean isHair()
	{
		return (flags & HAIR_MASK) > 0;
	}

	public boolean isUpperBody()
	{
		return (flags & UPPER_BODY_MASK) > 0;
	}

	public boolean isLowerBody()
	{
		return (flags & LOWER_BODY_MASK) > 0;
	}

	public boolean isHand()
	{
		return (flags & HAND_MASK) > 0;
	}

	public boolean isFoot()
	{
		return (flags & FOOT_MASK) > 0;
	}

	public boolean isRightRing()
	{
		return (flags & RIGHT_RING_MASK) > 0;
	}

	public boolean isLeftRing()
	{
		return (flags & LEFT_RING_MASK) > 0;
	}

	public boolean isAmulet()
	{
		return (flags & AMULET_MASK) > 0;
	}

	public boolean isWeapon()
	{
		return (flags & WEAPON_MASK) > 0;
	}

	public boolean isBackWeapon()
	{
		return (flags & BACK_WEAPON_MASK) > 0;
	}

	public boolean isSideWeapon()
	{
		return (flags & SIDE_WEAPON_MASK) > 0;
	}

	public boolean isQuiver()
	{
		return (flags & QUIVER_MASK) > 0;
	}

	public boolean isShield()
	{
		return (flags & SHIELD_MASK) > 0;
	}

	public boolean isTorch()
	{
		return (flags & TORCH_MASK) > 0;
	}

	public boolean isTail()
	{
		return (flags & TAIL_MASK) > 0;
	}

}
