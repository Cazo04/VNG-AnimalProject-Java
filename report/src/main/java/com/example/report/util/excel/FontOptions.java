package com.example.report.util.excel;

import org.apache.poi.ss.usermodel.Font;

import com.example.report.util.Rgb;

public class FontOptions {
    public String name = "Arial";
    public double sizePt = 10;
    public Rgb color = new Rgb("#000000");
    public boolean bold = false;
    public boolean italic = false;
    public byte underline = Font.U_NONE;
}
