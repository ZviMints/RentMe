package com.example.rentme.Comperators;

import com.example.rentme.model.Product;

import java.util.Comparator;

public class sortByLastUploaded implements Comparator<Product>
{
    public int compare(Product a, Product b) {  return b.getDate().compareTo(a.getDate()); }
}
