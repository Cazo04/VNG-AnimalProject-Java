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
    role VARCHAR(20) NOT NULL CHECK (role IN ('admin', 'operator')),
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

-- Logging table (username references staff.code)
CREATE TABLE IF NOT EXISTS logging (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50),
    method VARCHAR(10),
    endpoint VARCHAR(500),
    status_code INTEGER,
    duration INTEGER,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payload TEXT,
    CONSTRAINT fk_logging_staff FOREIGN KEY (username) REFERENCES staff(code) ON DELETE SET NULL
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_animal_enclosure ON animal(enclosure_id);
CREATE INDEX IF NOT EXISTS idx_animal_species ON animal(species);
CREATE INDEX IF NOT EXISTS idx_feeding_animal ON feeding(animal_id);
CREATE INDEX IF NOT EXISTS idx_feeding_time ON feeding(feeding_time);
CREATE INDEX IF NOT EXISTS idx_health_animal ON health(animal_id);
CREATE INDEX IF NOT EXISTS idx_health_time ON health(check_time);
CREATE INDEX IF NOT EXISTS idx_logging_timestamp ON logging(timestamp);
CREATE INDEX IF NOT EXISTS idx_logging_username ON logging(username);
CREATE INDEX IF NOT EXISTS idx_staff_code ON staff(code);
CREATE INDEX IF NOT EXISTS idx_staff_email ON staff(email);
CREATE INDEX IF NOT EXISTS idx_account_username ON account(username);
CREATE INDEX IF NOT EXISTS idx_account_email ON account(email);

-- Insert sample data for testing

-- 1. Insert Accounts (3 example accounts)
INSERT INTO account (username, email, password, role) VALUES
('admin', 'admin@zoo.com', 'admin123', 'admin'),
('operator1', 'operator1@zoo.com', 'operator123', 'operator'),
('operator2', 'operator2@zoo.com', 'operator456', 'operator')
ON CONFLICT DO NOTHING;

-- 2. Insert Enclosures (20 enclosures) - Each enclosure for specific species
INSERT INTO enclosure (name, type, location, capacity, current_animal_count) VALUES
('Lion Pride Area', 'Open', 'North Section', 25, 0),
('Tiger Territory', 'Enclosed', 'East Section', 20, 0),
('Elephant Sanctuary', 'Open', 'South Section', 30, 0),
('Giraffe Plains', 'Open', 'West Section', 25, 0),
('Zebra Savanna', 'Open', 'North Section', 35, 0),
('Bear Mountain', 'Enclosed', 'East Section', 15, 0),
('Panda Bamboo Forest', 'Enclosed', 'South Section', 20, 0),
('Kangaroo Outback', 'Open', 'West Section', 40, 0),
('Koala Eucalyptus Grove', 'Enclosed', 'North Section', 30, 0),
('Penguin Colony', 'Climate Controlled', 'East Section', 80, 0),
('Flamingo Lake', 'Water', 'South Section', 100, 0),
('Parrot Aviary', 'Open Air', 'West Section', 60, 0),
('Eagle Cliff', 'Open Air', 'North Section', 25, 0),
('Snake Gallery', 'Enclosed', 'East Section', 40, 0),
('Crocodile Swamp', 'Water', 'South Section', 20, 0),
('Monkey Island', 'Island', 'West Section', 50, 0),
('Gorilla Habitat', 'Enclosed', 'North Section', 15, 0),
('Wolf Den', 'Enclosed', 'East Section', 20, 0),
('Deer Forest', 'Open', 'South Section', 45, 0),
('Rhino Reserve', 'Open', 'West Section', 18, 0)
ON CONFLICT DO NOTHING;

-- 2. Insert Staff (50 staff members)
DO $$
BEGIN
    FOR i IN 1..50 LOOP
        INSERT INTO staff (code, full_name, email, role, status, created_at)
        VALUES (
            'STAFF' || LPAD(i::TEXT, 3, '0'),
            CASE 
                WHEN i % 5 = 0 THEN 'Manager ' || i
                WHEN i % 5 = 1 THEN 'Keeper ' || i
                WHEN i % 5 = 2 THEN 'Veterinarian ' || i
                WHEN i % 5 = 3 THEN 'Nutritionist ' || i
                ELSE 'Caretaker ' || i
            END,
            'staff' || i || '@zoo.com',
            CASE 
                WHEN i % 5 = 0 THEN 'Manager'
                WHEN i % 5 = 1 THEN 'Keeper'
                WHEN i % 5 = 2 THEN 'Veterinarian'
                WHEN i % 5 = 3 THEN 'Nutritionist'
                ELSE 'Caretaker'
            END,
            CASE WHEN i % 10 = 0 THEN 'Inactive' ELSE 'Active' END,
            CURRENT_TIMESTAMP - (i || ' days')::INTERVAL
        )
        ON CONFLICT (code) DO NOTHING;
    END LOOP;
END $$;

