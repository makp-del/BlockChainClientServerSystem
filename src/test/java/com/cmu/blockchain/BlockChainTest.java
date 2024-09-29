package com.cmu.blockchain;

import com.cmu.blockchain.core.BlockChain;
import com.cmu.blockchain.model.Block;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class BlockChainTest {

    private BlockChain blockchain;

    @BeforeEach
    void setUp() {
        blockchain = new BlockChain();
    }

    @Test
    void testAddGenesisBlock() {
        assertEquals(1, blockchain.getChainSize(), "Blockchain should start with 1 block (genesis block).");
        assertNotNull(blockchain.getBlock(0), "Genesis block should not be null.");
    }

    @Test
    void testAddBlock() {
        int initialSize = blockchain.getChainSize();
        Block newBlock = new Block(initialSize, new Timestamp(System.currentTimeMillis()), "New transaction", 2);
        blockchain.addBlock(newBlock);

        assertEquals(initialSize + 1, blockchain.getChainSize(), "Blockchain size should increase after adding a new block.");
        assertEquals(newBlock, blockchain.getLatestBlock(), "Latest block should be the one just added.");
    }

    @Test
    void testChainValidityAfterAddingBlocks() {
        // Add blocks to blockchain
        for (int i = 0; i < 3; i++) {
            Block newBlock = new Block(blockchain.getChainSize(), new Timestamp(System.currentTimeMillis()), "Transaction " + i, 2);
            blockchain.addBlock(newBlock);
        }

        assertTrue(blockchain.isChainValid(), "Blockchain should be valid after adding correctly mined blocks.");
    }

    @Test
    void testCorruptChain() {
        // Add a valid block
        Block newBlock = new Block(blockchain.getChainSize(), new Timestamp(System.currentTimeMillis()), "Valid transaction", 2);
        blockchain.addBlock(newBlock);

        // Corrupt the block by modifying data
        blockchain.getBlock(1).setData("Corrupted transaction");

        assertFalse(blockchain.isChainValid(), "Blockchain should be invalid after corruption.");
    }

    @Test
    void testRepairChain() {
        // Add a valid block
        Block newBlock = new Block(blockchain.getChainSize(), new Timestamp(System.currentTimeMillis()), "Valid transaction", 2);
        blockchain.addBlock(newBlock);

        // Corrupt the block by modifying data
        blockchain.getBlock(1).setData("Corrupted transaction");

        assertFalse(blockchain.isChainValid(), "Blockchain should be invalid before repair.");

        // Repair the chain
        blockchain.repairChain();

        assertTrue(blockchain.isChainValid(), "Blockchain should be valid after repair.");
    }

    @Test
    void testChainSize() {
        assertEquals(1, blockchain.getChainSize(), "Blockchain should start with 1 block (genesis block).");

        Block newBlock = new Block(blockchain.getChainSize(), new Timestamp(System.currentTimeMillis()), "Transaction", 2);
        blockchain.addBlock(newBlock);

        assertEquals(2, blockchain.getChainSize(), "Blockchain size should increase after adding a block.");
    }

    @Test
    void testAddBlockWithHighDifficulty() {
        Block highDifficultyBlock = new Block(blockchain.getChainSize(), new Timestamp(System.currentTimeMillis()), "High difficulty transaction", 5);
        blockchain.addBlock(highDifficultyBlock);

        assertTrue(blockchain.isChainValid(), "Blockchain should remain valid even with high difficulty block.");
    }

    @Test
    void testTotalDifficulty() {
        int initialDifficulty = blockchain.getTotalDifficulty();

        // Add blocks with various difficulties
        blockchain.addBlock(new Block(blockchain.getChainSize(), new Timestamp(System.currentTimeMillis()), "Transaction 1", 2));
        blockchain.addBlock(new Block(blockchain.getChainSize(), new Timestamp(System.currentTimeMillis()), "Transaction 2", 3));
        blockchain.addBlock(new Block(blockchain.getChainSize(), new Timestamp(System.currentTimeMillis()), "Transaction 3", 4));

        assertEquals(initialDifficulty + 9, blockchain.getTotalDifficulty(), "Total difficulty should match the sum of all blocks' difficulty.");
    }

    @Test
    void testGetLatestBlock() {
        Block newBlock = new Block(blockchain.getChainSize(), new Timestamp(System.currentTimeMillis()), "New transaction", 2);
        blockchain.addBlock(newBlock);

        assertEquals(newBlock, blockchain.getLatestBlock(), "The latest block should be the most recently added block.");
    }

    @Test
    void testChainIntegrityWithMultipleBlocks() {
        // Add multiple blocks
        for (int i = 0; i < 5; i++) {
            Block newBlock = new Block(blockchain.getChainSize(), new Timestamp(System.currentTimeMillis()), "Transaction " + i, 2);
            blockchain.addBlock(newBlock);
        }

        assertTrue(blockchain.isChainValid(), "Blockchain should maintain integrity with multiple blocks.");
    }
}