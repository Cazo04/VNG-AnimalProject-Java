-- Create tables for Animal Management System

-- Enclosure table (referenced by Animal)
CREATE TABLE IF NOT EXISTS enclosure (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    location VARCHAR(200),
    capacity INTEGER NOT NULL,
    current_animal_count INTEGER DEFAULT 0
);

-- Animal table
CREATE TABLE IF NOT EXISTS animal (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    species VARCHAR(100) NOT NULL,
    gender VARCHAR(10),
    age INTEGER,
    arrival_date DATE,
    enclosure_id BIGINT,
    CONSTRAINT fk_enclosure FOREIGN KEY (enclosure_id) REFERENCES enclosure(id) ON DELETE SET NULL
);

-- Staff table (referenced by Feeding, Health, and Logging)
CREATE TABLE IF NOT EXISTS staff (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(200) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL,
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Account table for system authentication
CREATE TABLE IF NOT EXISTS account (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(200) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'OPERATOR')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Feeding table
CREATE TABLE IF NOT EXISTS feeding (
    id BIGSERIAL PRIMARY KEY,
    animal_id BIGINT NOT NULL,
    food_type VARCHAR(100) NOT NULL,
    quantity DECIMAL(10, 2),
    feeding_time TIMESTAMP NOT NULL,
    keeper_id BIGINT,
    CONSTRAINT fk_animal_feeding FOREIGN KEY (animal_id) REFERENCES animal(id) ON DELETE CASCADE,
    CONSTRAINT fk_keeper FOREIGN KEY (keeper_id) REFERENCES staff(id) ON DELETE SET NULL
);

-- Health table
CREATE TABLE IF NOT EXISTS health (
    id BIGSERIAL PRIMARY KEY,
    animal_id BIGINT NOT NULL,
    staff_id BIGINT,
    weight DECIMAL(10, 2),
    status VARCHAR(20) CHECK (status IN ('Excellent', 'Good', 'Fair', 'Poor')),
    activity TEXT,
    check_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_animal_health FOREIGN KEY (animal_id) REFERENCES animal(id) ON DELETE CASCADE,
    CONSTRAINT fk_staff_health FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS api_logging (
    request_id UUID PRIMARY KEY,
    username VARCHAR(50),
    method VARCHAR(100),
    endpoint VARCHAR(500),
    status_code INTEGER,
    duration BIGINT,
    timestamp TIMESTAMP,
    payload TEXT
);

CREATE TABLE IF NOT EXISTS sys_logging (
    id BIGSERIAL PRIMARY KEY,
    service_name VARCHAR(100),
    request_id UUID,
    event_id VARCHAR(100),
    processing_group VARCHAR(200),
    timestamp TIMESTAMP,
    action TEXT,
    duration BIGINT,
    error TEXT,
    CONSTRAINT fk_sys_api_logging FOREIGN KEY (request_id) REFERENCES api_logging(request_id) ON DELETE SET NULL
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_animal_enclosure ON animal(enclosure_id);
CREATE INDEX IF NOT EXISTS idx_animal_species ON animal(species);
CREATE INDEX IF NOT EXISTS idx_feeding_animal ON feeding(animal_id);
CREATE INDEX IF NOT EXISTS idx_feeding_time ON feeding(feeding_time);
CREATE INDEX IF NOT EXISTS idx_health_animal ON health(animal_id);
CREATE INDEX IF NOT EXISTS idx_health_time ON health(check_time);
CREATE INDEX IF NOT EXISTS idx_staff_code ON staff(code);
CREATE INDEX IF NOT EXISTS idx_staff_email ON staff(email);
CREATE INDEX IF NOT EXISTS idx_account_username ON account(username);
CREATE INDEX IF NOT EXISTS idx_account_email ON account(email);


-- Sample Enclosures
INSERT INTO enclosure (name, type, location, capacity, current_animal_count) VALUES
('Savannah Zone', 'Outdoor', 'North Area', 20, 5),
('Aquatic Zone', 'Indoor', 'East Wing', 10, 2);

-- Sample Animals
INSERT INTO animal (name, species, gender, age, arrival_date, enclosure_id) VALUES
('Leo', 'Lion', 'Male', 5, '2021-03-15', 1),
('Mia', 'Lion', 'Female', 4, '2022-05-10', 1),
('Splash', 'Dolphin', 'Male', 8, '2020-08-22', 2);

-- Sample Staff
INSERT INTO staff (code, full_name, email, role, status) VALUES
('STF001', 'Alice Smith', 'alice.smith@example.com', 'Keeper', 'Active'),
('STF002', 'Bob Johnson', 'bob.johnson@example.com', 'Veterinarian', 'Active');

-- Sample Feeding
INSERT INTO feeding (animal_id, food_type, quantity, feeding_time, keeper_id) VALUES
(1, 'Meat', 5.0, '2025-10-30 09:00:00', 1),
(2, 'Meat', 4.5, '2025-10-30 09:30:00', 1),
(3, 'Fish', 3.0, '2025-10-30 10:00:00', 1);

-- Sample Health
INSERT INTO health (animal_id, staff_id, weight, status, activity, check_time) VALUES
(1, 2, 190.5, 'Excellent', 'Active and healthy', '2025-10-30 11:00:00'),
(2, 2, 175.0, 'Good', 'Normal activity', '2025-10-30 11:30:00'),
(3, 2, 210.0, 'Excellent', 'Swimming actively', '2025-10-30 12:00:00');

