# BDBL Rent Management Software - Requirement Analysis & Planning

## 1. Current State Analysis

### 1.1 Information Derived from Excel Data Structure

After analyzing your provided Excel file, here's what I found:

#### 📊 Existing Locations:
1. **Head Office (HO)** - Dhaka
2. **Karwan Bazar** - Dhaka (Most data - 185+ entries)
3. **Khulna Branch**
4. **Agrabad Branch**

#### 📋 Current Data Structure (from Karwan Bazar):

**Main Information Columns:**
1. Serial Number (Sl. No.)
2. Tenant's Name & Position
3. Area (in Square Feet)
4. Rent (per sqft)
5. Monthly Rent (Taka)
6. Tenant's Length (Contract Duration)
7. Time Period
8. Controlled By (BDBL or Others)

**Financial Tracking Columns:**
- Previous Year Arrears (Accrued/Arrear 2024)
- 2025 Arrears (Accrued 2025)
- 2026 Monthly Payments (January to December)
- Total Received
- Arrears

#### 🔍 Observations:

**Positive Aspects:**
- Well-organized data structure
- Monthly payment tracking
- Arrears maintenance
- Parking and office space tracked separately

**Limitations:**
- No standard data validation
- Manual calculations prone to errors
- Time-consuming report generation
- Separate sheets for multiple locations (data duplication)
- Limited tenant/company detailed information
- No utility (electricity, gas, water) management
- No automatic alert system (contract expiry, payment due)

---

## 2. SDLC - Planning and Requirement Analysis Phase

### 2.1 Stakeholder Identification

#### Primary Stakeholders:
1. **Real Estate Department Team** (Main Users)
   - Department Head
   - Data Entry Operator
   - Account Manager

2. **IT Department** (BDBL)
   - Software Development Team
   - Database Admin
   - System Admin

3. **Finance/Accounts Department**
   - Accountant
   - Finance Manager

4. **Management/Executive**
   - CEO/GM
   - Deputy GM

#### Secondary Stakeholders:
- Branch Managers (Head Office, Karwan Bazar, Khulna, Agrabad)
- Audit Team

---

### 2.2 Requirement Gathering Methods

#### Step 1: Initial Meetings and Workshops

**To Do:**

1. **Kickoff Meeting** (1-2 hours)
   - Objective: Define project scope, goals, and constraints
   - Participants: All stakeholders
   - Discussion Topics:
     * What are the current system problems?
     * What do you expect from the new system?
     * Budget and timeline
     * Technical constraints

2. **Department-wise Workshops** (2-3 days)
   
   **Day 1: Real Estate Department**
   - Observe current workflow
   - See how Excel sheets are used
   - Identify daily challenges
   
   **Day 2: Finance Department**
   - Understand payment process
   - Reporting needs
   - Integration with accounting system
   
   **Day 3: Management**
   - Dashboard and reporting requirements
   - What information is needed for decision-making

#### Step 2: Documentation Review

**To Review:**
- Existing Excel sheets (Completed ✓)
- Sample lease agreements
- Payment vouchers/receipts
- Existing report formats
- Audit reports
- Legal documents

#### Step 3: User Observation

**Observe:**
- How data entry is done
- How reports are generated
- How payments are tracked
- Where most time is spent
- Where errors occur most frequently

#### Step 4: Questionnaire

**Questions for Real Estate Department:**

1. **Building Management:**
   - How many buildings are there? Will they increase in future?
   - How many floors in each building?
   - How many units per floor?
   - How many parking spaces? How are they allocated?

2. **Tenant/Company Management:**
   - What information do you want to keep about a tenant?
   - How to manage if they rent multiple spaces?
   - Contact person information?

3. **Rent Calculation:**
   - Sqft-based vs Unit-based - which is used when?
   - Is parking rent flat rate or calculated?
   - Is there annual rent increase?
   - How are VAT/Tax added?

4. **Utility Management:**
   - Electricity, gas, water - which to track?
   - How are meter readings taken?
   - How are bills calculated?
   - Who is responsible - BDBL or tenant?

5. **Payment Tracking:**
   - What are the payment methods? (Cash, cheque, bank transfer)
   - Is there advance payment?
   - How much security deposit?
   - Is there late payment charge?

6. **Reporting:**
   - What reports are needed?
   - How frequently? (Daily, weekly, monthly, annual)
   - For whom? (Management, audit, etc.)

---

### 2.3 Functional Requirement Document (FRD) - Initial Draft

#### Module 1: Building Management

**Sub-module 1.1: Building Master**
- Add/Edit/Delete building
- Information: Name, address, total floors, construction year, ownership
- Image and document attachments

