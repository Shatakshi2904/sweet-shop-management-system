Sweet Shop Management System

Table of Contents 1. Project Overview 2. Features 3. Technology Stack 4.
Prerequisites 5. Setup Instructions 6. API Documentation 7. Test Report
8. Screenshots 9. My AI Usage 10. Project Structure 11. Deployment 12.
Contributing

Project Overview

The Sweet Shop Management System is a full-stack web application
designed to manage an online sweet shop inventory. Built following
Test-Driven Development (TDD) principles, this application provides a
robust RESTful API backend and a modern, responsive frontend interface.

The system allows users to browse, search, and purchase sweets, while
administrators have additional capabilities to manage the inventory,
including adding, updating, deleting, and restocking items. The
application implements JWT-based authentication to secure API endpoints
and provides role-based access control (USER/ADMIN).

Key Highlights - Full-stack application with separated frontend and
backend - JWT-based authentication and authorization - Role-based access
control (USER/ADMIN) - Comprehensive test coverage following TDD
principles - RESTful API design - Modern, responsive UI/UX - Real-time
inventory management - Search and filter functionality

Features

For All Users - User Registration and Login: Secure authentication
system with JWT tokens - Browse Sweets: View all available sweets in an
attractive grid layout - Search and Filter:  - Search sweets by name  -
Filter by category  - Filter by price range (min/max) - Purchase Sweets:
Purchase sweets with quantity selection - Real-time Updates: Stock
quantities update immediately after purchases

For Admin Users - All Regular User Features: Full access to all user
functionalities - Add New Sweets: Create new sweet entries with name,
category, price, and initial quantity - Edit Sweets: Update existing
sweet details - Delete Sweets: Remove sweets from inventory (with
confirmation) - Restock Inventory: Add more quantity to existing sweets

Technology Stack

Backend - Framework: Spring Boot 3.5.8 - Language: Java 17 - Build Tool:
Maven - Database: MongoDB - Security: Spring Security with JWT -
Testing: JUnit 5, Mockito, Spring Boot Test - API Documentation:
Swagger/OpenAPI

Frontend - Framework: React 18 - Build Tool: Vite - Routing: React
Router DOM - HTTP Client: Axios - Styling: CSS3 (with modern features)

Development Tools - Version Control: Git - Package Manager: npm
(frontend), Maven (backend) - Database: MongoDB Community Edition

Prerequisites

Before setting up the project, ensure you have the following installed:

1\. Java Development Kit (JDK) 17 or higher  - Download from:
https://adoptium.net/  - Verify installation: java -version

2\. Apache Maven 3.6+  - Download from:
https://maven.apache.org/download.cgi  - Verify installation: mvn
-version

3\. MongoDB 5.0+  - Download from:
https://www.mongodb.com/try/download/community  - Verify installation:
mongod \--version  - MongoDB should be running on localhost:27017

4\. Node.js 16+ and npm  - Download from: https://nodejs.org/  - Verify
installation: node -v and npm -v

5\. Git (for version control)  - Download from:
https://git-scm.com/downloads  - Verify installation: git \--version

Setup Instructions

Step 1: Clone the Repository

git clone \<repository-url\> cd sweetshop

Step 2: Start MongoDB

Windows: - Start MongoDB service from Services (services.msc) - Or run:
mongod in a terminal

macOS/Linux: If installed via Homebrew (macOS): brew services start
mongodb-community

Or run directly: mongod

Verify MongoDB is Running: - Default connection:
mongodb://localhost:27017 - Database name: sweetshop (will be created
automatically)

Step 3: Configure Backend

1\. Navigate to the backend directory: cd sweetshop

2\. Update src/main/resources/application.properties if needed:  -
MongoDB URI: spring.data.mongodb.uri=mongodb://localhost:27017/sweetshop
 - Server port: server.port=8080 (default)  - JWT secret: Already
configured (change in production)

