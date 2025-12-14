# Quick Start Guide - Sweet Shop Management System

## Prerequisites

1. **Java 17+** installed
2. **Maven** installed
3. **MongoDB** installed and running
4. **Node.js 16+** installed
5. **npm** installed

## Step 1: Start MongoDB

### Windows:
- Start MongoDB service from Services (services.msc)
- Or run: `mongod` in a terminal

### Verify MongoDB is running:
- Default connection: `mongodb://localhost:27017`
- Database: `sweetshop`

## Step 2: Start Backend

```bash
cd sweetshop
mvn clean install
mvn spring-boot:run
```

**Wait for:**
- "Started SweetshopApplication"
- "Tomcat started on port(s): 8080"

**Backend will be available at:** `http://localhost:8080`

## Step 3: Start Frontend

Open a **new terminal**:

```bash
cd frontend
npm install
npm run dev
```

**Frontend will be available at:** `http://localhost:3000`

## Step 4: Access the Application

1. Open browser: `http://localhost:3000`
2. Register a new account
3. Login with your credentials
4. Start using the application!

## Testing Admin Features

To test admin features, you need to manually set a user's role to "ADMIN" in MongoDB:

1. Connect to MongoDB
2. Use database: `sweetshop`
3. Collection: `users`
4. Find your user document
5. Update: `{ role: "ADMIN" }`

Or use MongoDB Compass:
- Database: `sweetshop`
- Collection: `users`
- Find your user
- Edit document: Set `role` field to `"ADMIN"`

## API Documentation

Once backend is running, access Swagger UI:
- URL: `http://localhost:8080/swagger-ui.html`

## Troubleshooting

### Backend won't start:
- Check MongoDB is running
- Check port 8080 is not in use
- Check Java version: `java -version`

### Frontend won't start:
- Check Node.js version: `node -v`
- Delete `node_modules` and run `npm install` again
- Check port 3000 is not in use

### Network errors:
- Verify backend is running on port 8080
- Verify frontend is running on port 3000
- Check browser console for errors
- Check CORS configuration

### Registration/Login fails:
- Check MongoDB connection
- Check backend logs for errors
- Verify email format is valid
- Verify password is at least 8 characters

## Default Configuration

- **Backend Port:** 8080
- **Frontend Port:** 3000
- **MongoDB Port:** 27017
- **Database Name:** sweetshop

## Features Available

### For All Users:
- Register/Login
- View all sweets
- Search sweets by name
- Filter by category
- Filter by price range
- Purchase sweets

### For Admin Users:
- All regular user features
- Add new sweets
- Edit existing sweets
- Delete sweets
- Restock sweets

## Next Steps

1. Register a user account
2. Explore the dashboard
3. Try searching and filtering
4. Make a purchase
5. (If admin) Add/edit/delete sweets

Enjoy using the Sweet Shop Management System! 🍬