**Sub-module 1.2: Floor Management**
- Floor number
- Total area (sqft)
- Rentable area
- Common area

**Sub-module 1.3: Unit/Space Management**
- Unit ID
- Floor number
- Area (sqft)
- Type (office, shop, warehouse, parking)
- Status (vacant, rented, under maintenance)

**Sub-module 1.4: Parking Management**
- Parking number/ID
- Type (car, bike, heavy vehicle)
- Location
- Status (vacant, occupied)

#### Module 2: Tenant/Company Management

**Information Fields:**
- Company name
- Business type
- Trade license number
- TIN number
- Address (registered and contact)
- Phone number (multiple)
- Email
- Website
- Contact person information (multiple):
  * Name
  * Designation
  * Phone
  * Email
- Bank account information
- Document attachments (Trade license, TIN, agreement)

#### Module 3: Lease Agreement Management

**Information Fields:**
- Agreement number (auto-generated)
- Tenant ID (company)
- Building and unit (multiple units can be selected)
- Agreement type (new, renewal)
- Agreement start date
- Agreement end date
- Agreement duration (months/years)

**Rent Calculation Method:**
1. **Sqft-based:**
   - Total area
   - Rent per sqft
   - Automatic calculation

2. **Unit-based:**
   - Fixed monthly rent

**Additional Charges:**
- Parking rent (separately)
- Service charge
- VAT (%)
- Other charges

**Financial Terms:**
- Security deposit
- Advance rent (how many months?)
- Payment cycle (monthly, quarterly, annual)
- Payment due date
- Late payment penalty (% or fixed amount)
- Annual rent increase (% or amount)

**Agreement Renewal:**
- Renewal notice period (days)
- Automatic alert

**Documents:**
- Agreement document upload
- Annexures
- Other attachments

#### Module 4: Utility Management

**Sub-module 4.1: Utility Master**
- Utility type (electricity, gas, water, internet)
- Unit (kilowatt, cubic meter, liter)
- Rate slabs (tiered pricing)

**Sub-module 4.2: Meter Management**
- Meter number
- Unit/space linkage
- Meter type
- Installation date
- Initial reading

**Sub-module 4.3: Reading Entry**
- Reading date
- Previous reading (auto)
- Current reading
- Consumption (auto calculated)
- Image attachment

**Sub-module 4.4: Utility Bill Generation**
- Automatic bill creation
- Apply tiered pricing
- Add fixed charges
- Calculate VAT
- Print/email bill

#### Module 5: Payment Management

**Sub-module 5.1: Bill Generation**
- Rent bill (monthly/quarterly)
- Utility bill
- Other charges
- Automatic or manual generation

**Sub-module 5.2: Payment Entry**
- Payment date
- Payment method (cash, cheque, bank transfer, online)
- Amount
- Reference number
- Bank account
- Receipt generation

**Sub-module 5.3: Arrears Management**
- Automatic arrears calculation
- Arrears history
- Penalty calculation
- Reminder email/SMS

**Sub-module 5.4: Advance and Security Deposit**
- Advance payment tracking
- Advance adjustment
- Security deposit record
- Refund processing

#### Module 6: Reporting and Dashboard

**Sub-module 6.1: Dashboard (Executive)**
- Total rentable space
- Currently rented space (%)
- Vacant space (%)
- Monthly rent income
- Arrears amount
- Upcoming contract expiry
- Top tenants
- Payment trend (chart)

**Sub-module 6.2: Reports**

1. **Tenant Reports:**
   - All tenants list
   - Active agreements
   - Expired agreements
   - Contact information
   
2. **Rent Reports:**
   - Monthly rent summary
   - Building-wise rent income
   - Tenant-wise rent
   - Year comparison

3. **Payment Reports:**
   - Payment history
   - Monthly collection
   - Payment method-wise
   - Bank account-wise

4. **Arrears Reports:**
   - Tenant-wise arrears
   - Aging analysis (30, 60, 90+ days)
   - Arrears summary
   
5. **Occupancy/Utilization Reports:**
   - Building occupancy rate
   - Floor-wise occupancy
   - Vacant unit list
   - Occupancy trend

6. **Agreement Reports:**
   - Agreement expiry report
   - Renewal pending
   - New agreements
   
7. **Utility Reports:**
   - Meter reading report
   - Consumption analysis
   - Utility bill summary
   - Tenant-wise consumption

8. **Audit Reports:**
   - Complete transaction log
   - Payment verification
   - Arrears verification

**Report Features:**
- Excel export
- PDF export
- Email schedule
- Print format

#### Module 7: User Management

**Role-Based Access Control:**
1. **Super Admin**
   - Full access
   - User create/delete
   - System configuration

