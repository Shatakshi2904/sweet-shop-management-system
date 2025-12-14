import { useState } from 'react'
import { sweetsAPI } from '../services/api'
import './SweetCard.css'

function SweetCard({ sweet, onUpdate, isAdmin, onEdit, onDelete }) {
  const [purchasing, setPurchasing] = useState(false)
  const [purchaseQuantity, setPurchaseQuantity] = useState(1)
  const [error, setError] = useState('')

  const handlePurchase = async () => {
    if (purchaseQuantity <= 0) {
      setError('Quantity must be greater than 0')
      return
    }
    
    if (sweet.quantity == null || purchaseQuantity > sweet.quantity) {
      setError('Insufficient stock available')
      return
    }

    setPurchasing(true)
    setError('')

    try {
      const response = await sweetsAPI.purchase(sweet.id, purchaseQuantity)
      onUpdate(response.data)
      setPurchaseQuantity(1)
      setError('') // Clear any previous errors on success
    } catch (err) {
      const errorMsg = err.response?.data || err.message || 'Purchase failed'
      setError(errorMsg)
      console.error('Purchase error:', err)
    } finally {
      setPurchasing(false)
    }
  }

  const isOutOfStock = sweet.quantity === 0 || sweet.quantity === null

  return (
    <div className={`sweet-card ${isOutOfStock ? 'out-of-stock' : ''}`}>
      <div className="sweet-card-header">
        <h3 className="sweet-name">{sweet.name}</h3>
        {isAdmin && (
          <div className="sweet-actions">
            <button className="icon-button edit-button" onClick={() => onEdit(sweet)} title="Edit">
              ✏️
            </button>
            <button className="icon-button delete-button" onClick={() => onDelete(sweet)} title="Delete">
              🗑️
            </button>
          </div>
        )}
      </div>

      <div className="sweet-category">{sweet.category}</div>

      <div className="sweet-details">
        <div className="sweet-price">${parseFloat(sweet.price).toFixed(2)}</div>
        <div className={`sweet-quantity ${isOutOfStock ? 'out-of-stock-badge' : ''}`}>
          {isOutOfStock ? 'Out of Stock' : `${sweet.quantity} available`}
        </div>
      </div>

      {!isAdmin && (
        <div className="purchase-section">
          {error && <div className="error-message-small">{error}</div>}
          <div className="purchase-controls">
            <input
              type="number"
              min="1"
              max={sweet.quantity || 0}
              value={purchaseQuantity}
              onChange={(e) => {
                const val = parseInt(e.target.value) || 1
                setPurchaseQuantity(Math.max(1, Math.min(val, sweet.quantity || 1)))
              }}
              className="quantity-input"
              disabled={isOutOfStock || purchasing}
            />
            <button
              className="purchase-button"
              onClick={handlePurchase}
              disabled={isOutOfStock || purchasing}
            >
              {purchasing ? 'Purchasing...' : 'Purchase'}
            </button>
          </div>
        </div>
      )}

      {isAdmin && (
        <button className="restock-button" onClick={() => onEdit(sweet)}>
          Restock/Edit
        </button>
      )}
    </div>
  )
}

export default SweetCard

