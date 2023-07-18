package com.example.backend;

import java.util.HashMap;

public class Card
{
    static HashMap<String, Integer> map_val = new HashMap<>();
    static HashMap<String, Integer> map_suit = new HashMap<>();

    private String strValue, strSuit;
    private int value, suit;

    public Card(String strValue, String strSuit)
    {
        if(map_val.isEmpty())
        {
            map_val.put("A", 12);
            map_val.put("K", 11);
            map_val.put("Q", 10);
            map_val.put("J", 9);
            map_val.put("T", 8);
            map_val.put("9", 7);
            map_val.put("8", 6);
            map_val.put("7", 5);
            map_val.put("6", 4);
            map_val.put("5", 3);
            map_val.put("4", 2);
            map_val.put("3", 1);
            map_val.put("2", 0);

            map_suit.put("s", 3);
            map_suit.put("c", 2);
            map_suit.put("d", 1);
            map_suit.put("h", 0);
        }
        this.strValue = strValue;
        this.strSuit = strSuit;
        this.value = map_val.get(this.strValue);
        this.suit = map_suit.get(this.strSuit);
    }

    public int getValue()
    {
        return this.value;
    }

    public int getSuit()
    {
        return this.suit;
    }

    public String toString()
    {
        return this.strValue + this.strSuit;
    }
}
