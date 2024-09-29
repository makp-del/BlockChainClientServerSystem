//Andrew ID: mpanindr
//Name: Manjunath K P

package com.cmu.blockchain.message;

/**
 * ResponseMessage class represents a response message to be sent back to the client.
 * It encapsulates information such as success status, message, action, and blockchain data.
 */
public class ResponseMessage {

    // Fields
    private boolean success;            // Indicates if the operation was successful
    private String message;             // Additional message related to the response
    private String action;              // Specifies the action performed by the server
    private String blockchainData;      // Data related to the blockchain

    // Additional fields for detailed blockchain status
    private int chainSize;              // Number of blocks in the blockchain
    private int latestBlockDifficulty;  // Difficulty level of the latest block
    private int totalDifficulty;        // Total difficulty of all blocks
    private long hashesPerSecond;       // Number of hashes computed per second
    private double totalExpectedHashes; // Total expected hashes required for the whole chain
    private String latestBlockNonce;    // Nonce of the latest block
    private String chainHash;           // Hash of the entire blockchain

    // Constructors

    /**
     * Constructs a ResponseMessage object with specified success status, message, action, and blockchain data.
     *
     * @param success        Indicates if the operation was successful
     * @param message        Additional message related to the response
     * @param action         Specifies the action performed by the server
     * @param blockchainData Data related to the blockchain
     */
    public ResponseMessage(boolean success, String message, String action, String blockchainData) {
        this.success = success;
        this.message = message;
        this.action = action;
        this.blockchainData = blockchainData;
    }

    // Getters and Setters

    /**
     * Gets the success status of the response.
     *
     * @return true if the operation was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success status of the response.
     *
     * @param success true if the operation was successful, false otherwise
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Gets the additional message related to the response.
     *
     * @return Additional message related to the response
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the additional message related to the response.
     *
     * @param message Additional message related to the response
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the action performed by the server.
     *
     * @return Action performed by the server
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the action performed by the server.
     *
     * @param action Action performed by the server
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Gets the data related to the blockchain.
     *
     * @return Data related to the blockchain
     */
    public String getBlockchainData() {
        return blockchainData;
    }

    /**
     * Sets the data related to the blockchain.
     *
     * @param blockchainData Data related to the blockchain
     */
    public void setBlockchainData(String blockchainData) {
        this.blockchainData = blockchainData;
    }

    /**
     * Gets the number of blocks in the blockchain.
     *
     * @return Number of blocks in the blockchain
     */
    public int getChainSize() {
        return chainSize;
    }

    /**
     * Sets the number of blocks in the blockchain.
     *
     * @param chainSize Number of blocks in the blockchain
     */
    public void setChainSize(int chainSize) {
        this.chainSize = chainSize;
    }

    /**
     * Gets the difficulty level of the latest block.
     *
     * @return Difficulty level of the latest block
     */
    public int getLatestBlockDifficulty() {
        return latestBlockDifficulty;
    }

    /**
     * Sets the difficulty level of the latest block.
     *
     * @param latestBlockDifficulty Difficulty level of the latest block
     */
    public void setLatestBlockDifficulty(int latestBlockDifficulty) {
        this.latestBlockDifficulty = latestBlockDifficulty;
    }

    /**
     * Gets the total difficulty of all blocks.
     *
     * @return Total difficulty of all blocks
     */
    public int getTotalDifficulty() {
        return totalDifficulty;
    }

    /**
     * Sets the total difficulty of all blocks.
     *
     * @param totalDifficulty Total difficulty of all blocks
     */
    public void setTotalDifficulty(int totalDifficulty) {
        this.totalDifficulty = totalDifficulty;
    }

    /**
     * Gets the number of hashes computed per second.
     *
     * @return Number of hashes computed per second
     */
    public long getHashesPerSecond() {
        return hashesPerSecond;
    }

    /**
     * Sets the number of hashes computed per second.
     *
     * @param hashesPerSecond Number of hashes computed per second
     */
    public void setHashesPerSecond(long hashesPerSecond) {
        this.hashesPerSecond = hashesPerSecond;
    }

    /**
     * Gets the total expected hashes required for the whole chain.
     *
     * @return Total expected hashes required for the whole chain
     */
    public double getTotalExpectedHashes() {
        return totalExpectedHashes;
    }

    /**
     * Sets the total expected hashes required for the whole chain.
     *
     * @param totalExpectedHashes Total expected hashes required for the whole chain
     */
    public void setTotalExpectedHashes(double totalExpectedHashes) {
        this.totalExpectedHashes = totalExpectedHashes;
    }

    /**
     * Gets the nonce of the latest block.
     *
     * @return Nonce of the latest block
     */
    public String getLatestBlockNonce() {
        return latestBlockNonce;
    }

    /**
     * Sets the nonce of the latest block.
     *
     * @param latestBlockNonce Nonce of the latest block
     */
    public void setLatestBlockNonce(String latestBlockNonce) {
        this.latestBlockNonce = latestBlockNonce;
    }

    /**
     * Gets the hash of the entire blockchain.
     *
     * @return Hash of the entire blockchain
     */
    public String getChainHash() {
        return chainHash;
    }

    /**
     * Sets the hash of the entire blockchain.
     *
     * @param chainHash Hash of the entire blockchain
     */
    public void setChainHash(String chainHash) {
        this.chainHash = chainHash;
    }
}
