# CBC-Analyzer

This program takes blood values from the user and compares them to normal ranges. Results are recorded into a patient record database. The program also supports search functionalities by labTest type, date, and flags.

## Current Capabilities

- Analyzer
  - The user is prompted for each blood value the analyzer supports. Supported species include: canine.
  - Values are compared to normal ranges, then displayed in a table where abnormalities are highlighted and indicated as higher (+) or lower (-) than normal.
  - The results are recorded to the patient record database.
  

- Patient record system
   - Patient information is stored in a PostgreSQL database.
   - Each test entry contains the date the test was run, the type of test, tags, and results.
   - Tags are used to give context to test results.
   - Patients can be searched by name, or selected by chart number.
   - Patient records can be created, updated, and deleted.
  

- Search
  - Patient test records can be searched by test type, date, and flags.
  - Multiple search filters can be used at once.
