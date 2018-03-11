package ru.csc.bdse.app.phonebook;

import java.util.Optional;
import java.util.Set;

/**
 * Phone book record
 *
 * @author alesavin
 */

//todo: suggest to use JSON
//todo: and just make fields additionalPhones & nickname optional in it
public interface Record {

    /**
     * Returns literals, associated with Record
     */
    Set<Character> literals();

    String getName();

    String getSurname();

    String getPhone();

    Optional<Set<String>> getAdditionalPhones();

    Optional<String> getNickname();
}