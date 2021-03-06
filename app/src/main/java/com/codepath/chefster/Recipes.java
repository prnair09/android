package com.codepath.chefster;

import com.codepath.chefster.models.Dish;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Recipes {

    public static ArrayList<Dish> fromInputStream(InputStream inputStream) {
        ArrayList<Dish> recipes;

        String json;
        try {

            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            recipes = gson.fromJson(json,new TypeToken<ArrayList<Dish>>(){}.getType());

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return  recipes;
    }
}
