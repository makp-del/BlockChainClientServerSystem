//Andrew ID: mpanindr
//Name: Manjunath K P

package com.cmu.blockchain.model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;

/**
 * Represents a block in the blockchain.
 * A block is a fundamental unit of a blockchain that contains transactions or data, a timestamp marking its creation,
 * a link to the previous block (via hash), and a nonce used for mining (proof-of-work).
 * The block also includes a difficulty level for the proof-of-work algorithm.
 * The block's hash is calculated using the SHA-256 hashing algorithm.
 * The proof-of-work process involves finding a nonce that results in a hash with a specific number of leading zeroes.
 * The difficulty level determines the number of leading zeroes required in the hash.
 * The nonce is incremented until the hash satisfies the difficulty condition.
 * The block's hash is recalculated after the proof-of-work process.
 * The block is immutable once created, and its attributes can be accessed using getters.
 * The block can be represented as a JSON string for serialization and communication.
 * The block is part of a blockchain, which is a chain of blocks linked together.
 */
public class Block {
    private final int index; // Position of the block within the blockchain.
    private final Timestamp timestamp; // Time the block was created.
    private String data; // Data (transaction details) contained in the block.
    private String previousHash; // Hash of the previous block in the chain.
    private BigInteger nonce; // Proof-of-work nonce.
    private int difficulty; // Difficulty level for the proof-of-work algorithm.

    /**
     * Constructor for creating a new block.
     *
     * @param index      The index of this block in the chain.
     * @param timestamp  The time when the block was created.
     * @param data       The data (transaction details) to be included in this block.
     * @param difficulty The difficulty level for mining this block.
     */
    public Block(int index, Timestamp timestamp, String data, int difficulty) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.difficulty = difficulty;
        this.nonce = BigInteger.ZERO;
    }

    /**
     * Calculates the hash of the block using SHA-256 hashing algorithm.
     *
     * @return A hexadecimal string representing the block's hash.
     */
    public String calculateHash() {
        // Concatenate block attributes to form the input for hashing
        String input = index + timestamp.toString() + data + previousHash + nonce + difficulty;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte elem : hash) {
                String hex = Integer.toHexString(0xff & elem);
                if (hex.length() == 1) hexString.append('0'); // Ensure leading zero for single digit hex values
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Performs proof-of-work to find a valid hash for the block.
     * Adjusts the nonce until the hash satisfies the required difficulty level.
     */
    public void proofOfWork() {
        String target = new String(new char[difficulty]).replace('\0', '0'); // Target string with required number of leading zeroes
        while (!calculateHash().substring(0, difficulty).equals(target)) { // Check if the hash satisfies the difficulty condition
            nonce = nonce.add(BigInteger.ONE); // Increment the nonce and recalculate the hash
        }
        calculateHash(); // Recalculate hash after proof-of-work
    }

    // Getters and Setters

    /**
     * Gets the index of the block within the blockchain.
     *
     * @return The index of the block.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the timestamp of when the block was created.
     *
     * @return The timestamp of the block.
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the data contained in the block (transaction details).
     *
     * @return The data of the block.
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the data contained in the block (transaction details).
     *
     * @param data The data to be set.
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Gets the hash of the previous block in the chain.
     *
     * @return The previous hash of the block.
     */
    public String getPreviousHash() {
        return previousHash;
    }

    /**
     * Sets the hash of the previous block in the chain.
     *
     * @param previousHash The previous hash to be set.
     */
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    /**
     * Gets the proof-of-work nonce of the block.
     *
     * @return The nonce of the block.
     */
    public BigInteger getNonce() {
        return nonce;
    }

    /**
     * Gets the difficulty level of mining the block.
     *
     * @return The difficulty level of the block.
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level of mining the block.
     *
     * @param difficulty The difficulty level to be set.
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Returns a JSON representation of the block.
     *
     * @return JSON string representing the block.
     */
    @Override
    public String toString() {
        return String.format("{\"index\": %d, \"timestamp\": \"%s\", \"data\": \"%s\", \"previousHash\": \"%s\", \"nonce\": \"%s\", \"difficulty\": %d}",
                index, timestamp, data, previousHash, nonce, difficulty);
    }
}
