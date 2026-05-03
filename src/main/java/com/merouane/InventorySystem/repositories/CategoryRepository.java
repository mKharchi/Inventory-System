package com.merouane.InventorySystem.repositories;

import com.merouane.InventorySystem.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
