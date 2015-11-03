package template;
import java.math.BigDecimal;

/**
 * Create by Mangol on 31.10.2015.
 */
public class NpcDrop
{
	private final int itemId;
	private final String name;
	private final long countMin;
	private final long countMax;
	private final BigDecimal chance;

	public NpcDrop(int itemId, String name, long countMin, long countMax, BigDecimal chance)
	{
		this.itemId = itemId;
		this.name = name;
		this.countMin = countMin;
		this.countMax = countMax;
		this.chance = chance;
	}

	public int getItemId()
	{
		return itemId;
	}

	public String getName()
	{
		return name;
	}

	public long getCountMin()
	{
		return countMin;
	}

	public long getCountMax()
	{
		return countMax;
	}

	public BigDecimal getChance()
	{
		return chance;
	}
}
