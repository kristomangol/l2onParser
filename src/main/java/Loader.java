import data.parser.NpcParser;
import data.parser.client.NpcNameParser;

import java.io.IOException;

/**
 * Create by Mangol on 31.10.2015.
 */
public class Loader
{
	public static void main(String args[]) throws IOException
	{
		NpcNameParser.getInstance().load();
		NpcParser.getInstance().load();
	}
}
