package com.github.zkxs.ps2pack;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a single .pack file
 * @author Michael Ripley (<a href="mailto:zkxs00@gmail.com">zkxs00@gmail.com</a>) Apr 12, 2016
 */
public class PackFile
{	
	private final static int METADATA_LENGTH = 0x2000;
	private final static Charset charset = Charset.forName("US-ASCII");
	private Path packFilePath;
	private List<PackObject> objects;
	
	/**
	 * Construct a new PackFile object
	 * @param path NIO path to the pack file
	 * @throws IOException 
	 */
	public PackFile(Path path) throws IOException
	{
		packFilePath = path;
		objects = parseMetaData();
	}
	
	/**
	 * Construct a new PackFile object
	 * @param file File object representing the pack file
	 * @throws IOException 
	 */
	public PackFile(File file) throws IOException
	{
		this(file.toPath());
	}
	
	/**
	 * Construct a new PackFile object
	 * @param path path to the pack file
	 * @throws IOException 
	 */
	public PackFile(String path) throws IOException
	{
		this(Paths.get(path));
	}
	
	public String getName()
	{
		return packFilePath.getFileName().toString();
	}
	
	public List<PackObject> getObjects()
	{
		return objects;
	}
	
	
	private List<PackObject> parseMetaData() throws IOException
	{
		List<PackObject> objects = new LinkedList<>();
		
		// buffer for storing metadata
		byte[] metadataBytes = new byte[METADATA_LENGTH];
		
		// makes it easier to extract elements
		ByteBuffer metadata = ByteBuffer.wrap(metadataBytes);
		metadata.order(ByteOrder.BIG_ENDIAN);
		
		RandomAccessFile in = new RandomAccessFile(packFilePath.toFile(), "r");
		int nextMetadatadataOffset = 0;
		
		do
		{
			// get a metadata block
			in.seek(nextMetadatadataOffset);
			in.readFully(metadataBytes);
			metadata.rewind();
			
			nextMetadatadataOffset = metadata.getInt();
			assert(nextMetadatadataOffset >= 0) : "Turns out signed types are an issue";
			
			int numberOfEntries = metadata.getInt();
			assert(numberOfEntries >= 0) : "Turns out signed types are an issue";
			
			// read all entries
			for (int i = 0; i < numberOfEntries; i++)
			{
				int stringLength = metadata.getInt();
				assert(stringLength >= 0) : "Turns out signed types are an issue";
				
				// get string
				ByteBuffer stringBuffer = metadata.slice();
				stringBuffer.limit(stringLength); // not actually required
				byte[] stringBytes = new byte[stringLength];
				stringBuffer.get(stringBytes, 0, stringLength);
				String objectName = new String(stringBytes, charset);
				metadata.position(metadata.position() + stringLength);
				
				int absoluteOffset = metadata.getInt();
				int objectLength = metadata.getInt();
				int crc32 = metadata.getInt();
				
				PackObject packObject = new PackObject(this, objectName, absoluteOffset, objectLength, crc32);
				objects.add(packObject);
			}
		} while (nextMetadatadataOffset != 0);
		
		in.close();
		return objects;
	}
}
