---

# Blockchain Client-Server System

This project implements a simple blockchain-based client-server system using TCP for communication. The system is designed to handle blockchain operations such as adding transactions, viewing the blockchain, verifying its integrity, and repairing corrupted blocks. The project follows a modular structure, adhering to industry standards for clean code, logging, and testing.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Directory Structure](#directory-structure)
- [Installation](#installation)
- [Usage](#usage)
- [Test Cases](#test-cases)
- [Contributing](#contributing)
- [License](#license)

## Overview

This blockchain system allows for the creation and maintenance of a blockchain, where each block contains transaction data, timestamp, and a nonce used for proof-of-work. The client can interact with the server to perform various operations like adding new transactions, viewing the current blockchain status, and verifying or repairing the chain.

### Key Components:

- **Client (ClientTCP)**: Interacts with the server by sending requests and displaying results. It supports operations like adding a transaction, corrupting a block, and verifying the chain.
- **Server (ServerTCP)**: Manages the blockchain on the server-side, processes client requests, and sends back appropriate responses.
- **Blockchain**: Contains classes like `Block` and `BlockChain` to represent and manage the chain, including mining blocks and ensuring chain integrity.

## Features

- **Blockchain Operations**: Add transactions, view status, verify the blockchain, corrupt and repair the chain.
- **Proof-of-Work**: Implements proof-of-work to ensure each block is mined properly.
- **Logging**: All operations are logged using SLF4J and Logback for improved observability.
- **Modular Design**: Each module (blockchain, network, utilities) is decoupled for better maintainability.
- **Testing**: Extensive JUnit and Mockito-based test cases cover various client-server interactions and edge cases.

## Directory Structure

```
.
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com
│   │   │   │   └── cmu
│   │   │   │       ├── blockchain
│   │   │   │       │   ├── Block.java
│   │   │   │       │   ├── BlockChain.java
│   │   │   │       │   └── ServerTCP.java
│   │   │   │       ├── message
│   │   │   │       │   ├── RequestMessage.java
│   │   │   │       │   └── ResponseMessage.java
│   │   │   │       ├── network
│   │   │   │       │   └── ClientTCP.java
│   │   │   │       └── util
│   │   │   │           └── LoggerUtil.java
│   │   ├── resources
│   │   │   └── logback.xml
│   ├── test
│   │   ├── java
│   │   │   ├── com
│   │   │   │   └── cmu
│   │   │   │       ├── blockchain
│   │   │   │       │   └── BlockChainTest.java
│   │   │   │       ├── network
│   │   │   │       │   └── ClientTCPTest.java


```

## Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/makp-del/CrimeDataAnalysis2DTrees.git
   cd CrimeDataAnalysis2DTrees
   ```

2. **Maven Setup**:
   Ensure that you have Maven installed. Run the following command to download dependencies and build the project:
   ```bash
   mvn clean install
   ```

## Usage

1. **Run the server**:
   Start the `ServerTCP` to listen for client requests.
   ```bash
   mvn exec:java -Dexec.mainClass="com.cmu.blockchain.network.ServerTCP"
   ```

2. **Run the client**:
   In another terminal, start the `ClientTCP` to interact with the server.
   ```bash
   mvn exec:java -Dexec.mainClass="com.cmu.blockchain.network.ClientTCP"
   ```

3. **Client Menu**:
   The client will display the following menu options:
   ```
   Block Chain Menu
   0. View basic blockchain status.
   1. Add a transaction to the blockchain.
   2. Verify the blockchain.
   3. View the blockchain.
   4. Corrupt the chain.
   5. Hide the corruption by repairing the chain.
   6. Exit.
   ```

## Test Cases

### Running Tests

JUnit and Mockito are used for testing. Run the following Maven command to execute all tests:

```bash
mvn test
```

### Test Coverage

- **`BlockChainTest.java`**: Tests the `BlockChain` class for adding blocks, verifying integrity, and repairing the chain.
- **`ClientTCPTest.java`**: Tests the client-side interactions, including adding transactions, corrupting blocks, and viewing blockchain status.

### Example Test Cases

- **Add Transaction**: Ensures a transaction is successfully added to the blockchain.
- **Verify Blockchain**: Tests that the blockchain remains valid after transactions.
- **Corrupt Block**: Verifies that corrupting a block is reflected in the blockchain integrity.
- **Repair Chain**: Confirms that a corrupted chain can be repaired correctly.

## Contributing

Contributions are welcome! Please follow the steps below:

1. Fork the repository.
2. Create a new feature branch.
   ```bash
   git checkout -b feature-branch
   ```
3. Commit your changes and push the branch.
   ```bash
   git push origin feature-branch
   ```
4. Open a pull request.

## License

This project is licensed under the MIT License.

---
