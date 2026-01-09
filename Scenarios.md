# Test Automation Scenarios

---

## Scenario 1: P&C Claim Intake – Create Case linked to Asset and Contract, attach evidence

Summary:
Create a new claim Case for a policyholder, link the insured vehicle (Asset) and the policy (Contract), and upload claim evidence (Files).

Preconditions:
1. Account "Maria Alvarez Household" is created.
2. Contact "Maria Alvarez" is created and linked to account.
3. Asset "2019 Subaru Outback" is created and linked to account and contact.
4. Contract is created and linked.

Steps:
1. Go to Accounts and open “Maria Alvarez Household.”
2. On the Related tab, confirm Contact “Maria Alvarez” Asset “2019 Subaru Outback” and related Contract exist.
3. From the Account’s Cases related list, click New.
4. Populate Case fields:
   - Contact Name: Maria Alvarez
   - Subject: Fender-bender claim on Outback
   - Description: Minor rear-end collision on I-35; bumper damage; no injuries.
   - Type: Mechanical
   - Case Origin: Phone
   - Reason: Other
   - Status: New
   - Asset: 2019 Subaru Outback
   - Contract: 00000100
   - Priority: Medium
5. Save the Case and note the Case Number.
6. In the Case record (Related tab), under Notes & Attachments or Files, click Add Files and upload:
   - “Police Report—Incident 2025-00123.pdf”
7. Verify:
   - Case shows correct Account, Contact, Asset, and Contract in Highlights panel/related lists.
   - Files are visible under Files related list.
   - Case feed reflects creation and file uploads.

---

## Scenario 2: Investment Management Service Case – Activities and Case updates

Summary:
Service an investment client inquiry by creating a Case, then adding a Task and adding an Event.

Preconditions:
1. Account "Aurora Capital Institutional" is created.
2. Contact "David Lin" is created and linked to account.
3. Asset "Aurora Income Fund" is created and linked to account and contact.
4. Contract is created and linked.

Steps:
1. Go to Accounts and open “Aurora Capital Institutional.”
2. Confirm related Contact “David Lin.”
3. From Cases related list > New, enter:
   - Contact Name: David Lin
   - Subject: Dividend reinvestment plan (DRIP) change request
   - Description: Client requests to switch to full dividend reinvestment for Aurora Income Fund holdings.
   - Type: Question
   - Case Origin: Phone
   - Reason: Other
   - Status: New
   - Priority: Low
   - Contract: 00000101
4. Save the Case.
5. In the Case record (Activity/Related tab):
   - Click New Task:
     - Subject: Send Letter
     - Status: Not Started
     - Priority: Normal
     - Related To (What): [This Case]
   - Save
   - Click New Event:
     - Subject: Meeting
     - Related To (What): [This Case]
     - Location: Conference Room A
   - Save
6. Update Case:
   - Status: Working
7. Save the Case.
8. Verify:
   - Task and Event appear under Activity related list.
   - Case status reflect changes.

---

## Scenario 3: Prospect Phone Intake – Create Account and Contact during Case creation and log the call

Summary:
Capture a new inbound phone call from a prospective policyholder, create a new Account and Contact inline from the Case creation form, and log the call on the Case.

Steps:
1. Navigate to the Cases tab and click New.
2. In the New Case form, enter:
   - Case Origin: Phone
   - Subject: Homeowners quote request
   - Description: Caller requests homeowners coverage options for a single-family residence at the listed address; seeking a premium estimate.
   - Status: New
   - Priority: Medium
3. Create the new Account inline:
   - In the Account Name lookup field, click New (or the + icon).
   - Enter:
     - Account Name: Harrison & Cole Household
     - Phone: (919) 555-0940
   - Save to return to the Case form (the Account should now be populated).
4. Create the new Contact inline:
   - In the Contact Name lookup field, click New.
   - Enter:
     - First Name: Jordan
     - Last Name: Cole
     - Account Name: Harrison & Cole Household (pre-populated)
     - Email: jordan.cole@example.com
     - Phone: (919) 555-0941
   - Save to return to the Case form (the Contact should now be populated).
5. Review the Case fields (Account and Contact should be filled), then click Save.
6. Go to created Account record, log the phone call:
   - Click Log a Call (Activity composer).
   - Subject: Initial quote discussion
   - Comments: Verified property details (year built 2012, 2,100 sqft) and discussed coverage limits and deductibles. Next step: prepare preliminary quote.
   - Related To: [This Case] (default)
7. Save the call log record.
8. Verify:
   - Case shows Account = Harrison & Cole Household and Contact = Jordan Cole.
   - Account’s Related tab lists the new Contact and the Case.
   - Activity related list shows the logged call.
