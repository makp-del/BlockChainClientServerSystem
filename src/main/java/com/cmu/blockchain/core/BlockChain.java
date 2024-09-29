//Andrew ID: mpanindr
//Name: Manjunath K P

package com.cmu.blockchain.core;

import com.cmu.blockchain.model.Block;
import com.cmu.blockchain.util.LoggerUtil;
import org.slf4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Represents a simple blockchain.
 * A blockchain is a chain of blocks that contains data and is secured by cryptographic hashes.
 * The blockchain maintains the integrity of the data by linking blocks using hashes.
 * It also includes a proof-of-work mechanism to ensure the security and immutability of the blockchain.
 * The blockchain can add new blocks, validate the chain, and repair the chain if needed.
 * The blockchain can also calculate the total difficulty of the chain and the expected number of hashes required.
 * The blockchain can be serialized to a JSON format for communication and storage.
 * The blockchain includes a genesis block as the first block in the chain.
 * The blockchain also computes the number of hashes the system can try per second to demonstrate proof-of-work difficulty.
 * The blockchain is part of a decentralized system that ensures data integrity and security.
 */
public class BlockChain {

    private final static Logger LOGGER = LoggerUtil.getLogger(BlockChain.class);
    private final ArrayList<Block> chain; // Holds the blocks in the chain.
    private String chainHash; // Hash of the most recently added block.

    private int hashesPerSecond;

    /**
     * Constructor for creating a new blockchain.
     */
    public BlockChain() {
        this.chain = new ArrayList<>();
        this.chainHash = "";
        this.hashesPerSecond = 0;
        computeHashesPerSecond(); // Compute and set the number of hashes per second the system can perform
        addGenesisBlock(); // Add the initial block (genesis block) to the blockchain
    }

    /**
     * Adds the genesis block to the blockchain.
     */
    private void addGenesisBlock() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Block genesisBlock = new Block(0, timestamp, "Genesis", 2); // Create the genesis block with initial data and difficulty level
        genesisBlock.setPreviousHash("0"); // Set previous hash for the genesis block
        genesisBlock.setDifficulty(2); // Set the initial difficulty level
        genesisBlock.proofOfWork(); // Compute the proof of work for the genesis block
        chain.add(genesisBlock); // Add the genesis block to the chain
        chainHash = genesisBlock.calculateHash(); // Update the chain hash with the hash of the genesis block
    }

    /**
     * Adds a new block to the blockchain.
     *
     * @param newBlock The new block to be added.
     */
    public void addBlock(Block newBlock) {
        if (chain.isEmpty()) {
            LOGGER.info("The blockchain is empty. Add a genesis block first.");
            return;
        }
        newBlock.setPreviousHash(chainHash); // Set the previous hash to the most recent chain hash
        newBlock.proofOfWork(); // Compute the proof of work for the new block
        chain.add(newBlock); // Add the new block to the blockchain
        chainHash = newBlock.calculateHash(); // Update the chain hash
    }

    /**
     * Validates the integrity of the blockchain.
     *
     * @return True if the blockchain is valid, otherwise false.
     */
    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            // Check current block's previous hash matches the previous block's hash
            if (!currentBlock.getPreviousHash().equals(previousBlock.calculateHash())) {
                LOGGER.info("Previous Hashes not equal");
                return false;
            }

            // Check current block's hash is valid with its difficulty
            String hashTarget = new String(new char[currentBlock.getDifficulty()]).replace('\0', '0');
            if (!currentBlock.calculateHash().substring(0, currentBlock.getDifficulty()).equals(hashTarget)) {
                LOGGER.info("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }

    /**
     * Repairs the blockchain by recomputing the hashes.
     */
    public void repairChain() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);
            currentBlock.setPreviousHash(previousBlock.calculateHash());
            currentBlock.proofOfWork(); // Recompute the proof of work for the current block
        }
        chainHash = chain.getLast().calculateHash(); // Update the chain hash
    }

    /**
     * Returns the blockchain in JSON format.
     *
     * @return A JSON representation of the blockchain.
     */
    @Override
    public String toString() {
        StringBuilder json = new StringBuilder();
        json.append("{\n\"ds_chain\": [");
        for (int i = 0; i < chain.size(); i++) {
            Block block = chain.get(i);
            json.append(String.format("\n {\"index\": %d, \"timestamp\": \"%s\", \"data\": \"%s\", \"previousHash\": \"%s\", \"nonce\": \"%s\", \"difficulty\": %d}",
                    block.getIndex(), block.getTimestamp().toString(), block.getData(), block.getPreviousHash(), block.getNonce().toString(), block.getDifficulty()));
            if (i < chain.size() - 1) {
                json.append(",");
            }
        }
        json.append("\n ],");
        json.append(String.format("\n \"chainHash\":\"%s\"", chainHash));
        json.append("\n}");
        return json.toString();
    }

    /**
     * Computes the number of hashes per second the system can perform.
     */
    private void computeHashesPerSecond() {
        String textToHash = "00000000"; // Text to be hashed
        long startTime = System.nanoTime(); // Start time for measuring duration
        int numberOfHashes = 2000000; // Number of hashes to compute

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            for (int i = 0; i < numberOfHashes; i++) {
                digest.digest(textToHash.getBytes());
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.info("Error while computing hash: " + e.getMessage());
            return;
        }

        long endTime = System.nanoTime(); // End time for measuring duration
        long duration = endTime - startTime; // Duration of hashing process
        double seconds = duration / 1_000_000_000.0; // Convert duration to seconds
        this.hashesPerSecond = (int) (numberOfHashes / seconds); // Compute hashes per second
        LOGGER.info("Hashes per second: " + this.hashesPerSecond); // Output the computed hashes per second
    }

    // Getters

    /**
     * Gets the total difficulty of the blockchain.
     *
     * @return The total difficulty of the blockchain.
     */
    public int getTotalDifficulty() {
        return chain.stream().mapToInt(Block::getDifficulty).sum();
    }

    /**
     * Calculates the total expected hashes required for the whole blockchain.
     * This method estimates the total hashes needed based on the difficulty of each block.
     *
     * @return The total expected hashes for the blockchain.
     */
    public double getTotalExpectedHashes() {
        // Simplified calculation
        return chain.stream().mapToDouble(block -> Math.pow(2, block.getDifficulty())).sum();
    }

    /**
     * Retrieves the current rate of hashes computed per second.
     *
     * @return The number of hashes computed per second.
     */
    public int getHashesPerSecond() {
        return hashesPerSecond;
    }

    /**
     * Retrieves the block at the specified index in the blockchain.
     *
     * @param i The index of the block to retrieve.
     * @return The block at the specified index.
     */
    public Block getBlock(int i) {
        return chain.get(i);
    }

    /**
     * Retrieves the hash of the most recently added block in the blockchain.
     *
     * @return The hash of the most recent block.
     */
    public String getChainHash() {
        return chainHash;
    }

    /**
     * Retrieves the most recent block added to the blockchain.
     *
     * @return The most recent block.
     */
    public Block getLatestBlock() {
        return chain.getLast();
    }

    /**
     * Retrieves the current size of the blockchain, indicating the number of blocks it contains.
     *
     * @return The size of the blockchain.
     */
    public int getChainSize() {
        return chain.size();
    }


}
