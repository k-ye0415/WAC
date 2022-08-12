package com.ioad.wac.model;

import com.ioad.wac.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClothesImages {

    Map<String, Integer> temperature28 = new HashMap<>();
    Map<String, Integer> temperature23to27 = new HashMap<>();
    Map<String, Integer> temperature20to22 = new HashMap<>();
    Map<String, Integer> temperature12to19 = new HashMap<>();
    Map<String, Integer> temperature9to11 = new HashMap<>();
    Map<String, Integer> temperature4to8 = new HashMap<>();
    Map<String, Integer> temperature3 = new HashMap<>();


//    Integer[] temperature28 = {
//            R.drawable.sleeveless,
//            R.drawable.tshirt,
//            R.drawable.short_pants,
//            R.drawable.skirt
//    };


//    Integer[] temperature23to27 = {
//            R.drawable.tshirt,
//            R.drawable.shirt,
//            R.drawable.short_pants,
//            R.drawable.trousers
//    };
//
//    Integer[] temperature20to22 = {
//            R.drawable.sweater,
//            R.drawable.tshirt,
//            R.drawable.trousers,
//            R.drawable.jeans,
//    };
//
//    Integer[] temperature12to19 = {
//            R.drawable.cardigan,
//            R.drawable.sweatshirt,
//            R.drawable.hoodie,
//            R.drawable.jacket,
//            R.drawable.trousers,
//            R.drawable.jeans,
//            R.drawable.starking
//    };
//
//    Integer[] temperature9to11 = {
//            R.drawable.jacket,
//            R.drawable.coat,
//            R.drawable.hoddie_jacket,
//            R.drawable.winter_sweater,
//            R.drawable.trousers,
//            R.drawable.jeans,
//            R.drawable.starking
//    };
//
//    Integer[] temperature4to8 = {
//            R.drawable.puffer_coat,
//            R.drawable.winter_sweater,
//            R.drawable.jeans,
//            R.drawable.leggings,
//    };
//
//    Integer[] temperature3 = {
//            R.drawable.padding_jacket,
//            R.drawable.puffer_coat,
//            R.drawable.scarf,
//            R.drawable.mitten,
//            R.drawable.beanie
//    };

    public ClothesImages() {
    }

    public Map<String, Integer> getTemperature28() {
        this.temperature28.put("민소매", R.drawable.sleeveless);
        this.temperature28.put("티셔츠", R.drawable.tshirt);
        this.temperature28.put("반바지", R.drawable.short_pants);
        this.temperature28.put("치마", R.drawable.skirt);
        return temperature28;
    }

    public void setTemperature28(Map<String, Integer> temperature28) {
        this.temperature28 = temperature28;
    }

    public Map<String, Integer> getTemperature23to27() {
        this.temperature23to27.put("티셔츠", R.drawable.tshirt);
        this.temperature23to27.put("얇은셔츠", R.drawable.shirt);
        this.temperature23to27.put("반바지", R.drawable.short_pants);
        this.temperature23to27.put("면바지", R.drawable.trousers);
        return temperature23to27;
    }

    public void setTemperature23to27(Map<String, Integer> temperature23to27) {
        this.temperature23to27 = temperature23to27;
    }

    public Map<String, Integer> getTemperature20to22() {
        this.temperature20to22.put("긴팔티", R.drawable.sweater);
        this.temperature20to22.put("가디건", R.drawable.cardigan);
        this.temperature20to22.put("면바지", R.drawable.trousers);
        this.temperature20to22.put("청바지", R.drawable.jeans);
        return temperature20to22;
    }

    public void setTemperature20to22(Map<String, Integer> temperature20to22) {
        this.temperature20to22 = temperature20to22;
    }

    public Map<String, Integer> getTemperature12to19() {
        this.temperature12to19.put("가디건", R.drawable.cardigan);
        this.temperature12to19.put("맨투맨", R.drawable.sweatshirt);
        this.temperature12to19.put("후드티", R.drawable.hoodie);
        this.temperature12to19.put("자켓", R.drawable.jacket);
        this.temperature12to19.put("면바지", R.drawable.trousers);
        this.temperature12to19.put("청바지", R.drawable.jeans);
        return temperature12to19;
    }

    public void setTemperature12to19(Map<String, Integer> temperature12to19) {
        this.temperature12to19 = temperature12to19;
    }

    public Map<String, Integer> getTemperature9to11() {
        this.temperature9to11.put("자켓", R.drawable.jacket);
        this.temperature9to11.put("얇은코트", R.drawable.coat);
        this.temperature9to11.put("야상", R.drawable.hoddie_jacket);
        this.temperature9to11.put("니트", R.drawable.winter_sweater);
        this.temperature9to11.put("면바지", R.drawable.trousers);
        this.temperature9to11.put("청바지", R.drawable.jeans);
        return temperature9to11;
    }

    public void setTemperature9to11(Map<String, Integer> temperature9to11) {
        this.temperature9to11 = temperature9to11;
    }

    public Map<String, Integer> getTemperature4to8() {
        this.temperature4to8.put("두꺼운코트", R.drawable.puffer_coat);
        this.temperature4to8.put("니트", R.drawable.winter_sweater);
        this.temperature4to8.put("청바지", R.drawable.jeans);
        return temperature4to8;
    }

    public void setTemperature4to8(Map<String, Integer> temperature4to8) {
        this.temperature4to8 = temperature4to8;
    }

    public Map<String, Integer> getTemperature3() {
        this.temperature3.put("패팅", R.drawable.padding_jacket);
        this.temperature3.put("두꺼운코트", R.drawable.puffer_coat);
        this.temperature3.put("목도리", R.drawable.scarf);
        return temperature3;
    }

    public void setTemperature3(Map<String, Integer> temperature3) {
        this.temperature3 = temperature3;
    }
}
