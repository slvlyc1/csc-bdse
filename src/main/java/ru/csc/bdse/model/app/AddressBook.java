package ru.csc.bdse.model.app;

import java.util.Iterator;

/**
 * Represent address book with phone records
 *
 * @author alesavin
 */
public interface AddressBook<R extends Record> {

    void add(R record); //TODO delete operation is special add?

    Iterator<R> get(Character letter);
}
