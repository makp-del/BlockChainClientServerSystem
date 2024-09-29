package com.cmu.network;

import com.cmu.blockchain.network.ClientTCP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientTCPTest {

    private PrintWriter mockOut;
    private BufferedReader mockIn;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @BeforeEach
    void setUp() throws Exception {
        // Mock output stream to simulate communication with server
        byteArrayOutputStream = new ByteArrayOutputStream();
        mockOut = new PrintWriter(byteArrayOutputStream, true);

        // Mock BufferedReader to simulate server's responses
        mockIn = mock(BufferedReader.class);
    }

    @Test
    void testViewBlockchainStatus() throws Exception {
        // Setup mock server response
        String mockResponse = "{\"success\":true, \"message\":\"Blockchain status viewed successfully.\", " +
                "\"action\":\"viewBlockchainStatus\", \"chainSize\":5, \"latestBlockDifficulty\":2, " +
                "\"totalDifficulty\":10, \"hashesPerSecond\":1000, \"totalExpectedHashes\":1000000.0, " +
                "\"latestBlockNonce\":\"1234\", \"chainHash\":\"abcd\"}";
        when(mockIn.readLine()).thenReturn(mockResponse);

        // Simulate user input for viewing blockchain status (option 0) and exiting (option 6)
        String simulatedInput = "0\n6\n";
        byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(byteArrayInputStream);

        // Redirect system output to the byteArrayOutputStream to capture it
        System.setOut(new PrintStream(byteArrayOutputStream));

        // Run the client interaction with the mocked objects
        ClientTCP.handleClient(mockOut, mockIn, scanner);

        // Verify that the blockchain status message was logged to the output
        String output = byteArrayOutputStream.toString();
        assertTrue(output.contains("Blockchain status viewed successfully."));
        assertTrue(output.contains("Current size of chain: 5"));
        assertTrue(output.contains("Difficulty of most recent block: 2"));
        assertTrue(output.contains("Approximate hashes per second on this machine: 1000"));
        assertTrue(output.contains("Chain hash: abcd"));
    }

    @Test
    void testAddTransaction() throws Exception {
        // Setup mock server response
        String mockResponse = "{\"success\":true, \"message\":\"Transaction added successfully.\", " +
                "\"action\":\"addTransaction\"}";
        when(mockIn.readLine()).thenReturn(mockResponse);

        // Simulate user input for adding a transaction and exiting
        String simulatedInput = "1\n1000\nTransaction 1\n6\n";
        byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(byteArrayInputStream);

        // Redirect system output to the byteArrayOutputStream to capture it
        System.setOut(new PrintStream(byteArrayOutputStream));

        // Run the client interaction with the mocked objects
        ClientTCP.handleClient(mockOut, mockIn, scanner);

        // Verify that the transaction was added successfully
        String output = byteArrayOutputStream.toString();
        assertTrue(output.contains("Transaction added successfully."));
    }

    @Test
    void testVerifyBlockchain() throws Exception {
        // Setup mock server response for blockchain verification
        String mockResponse = "{\"success\":true, \"message\":\"Blockchain is valid.\", " +
                "\"action\":\"verifyBlockchain\"}";
        when(mockIn.readLine()).thenReturn(mockResponse);

        // Simulate user input for verifying the blockchain and exiting
        String simulatedInput = "2\n6\n";
        byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(byteArrayInputStream);

        // Redirect system output to the byteArrayOutputStream to capture it
        System.setOut(new PrintStream(byteArrayOutputStream));

        // Run the client interaction with the mocked objects
        ClientTCP.handleClient(mockOut, mockIn, scanner);

        // Verify that the blockchain validity message was logged to the output
        String output = byteArrayOutputStream.toString();
        assertTrue(output.contains("Blockchain is valid."));
    }

    @Test
    void testViewBlockchain() throws Exception {
        // Setup mock server response for viewing the full blockchain
        String mockResponse = "{\"success\":true, \"message\":\"Viewing the Blockchain.\", " +
                "\"action\":\"viewBlockchain\", \"blockchainData\":\"[block1, block2]\"}";
        when(mockIn.readLine()).thenReturn(mockResponse);

        // Simulate user input for viewing the blockchain and exiting
        String simulatedInput = "3\n6\n";
        byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(byteArrayInputStream);

        // Redirect system output to the byteArrayOutputStream to capture it
        System.setOut(new PrintStream(byteArrayOutputStream));

        // Run the client interaction with the mocked objects
        ClientTCP.handleClient(mockOut, mockIn, scanner);

        // Verify that the blockchain data was printed
        String output = byteArrayOutputStream.toString();
        assertTrue(output.contains("Viewing the Blockchain"));
        assertTrue(output.contains("block1, block2"));
    }

    @Test
    void testCorruptBlock() throws Exception {
        // Setup mock server response for corrupting a block
        String mockResponse = "{\"success\":true, \"message\":\"Block 2 corrupted successfully.\", " +
                "\"action\":\"corruptBlockchain\"}";
        when(mockIn.readLine()).thenReturn(mockResponse);

        // Simulate user input for corrupting block ID 2 and exiting
        String simulatedInput = "4\n2\nNew corrupted data\n6\n";
        byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(byteArrayInputStream);

        // Redirect system output to the byteArrayOutputStream to capture it
        System.setOut(new PrintStream(byteArrayOutputStream));

        // Run the client interaction with the mocked objects
        ClientTCP.handleClient(mockOut, mockIn, scanner);

        // Verify that the block corruption message was printed
        String output = byteArrayOutputStream.toString();
        assertTrue(output.contains("Block 2 corrupted successfully."));
    }

    @Test
    void testRepairBlockchain() throws Exception {
        // Setup mock server response for repairing the blockchain
        String mockResponse = "{\"success\":true, \"message\":\"Blockchain repaired successfully.\", " +
                "\"action\":\"repairBlockchain\"}";
        when(mockIn.readLine()).thenReturn(mockResponse);

        // Simulate user input for repairing the blockchain and exiting
        String simulatedInput = "5\n6\n";
        byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(byteArrayInputStream);

        // Redirect system output to the byteArrayOutputStream to capture it
        System.setOut(new PrintStream(byteArrayOutputStream));

        // Run the client interaction with the mocked objects
        ClientTCP.handleClient(mockOut, mockIn, scanner);

        // Verify that the blockchain repair message was printed
        String output = byteArrayOutputStream.toString();
        assertTrue(output.contains("Blockchain repaired successfully."));
    }
}