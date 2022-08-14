/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author arjan
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Artist implements Comparable<Artist>{

    private int id;
    @XmlElement(name = "name")
    private String name;
     @XmlElement(name = "role")
    private Role role;

    public Artist() {
    }

    public Artist(int id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Artist(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return name;
    }

    public static List<Artist> parseArtistFromData(String data, int roleid) throws Exception {
        List<Artist> artists = new ArrayList<>();
        String[] split = data.split(", ");
        for (int i = 0; i < split.length; i++) {
            switch (roleid) {
                case 1: //ACTOR
                    Role actorRole = new Role(roleid, "Glumac");
                    Artist actor = new Artist(split[i], actorRole);
                    artists.add(actor);
                    break;
                case 2: //DIRECTOR
                    Role directorRole = new Role(roleid, "Redatelj");
                    Artist director = new Artist(split[i], directorRole);
                    artists.add(director);
                    break;
                default:
                    throw new Exception("Parsing actors wen't wrong!");
            }
        }
        
        return artists;

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.role);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Artist other = (Artist) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.role, other.role)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Artist o) {
       return Integer.compare(id, o.id);
    }
    
    

    

    

}
