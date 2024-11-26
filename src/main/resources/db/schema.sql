-- users table
CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(50),
    role VARCHAR(20) NOT NULL, -- 'ADMIN', 'EMPLOYEE', 'CLIENT'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- accounts table
CREATE TABLE IF NOT EXISTS accounts (
    account_id INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_id INTEGER,
    account_type VARCHAR(20), -- 'SAVINGS', 'CHECKING'
    account_number VARCHAR(20) UNIQUE,
    balance DECIMAL(15,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(user_id)
);

-- transactions table
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
    account_id INTEGER,
    transaction_type VARCHAR(20), -- 'DEPOSIT', 'WITHDRAW', 'TRANSFER'
    amount DECIMAL(15,2),
    description TEXT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'COMPLETED',
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

-- account_requests table
CREATE TABLE IF NOT EXISTS account_requests (
    request_id INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_id INTEGER,
    request_type VARCHAR(50), -- 'NEW_ACCOUNT', 'CLOSE_ACCOUNT', 'UPDATE_INFO'
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_by INTEGER,
    processed_at TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(user_id),
    FOREIGN KEY (processed_by) REFERENCES users(user_id)
);