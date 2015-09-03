package esmj3dtes3.data.subrecords;

import tools.io.ESMByteConvert;

public class SNDX
{
	public static int RandomFrequencyShift = 0x0001; //= Random Frequency Shift

	public static int PlayAtRandom = 0x0002; // = Play At Random

	public static int EnvironmentIgnored = 0x0004; // = Environment Ignored

	public static int RandomLocation = 0x0008; // = Random Location

	public static int Loop = 0x0010; // = Loop

	public static int MenuSound = 0x0020; // = Menu Sound

	public static int TwoD = 0x0040; // = 2D

	public static int Three60LFE = 0x0080; // = 360 LFE 

	public byte minimumAttenuationDistance = 0;

	public byte maximumAttenuationDistance = 0;

	public byte frequencyAdjustment = 0;

	public byte unknownByte = 0;

	public int flags = 0;

	public int unknownShort = 0;

	public int staticAttenuation = 0;

	public byte stopTime = 0;

	public byte startTime = 0;

	/**
	 *  SNDX 	
	byte 	Minimum attentuation distance (multiply by 5 to get value in game units).
	byte 	Maximum attentuation distance (multiply by 100 to get value in game units).
	char 	Frequency adjustment, %, signed value
	byte 	Unknown
	word 	Flags
	
	    * 0x0001 = Random Frequency Shift
	    * 0x0002 = Play At Random
	    * 0x0004 = Environment Ignored
	    * 0x0008 = Random Location
	    * 0x0010 = Loop
	    * 0x0020 = Menu Sound
	    * 0x0040 = 2D
	    * 0x0080 = 360 LFE 
	
	word 	Unknown
	word 	Static attentuation (divide by 100 to get value in dB).
	byte 	Stop time (multiply by 1440/256 to get value in minutes).
	byte 	Start time (multiply by 1440/256 to get value in minutes).
	 * @param bytes
	 */

	public SNDX(byte[] bytes)
	{
		minimumAttenuationDistance = ESMByteConvert.extractByte(bytes, 0);
		maximumAttenuationDistance = ESMByteConvert.extractByte(bytes, 1);
		frequencyAdjustment = ESMByteConvert.extractByte(bytes, 2);
		unknownByte = ESMByteConvert.extractByte(bytes, 3);
		flags = ESMByteConvert.extractShort(bytes, 4);
		unknownShort = ESMByteConvert.extractShort(bytes, 6);
		staticAttenuation = ESMByteConvert.extractShort(bytes, 8);
		stopTime = ESMByteConvert.extractByte(bytes, 10);
		startTime = ESMByteConvert.extractByte(bytes, 11);
	}

	public boolean isRandomFrequencyShift()
	{
		return (RandomFrequencyShift & flags) > 0;
	}

	public boolean isPlayAtRandom()
	{
		return (PlayAtRandom & flags) > 0;
	}

	public boolean isEnvironmentIgnored()
	{
		return (EnvironmentIgnored & flags) > 0;
	}

	public boolean isRandomLocation()
	{
		return (RandomLocation & flags) > 0;
	}

	public boolean isLoop()
	{
		return (Loop & flags) > 0;
	}

	public boolean isMenuSound()
	{
		return (MenuSound & flags) > 0;
	}

	public boolean is2D()
	{
		return (TwoD & flags) > 0;
	}

	public boolean is360LFE()
	{
		return (Three60LFE & flags) > 0;
	}
}
