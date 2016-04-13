package com.github.zkxs.ps2pack;

/**
 * Represents a single object contained within a .pack file
 * @author Michael Ripley (<a href="zkxs00@gmail.com">zkxs00@gmail.com</a>) Apr 12, 2016
 */

public class PackObject
{	
	private PackFile packFile;
	private String name;
	private int absoluteOffset;
	private int length;
	private int crc32;
	
	PackObject(PackFile packFile, String name, int absoluteOffset, int length, int crc32)
	{
		this.packFile = packFile;
		this.name = name;
		this.absoluteOffset = absoluteOffset;
		this.length = length;
		this.crc32 = crc32;
	}
	
	@Override
	public String toString()
	{
		return name;
	}

	/**
	 * @return the PackFile this PackObject is stored within
	 */
	public PackFile getPackFile()
	{
		return packFile;
	}

	/**
	 * @return the name of this PackObject
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the offset of this PackObject within the PackFile it is stored within
	 */
	public int getAbsoluteOffset()
	{
		return absoluteOffset;
	}

	/**
	 * @return The length, in bytes, of this PackObject
	 */
	public int getLength()
	{
		return length;
	}

	/**
	 * @return The CRC32 of this PackObject's bytes
	 */
	public int getCrc32()
	{
		return crc32;
	}
}
