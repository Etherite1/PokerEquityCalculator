package com.example.backend;

public class Hand
{
    private Card card1, card2;
    public Hand(Card card1, Card card2)
    {
        this.card1 = card1;
        this.card2 = card2;
    }

    public Card getCard1()
    {
        return card1;
    }

    public Card getCard2()
    {
        return card2;
    }

    public String toString()
    {
        return card1.toString() + " " + card2.toString();
    }
}