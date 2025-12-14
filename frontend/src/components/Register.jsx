import { useState } from 'react'
import { Link } from 'react-router-dom'
import { authAPI } from '../services/api'
import './Auth.css'

function Register({ onLogin }) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')

    if (password !== confirmPassword) {
      setError('Passwords do not match')
      return
    }

    if (password.length < 8) {
      setError('Password must be at least 8 characters long')
      return
    }

    setLoading(true)

    try {
      await authAPI.register(email, password)
      
      // Auto-login after registration
      const loginResponse = await authAPI.login(email, password)
      const { email: userEmail, token } = loginResponse.data
      
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
      let errorMessage = 'Registration failed. Please try again.'
      
      if (err.networkError) {
        errorMessage = 'Cannot connect to server. Please make sure:\n1. Backend is running on http://localhost:8080\n2. MongoDB is running\n3. Check browser console for details'
      } else if (err.response?.data) {
        errorMessage = err.response.data
      } else if (err.message) {
        errorMessage = err.message
      }
      
      setError(errorMessage)
      console.error('Registration error:', err)
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
          <p>Create your account to get started.</p>
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
              placeholder="Enter your password (min 8 characters)"
              minLength="8"
            />
          </div>

          <div className="form-group">
            <label htmlFor="confirmPassword">Confirm Password</label>
            <input
              type="password"
              id="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
              placeholder="Confirm your password"
              minLength="8"
            />
          </div>

          <button type="submit" className="auth-button" disabled={loading}>
            {loading ? 'Registering...' : 'Register'}
          </button>
        </form>

        <div className="auth-footer">
          <p>Already have an account? <Link to="/login">Login here</Link></p>
        </div>
      </div>
    </div>
  )
}

export default Register

