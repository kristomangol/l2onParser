package template;
import java.util.*;

/**
 * Create by Mangol on 31.10.2015.
 */
public class Npc
{
	private final int npcId;
	private int level;
	private String race;
	private int hp;
	private long exp;
	private long sp;
	private boolean herbs;
	private final Map<RewardState, Map<Integer, List<NpcDrop>>> drops = new HashMap<>();

	public Npc(final int npcId)
	{
		this.npcId = npcId;
	}

	public int getNpcId()
	{
		return npcId;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public String getRace()
	{
		return race;
	}

	public void setRace(String race)
	{
		this.race = race;
	}

	public int getHp()
	{
		return hp;
	}

	public void setHp(int hp)
	{
		this.hp = hp;
	}

	public long getExp()
	{
		return exp;
	}

	public void setExp(long exp)
	{
		this.exp = exp;
	}

	public long getSp()
	{
		return sp;
	}

	public void setSp(long sp)
	{
		this.sp = sp;
	}

	public boolean isHerbs()
	{
		return herbs;
	}

	public void setHerbs(boolean herbs)
	{
		this.herbs = herbs;
	}

	public void addDrop(final RewardState state, final int categoryId, final NpcDrop drop)
	{
		if(drops.containsKey(state))
		{
			if(drops.get(state).containsKey(categoryId))
			{
				final List<NpcDrop> d = drops.get(state).get(categoryId);
				d.add(drop);
			}
			else
			{
				drops.get(state).put(categoryId, new ArrayList<>(Collections.singletonList(drop)));
			}
		}
		else
		{
			final Map<Integer, List<NpcDrop>> map = new HashMap<>();
			map.put(categoryId, new ArrayList<>(Collections.singletonList(drop)));
			drops.put(state, map);
		}
	}

	public Map<RewardState, Map<Integer, List<NpcDrop>>> getDrops()
	{
		return drops;
	}
}
