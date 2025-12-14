import './Navbar.css'

function Navbar({ user, isAdmin, onLogout }) {
  return (
    <nav className="navbar">
      <div className="navbar-container">
        <h1 className="navbar-brand">🍬 Sweet Shop</h1>
        <div className="navbar-right">
          <span className="navbar-user">{user}</span>
          {isAdmin && <span className="navbar-badge">Admin</span>}
          <button className="navbar-logout" onClick={onLogout}>Logout</button>
        </div>
      </div>
    </nav>
  )
}

export default Navbar

