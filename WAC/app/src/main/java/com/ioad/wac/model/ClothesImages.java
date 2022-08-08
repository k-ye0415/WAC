package com.ioad.wac.model;

import com.ioad.wac.R;

import java.util.ArrayList;
import java.util.List;

public class ClothesImages {
    
    List<Integer> temperature;

    public ClothesImages() {
        this.temperature = new ArrayList<>();
        temperature.add(R.drawable.sleeveless);
        temperature.add(R.drawable.tshirt);
        temperature.add(R.drawable.short_pants);
        temperature.add(R.drawable.skirt);
    }

//    ArrayList<Integer> temperature28 = {
//            R.drawable.sleeveless,
//            R.drawable.tshirt,
//            R.drawable.short_pants,
//            R.drawable.skirt
//    };
//
//    ArrayList<Integer> temperature23to27 = {
//            R.drawable.tshirt,
//            R.drawable.shirt,
//            R.drawable.short_pants,
//            R.drawable.trousers
//    };
//
//    ArrayList<Integer> temperature20to22 = {
//            R.drawable.sweater,
//            R.drawable.tshirt,
//            R.drawable.trousers,
//            R.drawable.jeans,
//    };
//
//    ArrayList<Integer> temperature12to19 = {
//            R.drawable.cardigan,
//            R.drawable.sweatshirt,
//            R.drawable.hoodie,
//            R.drawable.jacket,
//            R.drawable.trousers,
//            R.drawable.jeans,
//            R.drawable.starking
//    };
//
//    ArrayList<Integer> temperature9to11 = {
//            R.drawable.jacket,
//            R.drawable.coat,
//            R.drawable.hoddie_jacket,
//            R.drawable.winter_sweater,
//            R.drawable.trousers,
//            R.drawable.jeans,
//            R.drawable.starking
//    };
//
//    ArrayList<Integer> temperature4to8 = {
//            R.drawable.puffer_coat,
//            R.drawable.winter_sweater,
//            R.drawable.jeans,
//            R.drawable.leggings,
//    };
//
//    ArrayList<Integer> temperature3 = {
//            R.drawable.padding_jacket,
//            R.drawable.puffer_coat,
//            R.drawable.scarf,
//            R.drawable.mitten,
//            R.drawable.beanie
//    };


    public List<Integer> getTemperature() {
        return temperature;
    }

    public void setTemperature(List<Integer> temperature) {
        this.temperature = temperature;
    }
}
