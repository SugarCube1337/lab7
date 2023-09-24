package org.lab7.udp;

import java.io.Serializable;

/**
 * Enum representing the types of server commands that can be sent between the client and server.
 */
public enum ServerCommandType implements Serializable {
    CLEAR, FILTER_BY_TYPE, GROUP_COUNTING_BY_TYPE, GET_INFO, GET_MAX_DISTANCE, REMOVE_BY_ID, REMOVE_GREATER, REMOVE_LAST, REORDER, SHOW, ADD, GET, UPDATE, ERROR,
    ACTUALIZE, AUTH, REGISTER;
}
