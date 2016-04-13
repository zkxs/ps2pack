package com.github.zkxs.ps2pack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple test for the current state of the API.
 * Right now it just lists the names of objects in Assets_000.pack
 * @author Michael Ripley (<a href="zkxs00@gmail.com">zkxs00@gmail.com</a>) Apr 12, 2016
 */

public class SimpleTest
{
	private final static String ASSETS_PATH = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\PlanetSide 2\\Resources\\Assets";
	
	public static void main(String[] args)
	{
		long startTime = System.nanoTime();
		
		Map<String, PackFile> packFiles = new HashMap<>();
		Map<String, PackObject> packObjects = new HashMap<>();
		
		try
		{
			Files.newDirectoryStream(Paths.get(ASSETS_PATH), p -> {
				String filename = p.getFileName().toString();
				return filename.startsWith("Assets_") && filename.endsWith(".pack");
			}).forEach(p -> {
				try
				{
					PackFile pf = new PackFile(p);
					packFiles.put(pf.getName(), pf);
					pf.getObjects().forEach(po -> {
						PackObject old = packObjects.putIfAbsent(po.getName(), po);
						if (old != null) throw new RuntimeException("Duplicate pack: " + po.getName());
					});
				}
				catch (IOException e)
				{
					System.err.printf("Error reading %s\n", p.toString());
					e.printStackTrace();
					System.exit(1);
				}
			});
		}
		catch (IOException e)
		{
			System.err.printf("Error enumerating directory %s\n", ASSETS_PATH.toString());
			e.printStackTrace();
			System.exit(1);
		}
		
		long stopTime = System.nanoTime();
		
		System.out.printf("Read all metadata in %.0fms\n", (stopTime - startTime) / 1_000_000d);
		
		packObjects.keySet().stream()
			.filter(s -> s.toLowerCase().endsWith(".jpg"))
			.forEach(s -> {
				PackObject po = packObjects.get(s);
				System.out.printf("%s in %s\n", s, po.getPackFile().getName());
			});
	}
}
