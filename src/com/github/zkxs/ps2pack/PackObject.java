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
}
