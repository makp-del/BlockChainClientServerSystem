//Andrew ID: mpanindr
//Name: Manjunath K P

package com.cmu.blockchain.network;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import com.cmu.blockchain.message.RequestMessage;
import com.cmu.blockchain.message.ResponseMessage;
import com.cmu.blockchain.util.LoggerUtil;
import com.google.gson.Gson; // Import Gson for JSON handling.
import org.slf4j.Logger;

/**
 * This class implements a TCP client that interacts with a blockchain server.
 * It allows the user to perform various operations related to a blockchain,
 * such as adding transactions, verifying integrity, and viewing blockchain status.
 */
public class ClientTCP {
    
    private static final Logger LOGGER = LoggerUtil.getLogger(ClientTCP.class);

    // Gson instance for handling JSON serialization and deserialization.
    private static final Gson gson = new Gson();
    // The port number of the server to connect to.
    private static final int serverPort = 7777;
    // The hostname or IP address of the server.
    private static final String hostname = "localhost"; // Can be modified to connect to different servers.

    public static void main(String[] args) {
        try (
                // Establish a socket connection to the server.
                Socket clientSocket = new Socket(hostname, serverPort);
                // Create a PrintWriter to send data to the server.
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                // Create a BufferedReader to receive data from the server.
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // Scanner to read user input from the console.
                Scanner scanner = new Scanner(System.in)
        ) {
            handleClient(out, in, scanner); // Refactored handling method
        } catch (Exception e) {
            LOGGER.error("Error: " + e.getMessage());
        }
    }

/**
 * This method handles client interactions with the server.
 * It processes user inputs, sends requests to the server, and processes the server's responses.
 *
 * @param out     PrintWriter to send data to the server.
 * @param in      BufferedReader to receive data from the server.
 * @param scanner Scanner to read user input.
 */
public static void handleClient(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException {
    while (true) {
        displayMenu(); // Display the menu options to the user.
        String choice = scanner.nextLine(); // Read the user's choice.
        if ("6".equals(choice)) {
            LOGGER.info("Exiting...");
            break;
        }
        // Handle the user input based on the selected choice.
        handleUserInput(choice, out, in, scanner);
    }
}

    /**
     * Displays the menu options to the user.
     */
    private static void displayMenu() {
        LOGGER.info("""

                Block Chain Menu
                0. View basic blockchain status.
                1. Add a transaction to the blockchain.
                2. Verify the blockchain.
                3. View the blockchain.
                4. Corrupt the chain.
                5. Hide the corruption by repairing the chain.
                6. Exit.""");
        System.out.print("Enter your choice: ");
    }

    /**
     * Handles the user input, creating and sending a request to the server based on the input,
     * and then displays the server's response.
     *
     * @param userInput The user's selected menu option.
     * @param out       PrintWriter to send data to the server.
     * @param in        BufferedReader to receive data from the server.
     * @param scanner   Scanner to read additional input from the user, if necessary.
     * @throws IOException If an I/O error occurs.
     */
    private static void handleUserInput(String userInput, PrintWriter out, BufferedReader in, Scanner scanner) throws IOException {
        RequestMessage request = createRequest(userInput, scanner); // Create a request based on the user input.
        if (request != null) {
            String jsonRequest = gson.toJson(request); // Convert the request to JSON.
            out.println(jsonRequest); // Send JSON request to the server.

            String jsonResponse = in.readLine(); // Wait for and read the response from the server.
            ResponseMessage response = gson.fromJson(jsonResponse, ResponseMessage.class); // Deserialize JSON response.
            displayResponse(response); // Display the server's response.
        } else {
            LOGGER.info("Invalid option selected."); // Invalid menu option.
        }
    }

    /**
     * Creates a request message from user input. The method prompts the user for additional
     * details based on the selected action, such as transaction details or the difficulty level
     * for adding a transaction, or the ID of a block to corrupt.
     *
     * @param userInput The user's choice indicating the action to be taken.
     * @param scanner A Scanner instance for reading user input from the console.
     * @return A RequestMessage object encapsulating the action, data, and difficulty (if applicable).
     *         Returns null if the user chooses to exit.
     */
    private static RequestMessage createRequest(String userInput, Scanner scanner) {
        String action = null; // The action to be performed, derived from the user's choice.
        String data = ""; // Additional data for the request, such as transaction details.
        int difficulty = 0; // The mining difficulty level for adding a transaction.

        // Determine the action based on the user's choice.
        switch (userInput) {
            case "0":
                action = "viewBlockchainStatus"; // No additional data needed.
                break;
            case "1":
                // Add a transaction: prompt for transaction details and difficulty level.
                System.out.print("Enter difficulty level (>1): ");
                difficulty = scanner.nextInt();
                System.out.print("Enter transaction details: ");
                data = scanner.nextLine();
                scanner.nextLine(); // Consume the trailing newline.
                action = "addTransaction";
                break;
            case "2":
                action = "verifyBlockchain"; // Verify the integrity of the blockchain.
                break;
            case "3":
                action = "viewBlockchain"; // Request the entire blockchain data.
                break;
            case "4":
                // Corrupt a block: prompt for the block ID and new data.
                System.out.print("Enter block ID to corrupt: ");
                int blockID = scanner.nextInt();
                scanner.nextLine(); // Consume newline.
                System.out.print("Enter new data for block: ");
                data = blockID + ":" + scanner.nextLine(); // Format: "blockID:newData"
                action = "corruptBlockchain";
                break;
            case "5":
                action = "repairBlockchain"; // Attempt to repair the corrupted blockchain.
                break;
            case "6":
                return null; // User chooses to exit.
        }
        // Return a new RequestMessage object encapsulating the specified action and data.
        return new RequestMessage(action, data, difficulty);
    }


    /**
     * Displays the server's response based on the requested action. It interprets the JSON response
     * from the server and presents the results or status information in a user-friendly manner. This method
     * handles different responses, including viewing blockchain status, adding transactions, verifying the
     * blockchain, and actions related to corrupting and repairing the blockchain.
     *
     * @param response The response message received from the server, parsed into a ResponseMessage object.
     */
    private static void displayResponse(ResponseMessage response) {
        // Print the general message
        LOGGER.info(response.getMessage());

        if (!response.isSuccess()) {
            LOGGER.info("Operation failed: " + response.getMessage());
            return;
        }

        // Assuming that "action" field is set by the server based on the requested operation
        switch (response.getAction()) {
            case "viewBlockchainStatus":
                // Assuming server sends detailed blockchain status in the message or as part of blockchainData
                LOGGER.info("Current size of chain: " + response.getChainSize());
                LOGGER.info("Difficulty of most recent block: " + response.getLatestBlockDifficulty());
                LOGGER.info("Total difficulty for all blocks: " + response.getTotalDifficulty());
                LOGGER.info("Approximate hashes per second on this machine: " + response.getHashesPerSecond());
                LOGGER.info("Expected total hashes required for the whole chain: " + response.getTotalExpectedHashes());
                LOGGER.info("Nonce for most recent block: " + response.getLatestBlockNonce());
                LOGGER.info("Chain hash: " + response.getChainHash());
                break;
            case "addTransaction":
                // Server might include new block details or confirmation message
                break;
            case "verifyBlockchain":
                // Server might include blockchain validity status
                break;
            case "viewBlockchain":
                // Server sends the entire blockchain. You could print it directly or format it as needed.
                LOGGER.info("Blockchain Data:\n" + response.getBlockchainData());
                break;
            case "corruptBlockchain":
                // Server might respond with details about the corruption if it were supported
                break;
            case "repairBlockchain":
                // Server might include details about the repair process
                break;
        }
    }

}