3\. Build the project: mvn clean install

4\. Run the backend: mvn spring-boot:run

Wait for the following messages: - \"Started SweetshopApplication\" -
\"Tomcat started on port(s): 8080\"

Backend will be available at: http://localhost:8080

API Documentation (Swagger UI): http://localhost:8080/swagger-ui.html

Step 4: Configure Frontend

1\. Open a new terminal and navigate to the frontend directory: cd
frontend

2\. Install dependencies: npm install

3\. Start the development server: npm run dev

Frontend will be available at: http://localhost:5173 (or the port shown
in terminal)

Step 5: Access the Application

1\. Open your browser and navigate to: http://localhost:5173 2. Register
a new account:  - Click \"Register\"  - Enter a valid email and password
(minimum 8 characters)  - Submit the form 3. Login with your credentials
4. Start using the application!

Step 6: Create an Admin User (Optional)

To test admin features, you need to manually set a user\'s role to
\"ADMIN\" in MongoDB:

Using MongoDB Compass: 1. Open MongoDB Compass 2. Connect to:
mongodb://localhost:27017 3. Select database: sweetshop 4. Open
collection: users 5. Find your user document 6. Edit the document and
set role field to \"ADMIN\" 7. Save the changes

Using MongoDB Shell: mongosh use sweetshop db.users.updateOne( { email:
\"your-email@example.com\" }, { \$set: { role: \"ADMIN\" } } )

API Documentation

Authentication Endpoints

POST /api/auth/register Register a new user.

Request Body: { \"email\": \"user@example.com\", \"password\":
\"password123\" }

Response: 201 Created { \"id\": \"user-id\", \"email\":
\"user@example.com\", \"role\": \"USER\" }

POST /api/auth/login Login and receive JWT token.

Request Body: { \"email\": \"user@example.com\", \"password\":
\"password123\" }

Response: 200 OK { \"email\": \"user@example.com\", \"token\":
\"jwt-token-here\" }

Sweet Management Endpoints (Protected)

GET /api/sweets Get all available sweets.

Headers: Authorization: Bearer \<token\>

Response: 200 OK \[ { \"id\": \"sweet-id\", \"name\": \"Chocolate Bar\",
\"category\": \"Candy\", \"price\": 5.99, \"quantity\": 100 } \]

GET /api/sweets/search Search sweets with filters.

Query Parameters: - name (optional): Search by name - category
(optional): Filter by category - minPrice (optional): Minimum price -
maxPrice (optional): Maximum price

Example:
/api/sweets/search?name=chocolate&category=Candy&minPrice=1&maxPrice=10

POST /api/sweets Create a new sweet (Admin only).

Request Body: { \"name\": \"New Sweet\", \"category\": \"Candy\",
\"price\": 3.99, \"quantity\": 50 }

PUT /api/sweets/:id Update a sweet (Admin only).

Request Body: { \"name\": \"Updated Sweet\", \"category\": \"Candy\",
\"price\": 4.99, \"quantity\": 75 }

DELETE /api/sweets/:id Delete a sweet (Admin only).

Response: 204 No Content

POST /api/sweets/:id/purchase Purchase a sweet (decreases quantity).

Query Parameters: - quantity: Number of items to purchase

Example: /api/sweets/sweet-id/purchase?quantity=5

POST /api/sweets/:id/restock Restock a sweet (Admin only, increases
quantity).

Query Parameters: - quantity: Number of items to add

Example: /api/sweets/sweet-id/restock?quantity=20

Interactive API Documentation

Once the backend is running, you can access the Swagger UI at: URL:
http://localhost:8080/swagger-ui.html

This provides an interactive interface to test all API endpoints.

Test Report

Test Execution Summary

The project follows Test-Driven Development (TDD) principles with
comprehensive test coverage.

Last Test Run Results: Tests run: 27 Failures: 0 Errors: 0 Skipped: 0
BUILD SUCCESS

Test Breakdown

Backend Tests

1\. AuthControllerTest (4 tests)  - User registration with valid data  -
Registration validation (bad request for invalid data)  - Login with
valid credentials  - Login rejection for invalid credentials

2\. AuthServiceTest (2 tests)  - User registration service logic  - User
login service logic

3\. SweetControllerTest (7 tests)  - Get all sweets  - Create sweet
(Admin only)  - Search sweets with filters  - Purchase sweet  - Update
sweet (Admin only)  - Delete sweet (Admin only)  - Restock sweet (Admin
only)

4\. SweetServiceTest (13 tests)  - Create sweet  - Get all sweets  -
Search sweets by name  - Purchase sweet with sufficient stock  -
Purchase sweet with insufficient stock (exception)  - Purchase sweet
with non-existent sweet (exception)  - Purchase sweet with invalid
quantity (exception)  - Restock sweet  - Restock sweet with null
quantity  - Update sweet  - Update sweet with non-existent ID
(exception)  - Delete sweet with valid ID  - Delete sweet with invalid
ID (exception)

5\. SweetshopApplicationTests (1 test)  - Application context loads
successfully

Running Tests

Backend Tests: cd sweetshop mvn test

Generate Test Report: mvn surefire-report:report

The test report will be available at: target/site/surefire-report.html

Test Coverage

The test suite covers: - All API endpoints - Service layer business
logic - Exception handling - Validation logic - Security
configurations - Application context loading

Screenshots

Application Screenshots

Note: Please add screenshots of your application here. Suggested
screenshots:

1\. Login Page  - Screenshot showing the login interface

2\. Registration Page  - Screenshot showing the registration form

3\. Dashboard (User View)  - Screenshot showing the sweets grid for
regular users  - Shows search and filter options

4\. Dashboard (Admin View)  - Screenshot showing admin controls (Add,
Edit, Delete buttons)  - Shows restock functionality

5\. Sweet Details Modal  - Screenshot showing the modal for
adding/editing sweets

6\. Purchase Flow  - Screenshot showing the purchase confirmation

7\. Search and Filter  - Screenshot showing filtered results

To add screenshots: 1. Take screenshots of your running application 2.
Save them in a screenshots/ directory 3. Update this section with image
references

My AI Usage

AI Tools Used

During the development of this project, I utilized the following AI
tools:

1\. Cursor AI (Auto)  - Primary AI coding assistant integrated into the
development environment  - Used for code generation, debugging, and
refactoring

2\. GitHub Copilot (if applicable)  - Used for code suggestions and
autocompletion

3\. ChatGPT/Claude (if applicable)  - Used for architectural decisions
and problem-solving

How AI Was Used

1\. Code Generation and Boilerplate - Initial Project Setup: Used AI to
generate Spring Boot project structure, including controllers, services,
repositories, and DTOs - Frontend Components: Generated React component
boilerplate for Login, Register, Dashboard, and SweetCard components -
API Service Layer: Created the axios-based API service layer with proper
error handling

2\. Test Writing - Unit Tests: Used AI to generate comprehensive unit
tests for controllers and services - Test Structure: AI helped structure
test cases following AAA pattern (Arrange-Act-Assert) - Mock Setup:
Generated Mockito mocks and stubs for service layer testing - Edge
Cases: AI suggested edge cases and exception scenarios to test

3\. Debugging and Problem Solving - Test Failures: When tests were
failing, AI helped identify issues:  - Package declaration mismatches  -
Security configuration problems in test contexts  - Mockito matcher
usage errors - ApplicationContext Errors: AI assisted in resolving
Spring Boot test context loading issues by suggesting proper test
configuration exclusions

4\. Code Refactoring - Security Configuration: AI suggested improvements
to exclude security components from test contexts - Code Cleanup: Used
AI to identify and remove unused imports and variables - Best Practices:
AI provided suggestions for following Spring Boot and React best
practices

5\. Documentation - API Documentation: AI helped structure API endpoint
documentation - README Generation: AI assisted in creating comprehensive
README sections - Code Comments: Generated meaningful comments for
complex logic

6\. Architecture Decisions - Security Implementation: Discussed JWT
implementation approach with AI - Database Design: AI provided insights
on MongoDB schema design - Frontend State Management: Discussed state
management patterns for React

Reflection on AI Impact

Positive Impacts: 1. Accelerated Development: AI significantly reduced
the time needed for boilerplate code generation, allowing focus on
business logic 2. Learning Tool: AI explanations helped understand
Spring Boot security configurations and React patterns 3. Error
Resolution: Quick identification and resolution of compilation and
runtime errors 4. Test Coverage: AI suggestions helped achieve
comprehensive test coverage 5. Code Quality: AI suggestions improved
code structure and adherence to best practices

Challenges and Learnings: 1. Over-reliance Risk: Initially, there was a
tendency to accept AI suggestions without understanding. Learned to
review and understand all generated code 2. Context Understanding: AI
sometimes missed project-specific context, requiring manual adjustments
3. Test Configuration: Security test configurations required multiple
iterations with AI to get right 4. Verification: Always needed to verify
AI-generated code against requirements and test it thoroughly

Best Practices Developed: 1. Review Before Commit: Always review
AI-generated code before committing 2. Understand, Don\'t Just Copy:
Take time to understand AI suggestions rather than blindly accepting 3.
Iterative Refinement: Use AI for initial generation, then refine based
on project needs 4. Test Everything: Even AI-generated code must be
thoroughly tested 5. Document AI Usage: Maintain transparency about AI
assistance in commit messages

Ethical Considerations: - All AI-generated code was reviewed and
modified to fit project requirements - No code was copied from other
repositories - AI was used as a tool to enhance productivity, not
replace understanding - All commits with AI assistance include co-author
attribution

AI Usage Statistics (Approximate) - Code Generation: \~40% of
boilerplate code - Test Writing: \~60% of test cases - Debugging: \~70%
of bug fixes involved AI assistance - Documentation: \~50% of
documentation structure

Conclusion

AI tools were instrumental in accelerating development while maintaining
code quality. The key was using AI as a collaborative tool rather than a
replacement for understanding. This project demonstrates that AI can
significantly enhance developer productivity when used responsibly and
transparently.

Project Structure

sweetshop/ ├── sweetshop/ \# Backend (Spring Boot) │ ├── src/ │ │ ├──
main/ │ │ │ ├── java/ │ │ │ │ └── com/sweetshop/backend/ │ │ │ │ ├──
auth/ \# Authentication module │ │ │ │ │ ├── controller/ │ │ │ │ │ ├──
dto/ │ │ │ │ │ ├── model/ │ │ │ │ │ ├── repository/ │ │ │ │ │ └──
service/ │ │ │ │ ├── sweets/ \# Sweet management module │ │ │ │ │ ├──
controller/ │ │ │ │ │ ├── exception/ │ │ │ │ │ ├── model/ │ │ │ │ │ ├──
repository/ │ │ │ │ │ └── service/ │ │ │ │ ├── config/ \# Configuration
│ │ │ │ ├── security/ \# JWT security │ │ │ │ └── common/ \# Common
utilities │ │ │ └── resources/ │ │ │ └── application.properties │ │ └──
test/ \# Test files │ │ └── java/ │ │ └── com/sweetshop/backend/ │ │ ├──
auth/ │ │ └── sweets/ │ ├── pom.xml │ └── target/ \# Build output ├──
frontend/ \# Frontend (React) │ ├── src/ │ │ ├── components/ │ │ │ ├──
Login.jsx │ │ │ ├── Register.jsx │ │ │ ├── Dashboard.jsx │ │ │ ├──
SweetCard.jsx │ │ │ ├── SweetModal.jsx │ │ │ └── Navbar.jsx │ │ ├──
services/ │ │ │ └── api.js │ │ ├── App.jsx │ │ └── main.jsx │ ├──
package.json │ └── vite.config.js ├── README.txt \# This file ├──
QUICK_START.md \# Quick start guide └── TROUBLESHOOTING.md \#
Troubleshooting guide

Deployment

Backend Deployment

Option 1: Heroku 1. Create a Procfile in the sweetshop directory: web:
java -jar target/sweetshop-0.0.1-SNAPSHOT.jar

2\. Configure MongoDB Atlas (cloud MongoDB)

3\. Update application.properties with MongoDB Atlas connection string

4\. Deploy using Heroku CLI: heroku create your-app-name git push heroku
main

Option 2: AWS/DigitalOcean 1. Build JAR file: mvn clean package

2\. Deploy JAR to server 3. Configure MongoDB (Atlas or self-hosted) 4.
Set environment variables for JWT secret

Frontend Deployment

Option 1: Vercel 1. Build the frontend: cd frontend npm run build

2\. Deploy to Vercel: npm install -g vercel vercel

3\. Update API base URL in production

Option 2: Netlify 1. Build the frontend 2. Deploy via Netlify CLI or
drag-and-drop 3. Configure environment variables

Option 3: GitHub Pages 1. Update vite.config.js with base path 2. Build
and deploy using GitHub Actions

Environment Variables

For production, configure: - JWT_SECRET: Strong secret key -
MONGODB_URI: MongoDB connection string - API_BASE_URL: Backend API URL
(for frontend)

Troubleshooting

Common Issues

Backend Issues

Problem: MongoDB connection failed - Solution: Ensure MongoDB is running
on localhost:27017 - Check: mongod \--version and verify service is
running

Problem: Port 8080 already in use - Solution: Change port in
application.properties: server.port=8081

Problem: Tests failing - Solution: Run mvn clean test to clear cached
test results - Check: Ensure MongoDB is running (some tests require it)

Problem: JWT token invalid - Solution: Check JWT secret in
application.properties - Ensure token is included in Authorization
header: Bearer \<token\>

Frontend Issues

Problem: Cannot connect to backend - Solution: Verify backend is running
on http://localhost:8080 - Check: Browser console for CORS errors -
Verify: API base URL in api.js

Problem: npm install fails - Solution: Delete node_modules and
package-lock.json, then run npm install again - Check: Node.js version
(should be 16+)

Problem: Blank page after login - Solution: Check browser console for
errors - Verify: JWT token is stored in localStorage - Check: API
responses in Network tab

Getting Help

1\. Check the TROUBLESHOOTING.md file for detailed solutions 2. Review
application logs (backend console output) 3. Check browser console for
frontend errors 4. Verify all prerequisites are installed correctly

Contributing

Development Workflow

1\. Create a feature branch: git checkout -b feature/your-feature-name

2\. Follow TDD:  - Write tests first (Red)  - Implement functionality
(Green)  - Refactor if needed (Refactor)

3\. Commit with clear messages: git commit -m \"feat: Add new feature
description\"

4\. Include AI co-author if AI was used: git commit -m \"feat: Implement
feature

Used AI assistant for initial implementation.

Co-authored-by: AI Tool Name \<AI@users.noreply.github.com\>\"

5\. Push and create pull request

Code Style

\- Follow Java naming conventions for backend - Follow React/JavaScript
conventions for frontend - Write meaningful comments - Keep functions
small and focused - Write tests for all new features

License

This project is developed as part of a TDD Kata exercise.

Contact

For questions or issues, please open an issue in the repository.

Acknowledgments

\- Spring Boot team for the excellent framework - React team for the
powerful UI library - MongoDB for the flexible database solution - All
open-source contributors whose libraries made this project possible

Last Updated: December 2024 Version: 0.0.1-SNAPSHOT
