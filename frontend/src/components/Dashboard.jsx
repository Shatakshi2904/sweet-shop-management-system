import { useState, useEffect } from 'react'
import { sweetsAPI } from '../services/api'
import SweetCard from './SweetCard'
import SweetModal from './SweetModal'
import './Dashboard.css'

function Dashboard({ isAdmin }) {
  const [sweets, setSweets] = useState([])
  const [filteredSweets, setFilteredSweets] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [searchTerm, setSearchTerm] = useState('')
  const [categoryFilter, setCategoryFilter] = useState('')
  const [priceRange, setPriceRange] = useState({ min: '', max: '' })
  const [showModal, setShowModal] = useState(false)
  const [editingSweet, setEditingSweet] = useState(null)
  const [categories, setCategories] = useState([])

  useEffect(() => {
    loadSweets()
  }, [])

  useEffect(() => {
    applyFilters()
  }, [sweets, searchTerm, categoryFilter, priceRange])

  const loadSweets = async () => {
    setLoading(true)
    setError('')
    try {
      const response = await sweetsAPI.getAll()
      const sweetsData = response.data
      setSweets(sweetsData)
      setFilteredSweets(sweetsData)
      
      // Extract unique categories
      const uniqueCategories = [...new Set(sweetsData.map(s => s.category))]
      setCategories(uniqueCategories)
    } catch (err) {
      setError(err.response?.data || 'Failed to load sweets')
    } finally {
      setLoading(false)
    }
  }

  const applyFilters = () => {
    let filtered = [...sweets]

    // Search by name
    if (searchTerm) {
      filtered = filtered.filter(sweet =>
        sweet.name.toLowerCase().includes(searchTerm.toLowerCase())
      )
    }

    // Filter by category
    if (categoryFilter) {
      filtered = filtered.filter(sweet => sweet.category === categoryFilter)
    }

    // Filter by price range
    if (priceRange.min) {
      filtered = filtered.filter(sweet => parseFloat(sweet.price) >= parseFloat(priceRange.min))
    }
    if (priceRange.max) {
      filtered = filtered.filter(sweet => parseFloat(sweet.price) <= parseFloat(priceRange.max))
    }

    setFilteredSweets(filtered)
  }

  const handleSweetUpdate = (updatedSweet) => {
    setSweets(prevSweets => {
      const updated = prevSweets.map(s => s.id === updatedSweet.id ? updatedSweet : s)
      return updated
    })
  }

  const handleAddSweet = () => {
    setEditingSweet(null)
    setShowModal(true)
  }

  const handleEditSweet = (sweet) => {
    setEditingSweet(sweet)
    setShowModal(true)
  }

  const handleDeleteSweet = async (sweet) => {
    if (!window.confirm(`Are you sure you want to delete "${sweet.name}"?`)) {
      return
    }

    try {
      await sweetsAPI.delete(sweet.id)
      setSweets(prevSweets => prevSweets.filter(s => s.id !== sweet.id))
    } catch (err) {
      const errorMsg = err.response?.data || err.message || 'Failed to delete sweet'
      alert(errorMsg)
      console.error('Delete error:', err)
    }
  }

  const handleModalClose = () => {
    setShowModal(false)
    setEditingSweet(null)
    loadSweets()
  }

  const clearFilters = () => {
    setSearchTerm('')
    setCategoryFilter('')
    setPriceRange({ min: '', max: '' })
  }

  if (loading) {
    return (
      <div className="dashboard-container">
        <div className="loading-spinner">Loading sweets... 🍬</div>
      </div>
    )
  }

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h2>Our Sweet Collection</h2>
        {isAdmin && (
          <button className="add-button" onClick={handleAddSweet}>
            + Add New Sweet
          </button>
        )}
      </div>

      {error && <div className="error-banner">{error}</div>}

      <div className="filters-section">
        <div className="search-bar">
          <input
            type="text"
            placeholder="Search by name..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
        </div>

        <div className="filters-row">
          <select
            value={categoryFilter}
            onChange={(e) => setCategoryFilter(e.target.value)}
            className="filter-select"
          >
            <option value="">All Categories</option>
            {categories.map(cat => (
              <option key={cat} value={cat}>{cat}</option>
            ))}
          </select>

          <input
            type="number"
            placeholder="Min Price"
            value={priceRange.min}
            onChange={(e) => setPriceRange({ ...priceRange, min: e.target.value })}
            className="price-input"
            step="0.01"
            min="0"
          />

          <input
            type="number"
            placeholder="Max Price"
            value={priceRange.max}
            onChange={(e) => setPriceRange({ ...priceRange, max: e.target.value })}
            className="price-input"
            step="0.01"
            min="0"
          />

          <button className="clear-filters-button" onClick={clearFilters}>
            Clear Filters
          </button>
        </div>
      </div>

      {filteredSweets.length === 0 ? (
        <div className="no-results">
          <p>No sweets found matching your criteria.</p>
          <button onClick={clearFilters} className="clear-filters-button">
            Clear Filters
          </button>
        </div>
      ) : (
        <div className="sweets-grid">
          {filteredSweets.map(sweet => (
            <SweetCard
              key={sweet.id}
              sweet={sweet}
              onUpdate={handleSweetUpdate}
              isAdmin={isAdmin}
              onEdit={handleEditSweet}
              onDelete={handleDeleteSweet}
            />
          ))}
        </div>
      )}

      {showModal && (
        <SweetModal
          sweet={editingSweet}
          onClose={handleModalClose}
          onSave={loadSweets}
        />
      )}
    </div>
  )
}

export default Dashboard

