package ru.csc.bdse.app.phonebook;

import java.util.Optional;
import java.util.Set;

/**
 * Phone book record
 *
 * @author alesavin
 */

public interface Record {

    /**
     * Returns literals, associated with Record
     */
    Set<Character> literals();

    String getName();

    String getSurname();

    String getPhone();

    String getNickname();

    String getAdditionalPhone();
}