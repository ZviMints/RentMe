package com.example.rentme.Comperators;

import com.example.rentme.model.Product;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class sortByLastUploaded implements java.util.Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String strDate1 = (p1.getProductDetails().getUploadTime());
        String strDate2 = (p2.getProductDetails().getUploadTime());

        Date uploadDate1 = null;
        Date uploadDate2 = null;

        try {
            uploadDate1 = dateFormat.parse(strDate1);
            uploadDate2 = dateFormat.parse(strDate2);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return uploadDate2.compareTo(uploadDate1);
    }
}
