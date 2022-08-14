/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author arjan
 */
public class PublishedDateAdapter extends XmlAdapter<String, LocalDateTime>{

    @Override
    public LocalDateTime unmarshal(String v) throws Exception {
        return LocalDateTime.parse(v);
    }

    

    @Override
    public String marshal(LocalDateTime date) throws Exception {
        return date.format(Movie.DATE_FORMATTER);
    }
    
}
