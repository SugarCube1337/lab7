package org.lab7.udp;

import java.io.Serializable;

/**
 * The `ServerCommandType` enum represents different types of commands that can be sent and processed by the server.
 * These command types correspond to various actions that can be performed on the server.
 */
public enum ServerCommandType implements Serializable {
    CLEAR, FILTER_BY_TYPE, GROUP_COUNTING_BY_TYPE, GET_INFO, GET_MAX_DISTANCE, REMOVE_BY_ID, REMOVE_GREATER, REMOVE_LAST, REORDER, SHOW, SAVE, ADD, GET, UPDATE, ERROR,
    ACTUALIZE, AUTH, REGISTER;
}
