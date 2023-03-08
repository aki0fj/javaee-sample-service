package org.employeeservice.entity;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
    @Override
    public LocalDate unmarshal(String v) {
        System.out.println("unmarshal before=" + v);
        LocalDate d = LocalDate.parse(v, formatter);
        System.out.println("unmarshal after=" + String.valueOf(d));
        return d;
    }
    @Override
    public String marshal(LocalDate v) {
        System.out.println("marshal before=" + String.valueOf(v));
        String s = v.format(formatter);
        System.out.println("marshal after=" + s);
        return s;
    }
}