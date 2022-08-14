/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.models;

/**
 *
 * @author arjan
 */
public class UserRole {
    
    private int id;
    private String name;

    public UserRole() {
    }

    public UserRole(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserRole(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
