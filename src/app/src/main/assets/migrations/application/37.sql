CREATE TABLE IF NOT EXISTS TransactionsTemp (Id INTEGER PRIMARY KEY AUTOINCREMENT, CreatedDate DATE, Refunded BOOLEAN NOT NULL DEFAULT 0);
INSERT INTO TransactionsTemp (Id, CreatedDate, Refunded) SELECT Id, CreatedDate, Refunded FROM Transactions;
DROP TABLE Transactions;
ALTER TABLE TransactionsTemp RENAME TO Transactions;