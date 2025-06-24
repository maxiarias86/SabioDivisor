CREATE DATABASE IF NOT EXISTS sabio_divisor
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE sabio_divisor;


CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL
);

CREATE TABLE payments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  amount DOUBLE NOT NULL CHECK (amount > 0),
  date DATE NOT NULL,
  payer_id INT NOT NULL,
  recipient_id INT NOT NULL,
  FOREIGN KEY (payer_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (recipient_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE expenses (
  id INT AUTO_INCREMENT PRIMARY KEY,
  amount DOUBLE NOT NULL CHECK (amount > 0),
  date DATE NOT NULL,
  installments INT NOT NULL CHECK (installments >= 1),
  description VARCHAR(255) NOT NULL
);

CREATE TABLE debts (
  id INT AUTO_INCREMENT PRIMARY KEY,
  amount DOUBLE NOT NULL CHECK (amount > 0),
  creditor_id INT NOT NULL,
  debtor_id INT NOT NULL,
  expense_id INT NOT NULL,
  due_date DATE NOT NULL,
  installment_number INT NOT NULL DEFAULT 1,
  FOREIGN KEY (creditor_id) REFERENCES users(id) ON DELETE RESTRICT,
  FOREIGN KEY (debtor_id) REFERENCES users(id) ON DELETE RESTRICT,
  FOREIGN KEY (expense_id) REFERENCES expenses(id) ON DELETE CASCADE
);
