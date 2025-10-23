package com.example.report.util;

import org.apache.poi.xssf.usermodel.XSSFColor;

public class Rgb {

    private String hex;

    public Rgb(String hex){
        this.hex = hex;
    }

    public byte[] toBytes() {
        String h = (hex == null ? "000000" : hex.trim());
        if (h.startsWith("#")) h = h.substring(1);
        if (h.length() != 6) {
            throw new IllegalArgumentException("Hex color must be 6 characters (RGB format)");
        }
        // Validate hex format
        if (!h.matches("[0-9A-Fa-f]{6}")) {
            throw new IllegalArgumentException("Invalid hex color format");
        }
        
        // Return RGB bytes for Apache POI
        int r = Integer.parseInt(h.substring(0, 2), 16);
        int g = Integer.parseInt(h.substring(2, 4), 16);
        int b = Integer.parseInt(h.substring(4, 6), 16);
        
        return new byte[] { (byte)r, (byte)g, (byte)b };
    }

    public XSSFColor toXSSFColor() {
        return new XSSFColor(toBytes(), null);
    }
}
