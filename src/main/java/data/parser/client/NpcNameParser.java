package data.parser.client;
import data.holder.client.NpcNameHolder;
import template.client.NpcName;

import java.io.*;

/**
 * Create by Mangol on 01.11.2015.
 */
public class NpcNameParser
{
	private static final NpcNameParser INSTANCE = new NpcNameParser();
	private final NpcNameHolder holder = NpcNameHolder.getInstance();

	public static NpcNameParser getInstance()
	{
		return INSTANCE;
	}

	public void load()
	{
		LineNumberReader lnr = null;
		int lineNum = 0;
		try
		{
			final File snData = new File("./data/client/npcname-e.txt");
			lnr = new LineNumberReader(new BufferedReader(new FileReader(snData)));
			String line;
			while((line = lnr.readLine()) != null)
			{
				lineNum++;
				if(line.contains("#"))
				{
					continue;
				}
				final String args[] = line.split("\t", -1);
				final int id = Integer.parseInt(args[0]);
				final String name = args[1].replace(".\\0", "").replace("u,", "").replace("a,", "");
				final String description = args[2].replace(".\\0", "").replace("u,", "").replace("a,", "");
				final NpcName client = new NpcName(id, name, description);
				holder.addNpc(client);
			}
			System.out.println("NpcNameParser: npcname-e.txt size " + holder.size());
		}
		catch(FileNotFoundException e)
		{
			System.out.println("NpcNameParser: npcname-e.txt is missing in data folder");
		}
		catch(Exception e)
		{
			System.out.println("NpcNameParser: npcname-e.txt Error in line " + lineNum + " :" + e);
		}
		finally
		{
			if(lnr != null)
			{
				try
				{
					lnr.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