2. **Manager**
   - All module access (view/edit)
   - Report access
   - Approval authority

3. **Data Entry Operator**
   - Data entry
   - Basic reports
   - No delete capability

4. **Accountant**
   - Payment module
   - Financial reports
   - Arrears management

5. **Viewer/Auditor**
   - View-only access
   - Report download

#### Module 8: Notifications and Alerts

**Automatic Notifications:**
1. Agreement expiry alerts (90, 60, 30 days before)
2. Payment due reminders
3. Overdue payment alerts
4. Utility bill generation notification
5. Meter reading reminder

**Notification Channels:**
- In-app notification
- Email
- SMS (optional)

#### Module 9: Audit Trail

**Logging:**
- Who did what when
- Data change history
- Login/logout history
- Deleted records

---

### 2.4 Non-Functional Requirements

#### 1. Performance
- Page load time: < 3 seconds
- Report generation: < 10 seconds (for 1000 records)
- Support 50+ concurrent users

#### 2. Security
- SSL certificate
- Password encryption
- Session management
- Data backup (daily)
- Disaster recovery plan

#### 3. Usability
- Bangla and English interface
- Intuitive design
- Help documentation
- Video tutorials

#### 4. Compatibility
- Web browsers: Chrome, Firefox, Edge (latest versions)
- Mobile responsive
- Desktop: Windows, Mac, Linux

#### 5. Scalability
- Ability to add more buildings in future
- Ability to add more modules
- Support data growth

#### 6. Reliability
- Uptime: 99.5%
- Backup retention: 30 days
- Auto backup

---

### 2.5 Technical Architecture Proposal

#### Frontend:
- **Framework:** React.js or Vue.js
- **UI Library:** Material-UI or Ant Design
- **Charts:** Chart.js or Recharts
- **State Management:** Redux or Vuex

#### Backend:
- **Language:** Python (Django/Flask) or Node.js (Express)
- **API:** RESTful API
- **Authentication:** JWT

#### Database:
- **Primary:** PostgreSQL (most suitable)
- **Alternative:** MySQL
- **Caching:** Redis (optional)

#### Cloud/Server:
- **BDBL Own Server** (On-Premise) or
- **Cloud:** AWS, Google Cloud, or Azure

#### Reporting:
- **Library:** ReportLab (Python), PDFKit
- **Excel:** openpyxl, pandas

---

### 2.6 Data Migration Plan

#### Phase 1: Data Cleaning
1. Consolidate all sheets from Excel
2. Identify and resolve duplicate data
3. Identify incomplete records
4. Data validation

#### Phase 2: Data Mapping
- Excel column → Database table/field mapping
- Data format standardization

#### Phase 3: Migration Script
- Create Python script
- Test migration
- Data verification

#### Phase 4: Production Migration
- Final migration
- Data verification
- Create backup

---

### 2.7 Project Timeline (Proposed)

#### Phase 1: Planning and Requirement Analysis (3 weeks)
- Week 1: Stakeholder meetings, workshops
- Week 2: Requirement gathering and documentation
- Week 3: FRD finalization, approval

#### Phase 2: Design (2 weeks)
- Week 1: Database design, API design
- Week 2: UI/UX design, prototype

#### Phase 3: Development (8 weeks)
- Week 1-2: Database setup, backend API
- Week 3-4: Frontend basic modules
- Week 5-6: Core modules (rent, payment)
- Week 7-8: Reporting, notification

#### Phase 4: Testing (2 weeks)
- Week 1: Unit testing, integration testing
- Week 2: User acceptance testing (UAT)

#### Phase 5: Deployment and Training (1 week)
- Server setup
- Data migration
- User training

**Total Time: Approximately 16 weeks (4 months)**

---

### 2.8 Risk Analysis

#### High Risk:
1. **Incomplete Requirements**
   - Impact: Project delay, rework
   - Solution: Extensive workshops, regular feedback

2. **Data Migration Complexity**
   - Impact: Data loss, incorrect data
   - Solution: Test migration, validation scripts

#### Medium Risk:
1. **Stakeholder Unavailability**
   - Solution: Dedicated project manager assignment

2. **Technical Challenges**
   - Solution: Experienced developer team

#### Low Risk:
1. **User Resistance**
   - Solution: Proper training, user-friendly interface

---

### 2.9 Budget Estimate (Preliminary)

**Development Cost:**
- Software Developers (2 × 4 months): BDT 800,000
- UI/UX Designer (1 × 1 month): BDT 80,000
- Project Manager (4 months): BDT 160,000

**Infrastructure:**
- Server (annual): BDT 150,000
- SSL Certificate: BDT 10,000

**Others:**
- Training: BDT 50,000
- Documentation: BDT 30,000

