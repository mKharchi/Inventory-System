package com.merouane.InventorySystem.repositories;

import com.merouane.InventorySystem.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
