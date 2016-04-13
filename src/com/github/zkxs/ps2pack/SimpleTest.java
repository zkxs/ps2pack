package com.github.zkxs.ps2pack;

import java.io.IOException;

/**
 * A simple test for the current state of the API.
 * Right now it just lists the names of objects in Assets_000.pack
 * @author Michael Ripley (<a href="zkxs00@gmail.com">zkxs00@gmail.com</a>) Apr 12, 2016
 */

public class SimpleTest
{	
	public static void main(String[] args)
	{
		try
		{
			PackFile pf = new PackFile(
				"C:\\Program Files (x86)\\Steam\\steamapps\\common\\PlanetSide 2\\Resources\\Assets\\Assets_000.pack"
			);
			pf.getObjects().forEach(System.out::println);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