**Total Estimated Cost: BDT 12-15 Lakhs**

*(Actual cost will depend on team size, experience, and technology choices)*

---

## 3. Next Steps - Your Action Items

### 3.1 Start Immediately (Now)

1. **Organize Stakeholder Meeting**
   - Real Estate Department Head
   - Finance Department
   - IT Department Head
   - Management representative

2. **Create Initial Requirements List**
   - Print this document
   - Review each module
   - Identify what you want, what you don't want
   - Set priorities (Must Have, Should Have, Nice to Have)

3. **Collect All Existing Documents**
   - Sample agreements
   - Payment vouchers
   - Report formats
   - Documents

### 3.2 First Week

1. **Conduct Workshop**
   - 2-3 day workshop with Real Estate team
   - Current process mapping
   - Problem identification

2. **Complete Questionnaire**
   - Collect answers to all questions in this document

3. **Create Initial FRD Draft**
   - Use this document as base
   - Add your collected information

### 3.3 Second Week

1. **Create Prototype/Wireframes**
   - On paper or digital tool (Figma, Adobe XD)
   - Main screen designs

2. **Technical Specification Document**
   - Finalize technology stack
   - Architecture design

### 3.4 Third Week

1. **FRD Approval**
   - Management sign-off
   - Budget approval

2. **Development Team Finalization**
   - In-house or outsource
   - Define roles and responsibilities

---

## 4. Important Tips and Recommendations

### 4.1 Keys to Success

1. **User Involvement**
   - Stay with users from start to finish
   - Take regular feedback

2. **Follow Agile Methodology**
   - Work in small sprints
   - Demo after each sprint

3. **Documentation**
   - Write everything down
   - Include screenshots

4. **Training**
   - Allocate sufficient time
   - Provide hands-on training

### 4.2 Avoid

1. **Scope Creep** - Don't add new features every week
2. **Over-Engineering** - Choose simple solutions
3. **Ignoring Testing** - Allocate sufficient testing time

---

## 5. Checklist - Requirement Analysis Complete

Check each item when completed:

### Stakeholder Management
- [ ] All stakeholders identified
- [ ] Kickoff meeting completed
- [ ] Stakeholder approval received

### Requirement Gathering
- [ ] Workshop completed
- [ ] Document review completed
- [ ] User observation completed
- [ ] Questionnaire completed

### Documentation
- [ ] Functional Requirement Document (FRD)
- [ ] Non-functional requirements
- [ ] Technical architecture document
- [ ] Data migration plan
- [ ] Project timeline
- [ ] Budget estimate

### Approval
- [ ] Real Estate Department approval
- [ ] IT Department approval
- [ ] Finance Department approval
- [ ] Management approval
- [ ] Budget approval

### Next Phase Preparation
- [ ] Development team formed
- [ ] Prototype created
- [ ] Technology stack finalized
- [ ] Project kickoff meeting scheduled

---

## 6. Appendix

### 6.1 Sample Questionnaire Template

**Section: Building Information**
1. Total number of buildings? _______
2. Name and address of each building:
   - Building 1: __________________
   - Building 2: __________________
3. Total floors in each building: _______
4. Rentable area (total sqft): _______

**Section: Rent Calculation**
1. How is rent determined?
   - [ ] Sqft-based
   - [ ] Unit-based
   - [ ] Both
2. Is there annual rent increase? Yes/No
   If yes, how much %? _______

**Section: Payment**
1. Payment cycle:
   - [ ] Monthly
   - [ ] Quarterly
   - [ ] Annual
2. Payment methods (check all applicable):
   - [ ] Cash
   - [ ] Cheque
   - [ ] Bank transfer
   - [ ] Online
3. Is there late payment charge? Yes/No
   If yes, how much? _______

*(Add more questions as needed)*

### 6.2 Recommended Reading/Resources

1. **SDLC Tutorials**
2. **Database Design Basics**
3. **Architecture Patterns**
4. **Project Management Basics**

---

## Summary

In this document, I have provided:

1. ✅ Complete Excel data analysis
2. ✅ Detailed functional requirements (9 modules)
3. ✅ Non-functional requirements
4. ✅ Technical architecture proposal
5. ✅ Data migration plan
6. ✅ Project timeline (16 weeks)
7. ✅ Risk analysis
8. ✅ Budget estimate
9. ✅ Next steps guide
10. ✅ Checklist

**Remember:** This is a living document. As you gather more information, it will become more refined.

---

**Prepared by:** Senior Software Developer and System Designer  
**Date:** February 2026  
**Version:** 1.0

---

*This document is a complete guide for the Planning and Requirement Analysis phase of SDLC for the BDBL Real Estate Management System Development Project.*
