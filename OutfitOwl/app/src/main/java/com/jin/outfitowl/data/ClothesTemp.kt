package com.jin.outfitowl.data

enum class ClothesTemp(val range: IntRange, val valueName: String) {
    FREEZING(IntRange(Int.MIN_VALUE, 4), "FREEZING"),           // ~ 4
    VERY_COLD(5..8, "VERY_COLD"),                                // 5 ~ 8
    COLD(9..11, "COLD"),                                    // 9 ~ 11
    COOL(12..16, "COOL"),                                   // 12 ~ 16
    MILD(17..19, "MILD"),                                   // 17 ~ 19
    WARM(20..22, "WARM"),                                   // 20 ~ 22
    HOT(23..27, "HOT"),                                    // 23 ~ 27
    VERY_HOT(28..Int.MAX_VALUE, "VERY_HOT");                    // 28 ~

    companion object {
        fun findByClothesTemp(temperature: Int): ClothesTemp {
            return entries.find { temperature in it.range } ?: FREEZING
        }
    }
}