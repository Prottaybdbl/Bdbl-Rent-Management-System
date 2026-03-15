# BDBL Rent Management System - Database Schema Design

## Database Table Structure

### 1. Building
```sql
CREATE TABLE buildings (
    building_id SERIAL PRIMARY KEY,
    building_name VARCHAR(255) NOT NULL,
    building_code VARCHAR(50) UNIQUE NOT NULL,
    address TEXT,
    city VARCHAR(100),
    district VARCHAR(100),
    total_floors INTEGER,
    construction_year INTEGER,
    ownership_type VARCHAR(50), -- owned, rented, leased
    total_area_sqft DECIMAL(12,2),
    rentable_area_sqft DECIMAL(12,2),
    status VARCHAR(50) DEFAULT 'active', -- active, inactive, under_maintenance
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    updated_by INTEGER
);
```

### 2. Floor
```sql
CREATE TABLE floors (
    floor_id SERIAL PRIMARY KEY,
    building_id INTEGER REFERENCES buildings(building_id),
    floor_number VARCHAR(50) NOT NULL, -- G, 1, 2, 3, ..., Roof
    floor_name VARCHAR(255),
    total_area_sqft DECIMAL(12,2),
    rentable_area_sqft DECIMAL(12,2),
    common_area_sqft DECIMAL(12,2),
    status VARCHAR(50) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(building_id, floor_number)
);
```

### 3. Unit/Space
```sql
CREATE TABLE units (
    unit_id SERIAL PRIMARY KEY,
    building_id INTEGER REFERENCES buildings(building_id),
    floor_id INTEGER REFERENCES floors(floor_id),
    unit_number VARCHAR(100) NOT NULL,
    unit_name VARCHAR(255),
    unit_type VARCHAR(50), -- office, shop, warehouse, parking, storage
    area_sqft DECIMAL(12,2),
    status VARCHAR(50) DEFAULT 'vacant', -- vacant, occupied, under_maintenance, reserved
    rent_type VARCHAR(50), -- sqft_based, unit_based
    base_rent_per_sqft DECIMAL(10,2),
    base_rent_unit DECIMAL(12,2),
    description TEXT,
    amenities TEXT, -- JSON or comma-separated
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(building_id, floor_id, unit_number)
);
```

### 4. Parking
```sql
CREATE TABLE parking_spaces (
    parking_id SERIAL PRIMARY KEY,
    building_id INTEGER REFERENCES buildings(building_id),
    parking_number VARCHAR(100) NOT NULL,
    parking_type VARCHAR(50), -- car, bike, heavy_vehicle
    location VARCHAR(255), -- basement, ground, roof
    status VARCHAR(50) DEFAULT 'vacant', -- vacant, occupied, reserved
    monthly_rent DECIMAL(12,2),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(building_id, parking_number)
);
```

