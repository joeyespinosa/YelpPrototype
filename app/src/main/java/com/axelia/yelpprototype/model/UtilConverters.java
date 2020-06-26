package com.axelia.yelpprototype.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class UtilConverters {

    private static Gson gson = new Gson();

    @TypeConverter
    public static String fromCategoryList(List<Category> items) {
        return gson.toJson(items);
    }

    @TypeConverter
    public static List<Category> toCategoryList(String items) {
        if (items == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Category>>() {}.getType();
        return gson.fromJson(items, listType);
    }

    @TypeConverter
    public static String fromLocation(Location item) {
        return gson.toJson(item);
    }

    @TypeConverter
    public static Location toLocation(String item) {
        Type itemType = new TypeToken<Location>() {}.getType();
        return gson.fromJson(item, itemType);
    }
}
