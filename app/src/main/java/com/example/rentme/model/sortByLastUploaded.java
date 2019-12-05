package com.example.rentme.model;

import java.util.Comparator;

public class sortByLastUploaded implements Comparator<Product>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Product a, Product b)
    {
        return b.getUtc().compareTo(a.getUtc());
    }
}