-- 3. Insert Animals (500 animals) - Each enclosure has same species
DO $$
DECLARE
    species_per_enclosure TEXT[] := ARRAY['Lion', 'Tiger', 'Elephant', 'Giraffe', 'Zebra', 
                                          'Bear', 'Panda', 'Kangaroo', 'Koala', 'Penguin',
                                          'Flamingo', 'Parrot', 'Eagle', 'Snake', 'Crocodile',
                                          'Monkey', 'Gorilla', 'Wolf', 'Deer', 'Rhino'];
    gender_list TEXT[] := ARRAY['Male', 'Female'];
    enclosure_id INT;
    species_name TEXT;
    animals_per_enclosure INT;
    counter INT := 1;
BEGIN
    -- Loop through each enclosure
    FOR enclosure_id IN 1..20 LOOP
        species_name := species_per_enclosure[enclosure_id];
        animals_per_enclosure := 25; -- 25 animals per enclosure = 500 total
        
        -- Insert animals for this enclosure
        FOR i IN 1..animals_per_enclosure LOOP
            INSERT INTO animal (name, species, gender, age, arrival_date, enclosure_id)
            VALUES (
                species_name || ' ' || counter,
                species_name,
                gender_list[(counter % 2) + 1],
                (counter % 20) + 1,
                CURRENT_DATE - (counter * 15 || ' days')::INTERVAL,
                enclosure_id
            );
            counter := counter + 1;
        END LOOP;
    END LOOP;
END $$;

-- Update enclosure animal counts
UPDATE enclosure e
SET current_animal_count = (
    SELECT COUNT(*) FROM animal a WHERE a.enclosure_id = e.id
);

-- 4. Insert Feeding records (2000 records)
DO $$
DECLARE
    food_types TEXT[] := ARRAY['Meat', 'Fish', 'Vegetables', 'Fruits', 'Grains', 'Hay', 'Pellets', 'Insects', 'Seeds', 'Nectar'];
BEGIN
    FOR i IN 1..2000 LOOP
        INSERT INTO feeding (animal_id, food_type, quantity, feeding_time, keeper_id)
        VALUES (
            ((i % 500) + 1),
            food_types[(i % 10) + 1],
            ROUND((RANDOM() * 50 + 1)::NUMERIC, 2),
            CURRENT_TIMESTAMP - (i || ' hours')::INTERVAL,
            ((i % 50) + 1)
        );
    END LOOP;
END $$;

-- 5. Insert Health records (1500 records)
DO $$
DECLARE
    health_status TEXT[] := ARRAY['Excellent', 'Good', 'Fair', 'Poor'];
    activities TEXT[] := ARRAY['Very active, eating well', 'Normal activity', 'Less active than usual', 'Resting more', 
                               'Eating normally', 'Playing with others', 'Exploring enclosure', 'Sleeping peacefully'];
BEGIN
    FOR i IN 1..1500 LOOP
        INSERT INTO health (animal_id, staff_id, weight, status, activity, check_time)
        VALUES (
            ((i % 500) + 1),
            ((i % 50) + 1),
            ROUND((RANDOM() * 500 + 10)::NUMERIC, 2),
            health_status[(i % 4) + 1],
            activities[(i % 8) + 1],
            CURRENT_TIMESTAMP - (i || ' hours')::INTERVAL
        );
    END LOOP;
END $$;

-- 6. Insert Logging records (3000 records)
DO $$
DECLARE
    methods TEXT[] := ARRAY['GET', 'POST', 'PUT', 'DELETE', 'PATCH'];
    endpoints TEXT[] := ARRAY['/api/animals', '/api/enclosures', '/api/staff', '/api/feeding', '/api/health', 
                              '/api/animals/{id}', '/api/enclosures/{id}', '/api/staff/{id}', '/api/reports', '/api/dashboard'];
    status_codes INTEGER[] := ARRAY[200, 201, 204, 400, 401, 403, 404, 500];
BEGIN
    FOR i IN 1..3000 LOOP
        INSERT INTO logging (username, method, endpoint, status_code, duration, timestamp, payload)
        VALUES (
            'STAFF' || LPAD(((i % 50) + 1)::TEXT, 3, '0'),
            methods[(i % 5) + 1],
            endpoints[(i % 10) + 1],
            status_codes[(i % 8) + 1],
            (RANDOM() * 1000)::INTEGER,
            CURRENT_TIMESTAMP - (i || ' minutes')::INTERVAL,
            CASE 
                WHEN (i % 5) = 1 THEN '{"action":"create","entity":"animal"}'
                WHEN (i % 5) = 2 THEN '{"action":"update","entity":"health"}'
                WHEN (i % 5) = 3 THEN '{"action":"delete","entity":"feeding"}'
                ELSE '{"action":"read"}'
            END
        );
    END LOOP;
END $$;

-- Add comments
COMMENT ON TABLE animal IS 'Stores information about animals in the zoo';
COMMENT ON TABLE enclosure IS 'Stores information about animal enclosures - each enclosure houses one species';
COMMENT ON TABLE feeding IS 'Records feeding schedules and history';
COMMENT ON TABLE staff IS 'Stores staff member information';
COMMENT ON TABLE health IS 'Records health check information for animals';
COMMENT ON TABLE logging IS 'Stores API request logs with username referencing staff.code';
COMMENT ON TABLE account IS 'Stores system authentication accounts with admin and operator roles';
