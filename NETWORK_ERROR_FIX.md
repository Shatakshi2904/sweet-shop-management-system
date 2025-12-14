# Fixing Network Error

## Quick Diagnosis Steps

### Step 1: Check if Backend is Running

Open a new terminal and run:
```bash
cd sweetshop
mvn spring-boot:run
```

**Look for:**
- "Started SweetshopApplication" message
- "Tomcat started on port(s): 8080"
- No error messages about MongoDB connection

### Step 2: Test Backend Directly

Open your browser and go to:
```
http://localhost:8080/api/auth/register
```

**Expected result:**
- If you see "405 Method Not Allowed" or similar → Backend is running ✅
- If you see "This site can't be reached" → Backend is NOT running ❌

### Step 3: Check MongoDB

Make sure MongoDB is running:
- **Windows:** Check Services (services.msc) for "MongoDB" service
- **Or:** Try connecting with MongoDB Compass or command line

### Step 4: Check Frontend

Make sure frontend is running:
```bash
cd frontend
npm run dev
```

Should show: "Local: http://localhost:3000"

### Step 5: Check Browser Console

1. Open browser Developer Tools (F12)
2. Go to **Console** tab
3. Look for error messages
4. Go to **Network** tab
5. Try registering again
6. Look for the `/api/auth/register` request
7. Check:
   - Request URL (should be `http://localhost:3000/api/auth/register`)
   - Status code
   - Response

## Common Network Error Causes

### 1. Backend Not Started
**Solution:** Start the backend server
```bash
cd sweetshop
mvn spring-boot:run
```

### 2. Wrong Port
**Check:** Backend should be on port 8080
- Check `application.properties` for `server.port` (if not set, default is 8080)
- Check backend console output for the port number

### 3. MongoDB Not Running
**Solution:** Start MongoDB service
- Windows: Start MongoDB service from Services
- Or run: `mongod` in a terminal

### 4. Firewall Blocking
**Solution:** 
- Check if Windows Firewall is blocking port 8080
- Temporarily disable firewall to test

### 5. Port Already in Use
**Solution:**
- Check if another application is using port 8080
- Windows: `netstat -ano | findstr :8080`
- Kill the process or change the port in `application.properties`

## Testing Backend Manually

### Using PowerShell (Windows):
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/auth/register" -Method POST -ContentType "application/json" -Body '{"email":"test@example.com","password":"password123"}'
```

### Using curl (if available):
```bash
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"email\":\"test@example.com\",\"password\":\"password123\"}"
```

## Quick Fix Checklist

- [ ] Backend is running (check terminal/console)
- [ ] Backend is accessible at http://localhost:8080
- [ ] MongoDB is running
- [ ] Frontend is running on http://localhost:3000
- [ ] No firewall blocking connections
- [ ] Port 8080 is not used by another application
- [ ] Browser console shows detailed error (check Network tab)

## Still Not Working?

1. **Check backend logs** - Look for error messages in the terminal where backend is running
2. **Check MongoDB connection** - Backend should connect to MongoDB without errors
3. **Try direct API call** - Use Postman or curl to test backend directly
4. **Restart everything** - Stop both frontend and backend, then restart
5. **Clear browser cache** - Clear cache and localStorage

## Alternative: Use Full URL

If proxy is not working, you can temporarily change the API base URL in `frontend/src/services/api.js`:

```javascript
const API_BASE_URL = 'http://localhost:8080/api'  // Instead of '/api'
```

**Note:** This will require CORS to be properly configured (which it should be).

