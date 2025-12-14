# Troubleshooting Registration Issues

## Common Issues and Solutions

### 1. Backend Not Running
**Symptom:** Registration fails with network error or connection refused

**Solution:**
- Make sure the Spring Boot backend is running on `http://localhost:8080`
- Check if MongoDB is running and accessible
- Verify the backend started without errors

**To check:**
```bash
cd sweetshop
mvn spring-boot:run
```

### 2. MongoDB Not Running
**Symptom:** Registration fails with database connection error

**Solution:**
- Start MongoDB service
- On Windows: Check if MongoDB service is running in Services
- Verify MongoDB is accessible at `mongodb://localhost:27017`

**To check MongoDB connection:**
- Default connection string: `mongodb://localhost:27017/sweetshop`
- Make sure MongoDB is installed and running

### 3. CORS Issues
**Symptom:** Registration fails with CORS error in browser console

**Solution:**
- Backend CORS is configured for `http://localhost:3000` and `http://localhost:5173`
- Make sure frontend is running on one of these ports
- Check browser console for CORS errors

### 4. Validation Errors
**Symptom:** Registration fails with validation error message

**Common validation errors:**
- Email must be valid format
- Password must be at least 8 characters
- Email cannot be blank
- Password cannot be blank

**Solution:**
- Ensure email is in valid format (e.g., `user@example.com`)
- Ensure password is at least 8 characters long
- Check the error message displayed in the registration form

### 5. Email Already Registered
**Symptom:** "Email already registered" error

**Solution:**
- Try a different email address
- Or delete the existing user from MongoDB

### 6. Network/Connection Issues
**Symptom:** Request timeout or network error

**Solution:**
- Check if backend is accessible: Open `http://localhost:8080/api/auth/register` in browser (should show 405 Method Not Allowed, which is expected)
- Check browser console for detailed error messages
- Verify frontend proxy is configured correctly in `vite.config.js`

## Testing Registration Manually

### Using curl:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

### Using Postman or similar:
- URL: `POST http://localhost:8080/api/auth/register`
- Headers: `Content-Type: application/json`
- Body (JSON):
```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

## Checking Backend Logs

Look for errors in the backend console output:
- Database connection errors
- Validation errors
- Exception stack traces

## Frontend Debugging

1. Open browser Developer Tools (F12)
2. Check Console tab for JavaScript errors
3. Check Network tab to see the actual HTTP request/response
4. Look for:
   - Request URL
   - Request method (should be POST)
   - Request payload
   - Response status code
   - Response body

## Quick Checklist

- [ ] Backend is running on port 8080
- [ ] MongoDB is running and accessible
- [ ] Frontend is running on port 3000
- [ ] No CORS errors in browser console
- [ ] Email is in valid format
- [ ] Password is at least 8 characters
- [ ] Email is not already registered
- [ ] Network tab shows the request being sent
- [ ] Backend logs show the request being received

## Still Having Issues?

1. Check backend logs for detailed error messages
2. Check browser console for frontend errors
3. Verify all dependencies are installed (`mvn clean install` for backend, `npm install` for frontend)
4. Try restarting both backend and frontend
5. Clear browser cache and localStorage

