//Andrew ID: mpanindr
//Name: Manjunath K P

package com.cmu.blockchain.message;

/**
 * RequestMessage class represents a request message sent by the client to the server.
 * It encapsulates information such as action, data, and difficulty level.
 *
 * @param action     Fields Specifies the action to be performed by the server
 * @param data       Data associated with the request
 * @param difficulty Difficulty level, if applicable
 */
public record RequestMessage(String action, String data, int difficulty) {

    // Constructors

    /**
     * Constructs a RequestMessage object with the specified action, data, and difficulty level.
     *
     * @param action     Specifies the action to be performed by the server
     * @param data       Data associated with the request
     * @param difficulty Difficulty level, if applicable
     */
    public RequestMessage {
    }

    // Getters and Setters

    /**
     * Gets the action specified in the request.
     *
     * @return Action specified in the request
     */
    @Override
    public String action() {
        return action;
    }

    /**
     * Gets the data associated with the request.
     *
     * @return Data associated with the request
     */
    @Override
    public String data() {
        return data;
    }

    /**
     * Gets the difficulty level specified in the request.
     *
     * @return Difficulty level specified in the request
     */
    @Override
    public int difficulty() {
        return difficulty;
    }
}
