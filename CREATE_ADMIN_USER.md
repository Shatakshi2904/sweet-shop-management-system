# How to Create an Admin User

Since all new users are created with "USER" role by default, you need to manually create an admin user or update an existing user's role in MongoDB.

## Method 1: Using MongoDB Compass (GUI)

1. Open MongoDB Compass
2. Connect to `mongodb://localhost:27017`
3. Select the `sweetshop` database
4. Go to the `users` collection
5. Either:
   - **Update existing user**: Find a user and change `role` field to `"ADMIN"`
   - **Create new admin**: Insert a new document:
   ```json
   {
     "_id": "admin-id-here",
     "email": "admin@example.com",
     "password": "$2a$10$...", // BCrypt hashed password
     "role": "ADMIN"
   }
   ```

## Method 2: Using MongoDB Shell

```javascript
// Connect to MongoDB
use sweetshop

// Update existing user to admin
db.users.updateOne(
  { email: "your-email@example.com" },
  { $set: { role: "ADMIN" } }
)

// Or create new admin user (password: admin123)
db.users.insertOne({
  email: "admin@example.com",
  password: "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy", // "admin123"
  role: "ADMIN"
})
```

## Method 3: Using Spring Boot Application

You can add a data initializer or create an endpoint to create admin users. Here's a simple approach:

### Option A: Add to application.properties
Create a startup script that creates an admin user if it doesn't exist.

### Option B: Use MongoDB directly
The easiest way is to:
1. Register a regular user through the frontend
2. Update that user's role in MongoDB to "ADMIN"

## Quick Test Admin Credentials

After creating an admin user, you can login with:
- Email: `admin@example.com` (or whatever email you used)
- Password: The password you set (or "admin123" if using the example above)

## Verify Admin Status

After logging in as admin:
1. Check navbar - should show "Admin" badge
2. Should see "+ Add New Sweet" button
3. Should see edit/delete buttons on sweet cards
4. Should see "Restock/Edit" button on sweet cards

## Security Note

In production, you should:
- Use a more secure method to create admin users
- Implement proper admin user management
- Add admin user creation endpoint (protected by super-admin)
- Use environment variables for initial admin credentials

