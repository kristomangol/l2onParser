package data.holder;
import template.Npc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by Mangol on 01.11.2015.
 */
public class NpcHolder
{
	private final static NpcHolder INSTANCE = new NpcHolder();
	private final Map<Integer, Npc> npcs = new HashMap<>();

	public static NpcHolder getInstance()
	{
		return INSTANCE;
	}

	public synchronized void addNpc(final Npc npc)
	{
		npcs.put(npc.getNpcId(), npc);
	}

	public Collection<Npc> getNpcs()
	{
		return npcs.values();
	}
}
