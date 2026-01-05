package com.library.pos.controllers;

import com.library.pos.dao.CategoryDAO;
import com.library.pos.models.Category;

import javax.swing.*;
import java.util.List;

/**
 * CategoryController - Business logic for Category operations
 */
public class CategoryController {
    private CategoryDAO categoryDAO;

    public CategoryController() {
        this.categoryDAO = new CategoryDAO();
    }

    /**
     * Get all categories
     */
    public List<Category> getAllCategories() {
        return categoryDAO.getAll();
    }

    /**
     * Get category by ID
     */
    public Category getCategoryById(int id) {
        return categoryDAO.getById(id);
    }

    /**
     * Create new category with validation
     */
    public boolean createCategory(String name) {
        String validationError = validateCategoryInput(name, 0);
        if (validationError != null) {
            JOptionPane.showMessageDialog(null, validationError, "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Category category = new Category();
        category.setName(name.trim());

        if (categoryDAO.create(category)) {
            JOptionPane.showMessageDialog(null, "Category created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Failed to create category.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Update existing category with validation
     */
    public boolean updateCategory(int id, String name) {
        String validationError = validateCategoryInput(name, id);
        if (validationError != null) {
            JOptionPane.showMessageDialog(null, validationError, "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Category category = categoryDAO.getById(id);
        if (category == null) {
            JOptionPane.showMessageDialog(null, "Category not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        category.setName(name.trim());

        if (categoryDAO.update(category)) {
            JOptionPane.showMessageDialog(null, "Category updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Failed to update category.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Delete category
     */
    public boolean deleteCategory(int id) {
        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this category?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (categoryDAO.delete(id)) {
                JOptionPane.showMessageDialog(null, "Category deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete category.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return false;
    }

    /**
     * Validate category input
     * Returns null if valid, error message if invalid
     */
    private String validateCategoryInput(String name, int excludeId) {
        if (name == null || name.trim().isEmpty()) {
            return "Category name is required.";
        }

        if (name.trim().length() < 3) {
            return "Category name must be at least 3 characters.";
        }

        if (name.trim().length() > 100) {
            return "Category name must not exceed 100 characters.";
        }

        // Check if name already exists
        if (categoryDAO.nameExists(name.trim(), excludeId)) {
            return "Category name already exists.";
        }

        return null;
    }
}