### 5. Tenant/Company
```sql
CREATE TABLE tenants (
    tenant_id SERIAL PRIMARY KEY,
    tenant_code VARCHAR(50) UNIQUE NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    business_type VARCHAR(100),
    trade_license_no VARCHAR(100),
    tin_number VARCHAR(100),
    registration_address TEXT,
    contact_address TEXT,
    phone_primary VARCHAR(20),
    phone_secondary VARCHAR(20),
    email_primary VARCHAR(255),
    email_secondary VARCHAR(255),
    website VARCHAR(255),
    bank_name VARCHAR(255),
    bank_account_no VARCHAR(100),
    bank_branch VARCHAR(255),
    routing_number VARCHAR(50),
    status VARCHAR(50) DEFAULT 'active', -- active, inactive, blacklisted
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 6. Contact Person
```sql
CREATE TABLE tenant_contacts (
    contact_id SERIAL PRIMARY KEY,
    tenant_id INTEGER REFERENCES tenants(tenant_id) ON DELETE CASCADE,
    contact_name VARCHAR(255) NOT NULL,
    designation VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(255),
    is_primary BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 7. Lease Agreement
```sql
CREATE TABLE lease_agreements (
    agreement_id SERIAL PRIMARY KEY,
    agreement_number VARCHAR(100) UNIQUE NOT NULL,
    tenant_id INTEGER REFERENCES tenants(tenant_id),
    agreement_type VARCHAR(50), -- new, renewal
    agreement_date DATE NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    duration_months INTEGER,
    
    -- Rent Calculation
    rent_calculation_type VARCHAR(50), -- sqft_based, unit_based, mixed
    total_area_sqft DECIMAL(12,2),
    rent_per_sqft DECIMAL(10,2),
    monthly_rent_base DECIMAL(12,2) NOT NULL,
    
    -- Additional Charges
    service_charge DECIMAL(12,2) DEFAULT 0,
    parking_charge DECIMAL(12,2) DEFAULT 0,
    other_charge DECIMAL(12,2) DEFAULT 0,
    
    -- VAT and Tax
    vat_percentage DECIMAL(5,2) DEFAULT 0,
    vat_amount DECIMAL(12,2) DEFAULT 0,
    tax_percentage DECIMAL(5,2) DEFAULT 0,
    tax_amount DECIMAL(12,2) DEFAULT 0,
    
    -- Total Monthly Rent
    total_monthly_rent DECIMAL(12,2) NOT NULL,
    
    -- Financial Terms
    security_deposit DECIMAL(12,2),
    advance_rent_months INTEGER DEFAULT 0,
    advance_rent_amount DECIMAL(12,2) DEFAULT 0,
    payment_cycle VARCHAR(50), -- monthly, quarterly, yearly
    payment_due_day INTEGER, -- 1-31
    late_payment_penalty_type VARCHAR(50), -- percentage, fixed
    late_payment_penalty_value DECIMAL(10,2),
    
    -- Annual Increase
    annual_increase_applicable BOOLEAN DEFAULT false,
    annual_increase_percentage DECIMAL(5,2),
    annual_increase_amount DECIMAL(12,2),
    
    -- Renewal Terms
    renewal_notice_days INTEGER DEFAULT 60,
    auto_renewal BOOLEAN DEFAULT false,
    
    -- Control
    controlled_by VARCHAR(100), -- BDBL, Other
    
    status VARCHAR(50) DEFAULT 'active', -- draft, active, expired, terminated
    
    -- Termination
    termination_date DATE,
    termination_reason TEXT,
    
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    updated_by INTEGER
);
```

### 8. Agreement Units (Many-to-Many Relationship)
```sql
CREATE TABLE agreement_units (
    id SERIAL PRIMARY KEY,
    agreement_id INTEGER REFERENCES lease_agreements(agreement_id) ON DELETE CASCADE,
    unit_id INTEGER REFERENCES units(unit_id),
    area_sqft DECIMAL(12,2),
    rent_per_sqft DECIMAL(10,2),
    monthly_rent DECIMAL(12,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 9. Agreement Parking
```sql
CREATE TABLE agreement_parking (
    id SERIAL PRIMARY KEY,
    agreement_id INTEGER REFERENCES lease_agreements(agreement_id) ON DELETE CASCADE,
    parking_id INTEGER REFERENCES parking_spaces(parking_id),
    monthly_rent DECIMAL(12,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 10. Agreement Documents
```sql
CREATE TABLE agreement_documents (
    document_id SERIAL PRIMARY KEY,
    agreement_id INTEGER REFERENCES lease_agreements(agreement_id) ON DELETE CASCADE,
    document_type VARCHAR(100), -- agreement, annexure, notice, other
    document_name VARCHAR(255) NOT NULL,
    file_path TEXT NOT NULL,
    file_size INTEGER,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    uploaded_by INTEGER
);
```

### 11. Utility Types
```sql
CREATE TABLE utility_types (
    utility_type_id SERIAL PRIMARY KEY,
    utility_name VARCHAR(100) NOT NULL UNIQUE, -- electricity, gas, water, internet
    unit VARCHAR(50), -- kwh, cubic_meter, liter, gb
    base_charge DECIMAL(12,2) DEFAULT 0,
    vat_applicable BOOLEAN DEFAULT true,
    vat_percentage DECIMAL(5,2) DEFAULT 0,
    status VARCHAR(50) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 12. Utility Rate Slabs
```sql
CREATE TABLE utility_rate_slabs (
    slab_id SERIAL PRIMARY KEY,
    utility_type_id INTEGER REFERENCES utility_types(utility_type_id),
    slab_name VARCHAR(100), -- 0-100, 101-300, 301+
    min_usage DECIMAL(12,2),
    max_usage DECIMAL(12,2), -- NULL for unlimited
    rate_per_unit DECIMAL(10,4),
    effective_from DATE,
    effective_to DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 13. Utility Meters
```sql
CREATE TABLE utility_meters (
    meter_id SERIAL PRIMARY KEY,
    meter_number VARCHAR(100) NOT NULL UNIQUE,
    utility_type_id INTEGER REFERENCES utility_types(utility_type_id),
    unit_id INTEGER REFERENCES units(unit_id),
    meter_type VARCHAR(50), -- digital, analog, smart
    installation_date DATE,
    initial_reading DECIMAL(12,2) DEFAULT 0,
    status VARCHAR(50) DEFAULT 'active', -- active, inactive, faulty
    last_reading_date DATE,
    last_reading_value DECIMAL(12,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 14. Meter Readings
```sql
CREATE TABLE meter_readings (
    reading_id SERIAL PRIMARY KEY,
    meter_id INTEGER REFERENCES utility_meters(meter_id),
    reading_date DATE NOT NULL,
    previous_reading DECIMAL(12,2),
    current_reading DECIMAL(12,2) NOT NULL,
    consumption DECIMAL(12,2), -- auto calculated
    reading_type VARCHAR(50), -- regular, special, estimated
    notes TEXT,
    photo_path TEXT,
    recorded_by INTEGER,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(meter_id, reading_date)
);
```

### 15. Utility Bills
```sql
CREATE TABLE utility_bills (
    utility_bill_id SERIAL PRIMARY KEY,
    bill_number VARCHAR(100) UNIQUE NOT NULL,
    agreement_id INTEGER REFERENCES lease_agreements(agreement_id),
    tenant_id INTEGER REFERENCES tenants(tenant_id),
    meter_id INTEGER REFERENCES utility_meters(meter_id),
    utility_type_id INTEGER REFERENCES utility_types(utility_type_id),
    
    billing_period_start DATE NOT NULL,
    billing_period_end DATE NOT NULL,
    
    previous_reading DECIMAL(12,2),
    current_reading DECIMAL(12,2),
    consumption DECIMAL(12,2),
    
    base_charge DECIMAL(12,2) DEFAULT 0,
    consumption_charge DECIMAL(12,2) DEFAULT 0,
    subtotal DECIMAL(12,2),
    vat_percentage DECIMAL(5,2),
    vat_amount DECIMAL(12,2),
    total_amount DECIMAL(12,2) NOT NULL,
    
    due_date DATE,
    status VARCHAR(50) DEFAULT 'pending', -- pending, paid, overdue, cancelled
    
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    generated_by INTEGER,
    
    -- Payment tracking
    paid_amount DECIMAL(12,2) DEFAULT 0,
    paid_date DATE,
    payment_id INTEGER
);
```

### 16. Rent Bills
```sql
CREATE TABLE rent_bills (
    bill_id SERIAL PRIMARY KEY,
    bill_number VARCHAR(100) UNIQUE NOT NULL,
    agreement_id INTEGER REFERENCES lease_agreements(agreement_id),
    tenant_id INTEGER REFERENCES tenants(tenant_id),
    
    billing_month DATE NOT NULL, -- first day of month
    billing_period_start DATE,
    billing_period_end DATE,
    
    -- Rent Components
    base_rent DECIMAL(12,2) NOT NULL,
    service_charge DECIMAL(12,2) DEFAULT 0,
    parking_charge DECIMAL(12,2) DEFAULT 0,
    other_charges DECIMAL(12,2) DEFAULT 0,
    
    subtotal DECIMAL(12,2),
    
    -- Deductions
    advance_adjustment DECIMAL(12,2) DEFAULT 0,
    discount DECIMAL(12,2) DEFAULT 0,
    
    -- Tax
    vat_percentage DECIMAL(5,2),
    vat_amount DECIMAL(12,2),
    tax_percentage DECIMAL(5,2),
    tax_amount DECIMAL(12,2),
    
    -- Penalty
    late_payment_penalty DECIMAL(12,2) DEFAULT 0,
    
    total_amount DECIMAL(12,2) NOT NULL,
    
    due_date DATE NOT NULL,
    status VARCHAR(50) DEFAULT 'pending', -- pending, paid, partial, overdue, cancelled
    
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    generated_by INTEGER,
    
    -- Payment tracking
    paid_amount DECIMAL(12,2) DEFAULT 0,
    outstanding_amount DECIMAL(12,2),
    
    notes TEXT
);
```

### 17. Payments
```sql
CREATE TABLE payments (
    payment_id SERIAL PRIMARY KEY,
    payment_number VARCHAR(100) UNIQUE NOT NULL,
    tenant_id INTEGER REFERENCES tenants(tenant_id),
    agreement_id INTEGER REFERENCES lease_agreements(agreement_id),
    
    payment_date DATE NOT NULL,
    payment_type VARCHAR(50), -- rent, utility, advance, security_deposit, penalty, other
    
    payment_method VARCHAR(50), -- cash, cheque, bank_transfer, online
    
    -- Cheque Details (if applicable)
    cheque_number VARCHAR(100),
    cheque_date DATE,
    bank_name VARCHAR(255),
    
    -- Bank Transfer (if applicable)
    transaction_reference VARCHAR(255),
    bank_account VARCHAR(100),
    
    amount DECIMAL(12,2) NOT NULL,
    
    received_by INTEGER,
    receipt_number VARCHAR(100),
    
    notes TEXT,
    
    status VARCHAR(50) DEFAULT 'completed', -- completed, bounced, cancelled
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER
);
```

### 18. Payment Allocations
```sql
CREATE TABLE payment_allocations (
    allocation_id SERIAL PRIMARY KEY,
    payment_id INTEGER REFERENCES payments(payment_id) ON DELETE CASCADE,
    
    allocation_type VARCHAR(50), -- rent_bill, utility_bill, advance, security
    
    rent_bill_id INTEGER REFERENCES rent_bills(bill_id),
    utility_bill_id INTEGER REFERENCES utility_bills(utility_bill_id),
    
    allocated_amount DECIMAL(12,2) NOT NULL,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 19. Arrears
```sql
CREATE TABLE arrears (
    arrear_id SERIAL PRIMARY KEY,
    tenant_id INTEGER REFERENCES tenants(tenant_id),
    agreement_id INTEGER REFERENCES lease_agreements(agreement_id),
    
    arrear_type VARCHAR(50), -- rent, utility, penalty
    
    rent_bill_id INTEGER REFERENCES rent_bills(bill_id),
    utility_bill_id INTEGER REFERENCES utility_bills(utility_bill_id),
    
    original_amount DECIMAL(12,2) NOT NULL,
    outstanding_amount DECIMAL(12,2) NOT NULL,
    
    due_date DATE NOT NULL,
    arrear_days INTEGER, -- calculated
    
    penalty_amount DECIMAL(12,2) DEFAULT 0,
    
    status VARCHAR(50) DEFAULT 'outstanding', -- outstanding, partial, cleared
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 20. Advance Payments
```sql
CREATE TABLE advance_payments (
    advance_id SERIAL PRIMARY KEY,
    tenant_id INTEGER REFERENCES tenants(tenant_id),
    agreement_id INTEGER REFERENCES lease_agreements(agreement_id),
    
    payment_id INTEGER REFERENCES payments(payment_id),
    
    advance_type VARCHAR(50), -- rent_advance, security_deposit
    
    original_amount DECIMAL(12,2) NOT NULL,
    remaining_amount DECIMAL(12,2) NOT NULL,
    
    status VARCHAR(50) DEFAULT 'active', -- active, adjusted, refunded
    
    received_date DATE,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 21. Advance Adjustments
```sql
CREATE TABLE advance_adjustments (
    adjustment_id SERIAL PRIMARY KEY,
    advance_id INTEGER REFERENCES advance_payments(advance_id),
    
    rent_bill_id INTEGER REFERENCES rent_bills(bill_id),
    
    adjusted_amount DECIMAL(12,2) NOT NULL,
    adjustment_date DATE NOT NULL,
    
    notes TEXT,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER
);
```

### 22. Notifications
```sql
CREATE TABLE notifications (
    notification_id SERIAL PRIMARY KEY,
    notification_type VARCHAR(100), -- agreement_expiry, payment_due, payment_overdue, meter_reading_due
    
    target_user_id INTEGER, -- internal user
    tenant_id INTEGER REFERENCES tenants(tenant_id), -- external
    
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    
    related_entity_type VARCHAR(100), -- agreement, payment, bill
    related_entity_id INTEGER,
    
    notification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    delivery_method VARCHAR(50), -- in_app, email, sms
    
    status VARCHAR(50) DEFAULT 'pending', -- pending, sent, read, failed
    
    sent_at TIMESTAMP,
    read_at TIMESTAMP
);
```

### 23. Users
```sql
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    
    role VARCHAR(50), -- super_admin, manager, data_entry, accountant, viewer
    
    department VARCHAR(100),
    designation VARCHAR(100),
    
    status VARCHAR(50) DEFAULT 'active', -- active, inactive, suspended
    
    last_login TIMESTAMP,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER
);
```

### 24. Permissions
```sql
CREATE TABLE user_permissions (
    permission_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    
    module_name VARCHAR(100), -- building, tenant, agreement, payment, utility, report
    
    can_view BOOLEAN DEFAULT false,
    can_create BOOLEAN DEFAULT false,
    can_edit BOOLEAN DEFAULT false,
    can_delete BOOLEAN DEFAULT false,
    can_approve BOOLEAN DEFAULT false,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 25. Audit Log
```sql
CREATE TABLE audit_logs (
    log_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id),
    
    action_type VARCHAR(100), -- create, update, delete, login, logout, export
    
    table_name VARCHAR(100),
    record_id INTEGER,
    
    old_values JSONB,
    new_values JSONB,
    
    ip_address VARCHAR(50),
    user_agent TEXT,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 26. System Settings
```sql
CREATE TABLE system_settings (
    setting_id SERIAL PRIMARY KEY,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT,
    setting_type VARCHAR(50), -- string, number, boolean, json
    description TEXT,
    
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by INTEGER
);
```

### 27. Reports (Optional - for saving report templates)
```sql
CREATE TABLE saved_reports (
    report_id SERIAL PRIMARY KEY,
    report_name VARCHAR(255) NOT NULL,
    report_type VARCHAR(100),
    
    filters JSONB, -- saved filter parameters
    
    created_by INTEGER REFERENCES users(user_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    is_scheduled BOOLEAN DEFAULT false,
    schedule_frequency VARCHAR(50), -- daily, weekly, monthly
    schedule_day INTEGER,
    schedule_time TIME
);
```

---

## Indexes - For Performance

```sql
-- Building related
CREATE INDEX idx_buildings_status ON buildings(status);
CREATE INDEX idx_floors_building ON floors(building_id);
CREATE INDEX idx_units_building_floor ON units(building_id, floor_id);
CREATE INDEX idx_units_status ON units(status);

-- Tenant related
CREATE INDEX idx_tenants_status ON tenants(status);
CREATE INDEX idx_tenant_contacts_tenant ON tenant_contacts(tenant_id);

-- Agreement related
CREATE INDEX idx_agreements_tenant ON lease_agreements(tenant_id);
CREATE INDEX idx_agreements_status ON lease_agreements(status);
CREATE INDEX idx_agreements_dates ON lease_agreements(start_date, end_date);
CREATE INDEX idx_agreement_units_agreement ON agreement_units(agreement_id);

-- Utility related
CREATE INDEX idx_meters_unit ON utility_meters(unit_id);
CREATE INDEX idx_readings_meter ON meter_readings(meter_id);
CREATE INDEX idx_utility_bills_agreement ON utility_bills(agreement_id);
CREATE INDEX idx_utility_bills_status ON utility_bills(status);

-- Payment related
CREATE INDEX idx_payments_tenant ON payments(tenant_id);
CREATE INDEX idx_payments_date ON payments(payment_date);
CREATE INDEX idx_rent_bills_agreement ON rent_bills(agreement_id);
CREATE INDEX idx_rent_bills_month ON rent_bills(billing_month);
CREATE INDEX idx_rent_bills_status ON rent_bills(status);

-- Arrears
CREATE INDEX idx_arrears_tenant ON arrears(tenant_id);
CREATE INDEX idx_arrears_status ON arrears(status);

-- Audit
CREATE INDEX idx_audit_user ON audit_logs(user_id);
CREATE INDEX idx_audit_date ON audit_logs(created_at);
CREATE INDEX idx_audit_table ON audit_logs(table_name, record_id);
```

---

## Views - For Easy Data Access

### 1. Current Active Agreements View
```sql
CREATE VIEW active_agreements AS
SELECT 
    la.agreement_id,
    la.agreement_number,
    t.tenant_id,
    t.company_name,
    b.building_name,
    la.start_date,
    la.end_date,
    la.total_monthly_rent,
    la.controlled_by,
    -- Days until expiry
    (la.end_date - CURRENT_DATE) as days_until_expiry
FROM lease_agreements la
JOIN tenants t ON la.tenant_id = t.tenant_id
JOIN agreement_units au ON la.agreement_id = au.agreement_id
JOIN units u ON au.unit_id = u.unit_id
JOIN buildings b ON u.building_id = b.building_id
WHERE la.status = 'active'
  AND la.end_date >= CURRENT_DATE
GROUP BY la.agreement_id, t.tenant_id, b.building_name;
```

### 2. Arrears Summary View
```sql
CREATE VIEW arrears_summary AS
SELECT 
    t.tenant_id,
    t.company_name,
    COUNT(DISTINCT a.arrear_id) as total_arrears,
    SUM(a.outstanding_amount) as total_outstanding,
    MIN(a.due_date) as oldest_due_date,
    MAX(a.arrear_days) as max_days_overdue
FROM arrears a
JOIN tenants t ON a.tenant_id = t.tenant_id
WHERE a.status = 'outstanding'
GROUP BY t.tenant_id, t.company_name;
```

### 3. Monthly Rent Revenue View
```sql
CREATE VIEW monthly_rent_revenue AS
SELECT 
    DATE_TRUNC('month', billing_month) as month,
    COUNT(*) as total_bills,
    SUM(total_amount) as total_billed,
    SUM(paid_amount) as total_collected,
    SUM(outstanding_amount) as total_outstanding
FROM rent_bills
GROUP BY DATE_TRUNC('month', billing_month)
ORDER BY month DESC;
```

### 4. Occupancy Rate View
```sql
CREATE VIEW occupancy_rate AS
SELECT 
    b.building_id,
    b.building_name,
    COUNT(u.unit_id) as total_units,
    SUM(CASE WHEN u.status = 'occupied' THEN 1 ELSE 0 END) as occupied_units,
    SUM(CASE WHEN u.status = 'vacant' THEN 1 ELSE 0 END) as vacant_units,
    ROUND(
        (SUM(CASE WHEN u.status = 'occupied' THEN 1 ELSE 0 END)::DECIMAL / 
         COUNT(u.unit_id)) * 100, 2
    ) as occupancy_percentage
FROM buildings b
LEFT JOIN units u ON b.building_id = u.building_id
WHERE u.unit_type != 'parking'
GROUP BY b.building_id, b.building_name;
```

---

## Triggers - For Automatic Processes

### 1. updated_at Trigger (for all tables)
```sql
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply to all tables with updated_at column
CREATE TRIGGER update_buildings_timestamp
    BEFORE UPDATE ON buildings
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();

-- Repeat for other tables...
```

### 2. Unit Status Update Trigger
```sql
CREATE OR REPLACE FUNCTION update_unit_status()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'active' THEN
        -- Update all associated units to occupied
        UPDATE units
        SET status = 'occupied'
        WHERE unit_id IN (
            SELECT unit_id FROM agreement_units
            WHERE agreement_id = NEW.agreement_id
        );
    ELSIF NEW.status = 'expired' OR NEW.status = 'terminated' THEN
        -- Update units back to vacant
        UPDATE units
        SET status = 'vacant'
        WHERE unit_id IN (
            SELECT unit_id FROM agreement_units
            WHERE agreement_id = NEW.agreement_id
        );
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER agreement_unit_status_trigger
    AFTER INSERT OR UPDATE OF status ON lease_agreements
    FOR EACH ROW EXECUTE FUNCTION update_unit_status();
```

### 3. Total Monthly Rent Calculation Trigger
```sql
CREATE OR REPLACE FUNCTION calculate_total_monthly_rent()
RETURNS TRIGGER AS $$
BEGIN
    NEW.subtotal = 
        NEW.monthly_rent_base + 
        COALESCE(NEW.service_charge, 0) + 
        COALESCE(NEW.parking_charge, 0) + 
        COALESCE(NEW.other_charge, 0);
    
    NEW.vat_amount = (NEW.subtotal * NEW.vat_percentage / 100);
    NEW.tax_amount = (NEW.subtotal * NEW.tax_percentage / 100);
    
    NEW.total_monthly_rent = 
        NEW.subtotal + 
        NEW.vat_amount + 
        NEW.tax_amount;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER calculate_rent_trigger
    BEFORE INSERT OR UPDATE ON lease_agreements
    FOR EACH ROW EXECUTE FUNCTION calculate_total_monthly_rent();
```

### 4. Payment Allocation Trigger
```sql
CREATE OR REPLACE FUNCTION update_bill_payment_status()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.allocation_type = 'rent_bill' THEN
        UPDATE rent_bills
        SET 
            paid_amount = paid_amount + NEW.allocated_amount,
            outstanding_amount = total_amount - (paid_amount + NEW.allocated_amount),
            status = CASE
                WHEN (paid_amount + NEW.allocated_amount) >= total_amount THEN 'paid'
                WHEN (paid_amount + NEW.allocated_amount) > 0 THEN 'partial'
                ELSE status
            END
        WHERE bill_id = NEW.rent_bill_id;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER payment_allocation_trigger
    AFTER INSERT ON payment_allocations
    FOR EACH ROW EXECUTE FUNCTION update_bill_payment_status();
```

---

## Stored Procedures - For Complex Operations

### 1. Generate Monthly Rent Bills
```sql
CREATE OR REPLACE FUNCTION generate_monthly_rent_bills(
    p_billing_month DATE
)
RETURNS INTEGER AS $$
DECLARE
    v_count INTEGER := 0;
    v_agreement RECORD;
BEGIN
    FOR v_agreement IN 
        SELECT * FROM lease_agreements
        WHERE status = 'active'
          AND start_date <= p_billing_month
          AND end_date >= p_billing_month
    LOOP
        -- Check if bill already exists
        IF NOT EXISTS (
            SELECT 1 FROM rent_bills
            WHERE agreement_id = v_agreement.agreement_id
              AND billing_month = p_billing_month
        ) THEN
            INSERT INTO rent_bills (
                bill_number,
                agreement_id,
                tenant_id,
                billing_month,
                billing_period_start,
                billing_period_end,
                base_rent,
                service_charge,
                parking_charge,
                subtotal,
                vat_percentage,
                vat_amount,
                total_amount,
                outstanding_amount,
                due_date
            ) VALUES (
                'RB-' || TO_CHAR(p_billing_month, 'YYYYMM') || '-' || LPAD(v_agreement.agreement_id::TEXT, 5, '0'),
                v_agreement.agreement_id,
                v_agreement.tenant_id,
                p_billing_month,
                p_billing_month,
                p_billing_month + INTERVAL '1 month' - INTERVAL '1 day',
                v_agreement.monthly_rent_base,
                v_agreement.service_charge,
                v_agreement.parking_charge,
                v_agreement.monthly_rent_base + v_agreement.service_charge + v_agreement.parking_charge,
                v_agreement.vat_percentage,
                (v_agreement.monthly_rent_base + v_agreement.service_charge + v_agreement.parking_charge) * v_agreement.vat_percentage / 100,
                v_agreement.total_monthly_rent,
                v_agreement.total_monthly_rent,
                p_billing_month + INTERVAL '1 month' + (v_agreement.payment_due_day - 1) * INTERVAL '1 day'
            );
            
            v_count := v_count + 1;
        END IF;
    END LOOP;
    
    RETURN v_count;
END;
$$ LANGUAGE plpgsql;
```

### 2. Update Arrears
```sql
CREATE OR REPLACE FUNCTION update_arrears()
RETURNS INTEGER AS $$
DECLARE
    v_count INTEGER := 0;
    v_bill RECORD;
BEGIN
    -- Process rent bills
    FOR v_bill IN 
        SELECT * FROM rent_bills
        WHERE status IN ('pending', 'partial')
          AND due_date < CURRENT_DATE
    LOOP
        -- Insert or update arrear record
        INSERT INTO arrears (
            tenant_id,
            agreement_id,
            arrear_type,
            rent_bill_id,
            original_amount,
            outstanding_amount,
            due_date,
            arrear_days
        ) VALUES (
            v_bill.tenant_id,
            v_bill.agreement_id,
            'rent',
            v_bill.bill_id,
            v_bill.total_amount,
            v_bill.outstanding_amount,
            v_bill.due_date,
            CURRENT_DATE - v_bill.due_date
        )
        ON CONFLICT (rent_bill_id) 
        DO UPDATE SET
            outstanding_amount = v_bill.outstanding_amount,
            arrear_days = CURRENT_DATE - v_bill.due_date,
            updated_at = CURRENT_TIMESTAMP;
        
        v_count := v_count + 1;
    END LOOP;
    
    RETURN v_count;
END;
$$ LANGUAGE plpgsql;
```

---

## Sample Data Insert Scripts

```sql
-- Sample Building
INSERT INTO buildings (building_name, building_code, address, city, total_floors, total_area_sqft)
VALUES ('Karwan Bazar Office Complex', 'KB-001', 'Karwan Bazar, Dhaka', 'Dhaka', 20, 250000);

-- Sample Tenant
INSERT INTO tenants (tenant_code, company_name, business_type, phone_primary, email_primary)
VALUES ('T-001', 'Lavender Limited', 'IT Services', '01700000000', 'info@lavender.com');

-- More samples as needed...
```

---

This database schema provides a complete data model for your system. It is normalized, scalable, and performant.
