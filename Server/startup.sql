
-- Create tables if not exists________________________________________________

CREATE TABLE IF NOT EXISTS Customer (
    CustomerID INT AUTO_INCREMENT PRIMARY KEY,
    LoginName VARCHAR(255) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Employee (
    EmployeeID INT AUTO_INCREMENT PRIMARY KEY,
    LoginName VARCHAR(255) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    StrikeCount INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS Ticket (
    TicketID INT AUTO_INCREMENT PRIMARY KEY,
    Description TEXT,
    IssueDate DATETIME,
    State VARCHAR(50),
    CustomerID INT,
    EmployeeID INT,                    -- FK to Employee
    AssignmentDate DATETIME,           
    CONSTRAINT fk_ticket_customer
        FOREIGN KEY (CustomerID)
        REFERENCES Customer(CustomerID)
        ON DELETE SET NULL,
    CONSTRAINT fk_ticket_employee
        FOREIGN KEY (EmployeeID)
        REFERENCES Employee(EmployeeID)
        ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS Answer (
    AnswerID INT AUTO_INCREMENT PRIMARY KEY,
    AnswerString TEXT,
    IsFinal BOOLEAN DEFAULT FALSE,
    TicketID INT,
    CONSTRAINT fk_answer_ticket
        FOREIGN KEY (TicketID)
        REFERENCES Ticket(TicketID)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Keyword (
    KeywordID INT AUTO_INCREMENT PRIMARY KEY,
    Keyword VARCHAR(255) NOT NULL
);

-- n:m link
CREATE TABLE IF NOT EXISTS Ticket_Keyword (
    TicketID INT,
    KeywordID INT,
    PRIMARY KEY (TicketID, KeywordID),
    CONSTRAINT fk_ticketkeyword_ticket
        FOREIGN KEY (TicketID)
        REFERENCES Ticket(TicketID)
        ON DELETE CASCADE,
    CONSTRAINT fk_ticketkeyword_keyword
        FOREIGN KEY (KeywordID)
        REFERENCES Keyword(KeywordID)
        ON DELETE CASCADE
);


-- create default data if not exists___________________________________________

-- create default customers if not exists
INSERT INTO Customer (LoginName, Password)
SELECT * FROM (
    SELECT 'admin' AS LoginName, 'admin' AS Password
    UNION ALL
    SELECT 'admin1', 'admin'
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM Customer);

-- create default employees if not exists
INSERT INTO Employee (LoginName, Password, StrikeCount)
SELECT * FROM (
    SELECT 'admin' AS LoginName, 'admin' AS Password, 0 AS StrikeCount
    UNION ALL
    SELECT 'admin1', 'admin', 0
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM Employee);

-- create default keywords if not exists
INSERT INTO Keyword (Keyword)
SELECT * FROM (
    SELECT 'GUI' AS Keyword
    UNION ALL SELECT 'Lesbarkeit'
    UNION ALL SELECT 'Bug'
    UNION ALL SELECT 'Systemabsturz'
    UNION ALL SELECT 'Security'
    UNION ALL SELECT 'Datenschutz'
    UNION ALL SELECT 'Allgemeine Frage'
    UNION ALL SELECT 'Verfügbarkeit'
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM Keyword);