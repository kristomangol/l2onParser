package template.client;
/**
 * Create by Mangol on 01.11.2015.
 */
public class NpcName
{
	private final int id;
	private final String description;
	private final String name;

	public NpcName(int id, String name, String description)
	{
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}
}
