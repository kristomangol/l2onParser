package data.holder.client;
import template.client.NpcName;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by Mangol on 01.11.2015.
 */
public class NpcNameHolder
{
	private static final NpcNameHolder INSTANCE = new NpcNameHolder();
	private final Map<Integer, NpcName> npcs = new HashMap<>();

	public static NpcNameHolder getInstance()
	{
		return INSTANCE;
	}

	public void addNpc(final NpcName npc)
	{
		npcs.put(npc.getId(), npc);
	}

	public int[] getNpcArray()
	{
		return npcs.keySet().stream().mapToInt(e -> e).toArray();
	}
	public int size()
	{
		return npcs.size();
	}

	public Map<Integer, NpcName> getNpcs()
	{
		return npcs;
	}
}
