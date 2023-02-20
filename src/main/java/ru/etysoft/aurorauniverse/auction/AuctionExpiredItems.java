package ru.etysoft.aurorauniverse.auction;

import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.etysoft.aurorauniverse.Logger;

import java.util.ArrayList;

import static ru.etysoft.aurorauniverse.auction.AuctionItem.itemStackFromString;
import static ru.etysoft.aurorauniverse.auction.AuctionItem.itemStackToString;

public class AuctionExpiredItems {

    private String name;

    private ArrayList<ItemStack> itemStacks = new ArrayList<>();

    public static ArrayList<String> namesOfAuctionPlayersItems = new ArrayList<>();

    private static ArrayList<AuctionExpiredItems> inventoriesOfPlayers = new ArrayList<>();

    public AuctionExpiredItems(String name)
    {
        this.name = name;
        namesOfAuctionPlayersItems.add(name);
        inventoriesOfPlayers.add(this);
    }

    public static ArrayList<String> getNamesOfAuctionPlayersItems() {
        return namesOfAuctionPlayersItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ArrayList<AuctionExpiredItems> getExpiredInventories() {
        return inventoriesOfPlayers;
    }

    public static AuctionExpiredItems getAuctionExpiredItems(String name) {
        AuctionExpiredItems items = null;

        for (AuctionExpiredItems playerItems: inventoriesOfPlayers)
        {
            if (playerItems.getName().equals(name))
            {
                items = playerItems;
                break;
            }
        }

        return items;
    }

    public ArrayList<ItemStack> getItemStacks() {
        return itemStacks;
    }

    public static void addItemToAuctionExpiredInventory(ItemStack itemStack, String name)
    {
        for (AuctionExpiredItems playerItems: inventoriesOfPlayers)
        {
            if (playerItems.getName().equals(name))
            {
                playerItems.getItemStacks().add(itemStack);
                return;
            }
        }
        AuctionExpiredItems auctionPlayerItems = new AuctionExpiredItems(name);
        auctionPlayerItems.getItemStacks().add(itemStack);
    }

    public static void removeItemInAuctionExpiredInventory(ItemStack itemStack, String name)
    {
        for (AuctionExpiredItems playerItems: inventoriesOfPlayers)
        {
            if (playerItems.getName().equals(name))
            {
                playerItems.getItemStacks().remove(itemStack);
                break;
            }
        }
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(Types.NAME, name);

        JSONArray itemsArray = new JSONArray();

        if (this.getItemStacks().size() > 0)
        {
            for (ItemStack itemStack: this.getItemStacks())
            {
                JSONObject itemStackJson = new JSONObject();
                itemStackJson.put(Types.ITEM_DATA, itemStackToString(itemStack));
                itemsArray.add(itemStackJson);
            }

            jsonObject.put(Types.DATA, itemsArray);
        }
        else
        {
            jsonObject.put(Types.DATA, "");
        }

        return jsonObject;
    }

    public static AuctionExpiredItems fromJSON(JSONObject jsonObject)
    {
        String name = jsonObject.get(Types.NAME).toString();

        AuctionExpiredItems auctionPlayerItems = new AuctionExpiredItems(name);

        if (!jsonObject.get(Types.DATA).toString().equals(""))
        {
            JSONArray jsonArray = (JSONArray) jsonObject.get(Types.DATA);

            for (int i = 0; i < jsonArray.size(); i++)
            {
                JSONObject object = (JSONObject) jsonArray.get(i);
                try {
                    ItemStack itemStack = itemStackFromString(object.get(Types.ITEM_DATA).toString());

                    auctionPlayerItems.getItemStacks().add(itemStack);
                }
                catch (Exception e)
                {
                    Logger.error("Can't load AuctionExpiredItem of " + name);
                }
            }
        }

        return auctionPlayerItems;
    }

    public static class Types {
        public static final String NAME = "NAME";
        public static final String ITEM_DATA = "ITEM_DATA";
        public static final String DATA = "DATA";
    }
}
