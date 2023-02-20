package ru.etysoft.aurorauniverse.auction;


import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.json.simple.JSONObject;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Auction;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.world.Resident;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AuctionItem {

    private String resident;
    private double price;
    private ItemStack itemStack;
    private long time;

    private static final String ITEM_DATA = "ITEM_DATA";
    private static final String RESIDENT = "RESIDENT";
    private static final String PRICE = "PRICE";
    private static final String TIME = "TIME";


    public AuctionItem(ItemStack itemStack, String resident, double price) {

        this.resident = resident;
        this.price = price;
        this.time = System.currentTimeMillis();
        this.itemStack = itemStack;
    }

    private static Map<String, Object> convertWithStream(String mapAsString) {
        Map<String, Object> map = Arrays.stream(mapAsString.split(","))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
        return map;
    }

    public long getExpTime()
    {
        return Auction.getTimeExpired(this);
    }

    public Resident getResident() {
        return Residents.getResident(resident);
    }

    public double getPrice() {
        return price;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public long getTime() {
        return time;
    }

    public static ItemStack itemStackFromString(String itemStackString) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(itemStackString));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            for (int Index = 0; Index < items.length; Index++) {
                Map<String, Object> stack = (Map<String, Object>) dataInput.readObject();

                if (stack != null) {
                    items[Index] = ItemStack.deserialize(stack);
                } else {
                    items[Index] = null;
                }
            }

            dataInput.close();
            return items[0];
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public static String itemStackToString(ItemStack itemStack) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(1);


            if (itemStack != null) {
                dataOutput.writeObject(itemStack.serialize());
            } else {
                dataOutput.writeObject(null);
            }


            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ITEM_DATA, itemStackToString(itemStack));
        jsonObject.put(RESIDENT, resident);
        jsonObject.put(PRICE, price);
        jsonObject.put(TIME, time);
        return jsonObject;
    }

    public static AuctionItem fromJSON(JSONObject jsonObject) {
        ItemStack itemStack = null;
        try {
            itemStack = itemStackFromString((String) jsonObject.get(ITEM_DATA));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("Error loading auction item!");
        }
        double price = (double) jsonObject.get(PRICE);
        String resident = (String) jsonObject.get(RESIDENT);
        AuctionItem auctionItem = new AuctionItem(itemStack, resident, price);
        auctionItem.time = (long) jsonObject.get(TIME);
        return auctionItem;
    }

}
