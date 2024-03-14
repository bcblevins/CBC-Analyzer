# CBC-Analyzer

This program takes blood values from the user and compares them to normal ranges. Results are recorded into a patient record system. The program also supports search functionalities by test type, date, and flags.

## Current Capabilities

- Analyzer
  - The user is prompted for each blood value the analyzer supports. Supported species include: canine.
  - Values are compared to normal ranges, then displayed in a table where abnormalities are highlighted and indicated as higher (+) or lower (-) than normal.
  - The tables are recorded to the patient record.  
  

- Patient record system
   - Patient information is stored in separate data files. Below the patient information is a log of all tests in that patient's history.
   - Each test entry contains the date the test was run, the type of test, flags, and test result table.
   - Flags are used to give context to test results and to allow flag-based searching.
   - New patient records can be made if the given patient ID (chart) number is not found in the records.
  

- Search
  - Patient records can be searched by test type, date, and flags.
  - Multiple search filters can be used at once.
  - Currently, the search will return any test result that matches any of the filters the user selected.

## Potential Future Capabilities

- Analyzer
  - Currently, the analyzer has hard coded reference ranges for only canine patients. I'd like the analyzer to read reference ranges from data files for multiple different species. The reference ranges could then be updated as the population normal ranges fluctuate.
  - Currently, the analyzer only supports CBC testing. I'd like to provide support for a greater number of test types, as well as combination tests (chemistry+CBC)
  

- Patient Record System
  - Search patient by name
  

- Search
  - Recall most recent test
  - Find all abnormal tests
  - 
