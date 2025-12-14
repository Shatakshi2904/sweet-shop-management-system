import { useState } from 'react'
import { Link } from 'react-router-dom'
import { authAPI } from '../services/api'
import './Auth.css'

function Login({ onLogin }) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      const response = await authAPI.login(email, password)
      const { email: userEmail, token } = response.data
      
      // Extract role from token
      let role = 'USER'
      try {
        if (token && token.includes('.')) {
          const parts = token.split('.')
          if (parts.length >= 2) {
            const payload = JSON.parse(atob(parts[1]))
            role = payload.role || 'USER'
          }
        }
      } catch (e) {
        console.warn('Could not decode token, using default role:', e)
      }

      localStorage.setItem('token', token)
      localStorage.setItem('email', userEmail)
      localStorage.setItem('role', role)
      
      onLogin(userEmail, token, role)
    } catch (err) {
      let errorMessage = 'Login failed. Please check your credentials.'
      
      if (err.networkError) {
        errorMessage = 'Cannot connect to server. Please make sure:\n1. Backend is running on http://localhost:8080\n2. MongoDB is running\n3. Check browser console for details'
      } else if (err.response?.data) {
        errorMessage = err.response.data
      } else if (err.message) {
        errorMessage = err.message
      }
      
      setError(errorMessage)
      console.error('Login error:', err)
      console.error('Error details:', {
        message: err.message,
        response: err.response,
        networkError: err.networkError
      })
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h1>🍬 Sweet Shop</h1>
          <p>Welcome back! Please login to continue.</p>
        </div>
        
        <form onSubmit={handleSubmit} className="auth-form">
          {error && <div className="error-message" style={{ whiteSpace: 'pre-line' }}>{error}</div>}
          
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              placeholder="Enter your email"
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              placeholder="Enter your password"
              minLength="8"
            />
          </div>

          <button type="submit" className="auth-button" disabled={loading}>
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <div className="auth-footer">
          <p>Don't have an account? <Link to="/register">Register here</Link></p>
        </div>
      </div>
    </div>
  )
}

export default Login

