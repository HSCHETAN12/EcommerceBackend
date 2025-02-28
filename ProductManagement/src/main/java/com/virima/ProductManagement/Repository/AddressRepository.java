package com.virima.ProductManagement.Repository;

import com.virima.ProductManagement.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
