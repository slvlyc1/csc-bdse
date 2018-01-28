package ru.csc.bdse.model.app;

/**
 * Address book record
 *
 * @author alesavin
 */
public interface Record {

    Character letter();

    String name();
    String surName();
    String phone(); // TODO nullable?
}
