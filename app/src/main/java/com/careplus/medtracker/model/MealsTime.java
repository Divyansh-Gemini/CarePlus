package com.careplus.medtracker.model;

public class MealsTime {
    int meal_hour, meal_minute;

    public MealsTime()
    {   }

    public MealsTime(int meal_hour, int meal_minute) {
        this.meal_hour = meal_hour;
        this.meal_minute = meal_minute;
    }

    public int getMeal_hour() {
        return meal_hour;
    }

    public void setMeal_hour(int meal_hour) {
        this.meal_hour = meal_hour;
    }

    public int getMeal_minute() {
        return meal_minute;
    }

    public void setMeal_minute(int meal_minute) {
        this.meal_minute = meal_minute;
    }
}