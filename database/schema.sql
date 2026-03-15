-- ============================================================
-- BDBL Rent Management System (BRMS)
-- MySQL Database Schema
-- Version: 1.0
-- Date: March 2026
-- ============================================================

CREATE DATABASE IF NOT EXISTS bdbl_rms
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE bdbl_rms;

-- ============================================================
-- 1. USERS (ব্যবহারকারী)
-- ============================================================
CREATE TABLE users (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(100)    NOT NULL UNIQUE,
    password_hash   VARCHAR(255)    NOT NULL,
    full_name       VARCHAR(255)    NOT NULL,
    email           VARCHAR(255),
    phone           VARCHAR(20),
    role            VARCHAR(20)     NOT NULL COMMENT 'SUPER_ADMIN, OFFICER',
    status          VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE, INACTIVE',
    last_login      TIMESTAMP       NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by      BIGINT          NULL,

    CONSTRAINT fk_users_created_by FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- ============================================================
-- 2. BUILDINGS (বিল্ডিং)
-- ============================================================
CREATE TABLE buildings (
    id                  BIGINT          AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(255)    NOT NULL,
    code                VARCHAR(50)     NOT NULL UNIQUE,
    address             TEXT,
    city                VARCHAR(100),
    district            VARCHAR(100),
    total_floors        INT             NOT NULL DEFAULT 0,
    total_area_sft      DECIMAL(12,2)   DEFAULT 0,
    construction_year   INT,
    parking_config_type VARCHAR(20)     NOT NULL DEFAULT 'UNIT_WISE' COMMENT 'UNIT_WISE, AREA_WISE',
    status              VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE, INACTIVE, UNDER_MAINTENANCE',
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by          BIGINT,

    CONSTRAINT fk_buildings_created_by FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- ============================================================
-- 3. FLOORS (ফ্লোর)
-- ============================================================
CREATE TABLE floors (
    id                  BIGINT          AUTO_INCREMENT PRIMARY KEY,
    building_id         BIGINT          NOT NULL,
    floor_number        VARCHAR(50)     NOT NULL COMMENT 'G, 1, 2, ... Roof, Basement-1',
    floor_name          VARCHAR(255),
    total_area_sft      DECIMAL(12,2)   NOT NULL DEFAULT 0,
    allocated_area_sft  DECIMAL(12,2)   NOT NULL DEFAULT 0 COMMENT 'Currently rented area',
    common_area_sft     DECIMAL(12,2)   DEFAULT 0,
    status              VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE, UNDER_MAINTENANCE',
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_floors_building FOREIGN KEY (building_id) REFERENCES buildings(id),
    UNIQUE KEY uk_building_floor (building_id, floor_number)
) ENGINE=InnoDB;

-- ============================================================
-- 4. PARKING SPACES (পার্কিং)
-- ============================================================
CREATE TABLE parking_spaces (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    building_id     BIGINT          NOT NULL,
    parking_number  VARCHAR(100)    NOT NULL,
    parking_type    VARCHAR(50)     NOT NULL COMMENT 'CAR, BIKE, HEAVY_VEHICLE',
    location        VARCHAR(255)    COMMENT 'Basement-1, Basement-2, Ground etc.',
    area_sft        DECIMAL(12,2)   DEFAULT 0 COMMENT 'For AREA_WISE config',
    monthly_rent    DECIMAL(12,2)   DEFAULT 0 COMMENT 'For UNIT_WISE config',
    status          VARCHAR(20)     NOT NULL DEFAULT 'VACANT' COMMENT 'VACANT, OCCUPIED, RESERVED',
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_parking_building FOREIGN KEY (building_id) REFERENCES buildings(id),
    UNIQUE KEY uk_building_parking (building_id, parking_number)
) ENGINE=InnoDB;

-- ============================================================
-- 5. TENANTS (ভাড়াটিয়া / কোম্পানি)
-- ============================================================
CREATE TABLE tenants (
    id                      BIGINT          AUTO_INCREMENT PRIMARY KEY,
    tenant_code             VARCHAR(50)     NOT NULL UNIQUE,
    company_name            VARCHAR(255)    NOT NULL,
    business_type           VARCHAR(100),
    trade_license_no        VARCHAR(100),
    tin_number              VARCHAR(100),
    registration_address    TEXT,
    contact_address         TEXT,
    phone_primary           VARCHAR(20),
    phone_secondary         VARCHAR(20),
    email                   VARCHAR(255),
    logo_path               VARCHAR(500),
    status                  VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE, INACTIVE',
    created_at              TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================================
-- 6. TENANT CONTACTS (কন্ট্যাক্ট পার্সন)
-- ============================================================
CREATE TABLE tenant_contacts (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    tenant_id       BIGINT          NOT NULL,
    contact_name    VARCHAR(255)    NOT NULL,
    designation     VARCHAR(100),
    phone           VARCHAR(20),
    email           VARCHAR(255),
    is_primary      BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_contacts_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 7. LEASE AGREEMENTS (লিজ এগ্রিমেন্ট / চুক্তি)
-- ============================================================
CREATE TABLE lease_agreements (
    id                      BIGINT          AUTO_INCREMENT PRIMARY KEY,
    agreement_number        VARCHAR(100)    NOT NULL UNIQUE,
    tenant_id               BIGINT          NOT NULL,
    building_id             BIGINT          NOT NULL,
    agreement_type          VARCHAR(20)     NOT NULL COMMENT 'NEW, RENEWAL',
    parent_agreement_id     BIGINT          NULL COMMENT 'Previous agreement if RENEWAL',
    start_date              DATE            NOT NULL,
    end_date                DATE            NOT NULL,
    duration_months         INT,
    controlled_by           VARCHAR(100)    COMMENT 'BDBL, OTHER',

    -- Service Charge (Fixed Lumpsum)
    service_charge          DECIMAL(12,2)   NOT NULL DEFAULT 0,

    -- Total Monthly Rent (Base + Service + Parking = auto calculated)
    total_monthly_rent      DECIMAL(12,2)   NOT NULL DEFAULT 0,

    -- Security Deposit & Advance
    has_security_deposit    BOOLEAN         NOT NULL DEFAULT FALSE,
    has_advance_payment     BOOLEAN         NOT NULL DEFAULT FALSE,

    -- Late Payment Penalty
    has_penalty             BOOLEAN         NOT NULL DEFAULT FALSE,
    penalty_percentage      DECIMAL(5,2)    DEFAULT 0,

    -- Annual Rent Increment
    has_annual_increment    BOOLEAN         NOT NULL DEFAULT FALSE,
    increment_percentage    DECIMAL(5,2)    DEFAULT 0,
    increment_period_months INT             DEFAULT 12,

    -- Notice Period
    notice_period_days      INT             NOT NULL DEFAULT 60,

    -- Status
    status                  VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE' COMMENT 'DRAFT, ACTIVE, EXPIRED, TERMINATED, RENEWED',
    notes                   TEXT,

    -- Audit Fields
    created_at              TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by              BIGINT,

    CONSTRAINT fk_agreement_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT fk_agreement_building FOREIGN KEY (building_id) REFERENCES buildings(id),
    CONSTRAINT fk_agreement_parent FOREIGN KEY (parent_agreement_id) REFERENCES lease_agreements(id),
    CONSTRAINT fk_agreement_created_by FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- ============================================================
-- 8. FLOOR ALLOCATIONS (ফ্লোর বরাদ্দ — এগ্রিমেন্ট ↔ ফ্লোর)
-- Flexible area model: no fixed units, area is carved from floor
-- ============================================================
CREATE TABLE floor_allocations (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    agreement_id    BIGINT          NOT NULL,
    floor_id        BIGINT          NOT NULL,
    allocation_label VARCHAR(255)   COMMENT 'e.g. Level-1, Ramp/Roof Top, Level-2 (Wooden)',
    area_sft        DECIMAL(12,2)   NOT NULL,
    rent_type       VARCHAR(20)     NOT NULL COMMENT 'SQFT_BASED, LUMPSUM',
    rent_per_sft    DECIMAL(10,2)   DEFAULT 0 COMMENT 'Rate per sft (if SQFT_BASED)',
    lumpsum_rent    DECIMAL(12,2)   DEFAULT 0 COMMENT 'Fixed rent (if LUMPSUM)',
    monthly_rent    DECIMAL(12,2)   NOT NULL COMMENT 'Calculated: area*rate OR lumpsum',
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_allocation_agreement FOREIGN KEY (agreement_id) REFERENCES lease_agreements(id) ON DELETE CASCADE,
    CONSTRAINT fk_allocation_floor FOREIGN KEY (floor_id) REFERENCES floors(id)
) ENGINE=InnoDB;

-- ============================================================
-- 9. AGREEMENT PARKING (চুক্তিতে পার্কিং)
-- ============================================================
CREATE TABLE agreement_parking (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    agreement_id    BIGINT          NOT NULL,
    parking_id      BIGINT          NOT NULL,
    monthly_rent    DECIMAL(12,2)   NOT NULL DEFAULT 0,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_agrpark_agreement FOREIGN KEY (agreement_id) REFERENCES lease_agreements(id) ON DELETE CASCADE,
    CONSTRAINT fk_agrpark_parking FOREIGN KEY (parking_id) REFERENCES parking_spaces(id)
) ENGINE=InnoDB;

-- ============================================================
-- 10. AGREEMENT DOCUMENTS (চুক্তির ডকুমেন্ট)
-- ============================================================
CREATE TABLE agreement_documents (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    agreement_id    BIGINT          NOT NULL,
    document_type   VARCHAR(50)     NOT NULL COMMENT 'AGREEMENT, ANNEXURE, APPROVAL, OTHER',
    document_name   VARCHAR(255)    NOT NULL,
    file_path       VARCHAR(500)    NOT NULL,
    uploaded_at     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    uploaded_by     BIGINT,

    CONSTRAINT fk_agrdoc_agreement FOREIGN KEY (agreement_id) REFERENCES lease_agreements(id) ON DELETE CASCADE,
    CONSTRAINT fk_agrdoc_uploaded_by FOREIGN KEY (uploaded_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- ============================================================
-- 11. ADVANCE DEPOSITS (অ্যাডভান্স / সিকিউরিটি ডিপোজিট)
-- ============================================================
CREATE TABLE advance_deposits (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    agreement_id    BIGINT          NOT NULL,
    deposit_type    VARCHAR(30)     NOT NULL COMMENT 'SECURITY_DEPOSIT, ADVANCE_PAYMENT',
    original_amount DECIMAL(12,2)   NOT NULL,
    remaining_amount DECIMAL(12,2)  NOT NULL,
    received_date   DATE,
    status          VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE, ADJUSTED, REFUNDED',
    notes           TEXT,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_advance_agreement FOREIGN KEY (agreement_id) REFERENCES lease_agreements(id)
) ENGINE=InnoDB;

-- ============================================================
-- 12. RENT BILLS (মাসিক ভাড়ার বিল)
-- ============================================================
CREATE TABLE rent_bills (
    id                  BIGINT          AUTO_INCREMENT PRIMARY KEY,
    bill_number         VARCHAR(100)    NOT NULL UNIQUE,
    agreement_id        BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    billing_month       DATE            NOT NULL COMMENT 'First day of month (e.g. 2026-03-01)',
    billing_days        INT             NOT NULL DEFAULT 30 COMMENT 'Days billed (for prorated)',
    total_days_in_month INT             NOT NULL DEFAULT 30,

    -- Rent Components
    base_rent           DECIMAL(12,2)   NOT NULL DEFAULT 0,
    service_charge      DECIMAL(12,2)   NOT NULL DEFAULT 0,
    parking_charge      DECIMAL(12,2)   NOT NULL DEFAULT 0,

    -- Additions & Deductions
    penalty_amount      DECIMAL(12,2)   NOT NULL DEFAULT 0,
    tax_adjustment      DECIMAL(12,2)   NOT NULL DEFAULT 0 COMMENT 'Deducted if tenant submits challan',
    waiver_amount       DECIMAL(12,2)   NOT NULL DEFAULT 0,

    -- Totals
    total_payable       DECIMAL(12,2)   NOT NULL DEFAULT 0 COMMENT 'base + svc + parking + penalty - tax - waiver',
    paid_amount         DECIMAL(12,2)   NOT NULL DEFAULT 0,
    outstanding_amount  DECIMAL(12,2)   NOT NULL DEFAULT 0 COMMENT 'total_payable - paid_amount',

    due_date            DATE,
    status              VARCHAR(20)     NOT NULL DEFAULT 'GENERATED' COMMENT 'GENERATED, PARTIALLY_PAID, PAID, OVERDUE',

    generated_at        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    generated_by        BIGINT,

    CONSTRAINT fk_bill_agreement FOREIGN KEY (agreement_id) REFERENCES lease_agreements(id),
    CONSTRAINT fk_bill_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT fk_bill_generated_by FOREIGN KEY (generated_by) REFERENCES users(id),
    UNIQUE KEY uk_agreement_month (agreement_id, billing_month)
) ENGINE=InnoDB;

-- ============================================================
-- 13. PAYMENTS (পেমেন্ট)
-- ============================================================
CREATE TABLE payments (
    id                  BIGINT          AUTO_INCREMENT PRIMARY KEY,
    payment_number      VARCHAR(100)    NOT NULL UNIQUE COMMENT 'Auto-generated receipt number',
    tenant_id           BIGINT          NOT NULL,
    rent_bill_id        BIGINT          NULL COMMENT 'NULL if arrear/advance payment',
    arrear_id           BIGINT          NULL COMMENT 'If paying against arrear',
    advance_deposit_id  BIGINT          NULL COMMENT 'If advance adjustment',
    amount              DECIMAL(12,2)   NOT NULL,
    payment_method      VARCHAR(30)     NOT NULL COMMENT 'CASH, CHEQUE, BANK_TRANSFER, PAY_ORDER',
    cheque_number       VARCHAR(100),
    cheque_date         DATE,
    bank_name           VARCHAR(255),
    transaction_ref     VARCHAR(255),
    payment_date        DATE            NOT NULL,
    document_path       VARCHAR(500)    COMMENT 'Uploaded payment proof',
    notes               TEXT,
    status              VARCHAR(20)     NOT NULL DEFAULT 'COMPLETED' COMMENT 'COMPLETED, BOUNCED, CANCELLED',
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          BIGINT,

    CONSTRAINT fk_payment_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT fk_payment_bill FOREIGN KEY (rent_bill_id) REFERENCES rent_bills(id),
    CONSTRAINT fk_payment_advance FOREIGN KEY (advance_deposit_id) REFERENCES advance_deposits(id),
    CONSTRAINT fk_payment_created_by FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- ============================================================
-- 14. ARREARS (বকেয়া)
-- ============================================================
CREATE TABLE arrears (
    id                  BIGINT          AUTO_INCREMENT PRIMARY KEY,
    tenant_id           BIGINT          NOT NULL,
    agreement_id        BIGINT          NOT NULL,
    financial_year      INT             NOT NULL COMMENT 'e.g. 2024, 2025, 2026',
    original_amount     DECIMAL(12,2)   NOT NULL,
    outstanding_amount  DECIMAL(12,2)   NOT NULL,
    status              VARCHAR(20)     NOT NULL DEFAULT 'OUTSTANDING' COMMENT 'OUTSTANDING, PARTIALLY_PAID, CLEARED',
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_arrear_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT fk_arrear_agreement FOREIGN KEY (agreement_id) REFERENCES lease_agreements(id),
    UNIQUE KEY uk_tenant_year (tenant_id, agreement_id, financial_year)
) ENGINE=InnoDB;

-- ============================================================
-- 15. RENT WAIVERS (ভাড়া মওকুফ)
-- ============================================================
CREATE TABLE rent_waivers (
    id                  BIGINT          AUTO_INCREMENT PRIMARY KEY,
    agreement_id        BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    waiver_month        DATE            NOT NULL COMMENT 'First day of waived month',
    waiver_amount       DECIMAL(12,2)   NOT NULL,
    reason              TEXT,
    approval_doc_path   VARCHAR(500)    NOT NULL COMMENT 'Mandatory approval document',
    approved_by         VARCHAR(255),
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          BIGINT,

    CONSTRAINT fk_waiver_agreement FOREIGN KEY (agreement_id) REFERENCES lease_agreements(id),
    CONSTRAINT fk_waiver_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT fk_waiver_created_by FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- ============================================================
-- 16. UTILITY BILLS (ইউটিলিটি বিল)
-- ============================================================
CREATE TABLE utility_bills (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    tenant_id       BIGINT          NOT NULL,
    agreement_id    BIGINT          NOT NULL,
    utility_type    VARCHAR(50)     NOT NULL COMMENT 'ELECTRICITY, WATER, GAS, INTERNET',
    billing_month   DATE            NOT NULL,
    amount          DECIMAL(12,2)   NOT NULL DEFAULT 0,
    document_path   VARCHAR(500)    COMMENT 'Uploaded bill copy (optional)',
    status          VARCHAR(20)     NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING, PAID',
    notes           TEXT,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_utility_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT fk_utility_agreement FOREIGN KEY (agreement_id) REFERENCES lease_agreements(id)
) ENGINE=InnoDB;

-- ============================================================
-- 17. AUDIT LOGS (অডিট লগ)
-- ============================================================
CREATE TABLE audit_logs (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT,
    action_type     VARCHAR(50)     NOT NULL COMMENT 'CREATE, UPDATE, DELETE, LOGIN, LOGOUT',
    table_name      VARCHAR(100),
    record_id       BIGINT,
    old_values      JSON            COMMENT 'Previous values as JSON',
    new_values      JSON            COMMENT 'Updated values as JSON',
    ip_address      VARCHAR(50),
    description     VARCHAR(500),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

-- ============================================================
-- 18. SYSTEM SETTINGS (সিস্টেম সেটিংস)
-- ============================================================
CREATE TABLE system_settings (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    setting_key     VARCHAR(100)    NOT NULL UNIQUE,
    setting_value   TEXT,
    description     TEXT,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by      BIGINT,

    CONSTRAINT fk_settings_updated_by FOREIGN KEY (updated_by) REFERENCES users(id)
) ENGINE=InnoDB;


-- ============================================================
-- INDEXES (পারফরম্যান্সের জন্য)
-- ============================================================

-- Buildings
CREATE INDEX idx_buildings_status ON buildings(status);

-- Floors
CREATE INDEX idx_floors_building ON floors(building_id);
CREATE INDEX idx_floors_status ON floors(status);

-- Parking
CREATE INDEX idx_parking_building ON parking_spaces(building_id);
CREATE INDEX idx_parking_status ON parking_spaces(status);

-- Tenants
CREATE INDEX idx_tenants_status ON tenants(status);
CREATE INDEX idx_tenants_company ON tenants(company_name);

-- Agreements
CREATE INDEX idx_agreement_tenant ON lease_agreements(tenant_id);
CREATE INDEX idx_agreement_building ON lease_agreements(building_id);
CREATE INDEX idx_agreement_status ON lease_agreements(status);
CREATE INDEX idx_agreement_dates ON lease_agreements(start_date, end_date);

-- Floor Allocations
CREATE INDEX idx_allocation_agreement ON floor_allocations(agreement_id);
CREATE INDEX idx_allocation_floor ON floor_allocations(floor_id);

-- Rent Bills
CREATE INDEX idx_bill_agreement ON rent_bills(agreement_id);
CREATE INDEX idx_bill_tenant ON rent_bills(tenant_id);
CREATE INDEX idx_bill_month ON rent_bills(billing_month);
CREATE INDEX idx_bill_status ON rent_bills(status);

-- Payments
CREATE INDEX idx_payment_tenant ON payments(tenant_id);
CREATE INDEX idx_payment_date ON payments(payment_date);
CREATE INDEX idx_payment_bill ON payments(rent_bill_id);

-- Arrears
CREATE INDEX idx_arrear_tenant ON arrears(tenant_id);
CREATE INDEX idx_arrear_status ON arrears(status);
CREATE INDEX idx_arrear_year ON arrears(financial_year);

-- Audit Logs
CREATE INDEX idx_audit_user ON audit_logs(user_id);
CREATE INDEX idx_audit_date ON audit_logs(created_at);
CREATE INDEX idx_audit_table ON audit_logs(table_name, record_id);


-- ============================================================
-- DEFAULT DATA (ডিফল্ট ডাটা)
-- ============================================================

-- Default Super Admin (password: admin123 → BCrypt hash)
INSERT INTO users (username, password_hash, full_name, email, role, status)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System Administrator', 'admin@bdbl.com.bd', 'SUPER_ADMIN', 'ACTIVE');

-- Default System Settings
INSERT INTO system_settings (setting_key, setting_value, description) VALUES
('app_name', 'BDBL Rent Management System', 'Application name'),
('default_theme', 'LIGHT', 'Default theme: LIGHT or DARK'),
('bill_generation_day', '1', 'Day of month for auto bill generation'),
('agreement_expiry_alert_days', '90,60,30', 'Days before expiry to show alerts'),
('financial_year_start_month', '7', 'July = start of Bangladesh financial year');
