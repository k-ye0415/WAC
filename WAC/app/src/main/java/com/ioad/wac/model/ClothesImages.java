package com.ioad.wac.model;

import com.ioad.wac.R;

import java.util.List;

public class ClothesImages {
    
    Integer[] temperature28 = {R.drawable.sleeveless,
            R.drawable.tshirt,
            R.drawable.short_pants,
            R.drawable.skirt
            };


    Integer[] temperature23to27 = {
            R.drawable.tshirt,
            R.drawable.shirt,
            R.drawable.short_pants,
            R.drawable.trousers
    };

    Integer[] temperature20to22 = {
            R.drawable.sweater,
            R.drawable.tshirt,
            R.drawable.trousers,
            R.drawable.jeans,
    };

    Integer[] temperature12to19 = {
            R.drawable.cardigan,
            R.drawable.sweatshirt,
            R.drawable.hoodie,
            R.drawable.jacket,
            R.drawable.trousers,
            R.drawable.jeans,
            R.drawable.starking
    };

    Integer[] temperature9to11 = {
            R.drawable.jacket,
            R.drawable.coat,
            R.drawable.hoddie_jacket,
            R.drawable.winter_sweater,
            R.drawable.trousers,
            R.drawable.jeans,
            R.drawable.starking
    };

    Integer[] temperature4to8 = {
            R.drawable.puffer_coat,
            R.drawable.winter_sweater,
            R.drawable.jeans,
            R.drawable.leggings,
    };

    Integer[] temperature3 = {
            R.drawable.padding_jacket,
            R.drawable.puffer_coat,
            R.drawable.scarf,
            R.drawable.mitten,
            R.drawable.beanie
    };


    public ClothesImages() {
    }

    public Integer[] getTemperature23to27() {
        return temperature23to27;
    }

    public void setTemperature23to27(Integer[] temperature23to27) {
        this.temperature23to27 = temperature23to27;
    }

    public Integer[] getTemperature20to22() {
        return temperature20to22;
    }

    public void setTemperature20to22(Integer[] temperature20to22) {
        this.temperature20to22 = temperature20to22;
    }

    public Integer[] getTemperature12to19() {
        return temperature12to19;
    }

    public void setTemperature12to19(Integer[] temperature12to19) {
        this.temperature12to19 = temperature12to19;
    }

    public Integer[] getTemperature9to11() {
        return temperature9to11;
    }

    public void setTemperature9to11(Integer[] temperature9to11) {
        this.temperature9to11 = temperature9to11;
    }

    public Integer[] getTemperature4to8() {
        return temperature4to8;
    }

    public void setTemperature4to8(Integer[] temperature4to8) {
        this.temperature4to8 = temperature4to8;
    }

    public Integer[] getTemperature3() {
        return temperature3;
    }

    public void setTemperature3(Integer[] temperature3) {
        this.temperature3 = temperature3;
    }

    public Integer[] getTemperature28() {
        return temperature28;
    }

    public void setTemperature28(Integer[] temperature28) {
        this.temperature28 = temperature28;
    }

}
