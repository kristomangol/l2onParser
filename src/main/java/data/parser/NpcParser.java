package data.parser;
import data.holder.NpcHolder;
import data.holder.client.NpcNameHolder;
import manager.NpcXmlManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import template.Npc;
import template.NpcDrop;
import template.RewardState;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Create by Mangol on 31.10.2015.
 */
public class NpcParser
{
	public static final NpcParser INSTANCE = new NpcParser();
	private final NpcNameHolder holderNpcName = NpcNameHolder.getInstance();
	private final NpcHolder holderNpc = NpcHolder.getInstance();

	public static NpcParser getInstance()
	{
		return INSTANCE;
	}

	public void load()
	{
		final String url = "http://l2on.net/en/?c=npc&id=";
		for(final Integer npcId : holderNpcName.getNpcArray())
		{
			System.out.println(" npc id " + npcId);
			Optional<Document> doc = Optional.empty();
			try
			{
				doc = Optional.ofNullable(Jsoup.connect(url + npcId).get());
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			if(doc.isPresent())
			{
				final Npc npc = new Npc(npcId);
				final Stream<Element> tr = doc.get().getElementsByTag("tr").stream();
				tr.forEach(e -> {
					final Optional<Element> th = Optional.ofNullable(e.getElementsByTag("th").first());
					if(th.isPresent())
					{
						final boolean bLevel = th.get().ownText().matches("^Level|LEVEL|level.*");
						final boolean bRace = th.get().ownText().matches("^Race|RACE|race.*");
						final boolean bHP = th.get().ownText().matches("^HP|Hp|hP|hp.*");
						final boolean bExp = th.get().ownText().matches("^Exp|EXP|exp.*");
						final boolean bSp = th.get().ownText().matches("^SP|Sp|sp|sP.*");
						final boolean bHerbs = th.get().ownText().matches("^Herbs|herbs|HERBS.*");
						final Optional<Element> td = Optional.ofNullable(e.getElementsByTag("td").first());
						if(td.isPresent())
						{
							String text;
							if(bLevel)
							{
								text = td.get().ownText();
								if(text.matches("^[0-9].*"))
								{
									npc.setLevel(Integer.parseInt(text));
								}
							}
							else if(bRace)
							{
								npc.setRace(td.get().ownText());
							}
							else if(bHP)
							{
								text = td.get().ownText().replaceAll("\\s", "");
								if(text.matches("^[0-9].*"))
								{
									npc.setHp(Integer.parseInt(text));
								}
							}
							else if(bExp)
							{
								text = td.get().ownText().replaceAll("\\s", "");
								if(text.matches("^[0-9].*"))
								{
									npc.setExp(Long.parseLong(text));
								}
							}
							else if(bSp)
							{
								text = td.get().ownText().replaceAll("\\s", "");
								if(text.matches("^[0-9].*"))
								{
									npc.setSp(Long.parseLong(text));
								}
							}
							else if(bHerbs)
							{
								npc.setHerbs(td.get().ownText().equalsIgnoreCase("yes"));
							}
						}
					}
				});
				final Elements mars = doc.get().getElementsByClass("mar");
				final Elements drop = doc.get().getElementsByAttributeValueContaining("class", "black-links npc-items drop-spoil");
				int index = 0;
				for(final Element mar : mars)
				{
					final boolean noNothing = mar.nextElementSibling().ownText().matches("^[N,n]o\\s*[N,n]othing.*");
					if(mar.ownText().matches("^[D,d]rop.*"))
					{
						if(!noNothing)
						{
							if(drop.size() > 0)
							{
								index++;
								final Optional<Elements> rewards = Optional.ofNullable(drop.get(index - 1).select("tbody").select("tr"));
								if(rewards.isPresent())
								{
									addCategory(RewardState.DROP, npc, rewards.get());
								}
							}
						}
					}
					else if(mar.ownText().matches("^[S,s]poil.*"))
					{
						if(!noNothing)
						{
							if(drop.size() > 0)
							{
								index++;
								final Optional<Elements> rewards = Optional.ofNullable(drop.get(index - 1).select("tbody").select("tr"));
								if(rewards.isPresent())
								{
									addCategory(RewardState.SPOIL, npc, rewards.get());
								}
							}
						}
					}
				}
				holderNpc.addNpc(npc);
			}
			else
			{
				System.out.println(" connect isNull ...");
			}
		}
		NpcXmlManager.getInstance().load();
	}

	private void addCategory(final RewardState state, final Npc npc, final Elements rewards)
	{
		for(final Element reward : rewards)
		{
			final int itemId = Integer.parseInt(reward.select("td[class=name]").select("a[href]").attr("data-item"));
			final String itemName = reward.select("td[class=name]").select("a[href]").text();
			final long[] amout = getCount(reward.select("td[class=amount]").text());
			final Optional<String> itemCategory0 = Optional.ofNullable(reward.select("td[class=chance pc0]").text());
			final Optional<String> itemCategory1 = Optional.ofNullable(reward.select("td[class=chance pc1]").text());
			final Optional<String> itemCategory2 = Optional.ofNullable(reward.select("td[class=chance pc2]").text());
			final Optional<String> itemCategory3 = Optional.ofNullable(reward.select("td[class=chance pc3]").text());
			BigDecimal chance;
			if(itemCategory0.isPresent() && itemCategory0.get().length() > 1)
			{
				chance = getChance(itemCategory0.get());
				npc.addDrop(state, 0, new NpcDrop(itemId, itemName, amout[0], amout[1], chance));
			}
			else if(itemCategory1.isPresent() && itemCategory1.get().length() > 1)
			{
				chance = getChance(itemCategory1.get());
				npc.addDrop(state, 1, new NpcDrop(itemId, itemName, amout[0], amout[1], chance));
			}
			else if(itemCategory2.isPresent() && itemCategory2.get().length() > 1)
			{
				chance = getChance(itemCategory2.get());
				npc.addDrop(state, 2, new NpcDrop(itemId, itemName, amout[0], amout[1], chance));
			}
			else if(itemCategory3.isPresent() && itemCategory3.get().length() > 1)
			{
				chance = getChance(itemCategory3.get());
				npc.addDrop(state, 3, new NpcDrop(itemId, itemName, amout[0], amout[1], chance));
			}
		}
	}

	/**
	 * @param str
	 * @return - возвращает шанс получения итема в @BigDecimal т.к. нам нужно получить точные цифры.
	 */
	private BigDecimal getChance(final String str)
	{
		final String replaces = str.replace("%", "").replaceAll("\\s", "");
		if(replaces.contains("/"))
		{
			final String[] str1 = replaces.split("/");
			return BigDecimal.valueOf(Double.parseDouble(str1[0]) / Double.parseDouble(str1[1]));
		}
		return BigDecimal.valueOf(Double.parseDouble(replaces));
	}

	/**
	 * @param str
	 * @return - возвращает массив из min[0],max[1]- количество награды.
	 */
	private long[] getCount(final String str)
	{
		/**
		 * replaceFirst("\\p{Pd}", "-")
		 * http://unicode.org/reports/tr18/
		 */
		final String replaces = str.replaceFirst("\\p{Pd}", "-").replaceAll("\\s", "");
		final long[] countArrays = new long[2];
		if(replaces.contains("-"))
		{
			final String[] str1 = replaces.split("-");
			countArrays[0] = Long.parseLong(str1[0]);
			countArrays[1] = Long.parseLong(str1[1]);
			return countArrays;
		}
		final long amout = Long.parseLong(replaces);
		countArrays[0] = amout;
		countArrays[1] = amout;
		return countArrays;
	}
}