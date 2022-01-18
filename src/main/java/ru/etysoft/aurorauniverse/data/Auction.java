package ru.etysoft.aurorauniverse.data;


import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.auction.AuctionItem;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.world.Nation;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Auction {

    private static ArrayList<AuctionItem> listings = new ArrayList<>();

    public static void addListing(AuctionItem auctionItem) {
        listings.add(auctionItem);
    }

    public static boolean removeListing(AuctionItem auctionItem) {
        return listings.remove(auctionItem);
    }

    public static long getTimeExpired(AuctionItem auctionItem)
    {
        long timeExp = AuroraUniverse.getInstance().getConfig().getLong("auction-expired-sec") * 1000;

        return timeExp - System.currentTimeMillis() + auctionItem.getTime();


    }


    public static boolean isExpired(AuctionItem auctionItem)
    {
        long timeExp = AuroraUniverse.getInstance().getConfig().getLong("auction-expired-sec") * 1000;


        if(System.currentTimeMillis() - auctionItem.getTime() > timeExp)
        {
            return true;
        }
        return false;

    }

    public static int getListingsCountByTown(Town t) {
        int count = 0;
        for(AuctionItem auctionItem : listings)
        {
            try {
                if(auctionItem.getResident().getTown() == t) count++;
            } catch (TownNotFoundedException ignored) {

            }
        }

        return count;
    }

    public static ArrayList<AuctionItem> getListings() {
        ArrayList<AuctionItem> toRemove = new ArrayList<>();
        for(AuctionItem auctionItem : listings)
        {
            if(isExpired(auctionItem))
            {
                toRemove.add(auctionItem);
            }
        }
        listings.removeAll(toRemove);
        return listings;
    }

    public static boolean loadListings() {
        listings.clear();
        if ((new File("plugins/AuroraUniverse/auction.json").exists())) {
            JSONParser jsonParser = new JSONParser();
            JSONObject mainJson = null;
            try {
                mainJson = (JSONObject) jsonParser.parse(readFile());

                JSONArray listings = (JSONArray) mainJson.get("listings");

                for (int i = 0; i < listings.size(); i++) {
                    JSONObject jsonObject = (JSONObject) listings.get(i);
                    AuctionItem auctionItem = AuctionItem.fromJSON(jsonObject);
                    addListing(auctionItem);

                }


            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            Logger.log("Storage not found. Loading finished");
        }
        return true;

    }

    private static String readFile() throws IOException {
        File file = new File("plugins/AuroraUniverse/auction.json");
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    private static void saveStringToFile(String string) {
        try (PrintWriter out = new PrintWriter("plugins/AuroraUniverse/auction.json")) {
            out.println(string);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void saveListings() {
        JSONObject jsonObject = new JSONObject();

        JSONArray listingsArray = new JSONArray();
        for (AuctionItem auctionItem : listings) {
            listingsArray.add(auctionItem.toJSON());
        }

        jsonObject.put("listings", listingsArray);
        saveStringToFile(jsonObject.toJSONString());
    }
}
