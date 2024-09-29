//Andrew ID: mpanindr
//Name: Manjunath K P

package com.cmu.blockchain.network;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;

import com.cmu.blockchain.message.RequestMessage;
import com.cmu.blockchain.message.ResponseMessage;
import com.cmu.blockchain.core.BlockChain;
import com.cmu.blockchain.model.Block;
import com.cmu.blockchain.util.LoggerUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;

/**
 * ServerTCP class represents the server side of a TCP-based blockchain system.
 * It listens for incoming client connections, processes requests, and sends responses.
 */
public class ServerTCP {
    
    private static final Logger LOGGER = LoggerUtil.getLogger(ServerTCP.class);

    // Gson instance for JSON handling
    private static final Gson gson = new Gson();

    // Blockchain instance to manage the blockchain data
    private static final BlockChain blockchain = new BlockChain();

    // Placeholder for the chain hash (not utilized in this example)
    private static final String chainHash = "";

    /**
     * Main method for starting the blockchain server.
     *
     * @param args Command-line arguments (not used in this example).
     */
    public static void main(String[] args) {
        // Port number on which the server listens for incoming connections
        int serverPort = 7777;

        try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
            LOGGER.info("Blockchain server running on port " + serverPort);

            // Server runs indefinitely, continuously accepting client connections
            while (true) {
                try (Socket clientSocket = listenSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true)) {

                    LOGGER.info("New client connected.");

                    // Process incoming requests from the client
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        String response = processRequest(inputLine);
                        out.println(response); // Send the response back to the client
                    }
                } catch (IOException e) {
                    LOGGER.error("Exception handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            LOGGER.error("Server Exception: " + e.getMessage());
        }
    }

    /**
     * Processes a request received from the client and generates an appropriate response.
     *
     * @param inputLine The JSON-formatted request received from the client.
     * @return A JSON-formatted response to be sent back to the client.
     */
    private static String processRequest(String inputLine) {
        // Log the received request
        LOGGER.info("Received Request: " + inputLine);

        try {
            // Parse the incoming JSON request into a RequestMessage object
            RequestMessage request = gson.fromJson(inputLine, RequestMessage.class);
            String action = request.action();

            // Initialize a default response with failure status and an error message
            ResponseMessage response = new ResponseMessage(false, "Invalid action", action, null);

            // Handle the request based on the specified action
            switch (action) {
                case "viewBlockchainStatus":
                    // Get the blockchain status and set it in the response
                    String blockchainStatus = blockchain.toString();
                    response.setSuccess(true);
                    response.setMessage("Blockchain status viewed successfully.");
                    response.setAction(action);
                    response.setBlockchainData(blockchainStatus);
                    // Set additional blockchain details
                    setBlockchainDetails(response);
                    break;
                case "addTransaction":
                    // Add a new transaction to the blockchain
                    blockchain.addBlock(new Block(blockchain.getChainSize(), new Timestamp(System.currentTimeMillis()), request.data(), request.difficulty()));
                    response.setSuccess(true);
                    response.setMessage("Transaction added successfully.");
                    response.setAction(action);
                    // Update blockchain status after adding the block
                    setBlockchainDetails(response);
                    break;
                case "verifyBlockchain":
                    // Verify the integrity of the blockchain
                    boolean isValid = blockchain.isChainValid();
                    response.setSuccess(isValid);
                    response.setMessage(isValid ? "Blockchain is valid." : "Blockchain validation failed.");
                    response.setAction(action);
                    // Provide blockchain details for verification context
                    setBlockchainDetails(response);
                    break;
                case "viewBlockchain":
                    // View the entire blockchain
                    response.setSuccess(true);
                    response.setMessage("Viewing the Blockchain");
                    response.setAction(action);
                    response.setBlockchainData(blockchain.toString());
                    break;
                case "corruptBlockchain":
                    // Corrupt a specific block in the blockchain
                    try {
                        // Extract block ID and new data from the request
                        String[] parts = request.data().split(":", 2);
                        int blockId = Integer.parseInt(parts[0]);
                        String newData = parts[1];

                        // Check if the block ID is valid
                        if (blockId >= 0 && blockId < blockchain.getChainSize()) {
                            // Update the block data with the new data
                            blockchain.getBlock(blockId).setData(newData);
                            response.setSuccess(true);
                            response.setMessage("Block " + blockId + " corrupted successfully with new data: " + newData);
                        } else {
                            // Invalid block ID provided
                            response.setSuccess(false);
                            response.setMessage("Invalid block ID provided.");
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        // Error processing request data for block corruption
                        response.setSuccess(false);
                        response.setMessage("Error processing request data for block corruption: " + e.getMessage());
                    }
                    response.setAction(action);
                    break;
                case "repairBlockchain":
                    // Repair the blockchain by restoring it to a valid state
                    blockchain.repairChain();
                    response.setSuccess(true);
                    response.setMessage("Blockchain repaired successfully.");
                    response.setAction(action);
                    // Update blockchain status after repair
                    setBlockchainDetails(response);
                    break;
                default:
                    // Unknown action requested
                    response.setSuccess(false);
                    response.setMessage("Unknown action requested.");
                    response.setAction(action);
            }

            // Convert the response to JSON format
            String jsonResponse = gson.toJson(response);
            // Log the generated response
            LOGGER.info("Generated Response: " + jsonResponse);
            LOGGER.info("Number of Blocks on Chain == " + blockchain.getChainSize() + ".");
            return jsonResponse;
        } catch (Exception e) {
            // Error occurred while processing the request
            String errorResponse = gson.toJson(new ResponseMessage(false, "Error processing request: " + e.getMessage(), "", ""));
            // Log the error response
            LOGGER.error("Error Response: " + errorResponse);
            LOGGER.error("Number of Blocks on Chain == " + blockchain.getChainSize() + ".");
            return errorResponse;
        }
    }

    /**
     * Sets additional blockchain details in the response message.
     *
     * @param response The ResponseMessage object to which the blockchain details will be added.
     */
    private static void setBlockchainDetails(ResponseMessage response) {
        // Example method for setting blockchain details in the response
        response.setChainSize(blockchain.getChainSize());
        response.setLatestBlockDifficulty(blockchain.getLatestBlock().getDifficulty());
        response.setTotalDifficulty(blockchain.getTotalDifficulty());
        response.setHashesPerSecond(blockchain.getHashesPerSecond());
        response.setTotalExpectedHashes(blockchain.getTotalExpectedHashes());
        response.setLatestBlockNonce(blockchain.getLatestBlock().getNonce().toString());
        response.setChainHash(blockchain.getChainHash());
    }
}
