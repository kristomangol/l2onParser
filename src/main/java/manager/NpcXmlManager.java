package manager;
import data.holder.NpcHolder;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import template.Npc;
import template.NpcDrop;
import template.RewardState;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

/**
 * Create by Mangol on 01.11.2015.
 */
public class NpcXmlManager
{
	private final static NpcXmlManager INSTANCE = new NpcXmlManager();
	private final NpcHolder holder = NpcHolder.getInstance();

	public static NpcXmlManager getInstance()
	{
		return INSTANCE;
	}

	public void load()
	{
		try
		{
			final Document doc = DocumentFactory.getInstance().createDocument();
			final Element add = doc.addElement("list");
			for(final Npc npc : holder.getNpcs())
			{
				final Element npcElemnent = add.addElement("npc");
				npcElemnent.addAttribute("id", String.valueOf(npc.getNpcId()));
				if(npc.getLevel() > 0)
				{
					addSet(npcElemnent, "level", npc.getLevel());
				}
				if(npc.getHp() > 0)
				{
					addSet(npcElemnent, "hp", npc.getHp());
				}
				if(npc.getExp() > 0)
				{
					addSet(npcElemnent, "exp", npc.getExp());
				}
				if(npc.getSp() > 0)
				{
					addSet(npcElemnent, "sp", npc.getSp());
				}
				if(npc.getRace() != null && npc.getRace().length() > 1)
				{
					addSet(npcElemnent, "race", npc.getRace());
				}
				if(npc.isHerbs())
				{
					addSet(npcElemnent, "isHerbs", true);
				}
				if(npc.getDrops().size() > 0)
				{
					final Element dropsElement = npcElemnent.addElement("drops");
					for(final Map.Entry<RewardState, Map<Integer, List<NpcDrop>>> state : npc.getDrops().entrySet())
					{
						final String group = state.getKey().name();
						final Element dropsGroup = dropsElement.addElement("group");
						dropsGroup.addAttribute("type", String.valueOf(group));
						for(final Map.Entry<Integer, List<NpcDrop>> dropCategory : state.getValue().entrySet())
						{
							final String categoryId = String.valueOf(dropCategory.getKey());
							final Element categoryElement = dropsGroup.addElement("category");
							categoryElement.addAttribute("id", categoryId);
							for(final NpcDrop drop : dropCategory.getValue())
							{
								final Element itemElement = categoryElement.addElement("item");
								itemElement.addAttribute("id", String.valueOf(drop.getItemId()));
								itemElement.addAttribute("name", String.valueOf(drop.getName()));
								itemElement.addAttribute("count_min", String.valueOf(drop.getCountMin()));
								itemElement.addAttribute("count_max", String.valueOf(drop.getCountMax()));
								itemElement.addAttribute("chance", String.valueOf(drop.getChance()));
							}
						}
					}
				}
			}
			final OutputFormat of = new OutputFormat("\t", true);
			of.setOmitEncoding(false);
			of.setEncoding("UTF-8");
			XMLWriter writer = null;
			try
			{
				writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream("./finish/" + "npc_drop.xml"), "UTF8"), of);
				writer.write(doc);
			}
			finally
			{
				if(writer != null)
				{
					writer.flush();
					writer.close();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private Element addSet(final Element e, final String name, final Object val)
	{
		final Element set = e.addElement("set");
		set.addAttribute("name", name);
		set.addAttribute("value", val.toString());
		return set;
	}
}
