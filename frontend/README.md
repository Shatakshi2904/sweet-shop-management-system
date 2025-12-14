# Sweet Shop Frontend

A modern React-based Single Page Application (SPA) for the Sweet Shop Management System.

## Features

- 🔐 User authentication (Login/Register)
- 🍬 Beautiful dashboard displaying all sweets
- 🔍 Search and filter functionality (by name, category, price range)
- 🛒 Purchase functionality with quantity selection
- 👨‍💼 Admin panel for managing sweets (Add, Edit, Delete, Restock)
- 📱 Fully responsive design
- 🎨 Modern UI with smooth animations

## Prerequisites

- Node.js (v16 or higher)
- npm or yarn
- Backend API running on `http://localhost:8080`

## Installation

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

## Running the Application

Start the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:3000`

## Building for Production

To create a production build:
```bash
npm run build
```

To preview the production build:
```bash
npm run preview
```

## Project Structure

```
frontend/
├── src/
│   ├── components/
│   │   ├── Login.jsx          # Login component
│   │   ├── Register.jsx       # Registration component
│   │   ├── Dashboard.jsx      # Main dashboard with sweets grid
│   │   ├── SweetCard.jsx      # Individual sweet card component
│   │   ├── SweetModal.jsx     # Modal for add/edit/restock sweets
│   │   ├── Navbar.jsx         # Navigation bar
│   │   └── *.css              # Component styles
│   ├── services/
│   │   └── api.js             # API service layer
│   ├── App.jsx                # Main app component with routing
│   ├── App.css                # Global app styles
│   ├── main.jsx               # Entry point
│   └── index.css              # Global styles
├── index.html
├── package.json
└── vite.config.js            # Vite configuration
```

## API Integration

The frontend communicates with the backend API through the `api.js` service layer. All API calls include JWT authentication tokens automatically.

### API Endpoints Used:
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `GET /api/sweets` - Get all sweets
- `GET /api/sweets/search` - Search sweets
- `POST /api/sweets` - Create sweet (Admin only)
- `PUT /api/sweets/:id` - Update sweet (Admin only)
- `DELETE /api/sweets/:id` - Delete sweet (Admin only)
- `POST /api/sweets/:id/purchase` - Purchase sweet
- `POST /api/sweets/:id/restock` - Restock sweet (Admin only)

## Features in Detail

### Authentication
- Secure login and registration
- JWT token-based authentication
- Automatic token refresh handling
- Role-based access control (USER/ADMIN)

### Dashboard
- Grid layout displaying all available sweets
- Real-time updates after purchases
- Visual indicators for out-of-stock items
- Responsive grid that adapts to screen size

### Search & Filter
- Search by sweet name
- Filter by category
- Filter by price range (min/max)
- Clear filters option

### Purchase Flow
- Quantity selector for each sweet
- Purchase button (disabled when out of stock)
- Real-time stock updates
- Error handling for insufficient stock

### Admin Features
- Add new sweets with full details
- Edit existing sweets
- Delete sweets with confirmation
- Restock functionality
- Visual admin badge in navbar

## Technologies Used

- **React 18** - UI library
- **React Router DOM** - Client-side routing
- **Axios** - HTTP client
- **Vite** - Build tool and dev server
- **CSS3** - Styling with modern features (gradients, animations, flexbox, grid)

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Development Notes

- The app uses React Router for client-side routing
- JWT tokens are stored in localStorage
- API proxy is configured in `vite.config.js` for development
- All API calls include error handling
- Responsive design works on mobile, tablet, and desktop

