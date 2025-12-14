import { useState, useEffect } from 'react'
import { sweetsAPI } from '../services/api'
import './SweetModal.css'

function SweetModal({ sweet, onClose, onSave }) {
  const [formData, setFormData] = useState({
    name: '',
    category: '',
    price: '',
    quantity: ''
  })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [restockMode, setRestockMode] = useState(false)
  const [restockQuantity, setRestockQuantity] = useState('')

  useEffect(() => {
    if (sweet) {
      setFormData({
        name: sweet.name || '',
        category: sweet.category || '',
        price: sweet.price || '',
        quantity: sweet.quantity || ''
      })
    } else {
      setFormData({
        name: '',
        category: '',
        price: '',
        quantity: ''
      })
    }
  }, [sweet])

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    
    // Validation
    if (!formData.name || formData.name.trim() === '') {
      setError('Name is required')
      return
    }
    if (!formData.category || formData.category.trim() === '') {
      setError('Category is required')
      return
    }
    if (!formData.price || parseFloat(formData.price) < 0) {
      setError('Price must be a positive number')
      return
    }
    if (formData.quantity === '' || parseInt(formData.quantity) < 0) {
      setError('Quantity must be a non-negative number')
      return
    }
    
    setLoading(true)

    try {
      if (sweet) {
        // Update existing sweet
        await sweetsAPI.update(sweet.id, {
          name: formData.name.trim(),
          category: formData.category.trim(),
          price: parseFloat(formData.price),
          quantity: parseInt(formData.quantity)
        })
      } else {
        // Create new sweet
        await sweetsAPI.create({
          name: formData.name.trim(),
          category: formData.category.trim(),
          price: parseFloat(formData.price),
          quantity: parseInt(formData.quantity)
        })
      }
      onSave()
      onClose()
    } catch (err) {
      const errorMsg = err.response?.data || err.message || 'Failed to save sweet'
      setError(errorMsg)
      console.error('Save sweet error:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleRestock = async (e) => {
    e.preventDefault()
    if (!restockQuantity || parseInt(restockQuantity) <= 0) {
      setError('Please enter a valid quantity')
      return
    }

    setError('')
    setLoading(true)

    try {
      await sweetsAPI.restock(sweet.id, parseInt(restockQuantity))
      onSave()
      onClose()
    } catch (err) {
      setError(err.response?.data || 'Failed to restock')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>{sweet ? (restockMode ? 'Restock Sweet' : 'Edit Sweet') : 'Add New Sweet'}</h2>
          <button className="modal-close" onClick={onClose}>×</button>
        </div>

        {error && <div className="error-message">{error}</div>}

        {sweet && !restockMode && (
          <div className="restock-toggle">
            <button
              className="toggle-button"
              onClick={() => setRestockMode(true)}
            >
              Switch to Restock Mode
            </button>
          </div>
        )}

        {restockMode && sweet ? (
          <form onSubmit={handleRestock} className="modal-form">
            <div className="form-group">
              <label>Sweet Name</label>
              <div className="readonly-field">{sweet.name}</div>
            </div>
            <div className="form-group">
              <label>Current Quantity</label>
              <div className="readonly-field">{sweet.quantity || 0}</div>
            </div>
            <div className="form-group">
              <label htmlFor="restockQuantity">Quantity to Add</label>
              <input
                type="number"
                id="restockQuantity"
                value={restockQuantity}
                onChange={(e) => setRestockQuantity(e.target.value)}
                required
                min="1"
                placeholder="Enter quantity to add"
              />
            </div>
            <div className="modal-actions">
              <button type="button" className="cancel-button" onClick={() => setRestockMode(false)}>
                Back to Edit
              </button>
              <button type="submit" className="submit-button" disabled={loading}>
                {loading ? 'Restocking...' : 'Restock'}
              </button>
            </div>
          </form>
        ) : (
          <form onSubmit={handleSubmit} className="modal-form">
            <div className="form-group">
              <label htmlFor="name">Sweet Name *</label>
              <input
                type="text"
                id="name"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                required
                placeholder="e.g., Chocolate Bar"
              />
            </div>

            <div className="form-group">
              <label htmlFor="category">Category *</label>
              <input
                type="text"
                id="category"
                value={formData.category}
                onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                required
                placeholder="e.g., Candy, Chocolate, Cookie"
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="price">Price ($) *</label>
                <input
                  type="number"
                  id="price"
                  value={formData.price}
                  onChange={(e) => setFormData({ ...formData, price: e.target.value })}
                  required
                  min="0"
                  step="0.01"
                  placeholder="0.00"
                />
              </div>

              <div className="form-group">
                <label htmlFor="quantity">Quantity *</label>
                <input
                  type="number"
                  id="quantity"
                  value={formData.quantity}
                  onChange={(e) => setFormData({ ...formData, quantity: e.target.value })}
                  required
                  min="0"
                  placeholder="0"
                />
              </div>
            </div>

            <div className="modal-actions">
              <button type="button" className="cancel-button" onClick={onClose}>
                Cancel
              </button>
              <button type="submit" className="submit-button" disabled={loading}>
                {loading ? 'Saving...' : sweet ? 'Update' : 'Create'}
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  )
}

export default SweetModal

